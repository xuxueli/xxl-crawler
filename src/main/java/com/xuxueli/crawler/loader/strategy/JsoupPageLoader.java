package com.xuxueli.crawler.loader.strategy;

import com.xuxueli.crawler.loader.PageLoader;
import com.xuxueli.crawler.model.PageRequest;
import com.xuxueli.crawler.util.JsoupUtil;
import org.jsoup.nodes.Document;

/**
 * jsoup page loader
 *
 * @author xuxueli 2017-12-28 00:29:49
 */
public class JsoupPageLoader extends PageLoader {

    @Override
    public Document load(PageRequest pageRequest) {
        return JsoupUtil.load(pageRequest);
    }

}
