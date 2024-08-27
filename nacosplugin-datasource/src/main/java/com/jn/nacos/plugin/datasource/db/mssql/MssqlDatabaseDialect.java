package com.jn.nacos.plugin.datasource.db.mssql;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class MssqlDatabaseDialect extends NacosDatabaseDialect {
    public MssqlDatabaseDialect() {
        super(DatabaseTypes.MSSQL);
    }

}
