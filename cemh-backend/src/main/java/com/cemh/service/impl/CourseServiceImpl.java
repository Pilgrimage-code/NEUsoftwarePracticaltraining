package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.CourseDTO;
import com.cemh.dto.CourseQueryDTO;
import com.cemh.entity.Course;
import com.cemh.entity.CourseCategory;
import com.cemh.entity.CourseChapter;
import com.cemh.entity.LearningRecord;
import com.cemh.mapper.CourseCategoryMapper;
import com.cemh.mapper.CourseChapterMapper;
import com.cemh.mapper.CourseMapper;
import com.cemh.mapper.LearningRecordMapper;
import com.cemh.service.CourseService;
import com.cemh.vo.CourseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 课程服务实现类
 */
@Service
@Transactional
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    
    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private CourseCategoryMapper categoryMapper;
    
    @Autowired
    private CourseChapterMapper chapterMapper;
    
    @Autowired
    private LearningRecordMapper learningRecordMapper;

    @Override
    public Result<Void> createCourse(CourseDTO courseDTO) {
        try {
            // 转换DTO为实体
            Course course = new Course();
            BeanUtils.copyProperties(courseDTO, course);
            
            // 设置默认值 - 根据现有数据库结构适配
            course.setTenantId(1L); // course表确实有tenant_id字段
            course.setCourseCode(generateCourseCode());
            course.setCreateTime(LocalDateTime.now());
            course.setUpdateTime(LocalDateTime.now());
            course.setDeleted(0);
            
            // 设置默认数值
            if (course.getDuration() == null) course.setDuration(0);
            if (course.getMaxStudents() == null) course.setMaxStudents(0);
            if (course.getCurrentStudents() == null) course.setCurrentStudents(0);
            if (course.getViewCount() == null) course.setViewCount(0);
            if (course.getRating() == null) course.setRating(BigDecimal.ZERO);
            if (course.getRatingCount() == null) course.setRatingCount(0);
            if (course.getPrice() == null) course.setPrice(BigDecimal.ZERO);
            if (course.getDifficulty() == null) course.setDifficulty(1);
            if (course.getStatus() == null) course.setStatus(1);
            if (course.getIsFree() == null) course.setIsFree(1);
            if (course.getIsHot() == null) course.setIsHot(0);
            if (course.getCourseOrder() == null) course.setCourseOrder(0);
            
            // 设置审核状态为未审核(1)
            course.setRemark("1");
            
            // 获取当前最大ID，并设置新ID为最大ID+1
            Long maxId = courseMapper.selectMaxId();
            if (maxId != null) {
                course.setId(maxId + 1);
                logger.info("设置新课程ID: {}", course.getId());
            }
            
            courseMapper.insert(course);
            
            // 保存章节
            if (courseDTO.getChapters() != null && !courseDTO.getChapters().isEmpty()) {
                int order = 1;
                for (CourseChapter chapter : courseDTO.getChapters()) {
                    chapter.setCourseId(course.getId());
                    chapter.setChapterOrder(order++);
                    chapter.setCreateTime(LocalDateTime.now());
                    chapter.setUpdateTime(LocalDateTime.now());
                    chapter.setCreateBy(course.getCreateBy());
                    chapterMapper.insert(chapter);
                }
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("创建课程失败", e);
            return Result.error("创建课程失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateCourse(CourseDTO courseDTO) {
        try {
            // 校验课程是否存在
            Course existingCourse = courseMapper.selectById(courseDTO.getId());
            if (existingCourse == null) {
                return Result.error("课程不存在");
            }
            
            Course course = new Course();
            BeanUtils.copyProperties(courseDTO, course);
            
            // 保留原有数据
            course.setTenantId(existingCourse.getTenantId());
            course.setCourseCode(existingCourse.getCourseCode());
            course.setCreateTime(existingCourse.getCreateTime());
            course.setUpdateTime(LocalDateTime.now());
            course.setDeleted(existingCourse.getDeleted());
            
            courseMapper.updateById(course);
            
            // 更新章节
            if (courseDTO.getChapters() != null) {
                // 获取原有章节信息
                LambdaQueryWrapper<CourseChapter> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(CourseChapter::getCourseId, course.getId());
                List<CourseChapter> existingChapters = chapterMapper.selectList(queryWrapper);
                Map<Long, CourseChapter> existingChapterMap = existingChapters.stream()
                    .collect(Collectors.toMap(CourseChapter::getId, chapter -> chapter));
                
                // 更新或添加新章节
                int order = 1;
                for (CourseChapter chapter : courseDTO.getChapters()) {
                    if (chapter.getId() != null && existingChapterMap.containsKey(chapter.getId())) {
                        // 更新现有章节
                        CourseChapter existingChapter = existingChapterMap.get(chapter.getId());
                        chapter.setCourseId(course.getId());
                        chapter.setChapterOrder(order++);
                        chapter.setCreateTime(existingChapter.getCreateTime());
                        chapter.setUpdateTime(LocalDateTime.now());
                        chapter.setCreateBy(existingChapter.getCreateBy());
                        chapterMapper.updateById(chapter);
                        existingChapterMap.remove(chapter.getId());
                    } else {
                        // 添加新章节
                        chapter.setCourseId(course.getId());
                        chapter.setChapterOrder(order++);
                        chapter.setCreateTime(LocalDateTime.now());
                        chapter.setUpdateTime(LocalDateTime.now());
                        chapter.setCreateBy(course.getUpdateBy());
                        chapterMapper.insert(chapter);
                    }
                }
                
                // 删除不再存在的章节
                if (!existingChapterMap.isEmpty()) {
                    chapterMapper.deleteBatchIds(existingChapterMap.keySet());
                }
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("更新课程失败", e);
            return Result.error("更新课程失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteCourse(Long id, Long tenantId) {
        try {
            // 校验课程是否存在
            LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Course::getId, id);
            if (tenantId != null) {
                queryWrapper.eq(Course::getTenantId, tenantId);
            }
            
            Course course = courseMapper.selectOne(queryWrapper);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            // 逻辑删除课程
            courseMapper.deleteById(id);
            
            // 删除关联的章节
            LambdaQueryWrapper<CourseChapter> chapterQueryWrapper = new LambdaQueryWrapper<>();
            chapterQueryWrapper.eq(CourseChapter::getCourseId, id);
            chapterMapper.delete(chapterQueryWrapper);
            
            return Result.success();
        } catch (Exception e) {
            logger.error("删除课程失败", e);
            return Result.error("删除课程失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> batchDeleteCourse(List<Long> ids, Long tenantId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的课程");
            }
            
            // 批量删除课程
            courseMapper.deleteBatchIds(ids);
            
            // 批量删除关联的章节
            chapterMapper.batchDeleteByCourseIds(ids);
            
            // 删除学习记录
            learningRecordMapper.batchDeleteByCourseIds(ids);
            
            return Result.success();
        } catch (Exception e) {
            logger.error("批量删除课程失败", e);
            return Result.error("批量删除课程失败: " + e.getMessage());
        }
    }

    @Override
    public Result<CourseVO> getCourseById(Long id, Long tenantId) {
        try {
            // 查询课程
            Course course = courseMapper.selectCourseDetail(id, tenantId);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            // 转换为VO
            CourseVO courseVO = convertToVO(course);
            
            // 查询章节
            List<CourseChapter> chapters = chapterMapper.selectChaptersByCourseId(id);
            List<Map<String, Object>> chapterList = new ArrayList<>();
            for (CourseChapter chapter : chapters) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", chapter.getId());
                map.put("name", chapter.getChapterName());
                map.put("order", chapter.getChapterOrder());
                chapterList.add(map);
            }
            courseVO.setChapters(chapterList);
            
            return Result.success(courseVO);
        } catch (Exception e) {
            logger.error("获取课程详情失败", e);
            return Result.error("获取课程详情失败: " + e.getMessage());
        }
    }

    @Override
    public Result<PageResult<CourseVO>> getCoursePage(CourseQueryDTO queryDTO, Long tenantId) {
        try {
            Page<Course> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
            
            // 查询课程列表
            IPage<Course> coursePage = courseMapper.selectCoursePage(
                page,
                tenantId,
                queryDTO.getCourseName(),
                queryDTO.getCourseOrder(),
                queryDTO.getCourseAuthor(),
                queryDTO.getStatus(),
                queryDTO.getCategoryId(),
                queryDTO.getRemark()
            );
            
            // 转换为VO列表
            List<CourseVO> courseVOList = coursePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            
            // 构建分页结果
            PageResult<CourseVO> pageResult = new PageResult<>();
            pageResult.setRecords(courseVOList);
            pageResult.setTotal(coursePage.getTotal());
            pageResult.setCurrent(coursePage.getCurrent());
            pageResult.setSize(coursePage.getSize());
            
            return Result.success(pageResult);
        } catch (Exception e) {
            logger.error("获取课程列表失败", e);
            return Result.error("获取课程列表失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Map<String, Object>>> getCourseCategories(Long tenantId) {
        try {
            // 查询课程分类树
            List<Map<String, Object>> categoryTree = categoryMapper.selectCategoryTree(tenantId);
            return Result.success(categoryTree);
        } catch (Exception e) {
            logger.error("获取课程分类失败", e);
            return Result.error("获取课程分类失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CourseVO>> getHotCourses(Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            
            List<Course> courses = courseMapper.selectHotCourses(limit.longValue());
            
            List<CourseVO> vos = courses.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            
            return Result.success(vos);
        } catch (Exception e) {
            logger.error("获取热门课程失败", e);
            return Result.error("获取热门课程失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<CourseVO>> getLatestCourses(Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            
            List<Course> courses = courseMapper.selectLatestCourses(limit.longValue());
            
            List<CourseVO> vos = courses.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
            
            return Result.success(vos);
        } catch (Exception e) {
            logger.error("获取最新课程失败", e);
            return Result.error("获取最新课程失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> incrementViewCount(Long id) {
        try {
            courseMapper.incrementViewCount(id);
            return Result.success();
        } catch (Exception e) {
            logger.error("增加课程浏览次数失败", e);
            return Result.error("增加课程浏览次数失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Course>> exportCourseList(CourseQueryDTO queryDTO, Long tenantId) {
        try {
            // 导出课程列表
            List<Course> courses = courseMapper.selectCourseExport(
                tenantId,
                queryDTO.getCourseName(),
                queryDTO.getCourseOrder()
            );
            return Result.success(courses);
        } catch (Exception e) {
            logger.error("导出课程列表失败", e);
            return Result.error("导出课程列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 将Course实体转换为CourseVO视图对象
     * @param course 课程实体
     * @return 课程视图对象
     */
    private CourseVO convertToVO(Course course) {
        CourseVO vo = new CourseVO();
        BeanUtils.copyProperties(course, vo);
        
        // 设置状态文本
        if (course.getStatus() != null) {
            vo.setStatus(course.getStatus()); // 直接使用数字类型
            vo.setStatusText(course.getStatus() == 1 ? "启用" : "禁用");
        }
        
        return vo;
    }

    private String generateCourseCode() {
        return "COURSE_" + System.currentTimeMillis();
    }

    @Override
    public void exportCourses(CourseQueryDTO queryDTO, HttpServletResponse response) throws Exception {
        // 查询所有课程数据
        List<Course> courses = courseMapper.selectList(new QueryWrapper<Course>().eq("deleted", 0));
        
        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("课程数据");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"课程编码", "课程名称", "课程描述", "讲师", "时长(分钟)", "难度", "价格", "最大学员数", "当前学员数", "浏览次数", "评分", "状态", "是否免费", "是否热门", "创建时间"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 填充数据
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                Row row = sheet.createRow(i + 1);
                
                row.createCell(0).setCellValue(course.getCourseCode() != null ? course.getCourseCode() : "");
                row.createCell(1).setCellValue(course.getCourseName() != null ? course.getCourseName() : "");
                row.createCell(2).setCellValue(course.getDescription() != null ? course.getDescription() : "");
                row.createCell(3).setCellValue(course.getInstructor() != null ? course.getInstructor() : "");
                row.createCell(4).setCellValue(course.getDuration() != null ? course.getDuration() : 0);
                row.createCell(5).setCellValue(getDifficultyText(course.getDifficulty()));
                row.createCell(6).setCellValue(course.getPrice() != null ? course.getPrice().doubleValue() : 0.0);
                row.createCell(7).setCellValue(course.getMaxStudents() != null ? course.getMaxStudents() : 0);
                row.createCell(8).setCellValue(course.getCurrentStudents() != null ? course.getCurrentStudents() : 0);
                row.createCell(9).setCellValue(course.getViewCount() != null ? course.getViewCount() : 0);
                row.createCell(10).setCellValue(course.getRating() != null ? course.getRating().doubleValue() : 0.0);
                row.createCell(11).setCellValue(course.getStatus() == 1 ? "已发布" : "草稿");
                row.createCell(12).setCellValue(course.getIsFree() == 1 ? "免费" : "付费");
                row.createCell(13).setCellValue(course.getIsHot() == 1 ? "热门" : "普通");
                row.createCell(14).setCellValue(course.getCreateTime() != null ? course.getCreateTime().toString() : "");
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"courses_" + System.currentTimeMillis() + ".xlsx\"");
            
            // 输出文件
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("导出课程数据异常", e);
            throw e;
        }
    }
    
    private String getDifficultyText(Integer difficulty) {
        if (difficulty == null) return "初级";
        switch (difficulty) {
            case 1: return "初级";
            case 2: return "中级";
            case 3: return "高级";
            default: return "初级";
        }
    }

    @Override
    public Result<List<CourseChapter>> getCourseChapters(Long courseId) {
        try {
            LambdaQueryWrapper<CourseChapter> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CourseChapter::getCourseId, courseId);
            wrapper.orderByAsc(CourseChapter::getChapterOrder);
            
            List<CourseChapter> chapters = chapterMapper.selectList(wrapper);
            return Result.success(chapters);
        } catch (Exception e) {
            logger.error("获取课程章节列表异常", e);
            return Result.error("获取课程章节列表失败");
        }
    }

    @Override
    public Result<CourseChapter> getChapterDetail(Long courseId, Long chapterId) {
        try {
            LambdaQueryWrapper<CourseChapter> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CourseChapter::getId, chapterId);
            wrapper.eq(CourseChapter::getCourseId, courseId);
            
            CourseChapter chapter = chapterMapper.selectOne(wrapper);
            if (chapter == null) {
                return Result.error("章节不存在");
            }
            
            // 获取课程名称
            Course course = courseMapper.selectById(courseId);
            if (course != null) {
                chapter.setCourseName(course.getCourseName());
            }
            
            return Result.success(chapter);
        } catch (Exception e) {
            logger.error("获取章节详情异常", e);
            return Result.error("获取章节详情失败");
        }
    }

    @Override
    public Result<Void> createChapter(CourseChapter chapter) {
        try {
            // 检查课程是否存在
            Course course = courseMapper.selectById(chapter.getCourseId());
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            // 获取当前最大章节ID，并设置新ID为最大ID+1
            Long maxChapterId = chapterMapper.selectMaxId();
            if (maxChapterId != null) {
                chapter.setId(maxChapterId + 1);
                logger.info("设置新章节ID: {}", chapter.getId());
            }
            
            // 设置租户ID
            chapter.setTenantId(course.getTenantId());
            chapter.setCreateTime(LocalDateTime.now());
            chapter.setUpdateTime(LocalDateTime.now());
            
            int rows = chapterMapper.insert(chapter);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("创建章节失败");
            }
        } catch (Exception e) {
            logger.error("创建章节异常", e);
            return Result.error("创建章节失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateChapter(CourseChapter chapter) {
        try {
            CourseChapter existingChapter = chapterMapper.selectById(chapter.getId());
            if (existingChapter == null) {
                return Result.error("章节不存在");
            }
            
            // 检查课程是否存在
            Course course = courseMapper.selectById(chapter.getCourseId());
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            // 保留租户ID
            chapter.setTenantId(existingChapter.getTenantId() != null ? existingChapter.getTenantId() : course.getTenantId());
            chapter.setUpdateTime(LocalDateTime.now());
            
            int rows = chapterMapper.updateById(chapter);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("更新章节失败");
            }
        } catch (Exception e) {
            logger.error("更新章节异常", e);
            return Result.error("更新章节失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteChapter(Long courseId, Long chapterId) {
        try {
            LambdaQueryWrapper<CourseChapter> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CourseChapter::getId, chapterId);
            wrapper.eq(CourseChapter::getCourseId, courseId);
            
            int rows = chapterMapper.delete(wrapper);
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("删除章节失败");
            }
        } catch (Exception e) {
            logger.error("删除章节异常", e);
            return Result.error("删除章节失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateLearningProgress(Long courseId, Long chapterId, Long userId, Integer progress) {
        try {
            // 检查课程和章节是否存在
            Course course = courseMapper.selectById(courseId);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            CourseChapter chapter = chapterMapper.selectById(chapterId);
            if (chapter == null) {
                return Result.error("章节不存在");
            }
            
            // 查找或创建学习记录
            LambdaQueryWrapper<LearningRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LearningRecord::getUserId, userId);
            wrapper.eq(LearningRecord::getCourseId, courseId);
            
            LearningRecord record = learningRecordMapper.selectOne(wrapper);
            
            if (record == null) {
                // 创建新记录
                record = new LearningRecord();
                record.setUserId(userId);
                record.setCourseId(courseId);
                record.setProgress(progress);
                record.setLastChapterId(chapterId);
                record.setCreateTime(LocalDateTime.now());
                record.setUpdateTime(LocalDateTime.now());
                record.setTenantId(course.getTenantId()); // 设置租户ID
                
                learningRecordMapper.insert(record);
            } else {
                // 更新记录
                record.setProgress(progress);
                record.setLastChapterId(chapterId);
                record.setUpdateTime(LocalDateTime.now());
                
                // 如果租户ID为空，则设置租户ID
                if (record.getTenantId() == null) {
                    record.setTenantId(course.getTenantId());
                }
                
                learningRecordMapper.updateById(record);
            }
            
            return Result.success();
        } catch (Exception e) {
            logger.error("更新学习进度异常", e);
            return Result.error("更新学习进度失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> reviewCourse(Long id, Integer status, String reviewComment, Long tenantId, Long userId) {
        try {
            // 校验课程是否存在
            Course course = courseMapper.selectById(id);
            if (course == null) {
                return Result.error("课程不存在");
            }
            
            // 校验审核状态
            if (status != 0 && status != 2) {
                return Result.error("无效的审核状态");
            }
            
            // 更新课程审核状态
            Course updateCourse = new Course();
            updateCourse.setId(id);
            updateCourse.setRemark(status.toString()); // 设置remark为审核状态
            updateCourse.setUpdateTime(LocalDateTime.now());
            updateCourse.setUpdateBy(userId);
            
            // 如果有审核备注，则添加到课程描述中
            if (StringUtils.hasText(reviewComment)) {
                String description = course.getDescription();
                if (description == null) {
                    description = "";
                }
                description += "\n\n审核备注：" + reviewComment;
                updateCourse.setDescription(description);
            }
            
            // 更新课程状态
            int result = courseMapper.updateById(updateCourse);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("审核课程失败");
            }
        } catch (Exception e) {
            logger.error("审核课程失败", e);
            return Result.error("审核课程失败: " + e.getMessage());
        }
    }
} 