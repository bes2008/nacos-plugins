package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("all")
public class CommonGroupCapacityMapper extends BaseMapper implements GroupCapacityMapper {

    private static String useDefaultGroupIfEmpty(Object group) {
        return group==null || Strings.isBlank(group.toString()) ? "DEFAULT_GROUP" : group.toString();
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

    /*****************************************************************************
     *  接下来的方法，从 nacos 2.5.0 版本开始新添加的
     *****************************************************************************/


    /**
     * @since nacos 2.5.0
     * @param context sql paramMap
     * @return
     */
    public MapperResult updateUsage(MapperContext context) {
        String sql = new StringBuilder()
                .append("UPDATE group_capacity SET ")
                .append(getIdentifierInDb("usage")).append(" = (SELECT count(*) FROM config_info), ")
                .append("gmt_modified = ? ")
                .append("WHERE group_id = ?")
                .toString();
        // "UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info), gmt_modified = ? WHERE group_id = ?"

        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));

        return new MapperResult(
                sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        groupId
                       ));
    }

    public MapperResult decrementUsageByWhere(MapperContext context) {
        String sql = new StringBuilder()
                .append("UPDATE group_capacity SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" - 1 ,")
                .append("gmt_modified = ? ")
                .append("WHERE group_id = ?")
                .append(" AND ")
                .append(getIdentifierInDb("usage")).append(" > 0")
                .toString();
        // "UPDATE group_capacity SET usage = usage - 1, gmt_modified = ? WHERE group_id = ? AND usage > 0"


        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));
        return new MapperResult(
                sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        groupId));
    }
    public MapperResult incrementUsageByWhere(MapperContext context) {
        String sql = new StringBuilder()
                .append("UPDATE group_capacity SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" + 1 ,")
                .append("gmt_modified = ? ")
                .append("WHERE group_id = ?")
                .toString();

        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));

        // "UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ?"
        return new MapperResult(sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        groupId));
    }

    public MapperResult incrementUsageByWhereQuotaEqualZero(MapperContext context) {
        String sql = new StringBuilder()
                .append("UPDATE group_capacity SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" + 1 ,")
                .append("gmt_modified = ? ")
                .append("WHERE group_id = ?")
                .append(" AND ")
                .append(getIdentifierInDb("usage")).append(" < ").append(getIdentifierInDb("quota"))
                .append(" AND ")
                .append(getIdentifierInDb("quota")).append(" = 0")
                .toString();

        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));
        // "UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ? AND usage < quota AND quota = 0",
        return new MapperResult(
                sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        groupId));
    }
    public MapperResult incrementUsageByWhereQuotaNotEqualZero(MapperContext context) {
        String sql = new StringBuilder()
                .append("UPDATE group_capacity SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" + 1 ,")
                .append("gmt_modified = ? ")
                .append("WHERE group_id = ? ")
                .append(" AND ")
                .append(getIdentifierInDb("usage")).append(" < ").append(getIdentifierInDb("quota"))
                .append(" AND ")
                .append(getIdentifierInDb("quota")).append(" != 0")
                .toString();
        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));
        // "UPDATE group_capacity SET usage = usage + 1, gmt_modified = ? WHERE group_id = ? AND usage < quota AND quota != 0"
        return new MapperResult(sql,CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        groupId));
    }


    public MapperResult insertIntoSelectByWhere(MapperContext context) {
        /*
        String sql =
                "INSERT INTO group_capacity (group_id, quota, usage, max_size, max_aggr_count, max_aggr_size, gmt_create,"
                        + " gmt_modified) "
                        + NamespaceUtil.getNamespaceDefaultId() + "'";
        */

        String sql = new StringBuilder()
                .append("INSERT INTO group_capacity (")
                .append(getColumns("group_id", "quota", "usage", "max_size", "max_aggr_count", "max_aggr_size", "gmt_create", "gmt_modified"))
                .append(") SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND tenant_id = '")
                .append(NamespaceUtil.getNamespaceDefaultId())
                .append("'")
                .toString();

        String groupId = useDefaultGroupIfEmpty(context.getUpdateParameter(FieldConstant.GROUP_ID));

        List<Object> paramList = new ArrayList<>();
        paramList.add(groupId);
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

        String groupId = useDefaultGroupIfEmpty(context.getUpdateParameter(FieldConstant.GROUP_ID));

        paramList.add(groupId);
        paramList.add(context.getUpdateParameter(FieldConstant.QUOTA));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_COUNT));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_CREATE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_MODIFIED));
        StringBuilder sqlBuilder = new StringBuilder();
        String sql = sqlBuilder.append("INSERT INTO ")
                .append(getIdentifierInDb("group_capacity"))
                .append(" (")
                .append(getColumns("group_id", "quota", "usage", "max_size", "max_aggr_count", "max_aggr_size", "gmt_create", "gmt_modified"))
                .append(") ")
                .append("SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info")
                .toString();

        /*

        String sql =
                "INSERT INTO group_capacity (group_id, quota, usage, max_size, max_aggr_count, max_aggr_size,gmt_create,"
                        + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info";

         */
        return new MapperResult(sql, paramList);
    }

    public MapperResult select(MapperContext context) {
        // "SELECT id, quota, usage, max_size, max_aggr_count, max_aggr_size, group_id FROM group_capacity WHERE group_id = ?";
        String sql = select(Lists.newArrayList("id", "quota", "usage", "max_size", "max_aggr_count", "max_aggr_size", "group_id"), Lists.newArrayList("group_id"));
        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));
        return new MapperResult(sql, Collections.singletonList(groupId));
    }

    public MapperResult updateUsageByWhere(MapperContext context) {
        String sql = new StringBuilder()
                .append("UPDATE group_capacity SET ")
                .append(getIdentifierInDb("usage")).append(" = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = '")
                .append(NamespaceUtil.getNamespaceDefaultId())
                .append("'),")
                .append("gmt_modified = ?")
                .append("WHERE group_id = ?")
                .toString();
        String groupId = useDefaultGroupIfEmpty(context.getWhereParameter(FieldConstant.GROUP_ID));
        // "UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = '"
        //                        + NamespaceUtil.getNamespaceDefaultId() + "')," + " gmt_modified = ? WHERE group_id= ?",
        return new MapperResult(
                sql,
                CollectionUtils.list(context.getWhereParameter(FieldConstant.GROUP_ID),
                        context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        groupId));
    }
}
