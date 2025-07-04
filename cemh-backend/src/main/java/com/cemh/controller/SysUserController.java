package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysUser;
import com.cemh.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import com.cemh.dto.LoginDTO;
import com.cemh.vo.LoginVO;
import com.cemh.service.SysTenantService;
import com.cemh.service.SysDeptService;
import com.cemh.service.CaptchaService;
import com.cemh.dto.RegisterUserDTO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysTenantService sysTenantService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private CaptchaService captchaService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping
    public Result<PageResult<SysUser>> getUserList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "昵称") @RequestParam(required = false) String nickname,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {

        return sysUserService.getUserList(page, size, tenantId, username, nickname, phone, status, deptId);
    }

    @Operation(summary = "根据ID查询用户")
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@Parameter(description = "用户ID") @PathVariable Long id,
                                       @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return sysUserService.getUserById(id, tenantId);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public Result<Void> createUser(@Valid @RequestBody SysUser user,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                   @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (tenantId != null) {
            user.setTenantId(tenantId);
        }
        if (userId != null) {
            user.setCreateBy(userId);
        }
        return sysUserService.createUser(user);
    }

    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public Result<Void> updateUser(@Parameter(description = "用户ID") @PathVariable Long id,
                                   @RequestBody SysUser user,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                   @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        user.setId(id);
        if (tenantId != null) {
            user.setTenantId(tenantId);
        }
        if (userId != null) {
            user.setUpdateBy(userId);
        }
        return sysUserService.updateUser(user);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                   @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return sysUserService.deleteUser(id, userId);
    }

    @Operation(summary = "批量删除用户")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteUsers(@RequestBody List<Long> ids,
                                         @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                         @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return sysUserService.batchDeleteUsers(ids, userId);
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateUserStatus(@Parameter(description = "用户ID") @PathVariable Long id,
                                         @RequestBody(required = false) java.util.Map<String, Integer> body,
                                         @RequestParam(required = false) Integer status,
                                         @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                         @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 支持两种方式：RequestParam或RequestBody
        Integer finalStatus = status;
        if (finalStatus == null && body != null) {
            finalStatus = body.get("status");
        }
        
        if (finalStatus == null) {
            return Result.error("状态不能为空");
        }
        
        return sysUserService.updateUserStatus(id, finalStatus, userId);
    }

    @Operation(summary = "重置密码")
    @PutMapping("/{id}/password/reset")
    public Result<Void> resetPassword(@Parameter(description = "用户ID") @PathVariable Long id,
                                      @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                      @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        // 重置为默认密码123456
        return sysUserService.resetPassword(id, "123456", userId);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/{id}/password")
    public Result<Void> changePassword(@Parameter(description = "用户ID") @PathVariable Long id,
                                       @Parameter(description = "旧密码") @RequestParam String oldPassword,
                                       @Parameter(description = "新密码") @RequestParam String newPassword,
                                       @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return sysUserService.changePassword(id, oldPassword, newPassword, tenantId);
    }

    @Operation(summary = "获取用户角色")
    @GetMapping("/{id}/roles")
    public Result<List<Long>> getUserRoles(@Parameter(description = "用户ID") @PathVariable Long id,
                                           @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return sysUserService.getUserRoles(id, tenantId);
    }

    @Operation(summary = "分配用户角色")
    @PutMapping("/{id}/roles")
    public Result<Void> assignUserRoles(@Parameter(description = "用户ID") @PathVariable Long id,
                                        @RequestBody List<Long> roleIds,
                                        @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return sysUserService.assignUserRoles(id, roleIds, tenantId);
    }

    @Operation(summary = "导出用户数据")
    @GetMapping("/export")
    public void exportUsers(@Parameter(description = "用户名") @RequestParam(required = false) String username,
                                      @Parameter(description = "昵称") @RequestParam(required = false) String nickname,
                                      @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
                            @Parameter(description = "租户ID") @RequestParam(required = false) Long tenantId,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            // 如果未指定租户ID，则使用当前用户的租户ID
            if (tenantId == null) {
                String userIdStr = request.getHeader("X-User-Id");
                if (userIdStr != null && !userIdStr.isEmpty()) {
                    try {
                        Long userId = Long.parseLong(userIdStr);
                        SysUser currentUser = sysUserService.getUserById(userId, null).getData();
                        if (currentUser != null) {
                            tenantId = currentUser.getTenantId();
                        }
                    } catch (Exception e) {
                        // 忽略解析错误
                    }
                }
            }
            
            // 设置默认租户ID为1，避免空值
            if (tenantId == null) {
                tenantId = 1L;
            }
            
            sysUserService.exportUsers(username, nickname, deptId, tenantId, response);
        } catch (Exception e) {
            e.printStackTrace();
            // 异常已在Service层处理
        }
    }

    @Operation(summary = "获取部门下的用户")
    @GetMapping("/dept/{deptId}")
    public Result<List<SysUser>> getUsersByDept(@Parameter(description = "部门ID") @PathVariable Long deptId,
                                                @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return sysUserService.getUsersByDept(deptId, tenantId);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginVO> registerUser(@RequestBody RegisterUserDTO dto) {
        // 校验验证码（假设有CaptchaService）
        if (!captchaService.verify(dto.getCaptchaKey(), dto.getCaptcha())) {
            return Result.error("验证码错误");
        }
        // 校验租户
        if (dto.getTenantId() == null || sysTenantService.getById(dto.getTenantId()) == null) {
            return Result.error("所属企业不存在");
        }
        // 校验部门
        if (dto.getDeptId() == null || sysDeptService.getById(dto.getDeptId()) == null) {
            return Result.error("所属部门不存在");
        }
        // 构造用户
        SysUser user = new SysUser();
        user.setTenantId(dto.getTenantId());
        user.setDeptId(dto.getDeptId());
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatar(dto.getAvatar());
        user.setStatus(1);
        Result<Void> res = sysUserService.createUser(user);
        if (!res.isSuccess()) return Result.error(res.getMessage());
        // 查询新用户
        SysUser created = sysUserService.getByUsername(dto.getUsername(), dto.getTenantId()).getData();
        LoginVO vo = new LoginVO();
        vo.setUserId(created.getId());
        vo.setUsername(created.getUsername());
        vo.setNickname(created.getNickname());
        vo.setAvatar(created.getAvatar());
        return Result.success(vo);
    }

    @Operation(summary = "修改头像")
    @PutMapping("/{id}/avatar")
    public Result<Void> updateAvatar(@Parameter(description = "用户ID") @PathVariable Long id,
                                     @RequestBody java.util.Map<String, String> body,
                                     @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        String avatar = body.get("avatar");
        if (avatar == null || avatar.trim().isEmpty()) {
            return Result.error("头像不能为空");
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setAvatar(avatar);
        if (tenantId != null) {
            user.setTenantId(tenantId);
        }
        if (userId != null) {
            user.setUpdateBy(userId);
        }
        return sysUserService.updateUserAvatar(user);
    }
}