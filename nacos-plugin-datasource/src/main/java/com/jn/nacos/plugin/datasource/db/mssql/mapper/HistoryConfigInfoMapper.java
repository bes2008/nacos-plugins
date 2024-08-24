package com.jn.nacos.plugin.datasource.db.mssql.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseHistoryConfigInfoMapper;

public class HistoryConfigInfoMapper extends BaseHistoryConfigInfoMapper {
    public HistoryConfigInfoMapper() {
        super(DatabaseTypes.MSSQL);
    }
}
