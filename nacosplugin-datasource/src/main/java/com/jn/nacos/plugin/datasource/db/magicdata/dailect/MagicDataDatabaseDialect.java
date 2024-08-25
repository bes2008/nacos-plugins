package com.jn.nacos.plugin.datasource.db.magicdata.dailect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class MagicDataDatabaseDialect extends NacosDatabaseDialect {
    public MagicDataDatabaseDialect() {
        super(DatabaseTypes.MAGICDATA);
    }
}
