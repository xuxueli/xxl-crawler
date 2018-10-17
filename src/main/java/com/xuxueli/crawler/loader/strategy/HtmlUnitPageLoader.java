package com.xuxueli.crawler.loader.strategy;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xuxueli.crawler.loader.PageLoader;
import com.xuxueli.crawler.model.PageRequest;
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
    public Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }

        WebClient webClient = new WebClient();
        try {
            WebRequest webRequest = new WebRequest(new URL(pageRequest.getUrl()));

            // 请求设置
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setDoNotTrackEnabled(false);
            webClient.getOptions().setUseInsecureSSL(!pageRequest.isValidateTLSCertificates());

            if (pageRequest.getParamMap() != null && !pageRequest.getParamMap().isEmpty()) {
                for (Map.Entry<String, String> paramItem : pageRequest.getParamMap().entrySet()) {
                    webRequest.getRequestParameters().add(new NameValuePair(paramItem.getKey(), paramItem.getValue()));
                }
            }
            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                webClient.getCookieManager().setCookiesEnabled(true);
                for (Map.Entry<String, String> cookieItem : pageRequest.getCookieMap().entrySet()) {
                    webClient.getCookieManager().addCookie(new Cookie("", cookieItem.getKey(), cookieItem.getValue()));
                }
            }
            if (pageRequest.getHeaderMap() != null && !pageRequest.getHeaderMap().isEmpty()) {
                webRequest.setAdditionalHeaders(pageRequest.getHeaderMap());
            }
            if (pageRequest.getUserAgent() != null) {
                webRequest.setAdditionalHeader("User-Agent", pageRequest.getUserAgent());
            }
            if (pageRequest.getReferrer() != null) {
                webRequest.setAdditionalHeader("Referer", pageRequest.getReferrer());
            }

            webClient.getOptions().setTimeout(pageRequest.getTimeoutMillis());
            webClient.setJavaScriptTimeout(pageRequest.getTimeoutMillis());
            webClient.waitForBackgroundJavaScript(pageRequest.getTimeoutMillis());

            // 代理
            if (pageRequest.getProxy() != null) {
                InetSocketAddress address = (InetSocketAddress) pageRequest.getProxy().address();
                boolean isSocks = pageRequest.getProxy().type() == Proxy.Type.SOCKS;
                webClient.getOptions().setProxyConfig(new ProxyConfig(address.getHostName(), address.getPort(), isSocks));
            }

            // 发出请求
            if (pageRequest.isIfPost()) {
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