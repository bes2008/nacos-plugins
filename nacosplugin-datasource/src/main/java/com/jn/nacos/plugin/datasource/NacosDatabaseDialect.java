package com.jn.nacos.plugin.datasource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.reflect.Reflects;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;
import java.util.Map;

public abstract class NacosDatabaseDialect {

    private String name;
    private Dialect delegate;
    private Map<String, String> functionMap;
    protected NacosDatabaseDialect(String name){
        Preconditions.checkNotEmpty(name, "invalid dialect in class {}", Reflects.getFQNClassName(this.getClass()));
        this.name = name;
        String sqlhelperDialect = getCustomizedDialect(this.name);
        this.delegate = DialectRegistry.getInstance().getDialectByName(sqlhelperDialect);
        this.functionMap = initFunctionMap();
    }

    private static String getCustomizedDialect(String dialect){
        String customizedDialectKey = "nacos.database.dialect."+dialect;
        String customizedDialect = System.getProperty(customizedDialectKey);
        if(Strings.isEmpty(customizedDialect)){
            String customizedDialectEnvKey = Strings.replaceChars(customizedDialectKey, '.', '_');
            customizedDialectEnvKey = Strings.upperCase(customizedDialectEnvKey);
            customizedDialect = System.getenv(customizedDialectEnvKey);
        }
        if(Strings.isEmpty(customizedDialect)){
            customizedDialect = dialect;
        }
        return customizedDialect;
    }

    private static Map<String,String> COMMON_FUNCTIONS = defineCommonFunctionMap();

    /**
     * <pre>
     *  -----------------------定义公有函数-------------------------
     *  在这里定义的是公有函数，没有必要必须在所有的数据库中存在，只保证在大部分数据库即可;
     *  并且这里要将所有需要的函数全部定义上
     *  </pre>
     * @return 公有函数
     */
    private static Map<String,String> defineCommonFunctionMap(){
        Map<String,String> map = Maps.newHashMap();
        // 需要获取到毫秒
        map.put("NOW()", "CURRENT_TIMESTAMP(3)");

        return map;
    }

    private Map<String,String> initFunctionMap(){
        Map<String,String> map = Maps.newHashMap();
        map.putAll(COMMON_FUNCTIONS);

        // 使用自定义的函数进行重写
        Map<String,String> specifiedFunctions = specifiedFunctions();
        if(specifiedFunctions!=null){
            for (Map.Entry<String,String> entry : specifiedFunctions.entrySet()){
                if(COMMON_FUNCTIONS.containsKey(entry.getKey())){
                    map.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return map;
    }

    /**
     * -----------------------定义特定函数-------------------------
     * 对于特定的数据库，如果一个函数，与公有函数定义的有所不同时，需要在这里定义
     * @return 特定函数集
     */
    protected Map<String,String> specifiedFunctions(){
        return null;
    }
    public String getFunction(String functionName){
        String func = this.functionMap.get(functionName);
        if(Objs.isEmpty(func)){
            throw new RuntimeException(StringTemplates.formatWithPlaceholder("function {} in {} database dialect is not supported", functionName, this.getName()));
        }
        return func;
    }

    public String getLimitSql(String sql, RowSelection rowSelection){
        return this.delegate.getLimitSql(sql, rowSelection);
    }

    public String getLimitSql(String sql, boolean subQuery, boolean useLimitVariable, RowSelection rowSelection){
        return this.delegate.getLimitSql(sql,useLimitVariable, subQuery, rowSelection);
    }

    public List rebuildParameters(List queryParams, RowSelection selection){
        return rebuildParameters(false, true, queryParams, selection);
    }

    public List rebuildParameters(boolean subquery, boolean useLimitVariable, List queryParams, RowSelection selection){
        return this.delegate.rebuildParameters(subquery,useLimitVariable, selection, queryParams);
    }

    private static String IDENTIFIER_BEFORE_QUOTES="\"'`[";
    private static String IDENTIFIER_AFTER_QUOTES="\"'`]";


    public List<String> wrapQuotes(List<String> identifiers){
        return Pipeline.of(identifiers).map(new Function<String, String>() {
            @Override
            public String apply(String identifier) {
                return wrapQuote(identifier);
            }
        }).asList();
    }

    private String wrapQuote(String identifier){
        return this.delegate.getQuotedIdentifier(removeQuote(identifier));
    }

    @Deprecated
    private final List<String> removeQuote(List<String> identifiers){
        return Pipeline.of(identifiers).map(new Function<String, String>() {
            @Override
            public String apply(String identifier) {
                return removeQuote(identifier);
            }
        }).asList();
    }

    private static String removeQuote(String identifier){
        if(Strings.isBlank(identifier)){
            return identifier;
        }
        identifier=Strings.stripStart(identifier, IDENTIFIER_BEFORE_QUOTES);
        identifier=Strings.stripEnd(identifier, IDENTIFIER_AFTER_QUOTES);
        return identifier;
    }

    public boolean isAutoCastEmptyStringToNull(){
        return false;
    }

    public String genCastNullToDefaultExpression(String expressionOrIdentifier,@Nullable String defaultValue){
        return expressionOrIdentifier;
    }

    public String getName() {
        return name;
    }
}
