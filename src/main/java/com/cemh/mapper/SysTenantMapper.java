package com.cemh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cemh.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户信息Mapper接口
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2024-06-16
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {
    
}

