package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

@SuppressWarnings("all")
public abstract class BaseGroupCapacityMapper extends BaseMapper implements GroupCapacityMapper {
    protected BaseGroupCapacityMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult selectGroupInfoBySize(MapperContext context) {
        RowSelection rowSelection = new RowSelection(0, context.getPageSize());
        String sql = "SELECT id, group_id FROM group_capacity WHERE id > ? order by id asc";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }
}
