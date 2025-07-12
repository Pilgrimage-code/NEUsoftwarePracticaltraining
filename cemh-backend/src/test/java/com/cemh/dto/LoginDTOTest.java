package com.cemh.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginDTO 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
class LoginDTOTest {

    private Validator validator;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        loginDTO = new LoginDTO();
    }

    @Test
    void testDefaultConstructor() {
        LoginDTO dto = new LoginDTO();
        assertNotNull(dto);
        assertFalse(dto.getRememberMe()); // 默认值应该是false
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
        assertNull(dto.getTenantCode());
        assertNull(dto.getCaptcha());
        assertNull(dto.getCaptchaKey());
        assertNull(dto.getClientIp());
    }

    @Test
    void testValidLoginDTO() {
        // 设置有效的登录数据
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");
        loginDTO.setTenantCode("tenant001");
        loginDTO.setCaptcha("1234");
        loginDTO.setCaptchaKey("captcha-key");
        loginDTO.setRememberMe(true);
        loginDTO.setClientIp("192.168.1.100");

        // 验证数据有效性
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.isEmpty(), "有效的LoginDTO不应该有验证错误");
    }

    @Test
    void testUsernameValidation() {
        loginDTO.setPassword("password123");

        // 测试用户名为空
        loginDTO.setUsername(null);
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名不能为空")));

        // 测试用户名为空字符串
        loginDTO.setUsername("");
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名不能为空")));

        // 测试用户名为空白字符串
        loginDTO.setUsername("   ");
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名不能为空")));

        // 测试用户名长度过短
        loginDTO.setUsername("ab");
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名长度必须在3-50个字符之间")));

        // 测试用户名长度过长
        loginDTO.setUsername(repeat("a", 51));
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("用户名长度必须在3-50个字符之间")));

        // 测试有效用户名
        loginDTO.setUsername("validuser");
        violations = validator.validate(loginDTO);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void testPasswordValidation() {
        loginDTO.setUsername("testuser");

        // 测试密码为空
        loginDTO.setPassword(null);
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("密码不能为空")));

        // 测试密码为空字符串
        loginDTO.setPassword("");
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("密码不能为空")));

        // 测试密码长度过短
        loginDTO.setPassword("12345");
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("密码长度必须在6-20个字符之间")));

        // 测试密码长度过长
        loginDTO.setPassword(repeat("a", 21));
        violations = validator.validate(loginDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("密码长度必须在6-20个字符之间")));

        // 测试有效密码
        loginDTO.setPassword("validpassword");
        violations = validator.validate(loginDTO);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testGettersAndSetters() {
        // 测试username
        String username = "testuser";
        loginDTO.setUsername(username);
        assertEquals(username, loginDTO.getUsername());

        // 测试password
        String password = "testpassword";
        loginDTO.setPassword(password);
        assertEquals(password, loginDTO.getPassword());

        // 测试tenantCode
        String tenantCode = "tenant001";
        loginDTO.setTenantCode(tenantCode);
        assertEquals(tenantCode, loginDTO.getTenantCode());

        // 测试captcha
        String captcha = "1234";
        loginDTO.setCaptcha(captcha);
        assertEquals(captcha, loginDTO.getCaptcha());

        // 测试captchaKey
        String captchaKey = "captcha-key";
        loginDTO.setCaptchaKey(captchaKey);
        assertEquals(captchaKey, loginDTO.getCaptchaKey());

        // 测试rememberMe
        Boolean rememberMe = true;
        loginDTO.setRememberMe(rememberMe);
        assertEquals(rememberMe, loginDTO.getRememberMe());

        // 测试clientIp
        String clientIp = "192.168.1.100";
        loginDTO.setClientIp(clientIp);
        assertEquals(clientIp, loginDTO.getClientIp());
    }

    @Test
    void testRememberMeDefaultValue() {
        LoginDTO dto = new LoginDTO();
        assertFalse(dto.getRememberMe());

        // 测试设置为null后的行为
        dto.setRememberMe(null);
        assertNull(dto.getRememberMe());

        // 测试设置为true
        dto.setRememberMe(true);
        assertTrue(dto.getRememberMe());

        // 测试设置为false
        dto.setRememberMe(false);
        assertFalse(dto.getRememberMe());
    }

    @Test
    void testToString() {
        loginDTO.setUsername("testuser");
        loginDTO.setTenantCode("tenant001");
        loginDTO.setCaptcha("1234");
        loginDTO.setCaptchaKey("captcha-key");
        loginDTO.setRememberMe(true);
        loginDTO.setClientIp("192.168.1.100");

        String toString = loginDTO.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("tenant001"));
        assertTrue(toString.contains("1234"));
        assertTrue(toString.contains("captcha-key"));
        assertTrue(toString.contains("true"));
        assertTrue(toString.contains("192.168.1.100"));
        
        // 确保密码不在toString中显示（安全考虑）
        loginDTO.setPassword("secretpassword");
        toString = loginDTO.toString();
        assertFalse(toString.contains("secretpassword"));
    }

    @Test
    void testBoundaryValues() {
        // 测试用户名边界值
        loginDTO.setPassword("password123");
        
        // 最小长度用户名
        loginDTO.setUsername("abc");
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("username")));

        // 最大长度用户名
        loginDTO.setUsername(repeat("a", 50));
        violations = validator.validate(loginDTO);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("username")));

        // 测试密码边界值
        loginDTO.setUsername("testuser");
        
        // 最小长度密码
        loginDTO.setPassword("123456");
        violations = validator.validate(loginDTO);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("password")));

        // 最大长度密码
        loginDTO.setPassword(repeat("a", 20));
        violations = validator.validate(loginDTO);
        assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testOptionalFields() {
        // 设置必填字段
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        // 可选字段为null应该不影响验证
        loginDTO.setTenantCode(null);
        loginDTO.setCaptcha(null);
        loginDTO.setCaptchaKey(null);
        loginDTO.setRememberMe(null);
        loginDTO.setClientIp(null);

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.isEmpty(), "可选字段为null不应该导致验证失败");
    }

    @Test
    void testSpecialCharactersInFields() {
        loginDTO.setUsername("test@user");
        loginDTO.setPassword("pass@word#123");
        loginDTO.setTenantCode("tenant-001");
        loginDTO.setCaptcha("AB12");
        loginDTO.setCaptchaKey("key-123-abc");
        loginDTO.setClientIp("192.168.1.100");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(loginDTO);
        assertTrue(violations.isEmpty(), "包含特殊字符的有效数据应该通过验证");
    }

    // 兼容Java8的repeat方法
    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }
}

