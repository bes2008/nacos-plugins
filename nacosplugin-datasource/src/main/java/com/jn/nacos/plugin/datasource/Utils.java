package com.jn.nacos.plugin.datasource;

import com.alibaba.nacos.common.utils.VersionUtils;
import com.jn.langx.util.Strings;

public class Utils {
    public static int versionCompare(String comparedVersion){
        String currentVersion = toNacosStandardVersion(VersionUtils.version);
        String version2 = toNacosStandardVersion(comparedVersion);
        return VersionUtils.compareVersion(currentVersion, version2);
    }
    private static String toNacosStandardVersion(String version){
        String[] segments = Strings.split(version, ".");
        String nacosVersion = Strings.join(".", segments, 0, 3);
        return nacosVersion;
    }

    /**
     * 内置 数据库插件 （derby, mysql） 是否可以被替换
     * @return 是否可替换
     */
    public static boolean supportsBuiltinDatabasePluginReplaced(){
        return Utils.versionCompare("2.3.0")>=0;
    }

}
