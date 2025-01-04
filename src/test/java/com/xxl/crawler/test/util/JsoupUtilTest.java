package com.xxl.crawler.test.util;

import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Request;
import com.xxl.crawler.util.JsoupUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
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

        Document html = JsoupUtil.load(new Request(url, null, null, null,
                Const.USER_AGENT_CHROME, null, false, Const.TIMEOUT_MILLIS_DEFAULT, false, null));
        logger.info(html.html());
    }

    /**
     * 获取页面上所有超链接地址
     */
    @Test
    public void findLinksTest() {
        String url = "http://www.baidu.com/";

        Document html = JsoupUtil.load(new Request(url, null, null, null,
                Const.USER_AGENT_CHROME, null, false, Const.TIMEOUT_MILLIS_DEFAULT, false,null));
        Set<String> linkList = JsoupUtil.findLinks(html);

        logger.info("link num {}", linkList.size());
        if (linkList!=null && linkList.size() > 0) {
            for (String link : linkList) {
                logger.info(link);
            }
        }

    }

    /**
     * 获取页面上所有图片地址
     */
    @Test
    public void findImagesTest() {
        String url = "http://www.baidu.com/";

        Document html = JsoupUtil.load(new Request(url, null, null, null,
                Const.USER_AGENT_CHROME, null, false, Const.TIMEOUT_MILLIS_DEFAULT, false,null));
        Set<String> linkList = JsoupUtil.findImages(html);

        logger.info("images num {}", linkList.size());
        if (linkList!=null && linkList.size() > 0) {
            for (String link : linkList) {
                logger.info(link);
            }
        }

    }

}
