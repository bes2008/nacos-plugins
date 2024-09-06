package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoBetaMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.List;

public class CommonConfigInfoBetaMapper extends BaseMapper implements ConfigInfoBetaMapper {

    /**
     * 当前nacos版本，不能使用 ? 用作 limit, offset 参数
     */
    @Override
    public MapperResult findAllConfigInfoBetaForDumpAllFetchRows(MapperContext context) {
        int startRow = context.getStartRow();
        int pageSize = context.getPageSize();

        RowSelection rowSelection = new RowSelection(startRow, pageSize);
        String subquery = "SELECT id FROM config_info_beta ORDER BY id ";
        subquery = getDialect().getLimitSql(subquery, true, false, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,beta_ips " + (hasEncryptedDataKeyColumn() ? ", encrypted_data_key ":"")
                + " FROM ( " + subquery + " )" + " g, config_info_beta t WHERE g.id = t.id";

        List<Object> paramList = Lists.newArrayList();
        List pagedParams = getDialect().rebuildParameters(true, false, paramList, rowSelection);

        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult updateConfigInfo4BetaCas(MapperContext context) {

        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id");
        final String sql = "UPDATE config_info_beta SET content = ?,md5 = ?,beta_ips = ?,src_ip = ?,src_user = ?,gmt_modified = "
                + getFunction("NOW()")
                + ",app_name = ? " + genWhereClause(where)
                + " AND (md5 = ? OR md5 is null OR md5 = '')";

        List<Object> paramList = new ArrayList<>();

        paramList.add(context.getUpdateParameter(FieldConstant.CONTENT));
        paramList.add(context.getUpdateParameter(FieldConstant.MD5));
        paramList.add(context.getUpdateParameter(FieldConstant.BETA_IPS));
        paramList.add(context.getUpdateParameter(FieldConstant.SRC_IP));
        paramList.add(context.getUpdateParameter(FieldConstant.SRC_USER));
        paramList.add(context.getUpdateParameter(FieldConstant.APP_NAME));

        paramList.add(context.getWhereParameter(FieldConstant.DATA_ID));
        paramList.add(context.getWhereParameter(FieldConstant.GROUP_ID));
        paramList.add(context.getWhereParameter(FieldConstant.TENANT_ID));
        paramList.add(context.getWhereParameter(FieldConstant.MD5));

        return new MapperResult(sql, paramList);
    }
}
