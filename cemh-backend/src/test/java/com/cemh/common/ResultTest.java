package com.cemh.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testSuccessNoArg() {
        Result<String> result = Result.success();
        assertEquals(200, result.getCode());
        assertNull(result.getData());
    }

    @Test
    void testSuccessWithData() {
        Result<String> result = Result.success("ok");
        assertEquals(200, result.getCode());
        assertEquals("ok", result.getData());
    }

    @Test
    void testSuccessWithMessageAndData() {
        Result<String> result = Result.success("msg", "data");
        assertEquals(200, result.getCode());
        assertEquals("msg", result.getMessage());
        assertEquals("data", result.getData());
    }

    @Test
    void testError() {
        Result<String> result = Result.error("fail");
        assertEquals(500, result.getCode());
        assertEquals("fail", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testErrorWithCodeAndMsg() {
        Result<String> result = Result.error(404, "not found");
        assertEquals(404, result.getCode());
        assertEquals("not found", result.getMessage());
    }

    @Test
    void testErrorNoArg() {
        Result<String> result = Result.error();
        assertEquals(500, result.getCode());
        assertEquals("操作失败", result.getMessage());
    }

    @Test
    void testUnauthorized() {
        Result<String> result = Result.unauthorized();
        assertEquals(401, result.getCode());
        assertEquals("未授权访问", result.getMessage());
        Result<String> result2 = Result.unauthorized("自定义未授权");
        assertEquals(401, result2.getCode());
        assertEquals("自定义未授权", result2.getMessage());
    }

    @Test
    void testForbidden() {
        Result<String> result = Result.forbidden();
        assertEquals(403, result.getCode());
        assertEquals("禁止访问", result.getMessage());
        Result<String> result2 = Result.forbidden("自定义禁止");
        assertEquals(403, result2.getCode());
        assertEquals("自定义禁止", result2.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        Result<Integer> result = new Result<>();
        result.setCode(123);
        result.setMessage("msg");
        result.setData(456);
        assertEquals(123, result.getCode());
        assertEquals("msg", result.getMessage());
        assertEquals(456, result.getData());
    }

    @Test
    void testSetTimestamp() {
        Result<String> result = Result.success();
        result.setTimestamp(123456789L);
        assertEquals(123456789L, result.getTimestamp());
    }

    @Test
    void testToString() {
        Result<String> result = Result.success("abc");
        assertTrue(result.toString().contains("abc"));
    }

    @Test
    void testNullData() {
        Result<String> result = Result.success(null);
        assertNull(result.getData());
    }
} 