package com.cemh.service;

import com.cemh.common.Result;
import com.cemh.entity.NewsCategory;

import java.util.List;

public interface NewsCategoryService {
    Result<List<NewsCategory>> getAllNewsCategories();
}


