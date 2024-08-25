package com.jn.nacos.plugin.datasource.db.oscar.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class OscarDatabaseDialect extends NacosDatabaseDialect {
    public OscarDatabaseDialect(){
        super(DatabaseTypes.OSCAR);
    }
}
