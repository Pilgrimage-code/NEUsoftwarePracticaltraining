package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.RegisterUserDTO;
import com.cemh.entity.SysDept;
import com.cemh.entity.SysTenant;
import com.cemh.entity.SysUser;
import com.cemh.service.CaptchaService;
import com.cemh.service.SysDeptService;
import com.cemh.service.SysTenantService;
import com.cemh.service.SysUserService;
import com.cemh.vo.LoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysUserControllerTest {

    @InjectMocks
    private SysUserController sysUserController;

    @Mock
    private SysUserService sysUserService;

    @Mock
    private SysTenantService sysTenantService;

    @Mock
    private SysDeptService sysDeptService;

    @Mock
    private CaptchaService captchaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserList() {
        // 准备测试数据
        Integer page = 1;
        Integer size = 10;
        String username = "test";
        String nickname = "测试";
        String phone = "13800138000";
        Integer status = 1;
        Long deptId = 1L;
        Long tenantId = 1L;

        // 模拟服务层返回
        PageResult<SysUser> pageResult = new PageResult<>();
        pageResult.setRecords(Arrays.asList(new SysUser(), new SysUser()));
        pageResult.setTotal(2L);
        Result<PageResult<SysUser>> expectedResult = Result.success(pageResult);

        when(sysUserService.getUserList(page, size, tenantId, username, nickname, phone, status, deptId))
                .thenReturn(expectedResult);

        // 执行测试
        Result<PageResult<SysUser>> result = sysUserController.getUserList(page, size, username, nickname, phone, status, deptId, tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().getRecords().size());
        assertEquals(2L, result.getData().getTotal());
        verify(sysUserService, times(1)).getUserList(page, size, tenantId, username, nickname, phone, status, deptId);
    }

    @Test
    void testGetUserById() {
        // 准备测试数据
        Long userId = 1L;
        Long tenantId = 1L;

        SysUser user = new SysUser();
        user.setId(userId);
        user.setUsername("testuser");
        Result<SysUser> expectedResult = Result.success(user);

        when(sysUserService.getUserById(userId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<SysUser> result = sysUserController.getUserById(userId, tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(userId, result.getData().getId());
        assertEquals("testuser", result.getData().getUsername());
        verify(sysUserService, times(1)).getUserById(userId, tenantId);
    }

    @Test
    void testCreateUser() {
        // 准备测试数据
        SysUser user = new SysUser();
        user.setUsername("newuser");
        user.setPassword("password");
        Long tenantId = 1L;
        Long userId = 1L;

        Result<Void> expectedResult = Result.success();
        when(sysUserService.createUser(any(SysUser.class))).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysUserController.createUser(user, tenantId, userId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(tenantId, user.getTenantId());
        assertEquals(userId, user.getCreateBy());
        verify(sysUserService, times(1)).createUser(user);
    }

    @Test
    void testUpdateUser() {
        // 准备测试数据
        Long userId = 1L;
        SysUser user = new SysUser();
        user.setUsername("updateduser");
        Long tenantId = 1L;
        Long updateUserId = 2L;

        Result<Void> expectedResult = Result.success();
        when(sysUserService.updateUser(any(SysUser.class))).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysUserController.updateUser(userId, user, tenantId, updateUserId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(userId, user.getId());
        assertEquals(tenantId, user.getTenantId());
        assertEquals(updateUserId, user.getUpdateBy());
        verify(sysUserService, times(1)).updateUser(user);
    }

    @Test
    void testDeleteUser() {
        // 准备测试数据
        Long userId = 1L;
        Long tenantId = 1L;
        Long opUserId = 2L;

        Result<Void> expectedResult = Result.success();
        when(sysUserService.deleteUser(userId, opUserId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysUserController.deleteUser(userId, tenantId, opUserId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysUserService, times(1)).deleteUser(userId, opUserId);
    }

    @Test
    void testBatchDeleteUsers() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L);
        Long tenantId = 1L;
        Long opUserId = 2L;

        Result<Void> expectedResult = Result.success();
        when(sysUserService.batchDeleteUsers(ids, opUserId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysUserController.batchDeleteUsers(ids, tenantId, opUserId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysUserService, times(1)).batchDeleteUsers(ids, opUserId);
    }

    @Test
    void testRegisterUserSuccess() {
        // 准备测试数据
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("newuser");
        dto.setPassword("password");
        dto.setNickname("测试用户");
        dto.setPhone("13800138000");
        dto.setEmail("test@example.com");
        dto.setTenantId(1L);
        dto.setDeptId(1L);
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");

        // 模拟验证码验证通过
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        
        // 模拟租户和部门存在
        SysTenant tenant = new SysTenant();
        tenant.setId(dto.getTenantId());
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(tenant);
        
        SysDept dept = new SysDept();
        dept.setId(dto.getDeptId());
        when(sysDeptService.getById(dto.getDeptId())).thenReturn(dept);
        
        // 模拟用户创建成功
        when(sysUserService.createUser(any(SysUser.class))).thenReturn(Result.success());
        
        // 模拟查询新创建的用户
        SysUser createdUser = new SysUser();
        createdUser.setId(1L);
        createdUser.setUsername(dto.getUsername());
        createdUser.setNickname(dto.getNickname());
        when(sysUserService.getByUsername(dto.getUsername(), dto.getTenantId())).thenReturn(Result.success(createdUser));

        // 执行测试
        Result<LoginVO> result = sysUserController.registerUser(dto);

        // 验证结果
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(createdUser.getId(), result.getData().getUserId());
        assertEquals(createdUser.getUsername(), result.getData().getUsername());
        assertEquals(createdUser.getNickname(), result.getData().getNickname());
        verify(sysUserService, times(1)).createUser(any(SysUser.class));
        verify(sysUserService, times(1)).getByUsername(dto.getUsername(), dto.getTenantId());
    }

    @Test
    void testRegisterUserInvalidCaptcha() {
        // 准备测试数据
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("wrong");

        // 模拟验证码验证失败
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(false);

        // 执行测试
        Result<LoginVO> result = sysUserController.registerUser(dto);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("验证码错误", result.getMessage());
        verify(sysUserService, never()).createUser(any(SysUser.class));
    }

    @Test
    void testRegisterUserInvalidTenant() {
        // 准备测试数据
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");
        dto.setTenantId(999L); // 不存在的租户ID

        // 模拟验证码验证通过
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        
        // 模拟租户不存在
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(null);

        // 执行测试
        Result<LoginVO> result = sysUserController.registerUser(dto);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("所属企业不存在", result.getMessage());
        verify(sysUserService, never()).createUser(any(SysUser.class));
    }

    @Test
    void testRegisterUserInvalidDept() {
        // 准备测试数据
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");
        dto.setTenantId(1L);
        dto.setDeptId(999L); // 不存在的部门ID

        // 模拟验证码验证通过
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        
        // 模拟租户存在
        SysTenant tenant = new SysTenant();
        tenant.setId(dto.getTenantId());
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(tenant);
        
        // 模拟部门不存在
        when(sysDeptService.getById(dto.getDeptId())).thenReturn(null);

        // 执行测试
        Result<LoginVO> result = sysUserController.registerUser(dto);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("所属部门不存在", result.getMessage());
        verify(sysUserService, never()).createUser(any(SysUser.class));
    }

    @Test
    void testRegisterUserCreateFailed() {
        // 准备测试数据
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("newuser");
        dto.setPassword("password");
        dto.setTenantId(1L);
        dto.setDeptId(1L);
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");

        // 模拟验证码验证通过
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        
        // 模拟租户和部门存在
        SysTenant tenant = new SysTenant();
        tenant.setId(dto.getTenantId());
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(tenant);
        
        SysDept dept = new SysDept();
        dept.setId(dto.getDeptId());
        when(sysDeptService.getById(dto.getDeptId())).thenReturn(dept);
        
        // 模拟用户创建失败
        when(sysUserService.createUser(any(SysUser.class))).thenReturn(Result.error("用户名已存在"));

        // 执行测试
        Result<LoginVO> result = sysUserController.registerUser(dto);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("用户名已存在", result.getMessage());
        verify(sysUserService, times(1)).createUser(any(SysUser.class));
        verify(sysUserService, never()).getByUsername(anyString(), anyLong());
    }

    @Test
    void testUpdateUserStatus_Success() {
        Long userId = 1L;
        Integer status = 1;
        Long opUserId = 2L;
        when(sysUserService.updateUserStatus(userId, status, opUserId)).thenReturn(Result.success());
        Result<Void> result = sysUserController.updateUserStatus(userId, null, status, null, opUserId);
        assertTrue(result.isSuccess());
        verify(sysUserService).updateUserStatus(userId, status, opUserId);
    }

    @Test
    void testUpdateUserStatus_BodyStatus() {
        Long userId = 1L;
        Long opUserId = 2L;
        java.util.Map<String, Integer> body = new java.util.HashMap<>();
        body.put("status", 1);
        when(sysUserService.updateUserStatus(userId, 1, opUserId)).thenReturn(Result.success());
        Result<Void> result = sysUserController.updateUserStatus(userId, body, null, null, opUserId);
        assertTrue(result.isSuccess());
    }

    @Test
    void testUpdateUserStatus_StatusNull() {
        Long userId = 1L;
        Long opUserId = 2L;
        Result<Void> result = sysUserController.updateUserStatus(userId, null, null, null, opUserId);
        assertFalse(result.isSuccess());
        assertEquals("状态不能为空", result.getMessage());
    }

    @Test
    void testResetPassword() {
        Long userId = 1L;
        Long opUserId = 2L;
        when(sysUserService.resetPassword(userId, "123456", opUserId)).thenReturn(Result.success());
        Result<Void> result = sysUserController.resetPassword(userId, null, opUserId);
        assertTrue(result.isSuccess());
        verify(sysUserService).resetPassword(userId, "123456", opUserId);
    }

    @Test
    void testChangePassword() {
        Long userId = 1L;
        String oldPwd = "old";
        String newPwd = "new";
        Long tenantId = 1L;
        when(sysUserService.changePassword(userId, oldPwd, newPwd, tenantId)).thenReturn(Result.success());
        Result<Void> result = sysUserController.changePassword(userId, oldPwd, newPwd, tenantId);
        assertTrue(result.isSuccess());
        verify(sysUserService).changePassword(userId, oldPwd, newPwd, tenantId);
    }

    @Test
    void testGetUserRoles() {
        Long userId = 1L;
        Long tenantId = 1L;
        when(sysUserService.getUserRoles(userId, tenantId)).thenReturn(Result.success(java.util.Arrays.asList(1L, 2L)));
        Result<List<Long>> result = sysUserController.getUserRoles(userId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testAssignUserRoles() {
        Long userId = 1L;
        Long tenantId = 1L;
        List<Long> roleIds = java.util.Arrays.asList(1L, 2L);
        when(sysUserService.assignUserRoles(userId, roleIds, tenantId)).thenReturn(Result.success());
        Result<Void> result = sysUserController.assignUserRoles(userId, roleIds, tenantId);
        assertTrue(result.isSuccess());
        verify(sysUserService).assignUserRoles(userId, roleIds, tenantId);
    }

    @Test
    void testExportUsers() throws Exception {
        javax.servlet.http.HttpServletRequest req = mock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse resp = mock(javax.servlet.http.HttpServletResponse.class);
        doNothing().when(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
        sysUserController.exportUsers("u", "n", 1L, 1L, req, resp);
        verify(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
    }

    @Test
    void testExportUsers_Exception() throws Exception {
        javax.servlet.http.HttpServletRequest req = mock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse resp = mock(javax.servlet.http.HttpServletResponse.class);
        doThrow(new RuntimeException("导出异常")).when(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
        sysUserController.exportUsers("u", "n", 1L, 1L, req, resp);
        verify(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
    }

    @Test
    void testGetUsersByDept() {
        Long deptId = 1L;
        Long tenantId = 1L;
        when(sysUserService.getUsersByDept(deptId, tenantId)).thenReturn(Result.success(java.util.Arrays.asList(new SysUser())));
        Result<List<SysUser>> result = sysUserController.getUsersByDept(deptId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testUpdateAvatar_Success() {
        Long userId = 1L;
        Long tenantId = 1L;
        Long opUserId = 2L;
        java.util.Map<String, String> body = new java.util.HashMap<>();
        body.put("avatar", "url");
        when(sysUserService.updateUserAvatar(any(SysUser.class))).thenReturn(Result.success());
        Result<Void> result = sysUserController.updateAvatar(userId, body, tenantId, opUserId);
        assertTrue(result.isSuccess());
        verify(sysUserService).updateUserAvatar(any(SysUser.class));
    }

    @Test
    void testUpdateAvatar_AvatarNull() {
        Long userId = 1L;
        java.util.Map<String, String> body = new java.util.HashMap<>();
        Result<Void> result = sysUserController.updateAvatar(userId, body, null, null);
        assertFalse(result.isSuccess());
        assertEquals("头像不能为空", result.getMessage());
    }

    @Test
    void testUpdateAvatar_AvatarEmpty() {
        Long userId = 1L;
        java.util.Map<String, String> body = new java.util.HashMap<>();
        body.put("avatar", "   ");
        Result<Void> result = sysUserController.updateAvatar(userId, body, null, null);
        assertFalse(result.isSuccess());
        assertEquals("头像不能为空", result.getMessage());
    }

    @Test
    void testExportUsers_TenantIdNull_HeaderUserId() throws Exception {
        javax.servlet.http.HttpServletRequest req = mock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse resp = mock(javax.servlet.http.HttpServletResponse.class);
        when(req.getHeader("X-User-Id")).thenReturn("123");
        SysUser user = new SysUser();
        user.setTenantId(99L);
        when(sysUserService.getUserById(123L, null)).thenReturn(Result.success(user));
        doNothing().when(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
        sysUserController.exportUsers("u", "n", 1L, null, req, resp);
        verify(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
    }

    @Test
    void testExportUsers_TenantIdNull_HeaderUserIdParseException() throws Exception {
        javax.servlet.http.HttpServletRequest req = mock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse resp = mock(javax.servlet.http.HttpServletResponse.class);
        when(req.getHeader("X-User-Id")).thenReturn("abc"); // 非数字
        doNothing().when(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
        sysUserController.exportUsers("u", "n", 1L, null, req, resp);
        verify(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
    }

    @Test
    void testExportUsers_TenantIdNull_HeaderUserIdNull() throws Exception {
        javax.servlet.http.HttpServletRequest req = mock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse resp = mock(javax.servlet.http.HttpServletResponse.class);
        when(req.getHeader("X-User-Id")).thenReturn(null);
        doNothing().when(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
        sysUserController.exportUsers("u", "n", 1L, null, req, resp);
        verify(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
    }

    @Test
    void testExportUsers_TenantIdNull_UserNotFound() throws Exception {
        javax.servlet.http.HttpServletRequest req = mock(javax.servlet.http.HttpServletRequest.class);
        javax.servlet.http.HttpServletResponse resp = mock(javax.servlet.http.HttpServletResponse.class);
        when(req.getHeader("X-User-Id")).thenReturn("123");
        when(sysUserService.getUserById(123L, null)).thenReturn(Result.success(null));
        doNothing().when(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
        sysUserController.exportUsers("u", "n", 1L, null, req, resp);
        verify(sysUserService).exportUsers(any(), any(), any(), any(), eq(resp));
    }

    @Test
    void testUpdateUserStatus_ServiceException() {
        Long userId = 1L;
        Integer status = 1;
        Long opUserId = 2L;
        when(sysUserService.updateUserStatus(userId, status, opUserId)).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.updateUserStatus(userId, null, status, null, opUserId));
    }


    @Test
    void testResetPassword_ServiceException() {
        Long userId = 1L;
        Long opUserId = 2L;
        when(sysUserService.resetPassword(userId, "123456", opUserId)).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.resetPassword(userId, null, opUserId));
    }

    @Test
    void testChangePassword_ServiceException() {
        Long userId = 1L;
        String oldPwd = "old";
        String newPwd = "new";
        Long tenantId = 1L;
        when(sysUserService.changePassword(userId, oldPwd, newPwd, tenantId)).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.changePassword(userId, oldPwd, newPwd, tenantId));
    }

    @Test
    void testGetUserRoles_ServiceException() {
        Long userId = 1L;
        Long tenantId = 1L;
        when(sysUserService.getUserRoles(userId, tenantId)).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.getUserRoles(userId, tenantId));
    }

    @Test
    void testAssignUserRoles_ServiceException() {
        Long userId = 1L;
        Long tenantId = 1L;
        List<Long> roleIds = java.util.Arrays.asList(1L, 2L);
        when(sysUserService.assignUserRoles(userId, roleIds, tenantId)).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.assignUserRoles(userId, roleIds, tenantId));
    }

    @Test
    void testGetUsersByDept_ServiceException() {
        Long deptId = 1L;
        Long tenantId = 1L;
        when(sysUserService.getUsersByDept(deptId, tenantId)).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.getUsersByDept(deptId, tenantId));
    }

    @Test
    void testUpdateAvatar_ServiceException() {
        Long userId = 1L;
        Long tenantId = 1L;
        Long opUserId = 2L;
        java.util.Map<String, String> body = new java.util.HashMap<>();
        body.put("avatar", "url");
        when(sysUserService.updateUserAvatar(any(SysUser.class))).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.updateAvatar(userId, body, tenantId, opUserId));
    }

    @Test
    void testRegisterUser_TenantIdNull() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");
        dto.setDeptId(1L);
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        Result<LoginVO> result = sysUserController.registerUser(dto);
        assertFalse(result.isSuccess());
        assertEquals("所属企业不存在", result.getMessage());
    }

    @Test
    void testRegisterUser_DeptIdNull() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");
        dto.setTenantId(1L);
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        SysTenant tenant = new SysTenant();
        tenant.setId(dto.getTenantId());
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(tenant);
        Result<LoginVO> result = sysUserController.registerUser(dto);
        assertFalse(result.isSuccess());
        assertEquals("所属部门不存在", result.getMessage());
    }

    @Test
    void testRegisterUser_GetByUsernameNull() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");
        dto.setTenantId(1L);
        dto.setDeptId(1L);
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        SysTenant tenant = new SysTenant();
        tenant.setId(dto.getTenantId());
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(tenant);
        SysDept dept = new SysDept();
        dept.setId(dto.getDeptId());
        when(sysDeptService.getById(dto.getDeptId())).thenReturn(dept);
        when(sysUserService.createUser(any(SysUser.class))).thenReturn(Result.success());
        when(sysUserService.getByUsername(dto.getUsername(), dto.getTenantId())).thenReturn(Result.success(null));
        Result<LoginVO> result = sysUserController.registerUser(dto);
        assertTrue(result.isSuccess());
        assertNull(result.getData().getUserId());
    }

    @Test
    void testRegisterUser_CreateUserException() {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setCaptchaKey("key");
        dto.setCaptcha("1234");
        dto.setTenantId(1L);
        dto.setDeptId(1L);
        when(captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())).thenReturn(true);
        SysTenant tenant = new SysTenant();
        tenant.setId(dto.getTenantId());
        when(sysTenantService.getById(dto.getTenantId())).thenReturn(tenant);
        SysDept dept = new SysDept();
        dept.setId(dto.getDeptId());
        when(sysDeptService.getById(dto.getDeptId())).thenReturn(dept);
        when(sysUserService.createUser(any(SysUser.class))).thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class, () -> sysUserController.registerUser(dto));
    }
} 