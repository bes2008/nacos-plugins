package com.jn.nacos.plugin.datasource.db.postgresql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseConfigTagRelationMapper;

public class ConfigTagRelationMapper extends BaseConfigTagRelationMapper {
    public ConfigTagRelationMapper() {
        super(DatabaseTypes.POSTGRESQL);
    }
}
