package com.jn.nacos.plugin.datasource.db.postgresql.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class PostgresqlDatabaseDialect extends NacosDatabaseDialect {
    public PostgresqlDatabaseDialect(){
        super(DatabaseTypes.POSTGRESQL);
    }
}
