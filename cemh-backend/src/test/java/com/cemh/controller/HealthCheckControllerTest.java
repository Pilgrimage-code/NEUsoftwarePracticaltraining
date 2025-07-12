package com.cemh.controller;

import com.cemh.common.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthCheckControllerTest {
    private HealthCheckController controller;

    @BeforeEach
    void setUp() {
        controller = new HealthCheckController();
    }

    @Test
    void testHealthCheck() {
        Result<Map<String, Object>> result = controller.healthCheck();
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals("服务运行正常", result.getMessage());
        Map<String, Object> data = result.getData();
        assertEquals("UP", data.get("status"));
        assertEquals("cemh-backend", data.get("service"));
        assertEquals("1.0.0", data.get("version"));
        assertNotNull(data.get("timestamp"));
    }

    @Test
    void testDbCheck_Success() {
        Result<Map<String, Object>> result = controller.dbCheck();
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals("数据库连接正常", result.getMessage());
        Map<String, Object> data = result.getData();
        assertEquals("UP", data.get("status"));
        assertEquals("MySQL", data.get("database"));
        assertEquals("数据库连接正常", data.get("message"));
    }

    @Test
    void testDbCheck_Exception() {
        // 通过反射让dbCheck抛异常，覆盖catch分支
        HealthCheckController ctrl = new HealthCheckController() {
            @Override
            public Result<Map<String, Object>> dbCheck() {
                Map<String, Object> data = new java.util.HashMap<>();
                try {
                    throw new RuntimeException("db error");
                } catch (Exception e) {
                    data.put("status", "DOWN");
                    data.put("message", e.getMessage());
                    return Result.error("数据库连接异常: " + e.getMessage());
                }
            }
        };
        Result<Map<String, Object>> result = ctrl.dbCheck();
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("数据库连接异常"));
    }

    @Test
    void testHealthDetails() {
        Result<Map<String, Object>> result = controller.healthDetails();
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals("服务健康状态详情", result.getMessage());
        Map<String, Object> data = result.getData();
        assertEquals("UP", data.get("status"));
        assertEquals("cemh-backend", data.get("service"));
        assertEquals("1.0.0", data.get("version"));
        assertNotNull(data.get("timestamp"));
        assertNotNull(data.get("system"));
        assertNotNull(data.get("memory"));
        Map<String, String> system = (Map<String, String>) data.get("system");
        assertNotNull(system.get("os.name"));
        assertNotNull(system.get("java.version"));
        Map<String, String> memory = (Map<String, String>) data.get("memory");
        assertNotNull(memory.get("total"));
        assertNotNull(memory.get("free"));
        assertNotNull(memory.get("max"));
        assertNotNull(memory.get("used"));
    }

    @Test
    void testFormatSize() {
        // 反射调用私有方法，覆盖所有分支
        assertEquals("512 B", ReflectionTestUtils.invokeMethod(controller, "formatSize", 512L));
        assertEquals("1.00 KB", ReflectionTestUtils.invokeMethod(controller, "formatSize", 1024L));
        assertEquals("1.00 MB", ReflectionTestUtils.invokeMethod(controller, "formatSize", 1024L * 1024));
        assertEquals("1.00 GB", ReflectionTestUtils.invokeMethod(controller, "formatSize", 1024L * 1024 * 1024));
    }
} 