package com.cemh.mapper;

import com.cemh.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SysDeptMapper {
    List<SysDept> selectByTenantId(@Param("tenantId") Long tenantId);
} 