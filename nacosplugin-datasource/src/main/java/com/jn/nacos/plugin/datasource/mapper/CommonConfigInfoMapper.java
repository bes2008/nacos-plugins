package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.ContextConstant;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommonConfigInfoMapper extends BaseMapper implements ConfigInfoMapper {



    @Override
    public MapperResult findChangeConfig(MapperContext context) {
        RowSelection rowSelection = new RowSelection(0, context.getPageSize());
        String sql = "SELECT id, data_id, group_id, tenant_id, app_name, content, gmt_modified, encrypted_data_key FROM config_info WHERE "
                + "gmt_modified >= ? and id > ? order by id ";

        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.START_TIME),
                context.getWhereParameter(FieldConstant.LAST_MAX_ID),
                context.getWhereParameter(FieldConstant.PAGE_SIZE));

        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigInfoByAppCountRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.findConfigInfoByAppCountRows(context);
    }

    @Override
    public MapperResult configInfoLikeTenantCount(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.configInfoLikeTenantCount(context);
    }

    @Override
    public MapperResult findAllConfigInfo4Export(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.findAllConfigInfo4Export(context);
    }

    @Override
    public MapperResult findConfigInfoBaseLikeCountRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.findConfigInfoBaseLikeCountRows(context);
    }

    @Override
    public MapperResult findConfigInfo4PageCountRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.findConfigInfo4PageCountRows(context);
    }

    @Override
    public MapperResult findConfigInfoLike4PageCountRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.findConfigInfoLike4PageCountRows(context);
    }

    @Override
    public MapperResult updateConfigInfoAtomicCas(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return ConfigInfoMapper.super.updateConfigInfoAtomicCas(context);
    }

    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findConfigInfoByAppFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);

        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        List paramList = Lists.newArrayList();

        String sql= "SELECT ID,data_id,group_id,tenant_id,app_name,content FROM config_info WHERE tenant_id LIKE ? AND app_name = ? order by id asc";
        paramList.add(tenantId);
        paramList.add(appName);
        sql = getDialect().getLimitSql(sql, rowSelection);
        List<Object> pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }



    @Override
    public MapperResult getTenantIdList(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT tenant_id FROM config_info WHERE tenant_id != ? GROUP BY tenant_id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(tenantId), rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    @Override
    public MapperResult getGroupIdList(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT group_id FROM config_info WHERE tenant_id = ? GROUP BY group_id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(tenantId), rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findAllConfigKey(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);
        List paramList = Lists.newArrayList();

        String subquery = "SELECT id FROM config_info WHERE tenant_id LIKE ? ORDER BY id ";
        paramList.add(tenantId);
        String pagedSubquery = getDialect().getLimitSql(subquery, true,true, rowSelection);
        String sql = " SELECT data_id,group_id,app_name FROM ( " + pagedSubquery + " ) g, config_info t  WHERE g.id = t.id ";

        List pagedParams = getDialect().rebuildParameters(true, true, paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findAllConfigInfoBaseFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String subquery = "SELECT id FROM config_info ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery,true,true, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,content,md5 " + " FROM ( " + pagedSubquery + " ) g, config_info t WHERE g.id = t.id ";
        List pagedParams = getDialect().rebuildParameters(true, true, Collects.emptyArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }



    @Override
    public MapperResult findAllConfigInfoFragment(MapperContext context) {
        String contextParameter = context.getContextParameter(ContextConstant.NEED_CONTENT);
        boolean needContent = contextParameter != null && Boolean.parseBoolean(contextParameter);

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name," + (needContent ? "content," : "")
                + "md5,gmt_modified,type "+ (hasEncryptedDataKeyColumn()? ",encrypted_data_key":"")
                +" FROM config_info WHERE id > ? " + "ORDER BY id ASC ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List queryParams = Lists.newArrayList(context.getWhereParameter(FieldConstant.ID));
        List pagedParams = getDialect().rebuildParameters(queryParams, rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    /**
     * 该方法没有被调用
     */
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

        if (!Strings.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!Strings.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }

        if (!Strings.isBlank(tenant)) {
            where += " AND tenant_id = ? ";
            paramList.add(tenant);
        }

        if (!Strings.isBlank(appName)) {
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
        String sql = sqlFetchRows + where +" order by id asc";

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult listGroupKeyMd5ByPageFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String subquery = "SELECT id FROM config_info ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery,true,true, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,tenant_id,app_name,type,md5,gmt_modified FROM ( " + pagedSubquery + " ) g, config_info t WHERE g.id = t.id";

        List pagedParams = getDialect().rebuildParameters(true, true, Collects.emptyArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);

        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);

        List<Object> paramList = new ArrayList<>();
        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
        String where = " tenant_id= ? ";
        paramList.add(tenant);
        if (!Strings.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!Strings.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }
        if (!Strings.isBlank(tenant)) {
            where += " AND content LIKE ? ";
            paramList.add(tenant);
        }
        String sql = sqlFetchRows + where + " order by id asc";

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = new ArrayList<>();

        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id= ?" );
        paramList.add(tenantId);
        if (Strings.isNotBlank(dataId)) {
            where.append(" AND data_id=? ");
            paramList.add(dataId);
        }
        if (Strings.isNotBlank(group)) {
            where.append(" AND group_id=? ");
            paramList.add(group);
        }

        if (Strings.isNotBlank(appName)) {
            where.append(" AND app_name=? ");
            paramList.add(appName);
        }
        if (!Strings.isBlank(content)) {
            where.append(" AND content LIKE ? ");
            paramList.add(content);
        }

        sql = sql + where + " order by id asc";
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        sql = getDialect().getLimitSql(sql, rowSelection);

        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }



    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findConfigInfoBaseByGroupFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT id,data_id,group_id,content FROM config_info WHERE group_id=? AND tenant_id=? order by id asc";

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.GROUP_ID),
                context.getWhereParameter(FieldConstant.TENANT_ID), tenantId);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = Lists.newArrayList();

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,encrypted_data_key FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");

        where.append(" tenant_id LIKE ? ");
        paramList.add(tenantId);
        if (!Strings.isBlank(dataId)) {
            where.append(" AND data_id LIKE ? ");
            paramList.add(dataId);
        }
        if (!Strings.isBlank(group)) {
            where.append(" AND group_id LIKE ? ");
            paramList.add(group);
        }
        if (!Strings.isBlank(appName)) {
            where.append(" AND app_name = ? ");
            paramList.add(appName);
        }
        if (!Strings.isBlank(content)) {
            where.append(" AND content LIKE ? ");
            paramList.add(content);
        }

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = sqlFetchRows + where +" order by id asc";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findAllConfigInfoFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        String tenantId =(String) context.getWhereParameter(FieldConstant.TENANT_ID);

        List paramList = Lists.newArrayList();
        String subquery = "SELECT id FROM config_info  WHERE tenant_id LIKE ?  ORDER BY id ";

        String pagedSubquery = getDialect().getLimitSql(subquery,true,true, rowSelection);
        paramList.add(tenantId);

        List pagedParams = getDialect().rebuildParameters(true,true, paramList, rowSelection);

        String sql = " SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5 FROM ( " + pagedSubquery + " ) g, config_info t  WHERE g.id = t.id ";
        return new MapperResult(sql, pagedParams);
    }

}
