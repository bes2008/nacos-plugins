package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.nacos.plugin.datasource.NacosEnvs;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonHistoryConfigInfoMapper extends BaseMapper implements HistoryConfigInfoMapper {

    @Override
    public MapperResult removeConfigHistory(MapperContext context) {

        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.LIMIT_SIZE).toString());

        RowSelection rowSelection = new RowSelection(0, pageSize);

        // MySQL 中，limit 如果在子查询中时，不支持放在 in/all/any/some 的子查询中。
        // 这个子查询是在 in 子句 中，且有 limit ，MySQL 是不支持的
        String sql=null;
        if(!getDialect().getDelegate().isSupportsVariableLimitInSubquery()){
            sql= "DELETE FROM his_config_info h WHERE gmt_modified < ? ";
            sql = getDialect().getLimitSql(sql, rowSelection);
        }else {
            String subQuerySql = "select hi.id AS id from his_config_info hi where hi.gmt_modified < ? order by hi.gmt_modified asc";
            subQuerySql = getDialect().getLimitSql(subQuerySql, true, true, rowSelection);
            sql = "DELETE FROM his_config_info h WHERE h.id in (  "+subQuerySql+" ) ";
        }
        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.START_TIME));

        List pagedParams = getDialect().rebuildParameters(true, true, paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    /**
     * 当前nacos版本，不能使用 ? 用作 limit, offset 参数
     */
    @Override
    public MapperResult pageFindConfigHistoryFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());

        List<String> selectedColumns = NacosEnvs.versionGreatEquals("2.5.0") ?
                Lists.newArrayList("nid","data_id","group_id","tenant_id","app_name","src_ip","src_user","op_type","ext_info","publish_type","gray_name","gmt_create","gmt_modified"):
                Lists.newArrayList("nid","data_id","group_id","tenant_id","app_name","src_ip","src_user","op_type","gmt_create","gmt_modified");
        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        String sql = select(selectedColumns, where)+ " ORDER BY nid DESC  ";

        sql = getDialect().getLimitSql(sql, false, false, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.DATA_ID),
                context.getWhereParameter(FieldConstant.GROUP_ID), context.getWhereParameter(FieldConstant.TENANT_ID));

        List pagedParams = getDialect().rebuildParameters(false, false,paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findDeletedConfig(MapperContext context) {

        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.PAGE_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);
        if(NacosEnvs.versionGreatEquals("2.5.0")){
            String sql = "SELECT id, nid, data_id, group_id, app_name, content, md5, gmt_create, gmt_modified, src_user, src_ip, op_type, tenant_id, publish_type, gray_name, ext_info, encrypted_data_key " +
                    "FROM his_config_info " +
                    "WHERE op_type = 'D' AND publish_type = ? and gmt_modified >= ? and nid > ? order by nid ";

            sql = getDialect().getLimitSql(sql, rowSelection);

            List paramList = Lists.newArrayList(
                    context.getWhereParameter("publishType"),
                    context.getWhereParameter(FieldConstant.START_TIME),
                    context.getWhereParameter(FieldConstant.LAST_MAX_ID));
            List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
            return new MapperResult(sql, pagedParams);
        }else{
            String sql = "SELECT data_id, group_id, tenant_id,gmt_modified,nid FROM his_config_info WHERE op_type = 'D' AND "
                    + "gmt_modified >= ? and nid > ? order by nid ";
            sql = getDialect().getLimitSql(sql, rowSelection);

            List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.START_TIME),
                    context.getWhereParameter(FieldConstant.LAST_MAX_ID));

            List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
            return new MapperResult(sql, pagedParams);
        }

    }

    @Override
    public MapperResult findConfigHistoryFetchRows(MapperContext context) {
        useDefaultTenantIdWithWhereParameter(context);
        return HistoryConfigInfoMapper.super.findConfigHistoryFetchRows(context);
    }
}
