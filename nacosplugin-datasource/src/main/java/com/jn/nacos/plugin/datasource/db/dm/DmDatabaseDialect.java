package com.jn.nacos.plugin.datasource.db.dm;

import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;

public class DmDatabaseDialect extends NacosDatabaseDialect {
    public DmDatabaseDialect() {
        super("dm");
    }

    @Override
    public SqlCompatibilityType getDefaultCompatibilityType() {
        return null;
    }
}
