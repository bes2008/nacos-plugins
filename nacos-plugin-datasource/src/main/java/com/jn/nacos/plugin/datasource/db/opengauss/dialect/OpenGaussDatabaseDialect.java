package com.jn.nacos.plugin.datasource.db.opengauss.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.DatabaseDialect;

public class OpenGaussDatabaseDialect extends DatabaseDialect {
    public OpenGaussDatabaseDialect(){
        super(DatabaseTypes.OPENGAUSS);
    }
}
