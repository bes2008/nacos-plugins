package com.jn.nacos.plugin.datasource.db.gauss;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;

public class OpenGaussDatabaseDialect  extends NacosDatabaseDialect {
    public OpenGaussDatabaseDialect() {
        super(DatabaseNames.OPENGAUSS);
        this.identifierQuotedMode = IdentifierQuotedMode.QUOTED;
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
