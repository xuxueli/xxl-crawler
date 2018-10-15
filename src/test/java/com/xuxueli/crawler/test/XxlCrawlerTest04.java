package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.proxy.ProxyMaker;
import com.xuxueli.crawler.proxy.strategy.RoundProxyMaker;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * 爬虫示例04：爬取页面，代理IP方式
 * (免费代理可搜索获取，免费代理不稳定可以多试几个；仅供学习测试使用，如有侵犯请联系删除； )
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest04 {


    public static void main(String[] args) {

        // 设置代理池
        ProxyMaker proxyMaker = new RoundProxyMaker()
                .addProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("---", 80)));

        // 构造爬虫     (代理方式请求IP地址查询网IP138，可从页面响应确认代理是否生效)
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("http://2018.ip138.com/ic.asp")
                .setAllowSpread(false)
                .setProxyMaker(proxyMaker)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, Object pageVo) {
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
