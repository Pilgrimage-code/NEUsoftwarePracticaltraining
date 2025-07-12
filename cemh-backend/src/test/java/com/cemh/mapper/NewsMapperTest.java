package com.cemh.mapper;

import com.cemh.entity.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * NewsMapper 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class NewsMapperTest {

    @Mock
    private NewsMapper newsMapper;

    @Test
    void testSelectPublishedNews_WithAllParameters() {
        // 准备测试数据
        Long tenantId = 1L;
        String category = "技术资讯";
        String keyword = "测试";
        
        News news1 = createTestNews(1L, "测试资讯1", "技术资讯");
        News news2 = createTestNews(2L, "测试资讯2", "技术资讯");
        List<News> expectedNews = Arrays.asList(news1, news2);

        // Mock方法调用
        when(newsMapper.selectPublishedNews(tenantId, category, keyword))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectPublishedNews(tenantId, category, keyword);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("测试资讯1", result.get(0).getTitle());
        assertEquals("测试资讯2", result.get(1).getTitle());
        assertEquals("技术资讯", result.get(0).getCategory());
        assertEquals("技术资讯", result.get(1).getCategory());

        // 验证方法调用
        verify(newsMapper, times(1)).selectPublishedNews(tenantId, category, keyword);
    }

    @Test
    void testSelectPublishedNews_WithNullCategory() {
        // 准备测试数据
        Long tenantId = 1L;
        String category = null;
        String keyword = "测试";
        
        News news = createTestNews(1L, "测试资讯", "行业动态");
        List<News> expectedNews = Arrays.asList(news);

        // Mock方法调用
        when(newsMapper.selectPublishedNews(tenantId, category, keyword))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectPublishedNews(tenantId, category, keyword);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试资讯", result.get(0).getTitle());

        // 验证方法调用
        verify(newsMapper, times(1)).selectPublishedNews(tenantId, category, keyword);
    }

    @Test
    void testSelectPublishedNews_WithNullKeyword() {
        // 准备测试数据
        Long tenantId = 1L;
        String category = "技术资讯";
        String keyword = null;
        
        List<News> expectedNews = Arrays.asList();

        // Mock方法调用
        when(newsMapper.selectPublishedNews(tenantId, category, keyword))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectPublishedNews(tenantId, category, keyword);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(newsMapper, times(1)).selectPublishedNews(tenantId, category, keyword);
    }

    @Test
    void testSelectPublishedNews_EmptyResult() {
        // 准备测试数据
        Long tenantId = 1L;
        String category = "不存在的分类";
        String keyword = "不存在的关键词";
        
        List<News> expectedNews = Arrays.asList();

        // Mock方法调用
        when(newsMapper.selectPublishedNews(tenantId, category, keyword))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectPublishedNews(tenantId, category, keyword);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(newsMapper, times(1)).selectPublishedNews(tenantId, category, keyword);
    }

    @Test
    void testSelectHotNews_Success() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer limit = 5;
        
        News hotNews1 = createTestNews(1L, "热门资讯1", "技术资讯");
        hotNews1.setViewCount(1000);
        News hotNews2 = createTestNews(2L, "热门资讯2", "行业动态");
        hotNews2.setViewCount(800);
        
        List<News> expectedNews = Arrays.asList(hotNews1, hotNews2);

        // Mock方法调用
        when(newsMapper.selectHotNews(tenantId, limit))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectHotNews(tenantId, limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("热门资讯1", result.get(0).getTitle());
        assertEquals(Integer.valueOf(1000), result.get(0).getViewCount());
        assertEquals("热门资讯2", result.get(1).getTitle());
        assertEquals(Integer.valueOf(800), result.get(1).getViewCount());

        // 验证方法调用
        verify(newsMapper, times(1)).selectHotNews(tenantId, limit);
    }

    @Test
    void testSelectHotNews_WithZeroLimit() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer limit = 0;
        
        List<News> expectedNews = Arrays.asList();

        // Mock方法调用
        when(newsMapper.selectHotNews(tenantId, limit))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectHotNews(tenantId, limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(newsMapper, times(1)).selectHotNews(tenantId, limit);
    }

    @Test
    void testSelectLatestNews_Success() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer limit = 10;
        
        News latestNews1 = createTestNews(1L, "最新资讯1", "技术资讯");
        latestNews1.setPublishTime(LocalDateTime.now());
        News latestNews2 = createTestNews(2L, "最新资讯2", "行业动态");
        latestNews2.setPublishTime(LocalDateTime.now().minusHours(1));
        
        List<News> expectedNews = Arrays.asList(latestNews1, latestNews2);

        // Mock方法调用
        when(newsMapper.selectLatestNews(tenantId, limit))
                .thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsMapper.selectLatestNews(tenantId, limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("最新资讯1", result.get(0).getTitle());
        assertEquals("最新资讯2", result.get(1).getTitle());

        // 验证方法调用
        verify(newsMapper, times(1)).selectLatestNews(tenantId, limit);
    }

    @Test
    void testIncrementViewCount_Success() {
        // 准备测试数据
        Long newsId = 1L;
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsMapper.incrementViewCount(newsId))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsMapper.incrementViewCount(newsId);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsMapper, times(1)).incrementViewCount(newsId);
    }

    @Test
    void testIncrementViewCount_NewsNotFound() {
        // 准备测试数据
        Long newsId = 999L; // 不存在的资讯ID
        int expectedAffectedRows = 0;

        // Mock方法调用
        when(newsMapper.incrementViewCount(newsId))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsMapper.incrementViewCount(newsId);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsMapper, times(1)).incrementViewCount(newsId);
    }

    @Test
    void testUpdateTopStatus_SetTop() {
        // 准备测试数据
        Long newsId = 1L;
        Integer isTop = 1; // 置顶
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsMapper.updateTopStatus(newsId, isTop))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsMapper.updateTopStatus(newsId, isTop);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsMapper, times(1)).updateTopStatus(newsId, isTop);
    }

    @Test
    void testUpdateTopStatus_CancelTop() {
        // 准备测试数据
        Long newsId = 1L;
        Integer isTop = 0; // 取消置顶
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsMapper.updateTopStatus(newsId, isTop))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsMapper.updateTopStatus(newsId, isTop);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsMapper, times(1)).updateTopStatus(newsId, isTop);
    }

    @Test
    void testUpdateStatus_Publish() {
        // 准备测试数据
        Long newsId = 1L;
        Integer status = 1; // 发布
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsMapper.updateStatus(newsId, status))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsMapper.updateStatus(newsId, status);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsMapper, times(1)).updateStatus(newsId, status);
    }

    @Test
    void testUpdateStatus_Unpublish() {
        // 准备测试数据
        Long newsId = 1L;
        Integer status = 0; // 下线
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsMapper.updateStatus(newsId, status))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsMapper.updateStatus(newsId, status);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsMapper, times(1)).updateStatus(newsId, status);
    }

    @Test
    void testCountByStatus_Published() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer status = 1; // 已发布
        Long expectedCount = 10L;

        // Mock方法调用
        when(newsMapper.countByStatus(tenantId, status))
                .thenReturn(expectedCount);

        // 执行测试
        Long result = newsMapper.countByStatus(tenantId, status);

        // 验证结果
        assertEquals(expectedCount, result);

        // 验证方法调用
        verify(newsMapper, times(1)).countByStatus(tenantId, status);
    }

    @Test
    void testCountByStatus_Draft() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer status = 0; // 草稿
        Long expectedCount = 5L;

        // Mock方法调用
        when(newsMapper.countByStatus(tenantId, status))
                .thenReturn(expectedCount);

        // 执行测试
        Long result = newsMapper.countByStatus(tenantId, status);

        // 验证结果
        assertEquals(expectedCount, result);

        // 验证方法调用
        verify(newsMapper, times(1)).countByStatus(tenantId, status);
    }

    @Test
    void testCountByStatus_ZeroCount() {
        // 准备测试数据
        Long tenantId = 999L; // 不存在的租户
        Integer status = 1;
        Long expectedCount = 0L;

        // Mock方法调用
        when(newsMapper.countByStatus(tenantId, status))
                .thenReturn(expectedCount);

        // 执行测试
        Long result = newsMapper.countByStatus(tenantId, status);

        // 验证结果
        assertEquals(expectedCount, result);

        // 验证方法调用
        verify(newsMapper, times(1)).countByStatus(tenantId, status);
    }

    @Test
    void testSelectCategories_Success() {
        // 准备测试数据
        Long tenantId = 1L;
        List<String> expectedCategories = Arrays.asList("技术资讯", "行业动态", "产品发布");

        // Mock方法调用
        when(newsMapper.selectCategories(tenantId))
                .thenReturn(expectedCategories);

        // 执行测试
        List<String> result = newsMapper.selectCategories(tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("技术资讯"));
        assertTrue(result.contains("行业动态"));
        assertTrue(result.contains("产品发布"));

        // 验证方法调用
        verify(newsMapper, times(1)).selectCategories(tenantId);
    }

    @Test
    void testSelectCategories_EmptyResult() {
        // 准备测试数据
        Long tenantId = 999L; // 不存在的租户
        List<String> expectedCategories = Arrays.asList();

        // Mock方法调用
        when(newsMapper.selectCategories(tenantId))
                .thenReturn(expectedCategories);

        // 执行测试
        List<String> result = newsMapper.selectCategories(tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(newsMapper, times(1)).selectCategories(tenantId);
    }

    @Test
    void testSelectCategories_SingleCategory() {
        // 准备测试数据
        Long tenantId = 1L;
        List<String> expectedCategories = Arrays.asList("技术资讯");

        // Mock方法调用
        when(newsMapper.selectCategories(tenantId))
                .thenReturn(expectedCategories);

        // 执行测试
        List<String> result = newsMapper.selectCategories(tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("技术资讯", result.get(0));

        // 验证方法调用
        verify(newsMapper, times(1)).selectCategories(tenantId);
    }

    /**
     * 创建测试用的News对象
     */
    private News createTestNews(Long id, String title, String category) {
        News news = new News();
        news.setId(id);
        news.setTitle(title);
        news.setCategory(category);
        news.setSummary("测试摘要");
        news.setContent("测试内容");
        news.setAuthor("测试作者");
        news.setStatus(1);
        news.setViewCount(0);
        news.setIsTop(0);
        news.setTenantId(1L);
        news.setCreateTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        news.setPublishTime(LocalDateTime.now());
        return news;
    }
}

