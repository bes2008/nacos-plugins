package com.jn.nacos.plugin.datasource.db.mssql;

import com.jn.langx.util.collection.Maps;
import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

import java.util.Map;

public class MssqlDatabaseDialect extends NacosDatabaseDialect {
    public MssqlDatabaseDialect() {
        super(DatabaseNames.MSSQL);
    }

    @Override
    protected Map<String, String> specifiedFunctions() {
        Map<String,String> map = Maps.newHashMap();
        map.put("NOW()", "CURRENT_TIMESTAMP");
        return map;
    }
}
