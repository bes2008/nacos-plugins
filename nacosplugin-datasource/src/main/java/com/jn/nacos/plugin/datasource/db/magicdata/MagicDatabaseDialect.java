package com.jn.nacos.plugin.datasource.db.magicdata;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class MagicDatabaseDialect extends NacosDatabaseDialect {
    public MagicDatabaseDialect() {
        super(DatabaseNames.MAGICDATA);
    }

    @Override
    public boolean isAutoCastEmptyStringToNull() {
        return true;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " COALESCE("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }
}
