package com.jn.nacos.plugin.datasource.tests;

import com.alibaba.nacos.common.utils.VersionUtils;
import com.jn.langx.util.Strings;
import org.junit.Test;

public class VersionTest {

    @Test
    public void test(){
        String currentVersion = "2.4.1.1";
        String[] segments = Strings.split(currentVersion, ".");
        String nacosVersion = Strings.join(".", segments, 0, 3);
        boolean gte= VersionUtils.compareVersion(nacosVersion, "2.1.0")>=0;
        System.out.println(gte);
    }


}
