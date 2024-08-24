package com.jn.nacos.plugin.datasource.db.opengauss.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseTenantCapacityMapper;

public class TenantCapacityMapper extends BaseTenantCapacityMapper {
    public TenantCapacityMapper() {
        super(DatabaseTypes.OPENGAUSS);
    }
}
