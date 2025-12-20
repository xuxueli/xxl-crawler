package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.annotation.PageFieldSelect;
import com.xxl.crawler.annotation.PageSelect;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.pageloader.strategy.SeleniumChromePageLoader;
import com.xxl.crawler.pageparser.PageParser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例：Selenium 用法
 *
 * @author xuxueli 2018-10-16
 */

/**
 * 爬虫示例04：
 *      - 爬虫名称：Hao123动态页面爬虫
 *      - 爬虫场景：爬虫获取Hao123动态页面，由于数据异步渲染；该方案使用 Selenium + ChromeDriver 方式JS渲染，模拟浏览器行为采集数据；
 *      - 实现步骤：
 *          1、下载驱动 chromedriver ，并解压在磁盘空间下
 *          2、定义 XxlCrawler
 *          3、通过 PageVO（ProductPageVo）注解式定义页面元素选择逻辑，实现页面元素到 Object 的自动映射。
 *          4、PageParser 开发：通过 “afterParse/后处理” 获取数据结果。
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2018-10-16
 */
public class XxlCrawlerTest05 {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerTest06.class);

    @Test
    public void test() {

        /**
         * 1、下载驱动 chromedriver ，并解压在磁盘空间下；
         *
         *  下载版本：>=143.0.7499.169
         *  下载地址：
         *      a、https://developer.chrome.com/docs/chromedriver?hl=zh-cn
         *      b、https://googlechromelabs.github.io/chrome-for-testing/#stable
         */
        String driverPath = "/Users/admin/Downloads/chromedriver-mac-arm64/chromedriver";


        /**
         * 2、定义 XxlCrawler
         */
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.hao123.com/")
                .setAllowSpread(false)
                .setTimeoutMillis(10 * 1000)
                .setPageLoader(new SeleniumChromePageLoader(driverPath))                // "selenisum + chrome" 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void afterParse(Response<PageVo> response) {
                        /**
                         * 4、PageParser 开发
                         */
                        if (response.getParseVoList() != null && !response.getParseVoList().isEmpty()) {
                            logger.info("JS动态渲染方式获取: {}", response.getParseVoList());
                        } else {
                            logger.info("JS动态渲染方式获取: 获取失败");
                        }

                    }
                })
                .build();

        crawler.start(true);

    }

    /**
     * 3、定义 PageVo
     */
    @PageSelect(cssQuery = ".js-hotword")
    public static class PageVo {

        @PageFieldSelect(cssQuery = "*", selectType = Const.SelectType.TEXT)
        private String title;

        @PageFieldSelect(cssQuery = "href", selectType = Const.SelectType.ATTR)
        private String link;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        @Override
        public String toString() {
            return "PageVo{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }

}
