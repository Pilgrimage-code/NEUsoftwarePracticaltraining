package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysUser;

import java.util.List;

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
    Result<PageResult<SysUser>> getUserList(int pageNum, int pageSize, Long tenantId, 
                                           String username, String nickname, String phone, 
                                           Integer status, Long deptId);

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
     */
    Result<Void> deleteUser(Long id, Long tenantId);

    /**
     * 批量删除用户
     */
    Result<Void> batchDeleteUsers(List<Long> ids, Long tenantId);

    /**
     * 重置密码
     */
    Result<Void> resetPassword(Long id, String newPassword, Long tenantId);

    /**
     * 更新用户状态
     */
    Result<Void> updateUserStatus(Long id, Integer status, Long tenantId);

    /**
     * 修改用户状态
     */
    Result<Void> changeUserStatus(Long id, Integer status, Long tenantId);

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
     * 导出用户列表
     */
    Result<String> exportUsers(String username, String realName, Long deptId, Long tenantId);

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

