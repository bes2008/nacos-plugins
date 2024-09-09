package com.jn.nacos.plugin.datasource.db.oracle;

import com.jn.nacos.plugin.datasource.DatabaseIds;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class OracleDatabaseDialect extends NacosDatabaseDialect {
    public OracleDatabaseDialect(){
        super(DatabaseIds.ORACLE);
    }

    @Override
    public boolean isAutoCastEmptyStringToNull() {
        return true;
    }

    @Override
    public String genCastNullToDefaultExpression(String expressionOrIdentifier, String defaultValue) {
        return " NVL("+expressionOrIdentifier+", '"+defaultValue+"') ";
    }
}
