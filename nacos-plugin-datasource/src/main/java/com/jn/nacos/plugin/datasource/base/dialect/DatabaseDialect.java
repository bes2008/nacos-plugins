package com.jn.nacos.plugin.datasource.base.dialect;

import com.jn.sqlhelper.dialect.Dialect;
import com.jn.sqlhelper.dialect.SQLDialectException;
import com.jn.sqlhelper.dialect.pagination.PagedPreparedParameterSetter;
import com.jn.sqlhelper.dialect.pagination.QueryParameters;
import com.jn.sqlhelper.dialect.pagination.RowSelection;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class DatabaseDialect {

    public abstract String getDatabase();
    public abstract String getFunction(String functionName);

    protected Dialect dialect;

    private int bindLimitParametersAtStartOfQuery(RowSelection paramRowSelection, List params, int paramInt){

    }

    public int bindLimitParametersAtEndOfQuery(RowSelection paramRowSelection, List params, int paramInt){

    }

    public String getLimitSql(String sql, RowSelection rowSelection){
        return this.dialect.getLimitSql(sql, rowSelection);
    }

}
