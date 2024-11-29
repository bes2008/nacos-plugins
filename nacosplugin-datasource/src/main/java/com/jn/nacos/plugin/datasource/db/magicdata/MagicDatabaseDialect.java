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
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " COALESCE("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }

    @Override
    public SqlCompatibilityType getDefaultCompatibilityType() {
        // Magicdata 本身的默认兼容类型为 POSTGRESQL，但 BES公司在使用时，默认使用 ORACLE
        return SqlCompatibilityType.ORACLE;
    }
}
