package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.News;
import com.cemh.mapper.NewsMapper;
import com.cemh.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资讯服务实现类
 */
@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    
    @Autowired
    private NewsMapper newsMapper;
    
    @Override
    public Result<Void> createNews(News news) {
        try {
            // 设置创建时间
            news.setCreateTime(LocalDateTime.now());
            news.setUpdateTime(LocalDateTime.now());
            news.setDeleted(0);
            news.setStatus(0); // 默认草稿状态
            news.setIsTop(0); // 默认不置顶
            news.setIsHot(0); // 默认不热门
            news.setViewCount(0);
            
            // 验证必填字段
            if (news.getTitle() == null || news.getTitle().trim().isEmpty()) {
                return Result.error("标题不能为空");
            }
            if (news.getContent() == null || news.getContent().trim().isEmpty()) {
                return Result.error("内容不能为空");
            }
            
            int result = newsMapper.insert(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("资讯创建失败");
            }
        } catch (Exception e) {
            return Result.error("资讯创建失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> updateNews(News news) {
        try {
            // 检查资讯是否存在
            News existingNews = newsMapper.selectById(news.getId());
            if (existingNews == null) {
                return Result.error("资讯不存在");
            }
            
            // 设置更新时间
            news.setUpdateTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("资讯更新失败");
            }
        } catch (Exception e) {
            return Result.error("资讯更新失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> deleteNews(Long id, Long tenantId) {
        try {
            // 检查资讯是否存在
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            // 检查租户权限
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限删除此资讯");
            }
            
            int result = newsMapper.deleteById(id);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("资讯删除失败");
            }
        } catch (Exception e) {
            return Result.error("资讯删除失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> batchDeleteNews(List<Long> ids, Long tenantId) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.error("请选择要删除的资讯");
            }
            
            // 检查所有资讯的租户权限
            QueryWrapper<News> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", ids).eq("tenant_id", tenantId);
            List<News> newsList = newsMapper.selectList(queryWrapper);
            
            if (newsList.size() != ids.size()) {
                return Result.error("部分资讯不存在或无权限删除");
            }
            
            int result = newsMapper.deleteBatchIds(ids);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<News> getNewsById(Long id, Long tenantId) {
        return getNewsDetail(id, tenantId);
    }
    
    @Override
    public Result<News> getNewsDetail(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            // 检查租户权限
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限查看此资讯");
            }
            
            return Result.success(news);
        } catch (Exception e) {
            return Result.error("获取资讯详情失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<PageResult<News>> getNewsList(int pageNum, int pageSize, Long tenantId, String title, String category, Integer status) {
        try {
            PageResult<News> pageResult = getNewsPage(pageNum, pageSize, tenantId, title, category, status);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("获取资讯列表失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> topNews(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此资讯");
            }
            
            news.setIsTop(1);
            news.setUpdateTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("置顶失败");
            }
        } catch (Exception e) {
            return Result.error("置顶失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> untopNews(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此资讯");
            }
            
            news.setIsTop(0);
            news.setUpdateTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("取消置顶失败");
            }
        } catch (Exception e) {
            return Result.error("取消置顶失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> setHotNews(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此资讯");
            }
            
            news.setIsHot(1);
            news.setUpdateTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("设置热门失败");
            }
        } catch (Exception e) {
            return Result.error("设置热门失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> unsetHotNews(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此资讯");
            }
            
            news.setIsHot(0);
            news.setUpdateTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("取消热门失败");
            }
        } catch (Exception e) {
            return Result.error("取消热门失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> publishNews(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此资讯");
            }
            
            news.setStatus(1); // 已发布
            news.setUpdateTime(LocalDateTime.now());
            news.setPublishTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("发布失败");
            }
        } catch (Exception e) {
            return Result.error("发布失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> unpublishNews(Long id, Long tenantId) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            if (!news.getTenantId().equals(tenantId)) {
                return Result.error("无权限操作此资讯");
            }
            
            news.setStatus(2); // 已下线
            news.setUpdateTime(LocalDateTime.now());
            
            int result = newsMapper.updateById(news);
            if (result > 0) {
                return Result.success();
            } else {
                return Result.error("下线失败");
            }
        } catch (Exception e) {
            return Result.error("下线失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> incrementViewCount(Long id) {
        try {
            News news = newsMapper.selectById(id);
            if (news == null) {
                return Result.error("资讯不存在");
            }
            
            news.setViewCount(news.getViewCount() + 1);
            newsMapper.updateById(news);
            
            return Result.success();
        } catch (Exception e) {
            return Result.error("更新浏览次数失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<News> getHotNews(Long limit) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_hot", 1)
                   .eq("status", 1)
                   .eq("deleted", 0)
                   .orderByDesc("view_count", "create_time")
                   .last("LIMIT " + limit);
        
        return newsMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<News> getRecommendNews(Long limit) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_recommend", 1)
                   .eq("status", 1)
                   .eq("deleted", 0)
                   .orderByDesc("create_time")
                   .last("LIMIT " + limit);
        
        return newsMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<News> getLatestNews(Long limit) {
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1)
                   .eq("deleted", 0)
                   .orderByDesc("create_time")
                   .last("LIMIT " + limit);
        
        return newsMapper.selectList(queryWrapper);
    }
    
    @Override
    public Result<PageResult<News>> searchNews(String keyword, int pageNum, int pageSize, Long tenantId) {
        try {
            Page<News> page = new Page<>(pageNum, pageSize);
            QueryWrapper<News> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id", tenantId)
                       .eq("deleted", 0)
                       .eq("status", 1)
                       .and(wrapper -> wrapper.like("title", keyword).or().like("content", keyword))
                       .orderByDesc("create_time");
            
            IPage<News> result = newsMapper.selectPage(page, queryWrapper);
            PageResult<News> pageResult = new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
            
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("搜索失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Map<String, Object>> getNewsCategories() {
        List<Map<String, Object>> categories = new ArrayList<>();
        
        Map<String, Object> category1 = new HashMap<>();
        category1.put("value", 1);
        category1.put("label", "行业动态");
        categories.add(category1);
        
        Map<String, Object> category2 = new HashMap<>();
        category2.put("value", 2);
        category2.put("label", "政策法规");
        categories.add(category2);
        
        Map<String, Object> category3 = new HashMap<>();
        category3.put("value", 3);
        category3.put("label", "技术分享");
        categories.add(category3);
        
        Map<String, Object> category4 = new HashMap<>();
        category4.put("value", 4);
        category4.put("label", "企业新闻");
        categories.add(category4);
        
        return categories;
    }
    
    @Override
    public PageResult<News> getNewsPage(int pageNum, int pageSize, Long tenantId, String title, 
                                      String category, Integer status) {
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", tenantId)
                   .eq("deleted", 0);
        
        if (title != null && !title.trim().isEmpty()) {
            queryWrapper.like("title", title);
        }
        if (category != null && !category.trim().isEmpty()) {
            queryWrapper.eq("category", category);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("is_top", "create_time");
        
        IPage<News> result = newsMapper.selectPage(page, queryWrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }
}

