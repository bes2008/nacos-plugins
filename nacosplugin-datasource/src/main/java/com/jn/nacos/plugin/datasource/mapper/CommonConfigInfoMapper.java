package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.common.utils.StringUtils;
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
        String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);
        String appName = (String)context.getWhereParameter(FieldConstant.APP_NAME);

        StringBuilder sqlBuilder = new StringBuilder("SELECT count(*) FROM config_info WHERE ");
        List paramList = Lists.newArrayList();
        if(Strings.isBlank(tenantId)){
            sqlBuilder.append(" tenant_id='").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            sqlBuilder.append(" tenant_id LIKE ?");
            paramList.add(tenantId);
        }
        String sql = sqlBuilder.append(" AND app_name = ?").toString();
        paramList.add(appName);
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult configInfoLikeTenantCount(MapperContext context) {
        String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        List paramList = Lists.newArrayList();
        StringBuilder sqlBuilder= new StringBuilder("SELECT count(*) FROM config_info WHERE ");
        if(Strings.isBlank(tenantId)){
            sqlBuilder.append(" tenant_id='").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            sqlBuilder.append(" tenant_id LIKE ?");
            paramList.add(tenantId);
        }
        String sql = sqlBuilder.toString();
        return new MapperResult(sql, paramList);
    }

    @Override
    public MapperResult findAllConfigInfo4Export(MapperContext context) {
        List<Long> ids = (List<Long>) context.getWhereParameter(FieldConstant.IDS);

        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_create,gmt_modified,"
                + "src_user,src_ip,c_desc,c_use,effect,c_schema,encrypted_data_key FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");

        List<Object> paramList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            where.append(" id IN (");
            for (int i = 0; i < ids.size(); i++) {
                if (i != 0) {
                    where.append(", ");
                }
                where.append('?');
                paramList.add(ids.get(i));
            }
            where.append(") ");
        } else {


            String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);
            String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);

            where.append(" tenant_id = "+ getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId()));
            paramList.add(tenantId);

            if (StringUtils.isNotBlank(dataId)) {
                where.append(" AND data_id LIKE ? ");
                paramList.add(dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                where.append(" AND group_id= ? ");
                paramList.add(group);
            }
            if (StringUtils.isNotBlank(appName)) {
                where.append(" AND app_name= ? ");
                paramList.add(appName);
            }
        }
        return new MapperResult(sql + where, paramList);
    }

    @Override
    public MapperResult findConfigInfoBaseLikeCountRows(MapperContext context) {
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        final List<Object> paramList = new ArrayList<>();
        final String sqlCountRows = "SELECT count(*) FROM config_info WHERE ";
        String where = "tenant_id='" + NamespaceUtil.getNamespaceDefaultId() + "' ";

        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
            paramList.add(group);
        }
        if (!StringUtils.isBlank(content)) {
            where += " AND content LIKE ? ";
            paramList.add(content);
        }
        return new MapperResult(sqlCountRows + where, paramList);
    }

    @Override
    public MapperResult findConfigInfo4PageCountRows(MapperContext context) {
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final List<Object> paramList = new ArrayList<>();

        final String sqlCount = "SELECT count(*) FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id= " + getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId()));
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
        return new MapperResult(sqlCount + where, paramList);
    }

    @Override
    public MapperResult findConfigInfoLike4PageCountRows(MapperContext context) {
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        final List<Object> paramList = new ArrayList<>();

        final String sqlCountRows = "SELECT count(*) FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");

        if(Strings.isBlank(tenantId)){
            where.append(" tenant_id='").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            where.append(" tenant_id LIKE ? ");
            paramList.add(tenantId);
        }


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
        return new MapperResult(sqlCountRows + where, paramList);
    }

    @Override
    public MapperResult updateConfigInfoAtomicCas(MapperContext context) {
        List<Object> paramList = new ArrayList<>();

        paramList.add(context.getUpdateParameter(FieldConstant.CONTENT));
        paramList.add(context.getUpdateParameter(FieldConstant.MD5));
        paramList.add(context.getUpdateParameter(FieldConstant.SRC_IP));
        paramList.add(context.getUpdateParameter(FieldConstant.SRC_USER));
        paramList.add(context.getUpdateParameter(FieldConstant.APP_NAME));
        paramList.add(context.getUpdateParameter(FieldConstant.C_DESC));
        paramList.add(context.getUpdateParameter(FieldConstant.C_USE));
        paramList.add(context.getUpdateParameter(FieldConstant.EFFECT));
        paramList.add(context.getUpdateParameter(FieldConstant.TYPE));
        paramList.add(context.getUpdateParameter(FieldConstant.C_SCHEMA));
        if(hasEncryptedDataKeyColumn()) {
            paramList.add(context.getUpdateParameter(FieldConstant.ENCRYPTED_DATA_KEY));
        }
        paramList.add(context.getWhereParameter(FieldConstant.DATA_ID));
        paramList.add(context.getWhereParameter(FieldConstant.GROUP_ID));
        paramList.add(context.getWhereParameter(FieldConstant.TENANT_ID));
        paramList.add(context.getWhereParameter(FieldConstant.MD5));
        String sql = "UPDATE config_info SET " + "content=?, md5=?, src_ip=?, src_user=?, gmt_modified="
                + getFunction("NOW()")
                + ", app_name=?, c_desc=?, c_use=?, effect=?, type=?, c_schema=?"
                + (hasEncryptedDataKeyColumn()? ", encrypted_data_key=? ":"")
                + "WHERE data_id=? AND group_id=? AND tenant_id="+getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId())
                +" AND (md5=? OR md5 IS NULL OR md5='')";
        return new MapperResult(sql, paramList);
    }

    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findConfigInfoByAppFetchRows(MapperContext context) {
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());


        List paramList = Lists.newArrayList();

        StringBuilder sqlBuilder = new StringBuilder("SELECT ID,data_id,group_id,tenant_id,app_name,content FROM config_info WHERE ");
        if(Strings.isBlank(tenantId)){
            sqlBuilder.append(" tenant_id= '").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            sqlBuilder.append(" tenant_id LIKE ?");
            paramList.add(tenantId);
        }
        String sql = sqlBuilder.append(" AND app_name = ? order by id asc").toString();
        paramList.add(appName);

        sql = getDialect().getLimitSql(sql, rowSelection);
        List<Object> pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }



    @Override
    public MapperResult getTenantIdList(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT tenant_id FROM config_info WHERE tenant_id != '" + NamespaceUtil.getNamespaceDefaultId() + "' GROUP BY tenant_id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    @Override
    public MapperResult getGroupIdList(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT group_id FROM config_info WHERE tenant_id ='" + NamespaceUtil.getNamespaceDefaultId() + "' GROUP BY group_id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }



    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findAllConfigKey(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        String tenantId = (String)context.getWhereParameter(FieldConstant.TENANT_ID);

        List paramList = Lists.newArrayList();

        StringBuilder subqueryBuilder = new StringBuilder("SELECT id FROM config_info  WHERE ");
        if(Strings.isBlank(tenantId)){
            subqueryBuilder.append(" tenant_id= '").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            subqueryBuilder.append("tenant_id LIKE ?");
            paramList.add(tenantId);
        }

        String subquery = subqueryBuilder.append("ORDER BY id ").toString();

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
        String sql = " SELECT t.id,data_id,group_id,tenant_id,app_name,type,md5,gmt_modified FROM ( " + pagedSubquery + " ) g, config_info t WHERE g.id = t.id";

        List pagedParams = getDialect().rebuildParameters(true, true, Collects.emptyArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    /**
     * 该方法没有被调用
     */
    @Override
    public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);

        List<Object> paramList = new ArrayList<>();
        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
        String where = " tenant_id='" + NamespaceUtil.getNamespaceDefaultId() + "' ";
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
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = new ArrayList<>();

        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id= " + getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId()));
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

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = "SELECT id,data_id,group_id,content FROM config_info WHERE group_id=? AND tenant_id="+getDialect().genCastNullToDefaultExpression("?", NamespaceUtil.getNamespaceDefaultId())+" order by id asc";

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.GROUP_ID),
                context.getWhereParameter(FieldConstant.TENANT_ID));
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }


    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {

        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

        List<Object> paramList = Lists.newArrayList();

        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,encrypted_data_key FROM config_info";
        StringBuilder where = new StringBuilder(" WHERE ");

        if(Strings.isBlank(tenantId)){
            where.append(" tenant_id='").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            where.append(" tenant_id LIKE ? ");
            paramList.add(tenantId);
        }
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
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        String tenantId =(String) context.getWhereParameter(FieldConstant.TENANT_ID);

        List paramList = Lists.newArrayList();

        StringBuilder subqueryBuilder = new StringBuilder("SELECT id FROM config_info  WHERE ");
        if(Strings.isBlank(tenantId)){
            subqueryBuilder.append(" tenant_id= '").append(NamespaceUtil.getNamespaceDefaultId()).append("' ");
        }else{
            subqueryBuilder.append("tenant_id LIKE ?");
            paramList.add(tenantId);
        }

        String subquery = subqueryBuilder.append(" ORDER BY id ").toString();
        String pagedSubquery = getDialect().getLimitSql(subquery,true,true, rowSelection);


        List pagedParams = getDialect().rebuildParameters(true,true, paramList, rowSelection);

        String sql = " SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5 FROM ( " + pagedSubquery + " ) g, config_info t  WHERE g.id = t.id ";
        return new MapperResult(sql, pagedParams);
    }

}
