package com.jn.nacos.plugin.datasource.db.kingbase.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class KingbaseDatabaseDialect extends NacosDatabaseDialect {
    public KingbaseDatabaseDialect() {
        super(DatabaseTypes.KINGBASE);
    }
}
