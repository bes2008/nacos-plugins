package com.jn.nacos.plugin.datasource.utils;


import com.alibaba.nacos.plugin.datasource.model.MapperResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Where Builder.
 * nacos 早期版本中，没有这个类，所以直接拿过来使用
 */
public final class WhereBuilder {

    /**
     * Base sql.
     */
    private final String sql;

    /**
     * Parameters.
     */
    private final List<Object> parameters = new ArrayList<>();

    /**
     * Where Conditional.
     */
    private final StringBuilder where = new StringBuilder(" WHERE ");

    /**
     * Default Construct.
     *
     * @param sql Sql Script
     */
    public WhereBuilder(String sql) {
        this.sql = sql;
    }

    /**
     * Build AND.
     *
     * @return Return {@link WhereBuilder}
     */
    public WhereBuilder and() {
        where.append(" AND ");
        return this;
    }

    /**
     * Build OR.
     *
     * @return Return {@link WhereBuilder}
     */
    public WhereBuilder or() {
        where.append(" OR ");
        return this;
    }

    /**
     * Build Equals.
     *
     * @param filed Filed name
     * @param parameter Parameters
     * @return Return {@link WhereBuilder}
     */
    public WhereBuilder eq(String filed, Object parameter) {
        where.append(filed).append(" = ? ");
        parameters.add(parameter);
        return this;
    }

    /**
     * Build LIKE.
     *
     * @param filed Filed name
     * @param parameter Parameters
     * @return Return {@link WhereBuilder}
     */
    public WhereBuilder like(String filed, Object parameter) {
        where.append(filed).append(" LIKE ? ");
        parameters.add(parameter);
        return this;
    }

    /**
     * Build IN.
     *
     * @param filed Filed name
     * @param parameterArr Parameters Array
     * @return Return {@link WhereBuilder}
     */
    public WhereBuilder in(String filed, Object[] parameterArr) {
        where.append(filed).append(" IN (");
        for (int i = 0; i < parameterArr.length; i++) {
            if (i != 0) {
                where.append(", ");
            }
            where.append('?');
            parameters.add(parameterArr[i]);
        }
        where.append(") ");
        return this;
    }

    /**
     * Build.
     *
     * @return Return {@link WhereBuilder}
     */
    public MapperResult build() {
        return new MapperResult(sql + where, parameters);
    }
}