package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.UrlUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * url tool test
 *
 * @author xuxueli 2017-10-10 14:58:21
 */
public class UrlUtilTest {

    /**
     * url格式校验
     */
    @Test
    public void isUrlTest(){
        String url = "http://www.baidu.com/";

        boolean ret = UrlUtil.isUrl(url);
        Assert.assertTrue(ret);
    }

}
