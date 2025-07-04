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
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

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
            
            // 更新状态：如果到期时间大于当前时间，则将状态设为1（正常），否则设为2（过期）
            LocalDateTime now = LocalDateTime.now();
            if (expireTime.isAfter(now)) {
                tenant.setStatus(1);
            } else {
                tenant.setStatus(2);
            }
            
            sysTenantMapper.updateById(tenant);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("续费失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Void> batchRenewTenants(List<Long> tenantIds, Integer years, Integer months, Integer days) {
        try {
            if (tenantIds == null || tenantIds.isEmpty()) {
                return Result.error("未选择任何租户");
            }
            
            int y = years != null ? years : 0;
            int m = months != null ? months : 0;
            int d = days != null ? days : 0;
            
            if (y == 0 && m == 0 && d == 0) {
                return Result.error("年/月/日不能全部为空");
            }
            
            LocalDateTime now = LocalDateTime.now();
            
            for (Long tenantId : tenantIds) {
                SysTenant tenant = sysTenantMapper.selectById(tenantId);
                if (tenant == null) {
                    continue;
                }
                
                LocalDateTime expireTime = tenant.getExpireTime();
                if (expireTime == null) {
                    expireTime = now;
                }
                
                // 增加时间
                expireTime = expireTime.plusYears(y).plusMonths(m).plusDays(d);
                tenant.setExpireTime(expireTime);
                
                // 更新状态：如果到期时间大于当前时间，则将状态设为1（正常），否则设为2（过期）
                if (expireTime.isAfter(now)) {
                    tenant.setStatus(1);
                } else {
                    tenant.setStatus(2);
                }
                
                sysTenantMapper.updateById(tenant);
            }
            
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("批量续费失败：" + e.getMessage());
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
    public Result<List<SysTenant>> getAllActiveTenants() {
        try {
            // 查询所有未删除且状态正常的租户
            QueryWrapper<SysTenant> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", 0).eq("status", 1);
            List<SysTenant> tenants = sysTenantMapper.selectList(wrapper);
            return Result.success(tenants);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取租户列表失败：" + e.getMessage());
        }
    }

    @Override
    public void exportTenants(String name, String code, Integer status, Integer packageType, HttpServletResponse response) {
        try {
            // 构建查询条件
            QueryWrapper<SysTenant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("deleted", 0);
            
            if (name != null && !name.isEmpty()) {
                queryWrapper.like("tenant_name", name);
            }
            
            if (code != null && !code.isEmpty()) {
                queryWrapper.like("tenant_code", code);
            }
            
            if (status != null) {
                queryWrapper.eq("status", status);
            }
            
            if (packageType != null) {
                queryWrapper.eq("create_by", packageType);
            }
            
            // 查询租户列表
            List<SysTenant> tenants = sysTenantMapper.selectList(queryWrapper);
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=tenants_" + System.currentTimeMillis() + ".xls");
            
            // 创建工作簿 - 使用HSSFWorkbook
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("租户数据");
            
            // 创建标题行
            HSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"租户编码", "租户名称", "联系人", "联系电话", "联系邮箱", "状态", "套餐类型", "最大用户数", "到期时间", "创建时间"};
            
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 填充数据
            for (int i = 0; i < tenants.size(); i++) {
                SysTenant tenant = tenants.get(i);
                HSSFRow row = sheet.createRow(i + 1);
                
                row.createCell(0).setCellValue(tenant.getTenantCode() != null ? tenant.getTenantCode() : "");
                row.createCell(1).setCellValue(tenant.getTenantName() != null ? tenant.getTenantName() : "");
                row.createCell(2).setCellValue(tenant.getContactName() != null ? tenant.getContactName() : "");
                row.createCell(3).setCellValue(tenant.getContactPhone() != null ? tenant.getContactPhone() : "");
                row.createCell(4).setCellValue(tenant.getContactEmail() != null ? tenant.getContactEmail() : "");
                row.createCell(5).setCellValue(getStatusText(tenant.getStatus()));
                row.createCell(6).setCellValue(getPackageTypeText(tenant.getCreateBy()));
                row.createCell(7).setCellValue(tenant.getMaxUsers() != null ? tenant.getMaxUsers() : 0);
                row.createCell(8).setCellValue(tenant.getExpireTime() != null ? tenant.getExpireTime().toString() : "");
                row.createCell(9).setCellValue(tenant.getCreateTime() != null ? tenant.getCreateTime().toString() : "");
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 输出文件
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常
            try {
                response.reset();
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("导出失败：" + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "停用";
            case 1: return "正常";
            case 2: return "过期";
            default: return "未知";
        }
    }
    
    /**
     * 获取套餐类型文本
     */
    private String getPackageTypeText(Integer packageType) {
        if (packageType == null) return "未知";
        switch (packageType) {
            case 1: return "基础版";
            case 2: return "专业版";
            case 3: return "企业版";
            default: return "未知";
        }
    }
    
    /**
     * 获取套餐类型文本 - 重载方法，支持Long类型参数
     */
    private String getPackageTypeText(Long packageType) {
        if (packageType == null) return "未知";
        return getPackageTypeText(packageType.intValue());
    }

    @Override
    public Object getById(Long tenantId) {
        return sysTenantMapper.selectById(tenantId);
    }
}

