package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    
    @Operation(summary = "上传图片")
    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("上传图片: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            String filePath = FileUtils.saveFile(file, "image");
            
            Map<String, String> data = new HashMap<>();
            data.put("url", filePath);
            data.put("name", file.getOriginalFilename());
            data.put("size", FileUtils.getFileSizeDisplay(file.getSize()));
            
            return Result.success(data);
        } catch (IOException e) {
            logger.error("上传图片失败", e);
            return Result.error("上传图片失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "上传视频")
    @PostMapping("/video")
    public Result<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("上传视频: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            String filePath = FileUtils.saveFile(file, "video");
            
            Map<String, String> data = new HashMap<>();
            data.put("url", filePath);
            data.put("name", file.getOriginalFilename());
            data.put("size", FileUtils.getFileSizeDisplay(file.getSize()));
            
            return Result.success(data);
        } catch (IOException e) {
            logger.error("上传视频失败", e);
            return Result.error("上传视频失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "上传文档")
    @PostMapping("/document")
    public Result<Map<String, String>> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("上传文档: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            String filePath = FileUtils.saveFile(file, "document");
            
            Map<String, String> data = new HashMap<>();
            data.put("url", filePath);
            data.put("name", file.getOriginalFilename());
            data.put("size", FileUtils.getFileSizeDisplay(file.getSize()));
            
            return Result.success(data);
        } catch (IOException e) {
            logger.error("上传文档失败", e);
            return Result.error("上传文档失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "上传其他文件")
    @PostMapping("/file")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("上传文件: {}, 大小: {}", file.getOriginalFilename(), file.getSize());
            String filePath = FileUtils.saveFile(file, "other");
            
            Map<String, String> data = new HashMap<>();
            data.put("url", filePath);
            data.put("name", file.getOriginalFilename());
            data.put("size", FileUtils.getFileSizeDisplay(file.getSize()));
            
            return Result.success(data);
        } catch (IOException e) {
            logger.error("上传文件失败", e);
            return Result.error("上传文件失败: " + e.getMessage());
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
    
    @Data
    public static class DeleteFileRequest {
        private String url;
    }
}

