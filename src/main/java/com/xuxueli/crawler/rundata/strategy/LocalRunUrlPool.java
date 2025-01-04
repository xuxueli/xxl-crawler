package com.xuxueli.crawler.rundata.strategy;

import com.xuxueli.crawler.exception.XxlCrawlerException;
import com.xuxueli.crawler.rundata.RunUrlPool;
import com.xuxueli.crawler.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * lcoal run data
 *
 * @author xuxueli 2017-12-14 11:42:23
 */
public class LocalRunUrlPool extends RunUrlPool {
    private static Logger logger = LoggerFactory.getLogger(LocalRunUrlPool.class);

    // url
    private volatile LinkedBlockingQueue<String> unVisitedUrlQueue = new LinkedBlockingQueue<>();             // 待采集URL池
    private volatile Set<String> visitedUrlSet = Collections.synchronizedSet(new HashSet<>());                // 已采集URL池


    /**
     * url add, will not valid
     * @param url
     */
    @Override
    public boolean addUrl(String url, boolean validUrlRegex) {
        // valid
        if (!UrlUtil.isUrl(url)) {
            logger.debug(">>>>>>>>>>> xxl-crawler isUrl fail, url not valid: {}", url);
            return false;
        }

        if (validUrlRegex) {
            if (!validUrlRegex(url)) {
                logger.debug(">>>>>>>>>>> xxl-crawler validUrlRegex fail, url not valid: {}", url);
                return false;
            }
        }

        // avoid repeat
        if (visitedUrlSet.contains(url) || unVisitedUrlQueue.contains(url)) {
            logger.debug(">>>>>>>>>>> xxl-crawler addUrl fail, url repeate: {}", url);
            return false;
        }
        unVisitedUrlQueue.add(url);
        logger.info(">>>>>>>>>>> xxl-crawler addUrl success, url: {}", url);
        return true;
    }

    /**
     * url take
     * @return String
     * @throws InterruptedException
     */
    @Override
    public String getUrl() {
        String link = null;
        try {
            link = unVisitedUrlQueue.take();
        } catch (InterruptedException e) {
            //throw new XxlCrawlerException("getUrl interrupted.");
            logger.debug(">>>>>>>>>>> xxl-crawler getUrl interrupted.");
        }
        if (link != null) {
            visitedUrlSet.add(link);
        }
        return link;
    }

    @Override
    public int getUrlNum() {
        return unVisitedUrlQueue.size();
    }

}
