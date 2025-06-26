package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 资讯分类实体类
 */
@TableName("news_category")
@Schema(description = "资讯分类信息")
public class NewsCategory {

    @Schema(description = "分类ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "分类名称")
    @TableField("category_name")
    private String categoryName;

    @Schema(description = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "父分类ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "排序")
    @TableField("sort_order")
    private Integer sortOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}


