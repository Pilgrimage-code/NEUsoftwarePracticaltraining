package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.mapper.SysTenantMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysTenantServiceImplTest {

    @InjectMocks
    private SysTenantServiceImpl sysTenantService;

    @Mock
    private SysTenantMapper sysTenantMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTenantList() {
        Integer page = 1;
        Integer size = 10;
        String name = null;
        String code = null;
        Integer status = null;
        Integer packageType = null;
        Integer deleted = 0;

        List<SysTenant> tenants = Arrays.asList(new SysTenant(), new SysTenant());

        when(sysTenantMapper.selectTenantPage(name, code, status, packageType, deleted, (page - 1) * size, size)).thenReturn(tenants);
        when(sysTenantMapper.countTenantPage(name, code, status, packageType, deleted)).thenReturn(2);

        Result<PageResult<SysTenant>> result = sysTenantService.getTenantList(page, size, name, code, status, packageType, deleted);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        verify(sysTenantMapper, times(1)).selectTenantPage(name, code, status, packageType, deleted, (page - 1) * size, size);
        verify(sysTenantMapper, times(1)).countTenantPage(name, code, status, packageType, deleted);
    }

    @Test
    void testGetTenantListWithFilters() {
        Integer page = 1;
        Integer size = 10;
        String name = "测试租户";
        String code = "TEST";
        Integer status = 1;
        Integer packageType = 2;
        Integer deleted = 0;

        List<SysTenant> tenants = Collections.singletonList(new SysTenant());

        when(sysTenantMapper.selectTenantPage(name, code, status, packageType, deleted, (page - 1) * size, size)).thenReturn(tenants);
        when(sysTenantMapper.countTenantPage(name, code, status, packageType, deleted)).thenReturn(1);

        Result<PageResult<SysTenant>> result = sysTenantService.getTenantList(page, size, name, code, status, packageType, deleted);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getRecords().size());
        assertEquals(1, result.getData().getTotal());
        assertEquals(page.longValue(), result.getData().getCurrent());
        assertEquals(size.longValue(), result.getData().getSize());
    }

    @Test
    void testCreateTenantSuccess() {
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("TEST001");
        tenant.setTenantName("Test Tenant");

        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysTenantMapper.insert(any(SysTenant.class))).thenReturn(1);

        Result<Void> result = sysTenantService.createTenant(tenant);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, times(1)).insert(any(SysTenant.class));
    }

    @Test
    void testCreateTenantCodeExists() {
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("EXISTING");
        tenant.setTenantName("Test Tenant");

        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(new SysTenant());

        Result<Void> result = sysTenantService.createTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("租户编码已存在，请使用其他编码", result.getMessage());
        verify(sysTenantMapper, times(1)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, never()).insert(any(SysTenant.class));
    }

    @Test
    void testCreateTenantNameExists() {
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("NEW_CODE");
        tenant.setTenantName("Existing Name");

        // 第一次查询返回null（编码不存在）
        // 第二次查询返回已存在的租户（名称存在）
        when(sysTenantMapper.selectOne(any(QueryWrapper.class)))
            .thenReturn(null)
            .thenReturn(new SysTenant());

        Result<Void> result = sysTenantService.createTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("租户名称已存在，请使用其他名称", result.getMessage());
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, never()).insert(any(SysTenant.class));
    }

    @Test
    void testCreateTenantException() {
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("TEST001");
        tenant.setTenantName("Test Tenant");

        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysTenantMapper.insert(any(SysTenant.class))).thenThrow(new RuntimeException("数据库错误"));

        Result<Void> result = sysTenantService.createTenant(tenant);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("新增租户失败"));
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, times(1)).insert(any(SysTenant.class));
    }

    @Test
    void testUpdateTenantSuccess() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("UPDATED");
        tenant.setTenantName("Updated Tenant");
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(tenant);
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
    }

    @Test
    void testUpdateTenantCodeExists() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("EXISTING");
        tenant.setTenantName("Updated Tenant");
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(tenant);
        SysTenant existingTenant = new SysTenant();
        existingTenant.setId(2L); // 不同的ID
        existingTenant.setTenantCode("EXISTING");
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(existingTenant);
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("租户编码已存在，请使用其他编码", result.getMessage());
        verify(sysTenantMapper, times(1)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, never()).updateById(any(SysTenant.class));
    }

    @Test
    void testUpdateTenantNameExists() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("CODE1");
        tenant.setTenantName("EXISTING_NAME");
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(tenant);
        // 第一次查询返回null（编码不存在）
        // 第二次查询返回已存在的租户（名称存在）
        when(sysTenantMapper.selectOne(any(QueryWrapper.class)))
            .thenReturn(null)
            .thenReturn(new SysTenant());
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("租户名称已存在，请使用其他名称", result.getMessage());
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, never()).updateById(any(SysTenant.class));
    }

    @Test
    void testUpdateTenantException() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("UPDATED");
        tenant.setTenantName("Updated Tenant");
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(tenant);
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenThrow(new RuntimeException("数据库错误"));
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("更新租户失败"));
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
    }

    @Test
    void testUpdateTenant_nullTenant() {
        Result<Void> result = sysTenantService.updateTenant(null);
        assertFalse(result.isSuccess());
        assertEquals("租户ID不能为空", result.getMessage());
    }

    @Test
    void testUpdateTenant_nullId() {
        SysTenant tenant = new SysTenant();
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("租户ID不能为空", result.getMessage());
    }

    @Test
    void testUpdateTenant_nullExpireTime() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("到期时间不能为空", result.getMessage());
    }

    @Test
    void testUpdateTenant_notFound() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(null);
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
    }

    @Test
    void testUpdateTenant_updateReturn0() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("CODE");
        tenant.setTenantName("NAME");
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        SysTenant original = new SysTenant();
        original.setId(1L);
        original.setCreateTime(LocalDateTime.now());
        original.setCreateBy(2L);
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(original);
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(0);
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertEquals("更新失败", result.getMessage());
    }

    @Test
    void testUpdateTenant_catchException() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("CODE");
        tenant.setTenantName("NAME");
        tenant.setExpireTime(LocalDateTime.now().plusDays(1));
        SysTenant original = new SysTenant();
        original.setId(1L);
        original.setCreateTime(LocalDateTime.now());
        original.setCreateBy(2L);
        when(sysTenantMapper.selectById(tenant.getId())).thenReturn(original);
        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenThrow(new RuntimeException("mock异常"));
        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("更新租户失败"));
    }

    @Test
    void testDeleteTenantSuccess() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.deleteById(tenantId)).thenReturn(1);

        Result<Void> result = sysTenantService.deleteTenant(tenantId);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(1)).selectById(tenantId);
        verify(sysTenantMapper, times(1)).deleteById(tenantId);
    }

    @Test
    void testDeleteTenantNotFound() {
        Long tenantId = 1L;
        when(sysTenantMapper.selectById(tenantId)).thenReturn(null);

        Result<Void> result = sysTenantService.deleteTenant(tenantId);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
        verify(sysTenantMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteTenantException() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.deleteById(tenantId)).thenThrow(new RuntimeException("数据库错误"));

        Result<Void> result = sysTenantService.deleteTenant(tenantId);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("删除租户失败"));
        verify(sysTenantMapper, times(1)).selectById(tenantId);
        verify(sysTenantMapper, times(1)).deleteById(tenantId);
    }

    @Test
    void testRealDeleteTenantSuccess() {
        Long tenantId = 1L;
        when(sysTenantMapper.realDeleteTenant(tenantId)).thenReturn(1);

        Result<Void> result = sysTenantService.realDeleteTenant(tenantId);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(1)).realDeleteTenant(tenantId);
    }

    @Test
    void testRealDeleteTenantNotFound() {
        Long tenantId = 1L;
        when(sysTenantMapper.realDeleteTenant(tenantId)).thenReturn(0);

        Result<Void> result = sysTenantService.realDeleteTenant(tenantId);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在或未被删除", result.getMessage());
        verify(sysTenantMapper, times(1)).realDeleteTenant(tenantId);
    }

    @Test
    void testRealDeleteTenantException() {
        Long tenantId = 1L;
        when(sysTenantMapper.realDeleteTenant(tenantId)).thenThrow(new RuntimeException("数据库错误"));

        Result<Void> result = sysTenantService.realDeleteTenant(tenantId);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("彻底删除租户失败"));
        verify(sysTenantMapper, times(1)).realDeleteTenant(tenantId);
    }

    @Test
    void testRestoreTenantSuccess() {
        Long tenantId = 1L;
        when(sysTenantMapper.restoreTenant(tenantId)).thenReturn(1);

        Result<Void> result = sysTenantService.restoreTenant(tenantId);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(1)).restoreTenant(tenantId);
    }

    @Test
    void testRestoreTenantNotFound() {
        Long tenantId = 1L;
        when(sysTenantMapper.restoreTenant(tenantId)).thenReturn(0);

        Result<Void> result = sysTenantService.restoreTenant(tenantId);
        assertFalse(result.isSuccess());
        assertEquals("恢复失败，租户不存在或未被删除", result.getMessage());
        verify(sysTenantMapper, times(1)).restoreTenant(tenantId);
    }

    @Test
    void testRenewTenantSuccess() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setExpireTime(LocalDateTime.now().minusDays(10));

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);

        Result<Void> result = sysTenantService.renewTenant(tenantId, 1, 0, 0);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(1)).selectById(tenantId);
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
        assertNotNull(tenant.getExpireTime());
    }

    @Test
    void testRenewTenantWithNullExpireTime() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setExpireTime(null); // 空的过期时间

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);

        Result<Void> result = sysTenantService.renewTenant(tenantId, 1, 2, 3);
        assertTrue(result.isSuccess());
        
        // 验证从当前时间开始计算
        ArgumentCaptor<SysTenant> tenantCaptor = ArgumentCaptor.forClass(SysTenant.class);
        verify(sysTenantMapper).updateById(tenantCaptor.capture());
        assertNotNull(tenantCaptor.getValue().getExpireTime());
    }

    @Test
    void testRenewTenantWithZeroPeriod() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);

        Result<Void> result = sysTenantService.renewTenant(tenantId, 0, 0, 0);
        assertFalse(result.isSuccess());
        assertEquals("年/月/日不能全部为空", result.getMessage());
        verify(sysTenantMapper, never()).updateById(any(SysTenant.class));
    }

    @Test
    void testRenewTenantNotFound() {
        Long tenantId = 1L;
        when(sysTenantMapper.selectById(tenantId)).thenReturn(null);

        Result<Void> result = sysTenantService.renewTenant(tenantId, 1, 0, 0);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
        verify(sysTenantMapper, never()).updateById(any(SysTenant.class));
    }

    @Test
    void testRenewTenantException() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setExpireTime(LocalDateTime.now());

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenThrow(new RuntimeException("数据库错误"));

        Result<Void> result = sysTenantService.renewTenant(tenantId, 1, 0, 0);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("续费失败"));
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
    }

    @Test
    void testRenewTenant_notFound() {
        when(sysTenantMapper.selectById(1L)).thenReturn(null);
        Result<Void> result = sysTenantService.renewTenant(1L, 1, 0, 0);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
    }

    @Test
    void testRenewTenant_expireTimeNull() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setExpireTime(null);
        when(sysTenantMapper.selectById(1L)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);
        Result<Void> result = sysTenantService.renewTenant(1L, 1, 0, 0);
        assertTrue(result.isSuccess());
    }

    @Test
    void testRenewTenant_zeroPeriod() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setExpireTime(LocalDateTime.now());
        when(sysTenantMapper.selectById(1L)).thenReturn(tenant);
        Result<Void> result = sysTenantService.renewTenant(1L, 0, 0, 0);
        assertFalse(result.isSuccess());
        assertEquals("年/月/日不能全部为空", result.getMessage());
    }

    @Test
    void testRenewTenant_expiredStatus() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setExpireTime(LocalDateTime.now().minusDays(10));
        when(sysTenantMapper.selectById(1L)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);
        Result<Void> result = sysTenantService.renewTenant(1L, 0, 0, -1); // 续费为负，保证到期时间仍小于当前
        assertTrue(result.isSuccess());
        assertEquals(2, tenant.getStatus());
    }

    @Test
    void testRenewTenant_catchException() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setExpireTime(LocalDateTime.now());
        when(sysTenantMapper.selectById(1L)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenThrow(new RuntimeException("mock异常"));
        Result<Void> result = sysTenantService.renewTenant(1L, 1, 0, 0);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("续费失败"));
    }

    @Test
    void testDisableTenantSuccess() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setStatus(1);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);

        Result<Void> result = sysTenantService.disableTenant(tenantId);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(1)).selectById(tenantId);
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
        assertEquals(0, tenant.getStatus());
    }

    @Test
    void testDisableTenantNotFound() {
        Long tenantId = 1L;
        when(sysTenantMapper.selectById(tenantId)).thenReturn(null);

        Result<Void> result = sysTenantService.disableTenant(tenantId);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
        verify(sysTenantMapper, never()).updateById(any(SysTenant.class));
    }

    @Test
    void testDisableTenantException() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setStatus(1);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenThrow(new RuntimeException("数据库错误"));

        Result<Void> result = sysTenantService.disableTenant(tenantId);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("停用失败"));
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
    }

    @Test
    void testDisableTenant_catchException() {
        when(sysTenantMapper.selectById(1L)).thenThrow(new RuntimeException("mock异常"));
        Result<Void> result = sysTenantService.disableTenant(1L);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("停用失败"));
    }

    @Test
    void testEnableTenantSuccess() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setStatus(0);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);

        Result<Void> result = sysTenantService.enableTenant(tenantId);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(1)).selectById(tenantId);
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
        assertEquals(1, tenant.getStatus());
    }

    @Test
    void testEnableTenantNotFound() {
        Long tenantId = 1L;
        when(sysTenantMapper.selectById(tenantId)).thenReturn(null);

        Result<Void> result = sysTenantService.enableTenant(tenantId);
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
        verify(sysTenantMapper, never()).updateById(any(SysTenant.class));
    }

    @Test
    void testEnableTenantException() {
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setId(tenantId);
        tenant.setStatus(0);

        when(sysTenantMapper.selectById(tenantId)).thenReturn(tenant);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenThrow(new RuntimeException("数据库错误"));

        Result<Void> result = sysTenantService.enableTenant(tenantId);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("启用失败"));
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
    }

    @Test
    void testEnableTenant_catchException() {
        when(sysTenantMapper.selectById(1L)).thenThrow(new RuntimeException("mock异常"));
        Result<Void> result = sysTenantService.enableTenant(1L);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("启用失败"));
    }

    @Test
    void testGetAllActiveTenants_catchException() {
        when(sysTenantMapper.selectList(any(QueryWrapper.class))).thenThrow(new RuntimeException("mock异常"));
        Result<List<SysTenant>> result = sysTenantService.getAllActiveTenants();
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("获取租户列表失败"));
    }

    @Test
    void testExportTenants_catchException() {
        javax.servlet.http.HttpServletResponse response = mock(javax.servlet.http.HttpServletResponse.class);
        doThrow(new RuntimeException("mock异常")).when(sysTenantMapper).selectList(any(QueryWrapper.class));
        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            when(response.getWriter()).thenReturn(pw);
        } catch (Exception ignored) {}
        sysTenantService.exportTenants("n", "c", 1, 2, response);
        // 只要不抛出异常即可
    }

    @Test
    void testExportTenants_normal() throws Exception {
        javax.servlet.http.HttpServletResponse response = mock(javax.servlet.http.HttpServletResponse.class);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new javax.servlet.ServletOutputStream() {
            @Override public void write(int b) { baos.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(javax.servlet.WriteListener writeListener) {}
        });
        doNothing().when(response).setContentType(anyString());
        doNothing().when(response).setHeader(anyString(), anyString());
        // mock租户数据，覆盖所有字段
        SysTenant t1 = new SysTenant();
        t1.setTenantCode("A"); t1.setTenantName("B"); t1.setContactName("C"); t1.setContactPhone("D"); t1.setContactEmail("E"); t1.setStatus(1); t1.setCreateBy(2L); t1.setMaxUsers(10); t1.setExpireTime(java.time.LocalDateTime.now()); t1.setCreateTime(java.time.LocalDateTime.now());
        SysTenant t2 = new SysTenant();
        t2.setTenantCode(null); t2.setTenantName(null); t2.setContactName(null); t2.setContactPhone(null); t2.setContactEmail(null); t2.setStatus(null); t2.setCreateBy(null); t2.setMaxUsers(null); t2.setExpireTime(null); t2.setCreateTime(null);
        when(sysTenantMapper.selectList(any(QueryWrapper.class))).thenReturn(java.util.Arrays.asList(t1, t2));
        sysTenantService.exportTenants("n", "c", 1, 2, response);
        verify(response, atLeastOnce()).setContentType(anyString());
        verify(response, atLeastOnce()).setHeader(anyString(), anyString());
    }

    @Test
    void testGetAllActiveTenants_success() {
        SysTenant t1 = new SysTenant();
        SysTenant t2 = new SysTenant();
        when(sysTenantMapper.selectList(any(QueryWrapper.class))).thenReturn(java.util.Arrays.asList(t1, t2));
        Result<java.util.List<SysTenant>> result = sysTenantService.getAllActiveTenants();
        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().size());
    }

    @Test
    void testBatchRenewTenants_allNormal() {
        SysTenant t1 = new SysTenant(); t1.setExpireTime(java.time.LocalDateTime.now());
        SysTenant t2 = new SysTenant(); t2.setExpireTime(java.time.LocalDateTime.now());
        when(sysTenantMapper.selectById(1L)).thenReturn(t1);
        when(sysTenantMapper.selectById(2L)).thenReturn(t2);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);
        Result<Void> result = sysTenantService.batchRenewTenants(java.util.Arrays.asList(1L, 2L), 1, 2, 3);
        assertTrue(result.isSuccess());
    }

    @Test
    void testBatchRenewTenants_expireTimeNull() {
        SysTenant t1 = new SysTenant(); t1.setExpireTime(null);
        SysTenant t2 = new SysTenant(); t2.setExpireTime(java.time.LocalDateTime.now());
        when(sysTenantMapper.selectById(1L)).thenReturn(t1);
        when(sysTenantMapper.selectById(2L)).thenReturn(t2);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);
        Result<Void> result = sysTenantService.batchRenewTenants(java.util.Arrays.asList(1L, 2L), 1, 0, 0);
        assertTrue(result.isSuccess());
    }

    @Test
    void testGetStatusText_allCases() throws Exception {
        java.lang.reflect.Method m = SysTenantServiceImpl.class.getDeclaredMethod("getStatusText", Integer.class);
        m.setAccessible(true);
        assertEquals("停用", m.invoke(sysTenantService, 0));
        assertEquals("正常", m.invoke(sysTenantService, 1));
        assertEquals("过期", m.invoke(sysTenantService, 2));
        assertEquals("未知", m.invoke(sysTenantService, (Object) null));
    }

    @Test
    void testGetPackageTypeText_allCases() throws Exception {
        java.lang.reflect.Method m = SysTenantServiceImpl.class.getDeclaredMethod("getPackageTypeText", Integer.class);
        m.setAccessible(true);
        assertEquals("基础版", m.invoke(sysTenantService, 1));
        assertEquals("专业版", m.invoke(sysTenantService, 2));
        assertEquals("企业版", m.invoke(sysTenantService, 3));
        assertEquals("未知", m.invoke(sysTenantService, (Object) null));
    }

    @Test
    void testGetPackageTypeTextLong_allCases() throws Exception {
        java.lang.reflect.Method m = SysTenantServiceImpl.class.getDeclaredMethod("getPackageTypeText", Long.class);
        m.setAccessible(true);
        assertEquals("基础版", m.invoke(sysTenantService, 1L));
        assertEquals("专业版", m.invoke(sysTenantService, 2L));
        assertEquals("企业版", m.invoke(sysTenantService, 3L));
        assertEquals("未知", m.invoke(sysTenantService, (Object) null));
    }

    @Test
    void testExportTenants_statusAndPackageTypeAllCases() throws Exception {
        javax.servlet.http.HttpServletResponse response = mock(javax.servlet.http.HttpServletResponse.class);
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new javax.servlet.ServletOutputStream() {
            @Override public void write(int b) { baos.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(javax.servlet.WriteListener writeListener) {}
        });
        doNothing().when(response).setContentType(anyString());
        doNothing().when(response).setHeader(anyString(), anyString());
        // mock租户数据，覆盖所有状态/套餐类型分支
        int[] statusArr = {0, 1, 2, 99};
        int[] pkgArr = {1, 2, 3, 99};
        java.util.List<SysTenant> tenants = new java.util.ArrayList<>();
        for (int s : statusArr) {
            for (int p : pkgArr) {
                SysTenant t = new SysTenant();
                t.setTenantCode("A");
                t.setTenantName("B");
                t.setContactName("C");
                t.setContactPhone("D");
                t.setContactEmail("E");
                t.setStatus(s);
                t.setCreateBy((long)p);
                t.setMaxUsers(10);
                t.setExpireTime(java.time.LocalDateTime.now());
                t.setCreateTime(java.time.LocalDateTime.now());
                tenants.add(t);
            }
        }
        when(sysTenantMapper.selectList(any(QueryWrapper.class))).thenReturn(tenants);
        sysTenantService.exportTenants("n", "c", 1, 2, response);
        verify(response, atLeastOnce()).setContentType(anyString());
        verify(response, atLeastOnce()).setHeader(anyString(), anyString());
    }
}


