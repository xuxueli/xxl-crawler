package com.xuxueli.crawler.parser;

import com.xuxueli.crawler.model.PageLoadInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
     *
     * @param pageLoadInfo  page request params
     */
    public void preLoad(PageLoadInfo pageLoadInfo) {
        // TODO
    }

    /**
     * parse pageVo
     *
     * @param html              page html data
     * @param pageVoElement     pageVo html data
     * @param pageVo            pageVo object
     */
    public abstract void parse(Document html, Element pageVoElement, T pageVo);

}
