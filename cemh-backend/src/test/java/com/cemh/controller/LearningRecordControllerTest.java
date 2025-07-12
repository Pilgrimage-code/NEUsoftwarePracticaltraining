package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.Course;
import com.cemh.entity.LearningRecord;
import com.cemh.mapper.CourseMapper;
import com.cemh.service.LearningRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * LearningRecordController测试类
 */
@ExtendWith(MockitoExtension.class)
public class LearningRecordControllerTest {

    @Mock
    private LearningRecordService learningRecordService;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private LearningRecordController controller;

    private LearningRecord learningRecord;
    private Result<Void> successResult;
    private Result<LearningRecord> recordResult;
    private PageResult<LearningRecord> pageResult;
    private Result<PageResult<LearningRecord>> pageResultWrapper;
    private Course course;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        learningRecord = new LearningRecord();
        learningRecord.setId(1L);
        learningRecord.setUserId(1L);
        learningRecord.setCourseId(1L);
        learningRecord.setProgress(50);
        learningRecord.setStatus(1);
        learningRecord.setCreateTime(LocalDateTime.now());
        learningRecord.setUpdateTime(LocalDateTime.now());

        successResult = Result.success();
        recordResult = Result.success(learningRecord);

        List<LearningRecord> records = new ArrayList<>();
        records.add(learningRecord);

        pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(1L);
        pageResult.setCurrent(1L);
        pageResult.setSize(10L);

        pageResultWrapper = Result.success(pageResult);

        course = new Course();
        course.setId(1L);
        course.setTenantId(1L);
        course.setCourseName("测试课程");
    }

    @Test
    void testSaveOrUpdateRecord() {
        // 模拟依赖行为
        when(courseMapper.selectById(anyLong())).thenReturn(course);
        when(learningRecordService.saveOrUpdateRecord(any(LearningRecord.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = controller.saveOrUpdateRecord(learningRecord, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证租户ID被设置
        assertEquals(1L, learningRecord.getTenantId());
    }

    @Test
    void testSaveOrUpdateRecord_WithoutUserId() {
        // 准备测试数据
        learningRecord.setUserId(null);

        // 模拟依赖行为
        when(courseMapper.selectById(anyLong())).thenReturn(course);
        when(learningRecordService.saveOrUpdateRecord(any(LearningRecord.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = controller.saveOrUpdateRecord(learningRecord, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证用户ID被设置
        assertEquals(1L, learningRecord.getUserId());
    }

    @Test
    void testDeleteRecord() {
        // 模拟依赖行为
        when(learningRecordService.deleteRecord(anyLong(), anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = controller.deleteRecord(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetRecordById() {
        // 模拟依赖行为
        when(learningRecordService.getRecordById(anyLong(), anyLong())).thenReturn(recordResult);

        // 执行测试方法
        Result<LearningRecord> result = controller.getRecordById(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1L, result.getData().getId());
    }

    @Test
    void testGetRecordList() {
        // 模拟依赖行为
        when(learningRecordService.getRecordList(anyInt(), anyInt(), anyLong(), anyLong(), anyInt())).thenReturn(pageResultWrapper);

        // 执行测试方法 - 使用当前用户ID
        Result<PageResult<LearningRecord>> result = controller.getRecordList(1, 10, null, 1L, 1, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().getRecords().size());
        
        // 验证使用了当前用户ID
        verify(learningRecordService).getRecordList(1, 10, 1L, 1L, 1);
    }

    @Test
    void testGetRecordList_WithProvidedUserId() {
        // 模拟依赖行为
        when(learningRecordService.getRecordList(anyInt(), anyInt(), anyLong(), anyLong(), anyInt())).thenReturn(pageResultWrapper);

        // 执行测试方法 - 提供特定用户ID
        Result<PageResult<LearningRecord>> result = controller.getRecordList(1, 10, 2L, 1L, 1, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        
        // 验证使用了提供的用户ID而不是当前用户ID
        verify(learningRecordService).getRecordList(1, 10, 2L, 1L, 1);
    }

    @Test
    void testUpdateProgress() {
        // 模拟依赖行为
        when(courseMapper.selectById(anyLong())).thenReturn(course);
        when(learningRecordService.saveOrUpdateRecord(any(LearningRecord.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = controller.updateProgress(1L, 75, 1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        
        // 验证调用了服务方法
        verify(learningRecordService).saveOrUpdateRecord(argThat(record -> 
            record.getUserId().equals(1L) &&
            record.getCourseId().equals(1L) &&
            record.getProgress().equals(75) &&
            record.getLastChapterId().equals(1L)
        ));
    }

    @Test
    void testUpdateProgress_NoUserId() {
        // 执行测试方法 - 没有用户ID
        Result<Void> result = controller.updateProgress(1L, 75, 1L, null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("用户ID不能为空", result.getMessage());
    }

    @Test
    void testUpdateProgress_Exception() {
        // 模拟依赖行为 - 抛出异常
        when(courseMapper.selectById(anyLong())).thenThrow(new RuntimeException("测试异常"));

        // 执行测试方法
        Result<Void> result = controller.updateProgress(1L, 75, 1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testGetUserLearningStats() {
        // 准备测试数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCourses", 5);
        stats.put("completedCourses", 3);
        stats.put("completionRate", 60.0);

        // 模拟依赖行为
        when(learningRecordService.getUserLearningStats(anyLong())).thenReturn(Result.success(stats));

        // 执行测试方法
        Result<Map<String, Object>> result = controller.getUserLearningStats(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(5, result.getData().get("totalCourses"));
    }

    @Test
    void testGetUserLearningStats_NoUserId() {
        // 执行测试方法 - 没有用户ID
        Result<Map<String, Object>> result = controller.getUserLearningStats(null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("用户ID不能为空", result.getMessage());
    }

    @Test
    void testGetCourseLearnStats() {
        // 准备测试数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLearners", 10);
        stats.put("completedLearners", 6);
        stats.put("completionRate", 60.0);

        // 模拟依赖行为
        when(learningRecordService.getCourseLearnStats(anyLong())).thenReturn(Result.success(stats));

        // 执行测试方法
        Result<Map<String, Object>> result = controller.getCourseLearnStats(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(10, result.getData().get("totalLearners"));
    }

    @Test
    void testCompleteCourse() {
        // 模拟依赖行为
        when(learningRecordService.completeCourse(anyLong(), anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = controller.completeCourse(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testCompleteCourse_NoUserId() {
        // 执行测试方法 - 没有用户ID
        Result<Void> result = controller.completeCourse(1L, null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("用户ID不能为空", result.getMessage());
    }
} 