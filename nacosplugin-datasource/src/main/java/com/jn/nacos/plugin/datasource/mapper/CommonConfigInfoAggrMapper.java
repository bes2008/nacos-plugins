package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoAggrMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonConfigInfoAggrMapper extends BaseMapper implements ConfigInfoAggrMapper {

    @Override
    public MapperResult findConfigInfoAggrByPageFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        final int startRow = context.getStartRow();
        final int pageSize = context.getPageSize();
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String groupId = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        RowSelection rowSelection = new RowSelection(startRow, pageSize);
        List<String> columns = Lists.newArrayList("data_id","group_id","tenant_id","datum_id","app_name","content");
        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        String sql = select(columns, where) + " ORDER BY datum_id ";
        List<Object> paramList = Lists.newArrayList(dataId, groupId, tenantId);

        sql = getDialect().getLimitSql(sql, false, true, rowSelection);

        List pagedParams = getDialect().rebuildParameters(false, true, paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult batchRemoveAggr(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoAggrMapper.super.batchRemoveAggr(context);
    }

    @Override
    public MapperResult aggrConfigInfoCount(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoAggrMapper.super.aggrConfigInfoCount(context);
    }

    @Override
    public MapperResult findConfigInfoAggrIsOrdered(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoAggrMapper.super.findConfigInfoAggrIsOrdered(context);
    }

}
