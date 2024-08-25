package com.jn.nacos.plugin.datasource.db.oracle.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class OracleDatabaseDialect extends NacosDatabaseDialect {
    public OracleDatabaseDialect() {
        super(DatabaseTypes.ORACLE);
    }
}
