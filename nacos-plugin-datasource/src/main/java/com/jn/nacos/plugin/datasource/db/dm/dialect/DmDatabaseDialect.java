package com.jn.nacos.plugin.datasource.db.dm.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;

public class DmDatabaseDialect extends DatabaseDialect {
    public DmDatabaseDialect() {
        super(DatabaseTypes.DAMENG);
    }
}
