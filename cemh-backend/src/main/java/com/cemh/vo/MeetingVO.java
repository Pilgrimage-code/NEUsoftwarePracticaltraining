package com.cemh.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 会议视图对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingVO {
    
    /**
     * 会议ID
     */
    private Long id;
    
    /**
     * 会议标题
     */
    private String title;
    
    /**
     * 会议描述
     */
    private String description;
    
    /**
     * 会议类型：online-线上，offline-线下，hybrid-混合
     */
    private Integer type;
    
    /**
     * 会议类型文本
     */
    private String typeText;
    
    /**
     * 会议状态：draft-草稿，published-已发布，ongoing-进行中，completed-已结束，cancelled-已取消
     */
    private Integer status;
    
    /**
     * 会议状态文本
     */
    private String statusText;
    
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-d'T'HH:mm:ss.SSSX")
    private OffsetDateTime startTime;
    
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-d'T'HH:mm:ss.SSSX")
    private OffsetDateTime endTime;
    
    /**
     * 会议地点
     */
    private String location;
    
    /**
     * 最大参与人数
     */
    private Integer maxParticipants;
    
    /**
     * 当前报名人数
     */
    private Integer currentParticipants;
    
    /**
     * 报名截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDeadline;
    
    /**
     * 是否需要审核
     */
    private Boolean requiresApproval;
    
    /**
     * 参会费用
     */
    private Double fee;
    
    /**
     * 参会要求
     */
    private String requirements;
    
    /**
     * 会议标签
     */
    private String tags;
    
    /**
     * 会议封面图片
     */
    private String coverImage;
    
    /**
     * 备注信息
     */
    private String remarks;
    
    /**
     * 创建人ID
     */
    private Long createBy;
    
    /**
     * 创建人姓名
     */
    private String createByName;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private Long tenantId;
    
    /**
     * 是否可以报名
     */
    private Boolean canRegister;
    
    /**
     * 是否已报名
     */
    private Boolean hasRegistered;
    
    /**
     * 会议时长（分钟）
     */
    private Long duration;
    
    /**
     * 距离开始时间（分钟）
     */
    private Long timeToStart;

    /**
     * 是否顶置
     */
    private Integer isTop;
}

