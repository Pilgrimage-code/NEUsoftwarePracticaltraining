package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.CourseDTO;
import com.cemh.dto.CourseQueryDTO;
import com.cemh.entity.Course;
import com.cemh.entity.CourseChapter;
import com.cemh.entity.LearningRecord;
import com.cemh.mapper.CourseCategoryMapper;
import com.cemh.mapper.CourseChapterMapper;
import com.cemh.mapper.CourseMapper;
import com.cemh.mapper.LearningRecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseCategoryMapper categoryMapper;

    @Mock
    private CourseChapterMapper chapterMapper;

    @Mock
    private LearningRecordMapper learningRecordMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CourseDTO createCourseDTO() {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Test Course");
        dto.setDescription("Test Description");
        dto.setInstructor("Test Instructor");
        dto.setDuration(60);
        dto.setDifficulty(1);
        dto.setPrice(BigDecimal.valueOf(99.99));
        dto.setMaxStudents(100);
        dto.setIsFree(0);
        dto.setIsHot(0);
        dto.setStatus(1);
        dto.setCategoryId(1L);
        dto.setCoverImage("http://example.com/cover.jpg");
        dto.setRemark("remark");
        dto.setCourseIntro("Test Intro");
        dto.setCourseAuthor("Test Author");
        dto.setCourseOrder(0);
        dto.setVideoUrl("http://example.com/video.mp4");

        CourseChapter chapter1 = new CourseChapter();
        chapter1.setChapterName("Chapter 1");
        CourseChapter chapter2 = new CourseChapter();
        chapter2.setChapterName("Chapter 2");
        dto.setChapters(Arrays.asList(chapter1, chapter2));
        return dto;
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Test Course");
        course.setDescription("Test Description");
        course.setInstructor("Test Instructor");
        course.setDuration(60);
        course.setDifficulty(1);
        course.setPrice(BigDecimal.valueOf(99.99));
        course.setMaxStudents(100);
        course.setCurrentStudents(0);
        course.setViewCount(0);
        course.setRating(BigDecimal.valueOf(5.0));
        course.setRatingCount(0);
        course.setStatus(1);
        course.setIsFree(0);
        course.setIsHot(0);
        course.setCourseOrder(0);
        course.setCreateBy(1L);
        course.setUpdateBy(1L);
        course.setCategoryId(1L);
        course.setCoverImage("http://example.com/cover.jpg");
        course.setRemark("remark");
        course.setTenantId(1L);
        course.setCourseCode("COURSE_123");
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        course.setDeleted(0);
        return course;
    }

    @Test
    void testCreateCourseSuccess() {
        CourseDTO courseDTO = createCourseDTO();
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        course.setId(1L);

        when(courseMapper.selectMaxId()).thenReturn(0L);
        when(courseMapper.insert(any(Course.class))).thenReturn(1);
        when(chapterMapper.insert(any(CourseChapter.class))).thenReturn(1);

        Result<Void> result = courseService.createCourse(courseDTO);
        assertTrue(result.isSuccess());
        verify(courseMapper, times(1)).insert(any(Course.class));
        verify(chapterMapper, times(2)).insert(any(CourseChapter.class));
    }

    @Test
    void testCreateCourseFailure() {
        CourseDTO courseDTO = createCourseDTO();
        when(courseMapper.selectMaxId()).thenReturn(0L);
        when(courseMapper.insert(any(Course.class))).thenReturn(0);

        Result<Void> result = courseService.createCourse(courseDTO);
        assertFalse(result.isSuccess());
        verify(courseMapper, times(1)).insert(any(Course.class));
        verify(chapterMapper, never()).insert(any(CourseChapter.class));
    }

    @Test
    void testUpdateCourseSuccess() {
        CourseDTO courseDTO = createCourseDTO();
        courseDTO.setId(1L);
        Course existingCourse = createCourse();

        when(courseMapper.selectById(1L)).thenReturn(existingCourse);
        when(courseMapper.updateById(any(Course.class))).thenReturn(1);
        when(chapterMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());

        Result<Void> result = courseService.updateCourse(courseDTO);
        assertTrue(result.isSuccess());
        verify(courseMapper, times(1)).updateById(any(Course.class));
    }

    @Test
    void testUpdateCourseNotFound() {
        CourseDTO courseDTO = createCourseDTO();
        courseDTO.setId(1L);

        when(courseMapper.selectById(1L)).thenReturn(null);

        Result<Void> result = courseService.updateCourse(courseDTO);
        assertFalse(result.isSuccess());
        assertEquals("课程不存在", result.getMessage());
        verify(courseMapper, never()).updateById(any(Course.class));
    }

    @Test
    void testDeleteCourseSuccess() {
        Long courseId = 1L;
        Long tenantId = 1L;
        Course course = createCourse();

        when(courseMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(course);
        when(courseMapper.deleteById(courseId)).thenReturn(1);
        when(chapterMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(1);

        Result<Void> result = courseService.deleteCourse(courseId, tenantId);
        assertTrue(result.isSuccess());
        verify(courseMapper, times(1)).deleteById(courseId);
        verify(chapterMapper, times(1)).delete(any(LambdaQueryWrapper.class));
    }

    @Test
    void testDeleteCourseNotFound() {
        Long courseId = 1L;
        Long tenantId = 1L;

        when(courseMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        Result<Void> result = courseService.deleteCourse(courseId, tenantId);
        assertFalse(result.isSuccess());
        assertEquals("课程不存在", result.getMessage());
        verify(courseMapper, never()).deleteById(any(Long.class));
    }

    @Test
    void testBatchDeleteCourseSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(courseMapper.deleteBatchIds(ids)).thenReturn(2);
        when(chapterMapper.batchDeleteByCourseIds(ids)).thenReturn(2);
        when(learningRecordMapper.batchDeleteByCourseIds(ids)).thenReturn(2);

        Result<Void> result = courseService.batchDeleteCourse(ids, null);
        assertTrue(result.isSuccess());
        verify(courseMapper, times(1)).deleteBatchIds(ids);
        verify(chapterMapper, times(1)).batchDeleteByCourseIds(ids);
        verify(learningRecordMapper, times(1)).batchDeleteByCourseIds(ids);
    }

    @Test
    void testBatchDeleteCourseEmptyIds() {
        Result<Void> result = courseService.batchDeleteCourse(Collections.emptyList(), null);
        assertFalse(result.isSuccess());
        assertEquals("请选择要删除的课程", result.getMessage());
        verify(courseMapper, never()).deleteBatchIds(anyList());
    }

    @Test
    void testGetCourseByIdSuccess() {
        Long courseId = 1L;
        Course course = createCourse();
        when(courseMapper.selectCourseDetail(courseId, null)).thenReturn(course);
        when(chapterMapper.selectChaptersByCourseId(courseId)).thenReturn(Collections.emptyList());

        Result<com.cemh.vo.CourseVO> result = courseService.getCourseById(courseId, null);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(courseId, result.getData().getId());
    }

    @Test
    void testGetCourseByIdNotFound() {
        Long courseId = 1L;
        when(courseMapper.selectCourseDetail(courseId, null)).thenReturn(null);

        Result<com.cemh.vo.CourseVO> result = courseService.getCourseById(courseId, null);
        assertFalse(result.isSuccess());
        assertEquals("课程不存在", result.getMessage());
    }

    @Test
    void testGetCoursePageSuccess() {
        CourseQueryDTO queryDTO = new CourseQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(10);

        Page<Course> page = new Page<>(1, 10);
        List<Course> courses = Arrays.asList(createCourse(), createCourse());
        IPage<Course> mockPage = mock(IPage.class);
        when(mockPage.getRecords()).thenReturn(courses);
        when(mockPage.getTotal()).thenReturn(2L);
        when(mockPage.getCurrent()).thenReturn(1L);
        when(mockPage.getSize()).thenReturn(10L);

        when(courseMapper.selectCoursePage(any(Page.class), any(), any(), any(), any(), any(), any(), any())).thenReturn(mockPage);

        Result<PageResult<com.cemh.vo.CourseVO>> result = courseService.getCoursePage(queryDTO, null);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        assertEquals(2L, result.getData().getTotal());
    }

    @Test
    void testGetCourseCategoriesSuccess() {
        List<Map<String, Object>> categories = Arrays.asList(Collections.singletonMap("id", (Object)1L));
        when(categoryMapper.selectCategoryTree(any())).thenReturn(categories);

        Result<List<Map<String, Object>>> result = courseService.getCourseCategories(null);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertFalse(result.getData().isEmpty());
    }

    @Test
    void testGetHotCoursesSuccess() {
        List<Course> courses = Arrays.asList(createCourse(), createCourse());
        when(courseMapper.selectHotCourses(anyLong())).thenReturn(courses);

        Result<List<com.cemh.vo.CourseVO>> result = courseService.getHotCourses(5);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetLatestCoursesSuccess() {
        List<Course> courses = Arrays.asList(createCourse(), createCourse());
        when(courseMapper.selectLatestCourses(anyLong())).thenReturn(courses);

        Result<List<com.cemh.vo.CourseVO>> result = courseService.getLatestCourses(5);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testIncrementViewCountSuccess() {
        when(courseMapper.incrementViewCount(anyLong())).thenReturn(1);

        Result<Void> result = courseService.incrementViewCount(1L);
        assertTrue(result.isSuccess());
        verify(courseMapper, times(1)).incrementViewCount(anyLong());
    }

    @Test
    void testExportCourseListSuccess() {
        CourseQueryDTO queryDTO = new CourseQueryDTO();
        List<Course> courses = Arrays.asList(createCourse(), createCourse());
        when(courseMapper.selectCourseExport(any(), any(), any())).thenReturn(courses);

        Result<List<Course>> result = courseService.exportCourseList(queryDTO, null);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetCourseChaptersSuccess() {
        Long courseId = 1L;
        List<CourseChapter> chapters = Arrays.asList(new CourseChapter(), new CourseChapter());
        when(chapterMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(chapters);

        Result<List<CourseChapter>> result = courseService.getCourseChapters(courseId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testGetChapterDetailSuccess() {
        Long courseId = 1L;
        Long chapterId = 1L;
        CourseChapter chapter = new CourseChapter();
        chapter.setId(chapterId);
        chapter.setCourseId(courseId);
        Course course = new Course();
        course.setCourseName("Test Course");

        when(chapterMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(chapter);
        when(courseMapper.selectById(courseId)).thenReturn(course);

        Result<CourseChapter> result = courseService.getChapterDetail(courseId, chapterId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals("Test Course", result.getData().getCourseName());
    }

    @Test
    void testCreateChapterSuccess() {
        CourseChapter chapter = new CourseChapter();
        chapter.setCourseId(1L);
        Course course = createCourse();

        when(courseMapper.selectById(1L)).thenReturn(course);
        when(chapterMapper.selectMaxId()).thenReturn(0L);
        when(chapterMapper.insert(any(CourseChapter.class))).thenReturn(1);

        Result<Void> result = courseService.createChapter(chapter);
        assertTrue(result.isSuccess());
        verify(chapterMapper, times(1)).insert(any(CourseChapter.class));
    }

    @Test
    void testUpdateChapterSuccess() {
        CourseChapter chapter = new CourseChapter();
        chapter.setId(1L);
        chapter.setCourseId(1L);
        CourseChapter existingChapter = new CourseChapter();
        existingChapter.setId(1L);
        existingChapter.setCourseId(1L);
        Course course = createCourse();

        when(chapterMapper.selectById(1L)).thenReturn(existingChapter);
        when(courseMapper.selectById(1L)).thenReturn(course);
        when(chapterMapper.updateById(any(CourseChapter.class))).thenReturn(1);

        Result<Void> result = courseService.updateChapter(chapter);
        assertTrue(result.isSuccess());
        verify(chapterMapper, times(1)).updateById(any(CourseChapter.class));
    }

    @Test
    void testDeleteChapterSuccess() {
        Long courseId = 1L;
        Long chapterId = 1L;
        when(chapterMapper.delete(any(LambdaQueryWrapper.class))).thenReturn(1);

        Result<Void> result = courseService.deleteChapter(courseId, chapterId);
        assertTrue(result.isSuccess());
        verify(chapterMapper, times(1)).delete(any(LambdaQueryWrapper.class));
    }

    @Test
    void testUpdateLearningProgressSuccess() {
        Long courseId = 1L;
        Long chapterId = 1L;
        Long userId = 1L;
        Integer progress = 50;
        Course course = createCourse();
        CourseChapter chapter = new CourseChapter();

        when(courseMapper.selectById(courseId)).thenReturn(course);
        when(chapterMapper.selectById(chapterId)).thenReturn(chapter);
        when(learningRecordMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(learningRecordMapper.insert(any(LearningRecord.class))).thenReturn(1);

        Result<Void> result = courseService.updateLearningProgress(courseId, chapterId, userId, progress);
        assertTrue(result.isSuccess());
        verify(learningRecordMapper, times(1)).insert(any(LearningRecord.class));
    }

    @Test
    void testReviewCourseSuccess() {
        Long courseId = 1L;
        Integer status = 0; // 0 for rejected, 2 for approved
        String reviewComment = "Looks good";
        Long tenantId = 1L;
        Long userId = 1L;
        Course course = createCourse();

        when(courseMapper.selectById(courseId)).thenReturn(course);
        when(courseMapper.updateById(any(Course.class))).thenReturn(1);

        Result<Void> result = courseService.reviewCourse(courseId, status, reviewComment, tenantId, userId);
        assertTrue(result.isSuccess());
        verify(courseMapper, times(1)).updateById(any(Course.class));
    }

    @Test
    void testReviewCourseInvalidStatus() {
        Long courseId = 1L;
        Integer status = 1; // Invalid status
        String reviewComment = "";
        Long tenantId = 1L;
        Long userId = 1L;
        Course course = createCourse();

        when(courseMapper.selectById(courseId)).thenReturn(course);

        Result<Void> result = courseService.reviewCourse(courseId, status, reviewComment, tenantId, userId);
        assertFalse(result.isSuccess());
        assertEquals("无效的审核状态", result.getMessage());
        verify(courseMapper, never()).updateById(any(Course.class));
    }
}


