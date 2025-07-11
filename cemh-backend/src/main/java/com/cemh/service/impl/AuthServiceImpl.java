package com.cemh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.entity.SysUser;
import com.cemh.entity.SysTenant;
import com.cemh.mapper.SysUserMapper;
import com.cemh.mapper.SysTenantMapper;
import com.cemh.service.AuthService;
import com.cemh.utils.JwtUtils;
import com.cemh.utils.PasswordUtils;
import com.cemh.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_PREFIX = "user:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh:";
    private static final int ACCESS_TOKEN_EXPIRE = 7200; // 2小时
    private static final int REFRESH_TOKEN_EXPIRE = 604800; // 7天

    @Override
    public Result<LoginVO> login(LoginDTO loginDTO) {
        try {
            // 1. 参数验证
            if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (loginDTO.getTenantCode() == null || loginDTO.getTenantCode().trim().isEmpty()) {
                return Result.error("租户代码不能为空");
            }

            // 2. 查询租户
            QueryWrapper<SysTenant> tenantQueryWrapper = new QueryWrapper<>();
            tenantQueryWrapper.eq("tenant_code", loginDTO.getTenantCode())
                             .eq("deleted", 0)
                             .eq("status", 1);
            
            SysTenant tenant = sysTenantMapper.selectOne(tenantQueryWrapper);
            if (tenant == null) {
                return Result.error("租户代码不存在或已禁用");
            }

            // 3. 查询用户
            QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", loginDTO.getUsername())
                       .eq("tenant_id", tenant.getId())
                       .eq("deleted", 0);
            
            SysUser user = sysUserMapper.selectOne(queryWrapper);
            if (user == null) {
                return Result.error("用户名或租户代码错误");
            }

            // 4. 验证密码
            if (!PasswordUtils.matches(loginDTO.getPassword(), user.getPassword())) {
                return Result.error("密码错误");
            }

            // 5. 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                return Result.error("用户已被禁用");
            }

            // 6. 生成JWT令牌
            String accessToken = JwtUtils.generateToken(user.getId(), user.getUsername(), user.getTenantId());
            String refreshToken = JwtUtils.generateRefreshToken(user.getId());

            // 7. 缓存用户信息到Redis（如果Redis可用）
            if (redisTemplate != null) {
                try {
                    String userKey = USER_PREFIX + user.getId();
                    String tokenKey = TOKEN_PREFIX + accessToken;
                    String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
                    
                    redisTemplate.opsForValue().set(userKey, user, ACCESS_TOKEN_EXPIRE, TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(tokenKey, user.getId(), ACCESS_TOKEN_EXPIRE, TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(refreshKey, user.getId(), REFRESH_TOKEN_EXPIRE, TimeUnit.SECONDS);
                } catch (Exception e) {
                    // Redis操作失败，记录日志但不影响登录
                    System.err.println("Redis操作失败: " + e.getMessage());
                }
            }

            // 7. 更新最后登录时间和IP
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(loginDTO.getClientIp());
            sysUserMapper.updateById(user);

            // 8. 构建返回结果
            LoginVO loginVO = new LoginVO();
            loginVO.setUserId(user.getId());
            loginVO.setUsername(user.getUsername());
            loginVO.setNickname(user.getNickname());
            loginVO.setAvatar(user.getAvatar());
            loginVO.setEmail(user.getEmail());
            loginVO.setPhone(user.getPhone());
            loginVO.setTenantId(user.getTenantId());
            // 注意：SysUser实体中没有tenantCode字段，使用tenantId
            loginVO.setTenantCode(user.getTenantId() != null ? user.getTenantId().toString() : null);
            loginVO.setAccessToken(accessToken);
            loginVO.setRefreshToken(refreshToken);
            loginVO.setExpiresIn((long) ACCESS_TOKEN_EXPIRE);

            return Result.success(loginVO);

        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> logout(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return Result.error("令牌不能为空");
            }

            // 从Redis中删除相关缓存（如果Redis可用）
            if (redisTemplate != null) {
                try {
                    // 解析token获取用户ID
                    Long userId = JwtUtils.getUserIdFromToken(token);
                    if (userId != null) {
                        String userKey = USER_PREFIX + userId;
                        String tokenKey = TOKEN_PREFIX + token;
                        
                        redisTemplate.delete(userKey);
                        redisTemplate.delete(tokenKey);
                    }
                } catch (Exception e) {
                    // Redis操作失败，记录日志但不影响退出
                    System.err.println("Redis操作失败: " + e.getMessage());
                }
            }

            return Result.success();
        } catch (Exception e) {
            return Result.error("退出失败：" + e.getMessage());
        }
    }

    @Override
    public Result<LoginVO> getUserInfo(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return Result.error("令牌不能为空");
            }

            // 验证token
            if (!JwtUtils.validateToken(token)) {
                return Result.error("令牌无效或已过期");
            }

            // 从token中获取用户ID
            Long userId = JwtUtils.getUserIdFromToken(token);
            if (userId == null) {
                return Result.error("无法解析用户信息");
            }

            // 先尝试从Redis获取用户信息（如果Redis可用）
            SysUser user = null;
            if (redisTemplate != null) {
                try {
                    String userKey = USER_PREFIX + userId;
                    user = (SysUser) redisTemplate.opsForValue().get(userKey);
                } catch (Exception e) {
                    // Redis操作失败，记录日志
                    System.err.println("Redis操作失败: " + e.getMessage());
                }
            }

            // 如果Redis中没有，从数据库查询
            if (user == null) {
                user = sysUserMapper.selectById(userId);
                if (user == null) {
                    return Result.error("用户不存在");
                }
            }

            // 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                return Result.error("用户已被禁用");
            }

            // 构建返回结果
            LoginVO loginVO = new LoginVO();
            loginVO.setUserId(user.getId());
            loginVO.setUsername(user.getUsername());
            loginVO.setNickname(user.getNickname());
            loginVO.setAvatar(user.getAvatar());
            loginVO.setEmail(user.getEmail());
            loginVO.setPhone(user.getPhone());
            loginVO.setTenantId(user.getTenantId());
            loginVO.setTenantCode(user.getTenantId() != null ? user.getTenantId().toString() : null);

            return Result.success(loginVO);

        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<LoginVO> refreshToken(String refreshToken) {
        try {
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return Result.error("刷新令牌不能为空");
            }

            // 验证刷新令牌
            if (!JwtUtils.validateToken(refreshToken)) {
                return Result.error("刷新令牌无效或已过期");
            }

            // 从刷新令牌中获取用户ID
            Long userId = JwtUtils.getUserIdFromToken(refreshToken);
            if (userId == null) {
                return Result.error("无法解析用户信息");
            }

            // 查询用户信息
            SysUser user = sysUserMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 检查用户状态
            if (user.getStatus() == null || user.getStatus() != 1) {
                return Result.error("用户已被禁用");
            }

            // 生成新的访问令牌
            String newAccessToken = JwtUtils.generateToken(user.getId(), user.getUsername(), user.getTenantId());
            String newRefreshToken = JwtUtils.generateRefreshToken(user.getId());

            // 更新Redis缓存（如果Redis可用）
            if (redisTemplate != null) {
                try {
                    String userKey = USER_PREFIX + user.getId();
                    String tokenKey = TOKEN_PREFIX + newAccessToken;
                    String refreshKey = REFRESH_TOKEN_PREFIX + newRefreshToken;
                    
                    redisTemplate.opsForValue().set(userKey, user, ACCESS_TOKEN_EXPIRE, TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(tokenKey, user.getId(), ACCESS_TOKEN_EXPIRE, TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(refreshKey, user.getId(), REFRESH_TOKEN_EXPIRE, TimeUnit.SECONDS);
                    
                    // 删除旧的刷新令牌
                    String oldRefreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
                    redisTemplate.delete(oldRefreshKey);
                } catch (Exception e) {
                    // Redis操作失败，记录日志但不影响刷新
                    System.err.println("Redis操作失败: " + e.getMessage());
                }
            }

            // 构建返回结果
            LoginVO loginVO = new LoginVO();
            loginVO.setUserId(user.getId());
            loginVO.setUsername(user.getUsername());
            loginVO.setNickname(user.getNickname());
            loginVO.setAvatar(user.getAvatar());
            loginVO.setEmail(user.getEmail());
            loginVO.setPhone(user.getPhone());
            loginVO.setTenantId(user.getTenantId());
            loginVO.setTenantCode(user.getTenantId() != null ? user.getTenantId().toString() : null);
            loginVO.setAccessToken(newAccessToken);
            loginVO.setRefreshToken(newRefreshToken);
            loginVO.setExpiresIn((long) ACCESS_TOKEN_EXPIRE);

            return Result.success(loginVO);

        } catch (Exception e) {
            return Result.error("刷新令牌失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Boolean> validateToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return Result.success(false);
            }

            // 验证JWT令牌
            boolean isValid = JwtUtils.validateToken(token);
            
            // 如果JWT有效，再检查Redis中是否存在（如果Redis可用）
            if (isValid && redisTemplate != null) {
                try {
                    String tokenKey = TOKEN_PREFIX + token;
                    Object userId = redisTemplate.opsForValue().get(tokenKey);
                    isValid = (userId != null);
                } catch (Exception e) {
                    // Redis操作失败，仅依赖JWT验证
                    System.err.println("Redis操作失败: " + e.getMessage());
                }
            }

            return Result.success(isValid);

        } catch (Exception e) {
            return Result.success(false);
        }
    }
}

