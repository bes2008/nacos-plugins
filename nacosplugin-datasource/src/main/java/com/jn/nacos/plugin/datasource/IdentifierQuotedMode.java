package com.jn.nacos.plugin.datasource;

/**
 * 表名、字段名是否有使用过引号
 */
public enum IdentifierQuotedMode {
    /**
     * 全部使用引号
     */
    QUOTED,
    /**
     * 全部不使用引号
     */
    UNQUOTED,
    // 混合模式
    MIXED
}
