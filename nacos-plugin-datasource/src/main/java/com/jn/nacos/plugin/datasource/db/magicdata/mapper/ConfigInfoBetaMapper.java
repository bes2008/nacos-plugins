package com.jn.nacos.plugin.datasource.db.magicdata.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseConfigInfoBetaMapper;

public class ConfigInfoBetaMapper extends BaseConfigInfoBetaMapper {
    public ConfigInfoBetaMapper() {
        super(DatabaseTypes.MAGICDATA);
    }
}
