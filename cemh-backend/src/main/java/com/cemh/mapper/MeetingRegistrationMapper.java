package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.MeetingRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 会议报名Mapper接口
 */
@Mapper
public interface MeetingRegistrationMapper extends BaseMapper<MeetingRegistration> {

    /**
     * 查询用户是否已报名会议
     */
    @Select("SELECT * FROM meeting_registration " +
            "WHERE meeting_id = #{meetingId} AND user_id = #{userId} AND deleted = 0")
    MeetingRegistration selectByMeetingAndUser(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    /**
     * 查询会议报名列表
     */
    @Select("SELECT mr.*, u.nickname as user_name, u.phone, u.email, d.name as dept_name " +
            "FROM meeting_registration mr " +
            "LEFT JOIN sys_user u ON mr.user_id = u.id " +
            "LEFT JOIN sys_dept d ON u.dept_id = d.id " +
            "WHERE mr.meeting_id = #{meetingId} AND mr.deleted = 0 " +
            "ORDER BY mr.create_time DESC")
    List<MeetingRegistration> selectByMeetingId(@Param("meetingId") Long meetingId);

    /**
     * 统计会议报名人数
     */
    @Select("SELECT COUNT(*) FROM meeting_registration " +
            "WHERE meeting_id = #{meetingId} AND status = 1 AND deleted = 0")
    Long countByMeetingId(@Param("meetingId") Long meetingId);

    /**
     * 更新报名状态
     */
    @Update("UPDATE meeting_registration SET status = #{status}, update_time = NOW() " +
            "WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 批量更新报名状态
     */
    @Update("<script>" +
            "UPDATE meeting_registration SET status = #{status}, update_time = NOW() " +
            "WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询用户报名的会议列表
     */
    @Select("SELECT mr.*, m.title as meeting_title, m.start_time, m.end_time, m.location " +
            "FROM meeting_registration mr " +
            "LEFT JOIN meeting m ON mr.meeting_id = m.id " +
            "WHERE mr.user_id = #{userId} AND mr.deleted = 0 " +
            "ORDER BY mr.create_time DESC")
    List<MeetingRegistration> selectByUserId(@Param("userId") Long userId);
}

