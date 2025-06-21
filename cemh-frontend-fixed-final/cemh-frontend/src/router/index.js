import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Layout',
    component: () => import('@/views/Layout.vue'),
    meta: {
      title: '控制台',
      requiresAuth: true
    },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: {
          title: '首页',
          requiresAuth: true
        }
      },
      {
        path: 'meetings',
        name: 'MeetingManagement',
        component: () => import('@/views/dashboard/MeetingManagement.vue'),
        meta: {
          title: '会议管理',
          requiresAuth: true
        }
      },
      {
        path: 'meetings/create',
        name: 'MeetingCreate',
        component: () => import('@/views/dashboard/MeetingForm.vue'),
        meta: {
          title: '创建会议',
          requiresAuth: true
        }
      },
      {
        path: 'meetings/edit/:id',
        name: 'MeetingEdit',
        component: () => import('@/views/dashboard/MeetingForm.vue'),
        meta: {
          title: '编辑会议',
          requiresAuth: true
        }
      },
      {
        path: 'meetings/:id',
        name: 'MeetingDetail',
        component: () => import('@/views/dashboard/MeetingDetail.vue'),
        meta: {
          title: '会议详情',
          requiresAuth: true
        }
      },
      {
        path: 'news',
        name: 'NewsManagement',
        component: () => import('@/views/dashboard/NewsManagement.vue'),
        meta: {
          title: '资讯管理',
          requiresAuth: true
        }
      },
      {
        path: 'news/create',
        name: 'NewsCreate',
        component: () => import('@/views/dashboard/NewsForm.vue'),
        meta: {
          title: '发布资讯',
          requiresAuth: true
        }
      },
      {
        path: 'news/:id',
        name: 'NewsDetail',
        component: () => import('@/views/dashboard/NewsDetail.vue'),
        meta: {
          title: '资讯详情',
          requiresAuth: true
        }
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/dashboard/UserManagement.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true
        }
      },
      {
        path: 'departments',
        name: 'DeptManagement',
        component: () => import('@/views/dashboard/DeptManagement.vue'),
        meta: {
          title: '部门管理',
          requiresAuth: true
        }
      },
      {
        path: 'tenants',
        name: 'TenantManagement',
        component: () => import('@/views/dashboard/TenantManagement.vue'),
        meta: {
          title: '租户管理',
          requiresAuth: true
        }
      },
      {
        path: 'files',
        name: 'FileManagement',
        component: () => import('@/views/dashboard/FileManagement.vue'),
        meta: {
          title: '文件管理',
          requiresAuth: true
        }
      },
      {
        path: 'analytics',
        name: 'Analytics',
        component: () => import('@/views/dashboard/Analytics.vue'),
        meta: {
          title: '数据分析',
          requiresAuth: true
        }
      },
      {
        path: 'system',
        name: 'SystemMonitor',
        component: () => import('@/views/dashboard/SystemMonitor.vue'),
        meta: {
          title: '系统监控',
          requiresAuth: true
        }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/dashboard/UserProfile.vue'),
        meta: {
          title: '个人中心',
          requiresAuth: true
        }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/dashboard/Settings.vue'),
        meta: {
          title: '系统设置',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: {
      title: '页面不存在'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 测盟汇管理系统` : '测盟汇管理系统'
  
  // 检查是否需要登录
  if (to.meta.requiresAuth) {
    if (userStore.isLoggedIn) {
      next()
    } else {
      next('/login')
    }
  } else {
    // 如果已登录且访问登录页，重定向到首页
    if (to.path === '/login' && userStore.isLoggedIn) {
      next('/dashboard')
    } else {
      next()
    }
  }
})

export default router

