package com.xuxueli.crawler.parser.strategy;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xuxueli.crawler.loader.PageLoader;
import com.xuxueli.crawler.model.PageLoadInfo;
import com.xuxueli.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

/**
 * htmlunit page loader
 *
 * @author xuxueli 2018-02-06 19:41:39
 */
public class HtmlUnitPageLoader extends PageLoader {
    private static Logger logger = LoggerFactory.getLogger(HtmlUnitPageLoader.class);

    @Override
    public Document load(PageLoadInfo pageLoadInfo) {
        if (!UrlUtil.isUrl(pageLoadInfo.getUrl())) {
            return null;
        }

        WebClient webClient = new WebClient();
        try {
            WebRequest webRequest = new WebRequest(new URL(pageLoadInfo.getUrl()));

            // 请求设置
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setDoNotTrackEnabled(false);

            if (pageLoadInfo.getParamMap() != null && !pageLoadInfo.getParamMap().isEmpty()) {
                for (Map.Entry<String, String> paramItem : pageLoadInfo.getParamMap().entrySet()) {
                    webRequest.getRequestParameters().add(new NameValuePair(paramItem.getKey(), paramItem.getValue()));
                }
            }
            if (pageLoadInfo.getCookieMap() != null && !pageLoadInfo.getCookieMap().isEmpty()) {
                webClient.getCookieManager().setCookiesEnabled(true);
                for (Map.Entry<String, String> cookieItem : pageLoadInfo.getCookieMap().entrySet()) {
                    webClient.getCookieManager().addCookie(new Cookie("", cookieItem.getKey(), cookieItem.getValue()));
                }
            }
            if (pageLoadInfo.getHeaderMap() != null && !pageLoadInfo.getHeaderMap().isEmpty()) {
                webRequest.setAdditionalHeaders(pageLoadInfo.getHeaderMap());
            }
            if (pageLoadInfo.getUserAgent() != null) {
                webRequest.setAdditionalHeader("User-Agent", pageLoadInfo.getUserAgent());
            }
            if (pageLoadInfo.getReferrer() != null) {
                webRequest.setAdditionalHeader("Referer", pageLoadInfo.getReferrer());
            }

            webClient.getOptions().setTimeout(pageLoadInfo.getTimeoutMillis());
            webClient.setJavaScriptTimeout(pageLoadInfo.getTimeoutMillis());
            webClient.waitForBackgroundJavaScript(pageLoadInfo.getTimeoutMillis());

            // 代理
            if (pageLoadInfo.getProxy() != null) {
                InetSocketAddress address = (InetSocketAddress) pageLoadInfo.getProxy().address();
                boolean isSocks = pageLoadInfo.getProxy().type() == Proxy.Type.SOCKS;
                webClient.getOptions().setProxyConfig(new ProxyConfig(address.getHostName(), address.getPort(), isSocks));
            }

            // 发出请求
            if (pageLoadInfo.getIfPost()) {
                webRequest.setHttpMethod(HttpMethod.POST);
            } else {
                webRequest.setHttpMethod(HttpMethod.GET);
            }
            HtmlPage page = webClient.getPage(webRequest);

            String pageAsXml = page.asXml();
            if (pageAsXml != null) {
                Document html = Jsoup.parse(pageAsXml);
                return html;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (webClient != null) {
                webClient.close();
            }
        }
        return null;
    }

}