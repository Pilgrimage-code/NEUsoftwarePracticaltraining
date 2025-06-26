package com.cemh.dto;

import com.cemh.entity.CourseChapter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 课程数据传输对象
 */
public class CourseDTO {
    
    /**
     * 课程ID（编辑时使用）
     */
    private Long id;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 课程编码
     */
    private String courseCode;
    
    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 200, message = "课程名称长度不能超过200个字符")
    private String courseName;
    
    /**
     * 课程描述
     */
    private String description;
    
    /**
     * 课程封面
     */
    private String coverImage;
    
    /**
     * 讲师
     */
    private String instructor;
    
    /**
     * 课程时长（分钟）
     */
    private Integer duration;
    
    /**
     * 难度等级（1=初级，2=中级，3=高级）
     */
    private Integer difficulty;
    
    /**
     * 课程价格
     */
    private BigDecimal price;
    
    /**
     * 最大学员数
     */
    private Integer maxStudents;
    
    /**
     * 是否免费（0=收费，1=免费）
     */
    private Integer isFree;
    
    /**
     * 是否热门（0=否，1=是）
     */
    private Integer isHot;
    
    /**
     * 课程状态（0：草稿，1：已发布，2：已下线）
     */
    private Integer status;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 课程章节列表
     */
    private List<CourseChapter> chapters;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 课程简介
     */
    @NotBlank(message = "课程简介不能为空")
    private String courseIntro;
    
    /**
     * 课程作者
     */
    @NotBlank(message = "课程作者不能为空")
    @Size(max = 100, message = "课程作者长度不能超过100个字符")
    private String courseAuthor;
    
    /**
     * 课程排序
     */
    @javax.validation.constraints.NotNull(message = "课程排序不能为空")
    private Integer courseOrder;
    
    /**
     * 课程视频URL
     */
    private String videoUrl;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<CourseChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<CourseChapter> chapters) {
        this.chapters = chapters;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
} 