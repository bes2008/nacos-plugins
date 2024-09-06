package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.List;

public class CommonTenantCapacityMapper extends BaseMapper implements TenantCapacityMapper {

    @Override
    public MapperResult getCapacityList4CorrectUsage(MapperContext context) {
        int pageSize =  Integer.parseInt(context.getWhereParameter(FieldConstant.LIMIT_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        String sql = "SELECT id, tenant_id FROM tenant_capacity WHERE id>? order by id asc";
        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult incrementUsageWithDefaultQuotaLimit(MapperContext context) {
        String sql = "UPDATE tenant_capacity SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = "+ getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId())+" AND usage < ? AND quota = 0";
        return new MapperResult( sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.TENANT_ID),
                        context.getWhereParameter(FieldConstant.USAGE)));
    }

    @Override
    public MapperResult incrementUsageWithQuotaLimit(MapperContext context) {
        String sql = "UPDATE tenant_capacity SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = "+getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId())+" AND usage < quota AND quota != 0";
        return new MapperResult(sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.TENANT_ID)));
    }

    @Override
    public MapperResult incrementUsage(MapperContext context) {
        String sql = "UPDATE tenant_capacity SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = " + getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId());
        return new MapperResult(sql,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.TENANT_ID)));
    }

    @Override
    public MapperResult decrementUsage(MapperContext context) {
        String sql = "UPDATE tenant_capacity SET usage = usage - 1, gmt_modified = ? WHERE tenant_id = "+getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId())+" AND usage > 0";
        return new MapperResult(sql ,
                CollectionUtils.list(context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.TENANT_ID)));
    }

    @Override
    public MapperResult insertTenantCapacity(MapperContext context) {
        List<Object> paramList = new ArrayList<>();
        paramList.add(context.getUpdateParameter(FieldConstant.TENANT_ID));
        paramList.add(context.getUpdateParameter(FieldConstant.QUOTA));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_COUNT));
        paramList.add(context.getUpdateParameter(FieldConstant.MAX_AGGR_SIZE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_CREATE));
        paramList.add(context.getUpdateParameter(FieldConstant.GMT_MODIFIED));
        paramList.add(context.getWhereParameter(FieldConstant.TENANT_ID));

        String sql = "INSERT INTO tenant_capacity (tenant_id, quota, usage, max_size, max_aggr_count, max_aggr_size, "
                + "gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE tenant_id= " + getDialect().genCastNullToDefaultExpression("?",NamespaceUtil.getNamespaceDefaultId());
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult correctUsage(MapperContext context) {
        String sql= "UPDATE tenant_capacity SET usage = (SELECT count(*) FROM config_info WHERE tenant_id = "+getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId())+"), gmt_modified = ? WHERE tenant_id = " + getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId());
        return new MapperResult(sql,
                CollectionUtils.list(context.getWhereParameter(FieldConstant.TENANT_ID),
                        context.getUpdateParameter(FieldConstant.GMT_MODIFIED),
                        context.getWhereParameter(FieldConstant.TENANT_ID)));
    }
}
