package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.annotation.PageFieldSelect;
import com.xxl.crawler.annotation.PageSelect;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.parser.PageParser;
import com.xxl.crawler.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 爬虫示例：常规用法
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerNormalTest {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerNormalTest.class);

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
                    public void afterParse(Response<PageDataVo> response) {
                        // 解析封装 PageVo 对象
                        String pageUrl = response.getHtml().baseUri();
                        for (PageDataVo pageVo: response.getParseVoList()) {
                            logger.info("pageUrl={}, PageDataVo={}", pageUrl, pageVo);
                        }
                    }
                })
                .build();

        crawler.start(true);
    }

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
     * 爬虫示例02：分页爬取页面 + 下载Html文件
     */
    @Test
    public void test02() {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://gitee.com/xuxueli0323/projects?page=1")
                .setWhiteUrlRegexs("^https:\\/\\/gitee\\.com\\/xuxueli0323\\/projects\\?page=\\d+$")
                .setThreadCount(3)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void afterParse(Response<Object> response) {
                        // 文件信息
                        String htmlData = response.getHtml().html();
                        String filePath = "/Users/admin/Downloads/tmp/html";
                        String fileName = FileUtil.getFileNameByUrl(response.getHtml().baseUri(), Const.CONTENT_TYPE_HTML);

                        // 下载Html文件
                        FileUtil.saveFile(htmlData, filePath, fileName);
                        logger.info("saveFile success, url = {}, file={}", response.getRequest().getUrl(), filePath + "/" + fileName);
                    }
                })
                .build();

        crawler.start(true);
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
                    public void afterParse(Response<PageImgVo> response) {

                        // 文件信息
                        String filePath = "/Users/admin/Downloads/tmp";

                        for (PageImgVo pageImgVo: response.getParseVoList()) {
                            if (pageImgVo.getImages()!=null && !pageImgVo.getImages().isEmpty()) {
                                for (String img: pageImgVo.getImages()) {
                                    // 下载图片文件
                                    String fileName = FileUtil.getFileNameByUrl(img, null);
                                    boolean ret = FileUtil.downFile(img, Const.TIMEOUT_MILLIS_DEFAULT, filePath, fileName);
                                    logger.info("down images " + (ret?"success":"fail") + "：" + img);
                                }
                            }
                        }

                    }
                })
                .build();

        crawler.start(true);
    }

    @PageSelect(cssQuery = "body")
    public static class PageImgVo {

        @PageFieldSelect(cssQuery = "img", selectType = Const.SelectType.ATTR, selectVal = "abs:src")
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
     * 爬虫示例04：采集非Web页面，如JSON接口等，直接输出响应数据
     */
    @Test
    public void test04() {

        // 构造爬虫
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("http://news.baidu.com/widget?id=LocalNews&ajax=json")
                .setTimeoutMillis(5000)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void afterParse(Response<Object> response) {
                        logger.info(response.getRequest().getUrl() + ": " + response.getHtml().outerHtml());
                    }
                })
                .build();

        crawler.start(true);
    }

}
