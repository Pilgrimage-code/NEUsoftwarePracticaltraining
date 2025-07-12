package com.cemh.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginVO 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
class LoginVOTest {

    private LoginVO loginVO;

    @BeforeEach
    void setUp() {
        loginVO = new LoginVO();
    }

    @Test
    void testDefaultConstructor() {
        LoginVO vo = new LoginVO();
        assertNotNull(vo);
        assertEquals("Bearer", vo.getTokenType()); // 默认值应该是Bearer
        assertNull(vo.getAccessToken());
        assertNull(vo.getRefreshToken());
        assertNull(vo.getExpiresIn());
        assertNull(vo.getUserId());
        assertNull(vo.getUsername());
        assertNull(vo.getNickname());
        assertNull(vo.getAvatar());
        assertNull(vo.getPhone());
        assertNull(vo.getEmail());
        assertNull(vo.getGender());
        assertNull(vo.getDeptId());
        assertNull(vo.getDeptName());
        assertNull(vo.getTenantId());
        assertNull(vo.getTenantCode());
        assertNull(vo.getRoles());
        assertNull(vo.getPermissions());
    }

    @Test
    void testAccessTokenGetterAndSetter() {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        loginVO.setAccessToken(accessToken);
        assertEquals(accessToken, loginVO.getAccessToken());

        // 测试null值
        loginVO.setAccessToken(null);
        assertNull(loginVO.getAccessToken());

        // 测试空字符串
        loginVO.setAccessToken("");
        assertEquals("", loginVO.getAccessToken());
    }

    @Test
    void testRefreshTokenGetterAndSetter() {
        String refreshToken = "refresh_token_123456";
        loginVO.setRefreshToken(refreshToken);
        assertEquals(refreshToken, loginVO.getRefreshToken());

        // 测试null值
        loginVO.setRefreshToken(null);
        assertNull(loginVO.getRefreshToken());
    }

    @Test
    void testTokenTypeGetterAndSetter() {
        // 测试默认值
        assertEquals("Bearer", loginVO.getTokenType());

        // 测试设置新值
        String tokenType = "Basic";
        loginVO.setTokenType(tokenType);
        assertEquals(tokenType, loginVO.getTokenType());

        // 测试null值
        loginVO.setTokenType(null);
        assertNull(loginVO.getTokenType());
    }

    @Test
    void testExpiresInGetterAndSetter() {
        Long expiresIn = 3600L;
        loginVO.setExpiresIn(expiresIn);
        assertEquals(expiresIn, loginVO.getExpiresIn());

        // 测试null值
        loginVO.setExpiresIn(null);
        assertNull(loginVO.getExpiresIn());

        // 测试边界值
        Long maxExpiresIn = Long.MAX_VALUE;
        loginVO.setExpiresIn(maxExpiresIn);
        assertEquals(maxExpiresIn, loginVO.getExpiresIn());

        Long zeroExpiresIn = 0L;
        loginVO.setExpiresIn(zeroExpiresIn);
        assertEquals(zeroExpiresIn, loginVO.getExpiresIn());
    }

    @Test
    void testUserIdGetterAndSetter() {
        Long userId = 1001L;
        loginVO.setUserId(userId);
        assertEquals(userId, loginVO.getUserId());

        // 测试null值
        loginVO.setUserId(null);
        assertNull(loginVO.getUserId());

        // 测试边界值
        Long maxUserId = Long.MAX_VALUE;
        loginVO.setUserId(maxUserId);
        assertEquals(maxUserId, loginVO.getUserId());
    }

    @Test
    void testUsernameGetterAndSetter() {
        String username = "testuser";
        loginVO.setUsername(username);
        assertEquals(username, loginVO.getUsername());

        // 测试null值
        loginVO.setUsername(null);
        assertNull(loginVO.getUsername());

        // 测试包含特殊字符的用户名
        String specialUsername = "test@user_123";
        loginVO.setUsername(specialUsername);
        assertEquals(specialUsername, loginVO.getUsername());
    }

    @Test
    void testNicknameGetterAndSetter() {
        String nickname = "测试昵称";
        loginVO.setNickname(nickname);
        assertEquals(nickname, loginVO.getNickname());

        // 测试null值
        loginVO.setNickname(null);
        assertNull(loginVO.getNickname());

        // 测试包含特殊字符和中文的昵称
        String specialNickname = "测试@昵称_123";
        loginVO.setNickname(specialNickname);
        assertEquals(specialNickname, loginVO.getNickname());
    }

    @Test
    void testAvatarGetterAndSetter() {
        String avatar = "https://example.com/avatar.jpg";
        loginVO.setAvatar(avatar);
        assertEquals(avatar, loginVO.getAvatar());

        // 测试null值
        loginVO.setAvatar(null);
        assertNull(loginVO.getAvatar());

        // 测试相对路径
        String relativePath = "/images/avatar.png";
        loginVO.setAvatar(relativePath);
        assertEquals(relativePath, loginVO.getAvatar());
    }

    @Test
    void testPhoneGetterAndSetter() {
        String phone = "13800138000";
        loginVO.setPhone(phone);
        assertEquals(phone, loginVO.getPhone());

        // 测试null值
        loginVO.setPhone(null);
        assertNull(loginVO.getPhone());

        // 测试国际电话号码格式
        String internationalPhone = "+86-138-0013-8000";
        loginVO.setPhone(internationalPhone);
        assertEquals(internationalPhone, loginVO.getPhone());
    }

    @Test
    void testEmailGetterAndSetter() {
        String email = "test@example.com";
        loginVO.setEmail(email);
        assertEquals(email, loginVO.getEmail());

        // 测试null值
        loginVO.setEmail(null);
        assertNull(loginVO.getEmail());

        // 测试复杂邮箱格式
        String complexEmail = "test.user+tag@example.co.uk";
        loginVO.setEmail(complexEmail);
        assertEquals(complexEmail, loginVO.getEmail());
    }

    @Test
    void testGenderGetterAndSetter() {
        Integer gender = 0; // 男
        loginVO.setGender(gender);
        assertEquals(gender, loginVO.getGender());

        // 测试女
        gender = 1;
        loginVO.setGender(gender);
        assertEquals(gender, loginVO.getGender());

        // 测试未知
        gender = 2;
        loginVO.setGender(gender);
        assertEquals(gender, loginVO.getGender());

        // 测试null值
        loginVO.setGender(null);
        assertNull(loginVO.getGender());
    }

    @Test
    void testDeptIdGetterAndSetter() {
        Long deptId = 2001L;
        loginVO.setDeptId(deptId);
        assertEquals(deptId, loginVO.getDeptId());

        // 测试null值
        loginVO.setDeptId(null);
        assertNull(loginVO.getDeptId());
    }

    @Test
    void testDeptNameGetterAndSetter() {
        String deptName = "技术部";
        loginVO.setDeptName(deptName);
        assertEquals(deptName, loginVO.getDeptName());

        // 测试null值
        loginVO.setDeptName(null);
        assertNull(loginVO.getDeptName());

        // 测试包含特殊字符的部门名
        String specialDeptName = "技术部-研发组";
        loginVO.setDeptName(specialDeptName);
        assertEquals(specialDeptName, loginVO.getDeptName());
    }

    @Test
    void testTenantIdGetterAndSetter() {
        Long tenantId = 1001L;
        loginVO.setTenantId(tenantId);
        assertEquals(tenantId, loginVO.getTenantId());

        // 测试null值
        loginVO.setTenantId(null);
        assertNull(loginVO.getTenantId());
    }

    @Test
    void testTenantCodeGetterAndSetter() {
        String tenantCode = "tenant001";
        loginVO.setTenantCode(tenantCode);
        assertEquals(tenantCode, loginVO.getTenantCode());

        // 测试null值
        loginVO.setTenantCode(null);
        assertNull(loginVO.getTenantCode());
    }

    @Test
    void testRolesGetterAndSetter() {
        List<String> roles = Arrays.asList("admin", "user", "manager");
        loginVO.setRoles(roles);
        assertEquals(roles, loginVO.getRoles());
        assertEquals(3, loginVO.getRoles().size());
        assertTrue(loginVO.getRoles().contains("admin"));
        assertTrue(loginVO.getRoles().contains("user"));
        assertTrue(loginVO.getRoles().contains("manager"));

        // 测试null值
        loginVO.setRoles(null);
        assertNull(loginVO.getRoles());

        // 测试空列表
        List<String> emptyRoles = Arrays.asList();
        loginVO.setRoles(emptyRoles);
        assertEquals(emptyRoles, loginVO.getRoles());
        assertEquals(0, loginVO.getRoles().size());
    }

    @Test
    void testPermissionsGetterAndSetter() {
        List<String> permissions = Arrays.asList("read", "write", "delete", "update");
        loginVO.setPermissions(permissions);
        assertEquals(permissions, loginVO.getPermissions());
        assertEquals(4, loginVO.getPermissions().size());
        assertTrue(loginVO.getPermissions().contains("read"));
        assertTrue(loginVO.getPermissions().contains("write"));
        assertTrue(loginVO.getPermissions().contains("delete"));
        assertTrue(loginVO.getPermissions().contains("update"));

        // 测试null值
        loginVO.setPermissions(null);
        assertNull(loginVO.getPermissions());

        // 测试空列表
        List<String> emptyPermissions = Arrays.asList();
        loginVO.setPermissions(emptyPermissions);
        assertEquals(emptyPermissions, loginVO.getPermissions());
        assertEquals(0, loginVO.getPermissions().size());
    }

    @Test
    void testAllFieldsSetAndGet() {
        // 设置所有字段
        String accessToken = "access_token_123";
        String refreshToken = "refresh_token_123";
        String tokenType = "Bearer";
        Long expiresIn = 3600L;
        Long userId = 1001L;
        String username = "testuser";
        String nickname = "测试用户";
        String avatar = "https://example.com/avatar.jpg";
        String phone = "13800138000";
        String email = "test@example.com";
        Integer gender = 0;
        Long deptId = 2001L;
        String deptName = "技术部";
        Long tenantId = 1001L;
        String tenantCode = "tenant001";
        List<String> roles = Arrays.asList("admin", "user");
        List<String> permissions = Arrays.asList("read", "write");

        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setTokenType(tokenType);
        loginVO.setExpiresIn(expiresIn);
        loginVO.setUserId(userId);
        loginVO.setUsername(username);
        loginVO.setNickname(nickname);
        loginVO.setAvatar(avatar);
        loginVO.setPhone(phone);
        loginVO.setEmail(email);
        loginVO.setGender(gender);
        loginVO.setDeptId(deptId);
        loginVO.setDeptName(deptName);
        loginVO.setTenantId(tenantId);
        loginVO.setTenantCode(tenantCode);
        loginVO.setRoles(roles);
        loginVO.setPermissions(permissions);

        // 验证所有字段
        assertEquals(accessToken, loginVO.getAccessToken());
        assertEquals(refreshToken, loginVO.getRefreshToken());
        assertEquals(tokenType, loginVO.getTokenType());
        assertEquals(expiresIn, loginVO.getExpiresIn());
        assertEquals(userId, loginVO.getUserId());
        assertEquals(username, loginVO.getUsername());
        assertEquals(nickname, loginVO.getNickname());
        assertEquals(avatar, loginVO.getAvatar());
        assertEquals(phone, loginVO.getPhone());
        assertEquals(email, loginVO.getEmail());
        assertEquals(gender, loginVO.getGender());
        assertEquals(deptId, loginVO.getDeptId());
        assertEquals(deptName, loginVO.getDeptName());
        assertEquals(tenantId, loginVO.getTenantId());
        assertEquals(tenantCode, loginVO.getTenantCode());
        assertEquals(roles, loginVO.getRoles());
        assertEquals(permissions, loginVO.getPermissions());
    }

    @Test
    void testToString() {
        // 设置一些字段
        loginVO.setAccessToken("access_token_123");
        loginVO.setRefreshToken("refresh_token_123");
        loginVO.setTokenType("Bearer");
        loginVO.setExpiresIn(3600L);
        loginVO.setUserId(1001L);
        loginVO.setUsername("testuser");
        loginVO.setNickname("测试用户");
        loginVO.setRoles(Arrays.asList("admin", "user"));
        loginVO.setPermissions(Arrays.asList("read", "write"));

        String toString = loginVO.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("access_token_123"));
        assertTrue(toString.contains("refresh_token_123"));
        assertTrue(toString.contains("Bearer"));
        assertTrue(toString.contains("3600"));
        assertTrue(toString.contains("1001"));
        assertTrue(toString.contains("testuser"));
        assertTrue(toString.contains("测试用户"));
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("user"));
        assertTrue(toString.contains("read"));
        assertTrue(toString.contains("write"));
    }

    @Test
    void testFieldsIndependence() {
        // 测试字段之间的独立性
        loginVO.setUsername("user1");
        loginVO.setNickname("nickname1");
        
        // 修改一个字段不应该影响其他字段
        loginVO.setUsername("user2");
        assertEquals("user2", loginVO.getUsername());
        assertEquals("nickname1", loginVO.getNickname());
    }

    @Test
    void testListFieldsModification() {
        // 测试列表字段的修改
        List<String> originalRoles = Arrays.asList("admin", "user");
        loginVO.setRoles(originalRoles);
        
        // 获取角色列表并验证
        List<String> retrievedRoles = loginVO.getRoles();
        assertEquals(originalRoles, retrievedRoles);
        
        // 设置新的角色列表
        List<String> newRoles = Arrays.asList("manager", "operator");
        loginVO.setRoles(newRoles);
        assertEquals(newRoles, loginVO.getRoles());
        assertNotEquals(originalRoles, loginVO.getRoles());
    }

    @Test
    void testGenderValidValues() {
        // 测试性别的有效值
        Integer[] validGenders = {0, 1, 2}; // 男、女、未知
        
        for (Integer gender : validGenders) {
            loginVO.setGender(gender);
            assertEquals(gender, loginVO.getGender());
        }
    }

    @Test
    void testTokenTypeDefaultValue() {
        // 测试tokenType的默认值
        LoginVO newVO = new LoginVO();
        assertEquals("Bearer", newVO.getTokenType());
        
        // 测试修改后再创建新对象
        newVO.setTokenType("Basic");
        LoginVO anotherVO = new LoginVO();
        assertEquals("Bearer", anotherVO.getTokenType()); // 新对象应该还是默认值
    }
}

