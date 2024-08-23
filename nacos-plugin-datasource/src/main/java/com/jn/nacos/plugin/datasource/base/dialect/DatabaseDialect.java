package com.jn.nacos.plugin.datasource.base.dialect;

import com.google.common.collect.Maps;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;
import com.jn.sqlhelper.dialect.instrument.Instrumentations;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;
import java.util.Map;

public abstract class DatabaseDialect {

    private String name;
    private Dialect delegate;
    private Map<String, String> functionMap;
    public DatabaseDialect(String name){
        this.name = name;
        this.delegate = DialectRegistry.getInstance().getDialectByName(name);
        this.functionMap = initFunctionMap();
    }

    protected Map<String,String> initFunctionMap(){
        Map<String,String> map = Maps.newHashMap();
        map.put("NOW()", "CURRENT_TIMESTAMP");
        return map;
    }
    public String getFunction(String functionName){
        String func =this.functionMap.get(functionName);
        if(Objs.isEmpty(func)){
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("function {} in {} database dialect is not supported", functionName, this.getName()));
        }
        return func;
    }

    public String getLimitSql(String sql, RowSelection rowSelection){
        return this.delegate.getLimitSql(sql, rowSelection);
    }

    public List rebuildParameters(List queryParams, RowSelection selection){
        return Instrumentations.rebuildParameters(this.delegate, queryParams, selection);
    }

    public String getName() {
        return name;
    }
}