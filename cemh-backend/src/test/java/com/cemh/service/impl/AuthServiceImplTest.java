package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.entity.SysUser;
import com.cemh.entity.SysTenant;
import com.cemh.mapper.SysUserMapper;
import com.cemh.mapper.SysTenantMapper;
import com.cemh.utils.JwtUtils;
import com.cemh.utils.PasswordUtils;
import com.cemh.vo.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.startsWith;

/**
 * AuthServiceImpl 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private SysTenantMapper sysTenantMapper;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginDTO loginDTO;
    private SysUser sysUser;
    private SysTenant sysTenant;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");
        loginDTO.setTenantCode("tenant001");
        loginDTO.setClientIp("192.168.1.100");

        sysTenant = new SysTenant();
        sysTenant.setId(1L);
        sysTenant.setTenantCode("tenant001");
        sysTenant.setTenantName("测试租户");
        sysTenant.setStatus(1);

        sysUser = new SysUser();
        sysUser.setId(1001L);
        sysUser.setUsername("testuser");
        sysUser.setPassword(PasswordUtils.encode("password123"));
        sysUser.setNickname("测试用户");
        sysUser.setEmail("test@example.com");
        sysUser.setPhone("13800138000");
        sysUser.setTenantId(1L);
        sysUser.setStatus(1);

        // Mock Redis操作 - 只在需要时配置
    }

    @Test
    void testLogin_Success() {
        // Mock依赖方法
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysTenant);
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysUser);
        when(sysUserMapper.updateById(any(SysUser.class))).thenReturn(1);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class);
             MockedStatic<PasswordUtils> passwordUtilsMock = mockStatic(PasswordUtils.class)) {
            
            passwordUtilsMock.when(() -> PasswordUtils.matches(anyString(), anyString())).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.generateToken(anyLong(), anyString(), anyLong())).thenReturn("access-token");
            jwtUtilsMock.when(() -> JwtUtils.generateRefreshToken(anyLong())).thenReturn("refresh-token");

            // 执行测试
            Result<LoginVO> result = authService.login(loginDTO);

            // 验证结果
            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertNotNull(result.getData());
            assertEquals("testuser", result.getData().getUsername());
            assertEquals("access-token", result.getData().getAccessToken());
            assertEquals("refresh-token", result.getData().getRefreshToken());

            // 验证方法调用
            verify(sysTenantMapper, times(1)).selectOne(any(QueryWrapper.class));
            verify(sysUserMapper, times(1)).selectOne(any(QueryWrapper.class));
            verify(sysUserMapper, times(1)).updateById(any(SysUser.class));
        }
    }

    @Test
    void testLogin_EmptyUsername() {
        loginDTO.setUsername("");

        Result<LoginVO> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("用户名不能为空", result.getMessage());
    }

    @Test
    void testLogin_EmptyPassword() {
        loginDTO.setPassword("");

        Result<LoginVO> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("密码不能为空", result.getMessage());
    }

    @Test
    void testLogin_EmptyTenantCode() {
        loginDTO.setTenantCode("");

        Result<LoginVO> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("租户代码不能为空", result.getMessage());
    }

    @Test
    void testLogin_TenantNotFound() {
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        Result<LoginVO> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("租户代码不存在或已禁用", result.getMessage());
    }

    @Test
    void testLogin_UserNotFound() {
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysTenant);
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        Result<LoginVO> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("用户名或租户代码错误", result.getMessage());
    }

    @Test
    void testLogin_WrongPassword() {
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysTenant);
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysUser);

        try (MockedStatic<PasswordUtils> passwordUtilsMock = mockStatic(PasswordUtils.class)) {
            passwordUtilsMock.when(() -> PasswordUtils.matches(anyString(), anyString())).thenReturn(false);

            Result<LoginVO> result = authService.login(loginDTO);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("密码错误", result.getMessage());
        }
    }

    @Test
    void testLogin_UserDisabled() {
        sysUser.setStatus(0); // 禁用用户
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysTenant);
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysUser);

        try (MockedStatic<PasswordUtils> passwordUtilsMock = mockStatic(PasswordUtils.class)) {
            passwordUtilsMock.when(() -> PasswordUtils.matches(anyString(), anyString())).thenReturn(true);

            Result<LoginVO> result = authService.login(loginDTO);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("用户已被禁用", result.getMessage());
        }
    }

    @Test
    void testLogout_Success() {
        String token = "test-token";

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(token)).thenReturn(1001L);

            Result<Object> result = authService.logout(token);

            assertNotNull(result);
            assertEquals(200, result.getCode());
        }
    }

    @Test
    void testLogout_EmptyToken() {
        Result<Object> result = authService.logout("");

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("令牌不能为空", result.getMessage());
    }

    @Test
    void testGetUserInfo_Success() {
        String token = "test-token";

        when(sysUserMapper.selectById(1001L)).thenReturn(sysUser);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(token)).thenReturn(1001L);

            Result<LoginVO> result = authService.getUserInfo(token);

            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertNotNull(result.getData());
            assertEquals("testuser", result.getData().getUsername());
            assertEquals(1001L, result.getData().getUserId());
        }
    }

    @Test
    void testGetUserInfo_EmptyToken() {
        Result<LoginVO> result = authService.getUserInfo("");

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("令牌不能为空", result.getMessage());
    }

    @Test
    void testGetUserInfo_InvalidToken() {
        String token = "invalid-token";

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(false);

            Result<LoginVO> result = authService.getUserInfo(token);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("令牌无效或已过期", result.getMessage());
        }
    }

    @Test
    void testGetUserInfo_UserNotFound() {
        String token = "test-token";

        when(sysUserMapper.selectById(1001L)).thenReturn(null);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(token)).thenReturn(1001L);

            Result<LoginVO> result = authService.getUserInfo(token);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("用户不存在", result.getMessage());
        }
    }

    @Test
    void testGetUserInfo_UserDisabled() {
        String token = "test-token";
        sysUser.setStatus(0); // 禁用用户

        when(sysUserMapper.selectById(1001L)).thenReturn(sysUser);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(token)).thenReturn(1001L);

            Result<LoginVO> result = authService.getUserInfo(token);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("用户已被禁用", result.getMessage());
        }
    }

    @Test
    void testRefreshToken_Success() {
        String refreshToken = "refresh-token";

        when(sysUserMapper.selectById(1001L)).thenReturn(sysUser);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(refreshToken)).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(refreshToken)).thenReturn(1001L);
            jwtUtilsMock.when(() -> JwtUtils.generateToken(anyLong(), anyString(), anyLong())).thenReturn("new-access-token");
            jwtUtilsMock.when(() -> JwtUtils.generateRefreshToken(anyLong())).thenReturn("new-refresh-token");

            Result<LoginVO> result = authService.refreshToken(refreshToken);

            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertNotNull(result.getData());
            assertEquals("new-access-token", result.getData().getAccessToken());
            assertEquals("new-refresh-token", result.getData().getRefreshToken());
        }
    }

    @Test
    void testRefreshToken_EmptyToken() {
        Result<LoginVO> result = authService.refreshToken("");

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("刷新令牌不能为空", result.getMessage());
    }

    @Test
    void testRefreshToken_InvalidToken() {
        String refreshToken = "invalid-refresh-token";

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(refreshToken)).thenReturn(false);

            Result<LoginVO> result = authService.refreshToken(refreshToken);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("刷新令牌无效或已过期", result.getMessage());
        }
    }

    @Test
    void testRefreshToken_UserNotFound() {
        String refreshToken = "refresh-token";

        when(sysUserMapper.selectById(1001L)).thenReturn(null);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(refreshToken)).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(refreshToken)).thenReturn(1001L);

            Result<LoginVO> result = authService.refreshToken(refreshToken);

            assertNotNull(result);
            assertEquals(500, result.getCode());
            assertEquals("用户不存在", result.getMessage());
        }
    }

    @Test
    void testValidateToken_Success() {
        String token = "valid-token";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("token:" + token)).thenReturn(1001L);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(true);

            Result<Boolean> result = authService.validateToken(token);

            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertTrue(result.getData());
        }
    }

    @Test
    void testValidateToken_EmptyToken() {
        Result<Boolean> result = authService.validateToken("");

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertFalse(result.getData());
    }

    @Test
    void testValidateToken_InvalidToken() {
        String token = "invalid-token";

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(false);

            Result<Boolean> result = authService.validateToken(token);

            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertFalse(result.getData());
        }
    }

    @Test
    void testValidateToken_NotInRedis() {
        String token = "valid-jwt-token";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("token:" + token)).thenReturn(null);

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(token)).thenReturn(true);

            Result<Boolean> result = authService.validateToken(token);

            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertFalse(result.getData());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void testLogin_WithRedisException() {
        // Mock Redis操作抛出异常
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysTenant);
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(sysUser);
        when(sysUserMapper.updateById(any(SysUser.class))).thenReturn(1);
        // when(valueOperations.set(
        //         anyString(),
        //         anyString(),
        //         anyLong(),
        //         any(TimeUnit.class)
        // )).thenThrow(new RuntimeException("Redis连接失败"));

        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class);
             MockedStatic<PasswordUtils> passwordUtilsMock = mockStatic(PasswordUtils.class)) {
            
            passwordUtilsMock.when(() -> PasswordUtils.matches(anyString(), anyString())).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.generateToken(anyLong(), anyString(), anyLong())).thenReturn("access-token");
            jwtUtilsMock.when(() -> JwtUtils.generateRefreshToken(anyLong())).thenReturn("refresh-token");

            // 执行测试 - 即使Redis失败，登录也应该成功
            Result<LoginVO> result = authService.login(loginDTO);

            assertNotNull(result);
            assertEquals(200, result.getCode());
            assertNotNull(result.getData());
        }
    }

    @Test
    void testLogin_Exception() {
        // Mock数据库操作抛出异常
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenThrow(new RuntimeException("数据库连接失败"));

        Result<LoginVO> result = authService.login(loginDTO);

        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("登录失败"));
    }

    @Test
    void testLogin_NullLoginDTO() {
        Result<LoginVO> result = authService.login(null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testLogin_MapperException() {
        when(sysTenantMapper.selectOne(any())).thenThrow(new RuntimeException("db error"));
        Result<LoginVO> result = authService.login(loginDTO);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetUserInfo_TokenNull() {
        Result<LoginVO> result = authService.getUserInfo(null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetUserInfo_TokenInvalid() {
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(anyString())).thenThrow(new RuntimeException("token error"));
            Result<LoginVO> result = authService.getUserInfo("bad-token");
            assertEquals(500, result.getCode());
        }
    }

    @Test
    void testLogout_TokenNull() {
        Result<Object> result = authService.logout(null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testLogout_RedisException() {
        lenient().doThrow(new RuntimeException("redis error")).when(redisTemplate).delete(anyString());
        Result<Object> result = authService.logout("token");
        assertEquals(200, result.getCode());
    }

    @Test
    void testRefreshToken_TokenNull() {
        Result<LoginVO> result = authService.refreshToken(null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testRefreshToken_TokenInvalid() {
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(anyString())).thenThrow(new RuntimeException("token error"));
            Result<LoginVO> result = authService.refreshToken("bad-token");
            assertEquals(500, result.getCode());
        }
    }

    @Test
    void testValidateToken_TokenNull() {
        Result<Boolean> result = authService.validateToken(null);
        assertEquals(200, result.getCode());
    }

    @Test
    void testValidateToken_RedisException() {
        lenient().when(redisTemplate.hasKey(anyString())).thenThrow(new RuntimeException("redis error"));
        Result<Boolean> result = authService.validateToken("token");
        assertEquals(200, result.getCode());
    }

    @Test
    void testLogin_UsernameNull() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(null);
        dto.setPassword("123");
        Result<LoginVO> result = authService.login(dto);
        assertEquals(500, result.getCode());
    }

    @Test
    void testLogin_PasswordNull() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("user");
        dto.setPassword(null);
        Result<LoginVO> result = authService.login(dto);
        assertEquals(500, result.getCode());
    }

    @Test
    void testLogin_RedisSetTokenKeyException() {
        // mock user/tenant/密码全部正常
        when(sysTenantMapper.selectOne(any())).thenReturn(sysTenant);
        when(sysUserMapper.selectOne(any())).thenReturn(sysUser);
        when(sysUserMapper.updateById(any())).thenReturn(1);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(startsWith("user:"), any(), anyLong(), any());
        doThrow(new RuntimeException("redis error")).when(valueOperations).set(startsWith("token:"), any(), anyLong(), any());
        try (MockedStatic<PasswordUtils> passwordUtilsMock = mockStatic(PasswordUtils.class);
             MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            passwordUtilsMock.when(() -> PasswordUtils.matches(anyString(), anyString())).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.generateToken(anyLong(), anyString(), anyLong())).thenReturn("access-token");
            jwtUtilsMock.when(() -> JwtUtils.generateRefreshToken(anyLong())).thenReturn("refresh-token");
            Result<LoginVO> result = authService.login(loginDTO);
            assertEquals(200, result.getCode());
        }
    }

    @Test
    void testLogin_AnyException() {
        when(sysTenantMapper.selectOne(any())).thenThrow(new RuntimeException("db error"));
        Result<LoginVO> result = authService.login(loginDTO);
        assertEquals(500, result.getCode());
    }

    @Test
    void testLogout_JwtUtilsException() {
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(anyString())).thenThrow(new RuntimeException("token error"));
            Result<Object> result = authService.logout("token");
            assertEquals(200, result.getCode());
        }
    }

    @Test
    void testGetUserInfo_JwtUtilsException() {
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(anyString())).thenThrow(new RuntimeException("token error"));
            Result<LoginVO> result = authService.getUserInfo("token");
            assertEquals(500, result.getCode());
        }
    }

    @Test
    void testRefreshToken_UserDisabled() {
        when(sysUserMapper.selectById(anyLong())).thenReturn(sysUser);
        sysUser.setStatus(0);
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(anyString())).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(anyString())).thenReturn(1L);
            Result<LoginVO> result = authService.refreshToken("refresh-token");
            assertEquals(500, result.getCode());
        }
    }

    @Test
    void testRefreshToken_AnyException() {
        when(sysUserMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(anyString())).thenReturn(true);
            jwtUtilsMock.when(() -> JwtUtils.getUserIdFromToken(anyString())).thenReturn(1L);
            Result<LoginVO> result = authService.refreshToken("refresh-token");
            assertEquals(500, result.getCode());
        }
    }

    @Test
    void testValidateToken_AnyException() {
        try (MockedStatic<JwtUtils> jwtUtilsMock = mockStatic(JwtUtils.class)) {
            jwtUtilsMock.when(() -> JwtUtils.validateToken(anyString())).thenThrow(new RuntimeException("token error"));
            Result<Boolean> result = authService.validateToken("token");
            assertEquals(200, result.getCode());
            assertFalse(result.getData());
        }
    }
}

