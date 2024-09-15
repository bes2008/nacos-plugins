package com.jn.nacos.plugin.datasource;

import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

public class PluginContext extends AbstractInitializable {
    private String databaseName;
    protected NacosDatabaseDialect dialect;
    private IdentifierQuotedMode identifierQuotedModeInDDL;
    private PluginContext(){
       init();
    }

    @Override
    protected void doInit() throws InitializationException {
        Logger logger = Loggers.getLogger(PluginContext.class);
        logger.info("==================nacos datasource plugin context initial==================");
        this.databaseName = NacosEnvs.getConfiguredDatabaseName();
        Preconditions.checkTrue(!Objs.equals(DatabaseNames.UNSUPPORTED, this.databaseName), "database {} is unsupported", this.databaseName);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseName);
        this.identifierQuotedModeInDDL = NacosEnvs.getConfiguredIdentifierQuotedMode(this.dialect);
        logger.info("dialect: {}, identifierQuotedMode: {}", this.databaseName, this.identifierQuotedModeInDDL);
        logger.info("==================nacos datasource plugin context initial finished ==================");
    }

    public static final PluginContext INSTANCE = new PluginContext();

    public String getDatabaseName() {
        return databaseName;
    }

    public IdentifierQuotedMode getIdentifierQuotedModeInDDL() {
        return identifierQuotedModeInDDL;
    }

    public NacosDatabaseDialect getDialect() {
        return dialect;
    }
}

