package com.xuxueli.crawler.util;

import com.xuxueli.crawler.conf.XxlCrawlerConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * proxy ip util
 *
 * @author xuxueli 2017-11-08 13:06:55
 */
public class ProxyIpUtil {
    private static Logger logger = LoggerFactory.getLogger(ProxyIpUtil.class);

    /**
     * check proxy
     *
     * @param proxy
     * @param validSite
     * @return int
     */
    public static int checkProxy(Proxy proxy, String validSite){
        try {
            URL url = new URL(validSite!=null?validSite:XxlCrawlerConf.SITE_BAIDU);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            httpURLConnection.setRequestProperty("User-Agent", XxlCrawlerConf.USER_AGENT_CHROME);
            httpURLConnection.setConnectTimeout(XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT);
            httpURLConnection.setReadTimeout(XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT);

            httpURLConnection.connect();
            int statusCode = httpURLConnection.getResponseCode();

            /*InputStream inputStream = httpURLConnection.getInputStream();
            String content = IOUtil.toString(inputStream, null);
            if(content.indexOf("百度") == -1){
                logger.info(content);
                return -1;
            }*/

            return statusCode;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return -2;
        }
    }

    /**
     * check proxy, repeat 3 times
     *
     * @param proxy
     * @param validSite
     * @return int
     */
    public static int checkProxyRepeat(Proxy proxy, String validSite){
        for (int i = 0; i < 3; i++) {
            int statusCode = checkProxy(proxy, validSite);
            if (statusCode > 0) {
                return statusCode;
            }
        }
        return -2;
    }

}
