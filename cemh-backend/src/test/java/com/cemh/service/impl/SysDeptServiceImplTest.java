package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.entity.SysDept;
import com.cemh.mapper.SysDeptMapper;
import com.cemh.common.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysDeptServiceImplTest {

    @InjectMocks
    @Spy // Use @Spy to allow partial mocking of sysDeptService
    private SysDeptServiceImpl sysDeptService;

    @Mock
    private SysDeptMapper sysDeptMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByTenantId() {
        Long tenantId = 1L;
        List<SysDept> expectedDepts = Arrays.asList(new SysDept(), new SysDept());
        when(sysDeptMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedDepts);

        List<SysDept> actualDepts = sysDeptService.getByTenantId(tenantId);
        assertEquals(expectedDepts.size(), actualDepts.size());
        verify(sysDeptMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testExistsByCodeTrue() {
        Long tenantId = 1L;
        String deptCode = "CODE001";
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        boolean exists = sysDeptService.existsByCode(tenantId, deptCode);
        assertTrue(exists);
        verify(sysDeptMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    void testExistsByCodeFalse() {
        Long tenantId = 1L;
        String deptCode = "CODE001";
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        boolean exists = sysDeptService.existsByCode(tenantId, deptCode);
        assertFalse(exists);
        verify(sysDeptMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    void testHasChildrenTrue() {
        Long parentId = 1L;
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        boolean hasChildren = sysDeptService.hasChildren(parentId);
        assertTrue(hasChildren);
        verify(sysDeptMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    void testHasChildrenFalse() {
        Long parentId = 1L;
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        boolean hasChildren = sysDeptService.hasChildren(parentId);
        assertFalse(hasChildren);
        verify(sysDeptMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    void testRemoveDeptSuccess() {
        Long deptId = 1L;
        SysDept dept = new SysDept();
        dept.setId(deptId);

        doReturn(dept).when(sysDeptService).getById(deptId); // Mock getById method
        doReturn(false).when(sysDeptService).hasChildren(deptId); // Mock hasChildren method
        doReturn(true).when(sysDeptService).removeById(deptId); // Mock removeById method

        boolean result = sysDeptService.removeDept(deptId);
        assertTrue(result);
        verify(sysDeptService, times(1)).removeById(deptId);
    }

    @Test
    void testRemoveDeptWithChildren() {
        Long deptId = 1L;
        SysDept dept = new SysDept();
        dept.setId(deptId);

        doReturn(dept).when(sysDeptService).getById(deptId);
        doReturn(true).when(sysDeptService).hasChildren(deptId); // Mock hasChildren method

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sysDeptService.removeDept(deptId);
        });
        assertEquals("请先删除子部门", exception.getMessage());
        verify(sysDeptService, never()).removeById(deptId);
    }

    @Test
    void testRemoveDeptNotFound() {
        Long deptId = 1L;
        doReturn(null).when(sysDeptService).getById(deptId);

        boolean result = sysDeptService.removeDept(deptId);
        assertFalse(result);
        verify(sysDeptService, never()).removeById(deptId);
    }
}


