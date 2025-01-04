package com.xxl.crawler.util;

import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.param.Request;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
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
     * load page
     *
     * @param request
     *
     * @return Document
     */
    public static Document load(Request request) {
        if (!UrlUtil.isUrl(request.getUrl())) {
            logger.debug("url is invalid, request="+request.toString());
            return null;
        }
        try {
            // 请求设置
            Connection conn = Jsoup.connect(request.getUrl());
            if (request.getParamMap() != null && !request.getParamMap().isEmpty()) {
                conn.data(request.getParamMap());
            }
            if (request.getHeaderMap()!=null && !request.getHeaderMap().isEmpty()) {
                conn.headers(request.getHeaderMap());
            }
            if (request.getCookieMap() != null && !request.getCookieMap().isEmpty()) {
                conn.cookies(request.getCookieMap());
            }
            if (request.getUserAgent()!=null) {
                conn.userAgent(request.getUserAgent());
            }
            if (request.getReferrer() != null) {
                conn.referrer(request.getReferrer());
            }
            conn.method(request.isIfPost()?Connection.Method.POST:Connection.Method.GET);
            conn.timeout(request.getTimeoutMillis());
            if (request.isValidateTLSCertificates()) {
                conn.sslSocketFactory(generateSSLSocketFactory());
            }
            conn.maxBodySize(0);          // 取消默认1M限制
            conn.ignoreContentType(true);       // ignore ContentType

            // 代理
            if (request.getProxy() != null) {
                conn.proxy(request.getProxy());
            }

            // execute
            Connection.Response resp = conn.execute();

            // parse
            Document document = resp.parse();
            return document;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
    private static SSLSocketFactory generateSSLSocketFactory() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory result = sslContext.getSocketFactory();
            return result;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create a SSL socket factory", e);
        }
    }

    /**
     * load PageSource
     *
     * @param request
     * @return
     */
    public static String loadPageSource(Request request) {
        Document html = load(request);
        return html!=null?html.outerHtml():null;
    }

    /**
     * 抽取元素数据
     *
     * @param fieldElement
     * @param selectType
     * @param selectVal
     * @return String
     */
    public static String parseElement(Element fieldElement, Const.SelectType selectType, String selectVal) {
        String fieldElementOrigin = null;
        if (Const.SelectType.HTML == selectType) {
            fieldElementOrigin = fieldElement.html();
        } else if (Const.SelectType.VAL == selectType) {
            fieldElementOrigin = fieldElement.val();
        } else if (Const.SelectType.TEXT == selectType) {
            fieldElementOrigin = fieldElement.text();
        } else if (Const.SelectType.ATTR == selectType) {
            fieldElementOrigin = fieldElement.attr(selectVal);
        }  else if (Const.SelectType.HAS_CLASS == selectType) {
            fieldElementOrigin = String.valueOf(fieldElement.hasClass(selectVal));
        }  else {
            fieldElementOrigin = fieldElement.toString();
        }
        return fieldElementOrigin;
    }

    /**
     * find links("<a href=XX ></>") from Document
     *
     * @param html
     * @return Set<String>
     */
    public static Set<String> findLinks(Document html) {
        // valid
        if (html == null) {
            return null;
        }

        // parse element
        /**
         *
         * Elements resultSelect = html.select(tagName);	            // 选择器方式
         * Element resultId = html.getElementById(tagName);	            // 元素ID方式
         * Elements resultClass = html.getElementsByClass(tagName);	    // ClassName方式
         * Elements resultTag = html.getElementsByTag(tagName);	        // html标签方式 "body"
         *
         */
        Elements hrefElements = html.select("a[href]");

        // parse url
        Set<String> links = new HashSet<String>();
        if (hrefElements!=null && !hrefElements.isEmpty()) {
            for (Element item : hrefElements) {
                String href = item.attr("abs:href");        // href、abs:href
                if (UrlUtil.isUrl(href)) {
                    links.add(href);
                }
            }
        }
        return links;
    }

    /**
     *  find imgs("<img src=XX ></>") from Document
     *
     * @param html
     * @return Set<String>
     */
    public static Set<String> findImages(Document html) {

        Elements imgs = html.getElementsByTag("img");

        Set<String> images = new HashSet<String>();
        if (imgs!=null && !imgs.isEmpty()) {
            for (Element element: imgs) {
                String imgSrc = element.attr("abs:src");
                images.add(imgSrc);
            }
        }

        return images;
    }

}
