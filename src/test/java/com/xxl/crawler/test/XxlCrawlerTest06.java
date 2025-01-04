package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.annotation.PageFieldSelect;
import com.xxl.crawler.annotation.PageSelect;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Request;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.pageloader.strategy.SeleniumChromePageLoader;
import com.xxl.crawler.parser.PageParser;
import com.xxl.crawler.proxy.ProxyPool;
import com.xxl.crawler.proxy.strategy.RoundProxyPool;
import com.xxl.crawler.util.JsoupUtil;
import com.xxl.crawler.util.ProxyIpUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

/**
 * 爬虫示例01：
 *      - 爬虫名称：代理方式爬取数据
 *      - 爬虫场景：爬取目标页面数据，通过代理进行；可突破访问限制、保障数据安全；
 *      - 实现步骤：
 *          1、ProxyPool 构建： 构建代理池：可人工构造代理池，或通过系统方式更新维护；
 *          2、定义 XxlCrawler：配置开发爬虫代码；
 *          3、PageParser 开发：通过 “afterParse/后处理逻辑” 获取爬虫输出结果数据；本示例针对数据只做log输出，仅供学习参考。
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest06 {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerTest06.class);

    @Test
    public void test() {

        /**
         * 1、构建代理池：可人工构造代理池，或通过系统方式更新维护；
         *
         * 说明：
         *      a、可从网络自行获取，进行学习使用；比如从 “https://www.kuaidaili.com/free/inha/”，下面进行人工维护代理池；
         *      b、真实业务场景通过人工维护代理池，成本太高；可参考 "test_proxypool_build" 构建代理池；
         */
        ProxyPool proxyPool = new RoundProxyPool()
                .addProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("{代理IP}", 0)))
                .addProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("{代理IP}", 0)));

        /**
         * 2、定义 XxlCrawler
         */
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("http://whois.pconline.com.cn/ipJson.jsp?json=true")
                .setAllowSpread(false)
                .setProxyPool(proxyPool)
                .setTimeoutMillis(15 * 1000)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void afterParse(Response<Object> response) {
                        /**
                         * 3、PageParser 开发：获取代理请求结果，可通过输出数据查看代理是否成功（会展示代理后IP和地址信息）。
                         */
                        logger.info(response.getRequest().getUrl() + "：" + response.getHtml().outerHtml());
                    }
                })
                .build();

        crawler.start(true);
    }



    /**
     * 爬虫示例：爬取公开的免费代理，生成动态代理池
     * (免费代理可从搜索获取，免费代理不稳定可以多试几个；仅供学习测试使用，如有侵犯请联系删除； )
     *//*
    @Test
    public void test_proxypool_build() {

        // 代理池
        final List<PageProxyVo> proxyPool = new ArrayList<PageProxyVo>();

        // 构造爬虫
        String driverPath = "/Users/admin/Downloads/chromedriver-mac-arm64/chromedriver";
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.kuaidaili.com/free/inha/1/")
                .setWhiteUrlRegexs("^https:\\/\\/www\\.kuaidaili\\.com\\/free\\/inha\\/[1-3]\\/$")      // 前3页数据
                .setPageLoader(new SeleniumChromePageLoader(driverPath))
                .setThreadCount(5)
                .setPageParser(new PageParser<PageProxyVo>() {
                    @Override
                    public void afterParse(Response<PageProxyVo> response) {
                        if (response.getParseVoList()==null || response.getParseVoList().isEmpty()) {
                            return;
                        }
                        for (PageProxyVo pageVo: response.getParseVoList()) {
                            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pageVo.getIp(), pageVo.getPort()));
                            if (pageVo.getPort() > 0 && ProxyIpUtil.checkProxy(proxy, null) == 200) {
                                proxyPool.add(pageVo);
                                logger.info("proxy pool size : {}, new proxy: {}", proxyPool.size(), pageVo);
                            }
                        }

                    }
                })
                .build();
        crawler.start(true);


        // 代理池数据
        logger.info("----------- proxy pool total size : {} -----------", proxyPool.size());
        logger.info(proxyPool.toString());

        // 校验代理池    (代理方式请求IP地址查询网IP138，可从页面响应确认代理是否生效)
        logger.info("----------- proxy pool check -----------");
        if (!proxyPool.isEmpty()) {
            for (PageProxyVo pageVo: proxyPool) {
                try {
                    Document html = JsoupUtil.load(new Request("http://pv.sohu.com/cityjson",
                            null,
                            null,
                            null,
                            Const.USER_AGENT_CHROME,
                            null,
                            false,
                            Const.TIMEOUT_MILLIS_DEFAULT,
                            false,
                            new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pageVo.getIp(), pageVo.getPort()))));
                    logger.info(pageVo + " : " + html.html());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @PageSelect(cssQuery = "#list > table > tbody > tr")
    public static class PageProxyVo {

        @PageFieldSelect(cssQuery = "td:eq(0)", selectType = Const.SelectType.TEXT)
        private String ip;

        @PageFieldSelect(cssQuery = "td:eq(1)", selectType = Const.SelectType.TEXT)
        private int port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "PageVo{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    '}';
        }
    }*/

}
