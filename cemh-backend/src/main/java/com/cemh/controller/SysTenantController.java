package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


