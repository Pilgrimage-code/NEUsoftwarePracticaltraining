package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 租户实体类
 */
@TableName("sys_tenant")
@Schema(description = "租户信息")
public class SysTenant extends BaseEntity {



    /**
     * 租户编码
     */
    @Schema(description = "租户编码")
    @TableField("tenant_code")
    private String tenantCode;

    /**
     * 租户名称
     */
    @Schema(description = "租户名称")
    @TableField("tenant_name")
    private String tenantName;

    /**
     * 联系人名称
     */
    @Schema(description = "联系人名称")
    @TableField("contact_name")
    private String contactName;

    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    @Schema(description = "联系人邮箱")
    @TableField("contact_email")
    private String contactEmail;

    /**
     * 租户Logo URL
     */
    @Schema(description = "租户Logo URL")
    @TableField("logo_url")
    private String logoUrl;

    /**
     * 租户域名
     */
    @Schema(description = "租户域名")
    @TableField("domain")
    private String domain;

    /**
     * 租户状态：0-禁用，1-启用
     */
    @Schema(description = "租户状态")
    @TableField("status")
    private Integer status;

    /**
     * 租户到期时间
     */
    @Schema(description = "租户到期时间")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    /**
     * 租户最大用户数
     */
    @Schema(description = "租户最大用户数")
    @TableField("max_users")
    private Integer maxUsers;

    /**
     * 租户备注
     */
    @Schema(description = "租户备注")
    @TableField("remark")
    private String remark;

    /**
     * 套餐类型：0=基础版，1=专业版，2=企业版
     */
    @Schema(description = "套餐类型")
    @TableField("create_by")
    private Integer packageType;



    // Getter and Setter methods

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
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
