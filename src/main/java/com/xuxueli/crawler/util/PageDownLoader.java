package com.xuxueli.crawler.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * page downloader
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class PageDownLoader {
	private static Logger logger = LoggerFactory.getLogger(PageDownLoader.class);

	/**
	 * 根据 url 和 contentType 生成文件名, 去除非文件名字符
	 *
	 * @param url
	 * @param contentType
	 * @return
	 */
	public static String getFileNameByUrl(String url, String contentType) {
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		if (contentType!=null && contentType.lastIndexOf("/")>-1) {
			url += "." + contentType.substring(contentType.lastIndexOf("/") + 1);	// text/html、application/pdf
		}
		return url;
	}

	/**
	 * 生成Html本地文件
	 *
	 * @param htmlData
	 * @param filePath
	 * @param fileName
	 */
	public static void saveHtml(byte[] htmlData, String filePath, String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath, fileName)));
			for (int i = 0; i < htmlData.length; i++){
				out.write(htmlData[i]);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}


}