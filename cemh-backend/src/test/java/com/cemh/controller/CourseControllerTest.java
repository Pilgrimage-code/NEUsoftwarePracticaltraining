package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.dto.CourseDTO;
import com.cemh.dto.CourseQueryDTO;
import com.cemh.entity.Course;
import com.cemh.entity.CourseChapter;
import com.cemh.service.CourseService;
import com.cemh.vo.CourseVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * CourseController测试类
 */
@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private CourseDTO courseDTO;
    private CourseVO courseVO;
    private List<CourseVO> courseVOList;
    private PageResult<CourseVO> pageResult;
    private Result<Void> successResult;
    private Result<CourseVO> courseResult;
    private Result<PageResult<CourseVO>> pageResultWrapper;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setCourseName("测试课程");
        courseDTO.setDescription("测试描述");

        courseVO = new CourseVO();
        courseVO.setId(1L);
        courseVO.setCourseName("测试课程");
        // 注意：CourseVO继承自Course类，所以应该有description字段
        // 如果CourseVO中没有description字段或方法，可以去掉这行
        // courseVO.setDescription("测试描述");

        courseVOList = new ArrayList<>();
        courseVOList.add(courseVO);

        pageResult = new PageResult<>();
        pageResult.setRecords(courseVOList);
        pageResult.setTotal(1L);
        pageResult.setCurrent(1L);
        pageResult.setSize(10L);

        successResult = Result.success();
        courseResult = Result.success(courseVO);
        pageResultWrapper = Result.success(pageResult);
    }

    @Test
    void testGetCourseList() {
        // 模拟依赖行为
        when(courseService.getCoursePage(any(CourseQueryDTO.class), anyLong())).thenReturn(pageResultWrapper);

        // 执行测试方法
        Result<PageResult<CourseVO>> result = courseController.getCourseList(new CourseQueryDTO(), 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().getRecords().size());
    }

    @Test
    void testGetCourseById() {
        // 模拟依赖行为
        when(courseService.getCourseById(anyLong(), anyLong())).thenReturn(courseResult);

        // 执行测试方法
        Result<CourseVO> result = courseController.getCourseById(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("测试课程", result.getData().getCourseName());
    }

    @Test
    void testCreateCourse() {
        // 模拟依赖行为
        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.createCourse(courseDTO, 1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证tenantId被设置
        assertEquals(1L, courseDTO.getTenantId());
    }

    @Test
    void testUpdateCourse() {
        // 模拟依赖行为
        when(courseService.updateCourse(any(CourseDTO.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.updateCourse(1L, courseDTO, 1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());

        // 验证ID和tenantId被设置
        assertEquals(1L, courseDTO.getId());
        assertEquals(1L, courseDTO.getTenantId());
    }

    @Test
    void testDeleteCourse() {
        // 模拟依赖行为
        when(courseService.deleteCourse(anyLong(), anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.deleteCourse(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testDeleteCourse_Exception() {
        // 模拟异常情况
        when(courseService.deleteCourse(anyLong(), anyLong())).thenThrow(new RuntimeException("测试异常"));

        // 执行测试方法
        Result<Void> result = courseController.deleteCourse(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testBatchDeleteCourse() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // 模拟依赖行为
        when(courseService.batchDeleteCourse(anyList(), anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.batchDeleteCourse(ids, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testBatchDeleteCourse_Exception() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        // 模拟异常情况
        when(courseService.batchDeleteCourse(anyList(), anyLong())).thenThrow(new RuntimeException("测试异常"));

        // 执行测试方法
        Result<Void> result = courseController.batchDeleteCourse(ids, 1L);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testGetCourseCategories() {
        // 准备测试数据
        List<Map<String, Object>> categories = new ArrayList<>();
        Map<String, Object> category = new HashMap<>();
        category.put("id", 1);
        category.put("name", "Java");
        categories.add(category);

        // 模拟依赖行为
        when(courseService.getCourseCategories(anyLong())).thenReturn(Result.success(categories));

        // 执行测试方法
        Result<List<Map<String, Object>>> result = courseController.getCourseCategories(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
        assertEquals("Java", result.getData().get(0).get("name"));
    }

    @Test
    void testGetHotCourses() {
        // 模拟依赖行为
        when(courseService.getHotCourses(anyInt())).thenReturn(Result.success(courseVOList));

        // 执行测试方法
        Result<List<CourseVO>> result = courseController.getHotCourses(10);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testGetLatestCourses() {
        // 模拟依赖行为
        when(courseService.getLatestCourses(anyInt())).thenReturn(Result.success(courseVOList));

        // 执行测试方法
        Result<List<CourseVO>> result = courseController.getLatestCourses(10);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testIncrementViewCount() {
        // 模拟依赖行为
        when(courseService.incrementViewCount(anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.incrementViewCount(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testExportCourses() throws Exception {
        // 准备测试数据
        MockHttpServletResponse response = new MockHttpServletResponse();
        CourseQueryDTO queryDTO = new CourseQueryDTO();

        // 执行测试方法 - 这里我们只验证方法不抛出异常
        assertDoesNotThrow(() -> courseController.exportCourses(queryDTO, response));

        // 验证调用了服务方法
        verify(courseService).exportCourses(eq(queryDTO), any());
    }

    @Test
    void testGetCourseChapters() {
        // 准备测试数据
        List<CourseChapter> chapters = new ArrayList<>();
        CourseChapter chapter = new CourseChapter();
        chapter.setId(1L);
        chapter.setCourseId(1L);
        chapter.setChapterName("章节1");
        chapters.add(chapter);

        // 模拟依赖行为
        when(courseService.getCourseChapters(anyLong())).thenReturn(Result.success(chapters));

        // 执行测试方法
        Result<List<CourseChapter>> result = courseController.getCourseChapters(1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
        assertEquals("章节1", result.getData().get(0).getChapterName());
    }

    @Test
    void testGetChapterDetail() {
        // 准备测试数据
        CourseChapter chapter = new CourseChapter();
        chapter.setId(1L);
        chapter.setCourseId(1L);
        chapter.setChapterName("章节1");

        // 模拟依赖行为
        when(courseService.getChapterDetail(anyLong(), anyLong())).thenReturn(Result.success(chapter));

        // 执行测试方法
        Result<CourseChapter> result = courseController.getChapterDetail(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("章节1", result.getData().getChapterName());
    }

    @Test
    void testCreateChapter() {
        // 准备测试数据
        CourseChapter chapter = new CourseChapter();
        chapter.setCourseId(1L);
        chapter.setChapterName("章节1");
        chapter.setVideoUrl("http://example.com/video/20250703_test.mp4");

        // 模拟依赖行为
        when(courseService.createChapter(any(CourseChapter.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.createChapter(chapter);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        
        // 验证视频URL被正确处理
        assertEquals("/uploads/20250703_test.mp4", chapter.getVideoUrl());
    }

    @Test
    void testCreateChapter_Exception() {
        // 准备测试数据
        CourseChapter chapter = new CourseChapter();
        chapter.setCourseId(1L);
        chapter.setChapterName("章节1");

        // 模拟异常情况
        when(courseService.createChapter(any(CourseChapter.class))).thenThrow(new RuntimeException("测试异常"));

        // 执行测试方法
        Result<Void> result = courseController.createChapter(chapter);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("测试异常"));
    }

    @Test
    void testUpdateChapter() {
        // 准备测试数据
        CourseChapter chapter = new CourseChapter();
        chapter.setChapterName("更新章节");
        chapter.setVideoUrl("http://example.com/video/20250703_updated.mp4");

        // 模拟依赖行为
        when(courseService.updateChapter(any(CourseChapter.class))).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.updateChapter(1L, 1L, chapter);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        
        // 验证ID和CourseID被正确设置
        assertEquals(1L, chapter.getId());
        assertEquals(1L, chapter.getCourseId());
        
        // 验证视频URL被正确处理
        assertEquals("/uploads/20250703_updated.mp4", chapter.getVideoUrl());
    }

    @Test
    void testDeleteChapter() {
        // 模拟依赖行为
        when(courseService.deleteChapter(anyLong(), anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.deleteChapter(1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testUpdateLearningProgress() {
        // 准备测试数据
        Map<String, Object> data = new HashMap<>();
        data.put("progress", 75);

        // 模拟依赖行为
        when(courseService.updateLearningProgress(anyLong(), anyLong(), anyLong(), anyInt())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.updateLearningProgress(1L, 1L, data, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testUpdateLearningProgress_NoUserId() {
        // 准备测试数据
        Map<String, Object> data = new HashMap<>();
        data.put("progress", 75);

        // 执行测试方法 - 没有用户ID
        Result<Void> result = courseController.updateLearningProgress(1L, 1L, data, null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("未登录或用户ID为空", result.getMessage());
    }

    @Test
    void testGetPendingReviewCourses() {
        // 模拟依赖行为
        when(courseService.getCoursePage(any(CourseQueryDTO.class), anyLong())).thenReturn(pageResultWrapper);

        // 执行测试方法
        Result<PageResult<CourseVO>> result = courseController.getPendingReviewCourses(1, 10, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().getRecords().size());
        
        // 验证查询参数
        verify(courseService).getCoursePage(argThat(dto -> 
            dto.getPage() == 1 && dto.getSize() == 10 && "1".equals(dto.getRemark())), eq(1L));
    }

    @Test
    void testReviewCourse() {
        // 模拟依赖行为
        when(courseService.reviewCourse(anyLong(), anyInt(), anyString(), anyLong(), anyLong())).thenReturn(successResult);

        // 执行测试方法
        Result<Void> result = courseController.reviewCourse(1L, 0, "审核通过", 1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void testReviewCourse_Exception() {
        // 模拟异常情况
        when(courseService.reviewCourse(anyLong(), anyInt(), anyString(), anyLong(), anyLong())).thenThrow(new RuntimeException("测试异常"));

        // 执行测试方法
        Result<Void> result = courseController.reviewCourse(1L, 0, "审核通过", 1L, 1L);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("测试异常"));
    }
} 