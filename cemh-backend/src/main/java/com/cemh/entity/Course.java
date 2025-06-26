package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 课程实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-07-10
 */
@Schema(description = "课程信息")
@TableName("course")
public class Course extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "分类ID")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "课程编码")
    @TableField("course_code")
    private String courseCode;

    @Schema(description = "课程名称")
    @TableField("course_name")
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 200, message = "课程名称长度不能超过200个字符")
    private String courseName;

    @Schema(description = "课程简介")
    @TableField("course_intro")
    @NotBlank(message = "课程简介不能为空")
    private String courseIntro;

    @Schema(description = "课程作者")
    @TableField("course_author")
    @NotBlank(message = "课程作者不能为空")
    @Size(max = 100, message = "课程作者长度不能超过100个字符")
    private String courseAuthor;

    @Schema(description = "课程排序")
    @TableField("course_order")
    @NotNull(message = "课程排序不能为空")
    private Integer courseOrder;

    @Schema(description = "课程描述")
    @TableField("description")
    private String description;

    @Schema(description = "课程封面")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "课程视频URL")
    @TableField("video_url")
    private String videoUrl;

    @Schema(description = "讲师")
    @TableField("instructor")
    private String instructor;

    @Schema(description = "课程时长（分钟）")
    @TableField("duration")
    private Integer duration;

    @Schema(description = "难度等级（1=初级，2=中级，3=高级）")
    @TableField("difficulty")
    private Integer difficulty;

    @Schema(description = "课程价格")
    @TableField("price")
    private java.math.BigDecimal price;

    @Schema(description = "最大学员数")
    @TableField("max_students")
    private Integer maxStudents;

    @Schema(description = "当前学员数")
    @TableField("current_students")
    private Integer currentStudents;

    @Schema(description = "课程浏览次数")
    @TableField("view_count")
    private Integer viewCount;

    @Schema(description = "评分")
    @TableField("rating")
    private java.math.BigDecimal rating;

    @Schema(description = "评分人数")
    @TableField("rating_count")
    private Integer ratingCount;

    @Schema(description = "课程状态（0：草稿，1：已发布，2：已下线）")
    @TableField("status")
    private Integer status;

    @Schema(description = "是否免费（0=收费，1=免费）")
    @TableField("is_free")
    private Integer isFree;

    @Schema(description = "是否热门（0=否，1=是）")
    @TableField("is_hot")
    private Integer isHot;

    // 非数据库字段
    @Schema(description = "分类名称")
    @TableField(exist = false)
    private String categoryName;

    public Course() {
        this.status = 1; // 默认已发布状态
        this.duration = 0; // 默认时长为0
        this.viewCount = 0; // 默认浏览次数为0
        this.difficulty = 1; // 默认初级
        this.isFree = 1; // 默认免费
        this.isHot = 0; // 默认非热门
        this.price = java.math.BigDecimal.ZERO; // 默认价格为0
        this.maxStudents = 0; // 默认不限制学员数
        this.currentStudents = 0; // 默认当前学员数为0
        this.rating = java.math.BigDecimal.ZERO; // 默认评分为0
        this.ratingCount = 0; // 默认评分人数为0
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseIntro() {
        return courseIntro;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public String getCourseAuthor() {
        return courseAuthor;
    }

    public void setCourseAuthor(String courseAuthor) {
        this.courseAuthor = courseAuthor;
    }

    public Integer getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(Integer courseOrder) {
        this.courseOrder = courseOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(Integer currentStudents) {
        this.currentStudents = currentStudents;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public java.math.BigDecimal getRating() {
        return rating;
    }

    public void setRating(java.math.BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "tenantId=" + tenantId +
                ", categoryId=" + categoryId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseIntro='" + courseIntro + '\'' +
                ", courseAuthor='" + courseAuthor + '\'' +
                ", courseOrder=" + courseOrder +
                ", description='" + description + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", instructor='" + instructor + '\'' +
                ", duration=" + duration +
                ", difficulty=" + difficulty +
                ", price=" + price +
                ", maxStudents=" + maxStudents +
                ", currentStudents=" + currentStudents +
                ", viewCount=" + viewCount +
                ", rating=" + rating +
                ", ratingCount=" + ratingCount +
                ", status=" + status +
                ", isFree=" + isFree +
                ", isHot=" + isHot +
                ", categoryName='" + categoryName + '\'' +
                "} " + super.toString();
    }
} 