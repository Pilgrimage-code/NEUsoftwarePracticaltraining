package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Schema(description = "部门信息")
@TableName("sys_dept")
public class SysDept extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户ID")
    @TableField("tenant_id")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "父部门ID，0表示顶级部门")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "部门编码")
    @TableField("dept_code")
    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码长度不能超过50个字符")
    private String deptCode;

    @Schema(description = "部门名称")
    @TableField("dept_name")
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String deptName;

    @Schema(description = "部门类型（1：普通部门，2：虚拟部门）")
    @TableField("dept_type")
    private Integer deptType;

    @Schema(description = "部门负责人ID")
    @TableField("leader_id")
    private Long leaderId;

    @Schema(description = "部门电话")
    @TableField("phone")
    @Size(max = 20, message = "部门电话长度不能超过20个字符")
    private String phone;

    @Schema(description = "部门邮箱")
    @TableField("email")
    @Size(max = 100, message = "部门邮箱长度不能超过100个字符")
    private String email;

    @Schema(description = "部门地址")
    @TableField("address")
    @Size(max = 200, message = "部门地址长度不能超过200个字符")
    private String address;

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

    @TableField(value = "create_time", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("create_by")
    private Long createBy;

    @TableField("update_by")
    private Long updateBy;

    @TableField("deleted")
    private Integer deleted;

    // 非数据库字段
    @Schema(description = "上级部门名称")
    @TableField(exist = false)
    private String parentName;

    @Schema(description = "负责人姓名")
    @TableField(exist = false)
    private String leaderName;

    @Schema(description = "租户名称")
    @TableField(exist = false)
    private String tenantName;

    @Schema(description = "子部门列表")
    @TableField(exist = false)
    private List<SysDept> children;

    public SysDept() {
        this.status = 1; // 默认启用状态
        this.parentId = 0L; // 默认顶级部门
        this.deptType = 1; // 默认普通部门
        this.sortOrder = 0; // 默认排序
    }

    // Getter and Setter methods
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getDeptType() {
        return deptType;
    }

    public void setDeptType(Integer deptType) {
        this.deptType = deptType;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public List<SysDept> getChildren() {
        return children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "SysDept{" +
                "tenantId=" + tenantId +
                ", parentId=" + parentId +
                ", deptCode='" + deptCode + '\'' +
                ", deptName='" + deptName + '\'' +
                ", deptType=" + deptType +
                ", leaderId=" + leaderId +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", parentName='" + parentName + '\'' +
                ", leaderName='" + leaderName + '\'' +
                ", tenantName='" + tenantName + '\'' +
                "} " + super.toString();
    }
}

