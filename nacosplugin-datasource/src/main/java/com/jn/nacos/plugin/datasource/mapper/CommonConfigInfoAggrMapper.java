package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoAggrMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.List;

public class CommonConfigInfoAggrMapper extends BaseMapper implements ConfigInfoAggrMapper {

    @Override
    public MapperResult findConfigInfoAggrByPageFetchRows(MapperContext context) {
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

        List pagedParams = getDialect().rebuildParameters(false, true,paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult batchRemoveAggr(MapperContext context) {
        final List<String> datumList = (List<String>) context.getWhereParameter(FieldConstant.DATUM_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String tenantTmp = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        List<Object> paramList = new ArrayList<>();
        paramList.add(dataId);
        paramList.add(group);
        paramList.add(tenantTmp);

        final StringBuilder placeholderString = new StringBuilder();
        for (int i = 0; i < datumList.size(); i++) {
            if (i != 0) {
                placeholderString.append(", ");
            }
            placeholderString.append('?');
            paramList.add(datumList.get(i));
        }

        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        String sql = delete(where) +" AND datum_id IN (" + placeholderString + ")";
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult aggrConfigInfoCount(MapperContext context) {
        final List<String> datumIds = (List<String>) context.getWhereParameter(FieldConstant.DATUM_ID);
        final Boolean isIn = (Boolean) context.getWhereParameter(FieldConstant.IS_IN);
        String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        String tenantTmp = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        List<Object> paramList = CollectionUtils.list(dataId, group, tenantTmp);

        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        StringBuilder sql = new StringBuilder(count(where)).append(" AND datum_id ").append(isIn? "": " NOT ").append(" IN ( ");
        for (int i = 0; i < datumIds.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append('?');
            paramList.add(datumIds.get(i));
        }
        sql.append(')');

        return new MapperResult(sql.toString(), paramList);
    }

    @Override
    public MapperResult findConfigInfoAggrIsOrdered(MapperContext context) {
        String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        String groupId = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        List<String> selectedColumns = Lists.newArrayList("data_id","group_id","tenant_id","datum_id","app_name","content");
        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        String sql = select(selectedColumns, where) + " ORDER BY datum_id";
        List<Object> paramList = CollectionUtils.list(dataId, groupId, tenantId);

        return new MapperResult(sql, paramList);
    }

}
