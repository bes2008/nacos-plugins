package com.jn.nacos.plugin.datasource.db.oracle.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseTenantInfoMapper;

public class TenantInfoMapper extends BaseTenantInfoMapper {
    public TenantInfoMapper() {
        super(DatabaseTypes.ORACLE);
    }
}
