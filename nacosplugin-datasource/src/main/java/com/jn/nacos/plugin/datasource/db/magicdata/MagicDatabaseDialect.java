package com.jn.nacos.plugin.datasource.db.magicdata;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;

public class MagicDatabaseDialect extends NacosDatabaseDialect {
    public MagicDatabaseDialect() {
        super(DatabaseNames.MAGICDATA);
        this.identifierQuotedMode = IdentifierQuotedMode.QUOTED;
    }

    @Override
    public boolean isAutoCastEmptyStringToNull(SqlCompatibilityType sqlCompatibilityType) {
        return true;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " COALESCE("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }

    @Override
    public SqlCompatibilityType getDefaultCompatibilityType() {
        return SqlCompatibilityType.ORACLE;
    }
}
