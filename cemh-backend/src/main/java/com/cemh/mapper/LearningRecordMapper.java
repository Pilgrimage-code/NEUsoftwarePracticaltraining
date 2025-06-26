package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.entity.LearningRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学习记录Mapper接口
 */
public interface LearningRecordMapper extends BaseMapper<LearningRecord> {
    
    /**
     * 分页查询学习记录列表
     * @param page 分页参数
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param status 学习状态
     * @return 分页结果
     */
    IPage<LearningRecord> selectRecordPage(Page<LearningRecord> page,
                                           @Param("userId") Long userId,
                                           @Param("courseId") Long courseId,
                                           @Param("status") Integer status);
    
    /**
     * 查询用户学习的课程列表
     * @param userId 用户ID
     * @return 课程列表
     */
    List<Map<String, Object>> selectUserLearningCourses(@Param("userId") Long userId);
    
    /**
     * 查询课程完成率统计
     * @param courseId 课程ID
     * @return 完成率统计
     */
    Map<String, Object> selectCourseCompletionRate(@Param("courseId") Long courseId);
    
    /**
     * 批量删除课程学习记录
     * @param courseIds 课程ID列表
     * @return 影响行数
     */
    int batchDeleteByCourseIds(@Param("courseIds") List<Long> courseIds);
} 