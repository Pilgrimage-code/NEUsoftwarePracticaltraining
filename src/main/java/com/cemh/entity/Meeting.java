package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会议实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Schema(description = "会议信息")
@TableName("meeting")
public class Meeting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会议标题")
    @TableField("title")
    @NotBlank(message = "会议标题不能为空")
    @Size(max = 200, message = "会议标题长度不能超过200个字符")
    private String title;

    @Schema(description = "会议描述")
    @TableField("description")
    @Size(max = 2000, message = "会议描述长度不能超过2000个字符")
    private String description;

    @Schema(description = "会议类型（1：线上会议，2：线下会议，3：混合会议）")
    @TableField("meeting_type")
    @NotNull(message = "会议类型不能为空")
    private Integer meetingType;

    @Schema(description = "会议地点")
    @TableField("location")
    private String location;

    @Schema(description = "会议开始时间")
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "会议开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "会议结束时间")
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "会议结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "报名开始时间")
    @TableField("registration_start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationStartTime;

    @Schema(description = "报名结束时间")
    @TableField("registration_end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationEndTime;

    @Schema(description = "最大参会人数")
    @TableField("max_participants")
    private Integer maxParticipants;

    @Schema(description = "当前报名人数")
    @TableField("current_participants")
    private Integer currentParticipants;

    @Schema(description = "会议费用")
    @TableField("fee")
    private BigDecimal fee;

    @Schema(description = "会议状态（0：草稿，1：已发布，2：报名中，3：报名结束，4：进行中，5：已结束，6：已取消）")
    @TableField("status")
    private Integer status;

    @Schema(description = "是否需要审核（0：不需要，1：需要）")
    @TableField("need_approval")
    private Integer needApproval;

    @Schema(description = "会议封面图")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "会议详情")
    @TableField("content")
    private String content;

    @Schema(description = "主办方")
    @TableField("organizer")
    private String organizer;

    @Schema(description = "联系人")
    @TableField("contact_person")
    private String contactPerson;

    @Schema(description = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    @Schema(description = "联系邮箱")
    @TableField("contact_email")
    private String contactEmail;

    @Schema(description = "会议链接（线上会议）")
    @TableField("meeting_link")
    private String meetingLink;

    @Schema(description = "会议密码（线上会议）")
    @TableField("meeting_password")
    private String meetingPassword;

    @Schema(description = "是否置顶（0：否，1：是）")
    @TableField("is_top")
    private Integer isTop;

    @Schema(description = "浏览次数")
    @TableField("view_count")
    private Integer viewCount;

    public Meeting() {
        this.meetingType = 2; // 默认线下会议
        this.status = 0; // 默认草稿状态
        this.needApproval = 0; // 默认不需要审核
        this.currentParticipants = 0; // 默认当前报名人数为0
        this.isTop = 0; // 默认不置顶
        this.viewCount = 0; // 默认浏览次数为0
        this.fee = BigDecimal.ZERO; // 默认免费
    }

    // Getter and Setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(Integer meetingType) {
        this.meetingType = meetingType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getRegistrationStartTime() {
        return registrationStartTime;
    }

    public void setRegistrationStartTime(LocalDateTime registrationStartTime) {
        this.registrationStartTime = registrationStartTime;
    }

    public LocalDateTime getRegistrationEndTime() {
        return registrationEndTime;
    }

    public void setRegistrationEndTime(LocalDateTime registrationEndTime) {
        this.registrationEndTime = registrationEndTime;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNeedApproval() {
        return needApproval;
    }

    public void setNeedApproval(Integer needApproval) {
        this.needApproval = needApproval;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getMeetingPassword() {
        return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", meetingType=" + meetingType +
                ", location='" + location + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", registrationStartTime=" + registrationStartTime +
                ", registrationEndTime=" + registrationEndTime +
                ", maxParticipants=" + maxParticipants +
                ", currentParticipants=" + currentParticipants +
                ", fee=" + fee +
                ", status=" + status +
                ", needApproval=" + needApproval +
                ", coverImage='" + coverImage + '\'' +
                ", organizer='" + organizer + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", meetingLink='" + meetingLink + '\'' +
                ", isTop=" + isTop +
                ", viewCount=" + viewCount +
                "} " + super.toString();
    }
}

