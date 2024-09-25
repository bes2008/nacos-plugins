package com.jn.nacos.plugin.datasource.db.oracle;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;

public class OracleDatabaseDialect extends NacosDatabaseDialect {
    public OracleDatabaseDialect(){
        super(DatabaseNames.ORACLE);
    }

    @Override
    public boolean isAutoCastEmptyStringToNull(SqlCompatibilityType sqlCompatibilityType) {
        return true;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " NVL("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }
}
