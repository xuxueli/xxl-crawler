package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.parser.PageParser;
import com.xxl.crawler.rundata.RunUrlPool;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例：分布式用法
 *
 * @author xuxueli 2018-02-08 16:56:46
 */
public class XxlCrawlerRedisTest {
    private static final Logger logger = LoggerFactory.getLogger(XxlCrawlerRedisTest.class);

    /**
     * 爬虫示例01：分页爬取页面 + Redis结合方式
     */
    @Test
    public void test01() {

        /**
         * Redis RunData：通过Redis共享运行数据来实现分布式爬虫，也可以通过其他方式扩展实现，如DB等。（以伪代码进行讲解）
         *
         * 申请两个 Redis Key：
         *
         *      unVisitedUrl：待采集URL池
         *      visitedUrl：已采集URL池
         *
         */
        RunUrlPool redisRunData = new RunUrlPool() {

            /**
             * 新增一个待采集的URL，接口需要做URL去重，爬虫线程将会获取到并进行处理；
             *
             * @param url
             */
            @Override
            public boolean addUrl(String url, boolean validUrlRegex){

                /**
                 * TODO:
                 *
                 * jedis.lpushx(unVisitedUrl, link);
                 *
                 */
                return true;
            }

            /**
             * 获取一个待采集的URL，并且将它从"待采集URL池"中移除，并且添加到"已采集URL池"中；
             */
            @Override
            public String getUrl() {

                /**
                 * TODO:
                 *
                 * String link = jedis.rpop(unVisitedUrl);
                 * jedis.lpushx(visitedUrl, link);
                 *
                 * return link;
                 */
                return null;
            }

            /**
             * 获取待采集URL数量；
             */
            @Override
            public int getUrlNum() {

                /**
                 * TODO:
                 *
                 * int length = jedis.llen(unVisitedUrl);
                 * return length;
                 */
                return 0;
            }

        };

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setRunUrlPool(redisRunData)
                .setUrls("https://gitee.com/xuxueli0323/projects?page=1")
                .setWhiteUrlRegexs("https://gitee\\.com/xuxueli0323/projects\\?page=\\d+")
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
