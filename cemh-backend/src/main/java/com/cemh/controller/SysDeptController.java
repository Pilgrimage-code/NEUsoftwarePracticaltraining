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
        List<SysDept> list = sysDeptService.getByTenantId(tenantId);
        return Result.success(list);
    }

    @PostMapping
    public Result<SysDept> createDept(@RequestBody SysDept sysDept) {
        // 检查部门编码唯一性
        if (sysDeptService.existsByCode(sysDept.getTenantId(), sysDept.getDeptCode())) {
            return Result.error("部门编码已存在");
        }

        // 保存部门
        boolean saved = sysDeptService.save(sysDept);
        if (saved) {
            return Result.success(sysDept);
        }
        return Result.error("创建部门失败");
    }

    @PutMapping("/{id}")
    public Result<SysDept> updateDept(@PathVariable Long id, @RequestBody SysDept sysDept) {
        SysDept existing = sysDeptService.getById(id);
        if (existing == null) {
            return Result.error("部门不存在");
        }

        // 检查部门编码唯一性（排除自身）
        if (!existing.getDeptCode().equals(sysDept.getDeptCode()) &&
                sysDeptService.existsByCode(sysDept.getTenantId(), sysDept.getDeptCode())) {
            return Result.error("部门编码已存在");
        }

        sysDept.setId(id);
        boolean updated = sysDeptService.updateById(sysDept);
        if (updated) {
            return Result.success(sysDept);
        }
        return Result.error("更新部门失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteDept(@PathVariable Long id) {
        try {
            if (sysDeptService.removeDept(id)) {
                return Result.success("部门删除成功");
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}