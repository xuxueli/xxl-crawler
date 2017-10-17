package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;

import java.util.Arrays;
import java.util.HashSet;

/**
 * xxl crawler test
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest {

    public static void main(String[] args) {
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setIndexUrl("https://my.oschina.net/xuxueli/blog")
                .setThreadCount(3)
                .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
                .build();

        System.out.println(111);
        crawler.start();
        System.out.println(222);
    }

}
