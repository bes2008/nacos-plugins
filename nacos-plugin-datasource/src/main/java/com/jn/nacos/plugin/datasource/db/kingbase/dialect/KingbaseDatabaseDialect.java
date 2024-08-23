package com.jn.nacos.plugin.datasource.db.kingbase.dialect;

import com.jn.nacos.plugin.datasource.base.DatabaseTypes;
import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;

public class KingbaseDatabaseDialect extends DatabaseDialect {
    public KingbaseDatabaseDialect() {
        super(DatabaseTypes.KINGBASE);
    }
}
