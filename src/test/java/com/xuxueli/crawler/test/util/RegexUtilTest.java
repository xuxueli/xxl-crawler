package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.RegexUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * regex tool test
 *
 * @author xuxueli 2017-10-10 15:30:29
 */
public class RegexUtilTest {


    /**
     * regex match
     */
    @Test
    public void matchesTest(){
        String regex = "https://my\\.oschina\\.net/xuxueli/blog/\\d+";
        String url = "https://my.oschina.net/xuxueli/blog/690978";

        boolean ret = RegexUtil.matches(regex, url);
        Assert.assertTrue(ret);
    }

    /**
     * url格式校验
     */
    @Test
    public void isUrlTest(){
        String url = "http://www.baidu.com/";

        boolean ret = RegexUtil.isUrl(url);
        Assert.assertTrue(ret);
    }

}
