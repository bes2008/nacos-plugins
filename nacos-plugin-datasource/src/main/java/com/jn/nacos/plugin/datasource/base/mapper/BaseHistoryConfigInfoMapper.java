package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseHistoryConfigInfoMapper extends BaseMapper implements HistoryConfigInfoMapper {
    public BaseHistoryConfigInfoMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult removeConfigHistory(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult pageFindConfigHistoryFetchRows(MapperContext context) {
        return null;
    }
}
