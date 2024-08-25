package com.jn.nacos.plugin.datasource.db.mssql.driver;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class MssqlDatabaseDialect extends NacosDatabaseDialect {
    public MssqlDatabaseDialect() {
        super(DatabaseTypes.MSSQL);
    }

}
