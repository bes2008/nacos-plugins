package com.jn.nacos.plugin.datasource.db.opengauss.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseConfigInfoBetaMapper;

public class ConfigInfoBetaMapper extends BaseConfigInfoBetaMapper {
    public ConfigInfoBetaMapper() {
        super(DatabaseTypes.OPENGAUSS);
    }
}
