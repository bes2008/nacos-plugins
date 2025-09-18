package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoGrayMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonConfigInfoGrayMapper extends BaseMapper implements ConfigInfoGrayMapper {
    public MapperResult findChangeConfig(MapperContext context) {
        String sql = "SELECT id, data_id, group_id, tenant_id, app_name,content,gray_name,gray_rule,md5, gmt_modified, encrypted_data_key "
                        + "FROM config_info_gray WHERE " + "gmt_modified >= ? and id > ? order by id ";
        int pageSize = Integer.parseInt(context.getWhereParameter(FieldConstant.PAGE_SIZE).toString());
        RowSelection rowSelection = new RowSelection(0, pageSize);

        sql = getDialect().getLimitSql(sql, rowSelection);

        List paramList = Lists.newArrayList(context.getWhereParameter(FieldConstant.START_TIME),
                context.getWhereParameter(FieldConstant.LAST_MAX_ID)
        );
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    public MapperResult findAllConfigInfoGrayForDumpAllFetchRows(MapperContext context){

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = " SELECT id,data_id,group_id,tenant_id,gray_name,gray_rule,app_name,content,md5,gmt_modified "
                + " FROM  config_info_gray  ORDER BY id ";
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(Lists.newArrayList(), rowSelection);
        return new MapperResult(sql, pagedParams);
    }
}
