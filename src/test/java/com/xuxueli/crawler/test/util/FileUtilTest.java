package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.FileUtil;
import org.junit.Test;

/**
 * page downloader test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class FileUtilTest {

    /**
     * 生成Html本地文件
     */
    @Test
    public void saveHtml() {

        byte[] htmlData = "<html>Hello world.</html>".getBytes();
        String filePath = "/Users/xuxueli/Downloads/tmp";
        String fileName = FileUtil.getFileNameByUrl("http://www.baidu.com/",	"text/html");

        FileUtil.saveFile(htmlData, filePath, fileName);
    }

}

