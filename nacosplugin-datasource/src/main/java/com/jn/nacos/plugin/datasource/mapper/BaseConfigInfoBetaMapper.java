package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoBetaMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.google.common.collect.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public abstract class BaseConfigInfoBetaMapper extends BaseMapper implements ConfigInfoBetaMapper {
    public BaseConfigInfoBetaMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findAllConfigInfoBetaForDumpAllFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();

        RowSelection rowSelection = new RowSelection(startRow, pageSize);
        String subquery= "SELECT id FROM config_info_beta ORDER BY id ";
        subquery = getDialect().getLimitSql(subquery, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,beta_ips "
                + " FROM ( "+subquery+" )" + " g, config_info_beta t WHERE g.id = t.id";

        List<Object> paramList = Lists.newArrayList();
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);

        return new MapperResult(sql, paramList);
    }
}
