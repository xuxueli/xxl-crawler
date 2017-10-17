package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.PageDownLoader;
import org.junit.Assert;
import org.junit.Test;

/**
 * page downloader test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class PageDownLoaderTest {

    /**
     * 页面文件下载
     */
    @Test
    public void downloadFile() {
        String url = "http://www.baidu.com/";
        String filePath = "/Users/xuxueli/Downloads";
        boolean ret = PageDownLoader.downloadFile(url, filePath);
        Assert.assertTrue(ret);
    }

}

