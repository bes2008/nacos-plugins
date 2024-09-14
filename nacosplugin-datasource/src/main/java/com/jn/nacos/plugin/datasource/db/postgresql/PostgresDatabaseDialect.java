package com.jn.nacos.plugin.datasource.db.postgresql;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class PostgresDatabaseDialect extends NacosDatabaseDialect {
    public PostgresDatabaseDialect() {
        super(DatabaseNames.POSTGRESQL);
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
