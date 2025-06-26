package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.LearningRecord;
import com.cemh.mapper.LearningRecordMapper;
import com.cemh.service.LearningRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习记录服务实现类
 */
@Service
public class LearningRecordServiceImpl extends ServiceImpl<LearningRecordMapper, LearningRecord> implements LearningRecordService {
    
    private static final Logger logger = LoggerFactory.getLogger(LearningRecordServiceImpl.class);

    @Override
    public Result<Void> saveOrUpdateRecord(LearningRecord learningRecord) {
        try {
            // 查找是否已存在记录
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", learningRecord.getUserId())
                       .eq("course_id", learningRecord.getCourseId())
                       .eq("deleted", 0);
            
            LearningRecord existingRecord = this.getOne(queryWrapper);
            
            if (existingRecord != null) {
                // 更新现有记录
                existingRecord.setProgress(learningRecord.getProgress());
                existingRecord.setUpdateTime(LocalDateTime.now());
                // 更新状态
                if (learningRecord.getProgress() >= 100) {
                    existingRecord.setStatus(2); // 已完成
                    existingRecord.setCompletionTime(LocalDateTime.now());
                } else if (learningRecord.getProgress() > 0) {
                    existingRecord.setStatus(1); // 学习中
                }
                existingRecord.setUpdateTime(LocalDateTime.now());
                this.updateById(existingRecord);
            } else {
                // 创建新记录
                learningRecord.setCreateTime(LocalDateTime.now());
                learningRecord.setUpdateTime(LocalDateTime.now());

                // 设置状态
                if (learningRecord.getProgress() >= 100) {
                    learningRecord.setStatus(2); // 已完成
                    learningRecord.setCompletionTime(LocalDateTime.now());
                } else if (learningRecord.getProgress() > 0) {
                    learningRecord.setStatus(1); // 学习中
                } else {
                    learningRecord.setStatus(0); // 未开始
                }
                learningRecord.setDeleted(0);
                this.save(learningRecord);
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("保存学习记录失败", e);
            return Result.error("保存学习记录失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteRecord(Long id, Long userId) {
        try {
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            if (userId != null) {
                queryWrapper.eq("user_id", userId);
            }
            
            LearningRecord record = this.getOne(queryWrapper);
            if (record == null) {
                return Result.error("学习记录不存在");
            }
            
            // 逻辑删除
            record.setDeleted(1);
            record.setUpdateTime(LocalDateTime.now());
            this.updateById(record);
            
            return Result.success();
        } catch (Exception e) {
            logger.error("删除学习记录失败", e);
            return Result.error("删除学习记录失败: " + e.getMessage());
        }
    }

    @Override
    public Result<LearningRecord> getRecordById(Long id, Long userId) {
        try {
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id)
                       .eq("deleted", 0);
            if (userId != null) {
                queryWrapper.eq("user_id", userId);
            }
            
            LearningRecord record = this.getOne(queryWrapper);
            if (record == null) {
                return Result.error("学习记录不存在");
            }
            
            return Result.success(record);
        } catch (Exception e) {
            logger.error("获取学习记录失败", e);
            return Result.error("获取学习记录失败: " + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<LearningRecord>> getRecordList(int page, int size, Long userId, Long courseId, Integer status) {
        try {
            Page<LearningRecord> pageParam = new Page<>(page, size);
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (userId != null) {
                queryWrapper.eq("user_id", userId);
            }
            if (courseId != null) {
                queryWrapper.eq("course_id", courseId);
            }
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            queryWrapper.orderByDesc("update_time");
            
            IPage<LearningRecord> recordPage = this.page(pageParam, queryWrapper);
            
            PageResult<LearningRecord> result = new PageResult<>();
            result.setRecords(recordPage.getRecords());
            result.setTotal(recordPage.getTotal());
            result.setPages(recordPage.getPages());
            result.setCurrent(recordPage.getCurrent());
            result.setSize(recordPage.getSize());
            
            return Result.success(result);
        } catch (Exception e) {
            logger.error("获取学习记录列表失败", e);
            return Result.error("获取学习记录列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateProgress(Long userId, Long courseId, Integer progress) {
        try {
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("course_id", courseId)
                       .eq("deleted", 0);
            
            LearningRecord record = this.getOne(queryWrapper);
            if (record == null) {
                // 创建新记录
                record = new LearningRecord();
                record.setUserId(userId);
                record.setCourseId(courseId);
                record.setProgress(progress);
                record.setStatus(progress >= 100 ? 1 : 0);
                record.setCreateTime(LocalDateTime.now());
                record.setUpdateTime(LocalDateTime.now());
                record.setDeleted(0);
                if (progress >= 100) {
                    record.setCompletionTime(LocalDateTime.now());
                }
                this.save(record);
            } else {
                // 更新进度
                record.setProgress(progress);
                record.setUpdateTime(LocalDateTime.now());
                // 更新状态
                if (progress >= 100 && record.getStatus() != 2) {
                    record.setStatus(2); // 已完成
                    record.setCompletionTime(LocalDateTime.now());
                } else if (progress > 0 && record.getStatus() == 0) {
                    record.setStatus(1); // 学习中
                }
                this.updateById(record);
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("更新学习进度失败", e);
            return Result.error("更新学习进度失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getUserLearningStats(Long userId) {
        try {
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("deleted", 0);
            
            List<LearningRecord> records = this.list(queryWrapper);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCourses", records.size());
            stats.put("completedCourses", records.stream().mapToInt(r -> r.getStatus() == 1 ? 1 : 0).sum());
            stats.put("inProgressCourses", records.stream().mapToInt(r -> r.getStatus() == 0 ? 1 : 0).sum());
            
            double completionRate = records.isEmpty() ? 0.0 : 
                (double) records.stream().mapToInt(r -> r.getStatus() == 1 ? 1 : 0).sum() / records.size() * 100;
            stats.put("completionRate", completionRate);
            
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取用户学习统计失败", e);
            return Result.error("获取用户学习统计失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Map<String, Object>> getCourseLearnStats(Long courseId) {
        try {
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId)
                       .eq("deleted", 0);
            
            List<LearningRecord> records = this.list(queryWrapper);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalLearners", records.size());
            stats.put("completedLearners", records.stream().mapToInt(r -> r.getStatus() == 1 ? 1 : 0).sum());
            
            double averageProgress = records.isEmpty() ? 0.0 : 
                records.stream().mapToInt(LearningRecord::getProgress).average().orElse(0.0);
            stats.put("averageProgress", averageProgress);
            
            double completionRate = records.isEmpty() ? 0.0 : 
                (double) records.stream().mapToInt(r -> r.getStatus() == 1 ? 1 : 0).sum() / records.size() * 100;
            stats.put("completionRate", completionRate);
            
            return Result.success(stats);
        } catch (Exception e) {
            logger.error("获取课程学习统计失败", e);
            return Result.error("获取课程学习统计失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> completeCourse(Long userId, Long courseId) {
        try {
            QueryWrapper<LearningRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("course_id", courseId)
                       .eq("deleted", 0);
            
            LearningRecord record = this.getOne(queryWrapper);
            if (record == null) {
                // 创建完成记录
                record = new LearningRecord();
                record.setUserId(userId);
                record.setCourseId(courseId);
                record.setProgress(100);
                record.setStatus(1);
                record.setCompletionTime(LocalDateTime.now());
                record.setCreateTime(LocalDateTime.now());
                record.setUpdateTime(LocalDateTime.now());
                record.setDeleted(0);
                this.save(record);
            } else {
                // 标记为完成
                record.setProgress(100);
                record.setStatus(1);
                record.setCompletionTime(LocalDateTime.now());
                record.setUpdateTime(LocalDateTime.now());
                this.updateById(record);
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("完成课程学习失败", e);
            return Result.error("完成课程学习失败: " + e.getMessage());
        }
    }
} 