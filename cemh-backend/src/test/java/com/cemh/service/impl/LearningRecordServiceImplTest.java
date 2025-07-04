package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.LearningRecord;
import com.cemh.mapper.LearningRecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LearningRecordServiceImplTest {

    @InjectMocks
    private LearningRecordServiceImpl learningRecordService;

    @Mock
    private LearningRecordMapper learningRecordMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrUpdateRecordNewRecord() {
        LearningRecord record = new LearningRecord();
        record.setUserId(1L);
        record.setCourseId(10L);
        record.setProgress(50);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(learningRecordMapper.insert(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.saveOrUpdateRecord(record);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, times(1)).insert(any(LearningRecord.class));
        verify(learningRecordMapper, never()).updateById(any(LearningRecord.class));
        assertEquals(1, record.getStatus()); // 学习中
    }

    @Test
    void testSaveOrUpdateRecordUpdateExisting() {
        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(1L);
        existingRecord.setCourseId(10L);
        existingRecord.setProgress(20);
        existingRecord.setStatus(1);

        LearningRecord updatedRecord = new LearningRecord();
        updatedRecord.setUserId(1L);
        updatedRecord.setCourseId(10L);
        updatedRecord.setProgress(70);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(existingRecord);
        when(learningRecordMapper.updateById(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.saveOrUpdateRecord(updatedRecord);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, never()).insert(any(LearningRecord.class));
        verify(learningRecordMapper, times(1)).updateById(any(LearningRecord.class));
        assertEquals(70, existingRecord.getProgress()); // Check if progress is updated on existingRecord
        assertEquals(1, existingRecord.getStatus()); // Still learning
    }

    @Test
    void testSaveOrUpdateRecordCompleteCourse() {
        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(1L);
        existingRecord.setCourseId(10L);
        existingRecord.setProgress(90);
        existingRecord.setStatus(1);

        LearningRecord updatedRecord = new LearningRecord();
        updatedRecord.setUserId(1L);
        updatedRecord.setCourseId(10L);
        updatedRecord.setProgress(100);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(existingRecord);
        when(learningRecordMapper.updateById(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.saveOrUpdateRecord(updatedRecord);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, times(1)).updateById(any(LearningRecord.class));
        assertEquals(100, existingRecord.getProgress());
        assertEquals(2, existingRecord.getStatus()); // Completed
        assertNotNull(existingRecord.getCompletionTime());
    }

    @Test
    void testDeleteRecordSuccess() {
        Long recordId = 1L;
        Long userId = 1L;

        LearningRecord record = new LearningRecord();
        record.setId(recordId);
        record.setUserId(userId);
        record.setDeleted(0);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(record);
        when(learningRecordMapper.updateById(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.deleteRecord(recordId, userId);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, times(1)).updateById(any(LearningRecord.class));
        assertEquals(1, record.getDeleted());
    }

    @Test
    void testGetRecordByIdSuccess() {
        Long recordId = 1L;
        Long userId = 1L;

        LearningRecord record = new LearningRecord();
        record.setId(recordId);
        record.setUserId(userId);
        record.setDeleted(0);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(record);

        Result<LearningRecord> result = learningRecordService.getRecordById(recordId, userId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(recordId, result.getData().getId());
        verify(learningRecordMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testGetRecordList() {
        int pageNum = 1;
        int pageSize = 10;
        Long userId = 1L;

        Page<LearningRecord> page = new Page<>(pageNum, pageSize);
        List<LearningRecord> records = Arrays.asList(new LearningRecord(), new LearningRecord());
        IPage<LearningRecord> iPage = mock(Page.class);
        when(iPage.getRecords()).thenReturn(records);
        when(iPage.getTotal()).thenReturn(2L);
        when(iPage.getPages()).thenReturn(1L);
        when(iPage.getCurrent()).thenReturn((long) pageNum);
        when(iPage.getSize()).thenReturn((long) pageSize);

        when(learningRecordMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn((Page) iPage);

        Result<PageResult<LearningRecord>> result = learningRecordService.getRecordList(pageNum, pageSize, userId, null, null);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        verify(learningRecordMapper, times(1)).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testUpdateProgressNewRecord() {
        Long userId = 1L;
        Long courseId = 10L;
        Integer progress = 60;

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(learningRecordMapper.insert(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, times(1)).insert(any(LearningRecord.class));
        verify(learningRecordMapper, never()).updateById(any(LearningRecord.class));
    }

    @Test
    void testUpdateProgressExistingRecord() {
        Long userId = 1L;
        Long courseId = 10L;
        Integer progress = 80;

        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(userId);
        existingRecord.setCourseId(courseId);
        existingRecord.setProgress(50);
        existingRecord.setStatus(1);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(existingRecord);
        when(learningRecordMapper.updateById(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.updateProgress(userId, courseId, progress);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, never()).insert(any(LearningRecord.class));
        verify(learningRecordMapper, times(1)).updateById(any(LearningRecord.class));
        assertEquals(80, existingRecord.getProgress());
        assertEquals(1, existingRecord.getStatus());
    }

    @Test
    void testGetUserLearningStats() {
        Long userId = 1L;
        List<LearningRecord> records = Arrays.asList(
                createLearningRecord(1L, 10L, 100, 2), // Completed
                createLearningRecord(2L, 11L, 50, 1),  // In Progress
                createLearningRecord(3L, 12L, 0, 0)   // Not Started
        );

        when(learningRecordMapper.selectList(any(QueryWrapper.class))).thenReturn(records);

        Result<Map<String, Object>> result = learningRecordService.getUserLearningStats(userId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(3, result.getData().get("totalCourses"));
        assertEquals(1, result.getData().get("completedCourses"));
        assertEquals(1, result.getData().get("inProgressCourses"));
        assertEquals(33.333333333333336, (Double) result.getData().get("completionRate"), 0.001);
    }

    @Test
    void testGetCourseLearnStats() {
        Long courseId = 10L;
        List<LearningRecord> records = Arrays.asList(
                createLearningRecord(1L, 10L, 100, 2), // Completed
                createLearningRecord(2L, 10L, 50, 1),  // In Progress
                createLearningRecord(3L, 10L, 0, 0)   // Not Started
        );

        when(learningRecordMapper.selectList(any(QueryWrapper.class))).thenReturn(records);

        Result<Map<String, Object>> result = learningRecordService.getCourseLearnStats(courseId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(3, result.getData().get("totalLearners"));
        assertEquals(1, result.getData().get("completedLearners"));
        assertEquals(50.0, (Double) result.getData().get("averageProgress"), 0.001);
        assertEquals(33.333333333333336, (Double) result.getData().get("completionRate"), 0.001);
    }

    @Test
    void testCompleteCourseNewRecord() {
        Long userId = 1L;
        Long courseId = 10L;

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(learningRecordMapper.insert(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.completeCourse(userId, courseId);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, times(1)).insert(any(LearningRecord.class));
        verify(learningRecordMapper, never()).updateById(any(LearningRecord.class));
    }

    @Test
    void testCompleteCourseExistingRecord() {
        Long userId = 1L;
        Long courseId = 10L;

        LearningRecord existingRecord = new LearningRecord();
        existingRecord.setId(1L);
        existingRecord.setUserId(userId);
        existingRecord.setCourseId(courseId);
        existingRecord.setProgress(50);
        existingRecord.setStatus(1);

        when(learningRecordMapper.selectOne(any(QueryWrapper.class))).thenReturn(existingRecord);
        when(learningRecordMapper.updateById(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = learningRecordService.completeCourse(userId, courseId);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, never()).insert(any(LearningRecord.class));
        verify(learningRecordMapper, times(1)).updateById(any(LearningRecord.class));
        assertEquals(100, existingRecord.getProgress());
        assertEquals(1, existingRecord.getStatus());
        assertNotNull(existingRecord.getCompletionTime());
    }

    private LearningRecord createLearningRecord(Long id, Long courseId, Integer progress, Integer status) {
        LearningRecord record = new LearningRecord();
        record.setId(id);
        record.setUserId(1L);
        record.setCourseId(courseId);
        record.setProgress(progress);
        record.setStatus(status);
        record.setDeleted(0);
        return record;
    }
}


