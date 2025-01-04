package com.xuxueli.crawler.rundata;

import com.xuxueli.crawler.util.RegexUtil;
import com.xuxueli.crawler.util.UrlUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * run url pool
 *
 * @author xuxueli 2017-12-14 11:40:50
 */
public abstract class RunUrlPool {

    /**
     * white url regex
     */
    private Set<String> whiteUrlRegexs = Collections.synchronizedSet(new HashSet<String>());

    /**
     * add white url regex
     */
    public void addWhiteUrlRegex(String whiteUrlRegex) {
        whiteUrlRegexs.add(whiteUrlRegex);
    }
    /**
     * valid url, include white url
     *
     * @param url
     * @return boolean
     */
    public boolean validUrlRegex(String url){
        if (!UrlUtil.isUrl(url)) {
            return false;   // false if url invalid
        }

        if (whiteUrlRegexs!=null && !whiteUrlRegexs.isEmpty()) {
            boolean underWhiteUrl = false;
            for (String whiteRegex: whiteUrlRegexs) {
                if (RegexUtil.matches(whiteRegex, url)) {
                    underWhiteUrl = true;
                }
            }
            if (!underWhiteUrl) {
                return false;   // check white
            }
        }
        return true;    // true if regex is empty
    }

    // ---------------------- method ----------------------

    /**
     * add url, with regex valid
     *
     * @param url
     * @param validUrlRegex
     *
     * @return boolean
     */
    public abstract boolean addUrl(String url, boolean validUrlRegex);

    /**
     * get url, remove from unVisitedUrlQueue and add to visitedUrlSet
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
