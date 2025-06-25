package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 租户管理控制器
 */
@Tag(name = "租户管理", description = "租户相关接口")
@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class SysTenantController {

    @Autowired
    private SysTenantService sysTenantService;

    @Operation(summary = "获取所有租户列表", description = "获取所有租户的简要信息")
    @GetMapping("/all")
    public Result<List<SysTenant>> getAllTenants() {
        return sysTenantService.getAllTenants();
    }
}


