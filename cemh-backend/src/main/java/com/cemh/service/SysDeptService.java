
package com.cemh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cemh.entity.SysDept;
import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    List<SysDept> getByTenantId(Long tenantId);
    boolean existsByCode(Long tenantId, String deptCode); // 修复方法名
    boolean hasChildren(Long parentId);
    boolean removeDept(Long id); // 添加删除方法
}
