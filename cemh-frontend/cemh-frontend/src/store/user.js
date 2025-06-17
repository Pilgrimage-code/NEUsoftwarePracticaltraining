import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    userInfo: null,
    permissions: [],
    roles: []
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userId: (state) => state.userInfo?.userId,
    username: (state) => state.userInfo?.username,
    nickname: (state) => state.userInfo?.nickname,
    avatar: (state) => state.userInfo?.avatar,
    tenantId: (state) => state.userInfo?.tenantId,
    tenantName: (state) => state.userInfo?.tenantName,
    deptId: (state) => state.userInfo?.deptId,
    deptName: (state) => state.userInfo?.deptName
  },

  actions: {
    // 登录
    async login(loginForm) {
      try {
        const response = await login(loginForm)
        const { accessToken, ...userInfo } = response.data
        
        this.token = accessToken
        this.userInfo = userInfo
        this.permissions = userInfo.permissions || []
        this.roles = userInfo.roles || []
        
        setToken(accessToken)
        
        return response
      } catch (error) {
        throw error
      }
    },

    // 获取用户信息
    async getUserInfo() {
      try {
        const response = await getUserInfo()
        const userInfo = response.data
        
        this.userInfo = userInfo
        this.permissions = userInfo.permissions || []
        this.roles = userInfo.roles || []
        
        return response
      } catch (error) {
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
        this.token = null
        this.userInfo = null
        this.permissions = []
        this.roles = []
        removeToken()
      }
    },

    // 重置状态
    resetState() {
      this.token = null
      this.userInfo = null
      this.permissions = []
      this.roles = []
      removeToken()
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
    }
  }
})

