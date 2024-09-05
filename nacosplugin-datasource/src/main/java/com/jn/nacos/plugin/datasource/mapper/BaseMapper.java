package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
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

        if (CollectionUtils.isEmpty(where)) {
            return sql.toString();
        }

        appendWhereClause(where, sql);
        return sql.toString();
    }

    @Override
    public String insert(List<String> quotedColumns) {
        List<String> columns = this.dialect.removeQuote(quotedColumns);
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

        if (Objs.isEmpty(where)) {
            return sql.toString();
        }

        sql.append(" ");
        appendWhereClause(where, sql);

        return sql.toString();
    }


    @Override
    public String delete(List<String> params) {
        StringBuilder sql = new StringBuilder();
        String method = "DELETE ";
        sql.append(method).append("FROM ").append(getTableName()).append(" ").append("WHERE ");
        for (int i = 0; i < params.size(); i++) {
            sql.append(params.get(i)).append(" ").append("=").append(" ? ");
            if (i != params.size() - 1) {
                sql.append("AND ");
            }
        }

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

        if (null == where || where.size() == 0) {
            return sql.toString();
        }

        appendWhereClause(where, sql);

        return sql.toString();
    }

    @Override
    public String[] getPrimaryKeyGeneratedKeys() {
        return new String[]{"id"};
    }

    private void appendWhereClause(List<String> where, StringBuilder sql) {
        sql.append("WHERE ");
        for (int i = 0; i < where.size(); i++) {
            sql.append(where.get(i)).append(" = ").append("?");
            if (i != where.size() - 1) {
                sql.append(" AND ");
            }
        }
    }
}
