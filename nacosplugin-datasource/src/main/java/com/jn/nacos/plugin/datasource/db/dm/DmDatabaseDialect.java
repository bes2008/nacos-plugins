package com.jn.nacos.plugin.datasource.db.dm;

import com.google.common.collect.Maps;
import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

import java.util.Map;

public class DmDatabaseDialect extends NacosDatabaseDialect {
    public DmDatabaseDialect() {
        super(DatabaseTypes.DAMENG);
    }

    @Override
    protected Map<String, String> specifiedFunctions() {
        Map<String, String> map= Maps.newHashMap();
        map.put("NOW()", "CURRENT_TIMESTAMP(3)");
        return map;
    }
}
