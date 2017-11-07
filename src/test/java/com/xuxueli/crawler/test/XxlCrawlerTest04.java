package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.proxy.ProxyMaker;
import com.xuxueli.crawler.proxy.impl.RandomProxyMaker;
import org.jsoup.nodes.Document;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 爬虫示例04：爬取页面，动态代理IP方式
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest04 {


    public static void main(String[] args) {

        // 设置代理池
        ProxyMaker proxyMaker = new RandomProxyMaker(Arrays.asList(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("---", 8080)),
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress("---", 8080))
        ));

        // 构造爬虫
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(new HashSet<String>(Arrays.asList("https://my.oschina.net/xuxueli/blog")))
                .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
                .setProxyMaker(proxyMaker)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void parse(Document html, Object pageVo) {
                        System.out.println(html.baseUri() + "：" + html.html());
                    }
                })
                .build();

        // 启动
        System.out.println("start");
        crawler.start(true);
        System.out.println("end");

    }

}
