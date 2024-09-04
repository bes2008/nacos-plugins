package com.jn.nacos.plugin.datasource.mapper;

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
     * @param columns The columns
     * @param where The where params
     * @return
     */
    @Override
    public String select(List<String> columns, List<String> where) {
        List<String> unwrapQuoteColumns = this.dialect.removeQuote(columns);
        return super.select(unwrapQuoteColumns, where);
    }

    @Override
    public String insert(List<String> columns) {
        List<String> unwrapQuoteColumns = this.dialect.removeQuote(columns);
        return super.insert(unwrapQuoteColumns);
    }

    @Override
    public String update(List<String> columns, List<String> where) {
        List<String> unwrapQuoteColumns = this.dialect.removeQuote(columns);
        return super.update(unwrapQuoteColumns,where);
    }
}
