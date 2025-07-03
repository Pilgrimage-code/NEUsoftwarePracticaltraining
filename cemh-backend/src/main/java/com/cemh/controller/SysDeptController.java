package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.SysDept;
import com.cemh.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;
    @GetMapping("/dept-tree")
    public Result<List<SysDept>> getDeptTree(@RequestHeader(value = "X-Tenant-Id", required = false) Long tenantId) {
        List<SysDept> tree = sysDeptService.getDeptTree(tenantId);
        return Result.success(tree);
    }
} 