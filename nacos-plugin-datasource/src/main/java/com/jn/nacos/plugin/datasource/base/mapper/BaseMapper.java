package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.jn.langx.lifecycle.Initializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Preconditions;
import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;

public abstract class BaseMapper extends AbstractMapper implements Initializable {
    private String databaseId;
    protected DatabaseDialect databaseDialect;

    public BaseMapper(String databaseId){
        Preconditions.checkNotEmpty(databaseId,"database id is empty");
        this.databaseId = databaseId;
        this.init();
    }

    @Override
    public void init() throws InitializationException {
        databaseDialect = null;
    }

    @Override
    public String getDataSource() {
        return databaseId;
    }

    @Override
    public String getFunction(String functionName) {
        return this.databaseDialect.getFunction(functionName);
    }
}
