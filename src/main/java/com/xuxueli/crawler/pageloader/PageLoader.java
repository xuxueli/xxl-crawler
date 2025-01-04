package com.xuxueli.crawler.pageloader;

import com.xuxueli.crawler.pageloader.param.Request;
import org.jsoup.nodes.Document;

/**
 * Page Loader
 *
 * @author xuxueli 2017-12-28 00:27:30
 */
public abstract class PageLoader {

    /**
     * load page
     *
     * @param request
     * @return Document
     */
    public abstract Document load(Request request);

}
