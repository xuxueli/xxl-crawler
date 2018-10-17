package com.xuxueli.crawler.parser.strategy;

import com.xuxueli.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * non page parser
 *
 * @author xuxueli 2018-10-17
 */
public abstract class NonPageParser extends PageParser {

    @Override
    public void parse(Document html, Element pageVoElement, Object pageVo) {
        // TODO，not parse page, output page source
    }

    /**
     * @param url
     * @param pageSource
     */
    public abstract void parse(String url, String pageSource);

}
