package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoAggrMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseConfigInfoAggrMapper extends BaseMapper implements ConfigInfoAggrMapper {
    public BaseConfigInfoAggrMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findConfigInfoAggrByPageFetchRows(MapperContext context) {
        return null;
    }
}
