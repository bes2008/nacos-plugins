package com.jn.nacos.plugin.datasource.db.oracle.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.DatabaseDialect;

public class OracleDatabaseDialect extends DatabaseDialect {
    public OracleDatabaseDialect() {
        super(DatabaseTypes.ORACLE);
    }
}
