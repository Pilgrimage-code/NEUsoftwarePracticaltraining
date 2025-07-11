package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 租户信息Mapper接口
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {
    /**
     * 自定义分页查询租户（多条件）
     */
    List<SysTenant> selectTenantPage(@org.apache.ibatis.annotations.Param("name") String name,
                                     @org.apache.ibatis.annotations.Param("code") String code,
                                     @org.apache.ibatis.annotations.Param("status") Integer status,
                                     @org.apache.ibatis.annotations.Param("packageType") Integer packageType,
                                     @org.apache.ibatis.annotations.Param("deleted") Integer deleted,
                                     @org.apache.ibatis.annotations.Param("offset") int offset,
                                     @org.apache.ibatis.annotations.Param("size") int size);

    /**
     * 查询总数（多条件）
     */
    int countTenantPage(@org.apache.ibatis.annotations.Param("name") String name,
                       @org.apache.ibatis.annotations.Param("code") String code,
                       @org.apache.ibatis.annotations.Param("status") Integer status,
                       @org.apache.ibatis.annotations.Param("packageType") Integer packageType,
                       @org.apache.ibatis.annotations.Param("deleted") Integer deleted);

    /**
     * 恢复被逻辑删除的租户
     */
    int restoreTenant(@org.apache.ibatis.annotations.Param("id") Long id);

    /**
     * 彻底物理删除被逻辑删除的租户
     */
    int realDeleteTenant(@org.apache.ibatis.annotations.Param("id") Long id);
}

