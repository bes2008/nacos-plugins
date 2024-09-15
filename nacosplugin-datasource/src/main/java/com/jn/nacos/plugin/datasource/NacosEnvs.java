package com.jn.nacos.plugin.datasource;

import com.alibaba.nacos.common.utils.VersionUtils;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;
import org.slf4j.Logger;

public class NacosEnvs {
    public static int versionCompare(String comparedVersion){
        String currentVersion = toNacosStandardVersion(VersionUtils.version);
        String version2 = toNacosStandardVersion(comparedVersion);
        return VersionUtils.compareVersion(currentVersion, version2);
    }
    private static String toNacosStandardVersion(String version){
        String[] segments = Strings.split(version, ".");
        String nacosVersion = Strings.join(".", segments, 0, 3);
        return nacosVersion;
    }

    /**
     * 内置 数据库插件 （derby, mysql） 是否可以被替换
     * @return 是否可替换
     */
    public static boolean supportsBuiltinDatabasePluginReplaced(){
        return versionCompare("2.3.0")>=0;
    }

    public static boolean hasEncryptedDataKeyColumn(){
        return versionCompare("2.1.0")>=0;
    }

    /**
     * 当使用的 create-schema.sql, create-tables.sql 不是插件提供的，需要指定该配置。
     */
    public static IdentifierQuotedMode getConfiguredIdentifierQuotedMode(NacosDatabaseDialect dialect){
        String modeString = EnvUtil.getProperty("db.sql.identifier.quoted.mode");
        IdentifierQuotedMode mode = null;
        if(Strings.isNotBlank(modeString)){
            mode = Enums.ofName(IdentifierQuotedMode.class, modeString);
        }
        if(mode==null){
            mode = dialect.getPluginProvidedDDLIdentifierQuotedMode();
        }
        if(mode==null){
            mode = IdentifierQuotedMode.QUOTED;
        }
        return mode;
    }

    public static String getConfiguredDatabaseName(){
        String databaseName = EnvUtil.getProperty("spring.sql.init.platform");
        if(Strings.isBlank(databaseName)){
            // 这个是 nacos 中更早的配置 datasource 类型的方式
            databaseName = EnvUtil.getProperty("spring.datasource.platform");
        }
        if(Strings.isBlank(databaseName)){
            // 内嵌数据库 derby
            if (EnvUtil.getStandaloneMode()){
                databaseName = DatabaseNames.DERBY;
            }
            else{ // 默认数据库 MySQL
                databaseName = DatabaseNames.MSSQL;
            }
        }else{
            Dialect dialect = DialectRegistry.getInstance().gaussDialect(databaseName);
            if(dialect==null){
                databaseName = DatabaseNames.UNSUPPORTED;
            }
        }

        if(Strings.isBlank(databaseName)){
            databaseName = DatabaseNames.UNDEFINED;
        }

        if(Objs.equals(DatabaseNames.DERBY, databaseName) || Objs.equals(DatabaseNames.MYSQL, databaseName)){
            // 只要不是false|False 等，就是禁用，默认值为 true
            boolean builtinDatasourcePluginEnabled = NacosEnvs.supportsBuiltinDatabasePluginReplaced() && (!Strings.equalsIgnoreCase(EnvUtil.getProperty("spring.sql.plugin.builtin.enabled","true"),"false")) ;

            // 自定义的插件会优先于 内置的 derby, mysql 插件
            // 放到 MapperManager 中使用了 map#putIfAbsent，所以要启用 内置的 derby, mysql，必须保证 自定义的插件名字不能是 mysql,derby
            Logger logger = Loggers.getLogger(NacosEnvs.class);
            logger.info("nacos builtin datasource plugin is {}", builtinDatasourcePluginEnabled?"enabled":"disabled");
            if(builtinDatasourcePluginEnabled) {
                databaseName = DatabaseNames.UNDEFINED;
                logger.info("rename the customized mapper {} to {}", Reflects.getFQNClassName(NacosEnvs.class), databaseName);
            }
        }
        return databaseName;
    }

}
