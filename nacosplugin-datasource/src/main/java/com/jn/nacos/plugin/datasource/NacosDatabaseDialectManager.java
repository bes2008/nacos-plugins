package com.jn.nacos.plugin.datasource;

import com.alibaba.nacos.common.spi.NacosServiceLoader;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Map;

public class NacosDatabaseDialectManager {
    private static final Logger logger = Loggers.getLogger(NacosDatabaseDialectManager.class);

    private Map<String, NacosDatabaseDialect> dialectMap;

    private NacosDatabaseDialectManager(){
        init();
    }
    private void init(){
        Collection<NacosDatabaseDialect> dialects = NacosServiceLoader.load(NacosDatabaseDialect.class);
        Map<String, NacosDatabaseDialect> map = Maps.newHashMap();
        for (NacosDatabaseDialect dialect : dialects){
            map.put(dialect.getName(), dialect);
        }
        this.dialectMap = map;
    }

    public NacosDatabaseDialect getDialect(String databaseType){
        NacosDatabaseDialect dialect = this.dialectMap.get(databaseType);
        if(dialect==null){
            dialect = new DefaultNacosDatabaseDialect(databaseType);
        }
        return dialect;
    }

    private static final NacosDatabaseDialectManager INSTANCE = new NacosDatabaseDialectManager();

    public static NacosDatabaseDialectManager getInstance(){
        return INSTANCE;
    }
}
