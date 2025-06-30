package com.cemh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;

import java.util.List;

public interface SysTenantService extends IService<SysTenant> {
    Result<List<SysTenant>> getAllTenants();
    boolean existsByCode(String tenantCode);
}


