package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public abstract class BaseHistoryConfigInfoMapper extends BaseMapper implements HistoryConfigInfoMapper {
    public BaseHistoryConfigInfoMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult removeConfigHistory(MapperContext context) {

        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.LIMIT_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        String subquery = "SELECT id FROM his_config_info WHERE gmt_modified < ? ";
        String pagedSubquery = getDialect().getLimitSql(subquery, rowSelection);

        String sql = "DELETE FROM his_config_info WHERE id IN( "+ pagedSubquery+ ")";

        List paramList = CollectionUtils.list(context.getWhereParameter(FieldConstant.START_TIME));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult pageFindConfigHistoryFetchRows(MapperContext context) {

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql =
                "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,gmt_create,gmt_modified FROM his_config_info "
                        + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC  ";

        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = CollectionUtils.list(context.getWhereParameter(FieldConstant.DATA_ID),
                context.getWhereParameter(FieldConstant.GROUP_ID), context.getWhereParameter(FieldConstant.TENANT_ID));

        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findDeletedConfig(MapperContext context) {

        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.PAGE_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        String sql = "SELECT data_id, group_id, tenant_id,gmt_modified,nid FROM his_config_info WHERE op_type = 'D' AND "
                + "gmt_modified >= ? and nid > ? order by nid ";
        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList =CollectionUtils.list(context.getWhereParameter(FieldConstant.START_TIME),
                context.getWhereParameter(FieldConstant.LAST_MAX_ID));

        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }
}
