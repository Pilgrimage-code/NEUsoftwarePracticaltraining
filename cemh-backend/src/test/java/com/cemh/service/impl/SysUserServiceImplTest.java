package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.entity.SysUser;
import com.cemh.mapper.SysUserMapper;
import com.cemh.utils.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysUserServiceImplTest {

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    @Mock
    private SysUserMapper sysUserMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        SysUser user = new SysUser();
        user.setUsername("tester");
        user.setPassword("password");
        user.setTenantId(1L);

        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysUserMapper.insert(any(SysUser.class))).thenReturn(1);

        Result<Void> result = sysUserService.createUser(user);
        assertTrue(result.isSuccess());
        verify(sysUserMapper, times(1)).insert(any(SysUser.class));
    }

    @Test
    void testCreateUserWithExistingUsername() {
        SysUser user = new SysUser();
        user.setUsername("existinguser");
        user.setPassword("password");
        user.setTenantId(1L);

        when(sysUserMapper.selectOne(any(QueryWrapper.class))).thenReturn(user);

        Result<Void> result = sysUserService.createUser(user);
        assertFalse(result.isSuccess());
        assertEquals("用户名已存在", result.getMessage());
        verify(sysUserMapper, never()).insert(any(SysUser.class));
    }

    @Test
    void testUpdateUserSuccess() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("updateduser");
        user.setTenantId(1L);

        SysUser existingUser = new SysUser();
        existingUser.setId(1L);
        existingUser.setTenantId(1L);

        when(sysUserMapper.selectById(user.getId())).thenReturn(existingUser);
        when(sysUserMapper.updateById(any(SysUser.class))).thenReturn(1);

        Result<Void> result = sysUserService.updateUser(user);
        assertTrue(result.isSuccess());
        verify(sysUserMapper, times(1)).updateById(any(SysUser.class));
    }

    @Test
    void testDeleteUserSuccess() {
        Long userId = 1L;
        Long tenantId = 1L;

        SysUser existingUser = new SysUser();
        existingUser.setId(userId);
        existingUser.setTenantId(tenantId);

        when(sysUserMapper.selectById(userId)).thenReturn(existingUser);
        when(sysUserMapper.deleteById(userId)).thenReturn(1);

        Result<Void> result = sysUserService.deleteUser(userId, tenantId);
        assertTrue(result.isSuccess());
        verify(sysUserMapper, times(1)).deleteById(userId);
    }

    @Test
    void testResetPasswordSuccess() {
        Long userId = 1L;
        String newPassword = "newpassword";
        Long tenantId = 1L;

        SysUser user = new SysUser();
        user.setId(userId);
        user.setTenantId(tenantId);

        when(sysUserMapper.selectById(userId)).thenReturn(user);
        when(sysUserMapper.updateById(any(SysUser.class))).thenReturn(1);

        Result<Void> result = sysUserService.resetPassword(userId, newPassword, tenantId);
        assertTrue(result.isSuccess());
        verify(sysUserMapper, times(1)).updateById(any(SysUser.class));
    }

    @Test
    void testChangePasswordSuccess() {
        Long userId = 1L;
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        Long tenantId = 1L;

        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(PasswordUtils.encode(oldPassword)); // Encode the old password for comparison
        user.setTenantId(tenantId);

        when(sysUserMapper.selectById(userId)).thenReturn(user);
        when(sysUserMapper.updateById(any(SysUser.class))).thenReturn(1);

        Result<Void> result = sysUserService.changePassword(userId, oldPassword, newPassword, tenantId);
        assertTrue(result.isSuccess());
        verify(sysUserMapper, times(1)).updateById(any(SysUser.class));
    }

    @Test
    void testChangePasswordWrongOldPassword() {
        Long userId = 1L;
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";
        Long tenantId = 1L;

        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(PasswordUtils.encode("correctoldpassword")); // Correct encoded password
        user.setTenantId(tenantId);

        when(sysUserMapper.selectById(userId)).thenReturn(user);

        Result<Void> result = sysUserService.changePassword(userId, oldPassword, newPassword, tenantId);
        assertFalse(result.isSuccess());
        assertEquals("原密码错误", result.getMessage());
        verify(sysUserMapper, never()).updateById(any(SysUser.class));
    }
}


