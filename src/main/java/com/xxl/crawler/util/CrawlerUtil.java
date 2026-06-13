package com.xxl.crawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crawler File Util
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class CrawlerUtil {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerUtil.class);


    /**
     * max length of filename
     */
    private static final int MAX_FILENAME_LENGTH = 255;


    /**
     * 根据 URL 和 内容类型 生成安全的文件名，移除非法字符
     *
     * @param url           origin  url
     * @param contentType   content-type
     */
    public static String generateFileNameWithUrl(String url, String contentType) {
        // valid url
        if (url == null || url.trim().isEmpty()) {
            return "unknown_file";
        }

        // parse fileName from url
        String fileName = url;

        // filter url after last "/"
        /*String fileName = url.substring(url.lastIndexOf('/') + 1);
        if (fileName.isEmpty()) {
            fileName = "index"; // parse url end with "/"
        }*/

        // replace all invalid-characters with "_", for windows and linux
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

        // append suffix to fileName
        if (contentType != null && contentType.contains("/")) {         //  ignore "." : /*!fileName.contains(".") &&*/
			// such as: text/html、application/pdf、image/png、image/jpeg
            String suffix = contentType.substring(contentType.lastIndexOf("/") + 1);
            // filter parameters of contentType, like : application/pdf;charset=utf-8
            if (suffix.contains(";")) {
                suffix = suffix.substring(0, suffix.indexOf(";"));
            }
            if (!suffix.isEmpty()) {
                fileName += "." + suffix;
            }
        }

        // limit length of filename, avoid filename too long
        if (fileName.length() > MAX_FILENAME_LENGTH) {
            int extIndex = fileName.lastIndexOf('.');
            if (extIndex > 0 && extIndex < fileName.length() - 1) {
                String extension = fileName.substring(extIndex);
                int maxNameLength = MAX_FILENAME_LENGTH - extension.length();
                fileName = fileName.substring(0, maxNameLength) + extension;
            } else {
                fileName = fileName.substring(0, MAX_FILENAME_LENGTH);
            }
        }

        return fileName;
    }


}