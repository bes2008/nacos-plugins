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
}
