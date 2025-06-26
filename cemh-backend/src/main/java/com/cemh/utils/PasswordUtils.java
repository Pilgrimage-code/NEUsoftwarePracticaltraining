package com.cemh.utils;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码工具类 - 简化版本（不使用BCrypt）
 */
@Component
public class PasswordUtils {
    
    /**
     * 加密密码 - 使用MD5（简单方式）
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return "";
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(rawPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 如果MD5不可用，直接返回原始密码（最简单的方式）
            return rawPassword;
        }
    }
    
    /**
     * 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        // 暂时使用明文比较，后续需要改为加密比较
        return rawPassword.equals(encodedPassword);
    }
    
    /**
     * 实例方法 - 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encodePassword(String rawPassword) {
        return encode(rawPassword);
    }
    
    /**
     * 实例方法 - 验证密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return matches(rawPassword, encodedPassword);
    }
    
    /**
     * 检查密码是否需要重新加密
     * @param encodedPassword 数据库中的密码
     * @return 是否需要重新加密
     */
    public static boolean needsReEncoding(String encodedPassword) {
        // BCrypt格式的密码需要重新加密
        return encodedPassword != null && 
               (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$"));
    }
}

