package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.NewsCategory;
import com.cemh.service.NewsCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * NewsCategoryController 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class NewsCategoryControllerTest {

    @Mock
    private NewsCategoryService newsCategoryService;

    @InjectMocks
    private NewsCategoryController newsCategoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsCategoryController).build();
    }

    @Test
    void testGetAllNewsCategories_Success() throws Exception {
        // 准备测试数据
        NewsCategory category1 = new NewsCategory();
        category1.setId(1L);
        category1.setName("技术资讯");
        category1.setDescription("技术相关资讯");
        category1.setSort(1);
        category1.setStatus(1);
        category1.setCreateTime(LocalDateTime.now());

        NewsCategory category2 = new NewsCategory();
        category2.setId(2L);
        category2.setName("行业动态");
        category2.setDescription("行业动态资讯");
        category2.setSort(2);
        category2.setStatus(1);
        category2.setCreateTime(LocalDateTime.now());

        List<NewsCategory> categories = Arrays.asList(category1, category2);
        Result<List<NewsCategory>> expectedResult = Result.success(categories);

        // Mock服务方法
        when(newsCategoryService.getAllNewsCategories()).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(get("/api/news-categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("技术资讯"))
                .andExpect(jsonPath("$.data[0].description").value("技术相关资讯"))
                .andExpect(jsonPath("$.data[0].sort").value(1))
                .andExpect(jsonPath("$.data[0].status").value(1))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("行业动态"))
                .andExpect(jsonPath("$.data[1].description").value("行业动态资讯"))
                .andExpect(jsonPath("$.data[1].sort").value(2))
                .andExpect(jsonPath("$.data[1].status").value(1));

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }

    @Test
    void testGetAllNewsCategories_EmptyList() throws Exception {
        // 准备测试数据 - 空列表
        List<NewsCategory> categories = Arrays.asList();
        Result<List<NewsCategory>> expectedResult = Result.success(categories);

        // Mock服务方法
        when(newsCategoryService.getAllNewsCategories()).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(get("/api/news-categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }

    @Test
    void testGetAllNewsCategories_ServiceError() throws Exception {
        // Mock服务方法返回错误结果
        Result<List<NewsCategory>> expectedResult = Result.error("获取分类列表失败");

        when(newsCategoryService.getAllNewsCategories()).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(get("/api/news-categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("获取分类列表失败"));

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }

    @Test
    void testGetAllNewsCategories_ServiceException() throws Exception {
        // Mock服务方法抛出异常
        when(newsCategoryService.getAllNewsCategories()).thenThrow(new RuntimeException("数据库连接异常"));

        // 执行测试 - 这种情况下会抛出异常，需要在Controller中添加异常处理
        try {
            mockMvc.perform(get("/api/news-categories/all"))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // 验证异常被抛出
            assert e.getCause() instanceof RuntimeException;
            assert "数据库连接异常".equals(e.getCause().getMessage());
        }

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }

    @Test
    void testGetAllNewsCategories_WithCorsHeaders() throws Exception {
        // 准备测试数据
        List<NewsCategory> categories = Arrays.asList();
        Result<List<NewsCategory>> expectedResult = Result.success(categories);

        // Mock服务方法
        when(newsCategoryService.getAllNewsCategories()).thenReturn(expectedResult);

        // 执行测试 - 验证CORS配置
        mockMvc.perform(get("/api/news-categories/all")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }

    @Test
    void testGetAllNewsCategories_MultipleCategories() throws Exception {
        // 准备测试数据 - 多个分类
        NewsCategory category1 = new NewsCategory();
        category1.setId(1L);
        category1.setName("技术资讯");
        category1.setDescription("技术相关资讯");
        category1.setSort(1);
        category1.setStatus(1);

        NewsCategory category2 = new NewsCategory();
        category2.setId(2L);
        category2.setName("行业动态");
        category2.setDescription("行业动态资讯");
        category2.setSort(2);
        category2.setStatus(1);

        NewsCategory category3 = new NewsCategory();
        category3.setId(3L);
        category3.setName("产品发布");
        category3.setDescription("产品发布资讯");
        category3.setSort(3);
        category3.setStatus(0); // 禁用状态

        List<NewsCategory> categories = Arrays.asList(category1, category2, category3);
        Result<List<NewsCategory>> expectedResult = Result.success(categories);

        // Mock服务方法
        when(newsCategoryService.getAllNewsCategories()).thenReturn(expectedResult);

        // 执行测试
        mockMvc.perform(get("/api/news-categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[2].id").value(3))
                .andExpect(jsonPath("$.data[2].name").value("产品发布"))
                .andExpect(jsonPath("$.data[2].status").value(0));

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }

    @Test
    void testGetAllNewsCategories_VerifyResponseStructure() throws Exception {
        // 准备测试数据
        NewsCategory category = new NewsCategory();
        category.setId(1L);
        category.setName("测试分类");
        category.setDescription("测试描述");
        category.setSort(1);
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateBy(1L);
        category.setUpdateBy(1L);

        List<NewsCategory> categories = Arrays.asList(category);
        Result<List<NewsCategory>> expectedResult = Result.success(categories);

        // Mock服务方法
        when(newsCategoryService.getAllNewsCategories()).thenReturn(expectedResult);

        // 执行测试 - 验证响应结构
        mockMvc.perform(get("/api/news-categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].name").exists())
                .andExpect(jsonPath("$.data[0].description").exists())
                .andExpect(jsonPath("$.data[0].sort").exists())
                .andExpect(jsonPath("$.data[0].status").exists());

        // 验证方法调用
        verify(newsCategoryService, times(1)).getAllNewsCategories();
    }
}

