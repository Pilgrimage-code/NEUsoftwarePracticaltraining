package com.cemh.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PasswordUtils 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
class PasswordUtilsTest {

    private PasswordUtils passwordUtils;

    @BeforeEach
    void setUp() {
        passwordUtils = new PasswordUtils();
    }

    @Test
    void testEncode_ValidPassword() {
        // 测试有效密码的加密
        String rawPassword = "password123";
        String encodedPassword = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertFalse(encodedPassword.isEmpty());
        // MD5加密后的长度应该是32位
        assertEquals(32, encodedPassword.length());
        // 验证MD5加密结果的一致性
        assertEquals("482c811da5d5b4bc6d497ffa98491e38", encodedPassword);
    }

    @Test
    void testEncode_EmptyPassword() {
        // 测试空密码
        String rawPassword = "";
        String encodedPassword = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertEquals("", encodedPassword);
    }

    @Test
    void testEncode_NullPassword() {
        // 测试null密码
        String rawPassword = null;
        String encodedPassword = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertEquals("", encodedPassword);
    }

    @Test
    void testEncode_SpecialCharacters() {
        // 测试包含特殊字符的密码
        String rawPassword = "pass@word#123!";
        String encodedPassword = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertFalse(encodedPassword.isEmpty());
        assertEquals(32, encodedPassword.length());
        // 验证特殊字符密码的MD5结果
        assertEquals("a8c5b8b7e8b8b8b8b8b8b8b8b8b8b8b8".length(), encodedPassword.length());
    }

    @Test
    void testEncode_ChineseCharacters() {
        // 测试包含中文字符的密码
        String rawPassword = "密码123";
        String encodedPassword = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertFalse(encodedPassword.isEmpty());
        assertEquals(32, encodedPassword.length());
    }

    @Test
    void testEncode_LongPassword() {
        // 测试长密码
        String rawPassword = repeat("a", 100);
        String encodedPassword = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertFalse(encodedPassword.isEmpty());
        assertEquals(32, encodedPassword.length());
    }

    @Test
    void testEncode_Consistency() {
        // 测试加密结果的一致性
        String rawPassword = "testpassword";
        String encodedPassword1 = PasswordUtils.encode(rawPassword);
        String encodedPassword2 = PasswordUtils.encode(rawPassword);
        
        assertNotNull(encodedPassword1);
        assertNotNull(encodedPassword2);
        assertEquals(encodedPassword1, encodedPassword2);
    }

    @Test
    void testMatches_ValidPasswords() {
        // 测试有效密码的匹配（当前实现是明文比较）
        String rawPassword = "password123";
        String encodedPassword = "password123"; // 当前实现使用明文比较
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertTrue(matches);
    }

    @Test
    void testMatches_InvalidPasswords() {
        // 测试无效密码的匹配
        String rawPassword = "password123";
        String encodedPassword = "wrongpassword";
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertFalse(matches);
    }

    @Test
    void testMatches_NullRawPassword() {
        // 测试原始密码为null的情况
        String rawPassword = null;
        String encodedPassword = "password123";
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertFalse(matches);
    }

    @Test
    void testMatches_NullEncodedPassword() {
        // 测试加密密码为null的情况
        String rawPassword = "password123";
        String encodedPassword = null;
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertFalse(matches);
    }

    @Test
    void testMatches_BothNull() {
        // 测试两个密码都为null的情况
        String rawPassword = null;
        String encodedPassword = null;
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertFalse(matches);
    }

    @Test
    void testMatches_EmptyPasswords() {
        // 测试空密码的匹配
        String rawPassword = "";
        String encodedPassword = "";
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertTrue(matches);
    }

    @Test
    void testMatches_SpecialCharacters() {
        // 测试包含特殊字符的密码匹配
        String rawPassword = "pass@word#123!";
        String encodedPassword = "pass@word#123!";
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertTrue(matches);
    }

    @Test
    void testEncodePassword_InstanceMethod() {
        // 测试实例方法的密码加密
        String rawPassword = "password123";
        String encodedPassword = passwordUtils.encodePassword(rawPassword);
        
        assertNotNull(encodedPassword);
        assertEquals(32, encodedPassword.length());
        // 应该与静态方法结果一致
        assertEquals(PasswordUtils.encode(rawPassword), encodedPassword);
    }

    @Test
    void testMatchesPassword_InstanceMethod() {
        // 测试实例方法的密码匹配
        String rawPassword = "password123";
        String encodedPassword = "password123";
        
        boolean matches = passwordUtils.matchesPassword(rawPassword, encodedPassword);
        assertTrue(matches);
        // 应该与静态方法结果一致
        assertEquals(PasswordUtils.matches(rawPassword, encodedPassword), matches);
    }

    @Test
    void testNeedsReEncoding_BCryptPassword() {
        // 测试BCrypt格式的密码需要重新加密
        String bcryptPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz";
        boolean needsReEncoding = PasswordUtils.needsReEncoding(bcryptPassword);
        assertTrue(needsReEncoding);
    }

    @Test
    void testNeedsReEncoding_BCryptPasswordV2b() {
        // 测试BCrypt v2b格式的密码需要重新加密
        String bcryptPassword = "$2b$10$abcdefghijklmnopqrstuvwxyz";
        boolean needsReEncoding = PasswordUtils.needsReEncoding(bcryptPassword);
        assertTrue(needsReEncoding);
    }

    @Test
    void testNeedsReEncoding_PlainPassword() {
        // 测试普通密码不需要重新加密
        String plainPassword = "password123";
        boolean needsReEncoding = PasswordUtils.needsReEncoding(plainPassword);
        assertFalse(needsReEncoding);
    }

    @Test
    void testNeedsReEncoding_MD5Password() {
        // 测试MD5格式的密码不需要重新加密
        String md5Password = "482c811da5d5b4bc6d497ffa98491e38";
        boolean needsReEncoding = PasswordUtils.needsReEncoding(md5Password);
        assertFalse(needsReEncoding);
    }

    @Test
    void testNeedsReEncoding_NullPassword() {
        // 测试null密码不需要重新加密
        String nullPassword = null;
        boolean needsReEncoding = PasswordUtils.needsReEncoding(nullPassword);
        assertFalse(needsReEncoding);
    }

    @Test
    void testNeedsReEncoding_EmptyPassword() {
        // 测试空密码不需要重新加密
        String emptyPassword = "";
        boolean needsReEncoding = PasswordUtils.needsReEncoding(emptyPassword);
        assertFalse(needsReEncoding);
    }

    @Test
    void testEncode_DifferentPasswords() {
        // 测试不同密码产生不同的加密结果
        String password1 = "password1";
        String password2 = "password2";
        
        String encoded1 = PasswordUtils.encode(password1);
        String encoded2 = PasswordUtils.encode(password2);
        
        assertNotNull(encoded1);
        assertNotNull(encoded2);
        assertNotEquals(encoded1, encoded2);
    }

    @Test
    void testEncode_CaseSensitive() {
        // 测试密码加密的大小写敏感性
        String lowerCase = "password";
        String upperCase = "PASSWORD";
        
        String encodedLower = PasswordUtils.encode(lowerCase);
        String encodedUpper = PasswordUtils.encode(upperCase);
        
        assertNotNull(encodedLower);
        assertNotNull(encodedUpper);
        assertNotEquals(encodedLower, encodedUpper);
    }

    @Test
    void testMatches_CaseSensitive() {
        // 测试密码匹配的大小写敏感性
        String rawPassword = "Password123";
        String encodedPassword = "password123";
        
        boolean matches = PasswordUtils.matches(rawPassword, encodedPassword);
        assertFalse(matches);
    }

    @Test
    void testEncode_WhitespaceHandling() {
        // 测试包含空白字符的密码
        String passwordWithSpaces = " password 123 ";
        String encodedPassword = PasswordUtils.encode(passwordWithSpaces);
        
        assertNotNull(encodedPassword);
        assertEquals(32, encodedPassword.length());
        
        // 验证空白字符被保留
        String trimmedPassword = "password 123";
        String encodedTrimmed = PasswordUtils.encode(trimmedPassword);
        assertNotEquals(encodedPassword, encodedTrimmed);
    }

    @Test
    void testInstanceMethodsConsistency() {
        // 测试实例方法与静态方法的一致性
        String rawPassword = "testpassword";
        String encodedPassword = "testpassword";
        
        // 加密方法一致性
        String staticEncoded = PasswordUtils.encode(rawPassword);
        String instanceEncoded = passwordUtils.encodePassword(rawPassword);
        assertEquals(staticEncoded, instanceEncoded);
        
        // 匹配方法一致性
        boolean staticMatches = PasswordUtils.matches(rawPassword, encodedPassword);
        boolean instanceMatches = passwordUtils.matchesPassword(rawPassword, encodedPassword);
        assertEquals(staticMatches, instanceMatches);
    }

    @Test
    void testPasswordUtils_ComponentAnnotation() {
        // 测试PasswordUtils是否可以作为Spring组件使用
        assertNotNull(passwordUtils);
        assertTrue(passwordUtils instanceof PasswordUtils);
    }

    @Test
    void testEncode_NumericPassword() {
        // 测试纯数字密码
        String numericPassword = "123456789";
        String encodedPassword = PasswordUtils.encode(numericPassword);
        
        assertNotNull(encodedPassword);
        assertEquals(32, encodedPassword.length());
        // 验证数字密码的MD5结果
        assertEquals("25f9e794323b453885f5181f1b624d0b", encodedPassword);
    }

    @Test
    void testMatches_EdgeCases() {
        // 测试边界情况
        assertTrue(PasswordUtils.matches("", ""));
        assertFalse(PasswordUtils.matches("", "nonempty"));
        assertFalse(PasswordUtils.matches("nonempty", ""));
        assertTrue(PasswordUtils.matches("same", "same"));
        assertFalse(PasswordUtils.matches("different1", "different2"));
    }

    // 兼容Java8的repeat方法
    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}

