package com.cemh.service;

import com.cemh.entity.SysDept;
import java.util.List;

public interface SysDeptService {
    List<SysDept> getDeptTree(Long tenantId);
} 