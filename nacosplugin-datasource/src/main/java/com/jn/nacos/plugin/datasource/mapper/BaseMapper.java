package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.common.utils.VersionUtils;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialectManager;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;

import java.util.List;

public abstract class BaseMapper extends AbstractMapper {
    private final String databaseId;
    protected NacosDatabaseDialect dialect;

    protected BaseMapper() {
        this.databaseId = getConfiguredDatabaseId();
        Preconditions.checkTrue(!Objs.equals(DatabaseTypes.UNSUPPORTED, this.databaseId), "database {} is unsupported", this.databaseId);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseId);
    }

    private String getConfiguredDatabaseId(){
        String databaseId = EnvUtil.getProperty("spring.sql.init.platform");
        if(Strings.isBlank(databaseId)){
            // 这个是 nacos 中更早的配置 datasource 类型的方式
            databaseId = EnvUtil.getProperty("spring.datasource.platform");
        }
        if(Strings.isBlank(databaseId)){
            // 内嵌数据库 derby
            if (EnvUtil.getStandaloneMode()){
                databaseId = DatabaseTypes.DERBY;
            }
            else{ // 默认数据库 MySQL
                databaseId = DatabaseTypes.MSSQL;
            }
        }else{
            Dialect dialect = DialectRegistry.getInstance().getDialectByName(databaseId);
            if(dialect==null){
                databaseId = DatabaseTypes.UNSUPPORTED;
            }
        }

        // 因为 mysql 不支持 在子查询中 的limit，所以 不使用自定义的SQL，而使用官方的插件
        if(Strings.isBlank(databaseId) || Objs.equals(DatabaseTypes.DERBY, databaseId) || Objs.equals(DatabaseTypes.MYSQL, databaseId)){
            databaseId=DatabaseTypes.UNDEFINED;
        }
        return databaseId;
    }


    @Override
    public String getDataSource() {
        return databaseId;
    }

    public NacosDatabaseDialect getDialect() {
        return dialect;
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
     * @param quotedColumns The columns
     * @param where The where params
     */
    @Override
    public String select(List<String> quotedColumns, List<String> where) {
        List<String> columns = this.dialect.removeQuote(quotedColumns);
        return generateSelectSql(columns, where);
    }
    private String generateSelectSql(List<String> columns, List<String> where){
        StringBuilder sql = new StringBuilder();
        String method = "SELECT ";
        sql.append(method);
        for (int i = 0; i < columns.size(); i++) {
            sql.append(columns.get(i));
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

        appendWhereClause(where, sql);
        return sql.toString();
    }

    @Override
    public String insert(List<String> quotedColumns) {
        List<String> columns = this.dialect.removeQuote(quotedColumns);
        return genInsertSql(columns);
    }
    public String genInsertSql(List<String> columns) {
        StringBuilder sql = new StringBuilder();
        String method = "INSERT INTO ";
        sql.append(method);
        sql.append(getTableName());

        int size = columns.size();
        sql.append("(");
        for (int i = 0; i < size; i++) {
            sql.append(columns.get(i).split("@")[0]);
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
                sql.append("?");
            }
            if (i != columns.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    @Override
    public String update(List<String> quotedColumns, List<String> where) {
        List<String> columns = this.dialect.removeQuote(quotedColumns);
        return genUpdateSql(columns, where);
    }

    public String genUpdateSql(List<String> columns, List<String> where) {

        StringBuilder sql = new StringBuilder();
        String method = "UPDATE ";
        sql.append(method);
        sql.append(getTableName()).append(" ").append("SET ");

        for (int i = 0; i < columns.size(); i++) {
            String[] parts = columns.get(i).split("@");
            String column = parts[0];
            if (parts.length == 2) {
                sql.append(column).append(" = ").append(getFunction(parts[1]));
            } else {
                sql.append(column).append(" = ").append("?");
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
        appendWhereClause(where, sql);

        return sql.toString();
    }


    @Override
    public String delete(List<String> where) {
        return genDeleteSql(where);
    }
    private String genDeleteSql(List<String> where) {
        StringBuilder sql = new StringBuilder();
        String method = "DELETE ";
        sql.append(method).append("FROM ").append(getTableName()).append(" ");
        where = Pipeline.of(where).clearEmptys().asList();
        if (Objs.isEmpty(where)) {
            return sql.toString();
        }
        appendWhereClause(where, sql);
        return sql.toString();
    }


    @Override
    public String count(List<String> where) {
        return genCountSql(where);
    }
    private String genCountSql(List<String> where) {
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

        appendWhereClause(where, sql);

        return sql.toString();
    }

    protected final void appendWhereClause(List<String> where, StringBuilder sql) {
        sql.append("WHERE ");
        for (int i = 0; i < where.size(); i++) {
            String condition = where.get(i);

            if(Strings.equalsIgnoreCase(condition, "tenant_id") && getDialect().isAutoCastEmptyStringToNull()){
                String castNullToDefaultExpression = getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId());
                sql.append("tenant_id = ").append(castNullToDefaultExpression);
            }else{
                sql.append(condition).append(" = ").append("?");
            }

            if (i != where.size() - 1) {
                sql.append(" AND ");
            }
        }
    }

    protected boolean hasEncryptedDataKeyColumn(){
        String currentVersion = VersionUtils.getFullClientVersion();
        return VersionUtils.compareVersion(currentVersion, "2.1.0")>=0;
    }
}
