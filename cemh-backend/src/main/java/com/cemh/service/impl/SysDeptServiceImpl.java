package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cemh.entity.SysDept;
import com.cemh.mapper.SysDeptMapper;
import com.cemh.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Override
    public List<SysDept> getByTenantId(Long tenantId) {
        return this.list(new QueryWrapper<SysDept>().eq("tenant_id", tenantId));
    }
} 