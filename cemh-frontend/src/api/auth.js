import request from '@/utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 用户退出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/auth/userinfo',
    method: 'get'
  })
}

// 刷新token
export function refreshToken(refreshToken) {
  return request({
    url: '/auth/refresh',
    method: 'post',
    params: { refreshToken }
  })
}

// 验证token
export function validateToken(token) {
  return request({
    url: '/auth/validate',
    method: 'post',
    params: { token }
  })
}

