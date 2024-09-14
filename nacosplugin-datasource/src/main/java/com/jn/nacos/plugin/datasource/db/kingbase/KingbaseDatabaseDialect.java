package com.jn.nacos.plugin.datasource.db.kingbase;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class KingbaseDatabaseDialect extends NacosDatabaseDialect {
    public KingbaseDatabaseDialect() {
        super(DatabaseNames.KINGBASE);
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " NVL("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }
}
