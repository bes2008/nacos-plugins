package com.jn.nacos.plugin.datasource.db.mssql.driver;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.DatabaseDialect;

public class MssqlDatabaseDialect extends DatabaseDialect {
    public MssqlDatabaseDialect() {
        super(DatabaseTypes.MSSQL);
    }

}
