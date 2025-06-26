package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.Course;
import com.cemh.entity.LearningRecord;
import com.cemh.mapper.CourseMapper;
import com.cemh.service.LearningRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 学习记录控制器
 */
@Tag(name = "学习记录管理", description = "学习记录相关接口")
@RestController
@RequestMapping("/api/learning-records")
@CrossOrigin(origins = "*")
public class LearningRecordController {
    
    private static final Logger logger = LoggerFactory.getLogger(LearningRecordController.class);
    
    @Autowired
    private LearningRecordService learningRecordService;
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Operation(summary = "创建或更新学习记录")
    @PostMapping
    public Result<Void> saveOrUpdateRecord(@Valid @RequestBody LearningRecord learningRecord,
                                          @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        logger.info("[saveOrUpdateRecord] 接收到的记录数据: {}", learningRecord);
        if (userId != null && learningRecord.getUserId() == null) {
            learningRecord.setUserId(userId);
        }
        
        // 添加租户ID
        if (learningRecord.getTenantId() == null && learningRecord.getCourseId() != null) {
            Course course = courseMapper.selectById(learningRecord.getCourseId());
            if (course != null) {
                learningRecord.setTenantId(course.getTenantId());
                logger.info("[saveOrUpdateRecord] 设置租户ID: {}", course.getTenantId());
            }
        }
        
        return learningRecordService.saveOrUpdateRecord(learningRecord);
    }
    
    @Operation(summary = "删除学习记录")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecord(@Parameter(description = "记录ID") @PathVariable Long id,
                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return learningRecordService.deleteRecord(id, userId);
    }
    
    @Operation(summary = "获取学习记录详情")
    @GetMapping("/{id}")
    public Result<LearningRecord> getRecordById(@Parameter(description = "记录ID") @PathVariable Long id,
                                               @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        return learningRecordService.getRecordById(id, userId);
    }
    
    @Operation(summary = "获取学习记录列表")
    @GetMapping
    public Result<PageResult<LearningRecord>> getRecordList(@RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(required = false) Long userId,
                                                          @RequestParam(required = false) Long courseId,
                                                          @RequestParam(required = false) Integer status,
                                                          @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {
        // 如果未提供用户ID且有当前用户，则使用当前用户ID
        if (userId == null && currentUserId != null) {
            userId = currentUserId;
        }
        
        logger.info("[getRecordList] 查询学习记录, 用户ID: {}, 课程ID: {}, 状态: {}, 页码: {}, 大小: {}", 
                   userId, courseId, status, page, size);
        
        return learningRecordService.getRecordList(page, size, userId, courseId, status);
    }
    
    @Operation(summary = "更新学习进度")
    @PutMapping("/progress")
    public Result<Void> updateProgress(@RequestParam Long courseId,
                                      @RequestParam Integer progress,
                                      @RequestParam(required = false) Long chapterId,
                                      @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        
        logger.info("[updateProgress] 更新学习进度, 用户ID: {}, 课程ID: {}, 章节ID: {}, 进度: {}", 
                   userId, courseId, chapterId, progress);
        
        try {
            LearningRecord record = new LearningRecord();
            record.setUserId(userId);
            record.setCourseId(courseId);
            record.setProgress(progress);
            record.setLastChapterId(chapterId);
            
            // 添加租户ID
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                record.setTenantId(course.getTenantId());
            }
            
            return learningRecordService.saveOrUpdateRecord(record);
        } catch (Exception e) {
            logger.error("[updateProgress] 更新进度失败", e);
            return Result.error("更新学习进度失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "获取用户学习统计")
    @GetMapping("/user-stats")
    public Result<Map<String, Object>> getUserLearningStats(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        
        logger.info("[getUserLearningStats] 获取用户学习统计, 用户ID: {}", userId);
        
        return learningRecordService.getUserLearningStats(userId);
    }
    
    @Operation(summary = "获取课程学习统计")
    @GetMapping("/course-stats")
    public Result<Map<String, Object>> getCourseLearnStats(@RequestParam Long courseId) {
        logger.info("[getCourseLearnStats] 获取课程学习统计, 课程ID: {}", courseId);
        
        return learningRecordService.getCourseLearnStats(courseId);
    }
    
    @Operation(summary = "完成课程学习")
    @PostMapping("/complete")
    public Result<Void> completeCourse(@RequestParam Long courseId,
                                      @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        
        logger.info("[completeCourse] 完成课程学习, 用户ID: {}, 课程ID: {}", userId, courseId);
        
        return learningRecordService.completeCourse(userId, courseId);
    }
} 