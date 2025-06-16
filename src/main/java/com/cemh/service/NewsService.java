package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.News;

import java.util.List;
import java.util.Map;

/**
 * 资讯服务接口
 */
public interface NewsService {
    
    /**
     * 创建资讯
     * @param news 资讯实体
     * @return 操作结果
     */
    Result<Void> createNews(News news);
    
    /**
     * 更新资讯
     * @param news 资讯实体
     * @return 操作结果
     */
    Result<Void> updateNews(News news);
    
    /**
     * 删除资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> deleteNews(Long id, Long tenantId);
    
    /**
     * 批量删除资讯
     * @param ids 资讯ID列表
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> batchDeleteNews(List<Long> ids, Long tenantId);
    
    /**
     * 根据ID获取资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 资讯详情
     */
    Result<News> getNewsById(Long id, Long tenantId);
    
    /**
     * 获取资讯详情
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 资讯详情
     */
    Result<News> getNewsDetail(Long id, Long tenantId);
    
    /**
     * 获取资讯列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param tenantId 租户ID
     * @param title 标题（可选）
     * @param category 分类（可选）
     * @param status 状态（可选）
     * @return 资讯列表
     */
    Result<PageResult<News>> getNewsList(int pageNum, int pageSize, Long tenantId, String title, String category, Integer status);
    
    /**
     * 置顶资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> topNews(Long id, Long tenantId);
    
    /**
     * 取消置顶资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> untopNews(Long id, Long tenantId);
    
    /**
     * 设置热门资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> setHotNews(Long id, Long tenantId);
    
    /**
     * 取消热门资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> unsetHotNews(Long id, Long tenantId);
    
    /**
     * 发布资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> publishNews(Long id, Long tenantId);
    
    /**
     * 下线资讯
     * @param id 资讯ID
     * @param tenantId 租户ID
     * @return 操作结果
     */
    Result<Void> unpublishNews(Long id, Long tenantId);
    
    /**
     * 增加浏览次数
     * @param id 资讯ID
     * @return 操作结果
     */
    Result<Void> incrementViewCount(Long id);
    
    /**
     * 获取热门资讯
     * @param limit 数量限制
     * @return 热门资讯列表
     */
    List<News> getHotNews(Long limit);
    
    /**
     * 获取推荐资讯
     * @param limit 数量限制
     * @return 推荐资讯列表
     */
    List<News> getRecommendNews(Long limit);
    
    /**
     * 获取最新资讯
     * @param limit 数量限制
     * @return 最新资讯列表
     */
    List<News> getLatestNews(Long limit);
    
    /**
     * 搜索资讯
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param tenantId 租户ID
     * @return 搜索结果
     */
    Result<PageResult<News>> searchNews(String keyword, int pageNum, int pageSize, Long tenantId);
    
    /**
     * 获取资讯分类列表
     * @return 分类列表
     */
    List<Map<String, Object>> getNewsCategories();
    
    /**
     * 获取资讯分页数据
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param tenantId 租户ID
     * @param title 标题
     * @param category 分类
     * @param status 状态
     * @return 分页结果
     */
    PageResult<News> getNewsPage(int pageNum, int pageSize, Long tenantId, String title, String category, Integer status);
}

