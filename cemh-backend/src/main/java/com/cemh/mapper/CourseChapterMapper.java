package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.CourseChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程章节Mapper接口
 */
public interface CourseChapterMapper extends BaseMapper<CourseChapter> {
    
    /**
     * 查询课程章节列表
     * @param courseId 课程ID
     * @return 章节列表
     */
    List<CourseChapter> selectChaptersByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 批量删除课程章节
     * @param courseIds 课程ID列表
     * @return 影响行数
     */
    int batchDeleteByCourseIds(@Param("courseIds") List<Long> courseIds);
    
    /**
     * 获取最大章节排序值
     * @param courseId 课程ID
     * @return 最大排序值
     */
    Integer selectMaxOrder(@Param("courseId") Long courseId);
    
    /**
     * 获取当前最大的章节ID
     * @return 最大ID
     */
    Long selectMaxId();
}