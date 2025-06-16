package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.MeetingDTO;
import com.cemh.dto.MeetingQueryDTO;
import com.cemh.service.MeetingService;
import com.cemh.vo.MeetingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 会议管理控制器
 */
@Tag(name = "会议管理", description = "会议相关接口")
@RestController
@RequestMapping("/api/meeting")
@CrossOrigin(origins = "*")
public class MeetingController {
    
    @Autowired
    private MeetingService meetingService;
    
    @Operation(summary = "创建会议", description = "创建新的会议")
    @PostMapping("/create")
    public Result<Void> createMeeting(@Valid @RequestBody MeetingDTO meetingDTO,
                                      @RequestHeader("X-Tenant-Id") Long tenantId,
                                      @RequestHeader("X-User-Id") Long userId) {
        meetingDTO.setTenantId(tenantId);
        return meetingService.createMeeting(meetingDTO);
    }
    
    @Operation(summary = "更新会议", description = "更新会议信息")
    @PutMapping("/update")
    public Result<Void> updateMeeting(@Valid @RequestBody MeetingDTO meetingDTO,
                                      @RequestHeader("X-Tenant-Id") Long tenantId,
                                      @RequestHeader("X-User-Id") Long userId) {
        meetingDTO.setTenantId(tenantId);
        return meetingService.updateMeeting(meetingDTO);
    }
    
    @Operation(summary = "删除会议", description = "删除指定会议")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteMeeting(@Parameter(description = "会议ID") @PathVariable Long id,
                                      @RequestHeader("X-Tenant-Id") Long tenantId) {
        return meetingService.deleteMeeting(id, tenantId);
    }
    
    @Operation(summary = "获取会议详情", description = "根据ID获取会议详细信息")
    @GetMapping("/detail/{id}")
    public Result<MeetingVO> getMeetingDetail(@Parameter(description = "会议ID") @PathVariable Long id,
                                              @RequestHeader("X-Tenant-Id") Long tenantId) {
        return meetingService.getMeetingDetail(id, tenantId);
    }
    
    @Operation(summary = "获取会议列表", description = "分页查询会议列表")
    @PostMapping("/list")
    public Result<PageResult<MeetingVO>> getMeetingList(@RequestBody MeetingQueryDTO queryDTO,
                                                        @RequestHeader("X-Tenant-Id") Long tenantId) {
        queryDTO.setTenantId(tenantId);
        return meetingService.getMeetingList(queryDTO);
    }
    
    @Operation(summary = "取消会议", description = "取消指定会议")
    @PutMapping("/cancel/{id}")
    public Result<Void> cancelMeeting(@Parameter(description = "会议ID") @PathVariable Long id,
                                      @RequestParam String reason,
                                      @RequestHeader("X-Tenant-Id") Long tenantId,
                                      @RequestHeader("X-User-Id") Long userId) {
        return meetingService.cancelMeeting(id, reason, tenantId, userId);
    }
    
    @Operation(summary = "发布会议", description = "发布指定会议")
    @PutMapping("/publish/{id}")
    public Result<Void> publishMeeting(@Parameter(description = "会议ID") @PathVariable Long id,
                                       @RequestHeader("X-Tenant-Id") Long tenantId,
                                       @RequestHeader("X-User-Id") Long userId) {
        return meetingService.publishMeeting(id, tenantId, userId);
    }
    
    @Operation(summary = "置顶会议", description = "设置会议置顶状态")
    @PutMapping("/top/{id}")
    public Result<Void> topMeeting(@Parameter(description = "会议ID") @PathVariable Long id,
                                   @RequestParam Boolean isTop,
                                   @RequestHeader("X-Tenant-Id") Long tenantId,
                                   @RequestHeader("X-User-Id") Long userId) {
        return meetingService.topMeeting(id, isTop, tenantId, userId);
    }
}

