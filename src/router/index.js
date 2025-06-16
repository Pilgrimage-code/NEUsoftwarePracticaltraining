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
        component: () => import('@/views/dashboard/Home.vue'),
        meta: {
          title: '首页',
          requiresAuth: true
        }
      },
      {
        path: 'meeting',
        name: 'MeetingManagement',
        component: () => import('@/views/dashboard/MeetingManagement.vue'),
        meta: {
          title: '会议管理',
          requiresAuth: true
        }
      },
      {
        path: 'meeting/create',
        name: 'MeetingCreate',
        component: () => import('@/views/dashboard/MeetingForm.vue'),
        meta: {
          title: '创建会议',
          requiresAuth: true
        }
      },
      {
        path: 'meeting/edit/:id',
        name: 'MeetingEdit',
        component: () => import('@/views/dashboard/MeetingForm.vue'),
        meta: {
          title: '编辑会议',
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
        path: 'user',
        name: 'UserManagement',
        component: () => import('@/views/dashboard/UserManagement.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true
        }
      },
      {
        path: 'dept',
        name: 'DeptManagement',
        component: () => import('@/views/dashboard/DeptManagement.vue'),
        meta: {
          title: '部门管理',
          requiresAuth: true
        }
      },
      {
        path: 'tenant',
        name: 'TenantManagement',
        component: () => import('@/views/dashboard/TenantManagement.vue'),
        meta: {
          title: '租户管理',
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

