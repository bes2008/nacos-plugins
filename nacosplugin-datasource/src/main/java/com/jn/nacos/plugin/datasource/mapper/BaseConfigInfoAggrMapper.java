package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoAggrMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public abstract class BaseConfigInfoAggrMapper extends BaseMapper implements ConfigInfoAggrMapper {
    public BaseConfigInfoAggrMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findConfigInfoAggrByPageFetchRows(MapperContext context) {
        final int startRow = context.getStartRow();
        final int pageSize = context.getPageSize();
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String groupId = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        RowSelection rowSelection = new RowSelection(startRow, pageSize);
        String sql = "SELECT data_id,group_id,tenant_id,datum_id,app_name,content FROM config_info_aggr WHERE data_id=? AND group_id=? AND tenant_id=? ORDER BY datum_id ";
        List<Object> paramList = CollectionUtils.list(dataId, groupId, tenantId);

        sql = getDialect().getLimitSql(sql, rowSelection);

        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }
}
