package com.cemh.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RegisterUserDTO 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
class RegisterUserDTOTest {

    private RegisterUserDTO registerUserDTO;

    @BeforeEach
    void setUp() {
        registerUserDTO = new RegisterUserDTO();
    }

    // 兼容Java8的repeat方法
    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    @Test
    void testDefaultConstructor() {
        RegisterUserDTO dto = new RegisterUserDTO();
        assertNotNull(dto);
        assertNull(dto.getTenantId());
        assertNull(dto.getDeptId());
        assertNull(dto.getUsername());
        assertNull(dto.getNickname());
        assertNull(dto.getPassword());
        assertNull(dto.getPhone());
        assertNull(dto.getEmail());
        assertNull(dto.getAvatar());
        assertNull(dto.getCaptcha());
        assertNull(dto.getCaptchaKey());
    }

    @Test
    void testTenantIdGetterAndSetter() {
        Long tenantId = 1001L;
        registerUserDTO.setTenantId(tenantId);
        assertEquals(tenantId, registerUserDTO.getTenantId());

        // 测试null值
        registerUserDTO.setTenantId(null);
        assertNull(registerUserDTO.getTenantId());

        // 测试边界值
        Long maxTenantId = Long.MAX_VALUE;
        registerUserDTO.setTenantId(maxTenantId);
        assertEquals(maxTenantId, registerUserDTO.getTenantId());

        Long minTenantId = 0L;
        registerUserDTO.setTenantId(minTenantId);
        assertEquals(minTenantId, registerUserDTO.getTenantId());
    }

    @Test
    void testDeptIdGetterAndSetter() {
        Long deptId = 2001L;
        registerUserDTO.setDeptId(deptId);
        assertEquals(deptId, registerUserDTO.getDeptId());

        // 测试null值
        registerUserDTO.setDeptId(null);
        assertNull(registerUserDTO.getDeptId());

        // 测试边界值
        Long maxDeptId = Long.MAX_VALUE;
        registerUserDTO.setDeptId(maxDeptId);
        assertEquals(maxDeptId, registerUserDTO.getDeptId());
    }

    @Test
    void testUsernameGetterAndSetter() {
        String username = "testuser";
        registerUserDTO.setUsername(username);
        assertEquals(username, registerUserDTO.getUsername());

        // 测试null值
        registerUserDTO.setUsername(null);
        assertNull(registerUserDTO.getUsername());

        // 测试空字符串
        registerUserDTO.setUsername("");
        assertEquals("", registerUserDTO.getUsername());

        // 测试包含特殊字符的用户名
        String specialUsername = "test@user_123";
        registerUserDTO.setUsername(specialUsername);
        assertEquals(specialUsername, registerUserDTO.getUsername());

        // 测试长用户名
        String longUsername = repeat("a", 100);
        registerUserDTO.setUsername(longUsername);
        assertEquals(longUsername, registerUserDTO.getUsername());
    }

    @Test
    void testNicknameGetterAndSetter() {
        String nickname = "测试昵称";
        registerUserDTO.setNickname(nickname);
        assertEquals(nickname, registerUserDTO.getNickname());

        // 测试null值
        registerUserDTO.setNickname(null);
        assertNull(registerUserDTO.getNickname());

        // 测试空字符串
        registerUserDTO.setNickname("");
        assertEquals("", registerUserDTO.getNickname());

        // 测试包含特殊字符和中文的昵称
        String specialNickname = "测试@昵称_123";
        registerUserDTO.setNickname(specialNickname);
        assertEquals(specialNickname, registerUserDTO.getNickname());
    }

    @Test
    void testPasswordGetterAndSetter() {
        String password = "testpassword123";
        registerUserDTO.setPassword(password);
        assertEquals(password, registerUserDTO.getPassword());

        // 测试null值
        registerUserDTO.setPassword(null);
        assertNull(registerUserDTO.getPassword());

        // 测试空字符串
        registerUserDTO.setPassword("");
        assertEquals("", registerUserDTO.getPassword());

        // 测试包含特殊字符的密码
        String specialPassword = "Test@Pass#123!";
        registerUserDTO.setPassword(specialPassword);
        assertEquals(specialPassword, registerUserDTO.getPassword());
    }

    @Test
    void testPhoneGetterAndSetter() {
        String phone = "13800138000";
        registerUserDTO.setPhone(phone);
        assertEquals(phone, registerUserDTO.getPhone());

        // 测试null值
        registerUserDTO.setPhone(null);
        assertNull(registerUserDTO.getPhone());

        // 测试包含特殊字符的电话号码
        String phoneWithDash = "138-0013-8000";
        registerUserDTO.setPhone(phoneWithDash);
        assertEquals(phoneWithDash, registerUserDTO.getPhone());

        // 测试国际电话号码格式
        String internationalPhone = "+86-138-0013-8000";
        registerUserDTO.setPhone(internationalPhone);
        assertEquals(internationalPhone, registerUserDTO.getPhone());
    }

    @Test
    void testEmailGetterAndSetter() {
        String email = "test@example.com";
        registerUserDTO.setEmail(email);
        assertEquals(email, registerUserDTO.getEmail());

        // 测试null值
        registerUserDTO.setEmail(null);
        assertNull(registerUserDTO.getEmail());

        // 测试空字符串
        registerUserDTO.setEmail("");
        assertEquals("", registerUserDTO.getEmail());

        // 测试复杂邮箱格式
        String complexEmail = "test.user+tag@example.co.uk";
        registerUserDTO.setEmail(complexEmail);
        assertEquals(complexEmail, registerUserDTO.getEmail());
    }

    @Test
    void testAvatarGetterAndSetter() {
        String avatar = "https://example.com/avatar.jpg";
        registerUserDTO.setAvatar(avatar);
        assertEquals(avatar, registerUserDTO.getAvatar());

        // 测试null值
        registerUserDTO.setAvatar(null);
        assertNull(registerUserDTO.getAvatar());

        // 测试空字符串
        registerUserDTO.setAvatar("");
        assertEquals("", registerUserDTO.getAvatar());

        // 测试相对路径
        String relativePath = "/images/avatar.png";
        registerUserDTO.setAvatar(relativePath);
        assertEquals(relativePath, registerUserDTO.getAvatar());
    }

    @Test
    void testCaptchaGetterAndSetter() {
        String captcha = "ABCD";
        registerUserDTO.setCaptcha(captcha);
        assertEquals(captcha, registerUserDTO.getCaptcha());

        // 测试null值
        registerUserDTO.setCaptcha(null);
        assertNull(registerUserDTO.getCaptcha());

        // 测试数字验证码
        String numericCaptcha = "1234";
        registerUserDTO.setCaptcha(numericCaptcha);
        assertEquals(numericCaptcha, registerUserDTO.getCaptcha());

        // 测试混合验证码
        String mixedCaptcha = "A1B2";
        registerUserDTO.setCaptcha(mixedCaptcha);
        assertEquals(mixedCaptcha, registerUserDTO.getCaptcha());
    }

    @Test
    void testCaptchaKeyGetterAndSetter() {
        String captchaKey = "captcha-key-123";
        registerUserDTO.setCaptchaKey(captchaKey);
        assertEquals(captchaKey, registerUserDTO.getCaptchaKey());

        // 测试null值
        registerUserDTO.setCaptchaKey(null);
        assertNull(registerUserDTO.getCaptchaKey());

        // 测试UUID格式的key
        String uuidKey = "550e8400-e29b-41d4-a716-446655440000";
        registerUserDTO.setCaptchaKey(uuidKey);
        assertEquals(uuidKey, registerUserDTO.getCaptchaKey());
    }

    @Test
    void testAllFieldsSetAndGet() {
        // 设置所有字段
        Long tenantId = 1001L;
        Long deptId = 2001L;
        String username = "testuser";
        String nickname = "测试用户";
        String password = "password123";
        String phone = "13800138000";
        String email = "test@example.com";
        String avatar = "https://example.com/avatar.jpg";
        String captcha = "ABCD";
        String captchaKey = "captcha-key-123";

        registerUserDTO.setTenantId(tenantId);
        registerUserDTO.setDeptId(deptId);
        registerUserDTO.setUsername(username);
        registerUserDTO.setNickname(nickname);
        registerUserDTO.setPassword(password);
        registerUserDTO.setPhone(phone);
        registerUserDTO.setEmail(email);
        registerUserDTO.setAvatar(avatar);
        registerUserDTO.setCaptcha(captcha);
        registerUserDTO.setCaptchaKey(captchaKey);

        // 验证所有字段
        assertEquals(tenantId, registerUserDTO.getTenantId());
        assertEquals(deptId, registerUserDTO.getDeptId());
        assertEquals(username, registerUserDTO.getUsername());
        assertEquals(nickname, registerUserDTO.getNickname());
        assertEquals(password, registerUserDTO.getPassword());
        assertEquals(phone, registerUserDTO.getPhone());
        assertEquals(email, registerUserDTO.getEmail());
        assertEquals(avatar, registerUserDTO.getAvatar());
        assertEquals(captcha, registerUserDTO.getCaptcha());
        assertEquals(captchaKey, registerUserDTO.getCaptchaKey());
    }

    @Test
    void testFieldsIndependence() {
        // 测试字段之间的独立性
        registerUserDTO.setUsername("user1");
        registerUserDTO.setNickname("nickname1");
        
        // 修改一个字段不应该影响其他字段
        registerUserDTO.setUsername("user2");
        assertEquals("user2", registerUserDTO.getUsername());
        assertEquals("nickname1", registerUserDTO.getNickname());
    }

    @Test
    void testNullSafety() {
        // 测试所有字段都可以安全地设置为null
        registerUserDTO.setTenantId(null);
        registerUserDTO.setDeptId(null);
        registerUserDTO.setUsername(null);
        registerUserDTO.setNickname(null);
        registerUserDTO.setPassword(null);
        registerUserDTO.setPhone(null);
        registerUserDTO.setEmail(null);
        registerUserDTO.setAvatar(null);
        registerUserDTO.setCaptcha(null);
        registerUserDTO.setCaptchaKey(null);

        // 验证所有字段都为null
        assertNull(registerUserDTO.getTenantId());
        assertNull(registerUserDTO.getDeptId());
        assertNull(registerUserDTO.getUsername());
        assertNull(registerUserDTO.getNickname());
        assertNull(registerUserDTO.getPassword());
        assertNull(registerUserDTO.getPhone());
        assertNull(registerUserDTO.getEmail());
        assertNull(registerUserDTO.getAvatar());
        assertNull(registerUserDTO.getCaptcha());
        assertNull(registerUserDTO.getCaptchaKey());
    }

    @Test
    void testEmptyStringHandling() {
        // 测试空字符串的处理
        registerUserDTO.setUsername("");
        registerUserDTO.setNickname("");
        registerUserDTO.setPassword("");
        registerUserDTO.setPhone("");
        registerUserDTO.setEmail("");
        registerUserDTO.setAvatar("");
        registerUserDTO.setCaptcha("");
        registerUserDTO.setCaptchaKey("");

        // 验证空字符串被正确存储
        assertEquals("", registerUserDTO.getUsername());
        assertEquals("", registerUserDTO.getNickname());
        assertEquals("", registerUserDTO.getPassword());
        assertEquals("", registerUserDTO.getPhone());
        assertEquals("", registerUserDTO.getEmail());
        assertEquals("", registerUserDTO.getAvatar());
        assertEquals("", registerUserDTO.getCaptcha());
        assertEquals("", registerUserDTO.getCaptchaKey());
    }

    @Test
    void testWhitespaceHandling() {
        // 测试空白字符的处理
        String whitespace = "   ";
        registerUserDTO.setUsername(whitespace);
        registerUserDTO.setNickname(whitespace);
        registerUserDTO.setPassword(whitespace);
        registerUserDTO.setPhone(whitespace);
        registerUserDTO.setEmail(whitespace);
        registerUserDTO.setAvatar(whitespace);
        registerUserDTO.setCaptcha(whitespace);
        registerUserDTO.setCaptchaKey(whitespace);

        // 验证空白字符被正确存储（不进行trim处理）
        assertEquals(whitespace, registerUserDTO.getUsername());
        assertEquals(whitespace, registerUserDTO.getNickname());
        assertEquals(whitespace, registerUserDTO.getPassword());
        assertEquals(whitespace, registerUserDTO.getPhone());
        assertEquals(whitespace, registerUserDTO.getEmail());
        assertEquals(whitespace, registerUserDTO.getAvatar());
        assertEquals(whitespace, registerUserDTO.getCaptcha());
        assertEquals(whitespace, registerUserDTO.getCaptchaKey());
    }
}

