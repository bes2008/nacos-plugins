package com.jn.nacos.plugin.datasource.db.dm.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

import java.util.Map;

public class DmDatabaseDialect extends NacosDatabaseDialect {
    public DmDatabaseDialect() {
        super(DatabaseTypes.DAMENG);
    }

    @Override
    protected Map<String, String> initFunctionMap() {
        Map<String, String> map= super.initFunctionMap();
        map.put("NOW()", "CURRENT_TIMESTAMP(3)");
        return map;
    }
}
