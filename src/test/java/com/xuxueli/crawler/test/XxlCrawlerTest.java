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
 * xxl crawler test
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest {

    @PageSelect(".body")
    public static class PageVo {

        @PageFieldSelect(value = ".blog-heading .title")
        private String title;

        @PageFieldSelect("#read")
        private int read;

        @PageFieldSelect(".comment-content")
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
                .setThreadCount(5)
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(String url, Document html, PageVo pageVo) {
                        System.out.println("-------------------");
                        System.out.println(url);
                        System.out.println(pageVo.toString());
                    }
                })
                .build();

        System.out.println(111);
        crawler.start();
        System.out.println(222);
    }

}
