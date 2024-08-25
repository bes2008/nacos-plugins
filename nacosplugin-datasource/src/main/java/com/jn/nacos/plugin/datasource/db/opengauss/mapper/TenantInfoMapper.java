package com.jn.nacos.plugin.datasource.db.opengauss.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseTenantInfoMapper;

public class TenantInfoMapper extends BaseTenantInfoMapper {
    public TenantInfoMapper() {
        super(DatabaseTypes.OPENGAUSS);
    }
}
