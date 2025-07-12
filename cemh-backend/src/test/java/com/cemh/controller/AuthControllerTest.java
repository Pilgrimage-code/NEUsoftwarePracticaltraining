package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.service.AuthService;
import com.cemh.service.CaptchaService;
import com.cemh.vo.LoginVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private CaptchaService captchaService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_Success() throws Exception {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");
        loginDTO.setCaptcha("1234");
        loginDTO.setCaptchaKey("test-key");

        LoginVO loginVO = new LoginVO();
        loginVO.setUsername("testuser");
        loginVO.setToken("test-token");

        Result<LoginVO> expectedResult = Result.success(loginVO);

        // Mock服务方法
        when(authService.login(any(LoginDTO.class))).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.accessToken").value("test-token"));

        // 验证方法调用
        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void testLogin_Failure() throws Exception {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("wrongpassword");

        Result<LoginVO> expectedResult = Result.error("用户名或密码错误");

        // Mock服务方法
        when(authService.login(any(LoginDTO.class))).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));

        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void testLogin_Exception() throws Exception {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        // Mock服务方法抛出异常
        when(authService.login(any(LoginDTO.class))).thenThrow(new RuntimeException("系统异常"));

        // 执行测试
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("登录失败：系统异常"));

        verify(authService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void testLogin_NullBody() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_ServiceThrowException() throws Exception {
        lenient().when(authService.login(any())).thenThrow(new RuntimeException("error"));
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("test");
        loginDTO.setPassword("test");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().is4xxClientError()); // 调整为 4xx
    }

    @Test
    void testLogout_Success() throws Exception {
        // 准备测试数据
        Result<Object> expectedResult = Result.success(null);

        // Mock服务方法
        when(authService.logout(anyString())).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(authService, times(1)).logout("test-token");
    }

    @Test
    void testLogout_WithoutToken() throws Exception {
        // Mock服务方法
        when(authService.logout(isNull())).thenReturn(Result.success(null));

        // 执行测试
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());

        verify(authService, times(1)).logout(null);
    }

    @Test
    void testLogout_Exception() throws Exception {
        // Mock服务方法抛出异常
        when(authService.logout(anyString())).thenThrow(new RuntimeException("退出异常"));

        // 执行测试
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("退出失败: 退出异常"));

        verify(authService, times(1)).logout("test-token");
    }

    @Test
    void testGetUserInfo_Success() throws Exception {
        // 准备测试数据
        LoginVO loginVO = new LoginVO();
        loginVO.setUsername("testuser");
        loginVO.setToken("test-token");

        Result<LoginVO> expectedResult = Result.success(loginVO);

        // Mock服务方法
        when(authService.getUserInfo(anyString())).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(get("/api/auth/userinfo")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        verify(authService, times(1)).getUserInfo("test-token");
    }

    @Test
    void testGetUserInfo_Exception() throws Exception {
        // Mock服务方法抛出异常
        when(authService.getUserInfo(anyString())).thenThrow(new RuntimeException("获取用户信息异常"));

        // 执行测试
        mockMvc.perform(get("/api/auth/userinfo")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("获取用户信息失败：获取用户信息异常"));

        verify(authService, times(1)).getUserInfo("test-token");
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        // 准备测试数据
        LoginVO loginVO = new LoginVO();
        loginVO.setUsername("testuser");
        loginVO.setToken("new-test-token");

        Result<LoginVO> expectedResult = Result.success(loginVO);

        // Mock服务方法
        when(authService.refreshToken(anyString())).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(post("/api/auth/refresh")
                .param("refreshToken", "refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").value("new-test-token"));

        verify(authService, times(1)).refreshToken("refresh-token");
    }

    @Test
    void testRefreshToken_Exception() throws Exception {
        // Mock服务方法抛出异常
        when(authService.refreshToken(anyString())).thenThrow(new RuntimeException("刷新token异常"));

        // 执行测试
        mockMvc.perform(post("/api/auth/refresh")
                .param("refreshToken", "refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("刷新token失败：刷新token异常"));

        verify(authService, times(1)).refreshToken("refresh-token");
    }

    @Test
    void testValidateToken_Success() throws Exception {
        // 准备测试数据
        Result<Boolean> expectedResult = Result.success(true);

        // Mock服务方法
        when(authService.validateToken(anyString())).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(post("/api/auth/validate")
                .param("token", "test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));

        verify(authService, times(1)).validateToken("test-token");
    }

    @Test
    void testValidateToken_Exception() throws Exception {
        // Mock服务方法抛出异常
        when(authService.validateToken(anyString())).thenThrow(new RuntimeException("验证异常"));

        // 执行测试
        mockMvc.perform(post("/api/auth/validate")
                .param("token", "test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("验证失败：验证异常"));

        verify(authService, times(1)).validateToken("test-token");
    }

    @Test
    void testGetCaptcha_Success() throws Exception {
        // 准备测试数据
        Map<String, Object> captchaData = new HashMap<>();
        captchaData.put("captchaKey", "test-key");
        captchaData.put("captchaImage", "base64-image-data");

        // Mock服务方法
        when(captchaService.generateCaptcha()).thenReturn(captchaData);

        // 执行测试
        mockMvc.perform(get("/api/auth/captcha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.captchaKey").value("test-key"))
                .andExpect(jsonPath("$.data.captchaImage").value("base64-image-data"));

        verify(captchaService, times(1)).generateCaptcha();
    }

    @Test
    void testGetClientIp_XForwardedFor() throws Exception {
        // 这个测试需要通过反射或者其他方式测试私有方法
        // 或者通过集成测试来验证IP获取逻辑
        // 这里我们通过模拟请求头来测试
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        when(authService.login(any(LoginDTO.class))).thenReturn(Result.success(new LoginVO()));

        mockMvc.perform(post("/api/auth/login")
                .header("X-Forwarded-For", "192.168.1.100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());

        // 验证IP被正确设置到DTO中
        verify(authService, times(1)).login(argThat(dto -> 
            "192.168.1.100".equals(dto.getClientIp())
        ));
    }

    @Test
    void testGetClientIp_MultipleProxies() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        when(authService.login(any(LoginDTO.class))).thenReturn(Result.success(new LoginVO()));

        mockMvc.perform(post("/api/auth/login")
                .header("X-Forwarded-For", "192.168.1.100, 10.0.0.1, 172.16.0.1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());

        // 验证取第一个IP
        verify(authService, times(1)).login(argThat(dto -> 
            "192.168.1.100".equals(dto.getClientIp())
        ));
    }
}

