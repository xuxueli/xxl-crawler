package com.xuxueli.crawler.parser.strategy;

import com.xuxueli.crawler.pageloader.param.Request;
import com.xuxueli.crawler.pageloader.param.Response;
import com.xuxueli.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * print page parser
 *
 * @author xuxueli 2024-01-04
 */
public class PrintPageParser extends PageParser<Object> {
    private static final Logger logger = LoggerFactory.getLogger(PrintPageParser.class);

    @Override
    public void preParse(Request request) {
        super.preParse(request);
    }

    @Override
    public void afterParse(Response<Object> response) {
        logger.info("url={}, pageVo={}", (response!=null&&response.getHtml()!=null?response.getHtml().location():null),
                (response.getParseVoList()!=null?response.getParseVoList().toString():null));
    }
}
