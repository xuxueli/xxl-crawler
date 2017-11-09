package com.xuxueli.crawler.conf;

/**
 * xxl craw conf
 *
 * @author xuxueli 2017-10-31 11:55:32
 */
public class XxlCrawlerConf {

    // userAgent
    public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
    public static final String USER_AGENT_FIREFOX_45 = "Mozilla/5.0 (Windows NT 6.1; rv:45.0) Gecko/20100101 Firefox/45.0";
    public static final String USER_AGENT_IE = "Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko";
    public static final String USER_AGENT_EDGE = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586";

    // timeout default, ms
    public static final int TIMEOUT_MILLIS_DEFAULT = 5*1000;

    // content type
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "text/xml";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String CONTENT_TYPE_JSONP = "text/javascript";

    // 数据抽取方式
    public enum SelectType {
        // .html()
        HTML,
        // .val()
        VAL,
        // .text()
        TEXT,
        // .toString()
        TOSTRING,
        // .attr("attributeKey")
        ATTR,
        // .hasClass("className")
        HAS_CLASS;
    }

    // site address
    public static final String SITE_BAIDU = "http://www.baidu.com/";
    public static final String SITE_GOOGLE = "http://www.google.com/";

}
