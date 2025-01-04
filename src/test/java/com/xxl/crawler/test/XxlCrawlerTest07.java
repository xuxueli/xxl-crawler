package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.exception.XxlCrawlerException;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.pageparser.PageParser;
import com.xxl.crawler.rundata.RunUrlPool;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例07：
 *      - 爬虫名称：集群方式爬取数据
 *      - 爬虫场景：爬取目标页面数据，通过集群方式进行；集群中多个XxlCrawler共享RunUrlPool，协同扩散URL并消费待采集任务，提升采集效率。
 *      - 实现步骤：
 *          1、RunUrlPool 构建： 构建代理池：可人工构造代理池，或通过系统方式更新维护；
 *          2、定义 XxlCrawler：配置开发爬虫代码；
 *          3、PageParser 开发：通过 “afterParse/后处理逻辑” 获取爬虫输出结果数据；
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest07 {
    private static final Logger logger = LoggerFactory.getLogger(XxlCrawlerTest07.class);

    @Test
    public void test01() {

        /**
         * 1、Redis RunUrlPool 构建：通过Redis共享运行数据来实现分布式爬虫，也可以通过其他方式扩展实现，如DB等。（以伪代码进行讲解）
         *
         * 申请两个 Redis Key：
         *      a、unVisitedUrl：待采集URL池
         *      b、visitedUrl：已采集URL池
         */
        RunUrlPool redisRunData = new RunUrlPool() {

            /**
             * 新增一个待采集的URL，接口需要做URL去重，爬虫线程将会获取到并进行处理；
             * @param url
             */
            @Override
            public boolean addUrl(String url, boolean validUrlRegex){
                /**
                 * TODO：
                 *      jedis.lpushx(unVisitedUrl, link);
                 */
                throw new XxlCrawlerException("not support.");
            }

            /**
             * 获取一个待采集的URL，并且将它从"待采集URL池"中移除，并且添加到"已采集URL池"中；
             */
            @Override
            public String getUrl() {
                /**
                 * TODO:
                 *      String link = jedis.rpop(unVisitedUrl);
                 *      jedis.lpushx(visitedUrl, link);
                 *      return link;
                 */
                throw new XxlCrawlerException("not support.");
            }

            /**
             * 获取待采集URL数量；
             */
            @Override
            public int getUrlNum() {
                /**
                 * TODO:
                 *      int length = jedis.llen(unVisitedUrl);
                 *      return length;
                 */
                throw new XxlCrawlerException("not support.");
            }

        };

        /**
         * 2、XxlCrawler 定义
         */
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setRunUrlPool(redisRunData)
                .setUrls("https://gitee.com/explore/all?order=starred&page=1")                              // 设置爬虫入口URL
                .setWhiteUrlRegexs("^https:\\/\\/gitee\\.com\\/explore\\/all\\?order=starred&page=[1-5]$")  // 设置爬虫扩散的URL白名单正则表达式，控制扩散范围
                .setThreadCount(3)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void afterParse(Response<Object> response) {
                        logger.info(response.getHtml().baseUri() + "：" + response.getHtml().html());
                    }
                })
                .build();

        crawler.start(true);
    }

}
