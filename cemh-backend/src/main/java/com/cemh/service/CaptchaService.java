package com.cemh.service;

import java.util.Map;

public interface CaptchaService {
    /**
     * 生成图片验证码，返回base64图片和key
     */
    Map<String, Object> generateCaptcha();

    /**
     * 校验验证码
     */
    boolean verify(String key, String code);
} 