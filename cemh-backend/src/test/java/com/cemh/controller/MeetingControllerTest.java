package com.cemh.controller;

import com.cemh.dto.MeetingDTO;
import com.cemh.dto.MeetingQueryDTO;
import com.cemh.entity.MeetingRegistration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.cemh.service.MeetingService;
import com.cemh.common.Result;
import com.cemh.vo.MeetingVO;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyBoolean;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@WebMvcTest(controllers = MeetingController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@org.springframework.test.context.ContextConfiguration(classes = MeetingController.class)
public class MeetingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long tenantId = 1L;
    private Long userId = 1L;

    @BeforeEach
    void setUp() {
        // 可在此初始化测试数据
    }

    @Test
    void testCreateMeeting() throws Exception {
        MeetingDTO dto = new MeetingDTO();
        dto.setTitle("测试会议");
        dto.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        dto.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toLocalDateTime());
        dto.setDescription("测试会议描述");
        dto.setLocation("测试地点");
        dto.setMaxParticipants(100);

        given(meetingService.createMeeting(any(MeetingDTO.class))).willReturn(Result.success());
        mockMvc.perform(post("/api/meeting/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateMeetingWithoutHeaders() throws Exception {
        MeetingDTO dto = new MeetingDTO();
        dto.setTitle("测试会议");
        dto.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        dto.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toLocalDateTime());

        given(meetingService.createMeeting(any(MeetingDTO.class))).willReturn(Result.success());
        mockMvc.perform(post("/api/meeting/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateMeetingFailure() throws Exception {
        MeetingDTO dto = new MeetingDTO();
        dto.setTitle("测试会议");
        dto.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        dto.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toLocalDateTime());

        given(meetingService.createMeeting(any(MeetingDTO.class))).willReturn(Result.error("创建失败"));
        mockMvc.perform(post("/api/meeting/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testUpdateMeeting() throws Exception {
        MeetingDTO dto = new MeetingDTO();
        dto.setId(1L);
        dto.setTitle("更新后的会议");
        dto.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        dto.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toLocalDateTime());

        given(meetingService.updateMeeting(any(MeetingDTO.class))).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateMeetingFailure() throws Exception {
        MeetingDTO dto = new MeetingDTO();
        dto.setId(1L);
        dto.setTitle("更新后的会议");
        dto.setStartTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        dto.setEndTime(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toLocalDateTime());

        given(meetingService.updateMeeting(any(MeetingDTO.class))).willReturn(Result.error("更新失败"));
        mockMvc.perform(put("/api/meeting/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testDeleteMeeting() throws Exception {
        given(meetingService.deleteMeeting(anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(delete("/api/meeting/delete/{id}", 1L)
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMeetingWithoutTenantId() throws Exception {
        given(meetingService.deleteMeeting(anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(delete("/api/meeting/delete/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMeetingFailure() throws Exception {
        given(meetingService.deleteMeeting(anyLong(), anyLong())).willReturn(Result.error("删除失败"));
        mockMvc.perform(delete("/api/meeting/delete/{id}", 1L)
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testGetMeetingDetail() throws Exception {
        MeetingVO meetingVO = new MeetingVO();
        meetingVO.setId(1L);
        meetingVO.setTitle("测试会议");
        
        given(meetingService.getMeetingDetail(anyLong(), anyLong())).willReturn(Result.success(meetingVO));
        mockMvc.perform(get("/api/meeting/detail/{id}", 1L)
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("测试会议"));
    }

    @Test
    void testGetMeetingDetailWithoutTenantId() throws Exception {
        MeetingVO meetingVO = new MeetingVO();
        meetingVO.setId(1L);
        meetingVO.setTitle("测试会议");
        
        given(meetingService.getMeetingDetail(anyLong(), anyLong())).willReturn(Result.success(meetingVO));
        mockMvc.perform(get("/api/meeting/detail/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMeetingDetailFailure() throws Exception {
        given(meetingService.getMeetingDetail(anyLong(), anyLong())).willReturn(Result.error("会议不存在"));
        mockMvc.perform(get("/api/meeting/detail/{id}", 1L)
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testGetMeetingList() throws Exception {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(20);
        
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.success(com.cemh.common.PageResult.empty()));
        mockMvc.perform(post("/api/meeting/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .content(objectMapper.writeValueAsString(queryDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMeetingListWithoutTenantId() throws Exception {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(20);
        
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.success(com.cemh.common.PageResult.empty()));
        mockMvc.perform(post("/api/meeting/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(queryDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMeetingListFailure() throws Exception {
        MeetingQueryDTO queryDTO = new MeetingQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(20);
        
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.error("查询失败"));
        mockMvc.perform(post("/api/meeting/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .content(objectMapper.writeValueAsString(queryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    // 新增：测试GET方式的会议列表查询
    @Test
    void testGetMeetingListByGet() throws Exception {
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.success(com.cemh.common.PageResult.empty()));
        mockMvc.perform(get("/api/meeting/list")
                        .param("title", "测试会议")
                        .param("tags", "重要")
                        .param("status", "1")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .param("page", "1")
                        .param("size", "20")
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMeetingListByGetWithInvalidStatus() throws Exception {
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.success(com.cemh.common.PageResult.empty()));
        mockMvc.perform(get("/api/meeting/list")
                        .param("status", "invalid")
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMeetingListByGetWithoutParams() throws Exception {
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.success(com.cemh.common.PageResult.empty()));
        mockMvc.perform(get("/api/meeting/list")
                        .header("X-Tenant-Id", tenantId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetMeetingListByGetWithoutTenantId() throws Exception {
        given(meetingService.getMeetingList(any(MeetingQueryDTO.class))).willReturn(Result.success(com.cemh.common.PageResult.empty()));
        mockMvc.perform(get("/api/meeting/list"))
                .andExpect(status().isOk());
    }

    // 新增：测试取消会议
    @Test
    void testCancelMeeting() throws Exception {
        given(meetingService.cancelMeeting(anyLong(), any(String.class), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/cancel/{id}", 1L)
                        .param("reason", "时间冲突")
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelMeetingWithoutHeaders() throws Exception {
        given(meetingService.cancelMeeting(anyLong(), any(String.class), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/cancel/{id}", 1L)
                        .param("reason", "时间冲突"))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelMeetingFailure() throws Exception {
        given(meetingService.cancelMeeting(anyLong(), any(String.class), anyLong(), anyLong())).willReturn(Result.error("取消失败"));
        mockMvc.perform(put("/api/meeting/cancel/{id}", 1L)
                        .param("reason", "时间冲突")
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testPublishMeeting() throws Exception {
        given(meetingService.publishMeeting(anyLong(), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/publish/{id}", 1L)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testPublishMeetingWithoutHeaders() throws Exception {
        given(meetingService.publishMeeting(anyLong(), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/publish/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testPublishMeetingFailure() throws Exception {
        given(meetingService.publishMeeting(anyLong(), anyLong(), anyLong())).willReturn(Result.error("发布失败"));
        mockMvc.perform(put("/api/meeting/publish/{id}", 1L)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testTopMeeting() throws Exception {
        given(meetingService.topMeeting(anyLong(), anyBoolean(), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/top/{id}", 1L)
                        .param("isTop", "true")
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testTopMeetingFalse() throws Exception {
        given(meetingService.topMeeting(anyLong(), anyBoolean(), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/top/{id}", 1L)
                        .param("isTop", "false")
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void testTopMeetingWithoutHeaders() throws Exception {
        given(meetingService.topMeeting(anyLong(), anyBoolean(), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(put("/api/meeting/top/{id}", 1L)
                        .param("isTop", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void testTopMeetingFailure() throws Exception {
        given(meetingService.topMeeting(anyLong(), anyBoolean(), anyLong(), anyLong())).willReturn(Result.error("操作失败"));
        mockMvc.perform(put("/api/meeting/top/{id}", 1L)
                        .param("isTop", "true")
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testRegisterMeeting() throws Exception {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        given(meetingService.registerMeeting(anyLong(), any(MeetingRegistration.class), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(post("/api/meeting/register/{meetingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterMeetingWithoutHeaders() throws Exception {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        given(meetingService.registerMeeting(anyLong(), any(MeetingRegistration.class), anyLong(), anyLong())).willReturn(Result.success());
        mockMvc.perform(post("/api/meeting/register/{meetingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterMeetingFailure() throws Exception {
        MeetingRegistration registration = new MeetingRegistration();
        registration.setParticipantName("张三");
        registration.setParticipantPhone("13800000000");
        
        given(meetingService.registerMeeting(anyLong(), any(MeetingRegistration.class), anyLong(), anyLong())).willReturn(Result.error("报名失败"));
        mockMvc.perform(post("/api/meeting/register/{meetingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-Id", tenantId)
                        .header("X-User-Id", userId)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }
}
