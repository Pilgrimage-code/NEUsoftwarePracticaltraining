package com.cemh.common;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {

    @Test
    void testDefaultConstructor() {
        PageResult<String> page = new PageResult<>();
        assertNull(page.getCurrent());
        assertNull(page.getSize());
        assertNull(page.getTotal());
        assertNull(page.getRecords());
    }

    @Test
    void testAllArgsConstructor() {
        List<String> records = Arrays.asList("a", "b");
        PageResult<String> page = new PageResult<>(1L, 10L, 2L, records);
        assertEquals(1L, page.getCurrent());
        assertEquals(10L, page.getSize());
        assertEquals(2L, page.getTotal());
        assertEquals(records, page.getRecords());
    }

    @Test
    void testSettersAndGetters() {
        PageResult<Integer> page = new PageResult<>();
        page.setTotal(100L);
        page.setCurrent(2L);
        page.setSize(20L);
        page.setRecords(Collections.singletonList(123));
        assertEquals(100L, page.getTotal());
        assertEquals(2L, page.getCurrent());
        assertEquals(20L, page.getSize());
        assertEquals(Collections.singletonList(123), page.getRecords());
    }

    @Test
    void testToString() {
        PageResult<String> page = new PageResult<>(1L, 1L, 1L, Arrays.asList("x"));
        assertTrue(page.toString().contains("x"));
    }

    @Test
    void testEmptyRecords() {
        PageResult<String> page = new PageResult<>(1L, 10L, 0L, Collections.emptyList());
        assertTrue(page.getRecords().isEmpty());
    }

    @Test
    void testNullRecords() {
        PageResult<String> page = new PageResult<>(1L, 10L, 0L, null);
        assertNull(page.getRecords());
    }

    @Test
    void testOfStaticMethod() {
        PageResult<String> page = PageResult.of(2L, 5L, 10L, Arrays.asList("a", "b"));
        assertEquals(2L, page.getCurrent());
        assertEquals(5L, page.getSize());
        assertEquals(10L, page.getTotal());
        assertEquals(Arrays.asList("a", "b"), page.getRecords());
    }

    @Test
    void testEmptyStaticMethod() {
        PageResult<String> page = PageResult.empty();
        assertEquals(1L, page.getCurrent());
        assertEquals(10L, page.getSize());
        assertEquals(0L, page.getTotal());
        assertNull(page.getRecords());
    }

    @Test
    void testSetPages() {
        PageResult<String> page = new PageResult<>();
        page.setPages(99L);
        assertEquals(99L, page.getPages());
    }

    @Test
    void testSettersNullAndNegative() {
        PageResult<String> page = new PageResult<>();
        page.setCurrent(null);
        page.setSize(null);
        page.setTotal(null);
        page.setPages(null);
        page.setRecords(null);
        assertNull(page.getCurrent());
        assertNull(page.getSize());
        assertNull(page.getTotal());
        assertNull(page.getPages());
        assertNull(page.getRecords());
    }
} 