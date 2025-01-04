package com.xxl.crawler.test;

import com.xxl.crawler.XxlCrawler;
import com.xxl.crawler.annotation.PageFieldSelect;
import com.xxl.crawler.annotation.PageSelect;
import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Response;
import com.xxl.crawler.pageparser.PageParser;
import com.xxl.crawler.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 爬虫示例03：
 *      - 爬虫名称：网易图片下载爬虫
 *      - 爬虫场景：爬取指定用户空间下文章以及相关图片，并将图片以文章分组下载至本地；
 *      - 实现步骤：
 *          1、XxlCrawler 开发：一行代码定义多线程爬虫，开启自动爬虫扩散，设置10线程并行运行；同时设计主动停顿时间等，避免对下游压力过大。
 *          2、PageVo 开发：通过 PageVO（ImgPageVo）注解式定义页面元素选择逻辑，实现页面元素到 Object 的自动映射。
 *          4、PageParser 开发：通过 “afterParse/后处理” 获取目标文章及图片数据，并将图片以文章分组下载至本地
 *
 * (仅供学习测试使用，如有侵犯请联系删除；)
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest03 {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawlerTest03.class);

    /**
     * 1、定义 XxlCrawler
     */
    @Test
    public void test() {

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.163.com/dy/media/T1466998806763.html")                                // 设置爬虫入口URL
                .setWhiteUrlRegexs("^https:\\/\\/www\\.163\\.com\\/dy\\/article\\/[A-Za-z0-9]+\\.html(\\?spss=dy_author)?$")    // 设置爬虫扩散的URL白名单正则表达式，控制扩散范围
                .setThreadCount(10)                                                                         // 设置爬虫线程池大小
                .setPauseMillis(100)                                                                        // 设置爬虫每次抓取间隔时间，避免对下游压力过大
                .setPageParser(new PageParser<ImgPageVo>() {
                    /**
                     * 3、PageParser 开发
                     */
                    @Override
                    public void afterParse(Response<ImgPageVo> response) {
                        // 爬虫范围限制，仅爬取“油画世界”相关页面
                        if (!response.getParseElementList().get(0).outerHtml().contains("油画世界")) {      // 上文入口setUrls为“油画世界”空间地址，此处限制扩散范围；
                            return;
                        }

                        // 生成下载目录，不同文档图片隔离存放
                        String filePath = "/Users/admin/Downloads/tmp/img";
                        filePath = filePath + "/" +response.getHtml().title();

                        // 下载图片至本地
                        if (response.getParseVoList()!=null) {
                            for (ImgPageVo pageImgVo: response.getParseVoList()) {
                                if (pageImgVo.getImages()!=null && !pageImgVo.getImages().isEmpty()) {
                                    for (String img: pageImgVo.getImages()) {
                                        // save
                                        String fileName = FileUtil.getFileNameByUrl(img, "image/jpeg");
                                        boolean ret = FileUtil.downFile(img, Const.TIMEOUT_MILLIS_DEFAULT, filePath, fileName);
                                        logger.info("down images " + (ret?"success":"fail") + "：" + img);
                                    }
                                }
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
    @PageSelect(cssQuery = "#content")
    public static class ImgPageVo {

        @PageFieldSelect(cssQuery = "img", selectType = Const.SelectType.ATTR, selectVal = "abs:src")
        private List<String> images;

        @PageFieldSelect(cssQuery = ".post_wemedia_name")
        private String title;

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "PageImgVo{" +
                    "images=" + images +
                    ", title='" + title + '\'' +
                    '}';
        }


    }


}
