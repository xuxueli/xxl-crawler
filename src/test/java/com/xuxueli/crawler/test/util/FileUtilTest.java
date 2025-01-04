package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.constant.Const;
import com.xuxueli.crawler.util.FileUtil;
import org.junit.jupiter.api.Test;

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
    public void saveFileTest() {

        String htmlData = "<html>Hello world.</html>";
        String filePath = "/Users/xuxueli/Downloads/tmp";
        String fileName = FileUtil.getFileNameByUrl("http://www.baidu.com/",	Const.CONTENT_TYPE_HTML);

        FileUtil.saveFile(htmlData, filePath, fileName);
    }

}

