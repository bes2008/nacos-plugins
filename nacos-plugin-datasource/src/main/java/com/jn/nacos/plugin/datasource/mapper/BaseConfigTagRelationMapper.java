package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigTagsRelationMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.ArrayList;
import java.util.List;

public class BaseConfigTagRelationMapper extends BaseMapper implements ConfigTagsRelationMapper {
    public BaseConfigTagRelationMapper(String databaseId) {
        super(databaseId);
    }

    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);

        List<Object> paramList = new ArrayList<>();
        StringBuilder where = new StringBuilder(" WHERE ");
        final String baseSql =
                "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content FROM config_info  a LEFT JOIN "
                        + "config_tags_relation b ON a.id=b.id";

        where.append(" a.tenant_id=? ");
        paramList.add(tenantId);

        if (StringUtils.isNotBlank(dataId)) {
            where.append(" AND a.data_id=? ");
            paramList.add(dataId);
        }
        if (StringUtils.isNotBlank(group)) {
            where.append(" AND a.group_id=? ");
            paramList.add(group);
        }
        if (StringUtils.isNotBlank(appName)) {
            where.append(" AND a.app_name=? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND a.content LIKE ? ");
            paramList.add(content);
        }
        where.append(" AND b.tag_name IN (");
        for (int i = 0; i < tagArr.length; i++) {
            if (i != 0) {
                where.append(", ");
            }
            where.append('?');
            paramList.add(tagArr[i]);
        }
        where.append(") ");

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = baseSql + where;
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

    @Override
    public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);

        List<Object> paramList = new ArrayList<>();
        StringBuilder where = new StringBuilder(" WHERE ");
        final String baseSql =
                "SELECT a.ID,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content FROM config_info  a LEFT JOIN "
                        + "config_tags_relation b ON a.id=b.id ";

        where.append(" a.tenant_id LIKE ? ");
        paramList.add(tenantId);

        if (!StringUtils.isBlank(dataId)) {
            where.append(" AND a.data_id LIKE ? ");
            paramList.add(dataId);
        }
        if (!StringUtils.isBlank(group)) {
            where.append(" AND a.group_id LIKE ? ");
            paramList.add(group);
        }
        if (!StringUtils.isBlank(appName)) {
            where.append(" AND a.app_name = ? ");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND a.content LIKE ? ");
            paramList.add(content);
        }

        where.append(" AND b.tag_name IN (");
        for (int i = 0; i < tagArr.length; i++) {
            if (i != 0) {
                where.append(", ");
            }
            where.append('?');
            paramList.add(tagArr[i]);
        }
        where.append(") ");

        RowSelection rowSelection = new RowSelection(context.getStartRow(), context.getPageSize());
        String sql = baseSql + where;
        sql = getDialect().getLimitSql(sql, rowSelection);
        List pagedParams = getDialect().rebuildParameters(paramList, rowSelection);
        return new MapperResult(sql, pagedParams);
    }

}
