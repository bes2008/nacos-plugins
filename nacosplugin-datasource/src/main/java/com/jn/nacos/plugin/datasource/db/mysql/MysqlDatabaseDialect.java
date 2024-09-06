package com.jn.nacos.plugin.datasource.db.mysql;

import com.alibaba.nacos.plugin.datasource.enums.mysql.TrustedMysqlFunctionEnum;
import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;


public class MysqlDatabaseDialect extends NacosDatabaseDialect {
    public MysqlDatabaseDialect() {
        super(DatabaseTypes.MYSQL);
    }

    @Override
    public String getFunction(String functionName) {
        return TrustedMysqlFunctionEnum.getFunctionByName(functionName);
    }
}
