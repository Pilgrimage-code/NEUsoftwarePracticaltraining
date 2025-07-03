package com.cemh.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "会议材料")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingMaterial {
    private static final long serialVersionUID = 1L;

    @Schema(description = "材料ID")
    private Integer id;

    @Schema(description = "材料URL")
    private String url;

    @Schema(description = "会议ID")
    private Long meetingId;

    @Schema(description = "材料名称")
    private String name;
}
