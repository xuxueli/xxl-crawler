package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.parser.strategy.NonPageParser;
import com.xuxueli.crawler.util.FileUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 爬虫示例：常规用法
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerNormalTest {

    @PageSelect(cssQuery = "#search-projects-ulist .project")
    public static class PageDataVo {

        @PageFieldSelect(cssQuery = ".repository")
        private String repository;

        @PageFieldSelect(cssQuery = ".description")
        private String description;

        public String getRepository() {
            return repository;
        }

        public void setRepository(String repository) {
            this.repository = repository;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "PageVo{" +
                    "repository='" + repository + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    /**
     * 爬虫示例01：分页爬取页面 + 注解式VO数据识别
     */
    @Test
    public void test01() {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://gitee.com/xuxueli0323/projects?page=1")
                .setWhiteUrlRegexs("https://gitee\\.com/xuxueli0323/projects\\?page=\\d+")
                .setThreadCount(3)
                .setPageParser(new PageParser<PageDataVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageDataVo pageVo) {
                        // 解析封装 PageVo 对象
                        String pageUrl = html.baseUri();
                        System.out.println(pageUrl + "：" + pageVo.toString());
                    }
                })
                .build();

        System.out.println("start");
        crawler.start(true);
        System.out.println("end");
    }

    /**
     * 爬虫示例02：分页爬取页面 + 下载Html文件
     */
    @Test
    public void test02() {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://gitee.com/xuxueli0323/projects?page=1")
                .setWhiteUrlRegexs("https://gitee\\.com/xuxueli0323/projects\\?page=\\d+")
                .setThreadCount(3)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, Object pageVo) {

                        // 文件信息
                        String htmlData = html.html();
                        String filePath = "/Users/admin/Downloads/tmp";
                        String fileName = FileUtil.getFileNameByUrl(html.baseUri(), XxlCrawlerConf.CONTENT_TYPE_HTML);

                        // 下载Html文件
                        FileUtil.saveFile(htmlData, filePath, fileName);
                    }
                })
                .build();

        System.out.println("start");
        crawler.start(true);
        System.out.println("end");
    }

    @PageSelect(cssQuery = "body")
    public static class PageImgVo {

        @PageFieldSelect(cssQuery = "img", selectType = XxlCrawlerConf.SelectType.ATTR, selectVal = "abs:src")
        private List<String> images;

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "PageVo{" +
                    "images=" + images +
                    '}';
        }
    }


    /**
     * 爬虫示例03：分页爬取页面 + 下载图片
     */
    @Test
    public void test03() {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://gitee.com/xuxueli0323/projects?page=1")
                .setWhiteUrlRegexs("https://gitee\\.com/xuxueli0323/projects\\?page=\\d+")
                .setThreadCount(3)
                .setPageParser(new PageParser<PageImgVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageImgVo pageVo) {

                        // 文件信息
                        String filePath = "/Users/admin/Downloads/tmp";

                        if (pageVo.getImages()!=null && pageVo.getImages().size() > 0) {
                            Set<String> imagesSet = new HashSet<>(pageVo.getImages());
                            for (String img: imagesSet) {

                                // 下载图片文件
                                String fileName = FileUtil.getFileNameByUrl(img, null);
                                boolean ret = FileUtil.downFile(img, XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT, filePath, fileName);
                                System.out.println("down images " + (ret?"success":"fail") + "：" + img);
                            }
                        }
                    }
                })
                .build();

        System.out.println("start");
        crawler.start(true);
        System.out.println("end");
    }

    /**
     * 爬虫示例04：采集非Web页面，如JSON接口等，直接输出响应数据
     */
    @Test
    public void test04() {

        // 构造爬虫
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("http://news.baidu.com/widget?id=LocalNews&ajax=json")
                .setTimeoutMillis(5000)
                .setPageParser(new NonPageParser() {
                    @Override
                    public void parse(String url, String pageSource) {
                        System.out.println(url + ": " + pageSource);
                    }
                })
                .build();

        // 启动
        crawler.start(true);

    }


}
