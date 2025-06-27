package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.CourseDTO;
import com.cemh.dto.CourseQueryDTO;
import com.cemh.entity.Course;
import com.cemh.entity.CourseChapter;
import com.cemh.service.CourseService;
import com.cemh.vo.CourseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 课程管理控制器
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public Result<PageResult<CourseVO>> getCourseList(CourseQueryDTO queryDTO,
                                                    @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return courseService.getCoursePage(queryDTO, tenantId);
    }
    
    @GetMapping("/{id}")
    public Result<CourseVO> getCourseById(@PathVariable Long id,
                                         @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return courseService.getCourseById(id, tenantId);
    }
    
    @PostMapping
    public Result<Void> createCourse(@Valid @RequestBody CourseDTO courseDTO,
                                     @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        logger.info("[createCourse] 接收到的课程数据: {}", courseDTO);
        
        if (tenantId != null) {
            courseDTO.setTenantId(tenantId);
        }
        
        return courseService.createCourse(courseDTO);
    }
    
    @PutMapping("/{id}")
    public Result<Void> updateCourse(@PathVariable Long id,
                                     @Valid @RequestBody CourseDTO courseDTO,
                                     @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        logger.info("[updateCourse] 接收到的课程数据: {}", courseDTO);
        
        courseDTO.setId(id);
        if (tenantId != null) {
            courseDTO.setTenantId(tenantId);
        }
        
        return courseService.updateCourse(courseDTO);
    }
    
    @Operation(summary = "删除课程")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCourse(@Parameter(description = "课程ID") @PathVariable Long id,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        try {
            logger.info("删除课程，ID: {}, 租户ID: {}", id, tenantId);
            return courseService.deleteCourse(id, tenantId);
        } catch (Exception e) {
            logger.error("删除课程失败", e);
            return Result.error("删除课程失败: " + e.getMessage());
        }
    }
    
    @Operation(summary = "批量删除课程")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteCourse(@Parameter(description = "课程ID列表") @RequestBody List<Long> ids,
                                     @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        try {
            logger.info("批量删除课程，ID列表: {}, 租户ID: {}", ids, tenantId);
            return courseService.batchDeleteCourse(ids, tenantId);
        } catch (Exception e) {
            logger.error("批量删除课程失败", e);
            return Result.error("批量删除课程失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/categories")
    public Result<List<Map<String, Object>>> getCourseCategories(@RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return courseService.getCourseCategories(tenantId);
    }
    
    @GetMapping("/hot")
    public Result<List<CourseVO>> getHotCourses(@RequestParam(defaultValue = "10") Integer limit) {
        return courseService.getHotCourses(limit);
    }
    
    @GetMapping("/latest")
    public Result<List<CourseVO>> getLatestCourses(@RequestParam(defaultValue = "10") Integer limit) {
        return courseService.getLatestCourses(limit);
    }
    
    @PutMapping("/{id}/view")
    public Result<Void> incrementViewCount(@PathVariable Long id) {
        return courseService.incrementViewCount(id);
    }
    
    /**
     * 导出课程数据
     */
    @GetMapping("/export")
    public void exportCourses(CourseQueryDTO queryDTO, HttpServletResponse response) {
        try {
            courseService.exportCourses(queryDTO, response);
        } catch (Exception e) {
            System.err.println("导出课程数据失败: " + e.getMessage());
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取课程章节列表
     */
    @GetMapping("/{courseId}/chapters")
    public Result<List<CourseChapter>> getCourseChapters(@PathVariable Long courseId) {
        return courseService.getCourseChapters(courseId);
    }
    
    /**
     * 获取章节详情
     */
    @GetMapping("/{courseId}/chapter/{chapterId}")
    public Result<CourseChapter> getChapterDetail(@PathVariable Long courseId, @PathVariable Long chapterId) {
        return courseService.getChapterDetail(courseId, chapterId);
    }
    
    /**
     * 创建课程章节
     */
    @Operation(summary = "创建章节")
    @PostMapping("/chapter")
    public Result<Void> createChapter(@Valid @RequestBody CourseChapter chapter) {
        try {
            logger.info("创建章节，课程ID: {}, 章节名称: {}", chapter.getCourseId(), chapter.getChapterName());
            return courseService.createChapter(chapter);
        } catch (Exception e) {
            logger.error("创建章节失败", e);
            return Result.error("创建章节失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新课程章节
     */
    @PutMapping("/{courseId}/chapter/{chapterId}")
    public Result<Void> updateChapter(@PathVariable Long courseId, @PathVariable Long chapterId, 
                                      @Valid @RequestBody CourseChapter chapter) {
        chapter.setId(chapterId);
        chapter.setCourseId(courseId);
        
        // 处理视频URL格式
        if (chapter.getVideoUrl() != null && chapter.getVideoUrl().startsWith("http")) {
            // 如果是完整URL格式，保留文件名部分
            String fullUrl = chapter.getVideoUrl();
            logger.info("处理章节视频URL: {}", fullUrl);
            
            // 从URL中提取文件名 - 保持原格式存储以兼容现有代码
            String filename = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);
            if (filename.matches("\\d{8}_.*\\.\\w+")) {
                // 如果是日期格式的文件名，则使用新格式存储
                chapter.setVideoUrl("/uploads/" + filename);
                logger.info("更新后的视频URL: {}", chapter.getVideoUrl());
            }
        }
        
        return courseService.updateChapter(chapter);
    }
    
    /**
     * 删除课程章节
     */
    @DeleteMapping("/{courseId}/chapter/{chapterId}")
    public Result<Void> deleteChapter(@PathVariable Long courseId, @PathVariable Long chapterId) {
        return courseService.deleteChapter(courseId, chapterId);
    }
    
    /**
     * 更新学习进度
     */
    @PostMapping("/{courseId}/chapter/{chapterId}/progress")
    public Result<Void> updateLearningProgress(@PathVariable Long courseId, @PathVariable Long chapterId,
                                              @RequestBody Map<String, Object> data,
                                              @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.error("未登录或用户ID为空");
        }
        
        Integer progress = data.get("progress") != null ? Integer.parseInt(data.get("progress").toString()) : 0;
        return courseService.updateLearningProgress(courseId, chapterId, userId, progress);
    }
} 