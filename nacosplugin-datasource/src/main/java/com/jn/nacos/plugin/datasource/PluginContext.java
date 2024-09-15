package com.jn.nacos.plugin.datasource;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;

public class PluginContext {
    private String databaseName;
    protected NacosDatabaseDialect dialect;
    private IdentifierQuotedMode identifierQuotedModeInDDL;
    private PluginContext(){
        this.databaseName = NacosEnvs.getConfiguredDatabaseName();
        Preconditions.checkTrue(!Objs.equals(DatabaseNames.UNSUPPORTED, this.databaseName), "database {} is unsupported", this.databaseName);
        this.dialect = NacosDatabaseDialectManager.getInstance().getDialect(this.databaseName);
        this.identifierQuotedModeInDDL = NacosEnvs.getConfiguredIdentifierQuotedMode(this.dialect);
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

