package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseTenantCapacityMapper extends BaseMapper implements TenantCapacityMapper {
    public BaseTenantCapacityMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult getCapacityList4CorrectUsage(MapperContext context) {
        return null;
    }
}
