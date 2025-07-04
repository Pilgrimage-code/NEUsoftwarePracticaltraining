package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.entity.SysUser;
import com.cemh.entity.SysTenant;
import com.cemh.mapper.SysUserMapper;
import com.cemh.mapper.SysTenantMapper;
import com.cemh.service.AuthService;
import com.cemh.utils.JwtUtils;
import com.cemh.utils.PasswordUtils;
import com.cemh.vo.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private SysTenantMapper sysTenantMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class);
             MockedStatic<JwtUtils> mockedJwtUtils = mockStatic(JwtUtils.class)) {

            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername("testuser");
            loginDTO.setPassword("password");
            loginDTO.setTenantCode("tenant1");

            SysTenant tenant = new SysTenant();
            tenant.setId(1L);
            tenant.setTenantCode("tenant1");
            tenant.setStatus(1);

            SysUser user = new SysUser();
            user.setId(1L);
            user.setUsername("testuser");
            user.setPassword("encodedpassword"); // Mocked password
            user.setTenantId(1L);
            user.setStatus(1);

            when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(tenant);
            when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(user);
            mockedPasswordUtils.when(() -> PasswordUtils.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
            mockedJwtUtils.when(() -> JwtUtils.generateToken(anyLong(), anyString(), anyLong())).thenReturn("mockAccessToken");
            mockedJwtUtils.when(() -> JwtUtils.generateRefreshToken(anyLong())).thenReturn("mockRefreshToken");

            Result<LoginVO> result = authService.login(loginDTO);
            assertTrue(result.isSuccess());
            assertNotNull(result.getData());
            assertEquals("testuser", result.getData().getUsername());
            assertEquals("mockAccessToken", result.getData().getAccessToken());
        }
    }

    @Test
    void testLoginUserNotFound() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("nonexistentuser");
        loginDTO.setPassword("password");
        loginDTO.setTenantCode("tenant1");

        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("tenant1");
        tenant.setStatus(1);

        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(tenant);
        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        Result<LoginVO> result = authService.login(loginDTO);
        assertFalse(result.isSuccess());
        assertEquals("用户名或租户代码错误", result.getMessage());
    }

    @Test
    void testLoginWrongPassword() {
        try (MockedStatic<PasswordUtils> mockedPasswordUtils = mockStatic(PasswordUtils.class)) {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername("testuser");
            loginDTO.setPassword("wrongpassword");
            loginDTO.setTenantCode("tenant1");

            SysTenant tenant = new SysTenant();
            tenant.setId(1L);
            tenant.setTenantCode("tenant1");
            tenant.setStatus(1);

            SysUser user = new SysUser();
            user.setId(1L);
            user.setUsername("testuser");
            user.setPassword("encodedpassword");
            user.setTenantId(1L);
            user.setStatus(1);

            when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(tenant);
            when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(user);
            mockedPasswordUtils.when(() -> PasswordUtils.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(false);

            Result<LoginVO> result = authService.login(loginDTO);
            assertFalse(result.isSuccess());
            assertEquals("密码错误", result.getMessage());
        }
    }
}


