package com.jn.nacos.plugin.datasource.db.derby;

import com.alibaba.nacos.plugin.datasource.enums.derby.TrustedDerbylFunctionEnum;
import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class DerbyDatabaseDialect extends NacosDatabaseDialect {
    public DerbyDatabaseDialect() {
        super(DatabaseNames.DERBY);
    }

    @Override
    public String getFunction(String functionName) {
        return TrustedDerbylFunctionEnum.getFunctionByName(functionName);
    }
}
