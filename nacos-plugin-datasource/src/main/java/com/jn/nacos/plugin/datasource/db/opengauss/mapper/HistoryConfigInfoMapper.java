package com.jn.nacos.plugin.datasource.db.opengauss.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseHistoryConfigInfoMapper;

public class HistoryConfigInfoMapper extends BaseHistoryConfigInfoMapper {
    public HistoryConfigInfoMapper() {
        super(DatabaseTypes.OPENGAUSS);
    }
}
