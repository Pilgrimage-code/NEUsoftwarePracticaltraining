import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getToken } from '@/utils/auth'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8080', // api的base_url
  timeout: 60000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 在请求发送之前做一些处理
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 设置租户ID和用户ID（如果有的话）
    const userStore = useUserStore()
    if (userStore.tenantId) {
      config.headers['X-Tenant-Id'] = userStore.tenantId
    }
    if (userStore.userId) {
      config.headers['X-User-Id'] = userStore.userId
    }
    
    // 设置Content-Type
    if (!config.headers['Content-Type'] && config.method !== 'get') {
      config.headers['Content-Type'] = 'application/json'
    }
    
    console.log('Request:', config.method.toUpperCase(), config.url, config.params || config.data)
    console.log('Headers:', JSON.stringify(config.headers))
    
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// response拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    console.log('Response:', response.config.url, res)
    
    // 如果返回的不是标准JSON格式，直接返回响应数据
    if (typeof res !== 'object') {
      return response.data
    }
    
    // 如果状态码不是200，认为有错误
    if (res.code !== 200) {
      // 401: 未登录或Token过期
      if (res.code === 401) {
        ElMessageBox.confirm(
          '您的登录状态已过期，请重新登录',
          '登录超时',
          {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          // 清除用户信息并重定向到登录页
          const userStore = useUserStore()
          userStore.resetUserInfo()
          router.replace(`/login?redirect=${router.currentRoute.value.fullPath}`)
        })
      } else if (res.code === 403) {
        // 403: 无权限
        ElMessage({
          message: res.message || '权限不足',
          type: 'error',
          duration: 5000
        })
      } else if (res.code === 500) {
        // 500: 服务器错误
        ElMessage({
          message: res.message || '服务器错误',
          type: 'error',
          duration: 5000
        })
      } else {
        // 其他错误
        ElMessage({
          message: res.message || '操作失败',
          type: 'error',
          duration: 5000
        })
      }
      return Promise.reject(res)
    } else {
      return res
    }
  },
  error => {
    console.error('Response error:', error)
    const { response } = error
    
    // 检查是否是网络错误
    if (!response) {
      console.log('网络连接异常，请检查您的网络连接')
      return Promise.reject(error)
    }
    
    // 如果是视频资源请求，不显示错误消息
    const url = error.config?.url || '';
    const isVideoRequest = url.includes('/uploads/') && 
                         (url.endsWith('.mp4') || url.endsWith('.webm') || url.endsWith('.ogg'));
    
    if (!isVideoRequest) {
      const errMsg = response.data?.message || error.message || '未知错误'
      ElMessage({
        message: errMsg,
        type: 'error',
        duration: 5000
      })
    } else {
      console.log('视频资源加载失败，但不显示错误消息:', url)
    }
    
    // 如果是未授权错误，重定向到登录页
    if (response.status === 401) {
      const userStore = useUserStore()
      userStore.resetUserInfo()
      router.replace(`/login?redirect=${router.currentRoute.value.fullPath}`)
    }
    
    return Promise.reject(error)
  }
)

// 添加一个测试连接的方法
service.testConnection = async () => {
  try {
    const response = await service.get('/api/health')
    return {
      success: true,
      message: `连接成功! 服务器时间: ${response.data?.serverTime || 'N/A'}`
    }
  } catch (error) {
    return {
      success: false,
      message: error.message || '无法连接到后端服务'
    }
  }
}

export default service

