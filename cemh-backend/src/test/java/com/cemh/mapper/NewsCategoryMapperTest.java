package com.cemh.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.entity.NewsCategory;
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
 * NewsCategoryMapper 测试类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-15
 */
@ExtendWith(MockitoExtension.class)
class NewsCategoryMapperTest {

    @Mock
    private NewsCategoryMapper newsCategoryMapper;

    @Test
    void testInsert_Success() {
        // 准备测试数据
        NewsCategory newsCategory = createTestNewsCategory(null, "技术资讯", "技术相关资讯");
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsCategoryMapper.insert(newsCategory))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.insert(newsCategory);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).insert(newsCategory);
    }

    @Test
    void testInsert_Failure() {
        // 准备测试数据
        NewsCategory newsCategory = createTestNewsCategory(null, "技术资讯", "技术相关资讯");
        int expectedAffectedRows = 0;

        // Mock方法调用
        when(newsCategoryMapper.insert(newsCategory))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.insert(newsCategory);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).insert(newsCategory);
    }

    @Test
    void testDeleteById_Success() {
        // 准备测试数据
        Long categoryId = 1L;
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsCategoryMapper.deleteById(categoryId))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.deleteById(categoryId);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteById_NotFound() {
        // 准备测试数据
        Long categoryId = 999L; // 不存在的ID
        int expectedAffectedRows = 0;

        // Mock方法调用
        when(newsCategoryMapper.deleteById(categoryId))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.deleteById(categoryId);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).deleteById(categoryId);
    }

    @Test
    void testUpdateById_Success() {
        // 准备测试数据
        NewsCategory newsCategory = createTestNewsCategory(1L, "更新后的技术资讯", "更新后的技术相关资讯");
        int expectedAffectedRows = 1;

        // Mock方法调用
        when(newsCategoryMapper.updateById(newsCategory))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.updateById(newsCategory);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).updateById(newsCategory);
    }

    @Test
    void testUpdateById_NotFound() {
        // 准备测试数据
        NewsCategory newsCategory = createTestNewsCategory(999L, "不存在的分类", "不存在的分类描述");
        int expectedAffectedRows = 0;

        // Mock方法调用
        when(newsCategoryMapper.updateById(newsCategory))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.updateById(newsCategory);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).updateById(newsCategory);
    }

    @Test
    void testSelectById_Success() {
        // 准备测试数据
        Long categoryId = 1L;
        NewsCategory expectedCategory = createTestNewsCategory(categoryId, "技术资讯", "技术相关资讯");

        // Mock方法调用
        when(newsCategoryMapper.selectById(categoryId))
                .thenReturn(expectedCategory);

        // 执行测试
        NewsCategory result = newsCategoryMapper.selectById(categoryId);

        // 验证结果
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("技术资讯", result.getName());
        assertEquals("技术相关资讯", result.getDescription());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectById(categoryId);
    }

    @Test
    void testSelectById_NotFound() {
        // 准备测试数据
        Long categoryId = 999L; // 不存在的ID

        // Mock方法调用
        when(newsCategoryMapper.selectById(categoryId))
                .thenReturn(null);

        // 执行测试
        NewsCategory result = newsCategoryMapper.selectById(categoryId);

        // 验证结果
        assertNull(result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectById(categoryId);
    }

    @Test
    void testSelectList_WithQueryWrapper() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        NewsCategory category1 = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        NewsCategory category2 = createTestNewsCategory(2L, "行业动态", "行业动态资讯");
        List<NewsCategory> expectedCategories = Arrays.asList(category1, category2);

        // Mock方法调用
        when(newsCategoryMapper.selectList(any(QueryWrapper.class)))
                .thenReturn(expectedCategories);

        // 执行测试
        List<NewsCategory> result = newsCategoryMapper.selectList(queryWrapper);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("技术资讯", result.get(0).getName());
        assertEquals("行业动态", result.get(1).getName());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testSelectList_EmptyResult() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0); // 查询禁用的分类
        
        List<NewsCategory> expectedCategories = Arrays.asList();

        // Mock方法调用
        when(newsCategoryMapper.selectList(any(QueryWrapper.class)))
                .thenReturn(expectedCategories);

        // 执行测试
        List<NewsCategory> result = newsCategoryMapper.selectList(queryWrapper);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testSelectList_WithNullQueryWrapper() {
        // 准备测试数据
        NewsCategory category1 = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        NewsCategory category2 = createTestNewsCategory(2L, "行业动态", "行业动态资讯");
        NewsCategory category3 = createTestNewsCategory(3L, "产品发布", "产品发布资讯");
        List<NewsCategory> expectedCategories = Arrays.asList(category1, category2, category3);

        // Mock方法调用
        when(newsCategoryMapper.selectList(isNull()))
                .thenReturn(expectedCategories);

        // 执行测试
        List<NewsCategory> result = newsCategoryMapper.selectList(null);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("技术资讯", result.get(0).getName());
        assertEquals("行业动态", result.get(1).getName());
        assertEquals("产品发布", result.get(2).getName());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectList(isNull());
    }

    @Test
    void testSelectOne_Success() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "技术资讯");
        
        NewsCategory expectedCategory = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");

        // Mock方法调用
        when(newsCategoryMapper.selectOne(any(QueryWrapper.class)))
                .thenReturn(expectedCategory);

        // 执行测试
        NewsCategory result = newsCategoryMapper.selectOne(queryWrapper);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("技术资讯", result.getName());
        assertEquals("技术相关资讯", result.getDescription());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testSelectOne_NotFound() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "不存在的分类");

        // Mock方法调用
        when(newsCategoryMapper.selectOne(any(QueryWrapper.class)))
                .thenReturn(null);

        // 执行测试
        NewsCategory result = newsCategoryMapper.selectOne(queryWrapper);

        // 验证结果
        assertNull(result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testSelectCount_WithQueryWrapper() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        Long expectedCount = 5L;

        // Mock方法调用
        when(newsCategoryMapper.selectCount(any(QueryWrapper.class)))
                .thenReturn(expectedCount);

        // 执行测试
        Long result = newsCategoryMapper.selectCount(queryWrapper);

        // 验证结果
        assertEquals(expectedCount, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    void testSelectCount_ZeroResult() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 999); // 不存在的状态
        
        Long expectedCount = 0L;

        // Mock方法调用
        when(newsCategoryMapper.selectCount(any(QueryWrapper.class)))
                .thenReturn(expectedCount);

        // 执行测试
        Long result = newsCategoryMapper.selectCount(queryWrapper);

        // 验证结果
        assertEquals(expectedCount, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    void testDelete_WithQueryWrapper() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 0); // 删除禁用的分类
        
        int expectedAffectedRows = 3;

        // Mock方法调用
        when(newsCategoryMapper.delete(any(QueryWrapper.class)))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.delete(queryWrapper);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).delete(any(QueryWrapper.class));
    }

    @Test
    void testDelete_NoMatchingRecords() {
        // 准备测试数据
        QueryWrapper<NewsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "不存在的分类");
        
        int expectedAffectedRows = 0;

        // Mock方法调用
        when(newsCategoryMapper.delete(any(QueryWrapper.class)))
                .thenReturn(expectedAffectedRows);

        // 执行测试
        int result = newsCategoryMapper.delete(queryWrapper);

        // 验证结果
        assertEquals(expectedAffectedRows, result);

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).delete(any(QueryWrapper.class));
    }

    @Test
    void testSelectBatchIds_Success() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        
        NewsCategory category1 = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        NewsCategory category2 = createTestNewsCategory(2L, "行业动态", "行业动态资讯");
        NewsCategory category3 = createTestNewsCategory(3L, "产品发布", "产品发布资讯");
        List<NewsCategory> expectedCategories = Arrays.asList(category1, category2, category3);

        // Mock方法调用
        when(newsCategoryMapper.selectBatchIds(ids))
                .thenReturn(expectedCategories);

        // 执行测试
        List<NewsCategory> result = newsCategoryMapper.selectBatchIds(ids);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(3L, result.get(2).getId());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectBatchIds(ids);
    }

    @Test
    void testSelectBatchIds_PartialMatch() {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 999L, 3L); // 999L不存在
        
        NewsCategory category1 = createTestNewsCategory(1L, "技术资讯", "技术相关资讯");
        NewsCategory category3 = createTestNewsCategory(3L, "产品发布", "产品发布资讯");
        List<NewsCategory> expectedCategories = Arrays.asList(category1, category3);

        // Mock方法调用
        when(newsCategoryMapper.selectBatchIds(ids))
                .thenReturn(expectedCategories);

        // 执行测试
        List<NewsCategory> result = newsCategoryMapper.selectBatchIds(ids);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(3L, result.get(1).getId());

        // 验证方法调用
        verify(newsCategoryMapper, times(1)).selectBatchIds(ids);
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

