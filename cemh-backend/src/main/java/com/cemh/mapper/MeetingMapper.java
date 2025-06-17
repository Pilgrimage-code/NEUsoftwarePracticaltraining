package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.entity.Meeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议Mapper接口
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@Mapper
public interface MeetingMapper extends BaseMapper<Meeting> {

    /**
     * 分页查询会议列表
     */
    @Select("SELECT m.*, u.nickname as creator_name, d.name as dept_name " +
            "FROM meeting m " +
            "LEFT JOIN sys_user u ON m.creator_id = u.id " +
            "LEFT JOIN sys_dept d ON m.dept_id = d.id " +
            "WHERE m.tenant_id = #{tenantId} AND m.deleted = 0 " +
            "ORDER BY m.is_top DESC, m.create_time DESC")
    IPage<Meeting> selectMeetingPage(Page<Meeting> page, 
                                   @Param("tenantId") Long tenantId,
                                   @Param("title") String title,
                                   @Param("status") Integer status,
                                   @Param("type") Integer type,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户参与的会议
     */
    @Select("SELECT m.*, u.nickname as creator_name " +
            "FROM meeting m " +
            "LEFT JOIN sys_user u ON m.creator_id = u.id " +
            "LEFT JOIN meeting_registration mr ON m.id = mr.meeting_id " +
            "WHERE mr.user_id = #{userId} AND mr.status = 1 " +
            "AND m.deleted = 0 AND mr.deleted = 0 " +
            "ORDER BY m.start_time ASC")
    List<Meeting> selectUserMeetings(@Param("userId") Long userId);

    /**
     * 查询即将开始的会议
     */
    @Select("SELECT m.*, u.nickname as creator_name " +
            "FROM meeting m " +
            "LEFT JOIN sys_user u ON m.creator_id = u.id " +
            "WHERE m.tenant_id = #{tenantId} " +
            "AND m.status = 1 " +
            "AND m.start_time BETWEEN #{startTime} AND #{endTime} " +
            "AND m.deleted = 0 " +
            "ORDER BY m.start_time ASC " +
            "LIMIT #{limit}")
    List<Meeting> selectUpcomingMeetings(@Param("tenantId") Long tenantId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       @Param("limit") Integer limit);

    /**
     * 更新会议状态
     */
    @Update("UPDATE meeting SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新会议置顶状态
     */
    @Update("UPDATE meeting SET is_top = #{isTop}, update_time = NOW() WHERE id = #{id}")
    int updateTopStatus(@Param("id") Long id, @Param("isTop") Integer isTop);

    /**
     * 统计会议数量
     */
    @Select("SELECT COUNT(*) FROM meeting WHERE tenant_id = #{tenantId} AND status = #{status} AND deleted = 0")
    Long countByStatus(@Param("tenantId") Long tenantId, @Param("status") Integer status);

    /**
     * 查询热门会议（按报名人数排序）
     */
    @Select("SELECT m.*, u.nickname as creator_name, " +
            "COALESCE(mr.registration_count, 0) as registration_count " +
            "FROM meeting m " +
            "LEFT JOIN sys_user u ON m.creator_id = u.id " +
            "LEFT JOIN (" +
            "    SELECT meeting_id, COUNT(*) as registration_count " +
            "    FROM meeting_registration " +
            "    WHERE status = 1 AND deleted = 0 " +
            "    GROUP BY meeting_id" +
            ") mr ON m.id = mr.meeting_id " +
            "WHERE m.tenant_id = #{tenantId} AND m.status = 1 AND m.deleted = 0 " +
            "ORDER BY registration_count DESC, m.create_time DESC " +
            "LIMIT #{limit}")
    List<Meeting> selectPopularMeetings(@Param("tenantId") Long tenantId, @Param("limit") Integer limit);
}

