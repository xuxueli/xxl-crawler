package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.pageparser.PageParser;
import com.xxl.crawler.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 爬虫示例01：
 *      - 爬虫名称：Gitee页面下载爬虫
 *      - 爬虫场景：爬取目标页面（Gitee开源项目），将整个页面下载问题本地文件。
 *      - 实现步骤：多线程、分页方式自动扩散爬取“Gitee开源项目列表”，按照Star排序提取Top5页面；获取相关页面html原始数据，本地生成html文件；
 *          1、XxlCrawler 开发：一行代码定义多线程爬虫，开启自动爬虫扩散，设置3线程并行运行；同时设计主动停顿时间等，避免对下游压力过大。
 *          2、PageParser 开发：通过 “afterParse/后处理” 获取目标页面html源码，然后本地生成写入相关html文件，完成下载功能。
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest02 {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerTest02.class);

    @Test
    public void test02() {

        /**
         * 1、定义 XxlCrawler
         */
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://gitee.com/explore/all?order=starred&page=1")                              // 设置爬虫入口URL
                .setWhiteUrlRegexs("^https:\\/\\/gitee\\.com\\/explore\\/all\\?order=starred&page=[1-5]$")  // 设置爬虫扩散的URL白名单正则表达式，控制扩散范围
                .setThreadCount(3)                                                                          // 设置爬虫线程池大小
                .setPauseMillis(100)                                                                        // 设置爬虫每次抓取间隔时间，避免对下游压力过大
                .setPageParser(new PageParser<Object>() {
                    /**
                     * 2、PageParser 开发
                     */
                    @Override
                    public void afterParse(Response<Object> response) {
                        // 获取页面html源码
                        String htmlData = response.getHtml().html();

                        // 下载Html文件
                        String filePath = "/Users/admin/Downloads/tmp/html";
                        String fileName = FileUtil.getFileNameByUrl(response.getHtml().baseUri(), Const.CONTENT_TYPE_HTML);
                        FileUtil.saveFile(htmlData, filePath, fileName);
                        logger.info("saveFile success, url = {}, file={}", response.getRequest().getUrl(), filePath + "/" + fileName);
                    }
                })
                .build();

        crawler.start(true);
    }

}
