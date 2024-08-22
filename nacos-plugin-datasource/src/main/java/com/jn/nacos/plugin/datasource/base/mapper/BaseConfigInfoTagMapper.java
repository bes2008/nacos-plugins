package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoTagMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseConfigInfoTagMapper extends BaseMapper implements ConfigInfoTagMapper {
    public BaseConfigInfoTagMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findAllConfigInfoTagForDumpAllFetchRows(MapperContext context) {
        return null;
    }
}
