package com.jn.nacos.plugin.datasource.db.oscar.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.DatabaseDialect;

public class OscarDatabaseDialect extends DatabaseDialect {
    public OscarDatabaseDialect(){
        super(DatabaseTypes.OSCAR);
    }
}
