package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.entity.SysDept;
import com.cemh.entity.SysTenant;
import com.cemh.service.CaptchaService;
import com.cemh.service.SysDeptService;
import com.cemh.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "租户管理", description = "租户相关接口")
@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class SysTenantController {

    @Autowired
    private SysTenantService sysTenantService;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private CaptchaService captchaService;

    @Operation(summary = "获取所有租户列表", description = "获取所有租户的简要信息")
    @GetMapping("/all")
    public Result<List<SysTenant>> getAllTenants() {
        return sysTenantService.getAllTenants();
    }

    @Operation(summary = "企业注册")
    @PostMapping("/register")
    public Result<SysTenant> registerTenant(@RequestBody Map<String, Object> dto) {
        // 校验验证码
        String captchaKey = (String) dto.get("captchaKey");
        String captcha = (String) dto.get("captcha");
        if (!captchaService.verify(captchaKey, captcha)) {
            return Result.error("验证码错误");
        }
        // 校验租户代码唯一
        String tenantCode = (String) dto.get("tenantCode");
        if (sysTenantService.existsByCode(tenantCode)) {
            return Result.error("租户代码已存在");
        }
        // 保存租户
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode(tenantCode);
        tenant.setTenantName((String) dto.get("tenantName"));
        tenant.setContactPerson((String) dto.get("contactName"));
        tenant.setContactPhone((String) dto.get("contactPhone"));
        tenant.setContactEmail((String) dto.get("contactEmail"));
        tenant.setStatus(1);
        sysTenantService.save(tenant);

        // 插入顶级部门 - 关键修改
        SysDept rootDept = new SysDept();
        rootDept.setTenantId(tenant.getId());
        rootDept.setParentId(null);  // 设置为null
        rootDept.setDeptName(tenant.getTenantName());
        rootDept.setStatus(1);
        sysDeptService.save(rootDept);

        // 插入子部门
        List<String> depts = (List<String>) dto.get("departments");
        if (depts != null) {
            for (String deptName : depts) {
                SysDept dept = new SysDept();
                dept.setTenantId(tenant.getId());
                dept.setParentId(rootDept.getId());
                dept.setDeptName(deptName);
                dept.setStatus(1);
                sysDeptService.save(dept);
            }
        }
        return Result.success(tenant);
    }
}