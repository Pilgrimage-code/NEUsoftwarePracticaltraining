package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 资讯实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Schema(description = "资讯信息")
@TableName("news")
public class News extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "分类ID")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "资讯标题")
    @TableField("title")
    @NotBlank(message = "资讯标题不能为空")
    @Size(max = 200, message = "资讯标题长度不能超过200个字符")
    private String title;

    @Schema(description = "资讯摘要")
    @TableField("summary")
    @Size(max = 500, message = "资讯摘要长度不能超过500个字符")
    private String summary;

    @Schema(description = "资讯内容")
    @TableField("content")
    @NotBlank(message = "资讯内容不能为空")
    private String content;

    @Schema(description = "封面图片")
    @TableField("cover_image")
    private String coverImage;

    @Schema(description = "作者")
    @TableField("author")
    private String author;

    @Schema(description = "来源")
    @TableField("source")
    private String source;

    @Schema(description = "标签，多个用逗号分隔")
    @TableField("tags")
    private String tags;

    @Schema(description = "浏览次数")
    @TableField("view_count")
    private Integer viewCount;

    @Schema(description = "点赞次数")
    @TableField("like_count")
    private Integer likeCount;

    @Schema(description = "评论次数")
    @TableField("comment_count")
    private Integer commentCount;

    @Schema(description = "是否置顶（0：否，1：是）")
    @TableField("is_top")
    private Integer isTop;

    @Schema(description = "是否热门（0：否，1：是）")
    @TableField("is_hot")
    private Integer isHot;

    @Schema(description = "资讯状态（0：草稿，1：已发布，2：已下线）")
    @TableField("status")
    private Integer status;

    @Schema(description = "发布时间")
    @TableField("publish_time")
    private LocalDateTime publishTime;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    // 非数据库字段
    @Schema(description = "分类名称")
    @TableField(exist = false)
    private String categoryName;

    public News() {
        this.status = 0; // 默认草稿状态
        this.isTop = 0; // 默认不置顶
        this.isHot = 0; // 默认不热门
        this.viewCount = 0; // 默认浏览次数为0
        this.likeCount = 0; // 默认点赞次数为0
        this.commentCount = 0; // 默认评论次数为0
    }

    // Getter and Setter methods
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
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

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "News{" +
                "tenantId=" + tenantId +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", tags='" + tags + '\'' +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", isTop=" + isTop +
                ", isHot=" + isHot +
                ", status=" + status +
                ", publishTime=" + publishTime +
                ", remark='" + remark + '\'' +
                ", categoryName='" + categoryName + '\'' +
                "} " + super.toString();
    }
}

