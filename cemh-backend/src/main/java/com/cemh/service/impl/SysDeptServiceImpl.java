package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cemh.entity.SysDept;
import com.cemh.mapper.SysDeptMapper;
import com.cemh.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> getByTenantId(Long tenantId) {
        return this.list(new QueryWrapper<SysDept>()
                .eq("tenant_id", tenantId)
                .eq("deleted", 0));
    }

    @Override
    public boolean existsByCode(Long tenantId, String deptCode) {
        return this.count(new QueryWrapper<SysDept>()
                .eq("tenant_id", tenantId)
                .eq("dept_code", deptCode)
                .eq("deleted", 0)) > 0;
    }

    @Override
    public boolean hasChildren(Long parentId) {
        return this.count(new QueryWrapper<SysDept>()
                .eq("parent_id", parentId)
                .eq("deleted", 0)) > 0;
    }

    @Override
    @Transactional
    public boolean removeDept(Long id) {
        SysDept dept = this.getById(id);
        if (dept == null) {
            return false;
        }

        if (this.hasChildren(id)) {
            throw new RuntimeException("请先删除子部门");
        }

        return this.removeById(id);
    }
}