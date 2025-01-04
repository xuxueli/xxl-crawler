package com.xuxueli.crawler.thread;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.constant.Const;
import com.xuxueli.crawler.exception.XxlCrawlerException;
import com.xuxueli.crawler.pageloader.param.Request;
import com.xuxueli.crawler.pageloader.param.Response;
import com.xuxueli.crawler.util.FieldReflectionUtil;
import com.xuxueli.crawler.util.JsoupUtil;
import com.xuxueli.crawler.util.UrlUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * crawler thread
 *
 * @author xuxueli 2017-10-10 10:58:19
 */
public class CrawlerThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

    private final XxlCrawler crawler;
    private volatile boolean running;       // wait or parse page
    private volatile boolean toStop;        // thread stop signal
    public CrawlerThread(XxlCrawler crawler) {
        this.crawler = crawler;
        this.running = true;
        this.toStop = false;
    }
    public void toStop() {
        this.toStop = true;
    }
    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {

        while (!toStop) {
            try {

                // cycle run, check if stop
                running = false;
                crawler.tryFinish();

                // tail url, will block if empty
                String link = crawler.getRunUrlPool().getUrl();

                // process url
                running = true;
                if (!UrlUtil.isUrl(link)) {
                    continue;
                }

                // process with failcount
                logger.info(">>>>>>>>>>> xxl crawler, process link : {}", link);
                int runCount = crawler.getRunConf().getFailRetryCount() + 1;
                for (int i = 0; i < runCount; i++) {

                    Response response = null;
                    try {
                        // build request
                        Request request = buildRequest(link);

                        // 1、pre-parse
                        crawler.getRunConf().getPageParser().preParse(request);

                        // 2、load and parse
                        response = loadAndParsePage(request);

                        // 3、after-parse
                        if (response.isSuccess()) {
                            crawler.getRunConf().getPageParser().afterParse(response);
                        } else {
                            crawler.getRunConf().getPageParser().afterParseFail(response);
                        }
                    } catch (Throwable e) {
                        logger.error(">>>>>>>>>>> xxl crawler proocess error.", e);
                    }
                    if (crawler.getRunConf().getPauseMillis() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(crawler.getRunConf().getPauseMillis());
                        } catch (InterruptedException e) {
                            logger.error(">>>>>>>>>>> xxl crawler thread is interrupted. {}", e.getMessage());
                        }
                    }
                    if (response!=null && response.isSuccess()) {
                        break;
                    }
                }

            } catch (Throwable e) {
                if (e instanceof XxlCrawlerException) {
                    logger.error(">>>>>>>>>>> xxl crawler thread {}", e.getMessage(), e);
                } else {
                    logger.error(e.getMessage(), e);
                }
            }

        }
    }

    /**
     * build request
     *
     * @param url
     * @return Request
     */
    private Request buildRequest(String url){
        String userAgent = crawler.getRunConf().getUserAgentList().size()>1
                ?crawler.getRunConf().getUserAgentList().get(new Random().nextInt(crawler.getRunConf().getUserAgentList().size()))
                :crawler.getRunConf().getUserAgentList().size()==1?crawler.getRunConf().getUserAgentList().get(0):null;
        Proxy proxy = null;
        if (crawler.getRunConf().getProxyPool() != null) {
            proxy = crawler.getRunConf().getProxyPool().getProxy();
        }

        Request pageRequest = new Request();
        pageRequest.setUrl(url);
        pageRequest.setParamMap(crawler.getRunConf().getParamMap());
        pageRequest.setHeaderMap(crawler.getRunConf().getHeaderMap());
        pageRequest.setCookieMap(crawler.getRunConf().getCookieMap());
        pageRequest.setUserAgent(userAgent);
        pageRequest.setReferrer(crawler.getRunConf().getReferrer());
        pageRequest.setIfPost(crawler.getRunConf().isIfPost());
        pageRequest.setTimeoutMillis(crawler.getRunConf().getTimeoutMillis());
        pageRequest.setValidateTLSCertificates(crawler.getRunConf().isValidateTLSCertificates());
        pageRequest.setProxy(proxy);

        return pageRequest;
    }

    /**
     * load and parse page
     *
     * @param request
     * @return boolean
     */
    private Response loadAndParsePage(Request request) throws IllegalAccessException, InstantiationException {

        // load page
        Document html = null;
        try {
            html = crawler.getRunConf().getPageLoader().load(request);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (html == null) {
            return new Response(request, false, null, null, null);
        }

        // spread find url (FIFO队列,广度优先)
        if (crawler.getRunConf().isAllowSpread()) {     // limit child spread
            Set<String> urlSet = JsoupUtil.findLinks(html);
            if (urlSet != null && !urlSet.isEmpty()) {
                for (String item : urlSet) {
                    crawler.getRunUrlPool().addUrl(item, true);
                }
            }
        }

        // parse page
        if (!crawler.getRunUrlPool().validUrlRegex(request.getUrl())) {     // limit unvalid-page parse, only allow spread child, finish here
            return new Response(request, true, html, null, null);
        }

        // pageVo ClassType
        Class pageVoClassType = Object.class;
        Type pageVoParserClass = crawler.getRunConf().getPageParser().getClass().getGenericSuperclass();
        if (pageVoParserClass instanceof ParameterizedType) {
            Type[] pageVoClassTypes = ((ParameterizedType)pageVoParserClass).getActualTypeArguments();
            pageVoClassType = (Class) pageVoClassTypes[0];
        }

        // result
        List<Element> parseElementList = new ArrayList<>();
        List<Object> parseVoList = new ArrayList<>();

        // parseElementList 2 parseVoList
        PageSelect pageVoSelect = (PageSelect) pageVoClassType.getAnnotation(PageSelect.class);
        String pageVoCssQuery = (pageVoSelect!=null && pageVoSelect.cssQuery()!=null && pageVoSelect.cssQuery().trim().length()>0)?pageVoSelect.cssQuery():"html";
        Elements pageVoElements = html.select(pageVoCssQuery);

        if (pageVoElements!=null && pageVoElements.hasText()) {
            for (Element pageVoElement : pageVoElements) {

                // build pageVo
                Object pageVo = pageVoClassType.newInstance();

                // parse pageVo-field
                Field[] fields = pageVoClassType.getDeclaredFields();
                if (fields!=null && fields.length>0) {
                    for (Field field: fields) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }

                        // field origin value
                        PageFieldSelect fieldSelect = field.getAnnotation(PageFieldSelect.class);
                        String cssQuery = null;
                        Const.SelectType selectType = null;
                        String selectVal = null;
                        if (fieldSelect != null) {
                            cssQuery = fieldSelect.cssQuery();
                            selectType = fieldSelect.selectType();
                            selectVal = fieldSelect.selectVal();
                        }
                        if (cssQuery==null || cssQuery.trim().isEmpty()) {
                            continue;
                        }

                        // field value
                        Object fieldValue = null;
                        if (field.getGenericType() instanceof ParameterizedType) {
                            // parse field-list
                            ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
                            if (fieldGenericType.getRawType().equals(List.class)) {

                                // Type gtATA = fieldGenericType.getActualTypeArguments()[0];
                                Elements fieldElementList = pageVoElement.select(cssQuery);
                                if (fieldElementList!=null && !fieldElementList.isEmpty()) {

                                    List<Object> fieldValueTmp = new ArrayList<Object>();
                                    for (Element fieldElement: fieldElementList) {

                                        // get field-value
                                        String fieldElementOrigin = JsoupUtil.parseElement(fieldElement, selectType, selectVal);
                                        if (fieldElementOrigin==null || fieldElementOrigin.length()==0) {
                                            continue;
                                        }
                                        try {
                                            fieldValueTmp.add(FieldReflectionUtil.parseValue(field, fieldElementOrigin));
                                        } catch (Exception e) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }

                                    if (!fieldValueTmp.isEmpty()) {
                                        fieldValue = fieldValueTmp;
                                    }
                                }
                            }
                        } else {
                            // parse field-item
                            Elements fieldElements = pageVoElement.select(cssQuery);
                            String fieldValueOrigin = null;
                            if (fieldElements!=null && !fieldElements.isEmpty()) {
                                fieldValueOrigin = JsoupUtil.parseElement(fieldElements.get(0), selectType, selectVal);
                            }

                            if (fieldValueOrigin==null || fieldValueOrigin.length()==0) {
                                continue;
                            }

                            try {
                                fieldValue = FieldReflectionUtil.parseValue(field, fieldValueOrigin);
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }

                        if (fieldValue!=null) {
                            /*PropertyDescriptor pd = new PropertyDescriptor(field.getName(), pageVoClassType);
                            Method method = pd.getWriteMethod();
                            method.invoke(pageVo, fieldValue);*/

                            field.setAccessible(true);
                            field.set(pageVo, fieldValue);
                        }
                    }
                }

                // fill result
                parseElementList.add(pageVoElement);
                parseVoList.add(pageVo);
            }
        }

        return new Response(request, true, html, parseElementList, parseVoList);
    }

}