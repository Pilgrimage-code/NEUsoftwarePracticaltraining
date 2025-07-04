package com.cemh.service;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;


import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
     * 批量续费租户，到期时间增加指定年/月/日，并更新状态
     */
    Result<Void> batchRenewTenants(List<Long> tenantIds, Integer years, Integer months, Integer days);

    /**
     * 停用指定租户，将status设为0
     */
    Result<Void> disableTenant(Long tenantId);

    /**
     * 启用指定租户，将status设为1
     */
    Result<Void> enableTenant(Long tenantId);
    
    /**
     * 获取所有有效租户（未删除且状态正常）
     */
    Result<List<SysTenant>> getAllActiveTenants();

    /**
     * 导出租户数据
     */
    void exportTenants(String name, String code, Integer status, Integer packageType, HttpServletResponse response);

    Object getById(Long tenantId);
}

