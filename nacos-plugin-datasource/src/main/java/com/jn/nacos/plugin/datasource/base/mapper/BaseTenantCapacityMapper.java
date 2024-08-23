package com.jn.nacos.plugin.datasource.base.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

public class BaseTenantCapacityMapper extends BaseMapper implements TenantCapacityMapper {
    public BaseTenantCapacityMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult getCapacityList4CorrectUsage(MapperContext context) {

        String sql = "SELECT id, tenant_id FROM tenant_capacity WHERE id>?";
        MapperResult result= new MapperResult(sql, CollectionUtils.list(context.getWhereParameter(FieldConstant.ID),
                context.getWhereParameter(FieldConstant.LIMIT_SIZE)));

        return result;
    }
}
