package com.jn.nacos.plugin.datasource.db.postgresql;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class PostgresqlDatabaseDialect extends NacosDatabaseDialect {
    public PostgresqlDatabaseDialect(){
        super(DatabaseTypes.POSTGRESQL);
    }
}
