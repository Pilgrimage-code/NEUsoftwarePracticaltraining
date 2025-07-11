package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 课程章节实体类
 */
@Schema(description = "课程章节信息")
@TableName("course_chapter")
public class CourseChapter extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "课程ID")
    @TableField("course_id")
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @Schema(description = "章节名称")
    @TableField("chapter_name")
    @NotBlank(message = "章节名称不能为空")
    @Size(max = 200, message = "章节名称长度不能超过200个字符")
    private String chapterName;

    @Schema(description = "章节排序")
    @TableField("chapter_order")
    private Integer chapterOrder;

    @Schema(description = "章节描述")
    @TableField("description")
    private String description;

    @Schema(description = "视频URL")
    @TableField("video_url")
    private String videoUrl;

    @Schema(description = "视频时长（秒）")
    @TableField("duration")
    private Integer duration;
    
    @Schema(description = "状态（0：禁用，1：启用）")
    @TableField("status")
    private Integer status;
    
    @Schema(description = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    // 非数据库字段
    @Schema(description = "课程名称")
    @TableField(exist = false)
    private String courseName;

    public CourseChapter() {
        this.chapterOrder = 0; // 默认排序为0
        this.duration = 0; // 默认时长为0
        this.status = 1; // 默认启用状态
    }

    // Getter and Setter methods
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(Integer chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "CourseChapter{" +
                "courseId=" + courseId +
                ", chapterName='" + chapterName + '\'' +
                ", chapterOrder=" + chapterOrder +
                ", description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", duration=" + duration +
                ", status=" + status +
                ", tenantId=" + tenantId +
                ", courseName='" + courseName + '\'' +
                "} " + super.toString();
    }
} 