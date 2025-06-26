package com.cemh.dto;

/**
 * 课程查询数据传输对象
 */
public class CourseQueryDTO {
    
    /**
     * 课程名称
     */
    private String courseName;
    
    /**
     * 课程排序
     */
    private Integer courseOrder;
    
    /**
     * 课程作者
     */
    private String courseAuthor;
    
    /**
     * 课程状态（0：禁用，1：启用）
     */
    private Integer status;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    // Getter and Setter methods
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseOrder() {
        return courseOrder;
    }

    public void setCourseOrder(Integer courseOrder) {
        this.courseOrder = courseOrder;
    }

    public String getCourseAuthor() {
        return courseAuthor;
    }

    public void setCourseAuthor(String courseAuthor) {
        this.courseAuthor = courseAuthor;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page != null && page > 0) {
            this.page = page;
        }
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        if (size != null && size > 0) {
            this.size = size;
        }
    }
} 