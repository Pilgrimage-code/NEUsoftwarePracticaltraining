package com.cemh.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.entity.Meeting;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议Mapper接口
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2025-6-28
 */
@Mapper
public interface MeetingMapper extends BaseMapper<Meeting> {

    /**
     * 分页查询会议列表
     */



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

    /**
     * 根据ID删除会议
     * @param id 会议ID
     * @return 删除成功的记录数
     */
    @Delete("DELETE FROM meeting WHERE id = #{id}")
    int deleteById(@Param("id") Long id);


    /**
     * 插入会议记录
     * @param meeting 会议实体
     * @return 插入成功的记录数
     */
    @Insert("INSERT INTO meeting (" +
            "id, title, description, type, status, start_time, end_time, " +
            "location, max_participants, registration_deadline, requires_approval, " +
            "fee, requirements, tags, cover_image, remark, tenant_id" +
            ") VALUES (" +
            "#{id}, #{title}, #{description}, #{type}, #{status}, #{startTime}, #{endTime}, " +
            "#{location}, #{maxParticipants}, #{registrationDeadline}, #{requiresApproval}, " +
            "#{fee}, #{requirements}, #{tags}, #{coverImage}, #{remark}, #{tenantId}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Meeting meeting);

    /**
     * 根据ID查询会议详情
     * @param id 会议ID
     * @return 会议实体
     */
    Meeting getById(Long id);
    
    /**
     * 获取会议表中当前最大的ID值
     * @return 当前最大ID，如果表为空则返回1
     */
    @Select("SELECT COALESCE(MAX(id), 1) FROM meeting")
    Long getMaxId();
}

