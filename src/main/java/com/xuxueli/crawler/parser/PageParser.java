package com.xuxueli.crawler.parser;

import com.xuxueli.crawler.model.PageLoadInfo;
import org.jsoup.nodes.Document;

/**
 * page parser
 *
 * @author xuxueli 2017-10-17 18:50:40
 *
 * @param <T>   PageVo
 */
public abstract class PageParser<T> {

    /**
     * pre page load
     */
    public void preLoad(PageLoadInfo pageLoadInfo) {
        // TODO
    }

    /**
     * post page load
     * @param html
     */
    public void postLoad(Document html) {
        // TODO
    }

    /**
     * parse pageVo
     *
     * @param html
     * @param pageVo
     */
    public abstract void parse(Document html, T pageVo);

}
