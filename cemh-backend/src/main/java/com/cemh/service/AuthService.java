package com.cemh.service;

import com.cemh.common.Result;
import com.cemh.dto.LoginDTO;
import com.cemh.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * 
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    Result<LoginVO> login(LoginDTO loginDTO);

    /**
     * 用户退出
     * 
     * @param token 访问令牌
     * @return 退出结果
     */
    Result<Void> logout(String token);

    /**
     * 获取用户信息
     * 
     * @param token 访问令牌
     * @return 用户信息
     */
    Result<LoginVO> getUserInfo(String token);

    /**
     * 刷新令牌
     * 
     * @param refreshToken 刷新令牌
     * @return 新的登录结果
     */
    Result<LoginVO> refreshToken(String refreshToken);

    /**
     * 验证令牌
     * 
     * @param token 访问令牌
     * @return 验证结果
     */
    Result<Boolean> validateToken(String token);
}

