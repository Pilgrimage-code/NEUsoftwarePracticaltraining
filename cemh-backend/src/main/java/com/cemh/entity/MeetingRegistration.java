package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 会议报名实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Schema(description = "会议报名信息")
@TableName("meeting_registration")
public class MeetingRegistration extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会议ID")
    @TableField("meeting_id")
    @NotNull(message = "会议ID不能为空")
    private Long meetingId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "报名人姓名")
    @TableField("participant_name")
    @NotBlank(message = "报名人姓名不能为空")
    @Size(max = 50, message = "报名人姓名长度不能超过50个字符")
    private String participantName;

    @Schema(description = "报名人手机号")
    @TableField("participant_phone")
    @NotBlank(message = "报名人手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String participantPhone;

    @Schema(description = "报名人邮箱")
    @TableField("participant_email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String participantEmail;

    @Schema(description = "报名人身份证号")
    @TableField("participant_id_card")
    private String participantIdCard;

    @Schema(description = "报名人单位")
    @TableField("participant_company")
    private String participantCompany;

    @Schema(description = "报名人职位")
    @TableField("participant_position")
    private String participantPosition;

    @Schema(description = "报名状态（0：待审核，1：已通过，2：已拒绝，3：已取消）")
    @TableField("status")
    private Integer status;

    @Schema(description = "审核意见")
    @TableField("approval_comment")
    private String approvalComment;

    @Schema(description = "审核人ID")
    @TableField("approval_by")
    private Long approvalBy;

    @Schema(description = "审核时间")
    @TableField("approval_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTime;

    @Schema(description = "签到状态（0：未签到，1：已签到）")
    @TableField("check_in_status")
    private Integer checkInStatus;

    @Schema(description = "签到时间")
    @TableField("check_in_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInTime;

    @Schema(description = "特殊需求")
    @TableField("special_requirements")
    private String specialRequirements;

    // 非数据库字段
    @Schema(description = "会议标题")
    @TableField(exist = false)
    private String meetingTitle;

    @Schema(description = "审核人姓名")
    @TableField(exist = false)
    private String approvalByName;

    public MeetingRegistration() {
        this.status = 0; // 默认待审核状态
        this.checkInStatus = 0; // 默认未签到
    }

    // Getter and Setter methods
    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantPhone() {
        return participantPhone;
    }

    public void setParticipantPhone(String participantPhone) {
        this.participantPhone = participantPhone;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getParticipantIdCard() {
        return participantIdCard;
    }

    public void setParticipantIdCard(String participantIdCard) {
        this.participantIdCard = participantIdCard;
    }

    public String getParticipantCompany() {
        return participantCompany;
    }

    public void setParticipantCompany(String participantCompany) {
        this.participantCompany = participantCompany;
    }

    public String getParticipantPosition() {
        return participantPosition;
    }

    public void setParticipantPosition(String participantPosition) {
        this.participantPosition = participantPosition;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public Long getApprovalBy() {
        return approvalBy;
    }

    public void setApprovalBy(Long approvalBy) {
        this.approvalBy = approvalBy;
    }

    public LocalDateTime getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(LocalDateTime approvalTime) {
        this.approvalTime = approvalTime;
    }

    public Integer getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(Integer checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getApprovalByName() {
        return approvalByName;
    }

    public void setApprovalByName(String approvalByName) {
        this.approvalByName = approvalByName;
    }

    @Override
    public String toString() {
        return "MeetingRegistration{" +
                "meetingId=" + meetingId +
                ", userId=" + userId +
                ", participantName='" + participantName + '\'' +
                ", participantPhone='" + participantPhone + '\'' +
                ", participantEmail='" + participantEmail + '\'' +
                ", participantIdCard='" + participantIdCard + '\'' +
                ", participantCompany='" + participantCompany + '\'' +
                ", participantPosition='" + participantPosition + '\'' +
                ", status=" + status +
                ", approvalComment='" + approvalComment + '\'' +
                ", approvalBy=" + approvalBy +
                ", approvalTime=" + approvalTime +
                ", checkInStatus=" + checkInStatus +
                ", checkInTime=" + checkInTime +
                ", specialRequirements='" + specialRequirements + '\'' +
                ", meetingTitle='" + meetingTitle + '\'' +
                ", approvalByName='" + approvalByName + '\'' +
                "} " + super.toString();
    }
}

