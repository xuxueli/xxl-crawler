package com.xuxueli.crawler.rundata;

/**
 * run data
 *
 * @author xuxueli 2017-12-14 11:40:50
 */
public abstract class RunData {

    public abstract boolean addUrl(String link);

    public abstract String getUrl();

    public abstract int getUrlNum();

    public abstract boolean addWhiteUrlRegex(String link);

    public abstract boolean validWhiteUrl(String link);

}
