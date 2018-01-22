package com.xuxueli.crawler.util;

import java.util.regex.Pattern;

/**
 * url tool
 *
 * @author xuxueli 2017-10-10 14:57:05
 */
public class UrlUtil {

    /**
     * url格式校验
     */
    public static boolean isUrl(String url) {
        String urlPattern = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        if (url!=null && url.trim().length()>0 && Pattern.matches(urlPattern, url)) {
            return true;
        }
        return false;
    }

}
