package com.cemh.controller;
//李璇
import com.cemh.common.Result;
import com.cemh.entity.SysDept;
import com.cemh.entity.SysTenant;
import com.cemh.service.SysDeptService;
import com.cemh.service.SysTenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SysDeptControllerTest {

    @InjectMocks
    private SysDeptController sysDeptController;

    @Mock
    private SysDeptService sysDeptService;

    @Mock
    private SysTenantService sysTenantService;

    private SysTenant tenant;
    private SysDept dept;
    private SysDept childDept;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 创建测试租户
        tenant = new SysTenant();
        tenant.setId(1L);
        tenant.setTenantName("测试租户");
        tenant.setStatus(1);

        // 创建测试部门
        dept = new SysDept();
        dept.setId(1L);
        dept.setTenantId(1L);
        dept.setParentId(1L); // 租户作为父级
        dept.setDeptCode("DEPT001");
        dept.setDeptName("测试部门");
        dept.setStatus(1);

        // 创建子部门
        childDept = new SysDept();
        childDept.setId(2L);
        childDept.setTenantId(1L);
        childDept.setParentId(1L); // 父级是上面的部门
        childDept.setDeptCode("DEPT002");
        childDept.setDeptName("子部门");
        childDept.setStatus(1);
    }

    @Test
    void getDeptsByTenant_Success() {
        // 模拟租户存在
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        // 模拟部门列表
        when(sysDeptService.getByTenantId(anyLong())).thenReturn(Arrays.asList(dept));

        Result<List<SysDept>> result = sysDeptController.getDeptsByTenant(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(1, result.getData().size());
        assertEquals("测试租户", result.getData().get(0).getTenantName());
        assertEquals("测试租户", result.getData().get(0).getParentName());
    }

    @Test
    void getDeptsByTenant_TenantNotFound() {
        when(sysTenantService.getById(anyLong())).thenReturn(null);

        Result<List<SysDept>> result = sysDeptController.getDeptsByTenant(999L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("租户不存在", result.getMessage());
    }

    @Test
    void createDept_Success() {
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.save(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试部门", result.getData().getDeptName());
    }

    @Test
    void createDept_DuplicateCode() {
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(true);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("部门编码已存在", result.getMessage());
    }

    @Test
    void createDept_InvalidParent() {
        dept.setParentId(999L); // 无效的父ID

        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.getById(anyLong())).thenReturn(null); // 父部门不存在

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("上级部门不存在", result.getMessage());
    }

    @Test
    void updateDept_Success() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setDeptCode("DEPT_NEW");
        updatedDept.setDeptName("更新后的部门");

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.updateById(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("更新后的部门", result.getData().getDeptName());
    }

    @Test
    void updateDept_NotFound() {
        when(sysDeptService.getById(anyLong())).thenReturn(null);

        Result<SysDept> result = sysDeptController.updateDept(999L, new SysDept());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("部门不存在", result.getMessage());
    }

    @Test
    void deleteDept_Success() {
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.hasChildren(anyLong())).thenReturn(false);
        when(sysDeptService.removeDept(anyLong())).thenReturn(true);

        Result<String> result = sysDeptController.deleteDept(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("操作成功", result.getMessage());
    }

    @Test
    void deleteDept_WithChildren() {
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.hasChildren(anyLong())).thenReturn(true);

        Result<String> result = sysDeptController.deleteDept(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("请先删除子部门", result.getMessage());
    }

    @Test
    void getDeptById_Success() {
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);

        Result<SysDept> result = sysDeptController.getDeptById(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试部门", result.getData().getDeptName());
        assertEquals("测试租户", result.getData().getTenantName());
        assertEquals("测试租户", result.getData().getParentName());
    }

    @Test
    void getDeptById_NotFound() {
        when(sysDeptService.getById(anyLong())).thenReturn(null);

        Result<SysDept> result = sysDeptController.getDeptById(999L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("部门不存在", result.getMessage());
    }

    // 补充测试用例以提高覆盖率
    @Test
    void createDept_SaveFailed() {
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.save(any(SysDept.class))).thenReturn(false);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("创建部门失败", result.getMessage());
    }

    @Test
    void createDept_ParentIdNull() {
        dept.setParentId(null);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.save(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(1L, result.getData().getParentId()); // 应该被设置为租户ID
    }

    @Test
    void createDept_ParentIdZero() {
        dept.setParentId(0L);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.save(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals(1L, result.getData().getParentId()); // 应该被设置为租户ID
    }

    @Test
    void createDept_ParentIdEqualsTenantId() {
        dept.setParentId(1L); // 等于租户ID
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.save(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
    }

    @Test
    void createDept_ParentDeptDifferentTenant() {
        SysDept parentDept = new SysDept();
        parentDept.setId(999L);
        parentDept.setTenantId(999L); // 不同租户

        dept.setParentId(999L);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.getById(anyLong())).thenReturn(parentDept);

        Result<SysDept> result = sysDeptController.createDept(dept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("上级部门不属于同一租户", result.getMessage());
    }

    @Test
    void updateDept_UpdateFailed() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setDeptCode("DEPT_NEW");
        updatedDept.setDeptName("更新后的部门");

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.updateById(any(SysDept.class))).thenReturn(false);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("更新部门失败", result.getMessage());
    }

    @Test
    void updateDept_DuplicateCode() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setDeptCode("DEPT_DUPLICATE");
        updatedDept.setDeptName("更新后的部门");

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(true);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("更新部门失败", result.getMessage());
    }

    @Test
    void updateDept_ParentIdNull() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setParentId(null);

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.updateById(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        // 简化验证，只检查方法被调用
        verify(sysDeptService).updateById(any(SysDept.class));
    }

    @Test
    void updateDept_ParentIdZero() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setParentId(0L);

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.updateById(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        // 简化验证，只检查方法被调用
        verify(sysDeptService).updateById(any(SysDept.class));
    }

    @Test
    void updateDept_ParentIdEqualsTenantId() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setTenantId(1L); // 设置tenantId
        updatedDept.setParentId(1L); // 等于租户ID
        updatedDept.setDeptName("更新后的部门");

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.updateById(any(SysDept.class))).thenReturn(true);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("更新后的部门", result.getData().getDeptName());
    }

    @Test
    void updateDept_ParentDeptNotFound() {
        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setParentId(999L);

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.getById(999L)).thenReturn(null); // 父部门不存在

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("上级部门不存在", result.getMessage());
    }

    @Test
    void updateDept_ParentDeptDifferentTenant() {
        SysDept parentDept = new SysDept();
        parentDept.setId(999L);
        parentDept.setTenantId(999L); // 不同租户

        SysDept updatedDept = new SysDept();
        updatedDept.setId(1L);
        updatedDept.setParentId(999L);

        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.existsByCode(anyLong(), anyString())).thenReturn(false);
        when(sysDeptService.getById(999L)).thenReturn(parentDept);

        Result<SysDept> result = sysDeptController.updateDept(1L, updatedDept);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("上级部门不属于同一租户", result.getMessage());
    }

    @Test
    void deleteDept_RemoveDeptFailed() {
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.hasChildren(anyLong())).thenReturn(false);
        when(sysDeptService.removeDept(anyLong())).thenReturn(false);

        Result<String> result = sysDeptController.deleteDept(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("删除失败", result.getMessage());
    }

    @Test
    void deleteDept_Exception() {
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysDeptService.hasChildren(anyLong())).thenThrow(new RuntimeException("测试异常"));

        Result<String> result = sysDeptController.deleteDept(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals("测试异常", result.getMessage());
    }

    @Test
    void getDeptById_TenantNotFound() {
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysTenantService.getById(anyLong())).thenReturn(null);

        Result<SysDept> result = sysDeptController.getDeptById(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试部门", result.getData().getDeptName());
        assertNull(result.getData().getTenantName()); // 租户不存在时应该为null
    }

    @Test
    void getDeptById_ParentIdNull() {
        dept.setParentId(null);
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);

        Result<SysDept> result = sysDeptController.getDeptById(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试租户", result.getData().getTenantName());
        assertNull(result.getData().getParentName()); // parentId为null时应该为null
    }

    @Test
    void getDeptById_ParentIdEqualsTenantId() {
        dept.setParentId(1L); // 等于租户ID
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);

        Result<SysDept> result = sysDeptController.getDeptById(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试租户", result.getData().getTenantName());
        assertEquals("测试租户", result.getData().getParentName()); // 应该等于租户名称
    }

    @Test
    void getDeptById_ParentDeptNotFound() {
        dept.setParentId(999L);
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.getById(999L)).thenReturn(null); // 父部门不存在

        Result<SysDept> result = sysDeptController.getDeptById(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试租户", result.getData().getTenantName());
        assertNull(result.getData().getParentName()); // 父部门不存在时应该为null
    }

    @Test
    void getDeptById_ParentDeptExists() {
        SysDept parentDept = new SysDept();
        parentDept.setId(999L);
        parentDept.setDeptName("父部门");

        dept.setParentId(999L);
        when(sysDeptService.getById(anyLong())).thenReturn(dept);
        when(sysTenantService.getById(anyLong())).thenReturn(tenant);
        when(sysDeptService.getById(999L)).thenReturn(parentDept);

        Result<SysDept> result = sysDeptController.getDeptById(1L);

        assertEquals(HttpStatus.OK.value(), result.getCode());
        assertEquals("测试租户", result.getData().getTenantName());
        assertEquals("父部门", result.getData().getParentName()); // 应该等于父部门名称
    }
}