package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.TenantInfoMapper;

public class BaseTenantInfoMapper extends BaseMapper implements TenantInfoMapper {
    public BaseTenantInfoMapper(String databaseId) {
        super(databaseId);
    }
}
