package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 课程分类实体类
 */
@Schema(description = "课程分类信息")
@TableName("course_category")
public class CourseCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "租户ID")
    @TableField("tenant_id")
    private Long tenantId;

    @Schema(description = "父分类ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "分类名称")
    @TableField("category_name")
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String categoryName;

    @Schema(description = "分类编码")
    @TableField("category_code")
    @NotBlank(message = "分类编码不能为空")
    @Size(max = 50, message = "分类编码长度不能超过50个字符")
    private String categoryCode;

    @Schema(description = "分类描述")
    @TableField("description")
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;

    @Schema(description = "显示顺序")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "状态（0：禁用，1：启用）")
    @TableField("status")
    private Integer status;

    // 非数据库字段
    @Schema(description = "父分类名称")
    @TableField(exist = false)
    private String parentName;

    public CourseCategory() {
        this.parentId = 0L; // 默认为顶级分类
        this.status = 1; // 默认启用状态
        this.sortOrder = 0; // 默认排序为0
    }

    // Getter and Setter methods
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return "CourseCategory{" +
                "tenantId=" + tenantId +
                ", parentId=" + parentId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", description='" + description + '\'' +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", parentName='" + parentName + '\'' +
                "} " + super.toString();
    }
} 