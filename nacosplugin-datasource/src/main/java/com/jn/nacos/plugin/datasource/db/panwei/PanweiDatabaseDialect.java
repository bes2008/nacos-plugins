package com.jn.nacos.plugin.datasource.db.panwei;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class PanweiDatabaseDialect extends NacosDatabaseDialect {
    public PanweiDatabaseDialect() {
        super(DatabaseNames.PANWEI);
        this.identifierQuotedMode = IdentifierQuotedMode.QUOTED;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " COALESCE(" + expressionOrIdentifier + ", '" + defaultValue + "') ";
    }

}