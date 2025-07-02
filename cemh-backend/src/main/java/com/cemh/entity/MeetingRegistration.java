package com.cemh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 会议报名实体类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Schema(description = "会议报名信息")
@TableName("meeting_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRegistration extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会议ID")
    @TableField("meeting_id")
    @NotNull(message = "会议ID不能为空")
    private Long meetingId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "报名人姓名")
    @TableField("participant_name")
    @NotBlank(message = "报名人姓名不能为空")
    @Size(max = 50, message = "报名人姓名长度不能超过50个字符")
    private String participantName;

    @Schema(description = "报名人手机号")
    @TableField("participant_phone")
    @NotBlank(message = "报名人手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String participantPhone;

    @Schema(description = "报名人邮箱")
    @TableField("participant_email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String participantEmail;

    @Schema(description = "报名人性别")
    @TableField("participant_gender")
    private Integer participantGender;

    @Schema(description = "报名人单位")
    @TableField("participant_company")
    private String participantCompany;

    @Schema(description = "报名状态（0：待审核，1：已通过，2：已拒绝，3：已取消）")
    @TableField("status")
    private Integer status;

    @Schema(description = "签到状态（0：未签到，1：已签到）")
    @TableField("check_in_status")
    private Integer checkInStatus;

    @Schema(description = "到达方式")
    @TableField("arrival_method")
    private String arrivalMethod;

    @Schema(description = "到达车次")
    @TableField("arrival_train")
    private String arrivalTrain;

    @Schema(description = "到达时间")
    @TableField("arrival_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivalTime;


    // 非数据库字段
    @Schema(description = "会议标题")
    @TableField(exist = false)
    private String meetingTitle;

    @Schema(description = "审核人姓名")
    @TableField(exist = false)
    private String approvalByName;
}

