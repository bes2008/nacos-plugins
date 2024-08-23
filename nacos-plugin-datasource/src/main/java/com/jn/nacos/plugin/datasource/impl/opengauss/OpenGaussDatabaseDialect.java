package com.jn.nacos.plugin.datasource.impl.opengauss;

import com.jn.nacos.plugin.datasource.base.dialect.DatabaseDialect;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;

public class OpenGaussDatabaseDialect implements DatabaseDialect {

    private Dialect dialect;
    private String database;
    public OpenGaussDatabaseDialect(String database){
        this.dialect = DialectRegistry.getInstance().getDialectByName(database);
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public String getFunction(String functionName) {
        return null;
    }
}
