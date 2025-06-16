package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 租户实体类
 */
@TableName("sys_tenant")
@Schema(description = "租户信息")
public class SysTenant {

    /**
     * 租户ID（主键）
     */
    @Schema(description = "租户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户代码（唯一标识）
     */
    @Schema(description = "租户代码")
    @TableField("tenant_code")
    private String tenantCode;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称")
    @TableField("tenant_name")
    private String tenantName;

    /**
     * 联系人
     */
    @Schema(description = "联系人")
    @TableField("contact_name")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Schema(description = "联系邮箱")
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 租户状态：0-禁用，1-启用
     */
    @Schema(description = "租户状态")
    @TableField("status")
    private Integer status;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 最大用户数
     */
    @Schema(description = "最大用户数")
    @TableField("max_users")
    private Integer maxUsers;

    /**
     * 备注信息
     */
    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人ID
     */
    @Schema(description = "更新人ID")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
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
}

