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
        String url1 = "http://www.baidu.com/";
        String url2 = "http://www.baidu.com";
        String url3 = "https://www.baidu.com/";
        String url4 = "www.baidu.com";
        String url5 = "www.baidu.com/";
        String url6 = "http//www.baidu.com/";
        String url7 = "http:///www.baidu.com/";
        String url8 = "http:/www.baidu.com/";
        Assert.assertTrue(UrlUtil.isUrl(url1));
        Assert.assertTrue(UrlUtil.isUrl(url2));
        Assert.assertTrue(UrlUtil.isUrl(url3));
        Assert.assertFalse(UrlUtil.isUrl(url4));
        Assert.assertFalse(UrlUtil.isUrl(url5));
        Assert.assertFalse(UrlUtil.isUrl(url6));
        Assert.assertFalse(UrlUtil.isUrl(url7));
        Assert.assertFalse(UrlUtil.isUrl(url8));
    }

}
