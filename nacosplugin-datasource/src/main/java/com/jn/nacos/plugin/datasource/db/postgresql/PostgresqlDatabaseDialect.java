package com.jn.nacos.plugin.datasource.db.postgresql;

import com.jn.langx.util.collection.Maps;
import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

import java.util.Map;

public class PostgresqlDatabaseDialect extends NacosDatabaseDialect {
    public PostgresqlDatabaseDialect(){
        super(DatabaseTypes.POSTGRESQL);
    }

}
