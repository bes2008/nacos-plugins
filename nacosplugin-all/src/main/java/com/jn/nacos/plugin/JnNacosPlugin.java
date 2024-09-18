package com.jn.nacos.plugin;

import com.alibaba.nacos.plugin.datasource.MapperManager;
import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;

public class JnNacosPlugin extends AbstractInitializable {
    private JnNacosPlugin(){}

    @Override
    public void init() throws InitializationException {
        MapperManager.instance(true).loadInitial();
    }
}
