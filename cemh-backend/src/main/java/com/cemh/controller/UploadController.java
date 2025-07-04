package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.utils.AliyunOSSOperator;
import com.cemh.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Tag(name = "文件上传", description = "文件上传相关接口")
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class UploadController {
    
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    
    @Value("${file.upload.path:D:/uploads}")
    private String uploadPath;

    @Value("${file.upload.url-prefix:http://localhost:8080/uploads}")
    private String urlPrefix;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${file.upload.domain:http://localhost}")
    private String domain;
    
    @Operation(summary = "上传图片")
    @PostMapping("/image")
    public Result<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }
        // 检查文件大小（2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.error("文件大小不能超过2MB");
        }
        try {
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String dateStr = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = dateStr + "_" + java.util.UUID.randomUUID().toString() + extension;

            // 保存文件
//            File destFile = new File(uploadDir, filename);
//            file.transferTo(destFile);

            //阿里云保存
            String url = aliyunOSSOperator.upload(file.getBytes(), filename);
            // 返回文件信息
            Map<String, Object> result = new HashMap<>();
            result.put("filename", filename);
            result.put("originalName", originalFilename);
            result.put("size", file.getSize());
//            result.put("url", urlPrefix + "/" + filename);
            //阿里云
            result.put("url", url);
            return Result.success(result);
        } catch (IOException e) {
            logger.error("上传图片失败", e);
            return Result.error("文件上传失败：" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Operation(summary = "上传视频")
    @PostMapping("/video")
    public Result<Map<String, Object>> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            return Result.error("只能上传视频文件");
        }
        // 检查文件大小（50MB）
        if (file.getSize() > 50 * 1024 * 1024) {
            return Result.error("文件大小不能超过50MB");
        }
        try {
            logger.info("上传视频: {}, 大小: {}, 类型: {}", file.getOriginalFilename(), file.getSize(), contentType);
            
            // 创建上传目录
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成文件名 - 使用统一格式：YYYYMMDD_UUID.ext
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String dateStr = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = dateStr + "_" + java.util.UUID.randomUUID().toString() + extension;
            
            // 保存文件 - 使用统一的文件路径结构
            File destFile = new File(uploadDir, filename);
            file.transferTo(destFile);
            
            // 构建URL路径 - 统一格式为 /uploads/YYYYMMDD_UUID.ext
            String filePath = "/uploads/" + filename;
            String fullUrl = buildFullUrl(filePath);
            
            logger.info("视频已保存: {}", destFile.getAbsolutePath());
            logger.info("访问URL: {}", fullUrl);
            
            Map<String, Object> result = new HashMap<>();
            result.put("filename", filename);
            result.put("originalName", file.getOriginalFilename());
            result.put("size", file.getSize());
            result.put("url", fullUrl);
            
            return Result.success(result);
        } catch (IOException e) {
            logger.error("上传视频失败", e);
            return Result.error("视频上传失败：" + e.getMessage());
        }
    }
    
    @Operation(summary = "上传文档")
    @PostMapping("/document")
    public Result<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        // 检查文件大小（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error("文件大小不能超过5MB");
        }
        try {
            logger.info("上传文档: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            //String filePath = FileUtils.saveFile(file, "document");
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String dateStr = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = dateStr + "_" + java.util.UUID.randomUUID().toString() + extension;
            //阿里云保存
            String url = aliyunOSSOperator.upload(file.getBytes(), filename);
            //String url = buildFullUrl(filePath);
            
            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("name", file.getOriginalFilename());
            data.put("size", FileUtils.getFileSizeDisplay(file.getSize()));
            
            return Result.success(data);
        } catch (IOException e) {
            logger.error("上传文档失败", e);
            return Result.error("上传文档失败: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Operation(summary = "上传其他文件")
    @PostMapping("/file")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("上传文件: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            //String filePath = FileUtils.saveFile(file, "other");
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String dateStr = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
            String filename = dateStr + "_" + java.util.UUID.randomUUID().toString() + extension;
            //阿里云保存
            String url = aliyunOSSOperator.upload(file.getBytes(), filename);
            //String url = buildFullUrl(filePath);

            Map<String, String> data = new HashMap<>();
            data.put("url", url);
            data.put("name", file.getOriginalFilename());
            data.put("size", FileUtils.getFileSizeDisplay(file.getSize()));

            return Result.success(data);
        } catch (IOException e) {
            logger.error("上传文件失败", e);
            return Result.error("上传文件失败: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Operation(summary = "删除文件")
    @DeleteMapping("/file")
    public Result<Void> deleteFile(@RequestBody DeleteFileRequest request) {
        if (request.getUrl() == null || request.getUrl().isEmpty()) {
            return Result.error("文件路径不能为空");
        }
        
        logger.info("删除文件: {}", request.getUrl());
        boolean success = FileUtils.deleteFile(request.getUrl());
        
        if (success) {
            return Result.success();
        } else {
            return Result.error("删除文件失败");
        }
    }
    
    private String buildFullUrl(String filePath) {
        // filePath 形如 /uploads/20250627_UUID.mp4 或 /uploads/images/2025/06/27/xxx.png
        
        String path = filePath;
        
        // 检查是否为新格式的视频路径 (/uploads/20250627_xxx.mp4)
        if (filePath.matches("/uploads/\\d{8}_[a-f0-9-]+\\.[a-zA-Z0-9]+")) {
            path = filePath;
        } else {
            // 处理旧格式的路径
            int idx = filePath.indexOf("/images");
            if (idx == -1) idx = filePath.indexOf("/videos");
            if (idx == -1) idx = filePath.indexOf("/documents");
            if (idx == -1) idx = filePath.indexOf("/others");
            if (idx != -1) {
                path = filePath.substring(idx);
            }
        }
        
        // 检查urlPrefix是否已经包含了domain和port
        if (urlPrefix.startsWith("http")) {
            // 如果urlPrefix已经是完整URL，直接使用
            // 确保URL前缀不重复出现
            if (path.startsWith("/") && !urlPrefix.endsWith("/")) {
                return urlPrefix + path;
            } else if (!path.startsWith("/") && urlPrefix.endsWith("/")) {
                return urlPrefix + path;
            } else if (path.startsWith("/") && urlPrefix.endsWith("/")) {
                return urlPrefix + path.substring(1);
            } else {
                return urlPrefix + "/" + path;
            }
        } else {
            // 否则拼接完整URL
            String baseUrl = domain + ":" + serverPort;
            
            // 检查urlPrefix是否以/开头
            if (!urlPrefix.startsWith("/")) {
                urlPrefix = "/" + urlPrefix;
            }
            
            // 检查path是否以/开头
            if (path.startsWith("/")) {
                return baseUrl + urlPrefix + path;
            } else {
                return baseUrl + urlPrefix + "/" + path;
            }
        }
    }
    
    @Data
    public static class DeleteFileRequest {
        private String url;
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}

