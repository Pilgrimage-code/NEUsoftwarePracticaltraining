<template>
  <div class="layout">
    <!-- 顶部导航栏 -->
    <div class="layout-header">
      <div class="header-left">
        <div class="logo">
          <img src="/logo.png" alt="测盟汇" class="logo-img" />
          <span class="logo-text">测盟汇管理系统</span>
        </div>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-dropdown">
            <el-avatar :size="32" :src="userInfo.avatar">
              {{ userInfo.name?.charAt(0) }}
            </el-avatar>
            <span class="username">{{ userInfo.name }}</span>
            <i class="el-icon-arrow-down"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <i class="el-icon-user"></i>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <i class="el-icon-setting"></i>
                系统设置
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <i class="el-icon-switch-button"></i>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="layout-container">
      <!-- 左侧菜单 -->
      <div class="layout-sidebar">
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          router
          :collapse="isCollapse"
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-odometer"></i>
            <template #title>仪表板</template>
          </el-menu-item>
          
          <el-sub-menu index="meeting">
            <template #title>
              <i class="el-icon-date"></i>
              <span>会议管理</span>
            </template>
            <el-menu-item index="/dashboard/meeting">会议列表</el-menu-item>
            <el-menu-item index="/dashboard/meeting/create">创建会议</el-menu-item>
            <el-menu-item index="/dashboard/registration">报名管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="content">
            <template #title>
              <i class="el-icon-document"></i>
              <span>内容管理</span>
            </template>
            <el-menu-item index="/dashboard/news">资讯管理</el-menu-item>
            <el-menu-item index="/dashboard/news/create">发布资讯</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="system">
            <template #title>
              <i class="el-icon-setting"></i>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/dashboard/user">用户管理</el-menu-item>
            <el-menu-item index="/dashboard/dept">部门管理</el-menu-item>
            <el-menu-item index="/dashboard/tenant">租户管理</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/dashboard/profile">
            <i class="el-icon-user"></i>
            <template #title>个人中心</template>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧内容区域 -->
      <div class="layout-main">
        <div class="main-content">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Layout',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    
    const isCollapse = ref(false)
    
    // 当前激活的菜单
    const activeMenu = computed(() => {
      return route.path
    })
    
    // 用户信息
    const userInfo = computed(() => {
      return userStore.userInfo || {
        name: '管理员',
        avatar: ''
      }
    })
    
    // 处理下拉菜单命令
    const handleCommand = async (command) => {
      switch (command) {
        case 'profile':
          router.push('/dashboard/profile')
          break
        case 'settings':
          ElMessage.info('系统设置功能开发中...')
          break
        case 'logout':
          try {
            await ElMessageBox.confirm(
              '确定要退出登录吗？',
              '退出确认',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            userStore.logout()
            router.push('/login')
            ElMessage.success('已退出登录')
          } catch (error) {
            // 用户取消
          }
          break
      }
    }
    
    // 切换菜单折叠状态
    const toggleCollapse = () => {
      isCollapse.value = !isCollapse.value
    }
    
    onMounted(() => {
      // 组件挂载时的初始化逻辑
    })
    
    return {
      isCollapse,
      activeMenu,
      userInfo,
      handleCommand,
      toggleCollapse
    }
  }
}
</script>

<style scoped>
.layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-img {
  width: 32px;
  height: 32px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #303133;
}

.layout-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.layout-sidebar {
  width: 200px;
  background: #fff;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
}

.sidebar-menu {
  border-right: none;
  height: 100%;
}

.sidebar-menu .el-menu-item,
.sidebar-menu .el-sub-menu__title {
  height: 50px;
  line-height: 50px;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409eff;
}

.layout-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .layout-sidebar {
    width: 64px;
  }
  
  .sidebar-menu {
    --el-menu-item-height: 50px;
  }
  
  .main-content {
    padding: 10px;
  }
  
  .logo-text {
    display: none;
  }
}
</style>

