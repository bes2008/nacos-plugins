package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseGroupCapacityMapper extends BaseMapper implements GroupCapacityMapper {
    public BaseGroupCapacityMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult selectGroupInfoBySize(MapperContext context) {
        return null;
    }
}
