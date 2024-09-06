package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoTagMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;

public class CommonConfigInfoTagMapper extends BaseMapper implements ConfigInfoTagMapper {

    /**
     * 当前nacos版本，不能使用 ? 用作 limit, offset 参数
     */
    @Override
    public MapperResult findAllConfigInfoTagForDumpAllFetchRows(MapperContext context) {
        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String subquery = " SELECT id FROM config_info_tag  ORDER BY id ";
        String pagedSubquery = getDialect().getLimitSql(subquery, true,false, rowSelection);
        String sql = "SELECT t.id,data_id,group_id,tenant_id,tag_id,app_name,content,md5,gmt_modified FROM ( " + pagedSubquery + " ) g, config_info_tag t  WHERE g.id = t.id";
        List pagedParams = getDialect().rebuildParameters(true, false,Collects.emptyArrayList(), rowSelection);

        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult updateConfigInfo4TagCas(MapperContext context) {
        Object content = context.getUpdateParameter(FieldConstant.CONTENT);
        Object md5 = context.getUpdateParameter(FieldConstant.MD5);
        Object srcIp = context.getUpdateParameter(FieldConstant.SRC_IP);
        Object srcUser = context.getUpdateParameter(FieldConstant.SRC_USER);
        Object gmtModified = context.getUpdateParameter(FieldConstant.GMT_MODIFIED);
        Object appName = context.getUpdateParameter(FieldConstant.APP_NAME);

        Object dataId = context.getWhereParameter(FieldConstant.DATA_ID);
        Object groupId = context.getWhereParameter(FieldConstant.GROUP_ID);
        Object tenantId = context.getWhereParameter(FieldConstant.TENANT_ID);
        Object tagId = context.getWhereParameter(FieldConstant.TAG_ID);
        Object oldMd5 = context.getWhereParameter(FieldConstant.MD5);

        List<String> updatedColumns = Lists.newArrayList("content", "md5", "src_ip", "src_user", "gmt_modified", "app_name");
        List<String> where = Lists.newArrayList("data_id", "group_id", "tenant_id", "tag_id");
        String sql = update(updatedColumns, where)+ " AND (md5 = ? OR md5 IS NULL OR md5 = '')";
        return new MapperResult(sql,
                CollectionUtils.list(content, md5, srcIp, srcUser, gmtModified, appName, dataId, groupId, tenantId,
                        tagId, oldMd5));
    }
}
