package com.cemh.service.impl;

import com.cemh.entity.SysDept;
import com.cemh.mapper.SysDeptMapper;
import com.cemh.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Override
    public List<SysDept> getDeptTree(Long tenantId) {
        List<SysDept> depts = sysDeptMapper.selectByTenantId(tenantId);
        return buildTree(depts, 0L);
    }
    private List<SysDept> buildTree(List<SysDept> depts, Long parentId) {
        List<SysDept> tree = new ArrayList<>();
        for (SysDept dept : depts) {
            if ((dept.getParentId() == null && parentId == 0L) || (dept.getParentId() != null && dept.getParentId().equals(parentId))) {
                dept.setChildren(buildTree(depts, dept.getId()));
                tree.add(dept);
            }
        }
        return tree;
    }
} 