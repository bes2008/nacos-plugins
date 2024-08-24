package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.TenantInfoMapper;

public abstract class BaseTenantInfoMapper extends BaseMapper implements TenantInfoMapper {
    public BaseTenantInfoMapper(String databaseId) {
        super(databaseId);
    }
}
