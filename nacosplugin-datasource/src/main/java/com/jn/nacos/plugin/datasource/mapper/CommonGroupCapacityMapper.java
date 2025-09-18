package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class CommonGroupCapacityMapper extends BaseMapper implements GroupCapacityMapper {

    @Override
    public MapperResult selectGroupInfoBySize(MapperContext context) {
        RowSelection rowSelection = new RowSelection(0, context.getPageSize());
        String sql = "SELECT id, group_id FROM group_capacity WHERE id > ? order by id asc";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    /*****************************************************************************
     *  接下来的方法，从 nacos 2.5.0 版本开始新添加的
     *****************************************************************************/


    /**
     * @since nacos 2.5.0
     * @param context sql paramMap
     * @return
     */
    public MapperResult updateUsage(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info), gmt_modified = ? WHERE group_id = ?",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    public MapperResult decrementUsageByWhere(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET usage = usage - 1, gmt_modified = ? WHERE group_id = ? AND usage > 0",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }
    public MapperResult incrementUsageByWhere(MapperContext context) {
        return new MapperResult("UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ?",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    public MapperResult incrementUsageByWhereQuotaEqualZero(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ? AND usage < quota AND quota = 0",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }
    public MapperResult incrementUsageByWhereQuotaNotEqualZero(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ? AND usage < quota AND quota != 0",
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }


    public MapperResult insertIntoSelectByWhere(MapperContext context) {
        final String sql =
                "INSERT INTO group_capacity (group_id, quota, usage, max_size, max_aggr_count, max_aggr_size, gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND tenant_id = '"
                        + NamespaceUtil.getNamespaceDefaultId() + "'";
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter(FieldConstant.GROUP_ID));
        paramList.add(context.getUpdateParameter(FieldConstant.QUOTA));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_COUNT));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_CREATE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_MODIFIED));

        paramList.add(context.getWhereParameter(FieldConstant.GROUP_ID));

        return new MapperResult(sql, paramList);
    }

    public MapperResult insertIntoSelect(MapperContext context) {
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter(FieldConstant.GROUP_ID));
        paramList.add(context.getUpdateParameter(FieldConstant.QUOTA));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_COUNT));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_CREATE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_MODIFIED));

        String sql =
                "INSERT INTO group_capacity (group_id, quota, usage, max_size, max_aggr_count, max_aggr_size,gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info";
        return new MapperResult(sql, paramList);
    }

    public MapperResult select(MapperContext context) {
        String sql = "SELECT id, quota, usage, max_size, max_aggr_count, max_aggr_size, group_id FROM group_capacity "
                + "WHERE group_id = ?";
        return new MapperResult(sql, Collections.singletonList(context.getWhereParameter(FieldConstant.GROUP_ID)));
    }

    public MapperResult updateUsageByWhere(MapperContext context) {
        return new MapperResult(
                "UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = '"
                        + NamespaceUtil.getNamespaceDefaultId() + "')," + " gmt_modified = ? WHERE group_id= ?",
                CollectionUtils.list(context.getWhereParameter(FieldConstant.GROUP_ID),
                        context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.GROUP_ID)));
    }
}
