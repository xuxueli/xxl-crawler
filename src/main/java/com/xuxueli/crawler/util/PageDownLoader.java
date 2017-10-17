package com.xuxueli.crawler.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 *
	 * @param url
	 * @param contentType
	 * @return
	 */
	private static String getFileNameByUrl(String url, String contentType) {
		url = url.substring(7);
		url = url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);	// text/html、application/pdf
		return url;
	}

	/**
	 * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
	 *
	 * @param data
	 * @param filePath
	 * @param fileName
	 */
	private static void saveToLocal(byte[] data, String filePath, String fileName) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath, fileName)));
			for (int i = 0; i < data.length; i++){
				out.write(data[i]);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 下载 url 指向的网页
	 *
	 * @param url
	 * @return
	 */
	public static boolean downloadFile(String url, String filePath) {

		// 设置请求和传输超时时间
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.build(); 
		httpGet.setConfig(requestConfig);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		// 3.执行 HTTP GET 请求
		try {
			HttpResponse response = httpClient.execute(httpGet);
			// 解析请求
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK && null != entity){
				byte[] responseBody = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
				
				// 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
				String fileName = getFileNameByUrl(url,	entity.getContentType().getValue());
				
				// 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
				saveToLocal(responseBody, filePath, fileName);
				return true;
			} else {
				logger.error("load page fail, statusCode {}", statusCode);
			}

		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally{
			httpGet.releaseConnection();
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return false;
	}

}