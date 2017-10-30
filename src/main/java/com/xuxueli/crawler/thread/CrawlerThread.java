package com.xuxueli.crawler.thread;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * crawler thread
 *
 * @author xuxueli 2017-10-10 10:58:19
 */
public class CrawlerThread implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

    private XxlCrawler crawler;
    private boolean running;
    private boolean toStop;
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

                // ------- url ----------
                running = false;
                crawler.tryFinish();
                String link = crawler.takeUrl();
                running = true;

                logger.info(">>>>>>>>>>> xxl crawler, process link : {}", link);
                if (!UrlUtil.isUrl(link)) {
                    continue;
                }

                // ------- html ----------
                Document html = JsoupUtil.load(link, crawler.getParamMap(), crawler.getCookieMap(), crawler.getIfPost(), crawler.getUserAgent());
                if (html == null) {
                    continue;
                }

                // ------- child link list (FIFO队列,广度优先) ----------
                if (crawler.getAllowSpread()) {
                    Set<String> links = JsoupUtil.findLinks(html);
                    if (links != null && links.size() > 0) {
                        for (String item : links) {
                            if (crawler.validWhiteUrl(item)) {
                                crawler.addUrl(item);   // child link, valid white
                            }
                        }
                    }
                }

                // ------- pagevo ----------
                if (!crawler.validWhiteUrl(link)) {
                    continue;   // pagevo parse, valid white
                }

                // pagevo class-field info
                Type[] pageVoClassTypes = ((ParameterizedType)crawler.getPageParser().getClass().getGenericSuperclass()).getActualTypeArguments();
                Class pageVoClassType = (Class) pageVoClassTypes[0];

                PageSelect pageVoSelect = pageVoClassType.getClass().getAnnotation(PageSelect.class);
                String pageVoSelectCss = (pageVoSelect!=null && pageVoSelect.value()!=null && pageVoSelect.value().trim().length()>0)?pageVoSelect.value():"body";

                // pagevo document 2 object
                Elements pageVoElements = html.select(pageVoSelectCss);

                if (pageVoElements != null && pageVoElements.hasText()) {
                    for (Element pageVoElement : pageVoElements) {

                        Object pageVo = pageVoClassType.newInstance();

                        Field[] fields = pageVoClassType.getDeclaredFields();
                        if (fields!=null) {
                            for (Field field: fields) {
                                if (Modifier.isStatic(field.getModifiers())) {
                                    continue;
                                }


                                // field origin value
                                PageFieldSelect fieldSelect = field.getAnnotation(PageFieldSelect.class);
                                String fieldSelectCss = null;
                                String valType = null;
                                if (fieldSelect != null) {
                                    fieldSelectCss = fieldSelect.value();
                                    valType = fieldSelect.valType();
                                }
                                if (fieldSelectCss==null || fieldSelectCss.trim().length()==0) {
                                    continue;
                                }

                                // field value
                                Object fieldValue = null;

                                if (field.getGenericType() instanceof ParameterizedType) {
                                    ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
                                    if (fieldGenericType.getRawType().equals(List.class)) {
                                        //Type gtATA = fieldGenericType.getActualTypeArguments()[0];

                                        Elements fieldElementList = pageVoElement.select(fieldSelectCss);

                                        if (fieldElementList!=null && fieldElementList.size()>0) {

                                            List<Object> fieldValue2 = new ArrayList<Object>();

                                            for (Element fieldElement: fieldElementList) {
                                                String fieldElementOrigin = null;
                                                if ("html".equals(valType)) {
                                                    fieldElementOrigin = fieldElement.html();
                                                } else if ("val".equals(valType)) {
                                                    fieldElementOrigin = fieldElement.val();
                                                } else {
                                                    fieldElementOrigin = fieldElement.text();
                                                }

                                                if (fieldElementOrigin==null || fieldElementOrigin.length()==0) {
                                                    continue;
                                                }

                                                fieldValue2.add(FieldReflectionUtil.parseValue(field, fieldElementOrigin));
                                            }

                                            if (fieldValue2.size() > 1) {
                                                fieldValue = fieldValue2;
                                            }


                                        }
                                    }
                                } else {

                                    Elements fieldElement = pageVoElement.select(fieldSelectCss);

                                    String fieldValueOrigin = null;
                                    if ("html".equals(valType)) {
                                        fieldValueOrigin = fieldElement.html();
                                    } else if ("val".equals(valType)) {
                                        fieldValueOrigin = fieldElement.val();
                                    } else {
                                        fieldValueOrigin = fieldElement.text();
                                    }

                                    if (fieldValueOrigin==null || fieldValueOrigin.length()==0) {
                                        continue;
                                    }

                                    fieldValue = FieldReflectionUtil.parseValue(field, fieldValueOrigin);
                                }

                                if (fieldValue!=null) {
                                    field.setAccessible(true);
                                    field.set(pageVo, fieldValue);
                                }
                            }
                        }

                        // pagevo output
                        crawler.getPageParser().parse(link, html, pageVo);
                    }
                }
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    logger.info(">>>>>>>>>>> xxl crawler thread is interrupted. {}", e.getMessage());
                } else {
                    logger.error(e.getMessage(), e);
                }
            }

        }
    }
}