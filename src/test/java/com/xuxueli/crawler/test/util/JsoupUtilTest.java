package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.JsoupUtil;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * jsoup tool test
 *
 * @author xuxueli 2017-10-09 17:47:13
 */
public class JsoupUtilTest {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtilTest.class);

        /**
     * 加载解析页面
     */
    @Test
    public void loadParseTest(){
        String url = "http://www.baidu.com/";

        // 组装规则
        Map<Integer, Set<String>> tagMap = new HashMap<Integer, Set<String>>();
        tagMap.put(0, new HashSet<String>(Arrays.asList("a[href]")));

        // 加载解析html
        Elements resultAll = JsoupUtil.loadParse(url, null, null, false, tagMap);
        logger.info(resultAll.toString());
    }

    /**
     * 获取页面上所有超链接地址
     */
    @Test
    public void findLinksTest() {
        String url = "http://www.baidu.com/";

        Set<String> linkList = JsoupUtil.findLinks(url);
        logger.info("link num {}", linkList.size());

        if (linkList!=null && linkList.size() > 0) {
            for (String link : linkList) {
                logger.info(link);
            }
        }

    }
}
