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
import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialectManager;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;

import java.util.List;

public abstract class BaseMapper extends AbstractMapper {
    private final String databaseName;
    protected NacosDatabaseDialect dialect;
    private IdentifierQuotedMode identifierQuotedModeInDDL;

    protected BaseMapper() {
        this.databaseName = getConfiguredDatabaseName();
        this.identifierQuotedModeInDDL = getConfiguredIdentifierQuotedMode();
        Preconditions.checkTrue(!Objs.equals(DatabaseNames.UNSUPPORTED, this.databaseName), "database {} is unsupported", this.databaseName);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseName);
    }

    private IdentifierQuotedMode getConfiguredIdentifierQuotedMode(){
        String modeString = EnvUtil.getProperty("spring.sql.identifier.quoted.mode");
        IdentifierQuotedMode mode = null;
        if(Strings.isNotBlank(modeString)){
            mode = Enums.ofName(IdentifierQuotedMode.class, modeString);
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

        // 因为 mysql 不支持 在子查询中 的limit，所以 不使用自定义的SQL，而使用官方的插件
        if(Strings.isBlank(databaseName) || Objs.equals(DatabaseNames.DERBY, databaseName) || Objs.equals(DatabaseNames.MYSQL, databaseName)){
            databaseName = DatabaseNames.UNDEFINED;
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
                // 没有加引号，则是按照数据库对未加引号的默认行为来处理
                string = dialect.wrapQuote(identifier, null);
                break;
            case MIXED:
            default:
                // 在混全模式下，则是按照 SQL-92 标准，统一使用大写形式
                string = dialect.wrapQuote(identifier, Dialect.IdentifierCase.UPPER_CASE);
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
        String currentVersion = VersionUtils.getFullClientVersion();
        return VersionUtils.compareVersion(currentVersion, "2.1.0")>=0;
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
