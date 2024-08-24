package com.jn.nacos.plugin.datasource.db.magicdata.mapper;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.mapper.BaseGroupCapacityMapper;

public class GroupCapacityMapper extends BaseGroupCapacityMapper {
    public GroupCapacityMapper() {
        super(DatabaseTypes.MAGICDATA);
    }
}
