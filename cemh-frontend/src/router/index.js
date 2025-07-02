import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: {
      title: '注册',
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
    component: () => import('@/views/layout/Layout.vue'),
    meta: {
      title: '控制台',
      requiresAuth: true
    },
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: {
          title: '首页',
          requiresAuth: true
        }
      },
      // 资讯管理路由
      {
        path: 'news',
        name: 'NewsManagement',
        component: () => import('@/views/news/NewsManagement.vue'),
        meta: {
          title: '资讯管理',
          requiresAuth: true
        }
      },
      {
        path: 'news/create',
        name: 'NewsCreate',
        component: () => import('@/views/news/NewsForm.vue'),
        meta: {
          title: '新增资讯',
          requiresAuth: true
        }
      },
      {
        path: 'news/edit/:id',
        name: 'NewsEdit',
        component: () => import('@/views/news/NewsForm.vue'),
        meta: {
          title: '编辑资讯',
          requiresAuth: true
        }
      },
      {
        path: 'news/:id',
        name: 'NewsDetail',
        component: () => import('@/views/news/NewsDetail.vue'),
        meta: {
          title: '资讯详情',
          requiresAuth: true
        }
      },
      // 会议管理路由
      {
        path: 'meetings',
        name: 'MeetingManagement',
        component: () => import('@/views/meeting/MeetingManagement.vue'),
        meta: {
          title: '会议管理',
          requiresAuth: true
        }
      },
      {
        path: 'meetings/create',
        name: 'MeetingCreate',
        component: () => import('@/views/meeting/MeetingForm.vue'),
        meta: {
          title: '创建会议',
          requiresAuth: true
        }
      },
      {
        path: 'meetings/edit/:id',
        name: 'MeetingEdit',
        component: () => import('@/views/meeting/MeetingForm.vue'),
        meta: {
          title: '编辑会议',
          requiresAuth: true
        }
      },
      {
        path: 'meetings/:id',
        name: 'MeetingDetail',
        component: () => import('@/views/meeting/MeetingDetail.vue'),
        meta: {
          title: '会议详情',
          requiresAuth: true
        }
      },
      // 用户管理路由
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/user/UserManagement.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true
        }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/UserProfile.vue'),
        meta: {
          title: '个人中心',
          requiresAuth: true
        }
      },

      {
        path: 'departments',
        name: 'DeptManagement',
        component: () => import('@/views/system/DeptManagement.vue'),
        meta: {
          title: '部门管理',
          requiresAuth: true
        }
      },
      {
        path: 'tenants',
        name: 'TenantManagement',
        component: () => import('@/views/system/TenantManagement.vue'),
        meta: {
          title: '租户管理',
          requiresAuth: true
        }
      },
      {
        path: 'settings',
        name: 'SystemSettings',
        component: () => import('@/views/system/Settings.vue'),
        meta: {
          title: '系统设置',
          requiresAuth: true
        }
      },



      // 课程管理路由
      {
        path: 'course-management',
        name: 'CourseManagement',
        component: () => import('@/views/course/CourseManagement.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'course-detail/:id',
        name: 'CourseDetail',
        component: () => import('@/views/course/CourseDetail.vue'),
        meta: { title: '课程详情' }
      },
      {
        path: 'learning-records',
        name: 'LearningRecords',
        component: () => import('@/views/course/LearningRecords.vue'),
        meta: { title: '学习记录' }
      },
      {
        path: 'learning-detail/:courseId',
        name: 'LearningDetail',
        component: () => import('@/views/course/LearningDetail.vue'),
        meta: { title: '学习详情' }
      },

      // 数据分析Chat路由
      {
        path: 'chat',
        name: 'DataAnalysisChat',
        component: () => import('@/views/Chat/Chat.vue'),
        meta: {
          title: '数据分析Chat',
          requiresAuth: true
        }
      },

    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/NotFound.vue'),
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

