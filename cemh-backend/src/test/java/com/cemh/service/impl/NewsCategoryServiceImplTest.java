package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.entity.NewsCategory;
import com.cemh.mapper.NewsCategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * NewsCategoryServiceImpl 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class NewsCategoryServiceImplTest {

    @Mock
    private NewsCategoryMapper newsCategoryMapper;

    @InjectMocks
    private NewsCategoryServiceImpl newsCategoryService;

    @BeforeEach
    void setUp() {
        // 初始化设置
    }

    @Test
    void testGetAllNewsCategories_Success() {
        // 准备测试数据
        NewsCategory category1 = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        NewsCategory category2 = createTestNewsCategory(2L, "行业动态", "行业动态资讯");
        NewsCategory category3 = createTestNewsCategory(3L, "产品发布", "产品发布资讯");
        
        List<NewsCategory> expectedCategories = Arrays.asList(category1, category2, category3);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(3, result.getData().size());
        
        List<NewsCategory> categories = result.getData();
        assertEquals("技术资讯", categories.get(0).getName());
        assertEquals("行业动态", categories.get(1).getName());
        assertEquals("产品发布", categories.get(2).getName());
        
        assertEquals("技术相关资讯", categories.get(0).getDescription());
        assertEquals("行业动态资讯", categories.get(1).getDescription());
        assertEquals("产品发布资讯", categories.get(2).getDescription());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_EmptyList() {
        // 准备测试数据 - 空列表
        List<NewsCategory> expectedCategories = Arrays.asList();

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("操作成功", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_SingleCategory() {
        // 准备测试数据 - 单个分类
        NewsCategory category = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        List<NewsCategory> expectedCategories = Arrays.asList(category);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals("技术资讯", result.getData().get(0).getName());
        assertEquals("技术相关资讯", result.getData().get(0).getDescription());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_WithDifferentStatuses() {
        // 准备测试数据 - 包含不同状态的分类
        NewsCategory enabledCategory = createTestNewsCategory(1L, "启用分类", "启用的分类");
        enabledCategory.setStatus(1);
        
        NewsCategory disabledCategory = createTestNewsCategory(2L, "禁用分类", "禁用的分类");
        disabledCategory.setStatus(0);
        
        List<NewsCategory> expectedCategories = Arrays.asList(enabledCategory, disabledCategory);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        
        // 验证状态
        assertEquals(1, result.getData().get(0).getStatus());
        assertEquals(0, result.getData().get(1).getStatus());
        assertEquals("启用分类", result.getData().get(0).getName());
        assertEquals("禁用分类", result.getData().get(1).getName());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_WithSortOrder() {
        // 准备测试数据 - 包含排序的分类
        NewsCategory category1 = createTestNewsCategory(1L, "第一个分类", "排序为1");
        category1.setSort(1);
        
        NewsCategory category2 = createTestNewsCategory(2L, "第二个分类", "排序为2");
        category2.setSort(2);
        
        NewsCategory category3 = createTestNewsCategory(3L, "第三个分类", "排序为3");
        category3.setSort(3);
        
        List<NewsCategory> expectedCategories = Arrays.asList(category1, category2, category3);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(3, result.getData().size());
        
        // 验证排序
        assertEquals(1, result.getData().get(0).getSort());
        assertEquals(2, result.getData().get(1).getSort());
        assertEquals(3, result.getData().get(2).getSort());
        
        assertEquals("第一个分类", result.getData().get(0).getName());
        assertEquals("第二个分类", result.getData().get(1).getName());
        assertEquals("第三个分类", result.getData().get(2).getName());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_WithCompleteFields() {
        // 准备测试数据 - 包含完整字段的分类
        NewsCategory category = createTestNewsCategory(1L, "完整分类", "包含所有字段的分类");
        category.setSort(1);
        category.setStatus(1);
        category.setCreateBy(1001L);
        category.setUpdateBy(1002L);
        category.setCreateTime(LocalDateTime.now().minusDays(1));
        category.setUpdateTime(LocalDateTime.now());
        
        List<NewsCategory> expectedCategories = Arrays.asList(category);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        
        NewsCategory resultCategory = result.getData().get(0);
        assertEquals(1L, resultCategory.getId());
        assertEquals("完整分类", resultCategory.getName());
        assertEquals("包含所有字段的分类", resultCategory.getDescription());
        assertEquals(1, resultCategory.getSort());
        assertEquals(1, resultCategory.getStatus());
        assertEquals(1001L, resultCategory.getCreateBy());
        assertEquals(1002L, resultCategory.getUpdateBy());
        assertNotNull(resultCategory.getCreateTime());
        assertNotNull(resultCategory.getUpdateTime());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_NullHandling() {
        // 准备测试数据 - 包含null值的分类
        NewsCategory category = new NewsCategory();
        category.setId(1L);
        category.setName("分类名称");
        // 其他字段保持null
        
        List<NewsCategory> expectedCategories = Arrays.asList(category);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        
        NewsCategory resultCategory = result.getData().get(0);
        assertEquals(1L, resultCategory.getId());
        assertEquals("分类名称", resultCategory.getName());
        assertNull(resultCategory.getDescription());
        assertNull(resultCategory.getSort());
        assertNull(resultCategory.getStatus());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_MapperException() {
        // Mock Mapper方法抛出异常
        when(newsCategoryMapper.selectList(null)).thenThrow(new RuntimeException("数据库连接失败"));

        // 执行测试 - 应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            newsCategoryService.getAllNewsCategories();
        });

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_LargeDataSet() {
        // 准备测试数据 - 大量分类数据
        List<NewsCategory> expectedCategories = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            NewsCategory category = createTestNewsCategory((long) i, "分类" + i, "分类" + i + "的描述");
            expectedCategories.add(category);
        }

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(100, result.getData().size());
        
        // 验证第一个和最后一个分类
        assertEquals("分类1", result.getData().get(0).getName());
        assertEquals("分类100", result.getData().get(99).getName());

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_MultipleCallsConsistency() {
        // 准备测试数据
        NewsCategory category = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        List<NewsCategory> expectedCategories = Arrays.asList(category);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行多次测试
        Result<List<NewsCategory>> result1 = newsCategoryService.getAllNewsCategories();
        Result<List<NewsCategory>> result2 = newsCategoryService.getAllNewsCategories();
        Result<List<NewsCategory>> result3 = newsCategoryService.getAllNewsCategories();

        // 验证结果一致性
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        
        assertEquals(result1.getCode(), result2.getCode());
        assertEquals(result2.getCode(), result3.getCode());
        
        assertEquals(result1.getData().size(), result2.getData().size());
        assertEquals(result2.getData().size(), result3.getData().size());
        
        assertEquals(result1.getData().get(0).getName(), result2.getData().get(0).getName());
        assertEquals(result2.getData().get(0).getName(), result3.getData().get(0).getName());

        // 验证Mapper方法调用次数
        verify(newsCategoryMapper, times(3)).selectList(null);
    }

    @Test
    void testGetAllNewsCategories_VerifyResultStructure() {
        // 准备测试数据
        NewsCategory category = createTestNewsCategory(1L, "测试分类", "测试描述");
        List<NewsCategory> expectedCategories = Arrays.asList(category);

        // Mock Mapper方法
        when(newsCategoryMapper.selectList(null)).thenReturn(expectedCategories);

        // 执行测试
        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();

        // 验证Result结构
        assertNotNull(result);
        assertTrue(result.getCode() > 0);
        assertNotNull(result.getMessage());
        assertNotNull(result.getData());
        
        // 验证数据结构
        assertTrue(result.getData() instanceof List);
        assertFalse(result.getData().isEmpty());
        assertTrue(result.getData().get(0) instanceof NewsCategory);

        // 验证Mapper方法调用
        verify(newsCategoryMapper, times(1)).selectList(null);
    }

    /**
     * 创建测试用的NewsCategory对象
     */
    private NewsCategory createTestNewsCategory(Long id, String name, String description) {
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setId(id);
        newsCategory.setName(name);
        newsCategory.setDescription(description);
        newsCategory.setSort(1);
        newsCategory.setStatus(1);
        newsCategory.setCreateTime(LocalDateTime.now());
        newsCategory.setUpdateTime(LocalDateTime.now());
        newsCategory.setCreateBy(1L);
        newsCategory.setUpdateBy(1L);
        return newsCategory;
    }
}

