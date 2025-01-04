package com.xuxueli.crawler.pageloader.strategy;

import com.xuxueli.crawler.pageloader.PageLoader;
import com.xuxueli.crawler.pageloader.param.Request;
import com.xuxueli.crawler.util.JsoupUtil;
import org.jsoup.nodes.Document;

/**
 * jsoup page loader
 *
 * @author xuxueli 2017-12-28 00:29:49
 */
public class JsoupPageLoader extends PageLoader {

    @Override
    public Document load(Request request) {
        return JsoupUtil.load(request);
    }

}
