package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * 用户角色关联实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-16
 */
@Schema(description = "用户角色关联信息")
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @TableField("user_id")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "角色ID")
    @TableField("role_id")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    public SysUserRole() {
    }

    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    // Getter and Setter methods
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "SysUserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                "} " + super.toString();
    }
}

