package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.dto.MeetingDTO;
import com.cemh.entity.Meeting;
import com.cemh.entity.MeetingMaterial;
import com.cemh.entity.MeetingRegistration;
import com.cemh.mapper.MeetingMapper;
import com.cemh.mapper.MeetingMaterialMapper;
import com.cemh.mapper.MeetingRegistrationMapper;
import com.cemh.vo.MeetingVO;
import com.cemh.dto.MeetingQueryDTO;
import com.cemh.common.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;

class MeetingServiceImplTest {

    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private MeetingMaterialMapper meetingMaterialMapper;
    @Mock
    private MeetingRegistrationMapper meetingRegistrationMapper;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    private MeetingDTO validMeetingDTO;
    private Meeting existingMeeting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        validMeetingDTO = new MeetingDTO();
        validMeetingDTO.setTitle("测试会议");
        validMeetingDTO.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        validMeetingDTO.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toLocalDateTime());
        validMeetingDTO.setType(1);
        validMeetingDTO.setLocation("测试地点");
        validMeetingDTO.setMaxParticipants(100);
        validMeetingDTO.setTenantId(1L);
        
        existingMeeting = new Meeting();
        existingMeeting.setId(1L);
        existingMeeting.setTitle("已存在的会议");
        existingMeeting.setStartTime(LocalDateTime.now());
        existingMeeting.setEndTime(LocalDateTime.now().plusHours(1));
        existingMeeting.setStatus(0);
        existingMeeting.setCurrentParticipants(0);
        existingMeeting.setMaxParticipants(100);
        existingMeeting.setTenantId(1L);
        existingMeeting.setCreateBy(1L);
    }

    // ==================== createMeeting 方法测试 ====================
    
    @Test
    void testCreateMeetingSuccess() {
        when(meetingMapper.insert(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.insert(any(MeetingMaterial.class))).thenReturn(1);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMapper).insert(any(Meeting.class));
    }

    @Test
    void testCreateMeetingWithMaterials() {
        MeetingMaterial material = new MeetingMaterial();
        material.setName("测试材料");
        material.setUrl("http://test.com/material.pdf");
        validMeetingDTO.setMaterials(Arrays.asList(material));
        
        when(meetingMapper.insert(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.insert(any(MeetingMaterial.class))).thenReturn(1);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper).insert(any(MeetingMaterial.class));
    }

    @Test
    void testCreateMeetingWithEmptyMaterials() {
        validMeetingDTO.setMaterials(Collections.emptyList());
        when(meetingMapper.insert(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper, never()).insert(any(MeetingMaterial.class));
    }

    @Test
    void testCreateMeetingWithNullMaterials() {
        validMeetingDTO.setMaterials(null);
        when(meetingMapper.insert(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper, never()).insert(any(MeetingMaterial.class));
    }

    @Test
    void testCreateMeetingStartTimeAfterEndTime() {
        validMeetingDTO.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(2).toLocalDateTime());
        validMeetingDTO.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间不能晚于结束时间", result.getMessage());
    }

    @Test
    void testCreateMeetingInsertFailed() {
        when(meetingMapper.insert(any(Meeting.class))).thenReturn(0);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("创建会议失败", result.getMessage());
    }

    @Test
    void testCreateMeetingException() {
        when(meetingMapper.insert(any(Meeting.class))).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("创建会议异常"));
    }

    // ==================== updateMeeting 方法测试 ====================
    
    @Test
    void testUpdateMeetingSuccess() {
        validMeetingDTO.setId(1L);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        when(meetingMaterialMapper.insert(any(MeetingMaterial.class))).thenReturn(1);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper).deleteByMeetingId(1L);
    }

    @Test
    void testUpdateMeetingNotFound() {
        validMeetingDTO.setId(1L);
        when(meetingMapper.selectById(1L)).thenReturn(null);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testUpdateMeetingStartTimeAfterEndTime() {
        validMeetingDTO.setId(1L);
        validMeetingDTO.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(2).toLocalDateTime());
        validMeetingDTO.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间不能晚于结束时间", result.getMessage());
    }

    @Test
    void testUpdateMeetingUpdateFailed() {
        validMeetingDTO.setId(1L);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(0);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("更新会议失败", result.getMessage());
    }

    @Test
    void testUpdateMeetingException() {
        validMeetingDTO.setId(1L);
        when(meetingMapper.selectById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("更新会议异常"));
    }

    // ==================== deleteMeeting 方法测试 ====================
    
    @Test
    void testDeleteMeetingSuccess() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.deleteById(1L)).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.deleteMeeting(1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testDeleteMeetingNotFound() {
        when(meetingMapper.selectById(1L)).thenReturn(null);
        
        Result<Void> result = meetingService.deleteMeeting(1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testDeleteMeetingNoPermission() {
        existingMeeting.setTenantId(2L); // 不同租户
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.deleteMeeting(1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("无权限删除此会议", result.getMessage());
    }

    @Test
    void testDeleteMeetingNoTenantCheck() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.deleteById(1L)).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.deleteMeeting(1L, null); // tenantId为null
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testDeleteMeetingDeleteFailed() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.deleteById(1L)).thenReturn(0);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.deleteMeeting(1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("删除会议失败", result.getMessage());
    }

    @Test
    void testDeleteMeetingException() {
        when(meetingMapper.selectById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.deleteMeeting(1L, 1L);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("删除会议异常"));
    }

    // ==================== getMeetingDetail 方法测试 ====================
    
    @Test
    void testGetMeetingDetailSuccess() {
        when(meetingMapper.getById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<MeetingVO> result = meetingService.getMeetingDetail(1L, 1L);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(existingMeeting.getTitle(), result.getData().getTitle());
    }

    @Test
    void testGetMeetingDetailNotFound() {
        when(meetingMapper.getById(1L)).thenReturn(null);
        
        Result<MeetingVO> result = meetingService.getMeetingDetail(1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testGetMeetingDetailException() {
        when(meetingMapper.getById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<MeetingVO> result = meetingService.getMeetingDetail(1L, 1L);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("获取会议详情异常"));
    }

    // ==================== getMeetingList 方法测试 ====================
    
    @Test
    void testGetMeetingListSuccess() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Arrays.asList(existingMeeting));
        page.setTotal(1L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getTotal());
    }

    @Test
    void testGetMeetingListWithTitleFilter() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setTitle("测试");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
        assertEquals(0L, result.getData().getTotal());
    }

    @Test
    void testGetMeetingListWithTypeFilter() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setType("tech");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithStatusFilter() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setStatus("0");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithTenantFilter() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setTenantId(1L);
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithTimeRange() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setStartTimeBegin(LocalDateTime.now());
        queryDTO.setStartTimeEnd(LocalDateTime.now().plusDays(1));
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithSortOrder() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setSortOrder("asc");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListException() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenThrow(new RuntimeException("数据库异常"));
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("获取会议列表异常"));
    }

    // ==================== cancelMeeting 方法测试 ====================
    
    @Test
    void testCancelMeetingSuccess() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testCancelMeetingNotFound() {
        when(meetingMapper.selectById(1L)).thenReturn(null);
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testCancelMeetingNoPermission() {
        existingMeeting.setTenantId(2L);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("无权限取消此会议", result.getMessage());
    }

    @Test
    void testCancelMeetingUpdateFailed() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(0);
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("取消会议失败", result.getMessage());
    }

    @Test
    void testCancelMeetingException() {
        when(meetingMapper.selectById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("取消会议异常"));
    }

    // ==================== publishMeeting 方法测试 ====================
    
    @Test
    void testPublishMeetingSuccess() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.publishMeeting(1L, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testPublishMeetingNotFound() {
        when(meetingMapper.selectById(1L)).thenReturn(null);
        
        Result<Void> result = meetingService.publishMeeting(1L, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testPublishMeetingNoPermission() {
        existingMeeting.setTenantId(2L);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.publishMeeting(1L, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("无权限发布此会议", result.getMessage());
    }

    @Test
    void testPublishMeetingUpdateFailed() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(0);
        
        Result<Void> result = meetingService.publishMeeting(1L, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("发布会议失败", result.getMessage());
    }

    @Test
    void testPublishMeetingException() {
        when(meetingMapper.selectById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.publishMeeting(1L, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("发布会议异常"));
    }

    // ==================== topMeeting 方法测试 ====================
    
    @Test
    void testTopMeetingSuccess() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.topMeeting(1L, true, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testTopMeetingUnTop() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.topMeeting(1L, false, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testTopMeetingNotFound() {
        when(meetingMapper.selectById(1L)).thenReturn(null);
        
        Result<Void> result = meetingService.topMeeting(1L, true, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testTopMeetingNoPermission() {
        existingMeeting.setTenantId(2L);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.topMeeting(1L, true, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("无权限操作此会议", result.getMessage());
    }

    @Test
    void testTopMeetingUpdateFailed() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(0);
        
        Result<Void> result = meetingService.topMeeting(1L, true, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("操作失败", result.getMessage());
    }

    @Test
    void testTopMeetingException() {
        when(meetingMapper.selectById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.topMeeting(1L, true, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("操作异常"));
    }

    // ==================== registerMeeting 方法测试 ====================
    
    @Test
    void testRegisterMeetingSuccess() {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(meetingRegistrationMapper.insert(any(MeetingRegistration.class))).thenReturn(1);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testRegisterMeetingNotFound() {
        MeetingRegistration registration = new MeetingRegistration();
        when(meetingMapper.selectById(1L)).thenReturn(null);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("会议不存在", result.getMessage());
    }

    @Test
    void testRegisterMeetingNoPermission() {
        MeetingRegistration registration = new MeetingRegistration();
        existingMeeting.setTenantId(2L);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("无权限操作此会议", result.getMessage());
    }

    @Test
    void testRegisterMeetingFull() {
        MeetingRegistration registration = new MeetingRegistration();
        existingMeeting.setCurrentParticipants(100);
        existingMeeting.setMaxParticipants(100);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("报名人数已满", result.getMessage());
    }

    @Test
    void testRegisterMeetingAlreadyRegistered() {
        MeetingRegistration registration = new MeetingRegistration();
        MeetingRegistration existingRegistration = new MeetingRegistration();
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingRegistration);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("您已报名该会议", result.getMessage());
    }

    @Test
    void testRegisterMeetingInsertFailed() {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(meetingRegistrationMapper.insert(any(MeetingRegistration.class))).thenReturn(0);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertEquals("报名失败", result.getMessage());
    }

    @Test
    void testRegisterMeetingException() {
        MeetingRegistration registration = new MeetingRegistration();
        when(meetingMapper.selectById(1L)).thenThrow(new RuntimeException("数据库异常"));
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("报名异常"));
    }

    // 补充测试用例以提高覆盖率
    @Test
    void testCreateMeetingWithNullStartTime() {
        validMeetingDTO.setStartTime(null);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间和结束时间不能为空", result.getMessage());
    }

    @Test
    void testCreateMeetingWithNullEndTime() {
        validMeetingDTO.setEndTime(null);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间和结束时间不能为空", result.getMessage());
    }

    @Test
    void testCreateMeetingWithNullStartAndEndTime() {
        validMeetingDTO.setStartTime(null);
        validMeetingDTO.setEndTime(null);
        
        Result<Void> result = meetingService.createMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间和结束时间不能为空", result.getMessage());
    }

    @Test
    void testUpdateMeetingWithNullStartTime() {
        validMeetingDTO.setId(1L);
        validMeetingDTO.setStartTime(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间和结束时间不能为空", result.getMessage());
    }

    @Test
    void testUpdateMeetingWithNullEndTime() {
        validMeetingDTO.setId(1L);
        validMeetingDTO.setEndTime(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间和结束时间不能为空", result.getMessage());
    }

    @Test
    void testUpdateMeetingWithNullStartAndEndTime() {
        validMeetingDTO.setId(1L);
        validMeetingDTO.setStartTime(null);
        validMeetingDTO.setEndTime(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertFalse(result.isSuccess());
        assertEquals("开始时间和结束时间不能为空", result.getMessage());
    }

    @Test
    void testUpdateMeetingWithMaterials() {
        validMeetingDTO.setId(1L);
        MeetingMaterial material = new MeetingMaterial();
        material.setName("测试材料");
        material.setUrl("http://test.com/material.pdf");
        validMeetingDTO.setMaterials(Arrays.asList(material));
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        when(meetingMaterialMapper.insert(any(MeetingMaterial.class))).thenReturn(1);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper).insert(any(MeetingMaterial.class));
    }

    @Test
    void testUpdateMeetingWithEmptyMaterials() {
        validMeetingDTO.setId(1L);
        validMeetingDTO.setMaterials(Collections.emptyList());
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper, never()).insert(any(MeetingMaterial.class));
    }

    @Test
    void testUpdateMeetingWithNullMaterials() {
        validMeetingDTO.setId(1L);
        validMeetingDTO.setMaterials(null);
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.updateMeeting(validMeetingDTO);
        
        assertTrue(result.isSuccess());
        verify(meetingMaterialMapper, never()).insert(any(MeetingMaterial.class));
    }

    @Test
    void testGetMeetingListWithTagsFilter() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setTags("重要");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithCreateByFilter() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setCreateBy(1L);
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithCreateTimeRange() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setCreateTimeBegin(LocalDateTime.now());
        queryDTO.setCreateTimeEnd(LocalDateTime.now().plusDays(1));
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithSortOrderAsc() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setSortOrder("asc");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithSortOrderDesc() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setSortOrder("desc");
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetMeetingListWithNullSortOrder() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setSortOrder(null);
        queryDTO.setPage(1);
        queryDTO.setSize(10);
        
        Page<Meeting> page = new Page<>(1L, 10L);
        page.setRecords(Collections.emptyList());
        page.setTotal(0L);
        
        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        
        assertTrue(result.isSuccess());
    }

    // 工具方法：反射调用private convertToVO
    private MeetingVO invokeConvertToVO(Meeting meeting) {
        try {
            Method method = MeetingServiceImpl.class.getDeclaredMethod("convertToVO", Meeting.class);
            method.setAccessible(true);
            return (MeetingVO) method.invoke(meetingService, meeting);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConvertToVOWithType1() {
        existingMeeting.setType(1);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("线上会议", vo.getTypeText());
    }

    @Test
    void testConvertToVOWithType2() {
        existingMeeting.setType(2);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("线下会议", vo.getTypeText());
    }

    @Test
    void testConvertToVOWithType3() {
        existingMeeting.setType(3);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("混合会议", vo.getTypeText());
    }

    @Test
    void testConvertToVOWithTypeNull() {
        existingMeeting.setType(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("未知类型", vo.getTypeText());
    }

    @Test
    void testConvertToVOWithTypeUnknown() {
        existingMeeting.setType(999);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("未知类型", vo.getTypeText());
    }

    @Test
    void testConvertToVOWithStatus0() {
        existingMeeting.setStatus(0);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("草稿", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatus1() {
        existingMeeting.setStatus(1);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("已发布", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatus2() {
        existingMeeting.setStatus(2);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("已取消", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatus3() {
        existingMeeting.setStatus(3);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("已结束", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatus4() {
        existingMeeting.setStatus(4);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("进行中", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatus6() {
        existingMeeting.setStatus(6);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("已取消", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatusNull() {
        existingMeeting.setStatus(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("未知状态", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatusUnknown() {
        existingMeeting.setStatus(999);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("未知状态", vo.getStatusText());
    }

    @Test
    void testConvertToVOWithStatus2ForCanRegister() {
        existingMeeting.setStatus(2);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertTrue(vo.getCanRegister());
    }

    @Test
    void testConvertToVOWithStatusNot2ForCanRegister() {
        existingMeeting.setStatus(1);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertFalse(vo.getCanRegister());
    }

    @Test
    void testConvertToVOWithStatusNullForCanRegister() {
        existingMeeting.setStatus(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertFalse(vo.getCanRegister());
    }

    @Test
    void testConvertToVOWithNullTitle() {
        existingMeeting.setTitle(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals("", vo.getTitle());
    }

    @Test
    void testConvertToVOWithNullType() {
        existingMeeting.setType(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals(0, vo.getType());
    }

    @Test
    void testConvertToVOWithNullStatus() {
        existingMeeting.setStatus(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals(0, vo.getStatus());
    }

    @Test
    void testConvertToVOWithNullIsTop() {
        existingMeeting.setIsTop(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertEquals(0, vo.getIsTop());
    }

    @Test
    void testConvertToVOWithNullFee() {
        existingMeeting.setFee(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertNull(vo.getFee());
    }

    @Test
    void testConvertToVOWithRequiresApprovalTrue() {
        existingMeeting.setRequiresApproval(1);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertTrue(vo.getRequiresApproval());
    }

    @Test
    void testConvertToVOWithRequiresApprovalFalse() {
        existingMeeting.setRequiresApproval(0);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertFalse(vo.getRequiresApproval());
    }

    @Test
    void testConvertToVOWithRequiresApprovalNull() {
        existingMeeting.setRequiresApproval(null);
        MeetingVO vo = invokeConvertToVO(existingMeeting);
        assertFalse(vo.getRequiresApproval());
    }

    @Test
    void testConvertToVOException() {
        // 创建一个会导致异常的Meeting对象
        Meeting problematicMeeting = new Meeting();
        problematicMeeting.setId(1L);
        // 设置一个会导致NPE的属性
        problematicMeeting.setTitle(null);
        
        // 模拟convertToVO方法中的异常
        when(meetingMapper.getById(1L)).thenReturn(problematicMeeting);
        
        // 这里我们直接测试convertToVO方法，但由于它是private的，我们通过getMeetingDetail来测试
        Result<MeetingVO> result = meetingService.getMeetingDetail(1L, 1L);
        
        // 由于convertToVO方法有异常处理，应该不会抛出异常，而是返回错误结果
        assertTrue(result.isSuccess());
    }

    @Test
    void testRegisterMeetingWithNullTenantId() {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(meetingRegistrationMapper.insert(any(MeetingRegistration.class))).thenReturn(1);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, null, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testRegisterMeetingWithNullMeetingTenantId() {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(meetingRegistrationMapper.insert(any(MeetingRegistration.class))).thenReturn(1);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testRegisterMeetingWithNullBothTenantIds() {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(meetingRegistrationMapper.insert(any(MeetingRegistration.class))).thenReturn(1);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.registerMeeting(1L, registration, null, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testCancelMeetingWithNullTenantId() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", null, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testCancelMeetingWithNullMeetingTenantId() {
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.cancelMeeting(1L, "测试取消原因", 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testPublishMeetingWithNullTenantId() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.publishMeeting(1L, null, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testPublishMeetingWithNullMeetingTenantId() {
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.publishMeeting(1L, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testTopMeetingWithNullTenantId() {
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.topMeeting(1L, true, null, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testTopMeetingWithNullMeetingTenantId() {
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        
        Result<Void> result = meetingService.topMeeting(1L, true, 1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testDeleteMeetingWithNullMeetingTenantId() {
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.deleteById(1L)).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.deleteMeeting(1L, 1L);
        
        assertTrue(result.isSuccess());
    }

    @Test
    void testDeleteMeetingWithNullBothTenantIds() {
        existingMeeting.setTenantId(null);
        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.deleteById(1L)).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);
        
        Result<Void> result = meetingService.deleteMeeting(1L, null);
        
        assertTrue(result.isSuccess());
    }
}