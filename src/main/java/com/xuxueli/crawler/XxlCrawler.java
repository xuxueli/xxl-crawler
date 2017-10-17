package com.xuxueli.crawler;

import com.xuxueli.crawler.thread.CrawlerThread;
import com.xuxueli.crawler.util.RegexUtil;
import com.xuxueli.crawler.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *  xxl crawler
 *
 *  Created by xuxueli on 2015-05-14 22:44:43
 */
public class XxlCrawler {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawler.class);

    private Set<String> whiteUrlRegexs; // URL白名单正则，非空时进行URL白名单过滤页面
    private int threadCount = 1;        // 爬虫线程数量
    private volatile LinkedBlockingQueue<String> unVisitedUrlQueue = new LinkedBlockingQueue<String>();  // 未访问过的URL
    private volatile Set<String> visitedUrlSet = Collections.synchronizedSet(new HashSet<String>());;    // 已经访问过的URL
    private ExecutorService crawlers = Executors.newCachedThreadPool();

    // ---------------------- builder ----------------------
    public static class Builder {
        private XxlCrawler crawler = new XxlCrawler();

        public Builder setIndexUrl(String url) {
            crawler.addUrl(url);
            return this;
        }
        public Builder setWhiteUrlRegexs(Set<String> whiteUrlRegexs) {
            crawler.whiteUrlRegexs = whiteUrlRegexs;
            return this;
        }
        public Builder setThreadCount(int threadCount) {
            crawler.threadCount = threadCount;
            return this;
        }
        public XxlCrawler build() {
            return crawler;
        }
    }

    // ---------------------- crawler url ----------------------

    /**
     * url add
     * @param link
     */
    public boolean addUrl(String link) {
        if (!UrlUtil.isUrl(link)) {
            return false; // check URL格式
        }
        if (visitedUrlSet.contains(link)) {
            return false; // check 未访问过
        }
        if (unVisitedUrlQueue.contains(link)) {
            return false; // check 未记录过
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
        unVisitedUrlQueue.add(link);
        logger.info(">>>>>>>>>>> xxl crawler addUrl ： {}", link);
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
    public void start(){
        if (unVisitedUrlQueue.size() < 1) {
            throw new RuntimeException("xxl crawler indexUrl can not be empty");
        }
        if (threadCount<1 || threadCount>1000) {
            throw new RuntimeException("xxl crawler threadCount invalid, threadCount : " + threadCount);
        }
        for (int i = 0; i < threadCount; i++) {
            CrawlerThread crawlerThread = new CrawlerThread(this);
            crawlers.execute(crawlerThread);
        }
        logger.info(">>>>>>>>>>> xxl crawler start ...");
        crawlers.shutdown();

        try {
            while (!crawlers.awaitTermination(3, TimeUnit.SECONDS)) {
                logger.info(">>>>>>>>>>> xxl crawler still running ...");
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            stop();
        }
    }
    public void stop(){
        logger.info(">>>>>>>>>>> xxl crawler stop ...");
        crawlers.shutdownNow();
    }

}
