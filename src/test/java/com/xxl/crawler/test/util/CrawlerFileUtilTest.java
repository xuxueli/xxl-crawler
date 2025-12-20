package com.xxl.crawler.test.util;

import com.xxl.crawler.constant.Const;
import com.xxl.crawler.util.CrawlerFileUtil;
import org.junit.jupiter.api.Test;

/**
 * page downloader test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class CrawlerFileUtilTest {

    /**
     * 生成Html本地文件
     */
    @Test
    public void saveFileTest() {

        String htmlData = "<html>Hello world.</html>";
        String filePath = "/Users/xuxueli/Downloads/tmp";
        String fileName = CrawlerFileUtil.getFileNameByUrl("http://www.baidu.com/",	Const.CONTENT_TYPE_HTML);

        CrawlerFileUtil.saveFile(htmlData, filePath, fileName);
    }

}

