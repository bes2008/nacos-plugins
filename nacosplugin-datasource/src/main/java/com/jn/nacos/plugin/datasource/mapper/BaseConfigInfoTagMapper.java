package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoTagMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Collects;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public abstract class BaseConfigInfoTagMapper extends BaseMapper implements ConfigInfoTagMapper {
    public BaseConfigInfoTagMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findAllConfigInfoTagForDumpAllFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String subquery = " SELECT id FROM config_info_tag  ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,tenant_id,tag_id,app_name,content,md5,gmt_modified FROM ( " + pagedSubquery + " ) g, config_info_tag t  WHERE g.id = t.id";
        List pagedParams = getDialect().rebuildParameters(Collects.emptyArrayList(), rowSelection);

        return new MapperResult(sql, pagedParams);
    }
}
