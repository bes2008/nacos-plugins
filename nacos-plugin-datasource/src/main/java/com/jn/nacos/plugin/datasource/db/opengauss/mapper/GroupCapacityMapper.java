package com.jn.nacos.plugin.datasource.db.opengauss.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.mapper.BaseGroupCapacityMapper;

public class GroupCapacityMapper extends BaseGroupCapacityMapper {
    public GroupCapacityMapper() {
        super(DatabaseTypes.OPENGAUSS);
    }
}
