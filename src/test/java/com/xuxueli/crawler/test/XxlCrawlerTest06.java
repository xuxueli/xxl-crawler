package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.rundata.RunData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 爬虫示例06：分布式爬虫示例
 *
 * @author xuxueli 2018-02-08 16:56:46
 */
public class XxlCrawlerTest06 {

    public static void main(String[] args) {

        /**
         * Redis RunData：通过Redis共享运行数据来实现分布式爬虫，也可以通过其他方式扩展实现，如DB等。（以伪代码进行讲解）
         *
         * 申请两个 Redis Key：
         *
         *      unVisitedUrl：待采集URL池
         *      visitedUrl：已采集URL池
         *
         */
        RunData redisRunData = new RunData() {

            /**
             * 新增一个待采集的URL，接口需要做URL去重，爬虫线程将会获取到并进行处理；
             *
             * @param link
             */
            @Override
            public boolean addUrl(String link) {

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
                .setRunData(redisRunData)
                .setUrls("https://gitee.com/xuxueli0323/projects?page=1")
                .setWhiteUrlRegexs("https://gitee\\.com/xuxueli0323/projects\\?page=\\d+")
                .setThreadCount(3)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, Object pageVo) {
                        String pageUrl = html.baseUri();
                        System.out.println(pageUrl + "：" + html.html());
                    }
                })
                .build();

        System.out.println("start");
        crawler.start(true);
        System.out.println("end");
    }

}
