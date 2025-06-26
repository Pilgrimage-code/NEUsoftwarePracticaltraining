package com.cemh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    
    /**
     * 上传根目录
     */
    public static final String UPLOAD_DIR = "uploads";
    
    /**
     * 视频目录
     */
    public static final String VIDEO_DIR = "videos";
    
    /**
     * 图片目录
     */
    public static final String IMAGE_DIR = "images";
    
    /**
     * 文档目录
     */
    public static final String DOCUMENT_DIR = "documents";
    
    /**
     * 其他文件目录
     */
    public static final String OTHER_DIR = "others";
    
    /**
     * 保存上传的文件
     *
     * @param file     文件
     * @param fileType 文件类型（video, image, document, other）
     * @return 文件访问路径
     * @throws IOException IO异常
     */
    public static String saveFile(MultipartFile file, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件为空");
        }
        
        String directory;
        switch (fileType.toLowerCase()) {
            case "video":
                directory = VIDEO_DIR;
                break;
            case "image":
                directory = IMAGE_DIR;
                break;
            case "document":
                directory = DOCUMENT_DIR;
                break;
            default:
                directory = OTHER_DIR;
        }
        
        // 按日期创建目录
        LocalDate now = LocalDate.now();
        String datePath = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = directory + "/" + datePath;
        
        // 创建目录
        Path uploadPath = Paths.get(UPLOAD_DIR, relativePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String filename = UUID.randomUUID().toString() + extension;
        
        // 保存文件
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath);
        
        logger.info("文件已保存: {}", filePath);
        
        // 返回访问路径（相对于uploads目录）
        return "/uploads/" + relativePath + "/" + filename;
    }
    
    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名（包含点号）
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotPos = filename.lastIndexOf('.');
        if (dotPos < 0) {
            return "";
        }
        return filename.substring(dotPos);
    }
    
    /**
     * 删除文件
     *
     * @param filePath 文件路径（相对于应用根目录）
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        // 去掉开头的斜杠
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            logger.error("删除文件失败: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * 获取文件的MIME类型
     *
     * @param file 文件
     * @return MIME类型
     */
    public static String getMimeType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            logger.error("获取文件MIME类型失败", e);
            return "application/octet-stream";
        }
    }
    
    /**
     * 获取文件大小的友好显示
     *
     * @param size 文件大小（字节）
     * @return 友好显示的文件大小
     */
    public static String getFileSizeDisplay(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
} 