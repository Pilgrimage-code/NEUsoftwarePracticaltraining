package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.CourseDTO;
import com.cemh.dto.CourseQueryDTO;
import com.cemh.entity.Course;
import com.cemh.entity.CourseChapter;
import com.cemh.vo.CourseVO;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * 课程服务接口
 */
public interface CourseService {
    
    /**
     * 创建课程
     * @param courseDTO 课程DTO
     * @return 操作结果
     */
    Result<Void> createCourse(CourseDTO courseDTO);
    
    /**
     * 更新课程
     * @param courseDTO 课程DTO
     * @return 操作结果
     */
    Result<Void> updateCourse(CourseDTO courseDTO);
    
    /**
     * 删除课程
     * @param id 课程ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> deleteCourse(Long id, Long tenantId);
    
    /**
     * 批量删除课程
     * @param ids 课程ID列表
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> batchDeleteCourse(List<Long> ids, Long tenantId);
    
    /**
     * 根据ID获取课程
     * @param id 课程ID
     * @param tenantId 租户ID
     * @return 课程详情
     */
    Result<CourseVO> getCourseById(Long id, Long tenantId);
    
    /**
     * 获取课程列表
     * @param queryDTO 查询参数
     * @param tenantId 租户ID
     * @return 课程列表
     */
    Result<PageResult<CourseVO>> getCoursePage(CourseQueryDTO queryDTO, Long tenantId);
    
    /**
     * 获取课程分类列表
     * @param tenantId 租户ID
     * @return 分类列表
     */
    Result<List<Map<String, Object>>> getCourseCategories(Long tenantId);
    
    /**
     * 获取热门课程
     * @param limit 数量限制
     * @return 热门课程列表
     */
    Result<List<CourseVO>> getHotCourses(Integer limit);
    
    /**
     * 获取最新课程
     * @param limit 数量限制
     * @return 最新课程列表
     */
    Result<List<CourseVO>> getLatestCourses(Integer limit);
    
    /**
     * 增加浏览次数
     * @param id 课程ID
     * @return 操作结果
     */
    Result<Void> incrementViewCount(Long id);
    
    /**
     * 课程列表导出
     * @param queryDTO 查询参数
     * @param tenantId 租户ID
     * @return 导出数据
     */
    Result<List<Course>> exportCourseList(CourseQueryDTO queryDTO, Long tenantId);
    
    /**
     * 导出课程数据
     */
    void exportCourses(CourseQueryDTO queryDTO, HttpServletResponse response) throws Exception;
    
    /**
     * 获取课程章节列表
     */
    Result<List<CourseChapter>> getCourseChapters(Long courseId);
    
    /**
     * 获取章节详情
     */
    Result<CourseChapter> getChapterDetail(Long courseId, Long chapterId);
    
    /**
     * 创建课程章节
     */
    Result<Void> createChapter(CourseChapter chapter);
    
    /**
     * 更新课程章节
     */
    Result<Void> updateChapter(CourseChapter chapter);
    
    /**
     * 删除课程章节
     */
    Result<Void> deleteChapter(Long courseId, Long chapterId);
    
    /**
     * 更新学习进度
     */
    Result<Void> updateLearningProgress(Long courseId, Long chapterId, Long userId, Integer progress);
    
    /**
     * 审核课程
     * @param id 课程ID
     * @param status 审核状态（0：通过，2：拒绝）
     * @param reviewComment 审核备注
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> reviewCourse(Long id, Integer status, String reviewComment, Long tenantId, Long userId);
} 