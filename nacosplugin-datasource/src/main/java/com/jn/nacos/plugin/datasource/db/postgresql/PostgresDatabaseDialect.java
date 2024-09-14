package com.jn.nacos.plugin.datasource.db.postgresql;

import com.jn.nacos.plugin.datasource.DatabaseNames;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class PostgresDatabaseDialect extends NacosDatabaseDialect {
    public PostgresDatabaseDialect() {
        super(DatabaseNames.POSTGRESQL);
    }

    @Override
    public boolean isAutoCastEmptyStringToNull() {
        // 此测试结果基于 postgresql 15
        return false;
    }
}
