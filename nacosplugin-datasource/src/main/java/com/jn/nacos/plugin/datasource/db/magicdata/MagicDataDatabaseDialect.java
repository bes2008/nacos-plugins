package com.jn.nacos.plugin.datasource.db.magicdata;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class MagicDataDatabaseDialect extends NacosDatabaseDialect {
    public MagicDataDatabaseDialect() {
        super(DatabaseTypes.MAGICDATA);
    }
}
