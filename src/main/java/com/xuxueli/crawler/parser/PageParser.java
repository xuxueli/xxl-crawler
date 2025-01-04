package com.xuxueli.crawler.parser;

import com.xuxueli.crawler.pageloader.param.Request;
import com.xuxueli.crawler.pageloader.param.Response;
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
     * pre parse, before "page-load and page-parse"
     *
     * @param request  request
     */
    public void preParse(Request request) {
        // do nothing, can be customized
    }

    /**
     * after parse pageVo, only for success response
     *
     * @param response  response
     */
    public abstract void afterParse(Response<T> response);

    /**
     * parse pageVo, only for fail response
     *
     * @param response
     */
    public void afterParseFail(Response<T> response){
        // default do nothing
    }

}
