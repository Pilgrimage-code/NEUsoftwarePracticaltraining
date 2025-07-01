import request from '@/utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

// 用户退出
export function logout() {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/api/auth/userinfo',
    method: 'get'
  })
}

// 获取用户信息（别名，保持向后兼容）
export const getInfo = getUserInfo

// 刷新token
export function refreshToken(refreshToken) {
  return request({
    url: '/api/auth/refresh',
    method: 'post',
    params: { refreshToken }
  })
}

// 验证token
export function validateToken(token) {
  return request({
    url: '/api/auth/validate',
    method: 'post',
    params: { token }
  })
}

// 获取图片验证码
export function getCaptcha() {
  return request({
    url: '/api/auth/captcha',
    method: 'get'
  })
}

// 用户注册
export function registerUser(data) {
  return request({
    url: '/api/users/register',
    method: 'post',
    data
  })
}

// 企业注册
export function registerTenant(data) {
  return request({
    url: '/api/tenants/register',
    method: 'post',
    data
  })
}

// 获取企业列表
export function getTenants() {
  return request({
    url: '/api/tenants/all',
    method: 'get'
  })
}

// 获取部门列表（根据企业ID）
export function getDeptsByTenant(tenantId) {
  return request({
    url: `/api/departments/by-tenant/${tenantId}`,
    method: 'get'
  })
}

