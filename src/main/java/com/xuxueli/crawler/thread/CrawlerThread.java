package com.xuxueli.crawler.thread;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.util.JsoupUtil;
import com.xuxueli.crawler.util.UrlUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
                running = false;
                String link = crawler.takeUrl();
                running = true;

                logger.info(">>>>>>>>>>> xxl crawler, link : {}", link);
                if (!UrlUtil.isUrl(link)) {
                    continue;
                }

                // ------- 解析 糗百页面 start ----------
                // 组装规则
                Map<Integer, Set<String>> tagMap = new HashMap<Integer, Set<String>>();
                Set<String> tagNameList = new HashSet<String>();
                tagNameList.add("article");
                tagMap.put(2, tagNameList);

                // 获取html
                Elements resultAll = JsoupUtil.loadParse(link, null, null, false, tagMap);

                // 解析html
                Set<String> result = new HashSet<String>();
                if (resultAll != null && resultAll.hasText())
                    for (Element item : resultAll) {
                        String content = item.getElementsByClass("content").html();
                        String thumb = item.getElementsByClass("thumb").html();
                        String video_holder = item.getElementsByClass("video_holder").html();

                        StringBuffer buffer = new StringBuffer();
                        buffer.append(content);
                        if (thumb != null && thumb.trim().length() > 0) {
                            buffer.append("<hr>");
                            buffer.append(thumb);
                        }
                        if (video_holder != null && video_holder.trim().length() > 0) {
                            buffer.append("<hr>");
                            buffer.append(video_holder);
                        }
                        String str = buffer.toString();
                        if (str != null && str.trim().length() > 0) {
                            result.add(str);
                        }
                    }
                for (String content : result) {
                    logger.info(content);
                }
                // ------- 解析 糗百页面 end ----------

                // 爬取子节点：爬取子链接 (FIFO队列,广度优先)
                Set<String> links = JsoupUtil.findLinks(link);
                if (links != null && links.size() > 0) {
                    for (String item : links) {
                        crawler.addUrl(item);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }
    }
}