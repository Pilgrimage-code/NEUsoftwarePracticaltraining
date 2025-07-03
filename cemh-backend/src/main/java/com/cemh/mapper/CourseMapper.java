package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 课程Mapper接口
 */
public interface CourseMapper extends BaseMapper<Course> {
    
    /**
     * 分页查询课程列表
     * @param page 分页参数
     * @param tenantId 租户ID
     * @param courseName 课程名称
     * @param courseOrder 课程排序
     * @param courseAuthor 课程作者
     * @param status 课程状态
     * @param categoryId 分类ID
     * @param remark 备注（用于审核状态）
     * @return 分页结果
     */
    IPage<Course> selectCoursePage(Page<Course> page, 
                                    @Param("tenantId") Long tenantId, 
                                    @Param("courseName") String courseName, 
                                    @Param("courseOrder") Integer courseOrder,
                                    @Param("courseAuthor") String courseAuthor,
                                    @Param("status") Integer status,
                                    @Param("categoryId") Long categoryId,
                                    @Param("remark") String remark);
    
    /**
     * 获取课程详情
     * @param id 课程ID
     * @param tenantId 租户ID
     * @return 课程详情
     */
    Course selectCourseDetail(@Param("id") Long id, @Param("tenantId") Long tenantId);
    
    /**
     * 查询热门课程
     * @param limit 数量限制
     * @return 热门课程列表
     */
    List<Course> selectHotCourses(@Param("limit") Long limit);
    
    /**
     * 查询最新课程
     * @param limit 数量限制
     * @return 最新课程列表
     */
    List<Course> selectLatestCourses(@Param("limit") Long limit);
    
    /**
     * 查询课程统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectCourseStatistics(@Param("tenantId") Long tenantId);
    
    /**
     * 增加课程浏览次数
     * @param id 课程ID
     * @return 影响行数
     */
    int incrementViewCount(@Param("id") Long id);
    
    /**
     * 导出课程列表
     * @param tenantId 租户ID
     * @param courseName 课程名称
     * @param courseOrder 课程排序
     * @return 课程列表
     */
    List<Course> selectCourseExport(@Param("tenantId") Long tenantId,
                                     @Param("courseName") String courseName,
                                     @Param("courseOrder") Integer courseOrder);
                                     
    /**
     * 获取当前最大的课程ID
     * @return 最大ID
     */
    Long selectMaxId();
}