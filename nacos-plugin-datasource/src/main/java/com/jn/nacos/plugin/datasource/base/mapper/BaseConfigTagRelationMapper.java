package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigTagsRelationMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseConfigTagRelationMapper extends BaseMapper implements ConfigTagsRelationMapper {
    public BaseConfigTagRelationMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
        return null;
    }
}
