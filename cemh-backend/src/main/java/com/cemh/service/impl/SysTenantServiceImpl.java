package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.common.PageResult;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.mapper.SysTenantMapper;
import com.cemh.service.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class SysTenantServiceImpl implements SysTenantService {

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Override
    public Result<PageResult<SysTenant>> getTenantList(Integer page, Integer size, String name, String code, Integer status, Integer packageType, Integer deleted) {
        int offset = (page - 1) * size;
        // 多条件查询
        java.util.List<SysTenant> records = sysTenantMapper.selectTenantPage(name, code, status, packageType, deleted, offset, size);
        int total = sysTenantMapper.countTenantPage(name, code, status, packageType, deleted);
        PageResult<SysTenant> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal((long) total);
        pageResult.setCurrent((long) page);
        pageResult.setSize((long) size);
        return Result.success(pageResult);
    }

    @Override
    public Result<Void> createTenant(SysTenant tenant) {
        try {
            // 检查租户编码是否已存在（只检查未删除的记录）
            QueryWrapper<SysTenant> wrapper = new QueryWrapper<>();
            wrapper.eq("tenant_code", tenant.getTenantCode())
                    .eq("deleted", 0);
            SysTenant existingTenant = sysTenantMapper.selectOne(wrapper);
            if (existingTenant != null) {
                return Result.error("租户编码已存在，请使用其他编码");
            }

            // 检查租户名称是否已存在（只检查未删除的记录）
            wrapper = new QueryWrapper<>();
            wrapper.eq("tenant_name", tenant.getTenantName())
                    .eq("deleted", 0);
            existingTenant = sysTenantMapper.selectOne(wrapper);
            if (existingTenant != null) {
                return Result.error("租户名称已存在，请使用其他名称");
            }

            sysTenantMapper.insert(tenant);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("新增租户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> updateTenant(SysTenant tenant) {
        try {
            // 检查租户编码是否已被其他租户使用（只检查未删除的记录）
            QueryWrapper<SysTenant> wrapper = new QueryWrapper<>();
            wrapper.eq("tenant_code", tenant.getTenantCode())
                    .ne("id", tenant.getId())
                    .eq("deleted", 0);
            SysTenant existingTenant = sysTenantMapper.selectOne(wrapper);
            if (existingTenant != null) {
                return Result.error("租户编码已存在，请使用其他编码");
            }

            // 检查租户名称是否已被其他租户使用（只检查未删除的记录）
            wrapper = new QueryWrapper<>();
            wrapper.eq("tenant_name", tenant.getTenantName())
                    .ne("id", tenant.getId())
                    .eq("deleted", 0);
            existingTenant = sysTenantMapper.selectOne(wrapper);
            if (existingTenant != null) {
                return Result.error("租户名称已存在，请使用其他名称");
            }

            sysTenantMapper.updateById(tenant);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新租户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteTenant(Long id) {
        try {
            // 检查租户是否存在
            SysTenant tenant = sysTenantMapper.selectById(id);
            if (tenant == null) {
                return Result.error("租户不存在");
            }

            // 执行逻辑删除
            sysTenantMapper.deleteById(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除租户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> realDeleteTenant(Long id) {
        try {
            int rows = sysTenantMapper.realDeleteTenant(id); // 只删 deleted=1 的数据
            if (rows > 0) {
                return Result.success();
            } else {
                return Result.error("租户不存在或未被删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("彻底删除租户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> restoreTenant(Long id) {
        int rows = sysTenantMapper.restoreTenant(id);
        if (rows > 0) {
            return Result.success();
        } else {
            return Result.error("恢复失败，租户不存在或未被删除");
        }
    }

    @Override
    public Result<Void> renewTenant(Long tenantId, Integer years, Integer months, Integer days) {
        try {
            SysTenant tenant = sysTenantMapper.selectById(tenantId);
            if (tenant == null) {
                return Result.error("租户不存在");
            }
            java.time.LocalDateTime expireTime = tenant.getExpireTime();
            if (expireTime == null) {
                expireTime = java.time.LocalDateTime.now();
            }
            int y = years != null ? years : 0;
            int m = months != null ? months : 0;
            int d = days != null ? days : 0;
            if (y == 0 && m == 0 && d == 0) {
                return Result.error("年/月/日不能全部为空");
            }
            expireTime = expireTime.plusYears(y).plusMonths(m).plusDays(d);
            tenant.setExpireTime(expireTime);
            sysTenantMapper.updateById(tenant);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("续费失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> disableTenant(Long tenantId) {
        try {
            SysTenant tenant = sysTenantMapper.selectById(tenantId);
            if (tenant == null) {
                return Result.error("租户不存在");
            }
            tenant.setStatus(0);
            sysTenantMapper.updateById(tenant);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("停用失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> enableTenant(Long tenantId) {
        try {
            SysTenant tenant = sysTenantMapper.selectById(tenantId);
            if (tenant == null) {
                return Result.error("租户不存在");
            }
            tenant.setStatus(1);
            sysTenantMapper.updateById(tenant);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("启用失败：" + e.getMessage());
        }
    }

    @Override
    public Object getById(Long tenantId) {
        return null;
    }
}

