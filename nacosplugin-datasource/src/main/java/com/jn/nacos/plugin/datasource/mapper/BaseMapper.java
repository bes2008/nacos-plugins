package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.jn.langx.util.Preconditions;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialectManager;

public abstract class BaseMapper extends AbstractMapper{
    private String databaseId;
    protected NacosDatabaseDialect dialect;

    public BaseMapper(String databaseId){
        Preconditions.checkNotEmpty(databaseId,"database id is empty");
        this.databaseId = databaseId;
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseId);
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
}
