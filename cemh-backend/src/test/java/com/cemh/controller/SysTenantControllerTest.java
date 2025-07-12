package com.cemh.controller;

import com.cemh.common.PageResult;
import com.cemh.common.Result;
import com.cemh.entity.SysTenant;
import com.cemh.service.SysTenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysTenantControllerTest {

    @InjectMocks
    private SysTenantController sysTenantController;

    @Mock
    private SysTenantService sysTenantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTenantList() {
        // 准备测试数据
        Integer page = 1;
        Integer size = 10;
        String name = "测试租户";
        String code = "TEST";
        Integer status = 1;
        Integer packageType = 2;
        Integer deleted = 0;

        // 模拟服务层返回
        PageResult<SysTenant> pageResult = new PageResult<>();
        pageResult.setRecords(Arrays.asList(new SysTenant(), new SysTenant()));
        pageResult.setTotal(2L);
        Result<PageResult<SysTenant>> expectedResult = Result.success(pageResult);

        when(sysTenantService.getTenantList(page, size, name, code, status, packageType, deleted))
                .thenReturn(expectedResult);

        // 执行测试
        Result<PageResult<SysTenant>> result = sysTenantController.getTenantList(page, size, name, code, status, packageType, deleted);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().getRecords().size());
        assertEquals(2L, result.getData().getTotal());
        verify(sysTenantService, times(1)).getTenantList(page, size, name, code, status, packageType, deleted);
    }

    @Test
    void testCreateTenant() {
        // 准备测试数据
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("TEST001");
        tenant.setTenantName("Test Tenant");

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.createTenant(tenant)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.createTenant(tenant);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).createTenant(tenant);
    }

    @Test
    void testUpdateTenant() {
        // 准备测试数据
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("UPDATED");
        tenant.setTenantName("Updated Tenant");

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.updateTenant(tenant)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.updateTenant(tenantId, tenant);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals(tenantId, tenant.getId());
        verify(sysTenantService, times(1)).updateTenant(tenant);
    }

    @Test
    void testDeleteTenant() {
        // 准备测试数据
        Long tenantId = 1L;

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.deleteTenant(tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.deleteTenant(tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).deleteTenant(tenantId);
    }

    @Test
    void testRealDeleteTenant() {
        // 准备测试数据
        Long tenantId = 1L;

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.realDeleteTenant(tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.realDeleteTenant(tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).realDeleteTenant(tenantId);
    }

    @Test
    void testRestoreTenant() {
        // 准备测试数据
        Long tenantId = 1L;

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.restoreTenant(tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.restoreTenant(tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).restoreTenant(tenantId);
    }

    @Test
    void testRenewTenant() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer years = 1;
        Integer months = 2;
        Integer days = 3;

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.renewTenant(tenantId, years, months, days)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.renewTenant(tenantId, years, months, days);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).renewTenant(tenantId, years, months, days);
    }

    @Test
    void testDisableTenant() {
        // 准备测试数据
        Long tenantId = 1L;

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.disableTenant(tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.disableTenant(tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).disableTenant(tenantId);
    }

    @Test
    void testEnableTenant() {
        // 准备测试数据
        Long tenantId = 1L;

        Result<Void> expectedResult = Result.success();
        when(sysTenantService.enableTenant(tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.enableTenant(tenantId);

        // 验证结果
        assertTrue(result.isSuccess());
        verify(sysTenantService, times(1)).enableTenant(tenantId);
    }

    @Test
    void testCreateTenantFailure() {
        // 准备测试数据
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("EXISTING");
        tenant.setTenantName("Existing Tenant");

        Result<Void> expectedResult = Result.error("租户编码已存在，请使用其他编码");
        when(sysTenantService.createTenant(tenant)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.createTenant(tenant);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("租户编码已存在，请使用其他编码", result.getMessage());
        verify(sysTenantService, times(1)).createTenant(tenant);
    }

    @Test
    void testUpdateTenantFailure() {
        // 准备测试数据
        Long tenantId = 1L;
        SysTenant tenant = new SysTenant();
        tenant.setTenantCode("EXISTING");
        tenant.setTenantName("Existing Tenant");

        Result<Void> expectedResult = Result.error("租户编码已存在，请使用其他编码");
        when(sysTenantService.updateTenant(tenant)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.updateTenant(tenantId, tenant);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("租户编码已存在，请使用其他编码", result.getMessage());
        assertEquals(tenantId, tenant.getId());
        verify(sysTenantService, times(1)).updateTenant(tenant);
    }

    @Test
    void testDeleteTenantFailure() {
        // 准备测试数据
        Long tenantId = 999L;

        Result<Void> expectedResult = Result.error("租户不存在");
        when(sysTenantService.deleteTenant(tenantId)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.deleteTenant(tenantId);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
        verify(sysTenantService, times(1)).deleteTenant(tenantId);
    }

    @Test
    void testRenewTenantWithZeroPeriod() {
        // 准备测试数据
        Long tenantId = 1L;
        Integer years = 0;
        Integer months = 0;
        Integer days = 0;

        Result<Void> expectedResult = Result.error("年/月/日不能全部为空");
        when(sysTenantService.renewTenant(tenantId, years, months, days)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.renewTenant(tenantId, years, months, days);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("年/月/日不能全部为空", result.getMessage());
        verify(sysTenantService, times(1)).renewTenant(tenantId, years, months, days);
    }

    @Test
    void testRenewTenantNotFound() {
        // 准备测试数据
        Long tenantId = 999L;
        Integer years = 1;
        Integer months = 0;
        Integer days = 0;

        Result<Void> expectedResult = Result.error("租户不存在");
        when(sysTenantService.renewTenant(tenantId, years, months, days)).thenReturn(expectedResult);

        // 执行测试
        Result<Void> result = sysTenantController.renewTenant(tenantId, years, months, days);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("租户不存在", result.getMessage());
        verify(sysTenantService, times(1)).renewTenant(tenantId, years, months, days);
    }

    @Test
    void testGetAllActiveTenants_success() {
        Result<List<SysTenant>> expected = Result.success(Arrays.asList(new SysTenant(), new SysTenant()));
        when(sysTenantService.getAllActiveTenants()).thenReturn(expected);
        Result<List<SysTenant>> result = sysTenantController.getAllActiveTenants();
        assertTrue(result.isSuccess());
        assertEquals(2, result.getData().size());
        verify(sysTenantService, times(1)).getAllActiveTenants();
    }

    @Test
    void testGetAllActiveTenants_serviceException() {
        when(sysTenantService.getAllActiveTenants()).thenThrow(new RuntimeException("error"));
        try {
            sysTenantController.getAllActiveTenants();
            fail("应抛出异常");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    void testBatchRenewTenants_success() {
        Result<Void> expected = Result.success();
        when(sysTenantService.batchRenewTenants(anyList(), any(), any(), any())).thenReturn(expected);
        Result<Void> result = sysTenantController.batchRenewTenants(Arrays.asList(1L, 2L), 1, 2, 3);
        assertTrue(result.isSuccess());
        verify(sysTenantService).batchRenewTenants(Arrays.asList(1L, 2L), 1, 2, 3);
    }

    @Test
    void testBatchRenewTenants_serviceError() {
        Result<Void> expected = Result.error("批量续费失败");
        when(sysTenantService.batchRenewTenants(anyList(), any(), any(), any())).thenReturn(expected);
        Result<Void> result = sysTenantController.batchRenewTenants(Arrays.asList(1L, 2L), 1, 2, 3);
        assertFalse(result.isSuccess());
        assertEquals("批量续费失败", result.getMessage());
    }

    @Test
    void testBatchRenewTenants_nullIds() {
        Result<Void> expected = Result.error("ID不能为空");
        when(sysTenantService.batchRenewTenants(isNull(), any(), any(), any())).thenReturn(expected);
        Result<Void> result = sysTenantController.batchRenewTenants(null, 1, 2, 3);
        assertFalse(result.isSuccess());
    }

    @Test
    void testExportTenants_normal() {
        doNothing().when(sysTenantService).exportTenants(any(), any(), any(), any(), any());
        sysTenantController.exportTenants("n", "c", "1", "2", mock(javax.servlet.http.HttpServletResponse.class));
        verify(sysTenantService).exportTenants(eq("n"), eq("c"), eq(1), eq(2), any());
    }

    @Test
    void testExportTenants_statusAndPackageTypeNullOrInvalid() {
        doNothing().when(sysTenantService).exportTenants(any(), any(), any(), any(), any());
        // status/packageType为null
        sysTenantController.exportTenants("n", "c", null, null, mock(javax.servlet.http.HttpServletResponse.class));
        // status/packageType为""
        sysTenantController.exportTenants("n", "c", "", "", mock(javax.servlet.http.HttpServletResponse.class));
        // status/packageType为"null"
        sysTenantController.exportTenants("n", "c", "null", "null", mock(javax.servlet.http.HttpServletResponse.class));
        // status/packageType为非法字符串
        sysTenantController.exportTenants("n", "c", "abc", "xyz", mock(javax.servlet.http.HttpServletResponse.class));
        verify(sysTenantService, times(4)).exportTenants(any(), any(), any(), any(), any());
    }

    @Test
    void testExportTenants_serviceException() {
        doThrow(new RuntimeException("导出异常")).when(sysTenantService).exportTenants(any(), any(), any(), any(), any());
        sysTenantController.exportTenants("n", "c", "1", "2", mock(javax.servlet.http.HttpServletResponse.class));
        verify(sysTenantService).exportTenants(eq("n"), eq("c"), eq(1), eq(2), any());
    }

    @Test
    void testDisableTenant_serviceError() {
        Result<Void> expected = Result.error("停用失败");
        when(sysTenantService.disableTenant(anyLong())).thenReturn(expected);
        Result<Void> result = sysTenantController.disableTenant(1L);
        assertFalse(result.isSuccess());
        assertEquals("停用失败", result.getMessage());
    }

    @Test
    void testEnableTenant_serviceError() {
        Result<Void> expected = Result.error("启用失败");
        when(sysTenantService.enableTenant(anyLong())).thenReturn(expected);
        Result<Void> result = sysTenantController.enableTenant(1L);
        assertFalse(result.isSuccess());
        assertEquals("启用失败", result.getMessage());
    }
} 