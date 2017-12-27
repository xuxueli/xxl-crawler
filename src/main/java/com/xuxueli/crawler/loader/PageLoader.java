package com.xuxueli.crawler.loader;

import com.xuxueli.crawler.model.PageLoadInfo;
import org.jsoup.nodes.Document;

/**
 * page loader
 *
 * @author xuxueli 2017-12-28 00:27:30
 */
public abstract class PageLoader {

    /**
     * load page
     *
     * @param pageLoadInfo
     * @return
     */
    public abstract Document load(PageLoadInfo pageLoadInfo);

}
