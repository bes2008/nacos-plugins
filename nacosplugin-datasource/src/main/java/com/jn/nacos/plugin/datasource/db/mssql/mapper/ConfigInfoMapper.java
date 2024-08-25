package com.jn.nacos.plugin.datasource.db.mssql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseConfigInfoMapper;

public class ConfigInfoMapper extends BaseConfigInfoMapper {
    public ConfigInfoMapper() {
        super(DatabaseTypes.MSSQL);
    }
}
