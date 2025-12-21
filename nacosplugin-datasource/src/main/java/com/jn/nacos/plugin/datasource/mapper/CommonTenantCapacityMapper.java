package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.Collections;
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
        useDefaultTenantIdWithWhereParameter(context);
        String sql = new StringBuilder()
                .append("UPDATE tenant_capacity ")
                .append("SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" + 1,")
                .append("gmt_modified = ? ")
                .append(" WHERE tenant_id = ?")
                .append(" AND ")
                .append(getIdentifierInDb("usage")).append(" < ?")
                .append(" AND ")
                .append(getIdentifierInDb("quota")).append(" = 0")
                .toString();
        // "UPDATE tenant_capacity SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = ? AND usage < ? AND quota = 0"
        return new MapperResult(sql, CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"), context.getWhereParameter("tenantId"), context.getWhereParameter("usage")}));
    }

    @Override
    public MapperResult incrementUsageWithQuotaLimit(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String sql = new StringBuilder()
                .append("UPDATE tenant_capacity ")
                .append("SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" + 1,")
                .append("gmt_modified = ? ")
                .append(" WHERE tenant_id = ?")
                .append(" AND ")
                .append(getIdentifierInDb("usage")).append(" < ").append(getIdentifierInDb("quota"))
                .append(" AND ")
                .append(getIdentifierInDb("quota")).append(" != 0")
                .toString();

        // "UPDATE tenant_capacity SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = ? AND usage < quota AND quota != 0"
        return new MapperResult(sql, CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"), context.getWhereParameter("tenantId")}));
    }

    @Override
    public MapperResult incrementUsage(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String sql = new StringBuilder()
                .append("UPDATE tenant_capacity ")
                .append("SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" + 1,")
                .append("gmt_modified = ? ")
                .append("WHERE tenant_id = ?")
                .toString();
        // "UPDATE tenant_capacity SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = ?"
        return new MapperResult(sql, CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"), context.getWhereParameter("tenantId")}));
    }

    @Override
    public MapperResult decrementUsage(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String sql = new StringBuilder()
                .append("UPDATE tenant_capacity ")
                .append("SET ")
                .append(getIdentifierInDb("usage")).append(" = ").append(getIdentifierInDb("usage")).append(" - 1,")
                .append("gmt_modified = ? ")
                .append(" WHERE tenant_id = ?")
                .append(" AND ")
                .append(getIdentifierInDb("usage")).append(" > 0")
                .toString();
        // "UPDATE tenant_capacity SET usage = usage - 1, gmt_modified = ? WHERE tenant_id = ? AND usage > 0"
        return new MapperResult(sql, CollectionUtils.list(new Object[]{context.getUpdateParameter("gmtModified"), context.getWhereParameter("tenantId")}));
    }

    @Override
    public MapperResult insertTenantCapacity(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        List<Object> paramList = new ArrayList();
        paramList.add(context.getUpdateParameter("tenantId"));
        paramList.add(context.getUpdateParameter("quota"));
        paramList.add(context.getUpdateParameter("maxSize"));
        paramList.add(context.getUpdateParameter("maxAggrCount"));
        paramList.add(context.getUpdateParameter("maxAggrSize"));
        paramList.add(context.getUpdateParameter("gmtCreate"));
        paramList.add(context.getUpdateParameter("gmtModified"));
        paramList.add(context.getWhereParameter("tenantId"));

        String sql = new StringBuilder()
                .append("INSERT INTO tenant_capacity (")
                .append(getColumns("tenant_id", "quota", "usage", "max_size", "max_aggr_count", "max_aggr_size", "gmt_create", "gmt_modified"))
                .append(") SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE tenant_id=?")
                .toString();
        // "INSERT INTO tenant_capacity (tenant_id, quota, usage, max_size, max_aggr_count, max_aggr_size, gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE tenant_id=?"
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult correctUsage(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String sql = new StringBuilder()
                .append("UPDATE tenant_capacity ")
                .append("SET ")
                .append(getIdentifierInDb("usage")).append(" = (SELECT count(*) FROM config_info WHERE tenant_id = ?),")
                .append("gmt_modified = ? ")
                .append("WHERE tenant_id = ?")
                .toString();
        // "UPDATE tenant_capacity SET usage = (SELECT count(*) FROM config_info WHERE tenant_id = ?), gmt_modified = ? WHERE tenant_id = ?"
        return new MapperResult(sql, CollectionUtils.list(new Object[]{context.getWhereParameter("tenantId"), context.getUpdateParameter("gmtModified"), context.getWhereParameter("tenantId")}));
    }

    public MapperResult select(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
       // String sql = "SELECT id, quota, usage, max_size, max_aggr_count, max_aggr_size, tenant_id FROM tenant_capacity WHERE tenant_id = ?";
        String sql = new StringBuilder()
                .append("SELECT ")
                .append(getColumns("id", "quota", "usage", "max_size", "max_aggr_count", "max_aggr_size", "tenant_id"))
                .append(" FROM tenant_capacity")
                .append(" WHERE tenant_id = ?")
                .toString();
        return new MapperResult(sql, Collections.singletonList(context.getWhereParameter(FieldConstant.TENANT_ID)));
    }
}
