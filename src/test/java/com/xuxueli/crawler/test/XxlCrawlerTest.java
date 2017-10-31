package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.parser.PageParser;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 爬虫示例01：爬取页面数据并封装VO对象
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest {

    @PageSelect(".body")
    public static class PageVo {

        @PageFieldSelect(cssQuery = ".blog-heading .title")
        private String title;

        @PageFieldSelect(cssQuery = "#read")
        private int read;

        @PageFieldSelect(cssQuery = ".comment-content")
        private List<String> comment;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRead() {
            return read;
        }

        public void setRead(int read) {
            this.read = read;
        }

        public List<String> getComment() {
            return comment;
        }

        public void setComment(List<String> comment) {
            this.comment = comment;
        }

        @Override
        public String toString() {
            return "PageVo{" +
                    "title='" + title + '\'' +
                    ", read=" + read +
                    ", comment=" + comment +
                    '}';
        }
    }

    public static void main(String[] args) {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls(new HashSet<String>(Arrays.asList("https://my.oschina.net/xuxueli/blog")))
                .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
                .setThreadCount(3)
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(String url, Document html, PageVo pageVo) {
                        // 解析封装 PageVo 对象
                        System.out.println(url + "：" + pageVo.toString());
                    }
                })
                .build();

        System.out.println("start");
        crawler.start(true);
        System.out.println("end");
    }

}
