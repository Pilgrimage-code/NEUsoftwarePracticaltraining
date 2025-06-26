package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.News;
import com.cemh.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 资讯管理控制器
 */
@Tag(name = "资讯管理", description = "资讯相关接口")
@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {
    
    @Autowired
    private NewsService newsService;
    
    @Operation(summary = "获取资讯列表", description = "分页查询资讯列表")
    @GetMapping
    public Result<PageResult<News>> getNewsList(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false) String title,
                                                @RequestParam(required = false) String category,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(required = false) String author,
                                                @RequestParam(required = false) String summary,
                                                @RequestParam(required = false) String content,
                                                @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.getNewsList(page, size, tenantId, title, category, status, author, summary, content);
    }
    
    @Operation(summary = "获取资讯详情", description = "根据ID获取资讯详细信息")
    @GetMapping("/{id}")
    public Result<News> getNewsById(@Parameter(description = "资讯ID") @PathVariable Long id,
                                    @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.getNewsDetail(id, tenantId);
    }
    
    @Operation(summary = "创建资讯", description = "创建新的资讯")
    @PostMapping
    public Result<Void> createNews(@Valid @RequestBody News news,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                   @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (tenantId != null) {
            news.setTenantId(tenantId);
        }
        if (userId != null) {
            news.setCreateBy(userId);
        }
        return newsService.createNews(news);
    }
    
    @Operation(summary = "更新资讯", description = "更新资讯信息")
    @PutMapping("/{id}")
    public Result<Void> updateNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                   @Valid @RequestBody News news,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId,
                                   @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        news.setId(id);
        if (tenantId != null) {
            news.setTenantId(tenantId);
        }
        if (userId != null) {
            news.setUpdateBy(userId);
        }
        return newsService.updateNews(news);
    }
    
    @Operation(summary = "删除资讯", description = "删除指定资讯")
    @DeleteMapping("/{id}")
    public Result<Void> deleteNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.deleteNews(id, tenantId);
    }
    
    @Operation(summary = "批量删除资讯", description = "批量删除指定资讯")
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteNews(@RequestBody List<Long> ids,
                                        @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.batchDeleteNews(ids, tenantId);
    }
    
    @Operation(summary = "发布资讯")
    @PutMapping("/{id}/publish")
    public Result<Void> publishNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                    @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.publishNews(id, tenantId);
    }
    
    @Operation(summary = "下线资讯")
    @PutMapping("/{id}/unpublish")
    public Result<Void> unpublishNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                      @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.unpublishNews(id, tenantId);
    }
    
    @Operation(summary = "置顶资讯")
    @PutMapping("/{id}/top")
    public Result<Void> topNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.topNews(id, tenantId);
    }
    
    @Operation(summary = "取消置顶资讯")
    @PutMapping("/{id}/untop")
    public Result<Void> untopNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                  @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.untopNews(id, tenantId);
    }
    
    @Operation(summary = "设置热门资讯")
    @PutMapping("/{id}/hot")
    public Result<Void> setHotNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                   @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.setHotNews(id, tenantId);
    }
    
    @Operation(summary = "取消热门资讯")
    @PutMapping("/{id}/unhot")
    public Result<Void> unsetHotNews(@Parameter(description = "资讯ID") @PathVariable Long id,
                                     @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.unsetHotNews(id, tenantId);
    }
    
    @Operation(summary = "获取资讯分类列表")
    @GetMapping("/categories")
    public Result<List<Map<String, Object>>> getNewsCategories() {
        List<Map<String, Object>> categories = newsService.getNewsCategories();
        return Result.success(categories);
    }
    
    @Operation(summary = "获取热门资讯")
    @GetMapping("/hot")
    public Result<List<News>> getHotNews(@Parameter(description = "数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<News> hotNews = newsService.getHotNews(limit.longValue());
        return Result.success(hotNews);
    }
    
    @Operation(summary = "获取推荐资讯")
    @GetMapping("/recommend")
    public Result<List<News>> getRecommendNews(@Parameter(description = "数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<News> recommendNews = newsService.getRecommendNews(limit.longValue());
        return Result.success(recommendNews);
    }
    
    @Operation(summary = "获取最新资讯")
    @GetMapping("/latest")
    public Result<List<News>> getLatestNews(@Parameter(description = "数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<News> latestNews = newsService.getLatestNews(limit.longValue());
        return Result.success(latestNews);
    }
    
    @Operation(summary = "搜索资讯")
    @GetMapping("/search")
    public Result<PageResult<News>> searchNews(@RequestParam String keyword,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        return newsService.searchNews(keyword, page, size, tenantId);
    }
    
    @Operation(summary = "增加浏览次数")
    @PutMapping("/{id}/view")
    public Result<Void> incrementViewCount(@Parameter(description = "资讯ID") @PathVariable Long id) {
        return newsService.incrementViewCount(id);
    }
}

