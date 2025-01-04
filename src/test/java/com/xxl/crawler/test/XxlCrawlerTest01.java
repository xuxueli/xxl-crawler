package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.annotation.PageFieldSelect;
import com.xxl.crawler.annotation.PageSelect;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.parser.PageParser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例01：
 *      - 爬虫名称：Gitee高星项目数据爬虫
 *      - 爬虫场景：爬取目标页面（Gitee开源项目），提取页面元素数据并输出；
 *      - 实现步骤：多线程、分页方式自动扩散爬取“Gitee开源项目列表”，按照Star排序提取Top75开源项目；最终通过“注解式”自动提取页面数据，封装成PageVo输出。
 *          1、XxlCrawler 开发：一行代码定义多线程爬虫，开启自动爬虫扩散，设置3线程并行运行；同时设计主动停顿时间等，避免对下游压力过大。
 *          2、PageVo 开发：通过 PageVO（GiteeProjectPageVo）注解式定义页面元素选择逻辑，实现页面元素到 Object 的自动映射。
 *          3、PageParser 开发：通过 “afterParse/后处理逻辑” 获取爬虫输出结果数据；本示例针对数据只做log输出，仅供学习参考。
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest01 {
    private static final Logger logger = LoggerFactory.getLogger(XxlCrawlerTest01.class);

    @Test
    public void test() {

        /**
         * 1、定义 XxlCrawler
         *
         * 说明：XxlCrawler 支持通过一行代码开发并启动一个爬虫，可结合如下示例以及官方文档深入了解。
         */
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://gitee.com/explore/all?order=starred&page=1")                              // 设置爬虫入口URL
                .setAllowSpread(true)                                                                       // 允许爬虫扩散
                .setWhiteUrlRegexs("^https:\\/\\/gitee\\.com\\/explore\\/all\\?order=starred&page=[1-5]$")  // 设置爬虫扩散的URL白名单正则表达式，控制扩散范围
                .setThreadCount(3)                                                                          // 设置爬虫线程池大小
                .setPauseMillis(100)                                                                        // 设置爬虫每次抓取间隔时间，避免对下游压力过大
                .setPageParser(new PageParser<GiteeProjectPageVo>() {
                    @Override
                    public void afterParse(Response<GiteeProjectPageVo> response) {
                        /**
                         * 3、获取爬虫结果数据
                         *
                         * 说明：afterParse 会在多线程爬虫运行过程中实时输出爬虫结果数据，避免结尾一次性反馈造成大对象问题；可实时消费处理数据，如存储在DB等。
                         */
                        if (response.getParseVoList() != null) {
                            for (GiteeProjectPageVo pageVo: response.getParseVoList()) {
                                logger.info("response.getHtml().baseUri()={}, PageDataVo={}", response.getHtml().baseUri(), pageVo);
                            }
                        }
                    }
                })
                .build();

        crawler.start(true);
    }

    /**
     * 2、定义 PageVo
     *
     * 说明：
     *      @PageSelect： 通过该注解页面元素，单个页面可以匹配多个页面元素生成多个 PageVo 对象。
     *      @PageFieldSelect： 通过该注解匹配页面元素的属性，每个页面元素可以匹配多个属性，即对应 PageVo 的多个属性值。
     */
    @PageSelect(cssQuery = ".project-title")
    public static class GiteeProjectPageVo {

        @PageFieldSelect(cssQuery = ".title")
        private String projectName;

        @PageFieldSelect(cssQuery = ".stars-count", selectType = Const.SelectType.ATTR, selectVal = "data-count")
        private String description;

        @PageFieldSelect(cssQuery = ".title", selectType = Const.SelectType.ATTR, selectVal = "href")
        private String href;

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHref() {
            return "https://gitee.com" + href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        @Override
        public String toString() {
            return "GiteeProjectPageVo{" +
                    "projectName='" + getProjectName() + '\'' +
                    ", description='" + getDescription() + '\'' +
                    ", href='" + getHref() + '\'' +
                    '}';
        }
    }

}
