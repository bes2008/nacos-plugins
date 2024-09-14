package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoBetaMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonConfigInfoBetaMapper extends BaseMapper implements ConfigInfoBetaMapper {

    /**
     * 当前nacos版本，不能使用 ? 用作 limit, offset 参数
     */
    @Override
    public MapperResult findAllConfigInfoBetaForDumpAllFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();

        RowSelection rowSelection = new RowSelection(startRow, pageSize);
        String subquery = "SELECT id FROM config_info_beta ORDER BY id ";
        subquery = getDialect().getLimitSql(subquery, true, false, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,beta_ips " + (hasEncryptedDataKeyColumn() ? ", encrypted_data_key ":"")
                + " FROM ( " + subquery + " )" + " g, config_info_beta t WHERE g.id = t.id";

        List<Object> paramList = Lists.newArrayList();
        List pagedParams = getDialect().rebuildParameters(true, false, paramList, rowSelection);

        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult updateConfigInfo4BetaCas(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoBetaMapper.super.updateConfigInfo4BetaCas(context);
    }
}
