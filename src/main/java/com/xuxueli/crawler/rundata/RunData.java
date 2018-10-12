package com.xuxueli.crawler.rundata;

/**
 * run data
 *
 * @author xuxueli 2017-12-14 11:40:50
 */
public abstract class RunData {

    /**
     * add link
     *
     * @param link
     * @return
     */
    public abstract boolean addUrl(String link);

    /**
     * get link, remove from unVisitedUrlQueue and add to visitedUrlSet
     *
     * @return
     */
    public abstract String getUrl() throws InterruptedException;

    /**
     * get url num
     *
     * @return
     */
    public abstract int getUrlNum();

}
