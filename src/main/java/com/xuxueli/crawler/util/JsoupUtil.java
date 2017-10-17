package com.xuxueli.crawler.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * jsoup tool
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class JsoupUtil {
    private static Logger logger = LoggerFactory.getLogger(JsoupUtil.class);

    /**
     * 加载解析页面
     *
     * @param url		：加载URL
     * @param paramMap	：请求参数
     * @param cookieMap	：请求cookie
     * @param ifPost	：是否使用post请求
     * @param tagMap	：解析规则
     *                      0：选择器方式
     *                      1：id方式
     *                      2：class方式
     *                      3：html tag
     * @return
     */
    public static Elements loadParse(String url, Map<String, String> paramMap, Map<String, String> cookieMap,
            boolean ifPost, Map<Integer, Set<String>> tagMap) {
        if (!UrlUtil.isUrl(url)) {
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(url);
            conn.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
            if (paramMap != null && !paramMap.isEmpty()) {
                conn.data(paramMap);
            }
            if (cookieMap != null && !cookieMap.isEmpty()) {
                conn.cookies(cookieMap);
            }
            conn.timeout(5000);

            // 发出请求
            Document html = null;
            if (ifPost) {
                html = conn.post();
            } else {
                html = conn.get();
            }

            // 过滤元素
            Elements resultAll = new Elements();
            if (tagMap != null && !tagMap.isEmpty()) {
                for (Entry<Integer, Set<String>> tag : tagMap.entrySet()) {
                    int tagType = tag.getKey();
                    Set<String> tagNameList = tag.getValue();
                    if (tagNameList != null && tagNameList.size() > 0) {
                        for (String tagName : tagNameList) {
                            if (tagType == 0) {
                                Elements resultSelect = html.select(tagName);	// 选择器方式
                                resultAll.addAll(resultSelect);
                            } else if (tagType == 1) {
                                Element resultId = html.getElementById(tagName);	// 元素ID方式
                                resultAll.add(resultId);
                            } else if (tagType == 2) {
                                Elements resultClass = html.getElementsByClass(tagName);	// ClassName方式
                                resultAll.addAll(resultClass);
                            } else if (tagType == 3) {
                                Elements resultTag = html.getElementsByTag(tagName);	// html标签方式
                                resultAll.addAll(resultTag);
                            }
                        }
                    }

                }

            } else {
                resultAll = html.getElementsByTag("body");
            }
            return resultAll;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取页面上所有超链接地址（<a>标签的href值）
     *
     * @param link  待爬行的页面链接
     * @return
     */
    public static Set<String> findLinks(String link) {
        // 组装规则
        Map<Integer, Set<String>> tagMap = new HashMap<Integer, Set<String>>();
        tagMap.put(0, new HashSet<String>(Arrays.asList("a[href]")));

        // 加载解析html
        Elements resultAll = loadParse(link, null, null, false, tagMap);

        // 抽取数据
        Set<String> links = new HashSet<String>();
        if (resultAll!=null && resultAll.size() > 0) {
            for (Element item : resultAll) {
                String href = item.attr("abs:href");    // href、abs:href
                if (UrlUtil.isUrl(href)) {
                    links.add(href);
                }
            }
        }
        return links;
    }

}
