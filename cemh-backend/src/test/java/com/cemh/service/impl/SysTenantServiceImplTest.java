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
    void testUpdateTenantSuccess() {
        SysTenant tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantCode("UPDATED");
        tenant.setTenantName("Updated Tenant");

        when(sysTenantMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(sysTenantMapper.updateById(any(SysTenant.class))).thenReturn(1);

        Result<Void> result = sysTenantService.updateTenant(tenant);
        assertTrue(result.isSuccess());
        verify(sysTenantMapper, times(2)).selectOne(any(QueryWrapper.class));
        verify(sysTenantMapper, times(1)).updateById(any(SysTenant.class));
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
}


