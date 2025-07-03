package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;

public interface SysTenantService {
    Result<PageResult<SysTenant>> getTenantList(Integer page, Integer size, String name, String code, Integer status, Integer packageType, Integer deleted);
    Result<Void> createTenant(SysTenant tenant);
    Result<Void> updateTenant(SysTenant tenant);
    Result<Void> deleteTenant(Long id);
    Result<Void> realDeleteTenant(Long id);

    /**
     * 恢复被逻辑删除的租户
     */
    Result<Void> restoreTenant(Long id);

    /**
     * 续费指定租户，到期时间增加指定年/月/日
     */
    Result<Void> renewTenant(Long tenantId, Integer years, Integer months, Integer days);

    /**
     * 停用指定租户，将status设为0
     */
    Result<Void> disableTenant(Long tenantId);

    /**
     * 启用指定租户，将status设为1
     */
    Result<Void> enableTenant(Long tenantId);
}


