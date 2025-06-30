package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 会议实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Schema(description = "会议信息")
@TableName("meeting")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meeting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会议ID")
    private Long id;

    @Schema(description = "会议标题")
    @TableField("title")
    @NotBlank(message = "会议标题不能为空")
    @Size(max = 200, message = "会议标题长度不能超过200个字符")
    private String title;

    @Schema(description = "会议备注")
    @TableField("remark")
    @Size(max = 2000, message = "会议备注长度不能超过2000个字符")
    private String remark;

    @Schema(description = "会议类型（1：线上会议，2：线下会议，3：混合会议）")
    @TableField("type")
    @NotNull(message = "会议类型不能为空")
    private Integer type;

    @Schema(description = "会议详情")
    @TableField("description")
    private String description;

    @Schema(description = "会议地点")
    @TableField("location")
    private String location;

    @Schema(description = "会议开始时间")
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-d'T'HH:mm:ss.SSSX")
    @NotNull(message = "会议开始时间不能为空")
    private OffsetDateTime startTime;

    @Schema(description = "会议结束时间")
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-d'T'HH:mm:ss.SSSX")
    @NotNull(message = "会议结束时间不能为空")
    private OffsetDateTime endTime;


    @Schema(description = "报名截至时间")
    @TableField("registration_deadline")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime registrationDeadline;

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
    @TableField("requires_approval")
    private Integer requiresApproval;

    @Schema(description = "会议封面图")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "主办方")
    @TableField("organizer")
    private String organizer;

    @Schema(description = "会议链接（线上会议）")
    @TableField("meeting_link")
    private String meetingLink;

    @Schema(description = "会议密码（线上会议）")
    @TableField("meeting_password")
    private String meetingPassword;

    @Schema(description = "是否置顶（0：否，1：是）")
    @TableField("is_top")
    private Integer isTop;

    @Schema(description = "会议标签")
    @TableField("tags")
    private String tags;

    @Schema(description = "租户id")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "参会要求")
    @TableField("requirements")
    private String requirements;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public OffsetDateTime getStartTime() { return startTime; }
    public void setStartTime(OffsetDateTime startTime) { this.startTime = startTime; }
    public OffsetDateTime getEndTime() { return endTime; }
    public void setEndTime(OffsetDateTime endTime) { this.endTime = endTime; }
    public OffsetDateTime getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(OffsetDateTime registrationDeadline) { this.registrationDeadline = registrationDeadline; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(Integer requiresApproval) { this.requiresApproval = requiresApproval; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getMeetingPassword() { return meetingPassword; }
    public void setMeetingPassword(String meetingPassword) { this.meetingPassword = meetingPassword; }
    public Integer getIsTop() { return isTop; }
    public void setIsTop(Integer isTop) { this.isTop = isTop; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
}

