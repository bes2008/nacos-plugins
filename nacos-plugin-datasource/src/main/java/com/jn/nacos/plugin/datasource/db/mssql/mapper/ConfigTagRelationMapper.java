package com.jn.nacos.plugin.datasource.db.mssql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseConfigTagRelationMapper;

public class ConfigTagRelationMapper extends BaseConfigTagRelationMapper {
    public ConfigTagRelationMapper() {
        super(DatabaseTypes.MSSQL);
    }
}
