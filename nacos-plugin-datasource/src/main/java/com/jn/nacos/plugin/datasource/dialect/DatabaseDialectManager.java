package com.jn.nacos.plugin.datasource.dialect;

import com.alibaba.nacos.common.spi.NacosServiceLoader;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

public class DatabaseDialectManager {
    private static final Logger logger = Loggers.getLogger(DatabaseDialectManager.class);

    private Map<String, DatabaseDialect> dialectMap;

    private DatabaseDialectManager(){
        init();
    }
    private void init(){
        Collection<DatabaseDialect> dialects = NacosServiceLoader.load(DatabaseDialect.class);
        Map<String, DatabaseDialect> map = Maps.newHashMap();
        for (DatabaseDialect dialect : dialects){
            map.put(dialect.getName(), dialect);
        }
        this.dialectMap = map;
    }

    public DatabaseDialect getDialect(String databaseType){
        DatabaseDialect dialect = this.dialectMap.get(databaseType);
        if(dialect==null){
            throw new RuntimeException("unsupported database dialect: " + databaseType);
        }
        return dialect;
    }

    private static final DatabaseDialectManager INSTANCE = new DatabaseDialectManager();

    public static DatabaseDialectManager getInstance(){
        return INSTANCE;
    }
}
