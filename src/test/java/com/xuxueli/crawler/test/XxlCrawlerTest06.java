package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.model.PageLoadInfo;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.util.JsoupUtil;
import com.xuxueli.crawler.util.ProxyIpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

/**
 * 爬虫示例06：JS渲染
 *
 * @author xuxueli 2017-12-29 23:29:48
 */
public class XxlCrawlerTest06 {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerTest05.class);

    @PageSelect(cssQuery = "body")
    public static class PageVo {

        @PageFieldSelect(cssQuery = "#u1 > a:nth-child(1)", selectType = XxlCrawlerConf.SelectType.TEXT)
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "PageVo{" +
                    "data='" + data + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {

        // 构造爬虫
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.baidu.com/")
                .setAllowSpread(false)
                .setThreadCount(1)
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageVo pageVo) {
                        logger.info(pageVo.getData());
                    }
                })
                .build();

        // 启动
        crawler.start(true);

    }

}
