package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 角色实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2025-6-28
 */
@Schema(description = "角色信息")
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户ID")
    @TableField("tenant_id")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "角色编码")
    @TableField("role_code")
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;

    @Schema(description = "角色名称")
    @TableField("role_name")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100个字符")
    private String roleName;

    @Schema(description = "角色类型（1：普通角色，2：系统角色）")
    @TableField("role_type")
    private Integer roleType;

    @Schema(description = "数据权限（1：全部，2：本部门，3：本部门及下级，4：仅本人）")
    @TableField("data_scope")
    private Integer dataScope;

    @Schema(description = "显示顺序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "状态（0：禁用，1：启用）")
    @TableField("status")
    private Integer status;

    @Schema(description = "备注")
    @TableField("remark")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    // 非数据库字段
    @Schema(description = "租户名称")
    @TableField(exist = false)
    private String tenantName;

    public SysRole() {
        this.status = 1; // 默认启用状态
        this.roleType = 1; // 默认普通角色
        this.dataScope = 1; // 默认全部数据权限
        this.sortOrder = 0; // 默认排序
    }

    // Getter and Setter methods
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "tenantId=" + tenantId +
                ", roleCode='" + roleCode + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleType=" + roleType +
                ", dataScope=" + dataScope +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", tenantName='" + tenantName + '\'' +
                "} " + super.toString();
    }
}

