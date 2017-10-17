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

                // ------- page ----------

                // html
                Document html = JsoupUtil.load(link, null, null, false);
                if (html == null) {
                    continue;
                }

                // pagevo class-field info
                Type[] pageVoClassTypes = ((ParameterizedType)crawler.getPageParser().getClass().getGenericSuperclass()).getActualTypeArguments();
                Class pageVoClassType = (Class) pageVoClassTypes[0];

                PageSelect pageVoSelect = pageVoClassType.getClass().getAnnotation(PageSelect.class);
                String pageVoSelectCss = (pageVoSelect!=null && pageVoSelect.value()!=null && pageVoSelect.value().trim().length()>0)?pageVoSelect.value():"body";

                // pagevo document 2 object
                Elements pageVoElements = html.select(pageVoSelectCss);

                if (pageVoElements != null && pageVoElements.hasText()) {
                    for (Element pageVoItem : pageVoElements) {

                        Object pageVo = pageVoClassType.newInstance();

                        Field[] fields = pageVoClassType.getDeclaredFields();
                        if (fields!=null) {
                            for (Field field: fields) {
                                if (Modifier.isStatic(field.getModifiers())) {
                                    continue;
                                }

                                // field origin value
                                PageFieldSelect fieldSelect = field.getAnnotation(PageFieldSelect.class);
                                String fieldSelectCss = (fieldSelect!=null && fieldSelect.value()!=null && fieldSelect.value().trim().length()>0)?fieldSelect.value():null;
                                if (fieldSelectCss == null) {
                                    continue;
                                }

                                String fieldValueOrigin = pageVoItem.select(fieldSelectCss).html().trim();
                                if (fieldValueOrigin.length() == 0) {
                                    continue;
                                }

                                // field object value
                                Object fieldValue = FieldReflectionUtil.parseValue(field, fieldValueOrigin);

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

                // ------- child link list (FIFO队列,广度优先) ----------
                Set<String> links = JsoupUtil.findLinks(html);
                if (links != null && links.size() > 0) {
                    for (String item : links) {
                        crawler.addUrl(item);
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