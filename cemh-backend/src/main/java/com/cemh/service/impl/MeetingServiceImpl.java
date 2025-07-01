package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.MeetingDTO;
import com.cemh.dto.MeetingQueryDTO;
import com.cemh.entity.Meeting;
import com.cemh.mapper.MeetingMapper;
import com.cemh.service.MeetingService;
import com.cemh.vo.MeetingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议服务实现类
 */
@Slf4j
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    
    @Autowired
    private MeetingMapper meetingMapper;
    
    @Override
    public Result<Void> createMeeting(MeetingDTO meetingDTO) {
        try {
            // 数据验证
            if (meetingDTO.getStartTime().isAfter(meetingDTO.getEndTime())) {
                return Result.error("开始时间不能晚于结束时间");
            }
            
            // DTO转实体
            Meeting meeting = new Meeting();
            BeanUtils.copyProperties(meetingDTO, meeting);
            
            // 设置默认值
            meeting.setStatus(0); // 草稿状态
            meeting.setCurrentParticipants(0);
            meeting.setIsTop(0);

            // 保存到数据库
            int result = meetingMapper.insert(meeting);

            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("创建会议失败");
            }
        } catch (Exception e) {
            return Result.error("创建会议异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> updateMeeting(MeetingDTO meetingDTO) {
        try {
            // 检查会议是否存在
            Meeting existingMeeting = meetingMapper.selectById(meetingDTO.getId());
            if (existingMeeting == null) {
                return Result.error("会议不存在");
            }
            
            // 数据验证
            if (meetingDTO.getStartTime().isAfter(meetingDTO.getEndTime())) {
                return Result.error("开始时间不能晚于结束时间");
            }
            
            // DTO转实体
            Meeting meeting = new Meeting();
            meeting.setUpdateTime(LocalDateTime.now());
            BeanUtils.copyProperties(meetingDTO, meeting);
            
            // 更新到数据库
            int result = meetingMapper.updateById(meeting);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("更新会议失败");
            }
        } catch (Exception e) {
            return Result.error("更新会议异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> deleteMeeting(Long id, Long tenantId) {
        try {
            // 检查会议是否存在
            Meeting meeting = meetingMapper.selectById(id);
            if (meeting == null) {
                return Result.error("会议不存在");
            }
            
            // 检查租户权限（非空时才检查）
            if (tenantId != null && meeting.getTenantId() != null && !meeting.getTenantId().equals(tenantId)) {
                return Result.error("无权限删除此会议");
            }
            
            // 删除会议
            int result = meetingMapper.deleteById(id);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("删除会议失败");
            }
        } catch (Exception e) {
            return Result.error("删除会议异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<MeetingVO> getMeetingDetail(Long id, Long tenantId) {
        try {
            // 查询会议
            Meeting meeting = meetingMapper.selectById(id);
            if (meeting == null) {
                return Result.error("会议不存在");
            }
            
            // 检查租户权限（非空时才检查）
            if (tenantId != null && meeting.getTenantId() != null && !meeting.getTenantId().equals(tenantId)) {
                return Result.error("无权限查看此会议");
            }
            
            // 实体转VO
            MeetingVO meetingVO = convertToVO(meeting);
            

            meetingMapper.updateById(meeting);
            
            return Result.success(meetingVO);
        } catch (Exception e) {
            return Result.error("获取会议详情异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<PageResult<MeetingVO>> getMeetingList(MeetingQueryDTO queryDTO) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<Meeting> queryWrapper = new LambdaQueryWrapper<>();
            
            // 标题模糊查询
            if (StringUtils.hasText(queryDTO.getTitle())) {
                queryWrapper.like(Meeting::getTitle, queryDTO.getTitle());
            }
            
            // 会议类型
            if (queryDTO.getType() != null) {
                queryWrapper.eq(Meeting::getType, queryDTO.getType());
            }
            
            // 会议状态
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(Meeting::getStatus, queryDTO.getStatus());
            }
            
            // 创建人
            if (queryDTO.getCreateBy() != null) {
                queryWrapper.eq(Meeting::getCreateBy, queryDTO.getCreateBy());
            }
            
            // 租户ID - 只有在tenantId不为空时才添加条件
            if (queryDTO.getTenantId() != null) {
                queryWrapper.eq(Meeting::getTenantId, queryDTO.getTenantId());
            }
            
            // 开始时间范围
            if (queryDTO.getStartTimeBegin() != null) {
                queryWrapper.ge(Meeting::getStartTime, queryDTO.getStartTimeBegin());
            }
            if (queryDTO.getStartTimeEnd() != null) {
                queryWrapper.le(Meeting::getStartTime, queryDTO.getStartTimeEnd());
            }
            
            // 创建时间范围
            if (queryDTO.getCreateTimeBegin() != null) {
                queryWrapper.ge(Meeting::getCreateTime, queryDTO.getCreateTimeBegin());
            }
            if (queryDTO.getCreateTimeEnd() != null) {
                queryWrapper.le(Meeting::getCreateTime, queryDTO.getCreateTimeEnd());
            }

            // 先按置顶状态降序排序，再按时间排序

            queryWrapper.orderByDesc(Meeting::getIsTop);
            if (!"asc".equals(queryDTO.getSortOrder())) {
                queryWrapper.orderByAsc(Meeting::getStartTime);
            } else {
                queryWrapper.orderByDesc(Meeting::getStartTime);
            }

            // 分页查询
            Page<Meeting> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
            IPage<Meeting> meetingPage = meetingMapper.selectPage(page, queryWrapper);
            
            // 实体转VO
            List<MeetingVO> meetingVOList = meetingPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
            
            // 构建分页结果
            PageResult<MeetingVO> pageResult = new PageResult<>();
            pageResult.setRecords(meetingVOList);
            pageResult.setTotal(meetingPage.getTotal());
            pageResult.setSize(meetingPage.getSize());
            pageResult.setCurrent(meetingPage.getCurrent());
            pageResult.setPages(meetingPage.getPages());
            
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("获取会议列表异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> cancelMeeting(Long id, String reason, Long tenantId, Long userId) {
        try {
            // 检查会议是否存在
            Meeting meeting = meetingMapper.selectById(id);
            if (meeting == null) {
                return Result.error("会议不存在");
            }
            
            // 检查租户权限（非空时才检查）
            if (tenantId != null && meeting.getTenantId() != null && !meeting.getTenantId().equals(tenantId)) {
                return Result.error("无权限取消此会议");
            }
            
            // 更新会议状态为已取消
            meeting.setStatus(6); // 已取消
            meeting.setRemark(reason);
            meeting.setUpdateBy(userId);
            meeting.setUpdateTime(LocalDateTime.now());
            
            int result = meetingMapper.updateById(meeting);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("取消会议失败");
            }
        } catch (Exception e) {
            return Result.error("取消会议异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> publishMeeting(Long id, Long tenantId, Long userId) {
        try {
            // 检查会议是否存在
            Meeting meeting = meetingMapper.selectById(id);
            if (meeting == null) {
                return Result.error("会议不存在");
            }
            
            // 检查租户权限（非空时才检查）
            if (tenantId != null && meeting.getTenantId() != null && !meeting.getTenantId().equals(tenantId)) {
                return Result.error("无权限发布此会议");
            }
            
            // 更新会议状态为已发布
            meeting.setStatus(1); // 已发布
            meeting.setUpdateBy(userId);
            meeting.setUpdateTime(LocalDateTime.now());
            
            int result = meetingMapper.updateById(meeting);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("发布会议失败");
            }
        } catch (Exception e) {
            return Result.error("发布会议异常：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> topMeeting(Long id, Boolean isTop, Long tenantId, Long userId) {
        try {
            // 检查会议是否存在
            Meeting meeting = meetingMapper.selectById(id);
            if (meeting == null) {
                return Result.error("会议不存在");
            }
            
            // 检查租户权限（非空时才检查）
            if (tenantId != null && meeting.getTenantId() != null && !meeting.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此会议");
            }
            
            // 更新置顶状态
            meeting.setIsTop(isTop ? 1 : 0);
            meeting.setUpdateBy(userId);
            meeting.setUpdateTime(LocalDateTime.now());
            
            int result = meetingMapper.updateById(meeting);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("操作失败");
            }
        } catch (Exception e) {
            return Result.error("操作异常：" + e.getMessage());
        }
    }
    
    /**
     * 实体转VO
     */
    private MeetingVO convertToVO(Meeting meeting) {
        try {
            MeetingVO vo = new MeetingVO();
            
            // 手动复制基本属性，避免日期时间类型转换问题
            vo.setId(meeting.getId());
            vo.setTitle(meeting.getTitle() != null ? meeting.getTitle() : "");
            vo.setDescription(meeting.getDescription());
            vo.setType(meeting.getType() != null ? meeting.getType() : 0);
            vo.setStatus(meeting.getStatus() != null ? meeting.getStatus() : 0);
            vo.setLocation(meeting.getLocation());
            vo.setMaxParticipants(meeting.getMaxParticipants());
            vo.setCurrentParticipants(meeting.getCurrentParticipants());
            vo.setFee(meeting.getFee() != null ? meeting.getFee().doubleValue() : null);
            vo.setRequiresApproval(meeting.getRequiresApproval() != null && meeting.getRequiresApproval() == 1);
            vo.setCoverImage(meeting.getCoverImage());
            vo.setRemarks(meeting.getRemark());
            vo.setCreateBy(meeting.getCreateBy());
            vo.setCreateTime(meeting.getCreateTime());
            vo.setUpdateTime(meeting.getUpdateTime());
            vo.setTenantId(meeting.getTenantId());
            vo.setIsTop(meeting.getIsTop() != null ? meeting.getIsTop() : 0);
            vo.setTags(meeting.getTags());
            vo.setRequirements(meeting.getRequirements());
            
            // 设置日期时间字段 - 直接赋值，因为现在都是LocalDateTime类型
            vo.setStartTime(meeting.getStartTime());
            vo.setEndTime(meeting.getEndTime());
            vo.setRegistrationDeadline(meeting.getRegistrationDeadline());
            
            // 设置类型文本
            if (meeting.getType() != null) {
                switch (meeting.getType()) {
                    case 1:
                        vo.setTypeText("线上会议");
                        break;
                    case 2:
                        vo.setTypeText("线下会议");
                        break;
                    case 3:
                        vo.setTypeText("混合会议");
                        break;
                    default:
                        vo.setTypeText("未知类型");
                }
            } else {
                vo.setTypeText("未知类型");
            }

            // 设置状态文本
            if (meeting.getStatus() != null) {
                switch (meeting.getStatus()) {
                    case 0:
                        vo.setStatusText("草稿");
                        break;
                    case 1:
                        vo.setStatusText("已发布");
                        break;
                    case 2:
                        vo.setStatusText("已取消");
                        break;
                    case 3:
                        vo.setStatusText("已结束");
                        break;
                    case 4:
                        vo.setStatusText("进行中");
                        break;
                    case 6:
                        vo.setStatusText("已取消");
                        break;
                    default:
                        vo.setStatusText("未知状态");
                }
            } else {
                vo.setStatusText("未知状态");
            }
            
            // 设置其他计算字段
            vo.setCanRegister(meeting.getStatus() != null && meeting.getStatus() == 2); // 报名中状态可以报名
            vo.setHasRegistered(false); // 需要根据用户查询
            
            return vo;
        } catch (Exception e) {
            // 记录异常，便于排查
            log.error("会议实体转VO异常: {}, 会议ID: {}", e.getMessage(), meeting != null ? meeting.getId() : "null", e);
            throw e;
        }
    }
}

