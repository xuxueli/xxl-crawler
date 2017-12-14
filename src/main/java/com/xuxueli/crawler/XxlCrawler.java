package com.xuxueli.crawler;

import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.proxy.ProxyMaker;
import com.xuxueli.crawler.rundata.RunData;
import com.xuxueli.crawler.rundata.strategy.LocalRunData;
import com.xuxueli.crawler.thread.CrawlerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  xxl crawler
 *
 *  Created by xuxueli on 2015-05-14 22:44:43
 */
public class XxlCrawler {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawler.class);

    // spread
    private volatile boolean allowSpread = true;                                    // 允许扩散爬取，将会以现有URL为起点扩散爬取整站

    // run data
    private volatile RunData runData = new LocalRunData();

    // site
    private volatile Map<String, String> paramMap;                                  // 请求参数
    private volatile Map<String, String> cookieMap;                                 // 请求Cookie
    private volatile Map<String, String> headerMap;                                 // 请求Header
    private volatile List<String> userAgentList = Collections.synchronizedList(new ArrayList<String>(Arrays.asList(XxlCrawlerConf.USER_AGENT_CHROME)));     // 请求UserAgent
    private volatile String referrer;                                               // 请求Referrer
    private volatile boolean ifPost = false;                                        // 请求方式：true=POST请求、false=GET请求
    private volatile int timeoutMillis = XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT;     // 超时时间，毫秒
    private volatile int pauseMillis = 0;                                           // 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
    private volatile ProxyMaker proxyMaker;                                         // 代理生成器
    private volatile int failRetryCount = 0;                                        // 失败重试次数，大于零时生效

    // thread
    private int threadCount = 1;                                                    // 爬虫线程数量
    private ExecutorService crawlers = Executors.newCachedThreadPool();             // 爬虫线程池
    private List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();     // 爬虫线程引用镜像

    // parser
    private PageParser pageParser;

    // ---------------------- builder ----------------------
    public static class Builder {
        private XxlCrawler crawler = new XxlCrawler();

        // spread
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
         * 设置运行数据类型
         *
         * @param runData
         * @return
         */
        public Builder setRunData(RunData runData){
            crawler.runData = runData;
            return this;
        }

        // run data
        /**
         * 待爬的URL列表
         *
         * @param urls
         * @return
         */
        public Builder setUrls(String... urls) {
            if (urls!=null && urls.length>0) {
                for (String url: urls) {
                    crawler.runData.addUrl(url);
                }
            }
            return this;
        }

        /**
         * URL白名单正则，非空时进行URL白名单过滤页面
         *
         * @param whiteUrlRegexs
         * @return
         */
        public Builder setWhiteUrlRegexs(String... whiteUrlRegexs) {
            if (whiteUrlRegexs!=null && whiteUrlRegexs.length>0) {
                for (String whiteUrlRegex: whiteUrlRegexs) {
                    crawler.runData.addWhiteUrlRegex(whiteUrlRegex);
                }
            }
            return this;
        }

        // site
        /**
         * 请求参数
         *
         * @param paramMap
         * @return
         */
        public Builder setParamMap(Map<String, String> paramMap){
            crawler.paramMap = paramMap;
            return this;
        }

        /**
         * 请求Cookie
         *
         * @param cookieMap
         * @return
         */
        public Builder setCookieMap(Map<String, String> cookieMap){
            crawler.cookieMap = cookieMap;
            return this;
        }

        /**
         * 请求Header
         *
         * @param headerMap
         * @return
         */
        public Builder setHeaderMap(Map<String, String> headerMap){
            crawler.headerMap = headerMap;
            return this;
        }

        /**
         * 请求UserAgent
         *
         * @param userAgents
         * @return
         */
        public Builder setUserAgent(String... userAgents){
            if (userAgents!=null && userAgents.length>0) {
                for (String userAgent: userAgents) {
                    if (!crawler.userAgentList.contains(userAgent)) {
                        crawler.userAgentList.add(userAgent);
                    }
                }
            }
            return this;
        }

        /**
         * 请求Referrer
         *
         * @param referrer
         * @return
         */
        public Builder setReferrer(String referrer){
            crawler.referrer = referrer;
            return this;
        }

        /**
         * 请求方式：true=POST请求、false=GET请求
         *
         * @param ifPost
         * @return
         */
        public Builder setIfPost(boolean ifPost){
            crawler.ifPost = ifPost;
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

        /**
         * 代理生成器
         *
         * @param proxyMaker
         * @return
         */
        public Builder setProxyMaker(ProxyMaker proxyMaker){
            crawler.proxyMaker = proxyMaker;
            return this;
        }

        /**
         * 失败重试次数，大于零时生效
         *
         * @param failRetryCount
         * @return
         */
        public Builder setFailRetryCount(int failRetryCount){
            if (failRetryCount > 0) {
                crawler.failRetryCount = failRetryCount;
            }
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

    public boolean isAllowSpread() {
        return allowSpread;
    }

    public RunData getRunData() {
        return runData;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public List<String> getUserAgentList() {
        return userAgentList;
    }

    public String getReferrer() {
        return referrer;
    }

    public boolean isIfPost() {
        return ifPost;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public int getPauseMillis() {
        return pauseMillis;
    }

    public ProxyMaker getProxyMaker() {
        return proxyMaker;
    }

    public int getFailRetryCount() {
        return failRetryCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public ExecutorService getCrawlers() {
        return crawlers;
    }

    public List<CrawlerThread> getCrawlerThreads() {
        return crawlerThreads;
    }

    public PageParser getPageParser() {
        return pageParser;
    }


    // ---------------------- crawler thread ----------------------

    /**
     * 启动
     *
     * @param sync  true=同步方式、false=异步方式
     */
    public void start(boolean sync){
        if (runData == null) {
            throw new RuntimeException("xxl crawler runData can not be null.");
        }
        if (runData.getUrlNum() <= 0) {
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
        boolean isEnd = runData.getUrlNum()==0 && !isRunning;
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
