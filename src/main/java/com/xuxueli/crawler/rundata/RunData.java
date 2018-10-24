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
     * @return boolean
     */
    public abstract boolean addUrl(String link);

    /**
     * get link, remove from unVisitedUrlQueue and add to visitedUrlSet
     *
     * @return String
     */
    public abstract String getUrl();

    /**
     * get url num
     *
     * @return int
     */
    public abstract int getUrlNum();

}
