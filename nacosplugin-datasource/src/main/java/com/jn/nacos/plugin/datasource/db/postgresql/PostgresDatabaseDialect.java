package com.jn.nacos.plugin.datasource.db.postgresql;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;

public class PostgresDatabaseDialect extends NacosDatabaseDialect {
    public PostgresDatabaseDialect() {
        super(DatabaseNames.POSTGRESQL);
        this.identifierQuotedMode = IdentifierQuotedMode.QUOTED;
    }

    @Override
    public boolean isAutoCastEmptyStringToNull(SqlCompatibilityType sqlCompatibilityType) {
        // 此测试结果基于 postgresql 15
        return false;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " COALESCE("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }
}
