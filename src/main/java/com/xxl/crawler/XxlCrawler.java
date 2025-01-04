package com.xxl.crawler;

import com.xxl.crawler.pageloader.PageLoader;
import com.xxl.crawler.runconf.RunConf;
import com.xxl.crawler.pageparser.PageParser;
import com.xxl.crawler.proxy.ProxyPool;
import com.xxl.crawler.rundata.RunUrlPool;
import com.xxl.crawler.rundata.strategy.LocalRunUrlPool;
import com.xxl.crawler.thread.CrawlerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
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

    // run UrlPool
    private volatile RunUrlPool runUrlPool = new LocalRunUrlPool();                                     // 运行时 UrlPool

    // run conf
    private volatile RunConf runConf = new RunConf();                                                   // 运行时配置

    // thread
    private volatile int threadCount = 1;                                                               // 爬虫线程数量
    private volatile ExecutorService crawlers = Executors.newCachedThreadPool();                        // 爬虫线程池
    private volatile List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();    // 爬虫线程引用镜像

    // ---------------------- get ----------------------

    public RunUrlPool getRunUrlPool() {
        return runUrlPool;
    }

    public RunConf getRunConf() {
        return runConf;
    }

    // ---------------------- builder ----------------------
    public static class Builder {
        private XxlCrawler crawler = new XxlCrawler();

        /**
         * set RunUrlPool
         *
         * @param runData
         * @return Builder
         */
        public Builder setRunUrlPool(RunUrlPool runData){
            crawler.runUrlPool = runData;
            return this;
        }

        /**
         * add url to RunUrlPool
         *
         * @param urls
         * @return Builder
         */
        public Builder setUrls(String... urls) {
            if (urls!=null && urls.length>0) {
                for (String url: urls) {
                    crawler.runUrlPool.addUrl(url, false);
                }
            }
            return this;
        }

        /**
         * URL白名单正则，非空时进行URL白名单过滤页面
         *
         * @param whiteUrlRegexs
         * @return Builder
         */
        public Builder setWhiteUrlRegexs(String... whiteUrlRegexs) {
            if (whiteUrlRegexs!=null && whiteUrlRegexs.length>0) {
                for (String whiteUrlRegex: whiteUrlRegexs) {
                    crawler.runUrlPool.addWhiteUrlRegex(whiteUrlRegex);
                }
            }
            return this;
        }

        /**
         * 允许扩散爬取，将会以现有URL为起点扩散爬取整站
         *
         * @param allowSpread
         * @return Builder
         */
        public Builder setAllowSpread(boolean allowSpread) {
            crawler.runConf.setAllowSpread(allowSpread);
            return this;
        }

        /**
         * 页面下载器
         *
         * @param pageLoader
         * @return Builder
         */
        public Builder setPageLoader(PageLoader pageLoader){
            crawler.runConf.setPageLoader(pageLoader);
            return this;
        }

        /**
         * 页面解析器
         *
         * @param pageParser
         * @return Builder
         */
        public Builder setPageParser(PageParser pageParser){
            crawler.runConf.setPageParser(pageParser);
            return this;
        }

        /**
         * 请求参数
         *
         * @param paramMap
         * @return Builder
         */
        public Builder setParamMap(Map<String, String> paramMap){
            crawler.runConf.setParamMap(paramMap);
            return this;
        }

        /**
         * 请求Header
         *
         * @param headerMap
         * @return Builder
         */
        public Builder setHeaderMap(Map<String, String> headerMap){
            crawler.runConf.setHeaderMap(headerMap);
            return this;
        }

        /**
         * 请求Cookie
         *
         * @param cookieMap
         * @return Builder
         */
        public Builder setCookieMap(Map<String, String> cookieMap){
            crawler.runConf.setCookieMap(cookieMap);
            return this;
        }

        /**
         * 请求UserAgent
         *
         * @param userAgents
         * @return Builder
         */
        public Builder setUserAgent(String... userAgents){
            if (userAgents!=null && userAgents.length>0) {
                for (String userAgent: userAgents) {
                    if (!crawler.runConf.getUserAgentList().contains(userAgent)) {
                        crawler.runConf.getUserAgentList().add(userAgent);
                    }
                }
            }
            return this;
        }

        /**
         * 请求Referrer
         *
         * @param referrer
         * @return Builder
         */
        public Builder setReferrer(String referrer){
            crawler.runConf.setReferrer(referrer);
            return this;
        }

        /**
         * 请求方式：true=POST请求、false=GET请求
         *
         * @param ifPost
         * @return Builder
         */
        public Builder setIfPost(boolean ifPost){
            crawler.runConf.setIfPost(ifPost);
            return this;
        }

        /**
         * 超时时间，毫秒
         *
         * @param timeoutMillis
         * @return Builder
         */
        public Builder setTimeoutMillis(int timeoutMillis){
            crawler.runConf.setTimeoutMillis(timeoutMillis);
            return this;
        }

        /**
         * 是否验证https
         *
         * @param validateTLSCertificates
         * @return
         */
        public Builder setValidateTLSCertificates(boolean validateTLSCertificates){
            crawler.runConf.setValidateTLSCertificates(validateTLSCertificates);
            return this;
        }

        /**
         * 代理生成器
         *
         * @param proxyPool
         * @return Builder
         */
        public Builder setProxyPool(ProxyPool proxyPool){
            crawler.runConf.setProxyPool(proxyPool);
            return this;
        }

        /**
         * 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
         *
         * @param pauseMillis
         * @return Builder
         */
        public Builder setPauseMillis(int pauseMillis){
            crawler.runConf.setPauseMillis(pauseMillis);
            return this;
        }

        /**
         * 失败重试次数，大于零时生效
         *
         * @param failRetryCount
         * @return Builder
         */
        public Builder setFailRetryCount(int failRetryCount){
            if (failRetryCount > 0) {
                crawler.runConf.setFailRetryCount(failRetryCount);
            }
            return this;
        }

        /**
         * 爬虫并发线程数
         *
         * @param threadCount
         * @return Builder
         */
        public Builder setThreadCount(int threadCount) {
            crawler.threadCount = threadCount;
            return this;
        }

        public XxlCrawler build() {
            return crawler;
        }
    }


    // ---------------------- crawler thread ----------------------

    /**
     * start crawler thread-pool
     *
     * @param sync  true=同步方式、false=异步方式
     */
    public void start(boolean sync){
        if (runUrlPool == null) {
            throw new RuntimeException("xxl crawler runUrlPool can not be null.");
        }
        if (runUrlPool.getUrlNum() <= 0) {
            throw new RuntimeException("xxl crawler indexUrl can not be empty.");
        }
        if (runConf == null) {
            throw new RuntimeException("xxl crawler runConf can not be empty.");
        }
        if (threadCount<1 || threadCount>1000) {
            throw new RuntimeException("xxl crawler threadCount invalid, threadCount : " + threadCount);
        }
        if (runConf.getPageLoader() == null) {
            throw new RuntimeException("xxl crawler pageLoader can not be null.");
        }
        if (runConf.getPageParser() == null) {
            throw new RuntimeException("xxl crawler pageParser can not be null.");
        }
        if (!(runConf.getTimeoutMillis()>0 && runConf.getTimeoutMillis() <= 5 * 60 * 1000)) {
            throw new RuntimeException("xxl crawler timeoutMillis invalid.");
        }
        if (!(runConf.getPauseMillis()>=0 && runConf.getPauseMillis() <= 10 * 60 * 1000)) {
            throw new RuntimeException("xxl crawler pauseMillis invalid.");
        }
        if (!(runConf.getFailRetryCount()>=0 && runConf.getFailRetryCount() <= 100)) {
            throw new RuntimeException("xxl crawler failRetryCount invalid.");
        }

        logger.info(">>>>>>>>>>> xxl crawler start ...");
        for (int i = 0; i < threadCount; i++) {
            CrawlerThread crawlerThread = new CrawlerThread(this);
            crawlerThreads.add(crawlerThread);
        }
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlers.execute(crawlerThread);
        }
        crawlers.shutdown();        // stop accept new thread, until all thread done

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
     * try stop, may be fail
     */
    public void tryFinish(){
        boolean isRunning = false;
        for (CrawlerThread crawlerThread: crawlerThreads) {
            if (crawlerThread.isRunning()) {
                isRunning = true;
                break;
            }
        }
        boolean isEnd = runUrlPool.getUrlNum()==0 && !isRunning;
        if (isEnd) {
            //logger.info(">>>>>>>>>>> xxl crawler is finished.");
            stop();
        }
    }

    /**
     * stop now
     */
    public void stop(){
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlerThread.toStop();
        }
        crawlers.shutdownNow();     // stop all thread now
        logger.info(">>>>>>>>>>> xxl crawler stop.");
    }

}
