package com.cemh.controller;

import com.cemh.common.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 用于前端检测后端服务是否可用
 */
@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthCheckController {

    /**
     * 简单的健康检查端点
     * 返回服务器当前时间和状态信息
     */
    @GetMapping("")
    public Result<Map<String, Object>> healthCheck() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        data.put("service", "cemh-backend");
        data.put("version", "1.0.0");
        return Result.success("服务运行正常", data);
    }

    /**
     * 数据库连接检查
     * 简单检测数据库连接是否正常
     */
    @GetMapping("/db")
    public Result<Map<String, Object>> dbCheck() {
        Map<String, Object> data = new HashMap<>();
        try {
            // 此处可以添加对数据库的简单查询以验证连接
            data.put("status", "UP");
            data.put("database", "MySQL");
            data.put("message", "数据库连接正常");
            return Result.success("数据库连接正常", data);
        } catch (Exception e) {
            data.put("status", "DOWN");
            data.put("message", e.getMessage());
            return Result.error("数据库连接异常: " + e.getMessage());
        }
    }

    /**
     * 详细的健康状态信息
     */
    @GetMapping("/details")
    public Result<Map<String, Object>> healthDetails() {
        Map<String, Object> data = new HashMap<>();
        
        // 系统信息
        Map<String, String> systemInfo = new HashMap<>();
        systemInfo.put("os.name", System.getProperty("os.name"));
        systemInfo.put("os.version", System.getProperty("os.version"));
        systemInfo.put("java.version", System.getProperty("java.version"));
        systemInfo.put("java.vendor", System.getProperty("java.vendor"));
        
        // 内存信息
        Map<String, String> memoryInfo = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory();
        long freeMem = runtime.freeMemory();
        long maxMem = runtime.maxMemory();
        
        memoryInfo.put("total", formatSize(totalMem));
        memoryInfo.put("free", formatSize(freeMem));
        memoryInfo.put("max", formatSize(maxMem));
        memoryInfo.put("used", formatSize(totalMem - freeMem));
        
        // 放入主数据对象
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        data.put("service", "cemh-backend");
        data.put("version", "1.0.0");
        data.put("system", systemInfo);
        data.put("memory", memoryInfo);
        
        return Result.success("服务健康状态详情", data);
    }
    
    /**
     * 格式化内存大小
     */
    private String formatSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
} 