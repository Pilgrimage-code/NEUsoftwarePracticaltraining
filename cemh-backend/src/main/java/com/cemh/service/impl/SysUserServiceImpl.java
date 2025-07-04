package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysDept;
import com.cemh.entity.SysUser;
import com.cemh.mapper.SysDeptMapper;
import com.cemh.mapper.SysUserMapper;
import com.cemh.service.SysUserService;
import com.cemh.utils.PasswordUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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

    @Autowired
    private SysDeptMapper sysDeptMapper;
    
    @Override
    public Result<PageResult<SysUser>> getUserList(Integer page, Integer size, Long tenantId, String username, String nickname, String phone, Integer status, Long deptId) {
        try {
            // 计算分页参数
            int offset = (page - 1) * size;
            int limit = size;
            
            // 查询总数
            Integer total = sysUserMapper.countUsers(tenantId, username, nickname, phone, status, deptId);
            
            // 查询列表
            List<SysUser> users = sysUserMapper.selectUserList(tenantId, username, nickname, phone, status, deptId, limit, offset);
            
            // 创建分页结果
            PageResult<SysUser> pageResult = new PageResult<>(users, total.longValue(), page, size);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
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
            
            // 如果密码为空，设置默认密码123456
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                user.setPassword("123456");
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

            // 确保租户ID不为空
            if (user.getTenantId() == null) {
                // 如果没有提供租户ID，设置为默认租户ID 1
                user.setTenantId(1L);
            }
            
            // 获取当前最大ID并设置新用户ID为最大ID+1
            Long maxId = sysUserMapper.selectMaxUserId();
            if (maxId != null) {
                user.setId(maxId + 1);
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
            if (user.getTenantId() != null && !existingUser.getTenantId().equals(user.getTenantId())) {
                return Result.error("无权限修改此用户");
            }
            
            // 设置更新时间
            user.setUpdateTime(LocalDateTime.now());
            
            // 如果有密码，需要加密，否则保留原密码
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                // 不更新密码，使用原密码
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(PasswordUtils.encode(user.getPassword()));
            }

            // 有选择地更新字段，避免将某些重要字段设为null
            if (user.getUsername() == null) user.setUsername(existingUser.getUsername());
            if (user.getNickname() == null) user.setNickname(existingUser.getNickname());
            if (user.getStatus() == null) user.setStatus(existingUser.getStatus());
            if (user.getDeleted() == null) user.setDeleted(existingUser.getDeleted());
            if (user.getTenantId() == null) user.setTenantId(existingUser.getTenantId());
            if (user.getDeptId() == null) user.setDeptId(existingUser.getDeptId());
            if (user.getCreateTime() == null) user.setCreateTime(existingUser.getCreateTime());
            if (user.getCreateBy() == null) user.setCreateBy(existingUser.getCreateBy());
            
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
            
            // 获取当前操作用户
            SysUser currentUser = getCurrentUser(tenantId);
            
            // 如果是管理员用户（userType=0）或者是同一租户，允许操作
            if (currentUser != null && currentUser.getUserType() == 0 || user.getTenantId().equals(tenantId)) {
                // 使用自定义方法进行物理删除
                int result = sysUserMapper.physicalDeleteById(id);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("用户删除失败");
                }
            } else {
                return Result.error("无权限删除此用户");
            }
        } catch (Exception e) {
            return Result.error("用户删除失败：" + e.getMessage());
        }
    }

    // 获取当前操作用户的辅助方法
    private SysUser getCurrentUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return sysUserMapper.selectById(userId);
    }
    
    @Override
    public Result<Void> batchDeleteUsers(List<Long> ids, Long tenantId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的用户");
            }

            // 获取当前操作用户
            SysUser currentUser = getCurrentUser(tenantId);
            boolean isAdmin = currentUser != null && currentUser.getUserType() == 0;
            
            // 检查所有用户的租户权限
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            if (!isAdmin) {
                // 非管理员只能删除自己租户的用户
            queryWrapper.in("id", ids).eq("tenant_id", tenantId);
            } else {
                // 管理员可以删除任何用户
                queryWrapper.in("id", ids);
            }
            
            List<SysUser> users = sysUserMapper.selectList(queryWrapper);
            
            if (users.size() != ids.size()) {
                return Result.error("部分用户不存在或无权限删除");
            }
            
            // 对每个用户执行物理删除
            int result = 0;
            for (Long id : ids) {
                result += sysUserMapper.physicalDeleteById(id);
            }
            
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
            
            // 获取当前操作用户
            SysUser currentUser = getCurrentUser(tenantId);
            
            // 如果是管理员用户（userType=0）或者是同一租户，允许操作
            if (currentUser != null && currentUser.getUserType() == 0 || user.getTenantId().equals(tenantId)) {
            user.setPassword(PasswordUtils.encode(newPassword));
            user.setUpdateTime(LocalDateTime.now());
            
            int result = sysUserMapper.updateById(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("密码重置失败");
                }
            } else {
                return Result.error("无权限重置此用户密码");
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
            
            // 获取当前操作用户
            SysUser currentUser = getCurrentUser(tenantId);
            
            // 如果是管理员用户（userType=0）或者是同一租户，允许操作
            if (currentUser != null && currentUser.getUserType() == 0 || user.getTenantId().equals(tenantId)) {
            user.setStatus(status);
            user.setUpdateTime(LocalDateTime.now());
            
            int result = sysUserMapper.updateById(user);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("状态更新失败");
                }
            } else {
                return Result.error("无权限修改此用户状态");
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
    public void exportUsers(String username, String nickname, Long deptId, Long tenantId, HttpServletResponse response) {
        try {
            // 构建查询条件
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (tenantId != null) {
                queryWrapper.eq("tenant_id", tenantId);
            }
            
            if (username != null && !username.isEmpty()) {
                queryWrapper.like("username", username);
            }
            
            if (nickname != null && !nickname.isEmpty()) {
                queryWrapper.like("nickname", nickname);
            }
            
            if (deptId != null) {
                queryWrapper.eq("dept_id", deptId);
            }
            
            // 查询用户列表
            List<SysUser> users = sysUserMapper.selectList(queryWrapper);
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=users_" + System.currentTimeMillis() + ".xls");
            
            // 创建工作簿 - 使用HSSFWorkbook而不是XSSFWorkbook，更简单
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("用户数据");
            
            // 创建标题行
            HSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"用户名", "昵称", "手机号", "邮箱", "性别", "用户类型", "状态", "部门", "备注", "创建时间"};
            
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 填充数据
            for (int i = 0; i < users.size(); i++) {
                SysUser user = users.get(i);
                HSSFRow row = sheet.createRow(i + 1);
                
                row.createCell(0).setCellValue(user.getUsername() != null ? user.getUsername() : "");
                row.createCell(1).setCellValue(user.getNickname() != null ? user.getNickname() : "");
                row.createCell(2).setCellValue(user.getPhone() != null ? user.getPhone() : "");
                row.createCell(3).setCellValue(user.getEmail() != null ? user.getEmail() : "");
                
                // 性别转换
                String gender = "";
                if (user.getGender() != null) {
                    if (user.getGender() == 0) {
                        gender = "男";
                    } else if (user.getGender() == 1) {
                        gender = "女";
                    } else {
                        gender = "未知";
                    }
                }
                row.createCell(4).setCellValue(gender);
                
                // 用户类型转换
                String userType = user.getUserType() != null && user.getUserType() == 0 ? "管理员" : "普通用户";
                row.createCell(5).setCellValue(userType);
                
                // 状态转换
                String status = user.getStatus() != null && user.getStatus() == 1 ? "启用" : "禁用";
                row.createCell(6).setCellValue(status);
                
                // 获取部门名称
                String deptName = "";
                if (user.getDeptId() != null) {
                    SysDept dept = sysDeptMapper.selectById(user.getDeptId());
                    if (dept != null) {
                        deptName = dept.getDeptName();
                    }
                }
                row.createCell(7).setCellValue(deptName);
                
                row.createCell(8).setCellValue(user.getRemark() != null ? user.getRemark() : "");
                row.createCell(9).setCellValue(user.getCreateTime() != null ? user.getCreateTime().toString() : "");
            }
            
            // 输出文件
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常
            try {
                response.reset();
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("导出失败：" + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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