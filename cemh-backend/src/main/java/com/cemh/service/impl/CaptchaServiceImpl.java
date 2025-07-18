package com.cemh.service.impl;

import com.cemh.service.CaptchaService;
import org.springframework.stereotype.Service;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    // 内存存储验证码，生产环境建议用Redis
    private static final Map<String, String> CAPTCHA_STORE = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> generateCaptcha() {
        SpecCaptcha captcha = new SpecCaptcha(120, 40, 4);
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        String code = captcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        CAPTCHA_STORE.put(key, code);

        String base64 = captcha.toBase64();
        String img;
        if (base64.startsWith("data:image")) {
            img = base64;
        } else {
            img = "data:image/png;base64," + base64;
        }
        
        // 使用Java 8兼容的方式创建Map
        Map<String, Object> result = new HashMap<>();
        result.put("img", img);
        result.put("key", key);
        return result;
    }

    @Override
    public boolean verify(String key, String code) {
        if (key == null || code == null) return false;
        String real = CAPTCHA_STORE.get(key);
        if (real != null && real.equalsIgnoreCase(code.trim())) {
            CAPTCHA_STORE.remove(key);
            return true;
        }
        return false;
    }
} 