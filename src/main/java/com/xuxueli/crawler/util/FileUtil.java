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
public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

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
	 * 保存Html至本地文件
	 *
	 * @param fileData
	 * @param filePath
	 * @param fileName
	 */
	public static void saveFile(byte[] fileData, String filePath, String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath, fileName)));
			for (int i = 0; i < fileData.length; i++){
				out.write(fileData[i]);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}


}