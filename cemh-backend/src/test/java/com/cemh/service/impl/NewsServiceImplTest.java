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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * NewsServiceImpl 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News testNews;

    @BeforeEach
    void setUp() {
        testNews = createTestNews(1L, "测试资讯", "测试内容");
    }

    @Test
    void testCreateNews_Success() {
        // 准备测试数据
        News news = createTestNews(null, "新建资讯", "新建内容");
        
        // Mock Mapper方法
        when(newsMapper.insert(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证字段设置
        assertNotNull(news.getCreateTime());
        assertNotNull(news.getUpdateTime());
        assertEquals(0, news.getDeleted());
        assertEquals(0, news.getStatus());
        assertEquals(0, news.getIsTop());
        assertEquals(0, news.getIsHot());
        assertEquals(0, news.getViewCount());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).insert(any(News.class));
    }

    @Test
    void testCreateNews_EmptyTitle() {
        // 准备测试数据 - 空标题
        News news = createTestNews(null, "", "测试内容");

        // 执行测试
        Result<?> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("标题不能为空", result.getMessage());

        // 验证Mapper方法未被调用
        verify(newsMapper, never()).insert(any(News.class));
    }

    @Test
    void testCreateNews_NullTitle() {
        // 准备测试数据 - null标题
        News news = createTestNews(null, null, "测试内容");

        // 执行测试
        Result<?> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("标题不能为空", result.getMessage());

        // 验证Mapper方法未被调用
        verify(newsMapper, never()).insert(any(News.class));
    }

    @Test
    void testCreateNews_EmptyContent() {
        // 准备测试数据 - 空内容
        News news = createTestNews(null, "测试标题", "");

        // 执行测试
        Result<?> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("内容不能为空", result.getMessage());

        // 验证Mapper方法未被调用
        verify(newsMapper, never()).insert(any(News.class));
    }

    @Test
    void testCreateNews_NullContent() {
        // 准备测试数据 - null内容
        News news = createTestNews(null, "测试标题", null);

        // 执行测试
        Result<?> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("内容不能为空", result.getMessage());

        // 验证Mapper方法未被调用
        verify(newsMapper, never()).insert(any(News.class));
    }

    @Test
    void testCreateNews_DatabaseError() {
        // 准备测试数据
        News news = createTestNews(null, "测试标题", "测试内容");
        
        // Mock Mapper方法抛出异常
        when(newsMapper.insert(any(News.class))).thenThrow(new RuntimeException("数据库错误"));

        // 执行测试
        Result<?> result = newsService.createNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertTrue(result.getMessage().contains("资讯创建失败"));

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).insert(any(News.class));
    }

    @Test
    void testUpdateNews_Success() {
        // 准备测试数据
        News news = createTestNews(1L, "更新标题", "更新内容");
        
        // Mock Mapper方法
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.updateNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证更新时间被设置
        assertNotNull(news.getUpdateTime());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUpdateNews_NotFound() {
        // 准备测试数据
        News news = createTestNews(999L, "不存在的资讯", "内容");
        
        // Mock Mapper方法返回0（未更新任何记录）
        when(newsMapper.updateById(any(News.class))).thenReturn(0);

        // 执行测试
        Result<?> result = newsService.updateNews(news);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("资讯不存在或无权限修改", result.getMessage());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUpdateNews_NullNews() {
        Result<?> result = newsService.updateNews(null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testDeleteNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.deleteNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testDeleteNews_NotFound() {
        // 准备测试数据
        Long newsId = 999L;
        Long tenantId = 1L;
        
        // Mock Mapper方法返回null
        when(newsMapper.selectById(newsId)).thenReturn(null);

        // 执行测试
        Result<?> result = newsService.deleteNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("资讯不存在", result.getMessage());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, never()).updateById(any(News.class));
    }

    @Test
    void testDeleteNews_WrongTenant() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 999L; // 不同的租户ID
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);

        // 执行测试
        Result<?> result = newsService.deleteNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("无权限删除该资讯", result.getMessage());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, never()).updateById(any(News.class));
    }

    @Test
    void testDeleteNews_NullId() {
        Result<?> result = newsService.deleteNews(null, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testDeleteNews_NullTenantId() {
        Result<?> result = newsService.deleteNews(1L, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testDeleteNews_AlreadyDeleted() {
        Long newsId = 1L;
        Long tenantId = 1L;
        News deletedNews = createTestNews(newsId, "已删除", "内容");
        deletedNews.setDeleted(1);
        when(newsMapper.selectById(newsId)).thenReturn(deletedNews);
        Result<?> result = newsService.deleteNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testDeleteNews_MapperException() {
        Long newsId = 1L;
        Long tenantId = 1L;
        when(newsMapper.selectById(newsId)).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.deleteNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testBatchDeleteNews_Success() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Long tenantId = 1L;
        
        News news1 = createTestNews(1L, "资讯1", "内容1");
        News news2 = createTestNews(2L, "资讯2", "内容2");
        News news3 = createTestNews(3L, "资讯3", "内容3");
        
        // Mock Mapper方法
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(news1, news2, news3));
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.batchDeleteNews(ids, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectList(any(QueryWrapper.class));
        verify(newsMapper, times(3)).updateById(any(News.class));
    }

    @Test
    void testBatchDeleteNews_EmptyList() {
        Result<?> result = newsService.batchDeleteNews(Collections.emptyList(), 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testBatchDeleteNews_NullTenantId() {
        List<Long> ids = Arrays.asList(1L, 2L);
        Result<?> result = newsService.batchDeleteNews(ids, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testBatchDeleteNews_MapperException() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(newsMapper.selectList(any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.batchDeleteNews(ids, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsById_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);

        // 执行测试
        Result<?> result = newsService.getNewsById(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("测试资讯", ((News)result.getData()).getTitle());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
    }

    @Test
    void testGetNewsById_NotFound() {
        // 准备测试数据
        Long newsId = 999L;
        Long tenantId = 1L;
        
        // Mock Mapper方法返回null
        when(newsMapper.selectById(newsId)).thenReturn(null);

        // 执行测试
        Result<?> result = newsService.getNewsById(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("资讯不存在", result.getMessage());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
    }

    @Test
    void testGetNewsById_NullId() {
        Result<?> result = newsService.getNewsById(null, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsById_NullTenantId() {
        Result<?> result = newsService.getNewsById(1L, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsDetail_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.getNewsDetail(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals("测试资讯", ((News)result.getData()).getTitle());

        // 验证浏览次数增加
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testGetNewsDetail_NullId() {
        Result<?> result = newsService.getNewsDetail(null, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsDetail_NullTenantId() {
        Result<?> result = newsService.getNewsDetail(1L, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsDetail_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.getNewsDetail(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testTopNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.topNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testTopNews_NullId() {
        Result<?> result = newsService.topNews(null, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testTopNews_NullTenantId() {
        Result<?> result = newsService.topNews(1L, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testTopNews_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.topNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testTopNews_AlreadyTop() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setIsTop(1); // 已置顶
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.topNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testTopNews_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.topNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testTopNews_UpdateFail() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(0); // 更新失败
        Result<?> result = newsService.topNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUntopNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        testNews.setIsTop(1); // 设置为已置顶
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.untopNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testUntopNews_NullId() {
        Result<?> result = newsService.untopNews(null, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUntopNews_NullTenantId() {
        Result<?> result = newsService.untopNews(1L, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUntopNews_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.untopNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUntopNews_AlreadyUntop() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setIsTop(0); // 已取消置顶
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.untopNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUntopNews_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.untopNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUntopNews_UpdateFail() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(0); // 更新失败
        Result<?> result = newsService.untopNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testPublishNews_Success() {
        // 准备测试数据
        Long newsId = 1L;
        Long tenantId = 1L;
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.publishNews(newsId, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testPublishNews_NullId() {
        Result<?> result = newsService.publishNews(null, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testPublishNews_NullTenantId() {
        Result<?> result = newsService.publishNews(1L, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testPublishNews_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.publishNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testPublishNews_AlreadyPublished() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setStatus(1); // 已发布
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.publishNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testPublishNews_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.publishNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testPublishNews_UpdateFail() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(0); // 更新失败
        Result<?> result = newsService.publishNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testIncrementViewCount_Success() {
        // 准备测试数据
        Long newsId = 1L;
        
        // Mock Mapper方法
        when(newsMapper.selectById(newsId)).thenReturn(testNews);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);

        // 执行测试
        Result<?> result = newsService.incrementViewCount(newsId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectById(newsId);
        verify(newsMapper, times(1)).updateById(any(News.class));
    }

    @Test
    void testIncrementViewCount_NullId() {
        Result<?> result = newsService.incrementViewCount(null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testIncrementViewCount_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.incrementViewCount(1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetHotNews_Success() {
        // 准备测试数据
        Long limit = 5L;
        News hotNews1 = createTestNews(1L, "热门资讯1", "内容1");
        News hotNews2 = createTestNews(2L, "热门资讯2", "内容2");
        List<News> expectedNews = Arrays.asList(hotNews1, hotNews2);
        
        // Mock Mapper方法
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsService.getHotNews(limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("热门资讯1", ((News)result.get(0)).getTitle());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testGetHotNews_MapperException() {
        when(newsMapper.selectList(any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        assertThrows(RuntimeException.class, () -> newsService.getHotNews(5L));
    }

    @Test
    void testGetLatestNews_Success() {
        // 准备测试数据
        Long limit = 10L;
        News latestNews1 = createTestNews(1L, "最新资讯1", "内容1");
        News latestNews2 = createTestNews(2L, "最新资讯2", "内容2");
        List<News> expectedNews = Arrays.asList(latestNews1, latestNews2);
        
        // Mock Mapper方法
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedNews);

        // 执行测试
        List<News> result = newsService.getLatestNews(limit);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("最新资讯1", ((News)result.get(0)).getTitle());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testGetLatestNews_MapperException() {
        when(newsMapper.selectList(any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        assertThrows(RuntimeException.class, () -> newsService.getLatestNews(10L));
    }

    @Test
    void testSearchNews_Success() {
        // 准备测试数据
        String keyword = "搜索关键词";
        int pageNum = 1;
        int pageSize = 10;
        Long tenantId = 1L;

        News searchResult1 = createTestNews(1L, "包含关键词的资讯", "内容");
        News searchResult2 = createTestNews(2L, "另一个搜索结果", "包含关键词的内容");
        List<News> newsList = Arrays.asList(searchResult1, searchResult2);
        
        Page<News> page = new Page<>(pageNum, pageSize);
        page.setRecords(newsList);
        page.setTotal(2L);
        
        // Mock Mapper方法
        when(newsMapper.selectPage(any(IPage.class), any(QueryWrapper.class))).thenReturn(page);

        // 执行测试
        Result<?> result = newsService.searchNews(keyword, pageNum, pageSize, tenantId);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, ((PageResult<News>)result.getData()).getRecords().size());

        // 验证Mapper方法调用
        verify(newsMapper, times(1)).selectPage(any(IPage.class), any(QueryWrapper.class));
    }

    @Test
    void testSearchNews_MapperException() {
        when(newsMapper.selectPage(any(IPage.class), any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.searchNews("关键词", 1, 10, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsCategories_Success() {
        // 执行测试
        List<Map<String, Object>> result = newsService.getNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertTrue(result instanceof List);
        // 由于这是一个简单的实现，可能返回空列表或固定数据
    }

    @Test
    void testGetNewsList_Success() {
        when(newsMapper.selectPage(any(IPage.class), any(QueryWrapper.class))).thenReturn(new Page<>());
        Result<?> result = newsService.getNewsList(1, 10, 1L, null, null, null, null, null, null);
        assertEquals(200, result.getCode());
    }

    @Test
    void testGetNewsList_MapperException() {
        when(newsMapper.selectPage(any(IPage.class), any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.getNewsList(1, 10, 1L, null, null, null, null, null, null);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetNewsPage_Success() {
        when(newsMapper.selectPage(any(IPage.class), any(QueryWrapper.class))).thenReturn(new Page<>());
        PageResult<News> result = newsService.getNewsPage(1, 10, 1L, null, null, null, null, null, null);
        assertNotNull(result);
    }

    @Test
    void testGetNewsPage_MapperException() {
        when(newsMapper.selectPage(any(IPage.class), any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        assertThrows(RuntimeException.class, () -> newsService.getNewsPage(1, 10, 1L, null, null, null, null, null, null));
    }

    @Test
    void testSetHotNews_Success() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);
        Result<?> result = newsService.setHotNews(newsId, tenantId);
        assertEquals(200, result.getCode());
    }

    @Test
    void testSetHotNews_NotFound() {
        when(newsMapper.selectById(anyLong())).thenReturn(null);
        Result<?> result = newsService.setHotNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testSetHotNews_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.setHotNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testSetHotNews_AlreadyHot() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setIsHot(1); // 已热门
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.setHotNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testSetHotNews_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.setHotNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testSetHotNews_UpdateFail() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(0); // 更新失败
        Result<?> result = newsService.setHotNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnsetHotNews_Success() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);
        Result<?> result = newsService.unsetHotNews(newsId, tenantId);
        assertEquals(200, result.getCode());
    }

    @Test
    void testUnsetHotNews_NotFound() {
        when(newsMapper.selectById(anyLong())).thenReturn(null);
        Result<?> result = newsService.unsetHotNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnsetHotNews_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.unsetHotNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnsetHotNews_AlreadyUnhot() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setIsHot(0); // 已取消热门
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.unsetHotNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnsetHotNews_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.unsetHotNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnsetHotNews_UpdateFail() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(0); // 更新失败
        Result<?> result = newsService.unsetHotNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnpublishNews_Success() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(1);
        Result<?> result = newsService.unpublishNews(newsId, tenantId);
        assertEquals(200, result.getCode());
    }

    @Test
    void testUnpublishNews_NotFound() {
        when(newsMapper.selectById(anyLong())).thenReturn(null);
        Result<?> result = newsService.unpublishNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnpublishNews_MapperException() {
        when(newsMapper.selectById(anyLong())).thenThrow(new RuntimeException("db error"));
        Result<?> result = newsService.unpublishNews(1L, 1L);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnpublishNews_AlreadyUnpublished() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setStatus(0); // 已下架
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.unpublishNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnpublishNews_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.unpublishNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testUnpublishNews_UpdateFail() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        when(newsMapper.selectById(newsId)).thenReturn(news);
        when(newsMapper.updateById(any(News.class))).thenReturn(0); // 更新失败
        Result<?> result = newsService.unpublishNews(newsId, tenantId);
        assertEquals(500, result.getCode());
    }

    @Test
    void testGetRecommendNews_Success() {
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(createTestNews(1L, "title", "content")));
        List<News> result = newsService.getRecommendNews(5L);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetRecommendNews_Empty() {
        when(newsMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        List<News> result = newsService.getRecommendNews(5L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRecommendNews_MapperException() {
        when(newsMapper.selectList(any(QueryWrapper.class))).thenThrow(new RuntimeException("db error"));
        assertThrows(RuntimeException.class, () -> newsService.getRecommendNews(5L));
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
        news.setDeleted(0);
        news.setCreateTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        news.setCreateBy(1L);
        news.setUpdateBy(1L);
        return news;
    }

    @Test
    void testGetNewsDetail_Deleted() {
        Long newsId = 1L, tenantId = 1L;
        News news = createTestNews(newsId, "title", "content");
        news.setDeleted(1); // 已删除
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.getNewsDetail(newsId, tenantId);
        assertEquals(200, result.getCode()); // 按实际业务调整
    }

    @Test
    void testGetNewsDetail_WrongTenant() {
        Long newsId = 1L, tenantId = 2L;
        News news = createTestNews(newsId, "title", "content");
        news.setTenantId(1L);
        when(newsMapper.selectById(newsId)).thenReturn(news);
        Result<?> result = newsService.getNewsDetail(newsId, tenantId);
        assertEquals(500, result.getCode());
    }
}

