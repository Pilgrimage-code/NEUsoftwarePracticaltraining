package com.cemh.mapper;

import com.cemh.entity.MeetingMaterial;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会议材料Mapper
 */
@Mapper
public interface MeetingMaterialMapper {

    /**
     * 插入会议材料
     * @param material 会议材料实体
     */
    @Insert("INSERT INTO meeting_material (url, meeting_id, name) VALUES (#{url}, #{meetingId}, #{name})")
    Integer insert(MeetingMaterial material);

    /**
     * 全部删除会议材料
     * @param meetingId 会议ID
     */
    @Delete("DELETE FROM meeting_material WHERE meeting_id = #{meetingId}")
    Integer deleteByMeetingId(Long meetingId);
}
