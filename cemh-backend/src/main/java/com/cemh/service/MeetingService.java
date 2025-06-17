package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.MeetingDTO;
import com.cemh.dto.MeetingQueryDTO;
import com.cemh.entity.Meeting;
import com.cemh.vo.MeetingVO;

/**
 * 会议服务接口
 */
public interface MeetingService {
    
    /**
     * 创建会议
     * @param meetingDTO 会议数据传输对象
     * @return 操作结果
     */
    Result<Void> createMeeting(MeetingDTO meetingDTO);
    
    /**
     * 更新会议
     * @param meetingDTO 会议数据传输对象
     * @return 操作结果
     */
    Result<Void> updateMeeting(MeetingDTO meetingDTO);
    
    /**
     * 删除会议
     * @param id 会议ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> deleteMeeting(Long id, Long tenantId);
    
    /**
     * 获取会议详情
     * @param id 会议ID
     * @param tenantId 租户ID
     * @return 会议详情
     */
    Result<MeetingVO> getMeetingDetail(Long id, Long tenantId);
    
    /**
     * 获取会议列表
     * @param queryDTO 查询条件
     * @return 会议列表
     */
    Result<PageResult<MeetingVO>> getMeetingList(MeetingQueryDTO queryDTO);
    
    /**
     * 取消会议
     * @param id 会议ID
     * @param reason 取消原因
     * @param tenantId 租户ID
     * @param userId 操作用户ID
     * @return 操作结果
     */
    Result<Void> cancelMeeting(Long id, String reason, Long tenantId, Long userId);
    
    /**
     * 发布会议
     * @param id 会议ID
     * @param tenantId 租户ID
     * @param userId 操作用户ID
     * @return 操作结果
     */
    Result<Void> publishMeeting(Long id, Long tenantId, Long userId);
    
    /**
     * 置顶会议
     * @param id 会议ID
     * @param isTop 是否置顶
     * @param tenantId 租户ID
     * @param userId 操作用户ID
     * @return 操作结果
     */
    Result<Void> topMeeting(Long id, Boolean isTop, Long tenantId, Long userId);
}

