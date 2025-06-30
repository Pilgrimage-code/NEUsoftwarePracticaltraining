package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.SysDept;
import com.cemh.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("/by-tenant/{tenantId}")
    public Result<List<SysDept>> getDeptsByTenant(@PathVariable Long tenantId) {
        List<com.cemh.entity.SysDept> list = sysDeptService.getByTenantId(tenantId);
        return Result.success(list);
    }
} 