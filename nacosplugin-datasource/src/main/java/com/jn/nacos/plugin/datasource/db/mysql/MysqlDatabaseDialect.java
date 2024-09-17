package com.jn.nacos.plugin.datasource.db.mysql;

import com.alibaba.nacos.plugin.datasource.enums.mysql.TrustedMysqlFunctionEnum;
import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.IdentifierQuotedMode;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;


public class MysqlDatabaseDialect extends NacosDatabaseDialect {
    public MysqlDatabaseDialect() {
        super(DatabaseNames.MYSQL);
        this.identifierQuotedMode = IdentifierQuotedMode.QUOTED;
    }

    @Override
    public String getFunction(String functionName) {
        return TrustedMysqlFunctionEnum.getFunctionByName(functionName);
    }
}
