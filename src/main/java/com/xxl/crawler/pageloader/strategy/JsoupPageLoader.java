package com.xxl.crawler.pageloader.strategy;

import com.xxl.crawler.pageloader.PageLoader;
import com.xxl.crawler.pageloader.param.Request;
import com.xxl.crawler.util.JsoupUtil;
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
