package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.jn.langx.util.Preconditions;
import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;

public abstract class BaseMapper extends AbstractMapper{
    private String databaseId;
    protected DatabaseDialect dialect;

    public BaseMapper(String databaseId){
        Preconditions.checkNotEmpty(databaseId,"database id is empty");
        this.databaseId = databaseId;
        this.dialect = null;
    }


    @Override
    public String getDataSource() {
        return databaseId;
    }

    public DatabaseDialect getDialect() {
        return dialect;
    }

    @Override
    public String getFunction(String functionName) {
        return this.dialect.getFunction(functionName);
    }
}
