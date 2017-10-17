package com.xuxueli.crawler;

import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.thread.CrawlerThread;
import com.xuxueli.crawler.util.RegexUtil;
import com.xuxueli.crawler.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
    private List<CrawlerThread> crawlerThreads = new ArrayList<CrawlerThread>();

    private PageParser pageParser;

    // ---------------------- builder ----------------------
    public static class Builder {
        private XxlCrawler crawler = new XxlCrawler();

        public Builder setUrls(Set<String> urlSet) {
            if (urlSet!=null && urlSet.size()>0) {
                for (String url: urlSet) {
                    crawler.addUrl(url);
                }
            }
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
        public Builder setPageParser(PageParser pageParser){
            crawler.pageParser = pageParser;
            return this;
        }

        public XxlCrawler build() {
            return crawler;
        }
    }

    // ---------------------- crawler url ----------------------


    public PageParser getPageParser() {
        return pageParser;
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
    public void start(){
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
            crawlers.execute(crawlerThread);
            crawlerThreads.add(crawlerThread);
        }
        crawlers.shutdown();
        /*try {
            while (!crawlers.awaitTermination(3, TimeUnit.SECONDS)) {
                logger.info(">>>>>>>>>>> xxl crawler still running ...");
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            stop();
        }*/
    }

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

    public void stop(){
        for (CrawlerThread crawlerThread: crawlerThreads) {
            crawlerThread.toStop();
        }
        crawlers.shutdownNow();
        logger.info(">>>>>>>>>>> xxl crawler stop.");
    }

}
