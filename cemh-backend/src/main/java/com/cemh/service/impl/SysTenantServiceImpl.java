package com.cemh.service.impl;

import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.mapper.SysTenantMapper;
import com.cemh.service.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTenantServiceImpl implements SysTenantService {

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Override
    public Result<List<SysTenant>> getAllTenants() {
        List<SysTenant> tenants = sysTenantMapper.selectList(null);
        return Result.success(tenants);
    }
}


