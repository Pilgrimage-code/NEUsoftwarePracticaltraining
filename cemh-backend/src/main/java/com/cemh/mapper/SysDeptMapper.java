package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.SysDept;
import java.util.List;

public interface SysDeptMapper extends BaseMapper<SysDept> {
    // 可以添加自定义SQL方法
    List<SysDept> selectByTenantId(Long tenantId);
}