package com.cemh.dto;

import com.cemh.entity.MeetingMaterial;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * 会议数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDTO {

    /**
     * 会议ID（编辑时使用）
     */
    private Long id;

    /**
     * 会议标题
     */
    @NotBlank(message = "会议标题不能为空")
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
     * 会议状态：draft-草稿，published-已发布，ongoing-进行中，completed-已结束，cancelled-已取消
     */
    private Integer status;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
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
     * 报名截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime registrationDeadline;

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
    private String remark;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 会议材料列表
     */
    private List<MeetingMaterial> materials;
}

