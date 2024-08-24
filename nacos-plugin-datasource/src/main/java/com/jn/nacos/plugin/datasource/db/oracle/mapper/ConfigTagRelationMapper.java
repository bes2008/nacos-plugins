package com.jn.nacos.plugin.datasource.db.oracle.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseConfigTagRelationMapper;

public class ConfigTagRelationMapper extends BaseConfigTagRelationMapper {
    public ConfigTagRelationMapper() {
        super(DatabaseTypes.ORACLE);
    }
}
