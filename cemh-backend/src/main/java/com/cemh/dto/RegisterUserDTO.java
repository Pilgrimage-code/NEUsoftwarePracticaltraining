package com.cemh.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegisterUserDTO {
    @Schema(description = "所属企业ID")
    private Long tenantId;
    @Schema(description = "所属部门ID")
    private Long deptId;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "头像URL")
    private String avatar;
    @Schema(description = "验证码")
    private String captcha;
    @Schema(description = "验证码key")
    private String captchaKey;

    // getter/setter
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getCaptcha() { return captcha; }
    public void setCaptcha(String captcha) { this.captcha = captcha; }
    public String getCaptchaKey() { return captchaKey; }
    public void setCaptchaKey(String captchaKey) { this.captchaKey = captchaKey; }
} 