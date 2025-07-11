package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 资讯Mapper接口
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2025-6-28
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

    /**
     * 查询已发布的资讯列表
     */
    @Select("<script>" +
            "SELECT n.*, u.nickname as author_name " +
            "FROM news n " +
            "LEFT JOIN sys_user u ON n.author_id = u.id " +
            "WHERE n.tenant_id = #{tenantId} AND n.status = 1 AND n.deleted = 0 " +
            "<if test='category != null and category != \"\"'>" +
            "AND n.category = #{category} " +
            "</if>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (n.title LIKE CONCAT('%', #{keyword}, '%') OR n.summary LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY n.is_top DESC, n.publish_time DESC" +
            "</script>")
    List<News> selectPublishedNews(@Param("tenantId") Long tenantId,
                                 @Param("category") String category,
                                 @Param("keyword") String keyword);

    /**
     * 查询热门资讯
     */
    @Select("SELECT n.*, u.nickname as author_name " +
            "FROM news n " +
            "LEFT JOIN sys_user u ON n.author_id = u.id " +
            "WHERE n.tenant_id = #{tenantId} AND n.status = 1 AND n.deleted = 0 " +
            "ORDER BY n.view_count DESC, n.publish_time DESC " +
            "LIMIT #{limit}")
    List<News> selectHotNews(@Param("tenantId") Long tenantId, @Param("limit") Integer limit);

    /**
     * 查询最新资讯
     */
    @Select("SELECT n.*, u.nickname as author_name " +
            "FROM news n " +
            "LEFT JOIN sys_user u ON n.author_id = u.id " +
            "WHERE n.tenant_id = #{tenantId} AND n.status = 1 AND n.deleted = 0 " +
            "ORDER BY n.publish_time DESC " +
            "LIMIT #{limit}")
    List<News> selectLatestNews(@Param("tenantId") Long tenantId, @Param("limit") Integer limit);

    /**
     * 更新浏览量
     */
    @Update("UPDATE news SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);

    /**
     * 更新置顶状态
     */
    @Update("UPDATE news SET is_top = #{isTop}, update_time = NOW() WHERE id = #{id}")
    int updateTopStatus(@Param("id") Long id, @Param("isTop") Integer isTop);

    /**
     * 更新发布状态
     */
    @Update("UPDATE news SET status = #{status}, " +
            "publish_time = CASE WHEN #{status} = 1 THEN NOW() ELSE publish_time END, " +
            "update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 统计资讯数量
     */
    @Select("SELECT COUNT(*) FROM news WHERE tenant_id = #{tenantId} AND status = #{status} AND deleted = 0")
    Long countByStatus(@Param("tenantId") Long tenantId, @Param("status") Integer status);

    /**
     * 查询资讯分类列表
     */
    @Select("SELECT DISTINCT category FROM news " +
            "WHERE tenant_id = #{tenantId} AND status = 1 AND deleted = 0 " +
            "AND category IS NOT NULL AND category != '' " +
            "ORDER BY category")
    List<String> selectCategories(@Param("tenantId") Long tenantId);
}

