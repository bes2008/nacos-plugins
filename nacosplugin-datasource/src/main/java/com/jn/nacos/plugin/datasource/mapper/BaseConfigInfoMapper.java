package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.ContextConstant;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseConfigInfoMapper extends BaseMapper implements ConfigInfoMapper {

    public BaseConfigInfoMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findConfigInfoByAppFetchRows(MapperContext context) {
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize() );
        String sql = "SELECT ID,data_id,group_id,tenant_id,app_name,content FROM config_info WHERE tenant_id LIKE ? AND app_name = ?";

        sql=getDialect().getLimitSql(sql, rowSelection);
        List<Object> pagedParams = getDialect().rebuildParameters(CollectionUtils.list(tenantId, appName), rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult getTenantIdList(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize() );
        String sql = "SELECT tenant_id FROM config_info WHERE tenant_id != '" + NamespaceUtil.getNamespaceDefaultId() + "' GROUP BY tenant_id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams );
    }

    @Override
    public MapperResult getGroupIdList(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize() );
        String sql = "SELECT group_id FROM config_info WHERE tenant_id ='" + NamespaceUtil.getNamespaceDefaultId() + "' GROUP BY group_id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findAllConfigKey(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize() );
        String subquery = "SELECT id FROM config_info WHERE tenant_id LIKE ? ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery, rowSelection);
        String sql = " SELECT data_id,group_id,app_name FROM ( "+pagedSubquery+" ) g, config_info t  WHERE g.id = t.id ";

        List queryParams =  CollectionUtils.list(context.getWhereParameter(FieldConstant.TENANT_ID));
        List pagedParams = getDialect().rebuildParameters(queryParams, rowSelection);
        return new MapperResult(sql,pagedParams);
    }

    @Override
    public MapperResult findAllConfigInfoBaseFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize() );
        String subquery = "SELECT id FROM config_info ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,content,md5 " + " FROM ( "+pagedSubquery+" ) g, config_info t WHERE g.id = t.id ";
        List pagedParams = getDialect().rebuildParameters(Collections.emptyList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findAllConfigInfoFragment(MapperContext context) {
        String contextParameter = context.getContextParameter(ContextConstant.NEED_CONTENT);
        boolean needContent = contextParameter != null && Boolean.parseBoolean(contextParameter);

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize() );
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name," + (needContent ? "content," : "")
                + "md5,gmt_modified,type FROM config_info WHERE id > ? " + "ORDER BY id ASC ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List queryParams =  CollectionUtils.list(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(queryParams, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findChangeConfigFetchRows(MapperContext context) {
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);

        final Timestamp startTime = (Timestamp) context.getWhereParameter(FieldConstant.START_TIME);
        final Timestamp endTime = (Timestamp) context.getWhereParameter(FieldConstant.END_TIME);

        List<Object> paramList = new ArrayList<>();

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_modified FROM"
                + " config_info WHERE ";
        String where = " 1=1 ";

        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }

        if (!StringUtils.isBlank(tenant)) {
            where += " AND tenant_id = ? ";
            paramList.add(tenant);
        }

        if (!StringUtils.isBlank(appName)) {
            where += " AND app_name = ? ";
            paramList.add(appName);
        }
        if (startTime != null) {
            where += " AND gmt_modified >=? ";
            paramList.add(startTime);
        }
        if (endTime != null) {
            where += " AND gmt_modified <=? ";
            paramList.add(endTime);
        }
        String sql = sqlFetchRows + where;

        RowSelection rowSelection =  new RowSelection(context.getStartRow(), context.getPageSize() );
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult listGroupKeyMd5ByPageFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String subquery = "SELECT id FROM config_info ORDER BY id ";
        String pagedSubquery= getDialect().getLimitSql(subquery, rowSelection);
        String sql = " SELECT t.id,data_id,group_id,tenant_id,app_name,type,md5,gmt_modified FROM ( "+pagedSubquery+" ) g, config_info t WHERE g.id = t.id";

        List pagedParams = getDialect().rebuildParameters(Collections.emptyList(),rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);

        List<Object> paramList = new ArrayList<>();
        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
        String where = " tenant_id='" + NamespaceUtil.getNamespaceDefaultId() + "' ";
        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }
        if (!StringUtils.isBlank(tenant)) {
            where += " AND content LIKE ? ";
            paramList.add(tenant);
        }
        String sql=sqlFetchRows + where;

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList,rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = new ArrayList<>();

        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id=? ");
        paramList.add(tenantId);
        if (StringUtils.isNotBlank(dataId)) {
            where.append(" AND data_id=? ");
            paramList.add(dataId);
        }
        if (StringUtils.isNotBlank(group)) {
            where.append(" AND group_id=? ");
            paramList.add(group);
        }

        if (StringUtils.isNotBlank(appName)) {
            where.append(" AND app_name=? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND content LIKE ? ");
            paramList.add(content);
        }

        sql = sql + where;
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        sql = getDialect().getLimitSql(sql, rowSelection);

        List pagedParams = getDialect().rebuildParameters(paramList,rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigInfoBaseByGroupFetchRows(MapperContext context) {

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT id,data_id,group_id,content FROM config_info WHERE group_id=? AND tenant_id=?";

        List paramList = CollectionUtils.list(context.getWhereParameter(FieldConstant.GROUP_ID),
                context.getWhereParameter(FieldConstant.TENANT_ID));
        List pagedParams = getDialect().rebuildParameters(paramList,rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {

        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = new ArrayList<>();

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,encrypted_data_key FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id LIKE ? ");
        paramList.add(tenantId);
        if (!StringUtils.isBlank(dataId)) {
            where.append(" AND data_id LIKE ? ");
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where.append(" AND group_id LIKE ? ");
            paramList.add(group);
        }
        if (!StringUtils.isBlank(appName)) {
            where.append(" AND app_name = ? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND content LIKE ? ");
            paramList.add(content);
        }

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = sqlFetchRows + where;
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList,rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findAllConfigInfoFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String subquery = "SELECT id FROM config_info  WHERE tenant_id LIKE ? ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery, rowSelection);
        String sql = " SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5 FROM ( "+pagedSubquery+" )  g, config_info t  WHERE g.id = t.id ";
        List paramList = CollectionUtils.list(context.getWhereParameter(FieldConstant.TENANT_ID), context.getStartRow(),
                context.getPageSize());
        List pagedParams = getDialect().rebuildParameters(paramList,rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    @Override
    public MapperResult findChangeConfig(MapperContext context) {
        RowSelection rowSelection = new RowSelection(0, context.getPageSize());
        String sql = "SELECT id, data_id, group_id, tenant_id, app_name, content, gmt_modified, encrypted_data_key FROM config_info WHERE "
                        + "gmt_modified >= ? and id > ? order by id ";

        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = CollectionUtils.list(context.getWhereParameter(FieldConstant.START_TIME),
                context.getWhereParameter(FieldConstant.LAST_MAX_ID),
                context.getWhereParameter(FieldConstant.PAGE_SIZE));

        List pagedParams = getDialect().rebuildParameters(paramList,rowSelection);
        return new MapperResult(sql, pagedParams);
    }
}
