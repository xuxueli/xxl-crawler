package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.util.FileUtil;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 爬虫示例02：爬取页面，下载Html文件
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest02 {

    public static void main(String[] args) {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(new HashSet<String>(Arrays.asList("https://my.oschina.net/xuxueli/blog")))
                .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
                .setThreadCount(3)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void parse(String url, Document html, Object pageVo) {

                        // 文件信息
                        String htmlData = html.html();
                        String filePath = "/Users/xuxueli/Downloads/tmp";
                        String fileName = FileUtil.getFileNameByUrl(url, XxlCrawlerConf.CONTENT_TYPE_HTML);

                        // 下载Html文件
                        FileUtil.saveFile(htmlData, filePath, fileName);
                    }
                })
                .build();

        System.out.println("start");
        crawler.start(true);
        System.out.println("end");
    }

}
