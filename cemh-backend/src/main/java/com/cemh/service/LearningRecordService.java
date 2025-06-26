package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.LearningRecord;

import java.util.List;
import java.util.Map;

/**
 * 学习记录服务接口
 */
public interface LearningRecordService {
    
    /**
     * 创建或更新学习记录
     * @param learningRecord 学习记录
     * @return 操作结果
     */
    Result<Void> saveOrUpdateRecord(LearningRecord learningRecord);
    
    /**
     * 删除学习记录
     * @param id 记录ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> deleteRecord(Long id, Long userId);
    
    /**
     * 获取学习记录详情
     * @param id 记录ID
     * @param userId 用户ID
     * @return 学习记录详情
     */
    Result<LearningRecord> getRecordById(Long id, Long userId);
    
    /**
     * 获取用户的学习记录
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param status 状态
     * @return 学习记录列表
     */
    Result<PageResult<LearningRecord>> getRecordList(int page, int size, Long userId, Long courseId, Integer status);
    
    /**
     * 更新学习进度
     * @param userId 用户ID
     * @param courseId 课程ID
     * @param progress 进度
     * @return 操作结果
     */
    Result<Void> updateProgress(Long userId, Long courseId, Integer progress);
    
    /**
     * 获取用户学习统计
     * @param userId 用户ID
     * @return 学习统计
     */
    Result<Map<String, Object>> getUserLearningStats(Long userId);
    
    /**
     * 获取课程学习情况统计
     * @param courseId 课程ID
     * @return 学习统计
     */
    Result<Map<String, Object>> getCourseLearnStats(Long courseId);
    
    /**
     * 完成课程学习
     * @param userId 用户ID
     * @param courseId 课程ID
     * @return 操作结果
     */
    Result<Void> completeCourse(Long userId, Long courseId);
} 