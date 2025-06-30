package com.cemh.service.impl;

import com.cemh.service.CaptchaService;
import org.springframework.stereotype.Service;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

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
        return Map.of(
            "img", "data:image/png;base64," + captcha.toBase64(),
            "key", key
        );
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