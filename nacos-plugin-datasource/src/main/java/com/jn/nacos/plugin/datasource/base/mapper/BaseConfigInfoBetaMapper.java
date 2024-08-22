package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoBetaMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseConfigInfoBetaMapper extends BaseMapper implements ConfigInfoBetaMapper {
    public BaseConfigInfoBetaMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findAllConfigInfoBetaForDumpAllFetchRows(MapperContext context) {
        return null;
    }
}
