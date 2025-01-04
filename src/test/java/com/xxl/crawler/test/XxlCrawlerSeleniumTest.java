package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.annotation.PageFieldSelect;
import com.xxl.crawler.annotation.PageSelect;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.pageloader.strategy.SeleniumChromePageLoader;
import com.xxl.crawler.parser.PageParser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例：Selenium 用法
 *
 * @author xuxueli 2018-10-16
 */
public class XxlCrawlerSeleniumTest {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerProxyTest.class);

    /**
     * 爬虫示例08：JS渲染方式采集数据，"selenisum + phantomjs" 方案
     * (仅供学习测试使用，如有侵犯请联系删除； )
     */
    @Test
    public void test01() {

        /**
         * chromedriver driver （驱动设置：方式一、驱动文件地址作为入参传入；方式二：加入环境变量 'System.setProperty("phantomjs.binary.path", driverPath);'，入参可空；）
         *
         * 1、https://developer.chrome.com/docs/chromedriver?hl=zh-cn
         * 2、https://googlechromelabs.github.io/chrome-for-testing/#stable
         */
        String driverPath = "/Users/admin/Downloads/chromedriver-mac-arm64/chromedriver";


        // 构造爬虫
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://detail.tmall.com/item.htm?id=603403919330")
                .setAllowSpread(false)
                .setTimeoutMillis(10 * 1000)
                //.setPageLoader(new SeleniumPhantomjsPageLoader(driverPath))           // "selenisum + phantomjs" 版本 PageLoader：支持 JS 渲染
                .setPageLoader(new SeleniumChromePageLoader(driverPath))                // "selenisum + chrome" 版本 PageLoader：支持 JS 渲染
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void afterParse(Response<PageVo> response) {
                        if (response.getParseVoList() != null && response.getParseVoList().size() > 0) {
                            logger.info("商品价格（JS动态渲染方式获取）: {}", response.getParseVoList());
                        } else {
                            logger.info("商品价格（JS动态渲染方式获取）: 获取失败");
                        }

                    }
                })
                .build();

        // 启动
        crawler.start(true);

    }

    @PageSelect(cssQuery = "body")
    public static class PageVo {

        @PageFieldSelect(cssQuery = ".text--Mdqy24Ex", selectType = Const.SelectType.TEXT)
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

}
