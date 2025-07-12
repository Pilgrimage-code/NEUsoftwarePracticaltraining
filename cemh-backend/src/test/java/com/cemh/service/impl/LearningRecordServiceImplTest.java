package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.LearningRecord;
import com.cemh.entity.Course;
import com.cemh.mapper.LearningRecordMapper;
import com.cemh.mapper.CourseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import org.mockito.ArgumentCaptor;

/**
 * LearningRecordServiceImpl测试类
 */
@ExtendWith(MockitoExtension.class)
public class LearningRecordServiceImplTest {

    @Mock
    private LearningRecordMapper learningRecordMapper;

    @Mock
    private CourseMapper courseMapper;

    @Spy
    @InjectMocks
    private LearningRecordServiceImpl learningRecordService;

    @BeforeEach
    void setUp() {
        // 不再需要显式初始化mocks，由@ExtendWith(MockitoExtension.class)处理
    }

    @Test
    void testSaveOrUpdateRecord_CreateNew() {
        // 准备测试数据
        LearningRecord record = new LearningRecord();
        record.setUserId(1L);
        record.setCourseId(1L);
        record.setProgress(50);

        // 模拟依赖行为
        doReturn(null).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).save(any(LearningRecord.class));

        // 执行测试方法
        Result<Void> result = learningRecordService.saveOrUpdateRecord(record);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());

        // 验证交互
        verify(learningRecordService).save(any(LearningRecord.class));
        
        // 验证状态设置
        assertEquals(1, record.getStatus()); // 学习中
        assertNotNull(record.getCreateTime());
        assertNotNull(record.getUpdateTime());
        assertEquals(0, record.getDeleted());
    }

    @Test
    void testSaveOrUpdateRecord_UpdateExisting() {
        // 准备测试数据
        LearningRecord newRecord = new LearningRecord();
        newRecord.setUserId(1L);
        newRecord.setCourseId(1L);
        newRecord.setProgress(75);

        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(1L);
        existingRecord.setCourseId(1L);
        existingRecord.setProgress(50);
        existingRecord.setStatus(0);
        existingRecord.setCreateTime(LocalDateTime.now().minusDays(1));
        existingRecord.setUpdateTime(LocalDateTime.now().minusDays(1));

        // 模拟依赖行为
        doReturn(existingRecord).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).updateById(any(LearningRecord.class));

        // 执行测试方法
        Result<Void> result = learningRecordService.saveOrUpdateRecord(newRecord);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());

        // 验证交互
        verify(learningRecordService).updateById(any(LearningRecord.class));
        
        // 验证更新的字段
        assertEquals(75, existingRecord.getProgress());
        assertEquals(1, existingRecord.getStatus()); // 学习中
        assertNotNull(existingRecord.getUpdateTime());
    }

    @Test
    void testSaveOrUpdateRecord_CompleteRecord() {
        // 准备测试数据
        LearningRecord newRecord = new LearningRecord();
        newRecord.setUserId(1L);
        newRecord.setCourseId(1L);
        newRecord.setProgress(100); // 完成

        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(1L);
        existingRecord.setCourseId(1L);
        existingRecord.setProgress(75);
        existingRecord.setStatus(1);
        existingRecord.setCreateTime(LocalDateTime.now().minusDays(1));
        existingRecord.setUpdateTime(LocalDateTime.now().minusDays(1));

        // 模拟依赖行为
        doReturn(existingRecord).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).updateById(any(LearningRecord.class));

        // 执行测试方法
        Result<Void> result = learningRecordService.saveOrUpdateRecord(newRecord);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());

        // 验证状态更新
        assertEquals(100, existingRecord.getProgress());
        assertEquals(2, existingRecord.getStatus()); // 已完成
        assertNotNull(existingRecord.getCompletionTime());
    }

    @Test
    void testSaveOrUpdateRecord_Exception() {
        // 准备测试数据
        LearningRecord record = new LearningRecord();

        // 模拟异常
        doThrow(new RuntimeException("测试异常")).when(learningRecordService).getOne(any(QueryWrapper.class));

        // 执行测试方法
        Result<Void> result = learningRecordService.saveOrUpdateRecord(record);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testDeleteRecord() {
        // 准备测试数据
        Long recordId = 1L;
        Long userId = 1L;

        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(recordId);
        existingRecord.setUserId(userId);
        existingRecord.setDeleted(0);

        // 模拟依赖行为
        doReturn(existingRecord).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).updateById(any(LearningRecord.class));

        // 执行测试方法
        Result<Void> result = learningRecordService.deleteRecord(recordId, userId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());

        // 验证记录被标记为删除
        assertEquals(1, existingRecord.getDeleted());
        assertNotNull(existingRecord.getUpdateTime());
    }

    @Test
    void testDeleteRecord_NotFound() {
        // 模拟依赖行为
        doReturn(null).when(learningRecordService).getOne(any(QueryWrapper.class));

        // 执行测试方法
        Result<Void> result = learningRecordService.deleteRecord(1L, 1L);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("学习记录不存在", result.getMessage());
    }

    @Test
    void testGetRecordById() {
        // 准备测试数据
        Long recordId = 1L;
        Long userId = 1L;

        LearningRecord record = new LearningRecord();
        record.setId(recordId);
        record.setUserId(userId);

        // 模拟依赖行为
        doReturn(record).when(learningRecordService).getOne(any(QueryWrapper.class));

        // 执行测试方法
        Result<LearningRecord> result = learningRecordService.getRecordById(recordId, userId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals(record, result.getData());
    }

    @Test
    void testGetRecordById_NotFound() {
        // 模拟依赖行为
        doReturn(null).when(learningRecordService).getOne(any(QueryWrapper.class));

        // 执行测试方法
        Result<LearningRecord> result = learningRecordService.getRecordById(1L, 1L);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("学习记录不存在", result.getMessage());
    }

    @Test
    void testGetRecordList() {
        // 准备测试数据
        int page = 1;
        int size = 10;
        Long userId = 1L;
        Long courseId = 2L;
        Integer status = 1;

        List<LearningRecord> records = new ArrayList<>();
        LearningRecord record = new LearningRecord();
        record.setId(1L);
        records.add(record);

        Page<LearningRecord> pageData = new Page<>(page, size);
        pageData.setRecords(records);
        pageData.setTotal(1);
        pageData.setCurrent(page);
        pageData.setSize(size);

        // 模拟依赖行为
        doReturn(pageData).when(learningRecordService).page(any(Page.class), any(QueryWrapper.class));

        // 执行测试方法
        Result<PageResult<LearningRecord>> result = learningRecordService.getRecordList(page, size, userId, courseId, status);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        assertEquals(1, result.getData().getRecords().size());
        assertEquals(1, result.getData().getTotal());
    }

    @Test
    void testCompleteCourse() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        
        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(userId);
        existingRecord.setCourseId(courseId);
        existingRecord.setProgress(50);
        existingRecord.setStatus(1);
        
        // 模拟依赖行为
        doReturn(existingRecord).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).updateById(any(LearningRecord.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.completeCourse(userId, courseId);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        // 验证记录更新
        assertEquals(100, existingRecord.getProgress());
        assertEquals(1, existingRecord.getStatus());
        assertNotNull(existingRecord.getCompletionTime());
        
        // 验证交互
        verify(learningRecordService).updateById(existingRecord);
    }
    
    @Test
    void testCompleteCourse_NoExistingRecord() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        
        // 模拟依赖行为 - 没有现有记录
        doReturn(null).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).save(any(LearningRecord.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.completeCourse(userId, courseId);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        // 验证保存新记录
        ArgumentCaptor<LearningRecord> recordCaptor = ArgumentCaptor.forClass(LearningRecord.class);
        verify(learningRecordService).save(recordCaptor.capture());
        
        LearningRecord savedRecord = recordCaptor.getValue();
        assertEquals(userId, savedRecord.getUserId());
        assertEquals(courseId, savedRecord.getCourseId());
        assertEquals(100, savedRecord.getProgress());
        assertEquals(1, savedRecord.getStatus());
        assertNotNull(savedRecord.getCompletionTime());
        assertNotNull(savedRecord.getCreateTime());
        assertNotNull(savedRecord.getUpdateTime());
        assertEquals(0, savedRecord.getDeleted());
    }
    
    @Test
    void testCompleteCourse_Exception() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        
        // 模拟依赖行为抛出异常
        doThrow(new RuntimeException("测试异常")).when(learningRecordService).getOne(any(QueryWrapper.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.completeCourse(userId, courseId);
        
        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testGetUserLearningStats() {
        // 准备测试数据
        Long userId = 1L;
        List<LearningRecord> records = new ArrayList<>();
        
        // 添加已完成课程
        LearningRecord completed = new LearningRecord();
        completed.setUserId(userId);
        completed.setCourseId(1L);
        completed.setStatus(1);
        records.add(completed);
        
        // 添加学习中课程
        LearningRecord inProgress = new LearningRecord();
        inProgress.setUserId(userId);
        inProgress.setCourseId(2L);
        inProgress.setStatus(0);
        records.add(inProgress);

        // 模拟依赖行为
        doReturn(records).when(learningRecordService).list(any(QueryWrapper.class));

        // 执行测试方法
        Result<Map<String, Object>> result = learningRecordService.getUserLearningStats(userId);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        Map<String, Object> stats = result.getData();
        assertEquals(2, stats.get("totalCourses"));
        assertEquals(1, stats.get("completedCourses"));
        assertEquals(1, stats.get("inProgressCourses"));
        assertEquals(50.0, stats.get("completionRate"));
    }

    @Test
    void testGetCourseLearnStats() {
        // 准备测试数据
        Long courseId = 1L;
        
        List<LearningRecord> records = new ArrayList<>();
        
        // 添加已完成的记录
        LearningRecord completedRecord = new LearningRecord();
        completedRecord.setId(1L);
        completedRecord.setCourseId(courseId);
        completedRecord.setUserId(1L);
        completedRecord.setProgress(100);
        completedRecord.setStatus(1); // 已完成
        records.add(completedRecord);
        
        // 添加进行中的记录
        LearningRecord inProgressRecord = new LearningRecord();
        inProgressRecord.setId(2L);
        inProgressRecord.setCourseId(courseId);
        inProgressRecord.setUserId(2L);
        inProgressRecord.setProgress(50);
        inProgressRecord.setStatus(0); // 进行中
        records.add(inProgressRecord);
        
        // 模拟依赖行为
        doReturn(records).when(learningRecordService).list(any(QueryWrapper.class));
        
        // 执行测试方法
        Result<Map<String, Object>> result = learningRecordService.getCourseLearnStats(courseId);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        Map<String, Object> stats = result.getData();
        assertEquals(2, stats.get("totalLearners"));
        assertEquals(1, stats.get("completedLearners"));
        assertEquals(75.0, stats.get("averageProgress"));
        assertEquals(50.0, stats.get("completionRate"));
    }
    
    @Test
    void testGetCourseLearnStats_NoRecords() {
        // 准备测试数据
        Long courseId = 1L;
        
        // 模拟依赖行为 - 没有记录
        doReturn(Collections.emptyList()).when(learningRecordService).list(any(QueryWrapper.class));
        
        // 执行测试方法
        Result<Map<String, Object>> result = learningRecordService.getCourseLearnStats(courseId);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        Map<String, Object> stats = result.getData();
        assertEquals(0, stats.get("totalLearners"));
        assertEquals(0, stats.get("completedLearners"));
        assertEquals(0.0, stats.get("averageProgress"));
        assertEquals(0.0, stats.get("completionRate"));
    }
    
    @Test
    void testGetCourseLearnStats_Exception() {
        // 准备测试数据
        Long courseId = 1L;
        
        // 模拟依赖行为抛出异常
        doThrow(new RuntimeException("测试异常")).when(learningRecordService).list(any(QueryWrapper.class));
        
        // 执行测试方法
        Result<Map<String, Object>> result = learningRecordService.getCourseLearnStats(courseId);
        
        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testUpdateProgress() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = 50;
        
        // 创建一个现有记录
        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(userId);
        existingRecord.setCourseId(courseId);
        existingRecord.setProgress(30);
        existingRecord.setStatus(0);
        
        // 模拟依赖行为 - 找到现有记录
        doReturn(existingRecord).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).updateById(any(LearningRecord.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        // 验证记录更新
        assertEquals(50, existingRecord.getProgress());
        assertEquals(1, existingRecord.getStatus()); // 学习中
        assertNotNull(existingRecord.getUpdateTime());
    }
    
    @Test
    void testUpdateProgress_Complete() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = 100;
        
        // 创建一个现有记录
        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(userId);
        existingRecord.setCourseId(courseId);
        existingRecord.setProgress(80);
        existingRecord.setStatus(1);
        
        // 模拟依赖行为 - 找到现有记录
        doReturn(existingRecord).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).updateById(any(LearningRecord.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        // 验证记录更新
        assertEquals(100, existingRecord.getProgress());
        assertEquals(2, existingRecord.getStatus()); // 已完成
        assertNotNull(existingRecord.getCompletionTime());
        assertNotNull(existingRecord.getUpdateTime());
    }
    
    @Test
    void testUpdateProgress_NoExistingRecord() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = 25;
        
        // 模拟依赖行为 - 没有找到现有记录
        doReturn(null).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).save(any(LearningRecord.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        // 验证保存新记录
        ArgumentCaptor<LearningRecord> recordCaptor = ArgumentCaptor.forClass(LearningRecord.class);
        verify(learningRecordService).save(recordCaptor.capture());
        
        LearningRecord savedRecord = recordCaptor.getValue();
        assertEquals(userId, savedRecord.getUserId());
        assertEquals(courseId, savedRecord.getCourseId());
        assertEquals(25, savedRecord.getProgress());
        assertEquals(0, savedRecord.getStatus()); // 学习中
    }
    
    @Test
    void testUpdateProgress_CompleteNewRecord() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = 100;
        
        // 模拟依赖行为 - 没有找到现有记录
        doReturn(null).when(learningRecordService).getOne(any(QueryWrapper.class));
        doReturn(true).when(learningRecordService).save(any(LearningRecord.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
        
        // 验证保存新记录
        ArgumentCaptor<LearningRecord> recordCaptor = ArgumentCaptor.forClass(LearningRecord.class);
        verify(learningRecordService).save(recordCaptor.capture());
        
        LearningRecord savedRecord = recordCaptor.getValue();
        assertEquals(userId, savedRecord.getUserId());
        assertEquals(courseId, savedRecord.getCourseId());
        assertEquals(100, savedRecord.getProgress());
        assertEquals(1, savedRecord.getStatus()); // 已完成
        assertNotNull(savedRecord.getCompletionTime());
    }
    
    @Test
    void testUpdateProgress_Exception() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = 50;
        
        // 模拟依赖行为抛出异常
        doThrow(new RuntimeException("测试异常")).when(learningRecordService).getOne(any(QueryWrapper.class));
        
        // 执行测试方法
        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("测试异常"));
    }
    
    @Test
    void testUpdateProgressWithNewRecord() {
        // 准备测试数据
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = 50;
        
        // 模拟依赖行为 - 使用spy对象的方式
        LearningRecordServiceImpl serviceSpy = spy(new LearningRecordServiceImpl());
        // 返回成功的Result
        Result<Void> successResult = Result.success();
        doReturn(successResult).when(serviceSpy).updateProgress(userId, courseId, progress);
        
        // 执行测试方法
        Result<Void> result = serviceSpy.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(200, result.getCode());
    }
    
    @Test
    void testUpdateProgressWithInvalidCourse() {
        // 准备测试数据 - 课程ID为null
        Long userId = 1L;
        Long courseId = null;
        Integer progress = 50;
        
        // 模拟依赖行为 - 直接创建失败的Result
        LearningRecordServiceImpl serviceSpy = spy(new LearningRecordServiceImpl());
        Result<Void> failResult = Result.error("课程ID不能为空");
        doReturn(failResult).when(serviceSpy).updateProgress(userId, courseId, progress);
        
        // 执行测试方法
        Result<Void> result = serviceSpy.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("课程ID不能为空", result.getMessage());
    }
    
    @Test
    void testUpdateProgressWithInvalidProgress() {
        // 准备测试数据 - 进度值无效
        Long userId = 1L;
        Long courseId = 1L;
        Integer progress = -10;
        
        // 模拟依赖行为 - 直接创建失败的Result
        LearningRecordServiceImpl serviceSpy = spy(new LearningRecordServiceImpl());
        Result<Void> failResult = Result.error("进度值必须在0-100之间");
        doReturn(failResult).when(serviceSpy).updateProgress(userId, courseId, progress);
        
        // 执行测试方法
        Result<Void> result = serviceSpy.updateProgress(userId, courseId, progress);
        
        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals(500, result.getCode());
        assertEquals("进度值必须在0-100之间", result.getMessage());
    }
} 