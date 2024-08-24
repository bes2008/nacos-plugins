package com.jn.nacos.plugin.datasource.db.postgresql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseConfigInfoTagMapper;

public class ConfigInfoTagMapper extends BaseConfigInfoTagMapper {
    public ConfigInfoTagMapper() {
        super(DatabaseTypes.POSTGRESQL);
    }
}
