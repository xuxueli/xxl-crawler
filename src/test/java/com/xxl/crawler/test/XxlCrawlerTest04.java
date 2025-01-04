package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.parser.PageParser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例04：
 *      - 爬虫名称：百度新闻爬虫
 *      - 爬虫场景：爬取非Web页面，本案例为JSON接口，直接输出响应数据
 *      - 实现步骤：
 *          1、XxlCrawler 开发：一行代码定义爬虫，可设置超时、Cookie、Header等请求附属信息。
 *          2、PageParser 开发：通过 “afterParse/后处理” 获取数据结果。
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest04 {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerTest04.class);

    /**
     * 1、定义 XxlCrawler
     */
    @Test
    public void test() {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("http://news.baidu.com/widget?id=LocalNews&ajax=json")
                .setTimeoutMillis(5000)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void afterParse(Response<Object> response) {
                        /**
                         * 2、PageParser 开发
                         */
                        logger.info(response.getRequest().getUrl() + ": " + response.getHtml().outerHtml());
                    }
                })
                .build();

        crawler.start(true);
    }

}
