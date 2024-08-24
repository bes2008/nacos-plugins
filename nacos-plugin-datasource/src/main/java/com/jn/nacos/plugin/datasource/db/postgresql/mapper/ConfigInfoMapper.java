package com.jn.nacos.plugin.datasource.db.postgresql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseConfigInfoMapper;

public class ConfigInfoMapper extends BaseConfigInfoMapper {
    public ConfigInfoMapper() {
        super(DatabaseTypes.POSTGRESQL);
    }
}
