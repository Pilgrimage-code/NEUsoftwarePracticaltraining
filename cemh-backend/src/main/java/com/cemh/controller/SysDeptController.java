package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.SysDept;
import com.cemh.entity.SysTenant;
import com.cemh.service.SysDeptService;
import com.cemh.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
@Tag(name = "部门管理", description = "部门相关接口")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;
    
    @Autowired
    private SysTenantService sysTenantService;

    @GetMapping("/by-tenant/{tenantId}")
    @Operation(summary = "获取企业下的部门列表", description = "根据租户ID获取部门列表")
    public Result<List<SysDept>> getDeptsByTenant(@PathVariable Long tenantId) {
        // 验证租户是否存在
        SysTenant tenant = (SysTenant) sysTenantService.getById(tenantId);
        if (tenant == null) {
            return Result.error("租户不存在");
        }
        
        List<SysDept> list = sysDeptService.getByTenantId(tenantId);
        
        // 设置租户名称和上级部门名称
        for (SysDept dept : list) {
            dept.setTenantName(tenant.getTenantName());
            
            // 如果parentId不为空且等于租户ID，上级是租户
            if (dept.getParentId() != null && dept.getParentId().equals(tenantId)) {
                dept.setParentName(tenant.getTenantName());
            }
            // 其他情况在XML查询中已经处理
        }
        
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "创建部门", description = "创建新部门")
    public Result<SysDept> createDept(@RequestBody SysDept sysDept) {
        // 验证租户是否存在
        SysTenant tenant = (SysTenant) sysTenantService.getById(sysDept.getTenantId());
        if (tenant == null) {
            return Result.error("租户不存在");
        }
        
        // 检查部门编码唯一性
        if (sysDeptService.existsByCode(sysDept.getTenantId(), sysDept.getDeptCode())) {
            return Result.error("部门编码已存在");
        }

        // 处理parentId，确保正确关联到租户
        if (sysDept.getParentId() == null || sysDept.getParentId() == 0) {
            // 如果parentId为空或0，则设置为租户ID
            sysDept.setParentId(sysDept.getTenantId());
        } else if (sysDept.getParentId().equals(sysDept.getTenantId())) {
            // 如果parentId等于租户ID，直接允许，无需进一步验证
            // 这是创建顶级部门的常见情况
        } else {
            // 验证parentId是否存在且属于同一租户
            SysDept parentDept = sysDeptService.getById(sysDept.getParentId());
            if (parentDept == null) {
                return Result.error("上级部门不存在");
            }
            if (!parentDept.getTenantId().equals(sysDept.getTenantId())) {
                return Result.error("上级部门不属于同一租户");
            }
        }

        // 保存部门
        boolean saved = sysDeptService.save(sysDept);
        if (saved) {
            return Result.success(sysDept);
        }
        return Result.error("创建部门失败");
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新部门", description = "更新部门信息")
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
        
        // 处理parentId，确保正确关联
        if (sysDept.getParentId() == null || sysDept.getParentId() == 0) {
            // 如果parentId为空或0，则设置为租户ID
            sysDept.setParentId(sysDept.getTenantId());
        } else if (sysDept.getParentId().equals(sysDept.getTenantId())) {
            // 如果parentId等于租户ID，直接允许，无需进一步验证
            // 这是顶级部门的常见情况
        } else {
            // 如果parentId不是租户ID，则验证是否存在且属于同一租户
            SysDept parentDept = sysDeptService.getById(sysDept.getParentId());
            if (parentDept == null) {
                return Result.error("上级部门不存在");
            }
            if (!parentDept.getTenantId().equals(sysDept.getTenantId())) {
                return Result.error("上级部门不属于同一租户");
            }
        }

        sysDept.setId(id);
        boolean updated = sysDeptService.updateById(sysDept);
        if (updated) {
            return Result.success(sysDept);
        }
        return Result.error("更新部门失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除部门", description = "删除指定部门")
    public Result<String> deleteDept(@PathVariable Long id) {
        try {
            // 检查部门是否存在
            SysDept dept = sysDeptService.getById(id);
            if (dept == null) {
                return Result.error("部门不存在");
            }
            
            // 检查是否有子部门
            if (sysDeptService.hasChildren(id)) {
                return Result.error("请先删除子部门");
            }
            
            if (sysDeptService.removeDept(id)) {
                return Result.success("部门删除成功");
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取部门详情", description = "根据ID获取部门详情")
    public Result<SysDept> getDeptById(@PathVariable Long id) {
        SysDept dept = sysDeptService.getById(id);
        if (dept == null) {
            return Result.error("部门不存在");
        }
        
        // 设置租户名称
        SysTenant tenant = (SysTenant) sysTenantService.getById(dept.getTenantId());
        if (tenant != null) {
            dept.setTenantName(tenant.getTenantName());
        }
        
        // 设置上级部门名称
        if (dept.getParentId() != null) {
            if (dept.getParentId().equals(dept.getTenantId())) {
                // 如果parentId是租户ID，则上级是租户
                dept.setParentName(dept.getTenantName());
            } else {
                // 否则查询上级部门
                SysDept parentDept = sysDeptService.getById(dept.getParentId());
                if (parentDept != null) {
                    dept.setParentName(parentDept.getDeptName());
                }
            }
        }
        
        return Result.success(dept);
    }
}