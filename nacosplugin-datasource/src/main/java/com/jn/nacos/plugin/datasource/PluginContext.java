package com.jn.nacos.plugin.datasource;

import com.jn.langx.lifecycle.AbstractInitializable;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.logging.Loggers;
import com.jn.sqlhelper.dialect.SqlCompatibilityType;
import org.slf4j.Logger;

public class PluginContext extends AbstractInitializable {
    private String databaseName;
    protected NacosDatabaseDialect dialect;
    private IdentifierQuotedMode identifierQuotedModeInDDL;
    private SqlCompatibilityType sqlCompatibilityType;

    private PluginContext(){
       init();
    }

    @Override
    protected void doInit() throws InitializationException {
        Logger logger = Loggers.getLogger(PluginContext.class);
        logger.info("================== nacos datasource plugin context initial==================");
        this.databaseName = NacosEnvs.getConfiguredDatabaseName();
        Preconditions.checkTrue(!Objs.equals(DatabaseNames.UNSUPPORTED, this.databaseName), "database {} is unsupported", this.databaseName);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseName);
        this.identifierQuotedModeInDDL = NacosEnvs.getIdentifierQuotedMode(this.dialect);
        this.sqlCompatibilityType = NacosEnvs.getSqlCompatibilityType(this.dialect);
        logger.info("dialect: {}, identifierQuotedMode: {}, sqlCompatibilityType: {}", this.databaseName, this.identifierQuotedModeInDDL, this.sqlCompatibilityType.getName());
        logger.info("================== nacos datasource plugin context initial finished ==================");
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

    public SqlCompatibilityType getSqlCompatibilityType() {
        return sqlCompatibilityType;
    }
}

