package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonHistoryConfigInfoMapper extends BaseMapper implements HistoryConfigInfoMapper {

    @Override
    public MapperResult removeConfigHistory(MapperContext context) {

        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.LIMIT_SIZE).toString());

        RowSelection rowSelection = new RowSelection(0, pageSize);
        String sql = "DELETE FROM his_config_info WHERE gmt_modified < ? ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.START_TIME), pageSize);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    /**
     * 当前nacos版本，不能使用 ? 用作 limit, offset 参数
     */
    @Override
    public MapperResult pageFindConfigHistoryFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        List<String> selectedColumns = Lists.newArrayList("nid","data_id","group_id","tenant_id","app_name","src_ip","src_user","op_type","gmt_create","gmt_modified");
        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        String sql = select(selectedColumns, where)+ " ORDER BY nid DESC  ";

        sql = getDialect().getLimitSql(sql, false, false, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.DATA_ID),
                context.getWhereParameter(FieldConstant.GROUP_ID), context.getWhereParameter(FieldConstant.TENANT_ID));

        List pagedParams = getDialect().rebuildParameters(false, false,paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findDeletedConfig(MapperContext context) {

        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.PAGE_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        String sql = "SELECT data_id, group_id, tenant_id,gmt_modified,nid FROM his_config_info WHERE op_type = 'D' AND "
                + "gmt_modified >= ? and nid > ? order by nid ";
        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.START_TIME),
                context.getWhereParameter(FieldConstant.LAST_MAX_ID));

        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigHistoryFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return HistoryConfigInfoMapper.super.findConfigHistoryFetchRows(context);
    }
}
