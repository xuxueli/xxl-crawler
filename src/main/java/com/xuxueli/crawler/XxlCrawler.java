package com.xuxueli.crawler;

import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.thread.CrawlerThread;
import com.xuxueli.crawler.util.RegexUtil;
import com.xuxueli.crawler.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 *  xxl crawler
 *
 *  Created by xuxueli on 2015-05-14 22:44:43
 */
public class XxlCrawler {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawler.class);

    // url
    private volatile LinkedBlockingQueue<String> unVisitedUrlQueue = new LinkedBlockingQueue<String>();  // 未访问过的URL
    private volatile Set<String> visitedUrlSet = Collections.synchronizedSet(new HashSet<String>());;    // 已经访问过的URL
    private volatile boolean allowSpread = true;   // 允许扩散爬取，将会以现有URL为起点扩散爬取整站
    private Set<String> whiteUrlRegexs; // URL白名单正则，非空时进行URL白名单过滤页面

    // site
    private volatile boolean ifPost = false;                                    // 请求方式：true=POST请求、false=GET请求
    private volatile String userAgent = XxlCrawlerConf.USER_AGENT_SAMPLE;       // UserAgent
    private volatile Map<String, String> paramMap;                              // 请求参数
    private volatile Map<String, String> cookieMap;                             // 请求Cookie
    private volatile int timeoutMillis = XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT; // 超时时间，毫秒
    private volatile int pauseMillis = 0;                                       // 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；

    // thread
    private int threadCount = 1;        // 爬虫线程数量
    private ExecutorService crawlers = Executors.newCachedThreadPool();
    private List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();

    // parser
    private PageParser pageParser;

    // ---------------------- builder ----------------------
    public static class Builder {
        private XxlCrawler crawler = new XxlCrawler();

        // url
        /**
         * 待爬的URL列表
         *
         * @param urlSet
         * @return
         */
        public Builder setUrls(Set<String> urlSet) {
            if (urlSet!=null && urlSet.size()>0) {
                for (String url: urlSet) {
                    crawler.addUrl(url);
                }
            }
            return this;
        }

        /**
         * 允许扩散爬取，将会以现有URL为起点扩散爬取整站
         *
         * @param allowSpread
         * @return
         */
        public Builder setAllowSpread(boolean allowSpread) {
            crawler.allowSpread = allowSpread;
            return this;
        }

        /**
         * URL白名单正则，非空时进行URL白名单过滤页面
         *
         * @param whiteUrlRegexs
         * @return
         */
        public Builder setWhiteUrlRegexs(Set<String> whiteUrlRegexs) {
            crawler.whiteUrlRegexs = whiteUrlRegexs;
            return this;
        }

        // site
        /**
         * 请求方式：true=POST请求、false=GET请求
         *
         * @param ifPost
         * @return
         */
        private Builder setIfPost(boolean ifPost){
            crawler.ifPost = ifPost;
            return this;
        }

        /**
         * UserAgent
         *
         * @param userAgent
         * @return
         */
        private Builder setUserAgent(String userAgent){
            crawler.userAgent = userAgent;
            return this;
        }

        /**
         * 请求参数
         *
         * @param paramMap
         * @return
         */
        private Builder setParamMap(Map<String, String> paramMap){
            crawler.paramMap = paramMap;
            return this;
        }

        /**
         * 请求Cookie
         *
         * @param cookieMap
         * @return
         */
        private Builder setCookieMap(Map<String, String> cookieMap){
            crawler.cookieMap = cookieMap;
            return this;
        }

        /**
         * 超时时间，毫秒
         *
         * @param timeoutMillis
         * @return
         */
        public Builder setTimeoutMillis(int timeoutMillis){
            crawler.timeoutMillis = timeoutMillis;
            return this;
        }

        /**
         * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
         *
         * @param pauseMillis
         * @return
         */
        public Builder setPauseMillis(int pauseMillis){
            crawler.pauseMillis = pauseMillis;
            return this;
        }


        // thread
        /**
         * 爬虫并发线程数
         *
         * @param threadCount
         * @return
         */
        public Builder setThreadCount(int threadCount) {
            crawler.threadCount = threadCount;
            return this;
        }

        // parser
        /**
         * 页面解析器
         *
         * @param pageParser
         * @return
         */
        public Builder setPageParser(PageParser pageParser){
            crawler.pageParser = pageParser;
            return this;
        }

        public XxlCrawler build() {
            return crawler;
        }
    }

    // ---------------------- set/get ----------------------
    public boolean getIfPost() {
        return ifPost;
    }

    public boolean getAllowSpread() {
        return allowSpread;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public PageParser getPageParser() {
        return pageParser;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public int getPauseMillis() {
        return pauseMillis;
    }

    // ---------------------- crawler url ----------------------

    /**
     * valid url, include white url
     * @param link
     * @return
     */
    public boolean validWhiteUrl(String link){
        if (!UrlUtil.isUrl(link)) {
            return false; // check URL格式
        }

        if (whiteUrlRegexs!=null && whiteUrlRegexs.size()>0) {
            boolean underWhiteUrl = false;
            for (String whiteRegex: whiteUrlRegexs) {
                if (RegexUtil.matches(whiteRegex, link)) {
                    underWhiteUrl = true;
                }
            }
            if (!underWhiteUrl) {
                return false; // check 白名单
            }
        }
        return true;
    }

    /**
     * url add
     * @param link
     */
    public boolean addUrl(String link) {
        if (!UrlUtil.isUrl(link)) {
            logger.debug(">>>>>>>>>>> xxl-crawler addUrl fail, link not valid: {}", link);
            return false; // check URL格式
        }
        if (visitedUrlSet.contains(link)) {
            logger.debug(">>>>>>>>>>> xxl-crawler addUrl fail, link repeate: {}", link);
            return false; // check 未访问过
        }
        if (unVisitedUrlQueue.contains(link)) {
            logger.debug(">>>>>>>>>>> xxl-crawler addUrl fail, link visited: {}", link);
            return false; // check 未记录过
        }
        unVisitedUrlQueue.add(link);
        logger.info(">>>>>>>>>>> xxl-crawler addUrl success, link: {}", link);
        return true;
    }

    /**
     * url take
     * @return
     * @throws InterruptedException
     */
    public String takeUrl() throws InterruptedException {
        String link = unVisitedUrlQueue.take();
        if (link != null) {
            visitedUrlSet.add(link);
        }
        return link;
    }

    // ---------------------- crawler thread ----------------------

    /**
     * 启动
     *
     * @param sync  true=同步方式、false=异步方式
     */
    public void start(boolean sync){
        if (unVisitedUrlQueue.size() < 1) {
            throw new RuntimeException("xxl crawler indexUrl can not be empty.");
        }
        if (threadCount<1 || threadCount>1000) {
            throw new RuntimeException("xxl crawler threadCount invalid, threadCount : " + threadCount);
        }
        if (pageParser == null) {
            throw new RuntimeException("xxl crawler pageParser can not be null.");
        }

        logger.info(">>>>>>>>>>> xxl crawler start ...");
        for (int i = 0; i < threadCount; i++) {
            CrawlerThread crawlerThread = new CrawlerThread(this);
            crawlerThreads.add(crawlerThread);
        }
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlers.execute(crawlerThread);
        }
        crawlers.shutdown();

        if (sync) {
            try {
                while (!crawlers.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.info(">>>>>>>>>>> xxl crawler still running ...");
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 尝试终止
     */
    public void tryFinish(){
        boolean isRunning = false;
        for (CrawlerThread crawlerThread: crawlerThreads) {
            if (crawlerThread.isRunning()) {
                isRunning = true;
                break;
            }
        }
        boolean isEnd = unVisitedUrlQueue.size()==0 && !isRunning;
        if (isEnd) {
            logger.info(">>>>>>>>>>> xxl crawler is finished.");
            stop();
        }
    }

    /**
     * 终止
     */
    public void stop(){
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlerThread.toStop();
        }
        crawlers.shutdownNow();
        logger.info(">>>>>>>>>>> xxl crawler stop.");
    }

}
