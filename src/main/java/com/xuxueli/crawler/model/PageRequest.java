package com.xuxueli.crawler.model;

import java.net.Proxy;
import java.util.Map;

/**
 * page load info
 *
 * @author xuxueli 2017-11-10 17:12:55
 */
public class PageRequest {

    private String url;
    private Map<String, String> paramMap;
    private Map<String, String> cookieMap;
    private Map<String, String> headerMap;
    private String userAgent;
    private String referrer;
    private boolean ifPost;
    private int timeoutMillis;
    private boolean isValidateTLSCertificates;
    private Proxy proxy;

    public PageRequest() {
    }

    public PageRequest(String url,
                       Map<String, String> paramMap,
                       Map<String, String> cookieMap,
                       Map<String, String> headerMap,
                       String userAgent,
                       String referrer,
                       boolean ifPost,
                       int timeoutMillis,
                       boolean isValidateTLSCertificates,
                       Proxy proxy) {
        this.url = url;
        this.paramMap = paramMap;
        this.cookieMap = cookieMap;
        this.headerMap = headerMap;
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.ifPost = ifPost;
        this.timeoutMillis = timeoutMillis;
        this.isValidateTLSCertificates = isValidateTLSCertificates;
        this.proxy = proxy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public boolean isIfPost() {
        return ifPost;
    }

    public void setIfPost(boolean ifPost) {
        this.ifPost = ifPost;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public boolean isValidateTLSCertificates() {
        return isValidateTLSCertificates;
    }

    public void setValidateTLSCertificates(boolean validateTLSCertificates) {
        isValidateTLSCertificates = validateTLSCertificates;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}