package com.xuxueli.crawler.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
	 * @return String
	 */
	public static String getFileNameByUrl(String url, String contentType) {
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		if (contentType!=null && contentType.lastIndexOf("/")>-1) {
			url += "." + contentType.substring(contentType.lastIndexOf("/") + 1);	// text/html、application/pdf
		}
		return url;
	}

	/**
	 * 保存文本文件
	 *
	 * @param fileData
	 * @param filePath
	 * @param fileName
	 */
	public static void saveFile(String fileData, String filePath, String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath, fileName)));
			out.writeChars(fileData);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 下载文件
	 *
	 * @param fileUrl
	 * @param timeoutMillis
	 * @param filePath
	 * @param fileName
	 */
	public static boolean downFile(String fileUrl, int timeoutMillis, String filePath, String fileName) {

		try {
			URL url = new URL(fileUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(timeoutMillis);

			InputStream inputStream = connection.getInputStream();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName)));

			byte[] buf = new byte[1024];
			int size;
			while (-1 != (size = inputStream.read(buf))) {
				bufferedOutputStream.write(buf, 0, size);
			}
			bufferedOutputStream.close();
			inputStream.close();

			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}


}