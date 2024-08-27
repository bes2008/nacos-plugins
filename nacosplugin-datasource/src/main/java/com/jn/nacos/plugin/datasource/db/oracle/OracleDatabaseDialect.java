package com.jn.nacos.plugin.datasource.db.oracle;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class OracleDatabaseDialect extends NacosDatabaseDialect {
    public OracleDatabaseDialect() {
        super(DatabaseTypes.ORACLE);
    }
}
