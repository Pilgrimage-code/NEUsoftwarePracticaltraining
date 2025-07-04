package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    @GetMapping
    public Result<PageResult<SysTenant>> getTenantList(@RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) String code,
                                                       @RequestParam(required = false) Integer status,
                                                       @RequestParam(required = false) Integer packageType,
                                                       @RequestParam(required = false) Integer deleted) {
        return sysTenantService.getTenantList(page, size, name, code, status, packageType, deleted);
    }

    @Operation(summary = "获取所有活跃租户", description = "获取所有未删除且状态正常的租户")
    @GetMapping("/all")
    public Result<List<SysTenant>> getAllActiveTenants() {
        return sysTenantService.getAllActiveTenants();
    }

    @PostMapping
    public Result<Void> createTenant(@RequestBody SysTenant tenant) {
        return sysTenantService.createTenant(tenant);
    }

    @PutMapping("/{id}")
    public Result<Void> updateTenant(@PathVariable Long id, @RequestBody SysTenant tenant) {
        tenant.setId(id);
        return sysTenantService.updateTenant(tenant);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTenant(@PathVariable Long id) {
        return sysTenantService.deleteTenant(id);
    }

    @DeleteMapping("/real/{id}")
    public Result<Void> realDeleteTenant(@PathVariable Long id) {
        return sysTenantService.realDeleteTenant(id);
    }

    @PostMapping("/restore/{id}")
    public Result<Void> restoreTenant(@PathVariable Long id) {
        return sysTenantService.restoreTenant(id);
    }

    /**
     * 续费指定租户，到期时间增加指定年/月/日
     */
    @PostMapping("/renew")
    public Result<Void> renewTenant(@RequestParam Long tenantId,
                                    @RequestParam(required = false) Integer years,
                                    @RequestParam(required = false) Integer months,
                                    @RequestParam(required = false) Integer days) {
        return sysTenantService.renewTenant(tenantId, years, months, days);
    }

    /**
     * 批量续费租户，到期时间增加指定年/月/日，并更新状态
     */
    @PostMapping("/batch-renew")
    @Operation(summary = "批量续费租户", description = "批量续费租户，到期时间增加指定年/月/日，并更新状态")
    public Result<Void> batchRenewTenants(@RequestBody List<Long> tenantIds,
                                         @RequestParam(required = false) Integer years,
                                         @RequestParam(required = false) Integer months,
                                         @RequestParam(required = false) Integer days) {
        return sysTenantService.batchRenewTenants(tenantIds, years, months, days);
    }

    /**
     * 导出租户数据
     */
    @GetMapping("/export")
    @Operation(summary = "导出租户数据", description = "导出租户数据到Excel文件")
    public void exportTenants(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String code,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String packageType,
                              HttpServletResponse response) {
        try {
            // 将字符串参数转换为Integer，确保安全处理
            Integer statusInt = null;
            Integer packageTypeInt = null;
            
            if (status != null && !status.isEmpty() && !"null".equals(status)) {
                try {
                    statusInt = Integer.parseInt(status);
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }
            
            if (packageType != null && !packageType.isEmpty() && !"null".equals(packageType)) {
                try {
                    packageTypeInt = Integer.parseInt(packageType);
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }
            
            sysTenantService.exportTenants(name, code, statusInt, packageTypeInt, response);
        } catch (Exception e) {
            e.printStackTrace();
            // 异常已在Service层处理
        }
    }

    /**
     * 停用指定租户，将status设为0
     */
    @PostMapping("/disable")
    public Result<Void> disableTenant(@RequestParam Long tenantId) {
        return sysTenantService.disableTenant(tenantId);
    }

    /**
     * 启用指定租户，将status设为1
     */
    @PostMapping("/enable")
    public Result<Void> enableTenant(@RequestParam Long tenantId) {
        return sysTenantService.enableTenant(tenantId);
    }
}


