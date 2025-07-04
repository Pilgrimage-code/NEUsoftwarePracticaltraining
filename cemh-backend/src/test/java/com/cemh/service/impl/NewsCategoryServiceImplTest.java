package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.entity.NewsCategory;
import com.cemh.mapper.NewsCategoryMapper;
import com.cemh.service.NewsCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsCategoryServiceImplTest {

    @InjectMocks
    private NewsCategoryServiceImpl newsCategoryService;

    @Mock
    private NewsCategoryMapper newsCategoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNewsCategories() {
        NewsCategory category1 = new NewsCategory();
        category1.setId(1L);
        category1.setCategoryName("Category 1");

        NewsCategory category2 = new NewsCategory();
        category2.setId(2L);
        category2.setCategoryName("Category 2");

        List<NewsCategory> categories = Arrays.asList(category1, category2);

        when(newsCategoryMapper.selectList(null)).thenReturn(categories);

        Result<List<NewsCategory>> result = newsCategoryService.getAllNewsCategories();
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("Category 1", result.getData().get(0).getCategoryName());
        assertEquals("Category 2", result.getData().get(1).getCategoryName());
        verify(newsCategoryMapper, times(1)).selectList(null);
    }
}


