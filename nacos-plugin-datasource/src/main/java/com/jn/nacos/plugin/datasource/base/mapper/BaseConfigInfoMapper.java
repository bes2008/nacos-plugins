package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseConfigInfoMapper extends BaseMapper implements ConfigInfoMapper {

    public BaseConfigInfoMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findConfigInfoByAppFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult getTenantIdList(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult getGroupIdList(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findAllConfigKey(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findAllConfigInfoBaseFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findAllConfigInfoFragment(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findChangeConfigFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult listGroupKeyMd5ByPageFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findConfigInfoBaseByGroupFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
        return null;
    }

    @Override
    public MapperResult findAllConfigInfoFetchRows(MapperContext context) {
        return null;
    }
}
