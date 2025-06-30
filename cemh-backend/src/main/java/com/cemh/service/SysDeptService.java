package com.cemh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cemh.entity.SysDept;
import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    List<SysDept> getByTenantId(Long tenantId);
} 