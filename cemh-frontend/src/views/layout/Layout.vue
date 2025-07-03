<template>
  <div class="modern-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo" @click="navigateToHome">
          <div class="logo-icon">
            <img src="/logo.png" alt="测盟汇" class="logo-image" />
          </div>
          <transition name="fade">
            <span v-show="!sidebarCollapsed" class="logo-text">测盟汇</span>
          </transition>
        </div>
        <button class="sidebar-toggle" @click="toggleSidebar">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M3 12H21M3 6H21M3 18H21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>
      </div>

      <nav class="sidebar-nav">
        <div class="nav-section">
          <div v-if="!sidebarCollapsed" class="nav-section-title">主要功能</div>
          <ul class="nav-menu">
            <li v-for="item in mainMenuItems" :key="item.path" class="nav-item">
              <router-link :to="item.path" class="nav-link" :class="{ 'active': isActiveRoute(item.path) }">
                <div class="nav-icon" v-html="item.icon"></div>
                <transition name="fade">
                  <span v-show="!sidebarCollapsed" class="nav-text">{{ item.title }}</span>
                </transition>
                <div v-if="item.badge && !sidebarCollapsed" class="nav-badge">{{ item.badge }}</div>
              </router-link>
            </li>
          </ul>
        </div>

        <div class="nav-section">
          <div v-if="!sidebarCollapsed" class="nav-section-title">系统管理</div>
          <ul class="nav-menu">
            <li v-for="item in systemMenuItems" :key="item.path" class="nav-item">
              <router-link :to="item.path" class="nav-link" :class="{ 'active': isActiveRoute(item.path) }">
                <div class="nav-icon" v-html="item.icon"></div>
                <transition name="fade">
                  <span v-show="!sidebarCollapsed" class="nav-text">{{ item.title }}</span>
                </transition>
              </router-link>
            </li>
          </ul>
        </div>

        <div class="nav-section">
          <div v-if="!sidebarCollapsed" class="nav-section-title">教学管理</div>
          <ul class="nav-menu">
            <li class="nav-item">
              <router-link to="/dashboard/course-management" class="nav-link" :class="{ 'active': isActiveRoute('/dashboard/course-management') }">
                <div class="nav-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M4 19.5V4.5C4 3.67157 4.67157 3 5.5 3H18.5C19.3284 3 20 3.67157 20 4.5V19.5C20 20.3284 19.3284 21 18.5 21H5.5C4.67157 21 4 20.3284 4 19.5Z" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                    <path d="M8 7H16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                    <path d="M8 11H16" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                    <path d="M8 15H12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                  </svg>
                </div>
                <transition name="fade">
                  <span v-show="!sidebarCollapsed" class="nav-text">课程管理</span>
                </transition>
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/dashboard/learning-records" class="nav-link" :class="{ 'active': isActiveRoute('/dashboard/learning-records') }">
                <div class="nav-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M14 2H6C4.89543 2 4 2.89543 4 4V20C4 21.1046 4.89543 22 6 22H18C19.1046 22 20 21.1046 20 20V8L14 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M14 2V8H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M16 13H8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M16 17H8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M10 9H9H8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
                <transition name="fade">
                  <span v-show="!sidebarCollapsed" class="nav-text">学习记录</span>
                </transition>
              </router-link>
            </li>
          </ul>
        </div>
      </nav>

      <div class="sidebar-footer">
        <div class="user-info" :class="{ 'collapsed': sidebarCollapsed }">
          <div class="user-avatar">
            <img :src="userStore.userInfo?.avatar || '/default-avatar.png'" :alt="userStore.userInfo?.nickname" />
          </div>
          <transition name="fade">
            <div v-show="!sidebarCollapsed" class="user-details">
              <div class="user-name">{{ userStore.userInfo?.username || '用户' }}</div>
              <div class="user-role">{{ userStore.userInfo?.nickname || '普通用户' }}</div>
            </div>
          </transition>
        </div>
      </div>
    </aside>

    <!-- 主内容区域 -->
    <div class="main-content" :class="{ 'content-expanded': sidebarCollapsed }">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="header-left">
          <div class="breadcrumb">
            <span v-for="(crumb, index) in breadcrumbs" :key="index" class="breadcrumb-item">
              <router-link v-if="crumb.path" :to="crumb.path" class="breadcrumb-link">
                {{ crumb.title }}
              </router-link>
              <span v-else class="breadcrumb-current">{{ crumb.title }}</span>
              <svg v-if="index < breadcrumbs.length - 1" class="breadcrumb-separator" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9 18L15 12L9 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
          </div>
        </div>

        <div class="header-right">
          <!-- 搜索框 -->
          <div class="search-box">
            <input v-model="searchQuery" type="text" placeholder="搜索..." class="search-input" />
            <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
              <path d="M21 21L16.65 16.65" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>

          <!-- 通知 -->
          <div class="notification-bell" @click="showNotifications = !showNotifications">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M18 8C18 6.4087 17.3679 4.88258 16.2426 3.75736C15.1174 2.63214 13.5913 2 12 2C10.4087 2 8.88258 2.63214 7.75736 3.75736C6.63214 4.88258 6 6.4087 6 8C6 15 3 17 3 17H21C21 17 18 15 18 8Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M13.73 21C13.5542 21.3031 13.3019 21.5547 12.9982 21.7295C12.6946 21.9044 12.3504 21.9965 12 21.9965C11.6496 21.9965 11.3054 21.9044 11.0018 21.7295C10.6981 21.5547 10.4458 21.3031 10.27 21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>

          <!-- 用户菜单 -->
          <div class="user-menu" @click.stop="showUserMenu = !showUserMenu">
            <div class="user-avatar-small">
              <img :src="userStore.userInfo?.avatar || '/default-avatar.png'" :alt="userStore.userInfo?.nickname" />
            </div>
            <svg class="dropdown-arrow" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M6 9L12 15L18 9" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>

            <!-- 用户菜单下拉 -->
            <transition name="dropdown">
              <div v-show="showUserMenu" class="user-dropdown" @click.stop>
                <div class="dropdown-header">
                  <div class="user-info-dropdown">
                    <div class="user-name">{{ userStore.userInfo?.nickname || '用户' }}</div>
                    <div class="user-email">{{ userStore.userInfo?.email || 'user@example.com' }}</div>
                  </div>
                </div>
                <div class="dropdown-divider"></div>
                <ul class="dropdown-menu">
                  <li><a href="#" @click.prevent="navigateToProfile">👤 个人资料</a></li>
                  <li><a href="#" @click.prevent="navigateToSettings">⚙️ 系统设置</a></li>
                  <li><a href="#" @click.prevent="toggleTheme">🌙 深色模式</a></li>
                  <li class="divider"></li>
                  <li><a href="#" @click.prevent="handleLogout" class="logout-link">🚪 退出登录</a></li>
                </ul>
              </div>
            </transition>
          </div>
        </div>
      </header>

      <!-- 页面内容 -->
      <main class="page-content">
        <router-view v-slot="{ Component }">
          <transition name="page" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>

    <!-- 通知面板 -->
    <transition name="slide-left">
      <div v-show="showNotifications" class="notification-panel" @click.stop>
        <div class="panel-header">
          <h3>通知中心</h3>
          <button @click="showNotifications = false" class="close-btn">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M18 6L6 18M6 6L18 18" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
        <div class="notification-list">
          <div v-for="notification in notifications" :key="notification.id" class="notification-item" :class="{ 'unread': !notification.read }">
            <div class="notification-icon" :class="notification.type">
              <span>{{ getNotificationIcon(notification.type) }}</span>
            </div>
            <div class="notification-content">
              <h4>{{ notification.title }}</h4>
              <p>{{ notification.message }}</p>
              <span class="notification-time">{{ formatTime(notification.time) }}</span>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Reading, Document } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 状态管理
const sidebarCollapsed = ref(false)
const showUserMenu = ref(false)
const showNotifications = ref(false)
const searchQuery = ref('')
const unreadNotifications = ref(3)

// 菜单数据
const mainMenuItems = ref([
  {
    path: '/dashboard',
    title: '仪表盘',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M3 13C6.6 5 17.4 5 21 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M12 17L12 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M12 9C13.1046 9 14 8.10457 14 7C14 5.89543 13.1046 5 12 5C10.8954 5 10 5.89543 10 7C10 8.10457 10.8954 9 12 9Z" stroke="currentColor" stroke-width="2"/>
    </svg>`
  },
  {
    path: '/dashboard/meetings',
    title: '会议管理',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <rect x="3" y="4" width="18" height="18" rx="2" ry="2" stroke="currentColor" stroke-width="2"/>
      <line x1="16" y1="2" x2="16" y2="6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
      <line x1="8" y1="2" x2="8" y2="6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
      <line x1="3" y1="10" x2="21" y2="10" stroke="currentColor" stroke-width="2"/>
    </svg>`
  },
  {
    path: '/dashboard/news',
    title: '资讯管理',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M14 2H6C4.89543 2 4 2.89543 4 4V20C4 21.1046 4.89543 22 6 22H18C19.1046 22 20 21.1046 20 20V8L14 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <polyline points="14,2 14,8 20,8" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <line x1="16" y1="13" x2="8" y2="13" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
      <line x1="16" y1="17" x2="8" y2="17" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
    </svg>`
  },
  {
    path: '/dashboard/users',
    title: '用户管理',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M20 21V19C20 16.7909 18.2091 15 16 15H8C5.79086 15 4 16.7909 4 19V21" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <circle cx="12" cy="7" r="4" stroke="currentColor" stroke-width="2"/>
    </svg>`
  },
  {
    path: '/dashboard/chat',
    title: '数据分析Chat',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <rect x="3" y="5" width="18" height="14" rx="2" stroke="currentColor" stroke-width="2"/>
      <path d="M7 8H17M7 12H13" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
      <path d="M7 16H10" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
    </svg>`
  }
])

const systemMenuItems = ref([
  {
    path: '/dashboard/departments',
    title: '部门管理',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M3 21H21M5 21V7L12 3L19 7V21M9 9H15M9 13H15M9 17H15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  },
  {
    path: '/dashboard/tenants',
    title: '租户管理',
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M17 21V19C17 16.7909 15.2091 15 13 15H5C2.79086 15 1 16.7909 1 19V21M23 21V19C23 16.7909 21.2091 15 19 15H17M13 7C13 9.20914 11.2091 11 9 11C6.79086 11 5 9.20914 5 7C5 4.79086 6.79086 3 9 3C11.2091 3 13 4.79086 13 7ZM23 7C23 9.20914 21.2091 11 19 11C16.7909 11 15 9.20914 15 7C15 4.79086 16.79086 3 19 3C21.2091 3 23 4.79086 23 7Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>`
  }
])

// 通知数据
const notifications = ref([
  {
    id: 1,
    type: 'success',
    title: '会议创建成功',
    message: '您的会议"技术分享会"已成功创建',
    time: new Date(Date.now() - 5 * 60 * 1000),
    read: false
  },
  {
    id: 2,
    type: 'warning',
    title: '系统维护通知',
    message: '系统将于今晚22:00进行维护',
    time: new Date(Date.now() - 30 * 60 * 1000),
    read: false
  },
  {
    id: 3,
    type: 'info',
    title: '新用户注册',
    message: '用户"张三"已成功注册',
    time: new Date(Date.now() - 2 * 60 * 60 * 1000),
    read: true
  }
])

// 计算属性
const breadcrumbs = computed(() => {
  const pathSegments = route.path.split('/').filter(segment => segment)
  const crumbs = [{ title: '首页', path: '/dashboard' }]

  let currentPath = ''
  pathSegments.forEach((segment, index) => {
    currentPath += `/${segment}`
    if (index > 0) { // 跳过第一个 'dashboard'
      const menuItem = [...mainMenuItems.value, ...systemMenuItems.value]
          .find(item => item.path === currentPath)

      if (menuItem) {
        crumbs.push({
          title: menuItem.title,
          path: index === pathSegments.length - 1 ? null : currentPath
        })
      }
    }
  })

  return crumbs
})

// 方法
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
  localStorage.setItem('sidebarCollapsed', sidebarCollapsed.value.toString())
}

const isActiveRoute = (path) => {
  return route.path === path || route.path.startsWith(path + '/')
}

const navigateToHome = () => {
  router.push('/dashboard')
}

const navigateToProfile = () => {
  router.push('/dashboard/profile')
  showUserMenu.value = false
}

const navigateToSettings = () => {
  router.push('/dashboard/settings')
  showUserMenu.value = false
}

const toggleTheme = () => {
  // 主题切换逻辑
  ElMessage.info('主题切换功能开发中...')
  showUserMenu.value = false
}

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    userStore.logout()
    router.push('/login')
    ElMessage.success('已退出登录')
  } catch {
    // 用户取消
  }
}

const getNotificationIcon = (type) => {
  const icons = {
    success: '✅',
    warning: '⚠️',
    error: '❌',
    info: 'ℹ️'
  }
  return icons[type] || 'ℹ️'
}

const formatTime = (time) => {
  const now = new Date()
  const diff = now - time
  const minutes = Math.floor(diff / (1000 * 60))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return time.toLocaleDateString()
}

// 生命周期
onMounted(() => {
  // 恢复侧边栏状态
  const savedState = localStorage.getItem('sidebarCollapsed')
  if (savedState) {
    sidebarCollapsed.value = savedState === 'true'
  }
})

// 监听路由变化，关闭下拉菜单
watch(route, () => {
  showUserMenu.value = false
  showNotifications.value = false
})
</script>

<style scoped>
.modern-layout {
  display: flex;
  height: 100vh;
  background: #f8fafc;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 侧边栏样式 */
.sidebar {
  width: 280px;
  background: white;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.05);
  z-index: 100;
}

.sidebar-collapsed {
  width: 80px;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #f1f5f9;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  width: 32px;
  height: 32px;
  color: #667eea;
  margin-right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a1a;
  letter-spacing: -0.5px;
}

.sidebar-toggle {
  width: 32px;
  height: 32px;
  border: none;
  background: #f8fafc;
  border-radius: 8px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-toggle:hover {
  background: #e2e8f0;
  color: #475569;
}

.sidebar-toggle svg {
  width: 16px;
  height: 16px;
}

/* 导航样式 */
.sidebar-nav {
  flex: 1;
  padding: 20px 0;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 32px;
}

.nav-section-title {
  font-size: 12px;
  font-weight: 600;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin: 0 20px 12px 20px;
}

.nav-menu {
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  margin: 0 12px 4px 12px;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  color: #64748b;
  text-decoration: none;
  border-radius: 12px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  background: #f1f5f9;
  color: #475569;
  transform: translateX(4px);
}

.nav-link.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.nav-icon {
  width: 20px;
  height: 20px;
  margin-right: 12px;
  flex-shrink: 0;
}

.nav-icon svg {
  width: 100%;
  height: 100%;
}

.nav-text {
  font-size: 14px;
  font-weight: 500;
  flex: 1;
}

.nav-badge {
  background: #ef4444;
  color: white;
  font-size: 11px;
  font-weight: 600;
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 18px;
  text-align: center;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 20px;
  border-top: 1px solid #f1f5f9;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #f8fafc;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.user-info.collapsed {
  justify-content: center;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-details {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-role {
  font-size: 12px;
  color: #64748b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  min-width: 0;
}

.content-expanded {
  margin-left: 0;
}

/* 顶部导航栏 */
.top-header {
  height: 70px;
  background: white;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
}

.breadcrumb {
  display: flex;
  align-items: center;
}

.breadcrumb-item {
  display: flex;
  align-items: center;
}

.breadcrumb-link {
  color: #64748b;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.breadcrumb-link:hover {
  color: #667eea;
}

.breadcrumb-current {
  color: #1a1a1a;
  font-size: 14px;
  font-weight: 500;
}

.breadcrumb-separator {
  width: 16px;
  height: 16px;
  color: #cbd5e1;
  margin: 0 8px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
  position: relative;
}

/* 搜索框 */
.search-box {
  position: relative;
  width: 300px;
}

.search-input {
  width: 100%;
  padding: 10px 16px 10px 40px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  font-size: 14px;
  background: #f8fafc;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  height: 16px;
  color: #9ca3af;
}

/* 通知铃铛 */
.notification-bell {
  position: relative;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f8fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.notification-bell:hover {
  background: #e2e8f0;
}

.notification-bell svg {
  width: 20px;
  height: 20px;
  color: #64748b;
}

/* 用户菜单 */
.user-menu {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 12px;
  transition: all 0.3s ease;
  position: relative;
}

.user-menu:hover {
  background: #f8fafc;
}

.user-avatar-small {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 8px;
}

.user-avatar-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dropdown-arrow {
  width: 16px;
  height: 16px;
  color: #9ca3af;
  transition: transform 0.3s ease;
}

/* 用户下拉菜单 */
.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  width: 240px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  overflow: hidden;
}

.dropdown-header {
  padding: 16px;
}

.user-info-dropdown .user-name {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.user-info-dropdown .user-email {
  font-size: 14px;
  color: #64748b;
}

.dropdown-divider {
  height: 1px;
  background: #f1f5f9;
  margin: 0 16px;
}

.dropdown-menu {
  list-style: none;
  margin: 0;
  padding: 8px 0;
}

.dropdown-menu li {
  margin: 0;
}

.dropdown-menu a {
  display: block;
  padding: 12px 16px;
  color: #374151;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
}

.dropdown-menu a:hover {
  background: #f8fafc;
  color: #1a1a1a;
}

.dropdown-menu .logout-link {
  color: #ef4444;
}

.dropdown-menu .logout-link:hover {
  background: #fef2f2;
  color: #dc2626;
}

.dropdown-menu .divider {
  height: 1px;
  background: #f1f5f9;
  margin: 8px 16px;
}

/* 通知面板 */
.notification-panel {
  position: fixed;
  top: 70px;
  right: 0;
  width: 400px;
  height: calc(100vh - 70px);
  background: white;
  border-left: 1px solid #e5e7eb;
  box-shadow: -5px 0 15px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border-bottom: 1px solid #f1f5f9;
}

.panel-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.close-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: #f8fafc;
  border-radius: 8px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  background: #e2e8f0;
  color: #475569;
}

.close-btn svg {
  width: 16px;
  height: 16px;
}

.notification-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 16px 20px;
  border-bottom: 1px solid #f8fafc;
  transition: all 0.3s ease;
}

.notification-item:hover {
  background: #f8fafc;
}

.notification-item.unread {
  background: #f0f9ff;
  border-left: 3px solid #0ea5e9;
}

.notification-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.notification-icon.success {
  background: #dcfce7;
}

.notification-icon.warning {
  background: #fef3c7;
}

.notification-icon.error {
  background: #fee2e2;
}

.notification-icon.info {
  background: #dbeafe;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-content h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 4px 0;
}

.notification-content p {
  font-size: 13px;
  color: #64748b;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.notification-time {
  font-size: 12px;
  color: #9ca3af;
}

/* 页面内容 */
.page-content {
  flex: 1;
  overflow: auto;
  background: #f8fafc;
}

/* 动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform-origin: top right;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: scale(0.95) translateY(-10px);
}

.slide-left-enter-active,
.slide-left-leave-active {
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-left-enter-from,
.slide-left-leave-to {
  transform: translateX(100%);
}

.page-enter-active,
.page-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.page-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-20px);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .sidebar {
    position: fixed;
    left: 0;
    top: 0;
    height: 100vh;
    z-index: 200;
    transform: translateX(-100%);
  }

  .sidebar.sidebar-open {
    transform: translateX(0);
  }

  .main-content {
    margin-left: 0;
  }

  .search-box {
    width: 200px;
  }

  .notification-panel {
    width: 100%;
    right: 0;
  }
}

@media (max-width: 768px) {
  .top-header {
    padding: 0 16px;
  }

  .search-box {
    display: none;
  }

  .breadcrumb {
    display: none;
  }

  .header-right {
    gap: 12px;
  }
}
</style>
