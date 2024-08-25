package com.jn.nacos.plugin.datasource.db.dm.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class DmDatabaseDialect extends NacosDatabaseDialect {
    public DmDatabaseDialect() {
        super(DatabaseTypes.DAMENG);
    }
}
