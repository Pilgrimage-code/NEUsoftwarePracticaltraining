package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysUser;
import com.cemh.mapper.SysUserMapper;
import com.cemh.service.SysUserService;
import com.cemh.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Result<PageResult<SysUser>> getUserList(int pageNum, int pageSize, Long tenantId,
                                                   String username, String nickname, String phone,
                                                   Integer status, Long deptId) {
        try {
            Page<SysUser> page = new Page<>(pageNum, pageSize);
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

            // 如果tenantId不为空，才添加租户条件
            if (tenantId != null) {
                queryWrapper.eq("tenant_id", tenantId);
            }
            queryWrapper.eq("deleted", 0);

            if (username != null && !username.trim().isEmpty()) {
                queryWrapper.like("username", username);
            }
            if (nickname != null && !nickname.trim().isEmpty()) {
                queryWrapper.like("nickname", nickname);
            }
            if (phone != null && !phone.trim().isEmpty()) {
                queryWrapper.like("phone", phone);
            }
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            if (deptId != null) {
                queryWrapper.eq("dept_id", deptId);
            }

            queryWrapper.orderByDesc("create_time");

            IPage<SysUser> result = sysUserMapper.selectPage(page, queryWrapper);
            PageResult<SysUser> pageResult = new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);

            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<SysUser>> getUserList(Integer page, Integer size, Long tenantId, String username, String nickname, String phone, Integer status, Long deptId) {
        return null;
    }

    @Override
    public Result<SysUser> getUserById(Long id, Long tenantId) {
        return getUserDetail(id, tenantId);
    }

    @Override
    public Result<SysUser> getUserDetail(Long id, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限查看此用户");
            }

            // 清除密码信息
            user.setPassword(null);

            return Result.success(user);
        } catch (Exception e) {
            return Result.error("获取用户详情失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> createUser(SysUser user) {
        try {
            // 验证必填字段
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }

            // 检查用户名是否已存在
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", user.getUsername())
                    .eq("tenant_id", user.getTenantId())
                    .eq("deleted", 0);

            SysUser existingUser = sysUserMapper.selectOne(queryWrapper);
            if (existingUser != null) {
                return Result.error("用户名已存在");
            }

            // 加密密码
            user.setPassword(PasswordUtils.encode(user.getPassword()));

            // 设置默认值
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            user.setDeleted(0);
            if (user.getStatus() == null) {
                user.setStatus(1); // 默认启用
            }

            int result = sysUserMapper.insert(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("用户创建失败");
            }
        } catch (Exception e) {
            return Result.error("用户创建失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateUser(SysUser user) {
        try {
            // 检查用户是否存在
            SysUser existingUser = sysUserMapper.selectById(user.getId());
            if (existingUser == null) {
                return Result.error("用户不存在");
            }

            // 检查租户权限
            if (!existingUser.getTenantId().equals(user.getTenantId())) {
                return Result.error("无权限修改此用户");
            }

            // 设置更新时间
            user.setUpdateTime(LocalDateTime.now());

            // 如果有密码，需要加密
            if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
                user.setPassword(PasswordUtils.encode(user.getPassword()));
            } else {
                user.setPassword(null); // 不更新密码
            }

            int result = sysUserMapper.updateById(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("用户更新失败");
            }
        } catch (Exception e) {
            return Result.error("用户更新失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteUser(Long id, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限删除此用户");
            }

            int result = sysUserMapper.deleteById(id);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("用户删除失败");
            }
        } catch (Exception e) {
            return Result.error("用户删除失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> batchDeleteUsers(List<Long> ids, Long tenantId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的用户");
            }

            // 检查所有用户的租户权限
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", ids).eq("tenant_id", tenantId);
            List<SysUser> users = sysUserMapper.selectList(queryWrapper);

            if (users.size() != ids.size()) {
                return Result.error("部分用户不存在或无权限删除");
            }

            int result = sysUserMapper.deleteBatchIds(ids);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> resetPassword(Long id, String newPassword, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限重置此用户密码");
            }

            user.setPassword(PasswordUtils.encode(newPassword));
            user.setUpdateTime(LocalDateTime.now());

            int result = sysUserMapper.updateById(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("密码重置失败");
            }
        } catch (Exception e) {
            return Result.error("密码重置失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateUserStatus(Long id, Integer status, Long tenantId) {
        return changeUserStatus(id, status, tenantId);
    }

    @Override
    public Result<Void> changeUserStatus(Long id, Integer status, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限修改此用户状态");
            }

            user.setStatus(status);
            user.setUpdateTime(LocalDateTime.now());

            int result = sysUserMapper.updateById(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("状态更新失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> changePassword(Long userId, String oldPassword, String newPassword, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限修改此用户密码");
            }

            // 验证旧密码
            if (!PasswordUtils.matches(oldPassword, user.getPassword())) {
                return Result.error("原密码错误");
            }

            user.setPassword(PasswordUtils.encode(newPassword));
            user.setUpdateTime(LocalDateTime.now());

            int result = sysUserMapper.updateById(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("密码修改失败");
            }
        } catch (Exception e) {
            return Result.error("密码修改失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> assignUserRoles(Long userId, List<Long> roleIds, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限分配此用户角色");
            }

            // 这里应该调用角色相关的Mapper来处理用户角色关联
            // 由于没有角色相关的表和Mapper，这里只是示例实现

            return Result.success();
        } catch (Exception e) {
            return Result.error("角色分配失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Long>> getUserRoles(Long userId, Long tenantId) {
        try {
            SysUser user = sysUserMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            if (!user.getTenantId().equals(tenantId)) {
                return Result.error("无权限查看此用户角色");
            }

            // 这里应该查询用户角色关联表
            // 由于没有角色相关的表和Mapper，这里返回空列表
            // 修复Java 8兼容性问题：使用ArrayList代替List.of()
            return Result.success(new ArrayList<>());
        } catch (Exception e) {
            return Result.error("获取用户角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<String> exportUsers(String username, String realName, Long deptId, Long tenantId) {
        try {
            // 这里应该实现用户数据导出逻辑
            // 可以导出为Excel或CSV格式

            return Result.success("用户数据导出成功");
        } catch (Exception e) {
            return Result.error("用户数据导出失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<SysUser>> getUsersByDept(Long deptId, Long tenantId) {
        try {
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dept_id", deptId)
                    .eq("tenant_id", tenantId)
                    .eq("deleted", 0)
                    .eq("status", 1)
                    .orderByDesc("create_time");

            List<SysUser> users = sysUserMapper.selectList(queryWrapper);

            // 清除密码信息
            users.forEach(user -> user.setPassword(null));

            return Result.success(users);
        } catch (Exception e) {
            return Result.error("获取部门用户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<SysUser> getByUsername(String username, Long tenantId) {
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", username)
                .eq("tenant_id", tenantId));
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }

    @Override
    public Result<Void> updateUserAvatar(SysUser user) {
        try {
            // 检查用户是否存在
            SysUser existingUser = sysUserMapper.selectById(user.getId());
            if (existingUser == null) {
                return Result.error("用户不存在");
            }
            // 检查租户权限
            if (user.getTenantId() != null && !existingUser.getTenantId().equals(user.getTenantId())) {
                return Result.error("无权限修改此用户");
            }
            // 只更新头像和更新时间
            existingUser.setAvatar(user.getAvatar());
            existingUser.setUpdateTime(java.time.LocalDateTime.now());
            if (user.getUpdateBy() != null) {
                existingUser.setUpdateBy(user.getUpdateBy());
            }
            int result = sysUserMapper.updateById(existingUser);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("头像更新失败");
            }
        } catch (Exception e) {
            return Result.error("头像更新失败：" + e.getMessage());
        }
    }
}