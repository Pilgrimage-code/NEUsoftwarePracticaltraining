package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户名和租户代码查询用户
     * @param username 用户名
     * @param tenantCode 租户代码
     * @return 用户信息
     */
    SysUser selectByUsername(@Param("username") String username, @Param("tenantCode") String tenantCode);
    
    /**
     * 查询用户列表
     * @param tenantId 租户ID
     * @param username 用户名（可选）
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param status 状态（可选）
     * @param deptId 部门ID（可选）
     * @param limit 每页数量
     * @param offset 偏移量
     * @return 用户列表
     */
    List<SysUser> selectUserList(@Param("tenantId") Long tenantId, 
                                @Param("username") String username,
                                @Param("nickname") String nickname,
                                @Param("phone") String phone,
                                @Param("status") Integer status,
                                @Param("deptId") Long deptId,
                                @Param("limit") Integer limit,
                                @Param("offset") Integer offset);
    
    /**
     * 统计用户数量
     * @param tenantId 租户ID
     * @param username 用户名（可选）
     * @param nickname 昵称（可选）
     * @param phone 手机号（可选）
     * @param status 状态（可选）
     * @param deptId 部门ID（可选）
     * @return 用户数量
     */
    Integer countUsers(@Param("tenantId") Long tenantId, 
                      @Param("username") String username,
                      @Param("nickname") String nickname,
                      @Param("phone") String phone,
                      @Param("status") Integer status,
                      @Param("deptId") Long deptId);
    
    /**
     * 查询用户角色
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectUserRoles(@Param("userId") Long userId);
    
    /**
     * 删除用户角色关联
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserRoles(@Param("userId") Long userId);
    
    /**
     * 插入用户角色关联
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 影响行数
     */
    int insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    
    /**
     * 根据部门查询用户
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SysUser> selectUsersByDept(@Param("deptId") Long deptId);
    
    /**
     * 根据用户名统计数量（用于检查重复）
     * @param username 用户名
     * @param tenantId 租户ID
     * @param excludeId 排除的用户ID（编辑时使用）
     * @return 数量
     */
    int countByUsername(@Param("username") String username, 
                       @Param("tenantId") Long tenantId, 
                       @Param("excludeId") Long excludeId);
    
    /**
     * 根据手机号统计数量（用于检查重复）
     * @param phone 手机号
     * @param tenantId 租户ID
     * @param excludeId 排除的用户ID（编辑时使用）
     * @return 数量
     */
    int countByPhone(@Param("phone") String phone, 
                    @Param("tenantId") Long tenantId, 
                    @Param("excludeId") Long excludeId);
    
    /**
     * 根据邮箱统计数量（用于检查重复）
     * @param email 邮箱
     * @param tenantId 租户ID
     * @param excludeId 排除的用户ID（编辑时使用）
     * @return 数量
     */
    int countByEmail(@Param("email") String email, 
                    @Param("tenantId") Long tenantId, 
                    @Param("excludeId") Long excludeId);
    
    /**
     * 获取最大用户ID
     * @return 最大用户ID
     */
    Long selectMaxUserId();
    
    /**
     * 物理删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int physicalDeleteById(@Param("id") Long id);
}

