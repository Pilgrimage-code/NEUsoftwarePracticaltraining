package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 学习记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("learning_record")
@Schema(description = "学习记录")
public class LearningRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "课程ID")
    @TableField("course_id")
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @Schema(description = "学习进度(0-100)")
    @TableField("progress")
    private Integer progress;

    @Schema(description = "状态(0:未开始,1:学习中,2:已完成,3:已暂停)")
    @TableField("status")
    private Integer status;

    @Schema(description = "学习时长(分钟)")
    @TableField("study_duration")
    private Integer studyDuration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "完成时间")
    @TableField("completion_time")
    private LocalDateTime completionTime;

    @Schema(description = "最后学习的章节ID")
    @TableField("last_chapter_id")
    private Long lastChapterId;

    @Schema(description = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    // 非数据库字段
    @TableField(exist = false)
    @Schema(description = "课程名称")
    private String courseName;

    @TableField(exist = false)
    @Schema(description = "课程封面")
    private String courseImage;

    @TableField(exist = false)
    @Schema(description = "讲师")
    private String instructor;

    public LearningRecord() {
        this.progress = 0; // 默认进度为0
        this.status = 0; // 默认未完成状态
    }

    // Getter and Setter methods
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLastChapterId() {
        return lastChapterId;
    }

    public void setLastChapterId(Long lastChapterId) {
        this.lastChapterId = lastChapterId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(String courseImage) {
        this.courseImage = courseImage;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }

    @Override
    public String toString() {
        return "LearningRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", progress=" + progress +
                ", status=" + status +
                ", studyDuration=" + studyDuration +
                ", completionTime=" + completionTime +
                ", lastChapterId=" + lastChapterId +
                ", tenantId=" + tenantId +
                ", courseName='" + courseName + '\'' +
                ", courseImage='" + courseImage + '\'' +
                ", instructor='" + instructor + '\'' +
                "} " + super.toString();
    }
} 