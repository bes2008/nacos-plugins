package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public abstract class BaseTenantCapacityMapper extends BaseMapper implements TenantCapacityMapper {
    public BaseTenantCapacityMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult getCapacityList4CorrectUsage(MapperContext context) {
        int pageSize =  Integer.parseInt(context.getWhereParameter(FieldConstant.LIMIT_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        String sql = "SELECT id, tenant_id FROM tenant_capacity WHERE id>? ";
        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = CollectionUtils.list(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }
}
