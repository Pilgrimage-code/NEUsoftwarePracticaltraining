package com.cemh.utils;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @Test
    void testGenerateAndValidateToken() {
        String token = JwtUtils.generateToken(1L, "testuser", 100L);
        assertNotNull(token);
        assertTrue(JwtUtils.validateToken(token));
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = JwtUtils.generateRefreshToken(2L);
        assertNotNull(refreshToken);
        assertTrue(JwtUtils.validateToken(refreshToken));
    }

    @Test
    void testGetUserIdFromToken() {
        String token = JwtUtils.generateToken(123L, "userA", 456L);
        Long userId = JwtUtils.getUserIdFromToken(token);
        assertEquals(123L, userId);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = JwtUtils.generateToken(123L, "userB", 456L);
        String username = JwtUtils.getUsernameFromToken(token);
        assertEquals("userB", username);
    }

    @Test
    void testGetTenantIdFromToken() {
        String token = JwtUtils.generateToken(123L, "userC", 789L);
        Long tenantId = JwtUtils.getTenantIdFromToken(token);
        assertEquals(789L, tenantId);
    }

    @Test
    void testGetExpirationDateFromToken() {
        String token = JwtUtils.generateToken(1L, "testuser", 100L);
        Date expiration = JwtUtils.getExpirationDateFromToken(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testIsTokenExpired() throws InterruptedException {
        // 生成一个1秒过期的token
        String token = JwtUtils.generateToken(1L, "testuser", 100L, 1);
        assertFalse(JwtUtils.isTokenExpired(token));
        Thread.sleep(1100);
        try {
            JwtUtils.isTokenExpired(token);
            fail("应抛出 ExpiredJwtException 或返回 true");
        } catch (Exception e) {
            // jjwt 2.x 可能直接抛 ExpiredJwtException
            assertTrue(e.getClass().getSimpleName().contains("ExpiredJwt"));
        }
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(JwtUtils.validateToken(invalidToken));
    }

    @Test
    void testGetUserIdFromToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> JwtUtils.getUserIdFromToken(invalidToken));
    }

    @Test
    void testGetUsernameFromToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> JwtUtils.getUsernameFromToken(invalidToken));
    }

    @Test
    void testGetTenantIdFromToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> JwtUtils.getTenantIdFromToken(invalidToken));
    }

    @Test
    void testGetExpirationDateFromToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> JwtUtils.getExpirationDateFromToken(invalidToken));
    }

    @Test
    void testIsTokenExpired_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> JwtUtils.isTokenExpired(invalidToken));
    }
} 