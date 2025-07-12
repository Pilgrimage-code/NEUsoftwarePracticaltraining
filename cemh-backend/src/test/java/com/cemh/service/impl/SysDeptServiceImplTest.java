package com.cemh.service.impl;
//李璇
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.entity.SysDept;
import com.cemh.mapper.SysDeptMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysDeptServiceImplTest {

    @Mock
    private SysDeptMapper sysDeptMapper;

    @InjectMocks
    private SysDeptServiceImpl sysDeptService;

    private SysDept dept;
    private SysDept childDept;

    @BeforeEach
    void setUp() {
        dept = new SysDept();
        dept.setId(1L);
        dept.setTenantId(1L);
        dept.setDeptCode("DEPT001");
        dept.setDeptName("测试部门");
        dept.setStatus(1);
        dept.setDeleted(0);

        childDept = new SysDept();
        childDept.setId(2L);
        childDept.setTenantId(1L);
        childDept.setParentId(1L);
        childDept.setDeptCode("DEPT002");
        childDept.setDeptName("子部门");
        childDept.setStatus(1);
        childDept.setDeleted(0);
    }

    @Test
    void getByTenantId_Success() {
        when(sysDeptMapper.selectByTenantId(anyLong())).thenReturn(Arrays.asList(dept, childDept));

        List<SysDept> result = sysDeptService.getByTenantId(1L);

        assertEquals(2, result.size());
        assertEquals("测试部门", result.get(0).getDeptName());
        assertEquals("子部门", result.get(1).getDeptName());
    }

    @Test
    void getByTenantId_Empty() {
        when(sysDeptMapper.selectByTenantId(anyLong())).thenReturn(Collections.emptyList());

        List<SysDept> result = sysDeptService.getByTenantId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void existsByCode_Exists() {
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        assertTrue(sysDeptService.existsByCode(1L, "DEPT001"));
    }

    @Test
    void existsByCode_NotExists() {
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        assertFalse(sysDeptService.existsByCode(1L, "INVALID_CODE"));
    }

    @Test
    void hasChildren_WithChildren() {
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(2L);

        assertTrue(sysDeptService.hasChildren(1L));
    }

    @Test
    void hasChildren_NoChildren() {
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        assertFalse(sysDeptService.hasChildren(999L));
    }

    @Test
    void removeDept_Success() {
        when(sysDeptMapper.selectById(anyLong())).thenReturn(dept);
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L); // 没有子部门
        when(sysDeptMapper.deleteById(anyLong())).thenReturn(1);

        assertTrue(sysDeptService.removeDept(1L));
    }

    @Test
    void removeDept_WithChildren() {
        when(sysDeptMapper.selectById(anyLong())).thenReturn(dept);
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(2L); // 有子部门

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> sysDeptService.removeDept(1L));

        assertEquals("请先删除子部门", exception.getMessage());
    }

    @Test
    void removeDept_NotFound() {
        when(sysDeptMapper.selectById(anyLong())).thenReturn(null);

        assertFalse(sysDeptService.removeDept(999L));
    }

    // 补充：测试removeDept数据库删除失败的分支
    @Test
    void removeDept_DeleteFailed() {
        when(sysDeptMapper.selectById(anyLong())).thenReturn(dept);
        when(sysDeptMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L); // 没有子部门
        when(sysDeptMapper.deleteById(anyLong())).thenReturn(0); // 删除失败

        // removeDept返回false
        assertFalse(sysDeptService.removeDept(1L));
    }
}