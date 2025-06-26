package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.NewsCategory;
import com.cemh.service.NewsCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资讯分类管理控制器
 */
@Tag(name = "资讯分类管理", description = "资讯分类相关接口")
@RestController
@RequestMapping("/api/news-categories")
@CrossOrigin(origins = "*")
public class NewsCategoryController {

    @Autowired
    private NewsCategoryService newsCategoryService;

    @Operation(summary = "获取所有资讯分类列表", description = "获取所有资讯分类的简要信息")
    @GetMapping("/all")
    public Result<List<NewsCategory>> getAllNewsCategories() {
        return newsCategoryService.getAllNewsCategories();
    }
}


