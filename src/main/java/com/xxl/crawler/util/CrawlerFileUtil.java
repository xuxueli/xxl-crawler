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
import java.nio.file.StandardCopyOption;

/**
 * Crawler File Util
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class CrawlerFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerFileUtil.class);

    /**
     * buffer size
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * max length of filename
     */
    private static final int MAX_FILENAME_LENGTH = 255;

    /**
     * 根据URL和内容类型生成安全的文件名，移除非法字符
     *
     * @param url           origin  url
     * @param contentType   content-type
     * @return fileName
     */
    public static String getFileNameByUrl(String url, String contentType) {
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

    /**
     * 保存字符串数据到本地文件
     *
     * @param fileData      file data
     * @param filePath      file path
     * @param fileName      file name
     */
    public static void saveFile(String fileData, String filePath, String fileName) {
        if (fileData == null) {
            logger.error("File data cannot be null");
            throw new IllegalArgumentException("File data cannot be null");
        }

        Path dirPath = Paths.get(filePath);
        Path outputPath = dirPath.resolve(fileName);

        try {
            // create directory if not exists
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                if (!Files.exists(dirPath)) {
                    throw new IOException("Failed to create directory: " + dirPath);
                }
            }

            // check fileName if exists, will be overwritten if exists
            if (Files.exists(outputPath)) {
                logger.warn("File already exists, will be overwritten: {}", outputPath);
            }

            // write file
            Files.write(outputPath, fileData.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error("Failed to save file to: " + outputPath, e);
            throw new RuntimeException("Failed to save file", e);
        }
    }

    /**
     * 从URL下载文件到本地
     *
     * @param fileUrl           file url
     * @param timeoutMillis     timeout millis
     * @param filePath          file path
     * @param fileName          file name
     * @return whether download success
     */
    public static boolean downFile(String fileUrl, int timeoutMillis, String filePath, String fileName) {
        // parse param
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            logger.error("Invalid file URL: {}", fileUrl);
            return false;
        }
        if (timeoutMillis <= 0) {
            logger.error("Invalid timeout value: {}", timeoutMillis);
            return false;
        }

        Path dirPath = Paths.get(filePath);
        Path outputPath = dirPath.resolve(fileName);
        Path tempPath = outputPath.resolveSibling(fileName + ".tmp"); // 临时文件路径

        try {
            // create directory if not exists
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // download temp file
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeoutMillis);
            connection.setReadTimeout(timeoutMillis); // 添加读取超时

            try (InputStream inputStream = connection.getInputStream();
                 BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(tempPath))) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // move temp file to output file
            Files.move(tempPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("File downloaded successfully: {}", outputPath);
            return true;
        } catch (MalformedURLException e) {
            logger.error("Invalid URL format: {}", fileUrl, e);
        } catch (IOException e) {
            logger.error("Failed to download file from URL: {} to path: {}", fileUrl, outputPath, e);
        } finally {
            // delete temp file
            try {
                if (Files.exists(tempPath)) {
                    Files.delete(tempPath);
                }
            } catch (IOException ex) {
                logger.warn("Failed to delete temporary file: {}", tempPath, ex);
            }
        }
        return false;
    }

    /**
     * 检查文件或目录是否存在
     *
     * @param filePath      check file or directory
     * @return true if exists, false otherwise
     */
    public static boolean exists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }
}