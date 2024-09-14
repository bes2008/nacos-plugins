package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.common.utils.VersionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialectManager;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;
import org.slf4j.Logger;

import java.util.List;

public abstract class BaseMapper extends AbstractMapper {
    private final String databaseName;
    protected NacosDatabaseDialect dialect;
    private IdentifierQuotedMode identifierQuotedModeInDDL;

    protected BaseMapper() {
        this.databaseName = getConfiguredDatabaseName();
        Preconditions.checkTrue(!Objs.equals(DatabaseNames.UNSUPPORTED, this.databaseName), "database {} is unsupported", this.databaseName);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseName);
        this.identifierQuotedModeInDDL = getConfiguredIdentifierQuotedMode();
    }

    /**
     * 当使用的 create-schema.sql, create-tables.sql 不是插件提供的，需要指定该配置。
     */
    private IdentifierQuotedMode getConfiguredIdentifierQuotedMode(){
        String modeString = EnvUtil.getProperty("db.sql.identifier.quoted.mode");
        IdentifierQuotedMode mode = null;
        if(Strings.isNotBlank(modeString)){
            mode = Enums.ofName(IdentifierQuotedMode.class, modeString);
        }
        if(mode==null){
            mode = this.dialect.getPluginProvidedDDLIdentifierQuotedMode();
        }
        if(mode==null){
            mode = IdentifierQuotedMode.QUOTED;
        }
        return mode;
    }

    private String getConfiguredDatabaseName(){
        String databaseName = EnvUtil.getProperty("spring.sql.init.platform");
        if(Strings.isBlank(databaseName)){
            // 这个是 nacos 中更早的配置 datasource 类型的方式
            databaseName = EnvUtil.getProperty("spring.datasource.platform");
        }
        if(Strings.isBlank(databaseName)){
            // 内嵌数据库 derby
            if (EnvUtil.getStandaloneMode()){
                databaseName = DatabaseNames.DERBY;
            }
            else{ // 默认数据库 MySQL
                databaseName = DatabaseNames.MSSQL;
            }
        }else{
            Dialect dialect = DialectRegistry.getInstance().gaussDialect(databaseName);
            if(dialect==null){
                databaseName = DatabaseNames.UNSUPPORTED;
            }
        }

        if(Strings.isBlank(databaseName)){
            databaseName = DatabaseNames.UNDEFINED;
        }

        if(Objs.equals(DatabaseNames.DERBY, databaseName) || Objs.equals(DatabaseNames.MYSQL, databaseName)){
            // 只要不是false|False 等，就是禁用，默认值为 true
            boolean builtinDatasourcePluginEnabled = !Strings.equalsIgnoreCase(EnvUtil.getProperty("spring.sql.plugin.builtin.enabled","true"),"false");
            // 自定义的插件会优先于 内置的 derby, mysql 插件
            // 放到 MapperManager 中使用了 map#putIfAbsent，所以要启用 内置的 derby, mysql，必须保证 自定义的插件名字不能是 mysql,derby
            Logger logger = Loggers.getLogger(getClass());
            logger.info("nacos builtin datasource plugin is {}", builtinDatasourcePluginEnabled?"enabled":"disabled");
            if(builtinDatasourcePluginEnabled) {
                databaseName = DatabaseNames.UNDEFINED;
                logger.info("rename the customized mapper {} to {}", Reflects.getFQNClassName(getClass()), databaseName);
            }
        }
        return databaseName;
    }


    @Override
    public String getDataSource() {
        return databaseName;
    }

    public NacosDatabaseDialect getDialect() {
        return dialect;
    }

    public String getIdentifierInDb(String identifier){
        String string;
        switch (identifierQuotedModeInDDL){
            case QUOTED:
                // 这个要求 DDL文件中，所有的表名、列名使用小写形式
                string = dialect.wrapQuote(identifier, Dialect.IdentifierCase.LOWER_CASE);
                break;
            case UNQUOTED:
                // 去掉引号
                string = dialect.unwrapQuote(identifier);
                break;
            case MIXED:
            default:
                // 按照数据库对未加引号的默认行为来处理
                string = dialect.wrapQuote(identifier, null);
                break;
        }
        return string;
    }

    @Override
    public String getFunction(String functionName) {
        return this.dialect.getFunction(functionName);
    }

    /***************************************************************************
     *  接下来是重写的父类的函数
     ***************************************************************************/

    /**
     *
     * @param columns The columns
     * @param where The where params
     */
    @Override
    public String select(List<String> columns, List<String> where) {
        StringBuilder sql = new StringBuilder();
        String method = "SELECT ";
        sql.append(method);
        for (int i = 0; i < columns.size(); i++) {
            sql.append(getIdentifierInDb(columns.get(i)));
            if (i == columns.size() - 1) {
                sql.append(" ");
            } else {
                sql.append(",");
            }
        }
        sql.append("FROM ");
        sql.append(getTableName());
        sql.append(" ");

        where = Pipeline.of(where).clearEmptys().asList();

        if (Objs.isEmpty(where)) {
            return sql.toString();
        }

        sql.append(genWhereClause(where));
        return sql.toString();
    }

    @Override
    public String insert(List<String> columns) {
        StringBuilder sql = new StringBuilder();
        String method = "INSERT INTO ";
        sql.append(method);
        sql.append(getTableName());


        int tenantIdColumnIndex = -1;
        int size = columns.size();
        sql.append("(");
        for (int i = 0; i < size; i++) {
            String columnName = getDialect().unwrapQuote(columns.get(i).split("@")[0]);
            if(Strings.equalsIgnoreCase(columnName, "tenant_id")){
                tenantIdColumnIndex = i;
            }
            sql.append(getIdentifierInDb(columnName));
            if (i != columns.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(") ");

        sql.append("VALUES");
        sql.append("(");
        for (int i = 0; i < size; i++) {
            String[] parts = columns.get(i).split("@");
            if (parts.length == 2) {
                sql.append(getFunction(parts[1]));
            } else {
                if(i==tenantIdColumnIndex){
                    sql.append(getDialect().genCastNullToDefaultExpression("?", getDefaultTenantId()));
                }else {
                    sql.append("?");
                }
            }
            if (i != columns.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    @Override
    public String update(List<String> columns, List<String> where) {
        StringBuilder sql = new StringBuilder();
        String method = "UPDATE ";
        sql.append(method);
        sql.append(getTableName()).append(" ").append("SET ");

        for (int i = 0; i < columns.size(); i++) {
            String[] parts = columns.get(i).split("@");
            String column = getDialect().unwrapQuote(parts[0]);
            if (parts.length == 2) {
                sql.append(getIdentifierInDb(column)).append(" = ").append(getFunction(parts[1]));
            } else {
                sql.append(getIdentifierInDb(column)).append(" = ").append("?");
            }
            if (i != columns.size() - 1) {
                sql.append(",");
            }
        }

        where = Pipeline.of(where).clearEmptys().asList();
        if (Objs.isEmpty(where)) {
            return sql.toString();
        }

        sql.append(" ");
        sql.append(genWhereClause(where));

        return sql.toString();
    }


    @Override
    public String delete(List<String> where) {
        StringBuilder sql = new StringBuilder();
        String method = "DELETE ";
        sql.append(method).append("FROM ").append(getTableName()).append(" ");
        where = Pipeline.of(where).clearEmptys().asList();
        if (Objs.isEmpty(where)) {
            return sql.toString();
        }
        sql.append(genWhereClause(where));
        return sql.toString();
    }


    @Override
    public String count(List<String> where) {
        StringBuilder sql = new StringBuilder();
        String method = "SELECT ";
        sql.append(method);
        sql.append("COUNT(*) FROM ");
        sql.append(getTableName());
        sql.append(" ");

        where = Pipeline.of(where).clearEmptys().asList();
        if (Objs.isEmpty(where)) {
            return sql.toString();
        }

        sql.append(genWhereClause(where));
        return sql.toString();
    }

    protected final String genWhereClause(List<String> where) {
        StringBuilder sql = new StringBuilder(" WHERE ");
        for (int i = 0; i < where.size(); i++) {
            String condition = getDialect().unwrapQuote(where.get(i));

            if(Strings.equalsIgnoreCase(condition, "tenant_id") && getDialect().isAutoCastEmptyStringToNull()){
                String castNullToDefaultExpression = getDialect().genCastNullToDefaultExpression("?", getDefaultTenantId());
                sql.append(getIdentifierInDb(condition)).append(" = ").append(castNullToDefaultExpression);
            }else{
                sql.append(getIdentifierInDb(condition)).append(" = ").append("?");
            }

            if (i != where.size() - 1) {
                sql.append(" AND ");
            }
        }
        return sql.toString();
    }

    protected boolean hasEncryptedDataKeyColumn(){
        String currentVersion = VersionUtils.version;
        String[] segments = Strings.split(currentVersion, ".");
        String nacosVersion = Strings.join(".", segments, 0, 3);
        return VersionUtils.compareVersion(nacosVersion, "2.1.0")>=0;
    }
    private static final String NAMESPACE_PUBLIC_KEY = "public";
    protected final String getDefaultTenantId(){
        if(getDialect().isAutoCastEmptyStringToNull()){
            return NAMESPACE_PUBLIC_KEY;
        }
        return NamespaceUtil.getNamespaceDefaultId();
    }
    protected final String useDefaultTenantIdIfBlank(String tenantId){
        return Strings.useValueIfBlank(tenantId, getDefaultTenantId());
    }

    protected final void useDefaultTenantIdWithWhereParameter(MapperContext context){
        Object tenantIdObj= context.getWhereParameter(FieldConstant.TENANT_ID);
        String tenantId=null;
        if(tenantIdObj!=null){
            tenantId = (String)tenantIdObj;
        }
        context.putWhereParameter(FieldConstant.TENANT_ID, useDefaultTenantIdIfBlank(tenantId));
    }
}
