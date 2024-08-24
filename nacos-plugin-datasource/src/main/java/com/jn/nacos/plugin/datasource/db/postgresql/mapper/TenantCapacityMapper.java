package com.jn.nacos.plugin.datasource.db.postgresql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseTenantCapacityMapper;

public class TenantCapacityMapper extends BaseTenantCapacityMapper {
    public TenantCapacityMapper() {
        super(DatabaseTypes.POSTGRESQL);
    }
}
