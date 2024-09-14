package com.jn.nacos.plugin.datasource.db.gauss;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class OpenGaussDatabaseDialect  extends NacosDatabaseDialect {
    public OpenGaussDatabaseDialect() {
        super(DatabaseNames.OPENGAUSS);
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
