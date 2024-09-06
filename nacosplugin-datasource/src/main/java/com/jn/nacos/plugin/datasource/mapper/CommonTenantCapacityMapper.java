package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonTenantCapacityMapper extends BaseMapper implements TenantCapacityMapper {

    @Override
    public MapperResult getCapacityList4CorrectUsage(MapperContext context) {
        int pageSize =  Integer.parseInt(context.getWhereParameter(FieldConstant.LIMIT_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        String sql = "SELECT id, tenant_id FROM tenant_capacity WHERE id>? order by id asc";
        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult incrementUsageWithDefaultQuotaLimit(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return TenantCapacityMapper.super.incrementUsageWithDefaultQuotaLimit(context);
    }

    @Override
    public MapperResult incrementUsageWithQuotaLimit(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return TenantCapacityMapper.super.incrementUsageWithQuotaLimit(context);
    }

    @Override
    public MapperResult incrementUsage(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return TenantCapacityMapper.super.incrementUsage(context);
    }

    @Override
    public MapperResult decrementUsage(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return TenantCapacityMapper.super.decrementUsage(context);
    }

    @Override
    public MapperResult insertTenantCapacity(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return TenantCapacityMapper.super.insertTenantCapacity(context);
    }

    @Override
    public MapperResult correctUsage(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return TenantCapacityMapper.super.correctUsage(context);
    }
}
