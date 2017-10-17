package com.xuxueli.crawler.parser;

import org.jsoup.nodes.Document;

/**
 * page parser
 *
 * @author xuxueli 2017-10-17 18:50:40
 *
 * @param <T>   PageVo
 */
public abstract class PageParser<T> {

    public abstract void parse(String url, Document html, T pageVo);

}
