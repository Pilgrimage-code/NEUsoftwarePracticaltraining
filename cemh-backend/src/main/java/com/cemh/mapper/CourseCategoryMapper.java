package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.CourseCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 课程分类Mapper接口
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    
    /**
     * 查询课程分类列表
     * @param tenantId 租户ID
     * @return 分类列表
     */
    List<CourseCategory> selectCategoryList(@Param("tenantId") Long tenantId);
    
    /**
     * 查询分类树
     * @param tenantId 租户ID
     * @return 分类树
     */
    List<Map<String, Object>> selectCategoryTree(@Param("tenantId") Long tenantId);
    
    /**
     * 查询子分类ID列表
     * @param parentId 父分类ID
     * @return 子分类ID列表
     */
    List<Long> selectChildIds(@Param("parentId") Long parentId);
} 