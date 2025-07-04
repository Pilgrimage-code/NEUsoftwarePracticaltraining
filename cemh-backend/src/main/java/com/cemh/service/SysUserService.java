package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysUser;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户服务接口
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 */
public interface SysUserService {

    /**
     * 分页查询用户列表
     */
    Result<PageResult<SysUser>> getUserList(Integer page, Integer size, Long tenantId, String username, String nickname, String phone, Integer status, Long deptId);

    /**
     * 根据ID查询用户
     */
    Result<SysUser> getUserById(Long id, Long tenantId);

    /**
     * 根据ID查询用户详情
     */
    Result<SysUser> getUserDetail(Long id, Long tenantId);

    /**
     * 创建用户
     */
    Result<Void> createUser(SysUser user);

    /**
     * 更新用户
     */
    Result<Void> updateUser(SysUser user);

    /**
     * 删除用户
     * @param id 用户ID
     * @param userId 当前操作用户ID
     * @return 操作结果
     */
    Result<Void> deleteUser(Long id, Long userId);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @param userId 当前操作用户ID
     * @return 操作结果
     */
    Result<Void> batchDeleteUsers(List<Long> ids, Long userId);

    /**
     * 重置密码
     * @param id 用户ID
     * @param newPassword 新密码
     * @param userId 当前操作用户ID
     * @return 操作结果
     */
    Result<Void> resetPassword(Long id, String newPassword, Long userId);

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 状态（0：禁用，1：启用）
     * @param userId 当前操作用户ID
     * @return 操作结果
     */
    Result<Void> updateUserStatus(Long id, Integer status, Long userId);

    /**
     * 修改用户状态
     * @param id 用户ID
     * @param status 状态（0：禁用，1：启用）
     * @param userId 当前操作用户ID
     * @return 操作结果
     */
    Result<Void> changeUserStatus(Long id, Integer status, Long userId);

    /**
     * 修改密码
     */
    Result<Void> changePassword(Long userId, String oldPassword, String newPassword, Long tenantId);

    /**
     * 分配用户角色
     */
    Result<Void> assignUserRoles(Long userId, List<Long> roleIds, Long tenantId);

    /**
     * 获取用户角色
     */
    Result<List<Long>> getUserRoles(Long userId, Long tenantId);

    /**
     * 导出用户数据
     * @param username 用户名
     * @param nickname 昵称
     * @param deptId 部门ID
     * @param tenantId 租户ID
     * @param response HTTP响应对象
     * @throws Exception 导出异常
     */
    void exportUsers(String username, String nickname, Long deptId, Long tenantId, HttpServletResponse response) throws Exception;

    /**
     * 获取部门下的用户
     */
    Result<List<SysUser>> getUsersByDept(Long deptId, Long tenantId);

    /**
     * 根据用户名查询用户
     */
    Result<SysUser> getByUsername(String username, Long tenantId);

    /**
     * 只更新用户头像
     */
    Result<Void> updateUserAvatar(SysUser user);
}

