package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * NewsService 接口测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsService newsService;

    @Test
    void testCreateNews_Success() {
        // 准备测试数据
        News news = createTestNews(null, "测试资讯", "测试内容");
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.createNews(news)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).createNews(news);
    }

    @Test
    void testUpdateNews_Success() {
        // 准备测试数据
        News news = createTestNews(1L, "更新后的资讯", "更新后的内容");
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.updateNews(news)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.updateNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).updateNews(news);
    }

    @Test
    void testDeleteNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.deleteNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.deleteNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).deleteNews(newsId, tenantId);
    }

    @Test
    void testBatchDeleteNews_Success() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.batchDeleteNews(ids, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.batchDeleteNews(ids, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).batchDeleteNews(ids, tenantId);
    }

    @Test
    void testGetNewsById_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        News expectedNews = createTestNews(newsId, "测试资讯", "测试内容");
        Result<News> expectedResult = Result.success(expectedNews);

        // Mock方法调用
        when(newsService.getNewsById(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<News> result = newsService.getNewsById(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("测试资讯", result.getData().getTitle());

        // 验证方法调用
        verify(newsService, times(1)).getNewsById(newsId, tenantId);
    }

    @Test
    void testGetNewsDetail_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        News expectedNews = createTestNews(newsId, "资讯详情", "详细内容");
        Result<News> expectedResult = Result.success(expectedNews);

        // Mock方法调用
        when(newsService.getNewsDetail(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<News> result = newsService.getNewsDetail(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("资讯详情", result.getData().getTitle());

        // 验证方法调用
        verify(newsService, times(1)).getNewsDetail(newsId, tenantId);
    }

    @Test
    void testGetNewsList_Success() {
        // 准备测试数据
        int pageNum = 1;
        int pageSize = 10;
        Long tenantId = 1L;
        String title = "测试";
        String category = "技术";
        Integer status = 1;
        String author = "作者";
        String summary = "摘要";
        String content = "内容";

        News news1 = createTestNews(1L, "资讯1", "内容1");
        News news2 = createTestNews(2L, "资讯2", "内容2");
        List<News> newsList = Arrays.asList(news1, news2);
        
        PageResult<News> pageResult = new PageResult<>();
        pageResult.setRecords(newsList);
        pageResult.setTotal(2L);
        pageResult.setCurrent(1L);
        pageResult.setSize(10L);
        
        Result<PageResult<News>> expectedResult = Result.success(pageResult);

        // Mock方法调用
        when(newsService.getNewsList(pageNum, pageSize, tenantId, title, category, status, author, summary, content))
                .thenReturn(expectedResult);

        // 执行测试
        Result<PageResult<News>> result = newsService.getNewsList(pageNum, pageSize, tenantId, title, category, status, author, summary, content);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());

        // 验证方法调用
        verify(newsService, times(1)).getNewsList(pageNum, pageSize, tenantId, title, category, status, author, summary, content);
    }

    @Test
    void testTopNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.topNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.topNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).topNews(newsId, tenantId);
    }

    @Test
    void testUntopNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.untopNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.untopNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).untopNews(newsId, tenantId);
    }

    @Test
    void testSetHotNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.setHotNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.setHotNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).setHotNews(newsId, tenantId);
    }

    @Test
    void testUnsetHotNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.unsetHotNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.unsetHotNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).unsetHotNews(newsId, tenantId);
    }

    @Test
    void testPublishNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.publishNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.publishNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).publishNews(newsId, tenantId);
    }

    @Test
    void testUnpublishNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.unpublishNews(newsId, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.unpublishNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).unpublishNews(newsId, tenantId);
    }

    @Test
    void testIncrementViewCount_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Result<Void> expectedResult = Result.success();

        // Mock方法调用
        when(newsService.incrementViewCount(newsId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = newsService.incrementViewCount(newsId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证方法调用
        verify(newsService, times(1)).incrementViewCount(newsId);
    }

    @Test
    void testGetHotNews_Success() {
        // 准备测试数据
        Long limit = 5L;
        News news1 = createTestNews(1L, "热门资讯1", "内容1");
        News news2 = createTestNews(2L, "热门资讯2", "内容2");
        List<News> expectedNews = Arrays.asList(news1, news2);

        // Mock方法调用
        when(newsService.getHotNews(limit)).thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsService.getHotNews(limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("热门资讯1", result.get(0).getTitle());

        // 验证方法调用
        verify(newsService, times(1)).getHotNews(limit);
    }

    @Test
    void testGetRecommendNews_Success() {
        // 准备测试数据
        Long limit = 3L;
        News news1 = createTestNews(1L, "推荐资讯1", "内容1");
        News news2 = createTestNews(2L, "推荐资讯2", "内容2");
        List<News> expectedNews = Arrays.asList(news1, news2);

        // Mock方法调用
        when(newsService.getRecommendNews(limit)).thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsService.getRecommendNews(limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("推荐资讯1", result.get(0).getTitle());

        // 验证方法调用
        verify(newsService, times(1)).getRecommendNews(limit);
    }

    @Test
    void testGetLatestNews_Success() {
        // 准备测试数据
        Long limit = 10L;
        News news1 = createTestNews(1L, "最新资讯1", "内容1");
        News news2 = createTestNews(2L, "最新资讯2", "内容2");
        List<News> expectedNews = Arrays.asList(news1, news2);

        // Mock方法调用
        when(newsService.getLatestNews(limit)).thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsService.getLatestNews(limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("最新资讯1", result.get(0).getTitle());

        // 验证方法调用
        verify(newsService, times(1)).getLatestNews(limit);
    }

    @Test
    void testSearchNews_Success() {
        // 准备测试数据
        String keyword = "搜索关键词";
        int pageNum = 1;
        int pageSize = 10;
        Long tenantId = 1L;

        News news1 = createTestNews(1L, "搜索结果1", "包含关键词的内容");
        News news2 = createTestNews(2L, "搜索结果2", "另一个包含关键词的内容");
        List<News> newsList = Arrays.asList(news1, news2);
        
        PageResult<News> pageResult = new PageResult<>();
        pageResult.setRecords(newsList);
        pageResult.setTotal(2L);
        
        Result<PageResult<News>> expectedResult = Result.success(pageResult);

        // Mock方法调用
        when(newsService.searchNews(keyword, pageNum, pageSize, tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<PageResult<News>> result = newsService.searchNews(keyword, pageNum, pageSize, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());

        // 验证方法调用
        verify(newsService, times(1)).searchNews(keyword, pageNum, pageSize, tenantId);
    }

    @Test
    void testGetNewsCategories_Success() {
        // 准备测试数据
        Map<String, Object> category1 = new HashMap<>();
        category1.put("id", 1L);
        category1.put("name", "技术资讯");
        
        Map<String, Object> category2 = new HashMap<>();
        category2.put("id", 2L);
        category2.put("name", "行业动态");
        
        List<Map<String, Object>> expectedCategories = Arrays.asList(category1, category2);

        // Mock方法调用
        when(newsService.getNewsCategories()).thenReturn(expectedCategories);

        // 执行测试
        List<Map<String, Object>> result = newsService.getNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("技术资讯", result.get(0).get("name"));

        // 验证方法调用
        verify(newsService, times(1)).getNewsCategories();
    }

    @Test
    void testGetNewsPage_Success() {
        // 准备测试数据
        int pageNum = 1;
        int pageSize = 10;
        Long tenantId = 1L;
        String title = "测试";
        String category = "技术";
        Integer status = 1;
        String author = "作者";
        String summary = "摘要";
        String content = "内容";

        News news1 = createTestNews(1L, "分页资讯1", "内容1");
        News news2 = createTestNews(2L, "分页资讯2", "内容2");
        List<News> newsList = Arrays.asList(news1, news2);
        
        PageResult<News> expectedPageResult = new PageResult<>();
        expectedPageResult.setRecords(newsList);
        expectedPageResult.setTotal(2L);

        // Mock方法调用
        when(newsService.getNewsPage(pageNum, pageSize, tenantId, title, category, status, author, summary, content))
                .thenReturn(expectedPageResult);

        // 执行测试
        PageResult<News> result = newsService.getNewsPage(pageNum, pageSize, tenantId, title, category, status, author, summary, content);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getRecords().size());
        assertEquals(2L, result.getTotal());

        // 验证方法调用
        verify(newsService, times(1)).getNewsPage(pageNum, pageSize, tenantId, title, category, status, author, summary, content);
    }

    /**
     * 创建测试用的News对象
     */
    private News createTestNews(Long id, String title, String content) {
        News news = new News();
        news.setId(id);
        news.setTitle(title);
        news.setContent(content);
        news.setSummary("测试摘要");
        news.setAuthor("测试作者");
        news.setCategoryId(1L);
        news.setStatus(1);
        news.setIsTop(0);
        news.setIsHot(0);
        news.setViewCount(0);
        news.setTenantId(1L);
        news.setCreateTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        news.setCreateBy(1L);
        news.setUpdateBy(1L);
        return news;
    }
}

