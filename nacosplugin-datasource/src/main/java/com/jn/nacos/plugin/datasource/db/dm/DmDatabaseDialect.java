package com.jn.nacos.plugin.datasource.db.dm;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;


public class DmDatabaseDialect extends NacosDatabaseDialect {
    public DmDatabaseDialect() {
        super(DatabaseTypes.DAMENG);
    }

}
