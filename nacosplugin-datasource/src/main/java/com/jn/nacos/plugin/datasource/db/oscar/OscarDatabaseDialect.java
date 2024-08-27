package com.jn.nacos.plugin.datasource.db.oscar;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class OscarDatabaseDialect extends NacosDatabaseDialect {
    public OscarDatabaseDialect(){
        super(DatabaseTypes.OSCAR);
    }
}
