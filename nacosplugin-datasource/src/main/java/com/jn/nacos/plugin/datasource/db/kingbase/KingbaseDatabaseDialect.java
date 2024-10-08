package com.jn.nacos.plugin.datasource.db.kingbase;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class KingbaseDatabaseDialect extends NacosDatabaseDialect {
    public KingbaseDatabaseDialect() {
        super(DatabaseNames.KINGBASE);
        this.identifierQuotedMode = IdentifierQuotedMode.QUOTED;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " NVL("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }
}
