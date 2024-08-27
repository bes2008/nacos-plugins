package com.jn.nacos.plugin.datasource.db.kingbase;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class KingbaseDatabaseDialect extends NacosDatabaseDialect {
    public KingbaseDatabaseDialect() {
        super(DatabaseTypes.KINGBASE);
    }
}
