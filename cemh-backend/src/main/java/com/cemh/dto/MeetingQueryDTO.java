package com.cemh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 会议查询数据传输对象
 */
public class MeetingQueryDTO {
    
    /**
     * 会议标题（模糊查询）
     */
    private String title;
    
    /**
     * 会议类型：online-线上，offline-线下，hybrid-混合
     */
    private String type;
    
    /**
     * 会议标签：tech-技术分享，product-产品讨论，training-培训学习，review-项目评审，team-团队建设
     */
    private String tags;
    
    /**
     * 会议状态：draft-草稿，published-已发布，ongoing-进行中，completed-已结束，cancelled-已取消
     */
    private String status;
    
    /**
     * 创建人ID
     */
    private Long createBy;
    
    /**
     * 开始时间范围 - 开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeBegin;
    
    /**
     * 开始时间范围 - 结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTimeEnd;
    
    /**
     * 创建时间范围 - 开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeBegin;
    
    /**
     * 创建时间范围 - 结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 当前页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    /**
     * 排序字段
     */
    private String sortField = "createTime";
    
    /**
     * 排序方向：asc-升序，desc-降序
     */
    private String sortOrder = "desc";

    // Getter and Setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getStartTimeBegin() {
        return startTimeBegin;
    }

    public void setStartTimeBegin(LocalDateTime startTimeBegin) {
        this.startTimeBegin = startTimeBegin;
    }

    public LocalDateTime getStartTimeEnd() {
        return startTimeEnd;
    }

    public void setStartTimeEnd(LocalDateTime startTimeEnd) {
        this.startTimeEnd = startTimeEnd;
    }

    public LocalDateTime getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(LocalDateTime createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public LocalDateTime getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(LocalDateTime createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}

