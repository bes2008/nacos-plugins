package com.jn.nacos.plugin.datasource;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.reflect.Reflects;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.util.List;
import java.util.Map;

public abstract class NacosDatabaseDialect {

    private String name;
    private Dialect delegate;
    private Map<String, String> functionMap;

    public Dialect getDelegate() {
        return delegate;
    }

    /**
     * 插件中提供在 schema DDL 文件中， identifier 的默认模式
     */
    protected IdentifierQuotedMode identifierQuotedMode;

    protected NacosDatabaseDialect(String name){
        Preconditions.checkNotEmpty(name, "invalid dialect in class {}", Reflects.getFQNClassName(this.getClass()));
        this.name = name;
        String sqlhelperDialect = getCustomizedDialect(this.name);
        this.delegate = DialectRegistry.getInstance().gaussDialect(sqlhelperDialect);
        this.functionMap = initFunctionMap();
        this.identifierQuotedMode = IdentifierQuotedMode.UNQUOTED;
    }

    /**
     * @return 获取DDL文件中相关语句的引号模式
     */
    public IdentifierQuotedMode getPluginProvidedDDLIdentifierQuotedMode(){
        return identifierQuotedMode;
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

    /**
     * 获取函数
     * @param functionName 函数名
     * @return 返回数据库特定的函数形态
     */
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

    /**
     * 将 limit, offset等加入到 queryParams中
     * @param queryParams 原始的参数
     * @param selection row selection 对象
     * @return 拼装后的参数列表
     */
    public List rebuildParameters(List queryParams, RowSelection selection){
        return rebuildParameters(false, true, queryParams, selection);
    }

    /**
     * 将 limit, offset等加入到 queryParams中
     * @param queryParams 原始的参数
     * @param selection row selection 对象
     * @return 拼装后的参数列表
     */
    public List rebuildParameters(boolean subquery, boolean useLimitVariable, List queryParams, RowSelection selection){
        return this.delegate.rebuildParameters(subquery,useLimitVariable, selection, queryParams);
    }

    /**
     * 对表名、字段名进行引号包装
     * @param identifier 表名、字段名
     * @param identifierCase 大小写形式
     * @return 包装后的值
     */
    public String wrapQuote(String identifier, Dialect.IdentifierCase identifierCase){
        // 数据库表名、列名的大小写，由 dialect 内部实现
        return this.delegate.getQuotedIdentifier(identifier, identifierCase);
    }

    /**
     * 对表名、字段名进行引号去包装
     * @param identifier 表名、字段名
     * @return 返回去包装后的值
     */
    public String unwrapQuote(String identifier){
        return this.delegate.getUnquoteIdentifier(identifier);
    }

    /**
     * 通常是在 ORACLE 兼容模式下，JDBC驱动会自动将参数中的 空字符串""转换成 NULL。
     */
    public boolean isAutoCastEmptyStringToNull(SqlCompatibilityType sqlCompatibilityType){
        return sqlCompatibilityType == SqlCompatibilityType.ORACLE;
    }

    /**
     * 为了应对存入 "" 字符串时，被当作 null 处理的情况。
     * 当不希望""被处理成null时，可以使用这个方法，将"" 转换成默认值。
     */
    public String genCastNullToDefaultExpression(String expressionOrIdentifier,@Nullable String defaultValue){
        return expressionOrIdentifier;
    }

    /**
     * @return 获取数据库方言名称
     */
    public String getName() {
        return name;
    }

    /**
     * @return 获取数据库的默认的兼容模式
     */
    public SqlCompatibilityType getDefaultCompatibilityType(){
        return delegate.getDefaultSqlCompatibilityType();
    }
}
