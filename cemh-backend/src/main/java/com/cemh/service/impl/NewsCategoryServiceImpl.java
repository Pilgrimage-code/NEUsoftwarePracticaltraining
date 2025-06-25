package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.entity.NewsCategory;
import com.cemh.mapper.NewsCategoryMapper;
import com.cemh.service.NewsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {

    @Autowired
    private NewsCategoryMapper newsCategoryMapper;

    @Override
    public Result<List<NewsCategory>> getAllNewsCategories() {
        List<NewsCategory> categories = newsCategoryMapper.selectList(null);
        return Result.success(categories);
    }
}


