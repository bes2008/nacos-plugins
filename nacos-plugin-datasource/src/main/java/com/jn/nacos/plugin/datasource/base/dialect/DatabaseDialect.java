package com.jn.nacos.plugin.datasource.base.dialect;

public interface DatabaseDialect {
    String getFunction(String functionName);
}
