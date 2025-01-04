package com.xxl.crawler.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
			url += "." + contentType.substring(contentType.lastIndexOf("/") + 1);	// text/html、application/pdf、image/png、image/jpeg
		}
		return url;
	}

	/**
	 * save local file
	 *
	 * @param fileData
	 * @param filePath
	 * @param fileName
	 */
	public static void saveFile(String fileData, String filePath, String fileName) {
        // create file dir
        File filePathDir = new File(filePath);
        if (!filePathDir.exists()) {
            filePathDir.mkdirs();
        }

		// write file data
		File file = new File(filePathDir, fileName);		// Paths.get(filePath) 	//	file.toPath()
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
			writer.write(fileData);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

	/**
	 * download file
	 *
	 * @param fileUrl
	 * @param timeoutMillis
	 * @param filePath
	 * @param fileName
	 */
	public static boolean downFile(String fileUrl, int timeoutMillis, String filePath, String fileName) {
		// 创建文件目录
		File filePathDir = new File(filePath);
		if (!filePathDir.exists()) {
			filePathDir.mkdirs();
		}

		Path outputPath = Paths.get(filePath, fileName);
		try {
			URL url = new URL(fileUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(timeoutMillis);

			try (InputStream inputStream = connection.getInputStream();
				 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(outputPath))) {

				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					bufferedOutputStream.write(buffer, 0, bytesRead);
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("Failed to download file from URL: " + fileUrl + " to path: " + outputPath, e);
			//throw new RuntimeException("Failed to download file from URL: " + fileUrl + " to path: " + outputPath, e);
			return false;
		}
	}


	/**
	 * validate file
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}
}