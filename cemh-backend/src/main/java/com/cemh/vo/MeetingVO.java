package com.cemh.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 会议视图对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingVO {
    
    /**
     * 会议ID
     */
    private Long id;
    
    /**
     * 会议标题
     */
    private String title;
    
    /**
     * 会议描述
     */
    private String description;
    
    /**
     * 会议类型：online-线上，offline-线下，hybrid-混合
     */
    private Integer type;
    
    /**
     * 会议类型文本
     */
    private String typeText;
    
    /**
     * 会议状态：draft-草稿，published-已发布，ongoing-进行中，completed-已结束，cancelled-已取消
     */
    private Integer status;
    
    /**
     * 会议状态文本
     */
    private String statusText;
    
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 会议地点
     */
    private String location;
    
    /**
     * 最大参与人数
     */
    private Integer maxParticipants;
    
    /**
     * 当前报名人数
     */
    private Integer currentParticipants;
    
    /**
     * 报名截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDeadline;
    
    /**
     * 是否需要审核
     */
    private Boolean requiresApproval;
    
    /**
     * 参会费用
     */
    private Double fee;
    
    /**
     * 参会要求
     */
    private String requirements;
    
    /**
     * 会议标签
     */
    private String tags;
    
    /**
     * 会议封面图片
     */
    private String coverImage;
    
    /**
     * 备注信息
     */
    private String remarks;
    
    /**
     * 创建人ID
     */
    private Long createBy;
    
    /**
     * 创建人姓名
     */
    private String createByName;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 是否可以报名
     */
    private Boolean canRegister;
    
    /**
     * 是否已报名
     */
    private Boolean hasRegistered;
    
    /**
     * 会议时长（分钟）
     */
    private Long duration;
    
    /**
     * 距离开始时间（分钟）
     */
    private Long timeToStart;

    /**
     * 是否顶置
     */
    private Integer isTop;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getTypeText() { return typeText; }
    public void setTypeText(String typeText) { this.typeText = typeText; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
    public LocalDateTime getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(LocalDateTime registrationDeadline) { this.registrationDeadline = registrationDeadline; }
    public Boolean getRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(Boolean requiresApproval) { this.requiresApproval = requiresApproval; }
    public Double getFee() { return fee; }
    public void setFee(Double fee) { this.fee = fee; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Long getCreateBy() { return createBy; }
    public void setCreateBy(Long createBy) { this.createBy = createBy; }
    public String getCreateByName() { return createByName; }
    public void setCreateByName(String createByName) { this.createByName = createByName; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public Boolean getCanRegister() { return canRegister; }
    public void setCanRegister(Boolean canRegister) { this.canRegister = canRegister; }
    public Boolean getHasRegistered() { return hasRegistered; }
    public void setHasRegistered(Boolean hasRegistered) { this.hasRegistered = hasRegistered; }
    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }
    public Long getTimeToStart() { return timeToStart; }
    public void setTimeToStart(Long timeToStart) { this.timeToStart = timeToStart; }
    public Integer getIsTop() { return isTop; }
    public void setIsTop(Integer isTop) { this.isTop = isTop; }
}

