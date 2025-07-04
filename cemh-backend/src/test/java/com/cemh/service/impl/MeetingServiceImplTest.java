package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.MeetingDTO;
import com.cemh.dto.MeetingQueryDTO;
import com.cemh.entity.Meeting;
import com.cemh.entity.MeetingMaterial;
import com.cemh.entity.MeetingRegistration;
import com.cemh.mapper.MeetingMapper;
import com.cemh.mapper.MeetingMaterialMapper;
import com.cemh.mapper.MeetingRegistrationMapper;
import com.cemh.vo.MeetingVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingServiceImplTest {

    @InjectMocks
    private MeetingServiceImpl meetingService;

    @Mock
    private MeetingMapper meetingMapper;

    @Mock
    private MeetingMaterialMapper meetingMaterialMapper;

    @Mock
    private MeetingRegistrationMapper meetingRegistrationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMeetingSuccess() {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setTitle("Test Meeting");
        meetingDTO.setStartTime(OffsetDateTime.of(LocalDateTime.now().plusHours(1), ZoneOffset.UTC));
        meetingDTO.setEndTime(OffsetDateTime.of(LocalDateTime.now().plusHours(2), ZoneOffset.UTC));
        meetingDTO.setMaterials(Collections.singletonList(new MeetingMaterial()));

        when(meetingMapper.insert(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.insert(any(MeetingMaterial.class))).thenReturn(1);

        Result<Void> result = meetingService.createMeeting(meetingDTO);
        assertTrue(result.isSuccess());
        verify(meetingMapper, times(1)).insert(any(Meeting.class));
        verify(meetingMaterialMapper, times(1)).insert(any(MeetingMaterial.class));
    }

    @Test
    void testCreateMeetingInvalidTime() {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setTitle("Test Meeting");
        meetingDTO.setStartTime(OffsetDateTime.of(LocalDateTime.now().plusHours(2), ZoneOffset.UTC));
        meetingDTO.setEndTime(OffsetDateTime.of(LocalDateTime.now().plusHours(1), ZoneOffset.UTC));

        Result<Void> result = meetingService.createMeeting(meetingDTO);
        assertFalse(result.isSuccess());
        assertEquals("开始时间不能晚于结束时间", result.getMessage());
        verify(meetingMapper, never()).insert(any(Meeting.class));
    }

    @Test
    void testUpdateMeetingSuccess() {
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(1L);
        meetingDTO.setTitle("Updated Meeting");
        meetingDTO.setStartTime(OffsetDateTime.of(LocalDateTime.now().plusHours(1), ZoneOffset.UTC));
        meetingDTO.setEndTime(OffsetDateTime.of(LocalDateTime.now().plusHours(2), ZoneOffset.UTC));

        Meeting existingMeeting = new Meeting();
        existingMeeting.setId(1L);

        when(meetingMapper.selectById(1L)).thenReturn(existingMeeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(1L)).thenReturn(1);

        Result<Void> result = meetingService.updateMeeting(meetingDTO);
        assertTrue(result.isSuccess());
        verify(meetingMapper, times(1)).updateById(any(Meeting.class));
        verify(meetingMaterialMapper, times(1)).deleteByMeetingId(1L);
    }

    @Test
    void testDeleteMeetingSuccess() {
        Long meetingId = 1L;
        Long tenantId = 1L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);

        when(meetingMapper.selectById(meetingId)).thenReturn(meeting);
        when(meetingMapper.deleteById(meetingId)).thenReturn(1);
        when(meetingMaterialMapper.deleteByMeetingId(meetingId)).thenReturn(1);

        Result<Void> result = meetingService.deleteMeeting(meetingId, tenantId);
        assertTrue(result.isSuccess());
        verify(meetingMapper, times(1)).deleteById(meetingId);
        verify(meetingMaterialMapper, times(1)).deleteByMeetingId(meetingId);
    }

    @Test
    void testGetMeetingDetailSuccess() {
        Long meetingId = 1L;
        Long tenantId = 1L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);
        meeting.setTitle("Test Meeting");
        meeting.setStartTime(LocalDateTime.now());
        meeting.setEndTime(LocalDateTime.now().plusHours(1));

        when(meetingMapper.getById(meetingId)).thenReturn(meeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);

        Result<MeetingVO> result = meetingService.getMeetingDetail(meetingId, tenantId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals("Test Meeting", result.getData().getTitle());
    }

    @Test
    void testGetMeetingList() {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(10);

        Page<Meeting> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        List<Meeting> meetings = Arrays.asList(new Meeting(), new Meeting());
        IPage<Meeting> iPage = mock(Page.class);
        when(iPage.getRecords()).thenReturn(meetings);
        when(iPage.getTotal()).thenReturn(2L);
        when(iPage.getSize()).thenReturn((long) queryDTO.getSize());
        when(iPage.getCurrent()).thenReturn((long) queryDTO.getPage());
        when(iPage.getPages()).thenReturn(1L);

        when(meetingMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn((Page) iPage);

        Result<PageResult<MeetingVO>> result = meetingService.getMeetingList(queryDTO);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        verify(meetingMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testCancelMeetingSuccess() {
        Long meetingId = 1L;
        Long tenantId = 1L;
        Long userId = 100L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);
        meeting.setStatus(1);

        when(meetingMapper.selectById(meetingId)).thenReturn(meeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);

        Result<Void> result = meetingService.cancelMeeting(meetingId, "Test Reason", tenantId, userId);
        assertTrue(result.isSuccess());
        verify(meetingMapper, times(1)).updateById(any(Meeting.class));
        assertEquals(6, meeting.getStatus());
    }

    @Test
    void testPublishMeetingSuccess() {
        Long meetingId = 1L;
        Long tenantId = 1L;
        Long userId = 100L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);
        meeting.setStatus(0);

        when(meetingMapper.selectById(meetingId)).thenReturn(meeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);

        Result<Void> result = meetingService.publishMeeting(meetingId, tenantId, userId);
        assertTrue(result.isSuccess());
        verify(meetingMapper, times(1)).updateById(any(Meeting.class));
        assertEquals(1, meeting.getStatus());
    }

    @Test
    void testTopMeetingSuccess() {
        Long meetingId = 1L;
        Long tenantId = 1L;
        Long userId = 100L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);
        meeting.setIsTop(0);

        when(meetingMapper.selectById(meetingId)).thenReturn(meeting);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);

        Result<Void> result = meetingService.topMeeting(meetingId, true, tenantId, userId);
        assertTrue(result.isSuccess());
        verify(meetingMapper, times(1)).updateById(any(Meeting.class));
        assertEquals(1, meeting.getIsTop());
    }

    @Test
    void testRegisterMeetingSuccess() {
        Long meetingId = 1L;
        Long userId = 100L;
        Long tenantId = 1L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);
        meeting.setMaxParticipants(10);
        meeting.setCurrentParticipants(0);

        MeetingRegistration registration = new MeetingRegistration();

        when(meetingMapper.selectById(meetingId)).thenReturn(meeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(meetingRegistrationMapper.insert(any(MeetingRegistration.class))).thenReturn(1);
        when(meetingMapper.updateById(any(Meeting.class))).thenReturn(1);

        Result<Void> result = meetingService.registerMeeting(meetingId, registration, tenantId, userId);
        assertTrue(result.isSuccess());
        verify(meetingRegistrationMapper, times(1)).insert(any(MeetingRegistration.class));
        verify(meetingMapper, times(1)).updateById(any(Meeting.class));
        assertEquals(1, meeting.getCurrentParticipants());
    }

    @Test
    void testRegisterMeetingAlreadyRegistered() {
        Long meetingId = 1L;
        Long userId = 100L;
        Long tenantId = 1L;

        Meeting meeting = new Meeting();
        meeting.setId(meetingId);
        meeting.setTenantId(tenantId);
        meeting.setMaxParticipants(10);
        meeting.setCurrentParticipants(0);

        MeetingRegistration existingRegistration = new MeetingRegistration();
        existingRegistration.setMeetingId(meetingId);
        existingRegistration.setUserId(userId);

        MeetingRegistration registration = new MeetingRegistration();

        when(meetingMapper.selectById(meetingId)).thenReturn(meeting);
        when(meetingRegistrationMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existingRegistration);

        Result<Void> result = meetingService.registerMeeting(meetingId, registration, tenantId, userId);
        assertFalse(result.isSuccess());
        assertEquals("您已报名该会议", result.getMessage());
        verify(meetingRegistrationMapper, never()).insert(any(MeetingRegistration.class));
        verify(meetingMapper, never()).updateById(any(Meeting.class));
    }
}


