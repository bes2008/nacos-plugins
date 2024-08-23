package com.jn.nacos.plugin.datasource.db.magicdata.dailect;

import com.jn.nacos.plugin.datasource.base.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;

public class MagicDataDatabaseDialect extends DatabaseDialect {
    public MagicDataDatabaseDialect() {
        super(DatabaseTypes.MAGICDATA);
    }
}
