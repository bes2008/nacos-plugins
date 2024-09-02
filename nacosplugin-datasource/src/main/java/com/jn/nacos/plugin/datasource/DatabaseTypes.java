package com.jn.nacos.plugin.datasource;

import com.alibaba.nacos.plugin.datasource.constants.DataSourceConstant;

public class DatabaseTypes extends DataSourceConstant {
    public static final String OPENGAUSS = "opengauss";
    public static final String KINGBASE = "kingbase";
    public static final String MAGICDATA = "magicdata";
    public static final String ORACLE = "oracle";
    public static final String DAMENG = "dm";
    public static final String MSSQL = "sqlserver";
    public static final String POSTGRESQL = "postgresql";
    public static final String OSCAR = "oscar";

    /**
     * 当 spring.sql.init.platform 配置没有配置，或者配置了 derby, mysql 时，都会将 自定义的插件设置为 undefined
     */
    public static final String UNDEFINED = "undefined";
    /**
     * 当spring.sql.init.platform 配置了一个没有支持的数据库时，则会被命名为 unsupported
     */
    public static final String UNSUPPORTED = "unsupported";
}
