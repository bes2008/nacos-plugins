package com.jn.nacos.plugin.datasource.db.opengauss.dialect;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.dialect.NacosDatabaseDialect;

public class OpenGaussDatabaseDialect extends NacosDatabaseDialect {
    public OpenGaussDatabaseDialect(){
        super(DatabaseTypes.OPENGAUSS);
    }
}
