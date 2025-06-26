package com.cemh.service;

import com.cemh.common.Result;
import com.cemh.entity.SysTenant;

import java.util.List;

public interface SysTenantService {
    Result<List<SysTenant>> getAllTenants();
}


