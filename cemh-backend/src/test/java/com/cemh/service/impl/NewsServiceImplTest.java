package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.News;
import com.cemh.mapper.NewsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsMapper newsMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private News createNews() {
        News news = new News();
        news.setId(1L);
        news.setTitle("Test News");
        news.setContent("Test Content");
        news.setCreateTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        news.setDeleted(0);
        news.setStatus(1);
        news.setIsTop(0);
        news.setIsHot(0);
        news.setViewCount(0);
        news.setTenantId(1L);
        return news;
    }

    @Test
    void testCreateNewsSuccess() {
        News news = createNews();
        when(newsMapper.insert(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.createNews(news);
        assertTrue(result.isSuccess());
        verify(newsMapper, times(1)).insert(any(News.class));
    }

    @Test
    void testCreateNewsFailure() {
        News news = createNews();
        news.setTitle(null); // Invalid input

        Result<Void> result = newsService.createNews(news);
        assertFalse(result.isSuccess());
        assertEquals("标题不能为空", result.getMessage());
        verify(newsMapper, never()).insert(any(News.class));
    }

    @Test
    void testUpdateNewsSuccess() {
        News news = createNews();
        when(newsMapper.selectById(anyLong())).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.updateNews(news);
        assertTrue(result.isSuccess());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUpdateNewsNotFound() {
        News news = createNews();
        when(newsMapper.selectById(anyLong())).thenReturn(null);

        Result<Void> result = newsService.updateNews(news);
        assertFalse(result.isSuccess());
        assertEquals("资讯不存在", result.getMessage());
        verify(newsMapper, never()).updateById(any(News.class));
    }

    @Test
    void testDeleteNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.deleteById(newsId)).thenReturn(1);

        Result<Void> result = newsService.deleteNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        verify(newsMapper, times(1)).deleteById(newsId);
    }

    @Test
    void testDeleteNewsNotFound() {
        Long newsId = 1L;
        Long tenantId = 1L;
        when(newsMapper.selectById(newsId)).thenReturn(null);

        Result<Void> result = newsService.deleteNews(newsId, tenantId);
        assertFalse(result.isSuccess());
        assertEquals("资讯不存在", result.getMessage());
        verify(newsMapper, never()).deleteById(anyLong());
    }

    @Test
    void testBatchDeleteNewsSuccess() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<News> newsList = Arrays.asList(createNews(), createNews());
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(newsList);
        when(newsMapper.deleteBatchIds(ids)).thenReturn(2);

        Result<Void> result = newsService.batchDeleteNews(ids, null);
        assertTrue(result.isSuccess());
        verify(newsMapper, times(1)).deleteBatchIds(ids);
    }

    @Test
    void testBatchDeleteNewsEmptyIds() {
        Result<Void> result = newsService.batchDeleteNews(Collections.emptyList(), null);
        assertFalse(result.isSuccess());
        assertEquals("请选择要删除的资讯", result.getMessage());
        verify(newsMapper, never()).deleteBatchIds(anyList());
    }

    @Test
    void testGetNewsByIdSuccess() {
        Long newsId = 1L;
        News news = createNews();
        when(newsMapper.selectById(newsId)).thenReturn(news);

        Result<News> result = newsService.getNewsById(newsId, null);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(newsId, result.getData().getId());
    }

    @Test
    void testGetNewsByIdNotFound() {
        Long newsId = 1L;
        when(newsMapper.selectById(newsId)).thenReturn(null);

        Result<News> result = newsService.getNewsById(newsId, null);
        assertFalse(result.isSuccess());
        assertEquals("资讯不存在", result.getMessage());
    }

    @Test
    void testGetNewsListSuccess() {
        int pageNum = 1;
        int pageSize = 10;
        Long tenantId = null;
        String title = null;
        String category = null;
        Integer status = null;
        String author = null;
        String summary = null;
        String content = null;

        List<News> newsList = Arrays.asList(createNews(), createNews());
        Page<News> mockPage = new Page<>(pageNum, pageSize);
        mockPage.setRecords(newsList);
        mockPage.setTotal(2L);
        mockPage.setCurrent(pageNum);
        mockPage.setSize(pageSize);

        when(newsMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        Result<PageResult<News>> result = newsService.getNewsList(pageNum, pageSize, tenantId, title, category, status, author, summary, content);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        assertEquals(2L, result.getData().getTotal());
    }

    @Test
    void testTopNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.topNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(1, news.getIsTop());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUntopNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        news.setIsTop(1);
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.untopNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(0, news.getIsTop());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testSetHotNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.setHotNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(1, news.getIsHot());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUnsetHotNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        news.setIsHot(1);
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.unsetHotNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(0, news.getIsHot());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testPublishNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        news.setStatus(0); // Draft
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.publishNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(1, news.getStatus());
        assertNotNull(news.getPublishTime());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUnpublishNewsSuccess() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News news = createNews();
        news.setStatus(1); // Published
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.unpublishNews(newsId, tenantId);
        assertTrue(result.isSuccess());
        assertEquals(2, news.getStatus());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testIncrementViewCountSuccess() {
        Long newsId = 1L;
        News news = createNews();
        news.setViewCount(10);
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        Result<Void> result = newsService.incrementViewCount(newsId);
        assertTrue(result.isSuccess());
        assertEquals(11, news.getViewCount());
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testGetHotNews() {
        List<News> hotNews = Arrays.asList(createNews(), createNews());
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(hotNews);

        List<News> result = newsService.getHotNews(5L);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRecommendNews() {
        List<News> recommendNews = Arrays.asList(createNews(), createNews());
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(recommendNews);

        List<News> result = newsService.getRecommendNews(5L);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetLatestNews() {
        List<News> latestNews = Arrays.asList(createNews(), createNews());
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(latestNews);

        List<News> result = newsService.getLatestNews(5L);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchNewsSuccess() {
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "test";
        Long tenantId = 1L;

        List<News> newsList = Arrays.asList(createNews(), createNews());
        Page<News> mockPage = new Page<>(pageNum, pageSize);
        mockPage.setRecords(newsList);
        mockPage.setTotal(2L);
        mockPage.setCurrent(pageNum);
        mockPage.setSize(pageSize);

        when(newsMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        Result<PageResult<News>> result = newsService.searchNews(keyword, pageNum, pageSize, tenantId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        assertEquals(2L, result.getData().getTotal());
    }

    @Test
    void testGetNewsCategories() {
        List<java.util.Map<String, Object>> categories = newsService.getNewsCategories();
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(4, categories.size());
    }
}


