package com.cemh.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class CaptchaServiceImplTest {
    private CaptchaServiceImpl captchaService;

    @BeforeEach
    void setUp() {
        captchaService = new CaptchaServiceImpl();
        // 清空静态存储，避免污染
        try {
            Field field = CaptchaServiceImpl.class.getDeclaredField("CAPTCHA_STORE");
            field.setAccessible(true);
            ((Map<?, ?>) field.get(null)).clear();
        } catch (Exception ignored) {}
    }

    @Test
    void testGenerateCaptcha_ReturnsKeyAndImg() {
        Map<String, Object> result = captchaService.generateCaptcha();
        assertNotNull(result.get("img"));
        assertNotNull(result.get("key"));
        assertTrue(result.get("img").toString().contains("image"));
    }

    @Test
    void testVerify_SuccessAndRemove() throws Exception {
        // 手动写入验证码
        String key = UUID.randomUUID().toString();
        String code = "AbCd";
        setCaptchaStore(key, code.toLowerCase());
        // 正确校验（忽略大小写、去空格）
        assertTrue(captchaService.verify(key, "  abcd  "));
        // 再次校验已被移除
        assertFalse(captchaService.verify(key, "abcd"));
    }

    @Test
    void testVerify_Fail_WrongCode() throws Exception {
        String key = UUID.randomUUID().toString();
        setCaptchaStore(key, "1234");
        assertFalse(captchaService.verify(key, "abcd"));
    }

    @Test
    void testVerify_KeyOrCodeNull() {
        assertFalse(captchaService.verify(null, "abcd"));
        assertFalse(captchaService.verify("key", null));
    }

    @Test
    void testVerify_KeyNotExist() {
        String randomKey = UUID.randomUUID().toString();
        assertFalse(captchaService.verify(randomKey, "abcd"));
    }

    // 工具方法：反射写入静态CAPTCHA_STORE
    private void setCaptchaStore(String key, String value) throws Exception {
        Field field = CaptchaServiceImpl.class.getDeclaredField("CAPTCHA_STORE");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> store = (Map<String, String>) field.get(null);
        store.put(key, value);
    }
} 