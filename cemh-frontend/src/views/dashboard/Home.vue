<template>
  <div class="home">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <el-card class="welcome-card">
        <div class="welcome-content">
          <div class="welcome-text">
            <h1 class="welcome-title">欢迎使用测盟汇管理系统</h1>
            <p class="welcome-subtitle">{{ getGreeting() }}，{{ userInfo.name || '管理员' }}！</p>
            <p class="welcome-description">今天是 {{ getCurrentDate() }}，祝您工作愉快！</p>
          </div>
          <div class="welcome-image">
            <div class="welcome-icon">
              <i class="el-icon-sunny"></i>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon users">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalUsers }}</div>
                <div class="stat-label">总用户数</div>
                <div class="stat-trend positive">
                  <i class="el-icon-top"></i>
                  +{{ stats.userGrowth }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon meetings">
                <i class="el-icon-date"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalMeetings }}</div>
                <div class="stat-label">总会议数</div>
                <div class="stat-trend positive">
                  <i class="el-icon-top"></i>
                  +{{ stats.meetingGrowth }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon news">
                <i class="el-icon-document"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalNews }}</div>
                <div class="stat-label">总资讯数</div>
                <div class="stat-trend positive">
                  <i class="el-icon-top"></i>
                  +{{ stats.newsGrowth }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon active">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.activeRate }}%</div>
                <div class="stat-label">活跃率</div>
                <div class="stat-trend negative">
                  <i class="el-icon-bottom"></i>
                  -{{ stats.activeRateChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 快捷操作 -->
    <div class="quick-actions-section">
      <el-card class="quick-actions-card">
        <template #header>
          <div class="card-header">
            <span>快捷操作</span>
          </div>
        </template>
        <div class="quick-actions">
          <div class="action-item" @click="handleQuickAction('meeting')">
            <div class="action-icon meeting">
              <i class="el-icon-plus"></i>
            </div>
            <div class="action-text">创建会议</div>
          </div>
          <div class="action-item" @click="handleQuickAction('news')">
            <div class="action-icon news">
              <i class="el-icon-edit"></i>
            </div>
            <div class="action-text">发布资讯</div>
          </div>
          <div class="action-item" @click="handleQuickAction('user')">
            <div class="action-icon user">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="action-text">添加用户</div>
          </div>
          <div class="action-item" @click="handleQuickAction('stats')">
            <div class="action-icon stats">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="action-text">查看统计</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 最近活动 -->
    <div class="recent-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="recent-card">
            <template #header>
              <div class="card-header">
                <span>最近会议</span>
                <el-button type="text" @click="$router.push('/dashboard/meeting')">查看更多</el-button>
              </div>
            </template>
            <div class="recent-list">
              <div v-for="meeting in recentMeetings" :key="meeting.id" class="recent-item">
                <div class="item-icon">
                  <i class="el-icon-date"></i>
                </div>
                <div class="item-content">
                  <div class="item-title">{{ meeting.title }}</div>
                  <div class="item-meta">{{ meeting.date }} · {{ meeting.participants }}人参与</div>
                </div>
                <div class="item-status">
                  <el-tag :type="getMeetingStatusType(meeting.status)">
                    {{ getMeetingStatusText(meeting.status) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="recent-card">
            <template #header>
              <div class="card-header">
                <span>最新资讯</span>
                <el-button type="text" @click="$router.push('/dashboard/news')">查看更多</el-button>
              </div>
            </template>
            <div class="recent-list">
              <div v-for="news in recentNews" :key="news.id" class="recent-item">
                <div class="item-icon">
                  <i class="el-icon-document"></i>
                </div>
                <div class="item-content">
                  <div class="item-title">{{ news.title }}</div>
                  <div class="item-meta">{{ news.publishTime }} · {{ news.views }}次浏览</div>
                </div>
                <div class="item-status">
                  <el-tag :type="getNewsStatusType(news.status)">
                    {{ getNewsStatusText(news.status) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

export default {
  name: 'Home',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    // 用户信息
    const userInfo = computed(() => {
      return userStore.userInfo || { name: '管理员' }
    })
    
    // 统计数据
    const stats = reactive({
      totalUsers: 1258,
      userGrowth: 15.6,
      totalMeetings: 342,
      meetingGrowth: 8.3,
      totalNews: 156,
      newsGrowth: 12.1,
      activeRate: 78.5,
      activeRateChange: 2.3
    })
    
    // 最近会议
    const recentMeetings = ref([
      {
        id: 1,
        title: '2024年度工作总结会议',
        date: '2024-06-15',
        participants: 25,
        status: 'upcoming'
      },
      {
        id: 2,
        title: '产品需求评审会',
        date: '2024-06-14',
        participants: 12,
        status: 'completed'
      },
      {
        id: 3,
        title: '技术分享交流会',
        date: '2024-06-13',
        participants: 18,
        status: 'completed'
      }
    ])
    
    // 最新资讯
    const recentNews = ref([
      {
        id: 1,
        title: '系统升级公告',
        publishTime: '2024-06-15',
        views: 156,
        status: 'published'
      },
      {
        id: 2,
        title: '新功能发布说明',
        publishTime: '2024-06-14',
        views: 89,
        status: 'published'
      },
      {
        id: 3,
        title: '用户使用指南',
        publishTime: '2024-06-13',
        views: 234,
        status: 'draft'
      }
    ])
    
    // 获取问候语
    const getGreeting = () => {
      const hour = new Date().getHours()
      if (hour < 12) return '上午好'
      if (hour < 18) return '下午好'
      return '晚上好'
    }
    
    // 获取当前日期
    const getCurrentDate = () => {
      return new Date().toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      })
    }
    
    // 快捷操作
    const handleQuickAction = (type) => {
      switch (type) {
        case 'meeting':
          router.push('/dashboard/meeting/create')
          break
        case 'news':
          router.push('/dashboard/news/create')
          break
        case 'user':
          router.push('/dashboard/user')
          break
        case 'stats':
          ElMessage.info('统计功能开发中...')
          break
      }
    }
    
    // 获取会议状态类型
    const getMeetingStatusType = (status) => {
      const types = {
        upcoming: 'warning',
        completed: 'success',
        cancelled: 'danger'
      }
      return types[status] || 'info'
    }
    
    // 获取会议状态文本
    const getMeetingStatusText = (status) => {
      const texts = {
        upcoming: '即将开始',
        completed: '已完成',
        cancelled: '已取消'
      }
      return texts[status] || '未知'
    }
    
    // 获取资讯状态类型
    const getNewsStatusType = (status) => {
      const types = {
        published: 'success',
        draft: 'info',
        archived: 'warning'
      }
      return types[status] || 'info'
    }
    
    // 获取资讯状态文本
    const getNewsStatusText = (status) => {
      const texts = {
        published: '已发布',
        draft: '草稿',
        archived: '已归档'
      }
      return texts[status] || '未知'
    }
    
    onMounted(() => {
      // 组件挂载时的初始化逻辑
    })
    
    return {
      userInfo,
      stats,
      recentMeetings,
      recentNews,
      getGreeting,
      getCurrentDate,
      handleQuickAction,
      getMeetingStatusType,
      getMeetingStatusText,
      getNewsStatusType,
      getNewsStatusText
    }
  }
}
</script>

<style scoped>
.home {
  padding: 0;
}

.welcome-section {
  margin-bottom: 20px;
}

.welcome-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.welcome-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 0;
}

.welcome-text {
  flex: 1;
}

.welcome-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.welcome-subtitle {
  font-size: 18px;
  color: #606266;
  margin: 0 0 8px 0;
}

.welcome-description {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.welcome-image {
  flex-shrink: 0;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
}

.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.users {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.meetings {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.news {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-trend {
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-trend.positive {
  color: #67c23a;
}

.stat-trend.negative {
  color: #f56c6c;
}

.quick-actions-section {
  margin-bottom: 20px;
}

.quick-actions-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  border-radius: 8px;
  background-color: #f8f9fa;
  cursor: pointer;
  transition: all 0.3s;
}

.action-item:hover {
  background-color: #e9ecef;
  transform: translateY(-2px);
}

.action-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.action-icon.meeting {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.action-icon.news {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.action-icon.user {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.action-icon.stats {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.action-text {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.recent-section {
  margin-bottom: 20px;
}

.recent-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.recent-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.recent-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  background-color: #f8f9fa;
  transition: background-color 0.3s;
}

.recent-item:hover {
  background-color: #e9ecef;
}

.item-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  color: white;
  flex-shrink: 0;
}

.item-content {
  flex: 1;
}

.item-title {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.item-meta {
  font-size: 12px;
  color: #909399;
}

.item-status {
  flex-shrink: 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-section .el-col {
    margin-bottom: 20px;
  }
  
  .quick-actions {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  
  .welcome-title {
    font-size: 24px;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
  
  .recent-section .el-col {
    margin-bottom: 20px;
  }
}
</style>

