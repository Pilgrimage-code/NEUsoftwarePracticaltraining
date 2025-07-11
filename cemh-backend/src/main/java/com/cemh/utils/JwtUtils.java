package com.cemh.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author 测盟汇技术团队
 * @version 1.0.0
 * @since 2025-6-28
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:cemh-management-system-secret-key-2024}")
    private String secret;

    @Value("${jwt.issuer:cemh-system}")
    private String issuer;

    private static final long ACCESS_TOKEN_EXPIRE = 7200; // 2小时
    private static final long REFRESH_TOKEN_EXPIRE = 604800; // 7天

    /**
     * 生成访问token
     */
    public static String generateToken(Long userId, String username, Long tenantId) {
        return generateToken(userId, username, tenantId, ACCESS_TOKEN_EXPIRE);
    }

    /**
     * 生成刷新token
     */
    public static String generateRefreshToken(Long userId) {
        return generateToken(userId, null, null, REFRESH_TOKEN_EXPIRE);
    }

    /**
     * 生成JWT token
     */
    public static String generateToken(Long userId, String username, Long tenantId, long expireSeconds) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireSeconds * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        if (username != null) {
            claims.put("username", username);
        }
        if (tenantId != null) {
            claims.put("tenantId", tenantId);
        }

        String secretKey = "cemh-management-system-secret-key-2024";
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username != null ? username : userId.toString())
                .setIssuer("cemh-system")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证token
     */
    public static boolean validateToken(String token) {
        try {
            String secretKey = "cemh-management-system-secret-key-2024";
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 从token中获取Claims
     */
    private static Claims getClaimsFromToken(String token) {
        String secretKey = "cemh-management-system-secret-key-2024";
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从token中获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object userId = claims.get("userId");
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    /**
     * 从token中获取用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 从token中获取租户ID
     */
    public static Long getTenantIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object tenantId = claims.get("tenantId");
        if (tenantId instanceof Integer) {
            return ((Integer) tenantId).longValue();
        }
        return (Long) tenantId;
    }

    /**
     * 从token中获取过期时间
     */
    public static Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否过期
     */
    public static boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}

