package com.xxl.crawler.runconf;

import com.xxl.crawler.constant.Const;
import com.xxl.crawler.pageloader.PageLoader;
import com.xxl.crawler.parser.PageParser;
import com.xxl.crawler.pageloader.strategy.JsoupPageLoader;
import com.xxl.crawler.parser.strategy.PrintPageParser;
import com.xxl.crawler.proxy.ProxyPool;

import java.util.*;

/**
 * run conf
 *
 * @author xuxueli 2017-12-14 15:52:49
 */
public class RunConf {

    private volatile boolean allowSpread = true;                                    // 是否允许扩散爬取，将会以现有URL为起点扩散爬取整站

    private PageLoader pageLoader = new JsoupPageLoader();                          // 页面下载器
    private PageParser pageParser = new PrintPageParser();                          // 页面解析器

    private volatile Map<String, String> paramMap;                                  // 请求参数
    private volatile Map<String, String> headerMap;                                 // 请求Header
    private volatile Map<String, String> cookieMap;                                 // 请求Cookie
    private volatile List<String> userAgentList =
            Collections.synchronizedList(
                    new ArrayList<String>(Arrays.asList(Const.USER_AGENT_CHROME))); // 请求UserAgent
    private volatile String referrer;                                               // 请求Referrer
    private volatile boolean ifPost = false;                                        // 请求方式：true=POST请求、false=GET请求
    private volatile int timeoutMillis = Const.TIMEOUT_MILLIS_DEFAULT;              // 超时时间，毫秒
    private volatile boolean isValidateTLSCertificates = true;                      // 是否验证https
    private volatile ProxyPool proxyPool;                                           // 代理生成器

    private volatile int pauseMillis = 1;                                           // 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
    private volatile int failRetryCount = 0;                                        // 失败重试次数，大于零时生效


    // set/get
    public boolean isAllowSpread() {
        return allowSpread;
    }

    public void setAllowSpread(boolean allowSpread) {
        this.allowSpread = allowSpread;
    }

    public PageLoader getPageLoader() {
        return pageLoader;
    }

    public void setPageLoader(PageLoader pageLoader) {
        this.pageLoader = pageLoader;
    }

    public PageParser getPageParser() {
        return pageParser;
    }

    public void setPageParser(PageParser pageParser) {
        this.pageParser = pageParser;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public List<String> getUserAgentList() {
        return userAgentList;
    }

    public void setUserAgentList(List<String> userAgentList) {
        this.userAgentList = userAgentList;
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

    public ProxyPool getProxyPool() {
        return proxyPool;
    }

    public void setProxyPool(ProxyPool proxyPool) {
        this.proxyPool = proxyPool;
    }

    public int getPauseMillis() {
        return pauseMillis;
    }

    public void setPauseMillis(int pauseMillis) {
        this.pauseMillis = pauseMillis;
    }

    public int getFailRetryCount() {
        return failRetryCount;
    }

    public void setFailRetryCount(int failRetryCount) {
        this.failRetryCount = failRetryCount;
    }

}
