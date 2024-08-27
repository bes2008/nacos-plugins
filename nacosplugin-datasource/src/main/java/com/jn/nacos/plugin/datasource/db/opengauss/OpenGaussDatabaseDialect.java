package com.jn.nacos.plugin.datasource.db.opengauss;

import com.jn.nacos.plugin.datasource.DatabaseTypes;
import com.jn.nacos.plugin.datasource.NacosDatabaseDialect;

public class OpenGaussDatabaseDialect extends NacosDatabaseDialect {
    public OpenGaussDatabaseDialect(){
        super(DatabaseTypes.OPENGAUSS);
    }
}
