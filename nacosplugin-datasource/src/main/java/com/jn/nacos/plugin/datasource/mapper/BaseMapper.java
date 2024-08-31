package com.jn.nacos.plugin.datasource.mapper;

import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.sys.env.EnvUtil;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialectManager;
import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.DialectRegistry;

public abstract class BaseMapper extends AbstractMapper {
    private final String databaseId;
    protected NacosDatabaseDialect dialect;

    protected BaseMapper() {
        this.databaseId = getConfiguredDatabaseId();
        Preconditions.checkTrue(!Objs.equals(DatabaseTypes.UNSUPPORTED, this.databaseId), "database {} is unsupported", this.databaseId);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseId);
    }

    private String getConfiguredDatabaseId(){
        String databaseId = EnvUtil.getProperty("spring.sql.init.platform");
        if(Strings.isBlank(databaseId)){
            // 内嵌数据库 derby
            if (EnvUtil.getStandaloneMode()){
                databaseId = DatabaseTypes.DERBY;
            }
            else{ // 默认数据库 MySQL
                databaseId = DatabaseTypes.MSSQL;
            }
        }else{
            Dialect dialect = DialectRegistry.getInstance().getDialectByName(databaseId);
            if(dialect==null){
                databaseId = DatabaseTypes.UNSUPPORTED;
            }
        }
        return databaseId;
    }


    @Override
    public String getDataSource() {
        return databaseId;
    }

    public NacosDatabaseDialect getDialect() {
        return dialect;
    }

    @Override
    public String getFunction(String functionName) {
        return this.dialect.getFunction(functionName);
    }
}
