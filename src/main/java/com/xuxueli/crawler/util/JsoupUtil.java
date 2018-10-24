package com.xuxueli.crawler.util;

import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.model.PageRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * jsoup tool
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class JsoupUtil {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    /**
     * 加载页面
     *
     * @param pageRequest
     *
     * @return Document
     */
    public static Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(pageRequest.getUrl());
            if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                conn.data(pageRequest.getParamMap());
            }
            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                conn.cookies(pageRequest.getCookieMap());
            }
            if (pageRequest.getHeaderMap()!=null && !pageRequest.getHeaderMap().isEmpty()) {
                conn.headers(pageRequest.getHeaderMap());
            }
            if (pageRequest.getUserAgent()!=null) {
                conn.userAgent(pageRequest.getUserAgent());
            }
            if (pageRequest.getReferrer() != null) {
                conn.referrer(pageRequest.getReferrer());
            }
            conn.timeout(pageRequest.getTimeoutMillis());
            conn.validateTLSCertificates(pageRequest.isValidateTLSCertificates());
            conn.maxBodySize(0);    // 取消默认1M限制

            // 代理
            if (pageRequest.getProxy() != null) {
                conn.proxy(pageRequest.getProxy());
            }

            // 发出请求
            Document html = null;
            if (pageRequest.isIfPost()) {
                html = conn.post();
            } else {
                html = conn.get();
            }
            return html;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    public static String loadPageSource(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(pageRequest.getUrl());
            if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                conn.data(pageRequest.getParamMap());
            }
            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                conn.cookies(pageRequest.getCookieMap());
            }
            if (pageRequest.getHeaderMap()!=null && !pageRequest.getHeaderMap().isEmpty()) {
                conn.headers(pageRequest.getHeaderMap());
            }
            if (pageRequest.getUserAgent()!=null) {
                conn.userAgent(pageRequest.getUserAgent());
            }
            if (pageRequest.getReferrer() != null) {
                conn.referrer(pageRequest.getReferrer());
            }
            conn.timeout(pageRequest.getTimeoutMillis());
            conn.validateTLSCertificates(pageRequest.isValidateTLSCertificates());
            conn.maxBodySize(0);    // 取消默认1M限制

            // 代理
            if (pageRequest.getProxy() != null) {
                conn.proxy(pageRequest.getProxy());
            }

            conn.ignoreContentType(true);
            conn.method(pageRequest.isIfPost()?Connection.Method.POST:Connection.Method.GET);

            // 发出请求
            Connection.Response resp = conn.execute();
            String pageSource = resp.body();
            return pageSource;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 抽取元素数据
     *
     * @param fieldElement
     * @param selectType
     * @param selectVal
     * @return String
     */
    public static String parseElement(Element fieldElement, XxlCrawlerConf.SelectType selectType, String selectVal) {
        String fieldElementOrigin = null;
        if (XxlCrawlerConf.SelectType.HTML == selectType) {
            fieldElementOrigin = fieldElement.html();
        } else if (XxlCrawlerConf.SelectType.VAL == selectType) {
            fieldElementOrigin = fieldElement.val();
        } else if (XxlCrawlerConf.SelectType.TEXT == selectType) {
            fieldElementOrigin = fieldElement.text();
        } else if (XxlCrawlerConf.SelectType.ATTR == selectType) {
            fieldElementOrigin = fieldElement.attr(selectVal);
        }  else if (XxlCrawlerConf.SelectType.HAS_CLASS == selectType) {
            fieldElementOrigin = String.valueOf(fieldElement.hasClass(selectVal));
        }  else {
            fieldElementOrigin = fieldElement.toString();
        }
        return fieldElementOrigin;
    }

    /**
     * 获取页面上所有超链接地址 （<a>标签的href值）
     *
     * @param html  页面文档
     * @return Set<String>
     */
    public static Set<String> findLinks(Document html) {

        if (html == null) {
            return null;
        }

        // element
        /**
         *
         * Elements resultSelect = html.select(tagName);	// 选择器方式
         * Element resultId = html.getElementById(tagName);	// 元素ID方式
         * Elements resultClass = html.getElementsByClass(tagName);	// ClassName方式
         * Elements resultTag = html.getElementsByTag(tagName);	// html标签方式 "body"
         *
         */
        Elements hrefElements = html.select("a[href]");

        // 抽取数据
        Set<String> links = new HashSet<String>();
        if (hrefElements!=null && hrefElements.size() > 0) {
            for (Element item : hrefElements) {
                String href = item.attr("abs:href");    // href、abs:href
                if (UrlUtil.isUrl(href)) {
                    links.add(href);
                }
            }
        }
        return links;
    }

    /**
     * 获取页面上所有图片地址 （<a>标签的href值）
     *
     * @param html
     * @return Set<String>
     */
    public static Set<String> findImages(Document html) {

        Elements imgs = html.getElementsByTag("img");

        Set<String> images = new HashSet<String>();
        if (imgs!=null && imgs.size() > 0) {
            for (Element element: imgs) {
                String imgSrc = element.attr("abs:src");
                images.add(imgSrc);
            }
        }

        return images;
    }

}
