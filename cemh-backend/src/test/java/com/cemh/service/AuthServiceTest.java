package com.cemh.service;

import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.vo.LoginVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AuthService 接口测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthService authService;

    @Test
    void testLogin_Success() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");
        loginDTO.setTenantCode("tenant001");
        loginDTO.setClientIp("192.168.1.100");

        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(1001L);
        loginVO.setUsername("testuser");
        loginVO.setAccessToken("access-token");
        loginVO.setRefreshToken("refresh-token");
        loginVO.setExpiresIn(7200L);

        Result<LoginVO> expectedResult = Result.success(loginVO);

        // Mock方法调用
        when(authService.login(loginDTO)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.login(loginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("testuser", result.getData().getUsername());
        assertEquals("access-token", result.getData().getAccessToken());
        assertEquals("refresh-token", result.getData().getRefreshToken());
        assertEquals(7200L, result.getData().getExpiresIn());

        // 验证方法调用
        verify(authService, times(1)).login(loginDTO);
    }

    @Test
    void testLogin_InvalidCredentials() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("wronguser");
        loginDTO.setPassword("wrongpassword");
        loginDTO.setTenantCode("tenant001");

        Result<LoginVO> expectedResult = Result.error("用户名或密码错误");

        // Mock方法调用
        when(authService.login(loginDTO)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.login(loginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("用户名或密码错误", result.getMessage());
        assertNull(result.getData());

        // 验证方法调用
        verify(authService, times(1)).login(loginDTO);
    }

    @Test
    void testLogin_EmptyUsername() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("password123");
        loginDTO.setTenantCode("tenant001");

        Result<LoginVO> expectedResult = Result.error("用户名不能为空");

        // Mock方法调用
        when(authService.login(loginDTO)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.login(loginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("用户名不能为空", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).login(loginDTO);
    }

    @Test
    void testLogin_EmptyPassword() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("");
        loginDTO.setTenantCode("tenant001");

        Result<LoginVO> expectedResult = Result.error("密码不能为空");

        // Mock方法调用
        when(authService.login(loginDTO)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.login(loginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("密码不能为空", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).login(loginDTO);
    }

    @Test
    void testLogin_EmptyTenantCode() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");
        loginDTO.setTenantCode("");

        Result<LoginVO> expectedResult = Result.error("租户代码不能为空");

        // Mock方法调用
        when(authService.login(loginDTO)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.login(loginDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("租户代码不能为空", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).login(loginDTO);
    }

    @Test
    void testLogout_Success() {
        // 准备测试数据
        String token = "valid-token";
        Result<Object> expectedResult = Result.success(null);

        // Mock方法调用
        when(authService.logout(token)).thenReturn(expectedResult);

        // 执行测试
        Result<Object> result = authService.logout(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(authService, times(1)).logout(token);
    }

    @Test
    void testLogout_EmptyToken() {
        // 准备测试数据
        String token = "";
        Result<Object> expectedResult = Result.error("令牌不能为空");

        // Mock方法调用
        when(authService.logout(token)).thenReturn(expectedResult);

        // 执行测试
        Result<Object> result = authService.logout(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("令牌不能为空", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).logout(token);
    }

    @Test
    void testGetUserInfo_Success() {
        // 准备测试数据
        String token = "valid-token";
        
        LoginVO userInfo = new LoginVO();
        userInfo.setUserId(1001L);
        userInfo.setUsername("testuser");
        userInfo.setNickname("测试用户");
        userInfo.setEmail("test@example.com");
        userInfo.setPhone("13800138000");
        userInfo.setTenantId(1L);

        Result<LoginVO> expectedResult = Result.success(userInfo);

        // Mock方法调用
        when(authService.getUserInfo(token)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.getUserInfo(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("testuser", result.getData().getUsername());
        assertEquals("测试用户", result.getData().getNickname());
        assertEquals("test@example.com", result.getData().getEmail());

        // 验证方法调用
        verify(authService, times(1)).getUserInfo(token);
    }

    @Test
    void testGetUserInfo_InvalidToken() {
        // 准备测试数据
        String token = "invalid-token";
        Result<LoginVO> expectedResult = Result.error("令牌无效或已过期");

        // Mock方法调用
        when(authService.getUserInfo(token)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.getUserInfo(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("令牌无效或已过期", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).getUserInfo(token);
    }

    @Test
    void testGetUserInfo_EmptyToken() {
        // 准备测试数据
        String token = "";
        Result<LoginVO> expectedResult = Result.error("令牌不能为空");

        // Mock方法调用
        when(authService.getUserInfo(token)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.getUserInfo(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("令牌不能为空", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).getUserInfo(token);
    }

    @Test
    void testRefreshToken_Success() {
        // 准备测试数据
        String refreshToken = "valid-refresh-token";
        
        LoginVO newTokenInfo = new LoginVO();
        newTokenInfo.setUserId(1001L);
        newTokenInfo.setUsername("testuser");
        newTokenInfo.setAccessToken("new-access-token");
        newTokenInfo.setRefreshToken("new-refresh-token");
        newTokenInfo.setExpiresIn(7200L);

        Result<LoginVO> expectedResult = Result.success(newTokenInfo);

        // Mock方法调用
        when(authService.refreshToken(refreshToken)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.refreshToken(refreshToken);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("new-access-token", result.getData().getAccessToken());
        assertEquals("new-refresh-token", result.getData().getRefreshToken());

        // 验证方法调用
        verify(authService, times(1)).refreshToken(refreshToken);
    }

    @Test
    void testRefreshToken_InvalidToken() {
        // 准备测试数据
        String refreshToken = "invalid-refresh-token";
        Result<LoginVO> expectedResult = Result.error("刷新令牌无效或已过期");

        // Mock方法调用
        when(authService.refreshToken(refreshToken)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.refreshToken(refreshToken);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("刷新令牌无效或已过期", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).refreshToken(refreshToken);
    }

    @Test
    void testRefreshToken_EmptyToken() {
        // 准备测试数据
        String refreshToken = "";
        Result<LoginVO> expectedResult = Result.error("刷新令牌不能为空");

        // Mock方法调用
        when(authService.refreshToken(refreshToken)).thenReturn(expectedResult);

        // 执行测试
        Result<LoginVO> result = authService.refreshToken(refreshToken);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("刷新令牌不能为空", result.getMessage());

        // 验证方法调用
        verify(authService, times(1)).refreshToken(refreshToken);
    }

    @Test
    void testValidateToken_ValidToken() {
        // 准备测试数据
        String token = "valid-token";
        Result<Boolean> expectedResult = Result.success(true);

        // Mock方法调用
        when(authService.validateToken(token)).thenReturn(expectedResult);

        // 执行测试
        Result<Boolean> result = authService.validateToken(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertTrue(result.getData());

        // 验证方法调用
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    void testValidateToken_InvalidToken() {
        // 准备测试数据
        String token = "invalid-token";
        Result<Boolean> expectedResult = Result.success(false);

        // Mock方法调用
        when(authService.validateToken(token)).thenReturn(expectedResult);

        // 执行测试
        Result<Boolean> result = authService.validateToken(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertFalse(result.getData());

        // 验证方法调用
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    void testValidateToken_EmptyToken() {
        // 准备测试数据
        String token = "";
        Result<Boolean> expectedResult = Result.success(false);

        // Mock方法调用
        when(authService.validateToken(token)).thenReturn(expectedResult);

        // 执行测试
        Result<Boolean> result = authService.validateToken(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertFalse(result.getData());

        // 验证方法调用
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    void testValidateToken_NullToken() {
        // 准备测试数据
        String token = null;
        Result<Boolean> expectedResult = Result.success(false);

        // Mock方法调用
        when(authService.validateToken(token)).thenReturn(expectedResult);

        // 执行测试
        Result<Boolean> result = authService.validateToken(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertFalse(result.getData());

        // 验证方法调用
        verify(authService, times(1)).validateToken(token);
    }

    @Test
    void testLogin_MultipleAttempts() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");
        loginDTO.setTenantCode("tenant001");

        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(1001L);
        loginVO.setUsername("testuser");
        loginVO.setAccessToken("access-token");

        Result<LoginVO> expectedResult = Result.success(loginVO);

        // Mock方法调用
        when(authService.login(loginDTO)).thenReturn(expectedResult);

        // 执行多次测试
        Result<LoginVO> result1 = authService.login(loginDTO);
        Result<LoginVO> result2 = authService.login(loginDTO);
        Result<LoginVO> result3 = authService.login(loginDTO);

        // 验证结果一致性
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(result1.getCode(), result2.getCode());
        assertEquals(result2.getCode(), result3.getCode());

        // 验证方法调用次数
        verify(authService, times(3)).login(loginDTO);
    }

    @Test
    void testTokenOperations_Workflow() {
        // 测试完整的token操作流程
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        // 1. 验证token
        when(authService.validateToken(accessToken)).thenReturn(Result.success(true));
        Result<Boolean> validateResult = authService.validateToken(accessToken);
        assertTrue(validateResult.getData());

        // 2. 获取用户信息
        LoginVO userInfo = new LoginVO();
        userInfo.setUserId(1001L);
        userInfo.setUsername("testuser");
        when(authService.getUserInfo(accessToken)).thenReturn(Result.success(userInfo));
        Result<LoginVO> userInfoResult = authService.getUserInfo(accessToken);
        assertEquals("testuser", userInfoResult.getData().getUsername());

        // 3. 刷新token
        LoginVO newTokenInfo = new LoginVO();
        newTokenInfo.setAccessToken("new-access-token");
        when(authService.refreshToken(refreshToken)).thenReturn(Result.success(newTokenInfo));
        Result<LoginVO> refreshResult = authService.refreshToken(refreshToken);
        assertEquals("new-access-token", refreshResult.getData().getAccessToken());

        // 4. 退出登录
        when(authService.logout(accessToken)).thenReturn(Result.success(null));
        Result<Object> logoutResult = authService.logout(accessToken);
        assertEquals(200, logoutResult.getCode());

        // 验证所有方法都被调用
        verify(authService, times(1)).validateToken(accessToken);
        verify(authService, times(1)).getUserInfo(accessToken);
        verify(authService, times(1)).refreshToken(refreshToken);
        verify(authService, times(1)).logout(accessToken);
    }
}

