package com.jn.nacos.plugin.datasource.db.magicdata.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseConfigInfoAggrMapper;

public class ConfigInfoAggrMapper extends BaseConfigInfoAggrMapper {
    public ConfigInfoAggrMapper() {
        super(DatabaseTypes.MAGICDATA);
    }
}