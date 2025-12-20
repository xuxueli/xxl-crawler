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
 * Page downloader with enhanced file handling capabilities
 *
 * @author xuxueli 2015-05-14 22:44:43
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final int BUFFER_SIZE = 8192; // 增大缓冲区提高IO效率
    private static final int MAX_FILENAME_LENGTH = 255; // 文件名最大长度限制

    /**
     * 根据URL和内容类型生成安全的文件名，移除非法字符
     *
     * @param url         源URL
     * @param contentType 内容类型
     * @return 处理后的文件名
     */
    public static String getFileNameByUrl(String url, String contentType) {
        if (url == null || url.trim().isEmpty()) {
            return "unknown_file";
        }

        // 提取URL中的文件名部分（最后一个/后的内容）
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        if (fileName.isEmpty()) {
            fileName = "index"; // 处理以/结尾的URL
        }

        // 替换所有非法字符（兼容Windows和Linux系统）
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

        // 仅在文件名不含后缀且contentType有效时添加后缀
        if (contentType != null && contentType.contains("/") && !fileName.contains(".")) {
            String suffix = contentType.substring(contentType.lastIndexOf("/") + 1);
            // 过滤包含参数的contentType（如image/jpeg;charset=utf-8）
            if (suffix.contains(";")) {
                suffix = suffix.substring(0, suffix.indexOf(";"));
            }
            if (!suffix.isEmpty()) {
                fileName += "." + suffix;
            }
        }

        // 限制文件名长度，防止超出系统限制
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
     * @param fileData  文件内容字符串
     * @param filePath  文件夹路径
     * @param fileName  文件名
     */
    public static void saveFile(String fileData, String filePath, String fileName) {
        if (fileData == null) {
            logger.error("File data cannot be null");
            throw new IllegalArgumentException("File data cannot be null");
        }

        Path dirPath = Paths.get(filePath);
        Path outputPath = dirPath.resolve(fileName);

        try {
            // 创建目录（支持多级目录）并验证
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                if (!Files.exists(dirPath)) {
                    throw new IOException("Failed to create directory: " + dirPath);
                }
            }

            // 检查文件是否已存在
            if (Files.exists(outputPath)) {
                logger.warn("File already exists, will be overwritten: {}", outputPath);
            }

            // 使用NIO写入文件，指定UTF-8编码
            Files.write(outputPath, fileData.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.error("Failed to save file to: " + outputPath, e);
            throw new RuntimeException("Failed to save file", e);
        }
    }

    /**
     * 从URL下载文件到本地
     *
     * @param fileUrl      待下载文件的URL
     * @param timeoutMillis 超时时间（毫秒）
     * @param filePath     保存目录
     * @param fileName     保存的文件名
     * @return 下载是否成功
     */
    public static boolean downFile(String fileUrl, int timeoutMillis, String filePath, String fileName) {
        // 验证输入参数
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
            // 创建目录
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeoutMillis);
            connection.setReadTimeout(timeoutMillis); // 添加读取超时

            // 下载到临时文件
            try (InputStream inputStream = connection.getInputStream();
                 BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(tempPath))) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // 下载完成后原子性移动到目标路径
            Files.move(tempPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("File downloaded successfully: {}", outputPath);
            return true;
        } catch (MalformedURLException e) {
            logger.error("Invalid URL format: {}", fileUrl, e);
        } catch (IOException e) {
            logger.error("Failed to download file from URL: {} to path: {}", fileUrl, outputPath, e);
        } finally {
            // 清理临时文件（无论成功失败）
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
     * @param filePath 路径
     * @return 是否存在
     */
    public static boolean exists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }
}