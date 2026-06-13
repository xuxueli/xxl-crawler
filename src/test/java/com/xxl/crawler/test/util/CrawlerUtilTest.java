package com.xxl.crawler.test.util;

import com.xxl.crawler.util.CrawlerUtil;
import com.xxl.tool.http.http.enums.ContentType;
import com.xxl.tool.io.FileTool;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

/**
 * page downloader test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class CrawlerUtilTest {

    /**
     * 生成Html本地文件
     */
    @Test
    public void generateFileNameWithUrl_test() throws IOException {

        String htmlData = "<html>Hello world.</html>";
        String filePath = "/Users/admin/Downloads/tmp";
        String fileName = CrawlerUtil.generateFileNameWithUrl("http://www.baidu.com/",	ContentType.TEXT_HTML.getValue());

        String finalFilePath = Path.of(filePath, fileName).toString();

        // 2.2、下载Html文件
        FileTool.writeString(finalFilePath, htmlData);
    }

}

