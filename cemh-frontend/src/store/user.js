import { defineStore } from 'pinia'
import { login, logout, getInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    permissions: [],
    roles: [],
    tenantId: null,
    userId: localStorage.getItem('userId') || '',
    username: '',
    nickname: '',
    avatar: '',
    tenantCode: '',
    deptId: '',
    email: '',
    phone: ''
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    getUserId: (state) => state.userId,
    getUsername: (state) => state.username,
    getNickname: (state) => state.nickname,
    getAvatar: (state) => state.avatar,
    getTenantId: (state) => state.tenantId,
    getTenantCode: (state) => state.tenantCode,
    getDeptId: (state) => state.deptId,
    getEmail: (state) => state.email,
    getPhone: (state) => state.phone
  },

  actions: {
    // 登录
    async login(loginForm) {
      try {
        const response = await login(loginForm)
        if (response.code === 200 && response.data) {
          const { accessToken, userId, username, nickname, avatar, email, phone, tenantId, tenantCode } = response.data
          
          // 保存token
          this.token = accessToken
          setToken(accessToken)
          localStorage.setItem('token', accessToken)
          
          // 保存用户信息
          this.userInfo = response.data
          this.userId = userId
          this.username = username
          this.nickname = nickname
          this.avatar = avatar
          this.email = email
          this.phone = phone
          this.tenantId = tenantId
          this.tenantCode = tenantCode
          
          localStorage.setItem('userInfo', JSON.stringify(response.data))
          localStorage.setItem('userId', userId)
          
          return response
        }
        throw new Error(response.msg || '登录失败')
      } catch (error) {
        console.error('登录失败:', error)
        throw error
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        const response = await getInfo()
        if (response.code === 200 && response.data) {
          const { userId, username, nickname, avatar, email, phone, tenantId, tenantCode } = response.data
          
          this.userInfo = response.data
          this.userId = userId
          this.username = username
          this.nickname = nickname
          this.avatar = avatar
          this.email = email
          this.phone = phone
          this.tenantId = tenantId
          this.tenantCode = tenantCode
          
          localStorage.setItem('userInfo', JSON.stringify(response.data))
          localStorage.setItem('userId', userId)
          
          return response.data
        }
        throw new Error(response.msg || '获取用户信息失败')
      } catch (error) {
        console.error('获取用户信息失败:', error)
        throw error
      }
    },

    // 退出登录
    async logout() {
      try {
        await logout()
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        this.resetState()
      }
    },

    // 重置状态
    resetState() {
      this.token = ''
      this.userInfo = {}
      this.permissions = []
      this.roles = []
      this.tenantId = null
      this.userId = ''
      this.username = ''
      this.nickname = ''
      this.avatar = ''
      this.tenantCode = ''
      this.deptId = ''
      this.email = ''
      this.phone = ''
      
      removeToken()
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      localStorage.removeItem('userId')
    },

    // 设置用户信息
    setUserInfo(userInfo) {
      if (!userInfo) return
      
      this.userInfo = userInfo
      this.userId = userInfo.userId
      this.username = userInfo.username
      this.nickname = userInfo.nickname
      this.avatar = userInfo.avatar
      this.email = userInfo.email
      this.phone = userInfo.phone
      this.tenantId = userInfo.tenantId
      this.tenantCode = userInfo.tenantCode
      
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
      localStorage.setItem('userId', userInfo.userId)
    },

    // 设置token
    setToken(token) {
      if (!token) return
      
      this.token = token
      setToken(token)
      localStorage.setItem('token', token)
    },

    // 检查权限
    hasPermission(permission) {
      if (!permission) return true
      if (this.permissions.includes('*:*:*')) return true
      return this.permissions.includes(permission)
    },

    // 检查角色
    hasRole(role) {
      if (!role) return true
      return this.roles.includes(role)
    },

    // 检查多个权限（任一满足）
    hasAnyPermission(permissions) {
      if (!permissions || permissions.length === 0) return true
      return permissions.some(permission => this.hasPermission(permission))
    },

    // 检查多个权限（全部满足）
    hasAllPermissions(permissions) {
      if (!permissions || permissions.length === 0) return true
      return permissions.every(permission => this.hasPermission(permission))
    },

    // 设置用户ID（用于测试）
    setUserId(id) {
      this.userId = id
      localStorage.setItem('userId', id)
    }
  }
})

