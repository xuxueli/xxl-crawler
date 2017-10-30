package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.PageDownLoader;
import org.junit.Test;

/**
 * page downloader test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class PageDownLoaderTest {

    /**
     * 生成Html本地文件
     */
    @Test
    public void saveHtml() {

        byte[] htmlData = "<html>Hello world.</html>".getBytes();
        String filePath = "/Users/xuxueli/Downloads";
        String fileName = PageDownLoader.getFileNameByUrl("http://www.baidu.com/",	"text/html");

        PageDownLoader.saveHtml(htmlData, filePath, fileName);
    }

}

