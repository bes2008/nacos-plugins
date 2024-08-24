package com.jn.nacos.plugin.datasource.db.postgresql.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;

public class PostgresqlDatabaseDialect extends DatabaseDialect {
    public PostgresqlDatabaseDialect(){
        super(DatabaseTypes.POSTGRESQL);
    }
}
