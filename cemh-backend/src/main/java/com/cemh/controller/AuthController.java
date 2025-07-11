package com.cemh.controller;

import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.service.AuthService;
import com.cemh.service.CaptchaService;
import com.cemh.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 认证控制器

 */
@Tag(name = "认证管理", description = "用户登录、注册、退出等认证相关接口")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CaptchaService captchaService;

    @Operation(summary = "用户登录", description = "用户通过用户名和密码进行登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        try {
            // 获取客户端IP
            String clientIp = getClientIp(request);
            loginDTO.setClientIp(clientIp);
            
            // 执行登录逻辑 - 修复返回值类型问题
            return authService.login(loginDTO);
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "用户退出", description = "用户退出登录，清除token")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 执行退出逻辑 - 修复返回值类型问题
            return authService.logout(token);
        } catch (Exception e) {
            return Result.error("退出失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取当前用户信息", description = "根据token获取当前登录用户的详细信息")
    @GetMapping("/userinfo")
    public Result<LoginVO> getUserInfo(HttpServletRequest request) {
        try {
            // 从请求头获取token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 获取用户信息 - 修复方法调用问题
            return authService.getUserInfo(token);
        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "刷新token", description = "使用refresh token刷新access token")
    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(@RequestParam String refreshToken) {
        try {
            // 刷新token - 修复返回值类型问题
            return authService.refreshToken(refreshToken);
        } catch (Exception e) {
            return Result.error("刷新token失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证token", description = "验证token是否有效")
    @PostMapping("/validate")
    public Result<Boolean> validateToken(@RequestParam String token) {
        try {
            // 验证token - 修复方法调用问题
            return authService.validateToken(token);
        } catch (Exception e) {
            return Result.error("验证失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取图片验证码", description = "生成图片验证码并返回base64和key")
    @GetMapping("/captcha")
    public Result<Map<String, Object>> getCaptcha() {
        Map<String, Object> captcha = captchaService.generateCaptcha();
        return Result.success(captcha);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 如果是多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
}

