<template>
  <div class="modern-dashboard">
    <!-- 顶部欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1>欢迎回来，{{ userStore.userInfo?.nickname || '用户' }}</h1>
          <p>今天是 {{ formatCurrentDate() }}，祝您工作愉快</p>
        </div>
        <div class="weather-widget">
          <div class="weather-icon">☀️</div>
          <div class="weather-info">
            <span class="temperature">22°C</span>
            <span class="weather-desc">晴朗</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计卡片网格 -->
    <div class="stats-grid">
      <div class="stat-card meetings" @click="navigateTo('/dashboard/meetings')">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M8 2V5M16 2V5M3.5 9.09H20.5M21 8.5V17C21 20 19.5 22 16 22H8C4.5 22 3 20 3 17V8.5C3 5.5 4.5 3.5 8 3.5H16C19.5 3.5 21 5.5 21 8.5Z" stroke="currentColor" stroke-width="1.5" stroke-miterlimit="10" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalMeetings }}</h3>
          <p>总会议数</p>
          <div class="stat-trend">
            <span class="trend-up">↗ +12%</span>
          </div>
        </div>
        <div class="stat-bg-pattern"></div>
      </div>

      <div class="stat-card users" @click="navigateTo('/dashboard/users')">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M18 7.16C17.94 7.15 17.87 7.15 17.81 7.16C16.43 7.11 15.33 5.98 15.33 4.58C15.33 3.15 16.48 2 17.91 2C19.34 2 20.49 3.16 20.49 4.58C20.48 5.98 19.38 7.11 18 7.16Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M16.97 14.44C18.34 14.67 19.85 14.43 20.91 13.72C22.32 12.78 22.32 11.24 20.91 10.3C19.84 9.59 18.31 9.35 16.94 9.59" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M5.97 7.16C6.03 7.15 6.1 7.15 6.16 7.16C7.54 7.11 8.64 5.98 8.64 4.58C8.64 3.15 7.49 2 6.06 2C4.63 2 3.48 3.16 3.48 4.58C3.49 5.98 4.59 7.11 5.97 7.16Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M7 14.44C5.63 14.67 4.12 14.43 3.06 13.72C1.65 12.78 1.65 11.24 3.06 10.3C4.13 9.59 5.66 9.35 7.03 9.59" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M12 14.63C11.94 14.62 11.87 14.62 11.81 14.63C10.43 14.58 9.33 13.45 9.33 12.05C9.33 10.62 10.48 9.47 11.91 9.47C13.34 9.47 14.49 10.63 14.49 12.05C14.48 13.45 13.38 14.59 12 14.63Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M9.09 17.78C7.68 18.72 7.68 20.26 9.09 21.2C10.69 22.27 13.31 22.27 14.91 21.2C16.32 20.26 16.32 18.72 14.91 17.78C13.32 16.72 10.69 16.72 9.09 17.78Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalUsers }}</h3>
          <p>注册用户</p>
          <div class="stat-trend">
            <span class="trend-up">↗ +8%</span>
          </div>
        </div>
        <div class="stat-bg-pattern"></div>
      </div>

      <div class="stat-card news" @click="navigateTo('/dashboard/news')">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M22 10V15C22 20 20 22 15 22H9C4 22 2 20 2 15V9C2 4 4 2 9 2H14" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M22 10H18C15 10 14 9 14 6V2L22 10Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M7 13H13" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M7 17H11" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalNews }}</h3>
          <p>发布资讯</p>
          <div class="stat-trend">
            <span class="trend-up">↗ +15%</span>
          </div>
        </div>
        <div class="stat-bg-pattern"></div>
      </div>

      <div class="stat-card registrations" @click="navigateTo('/dashboard/analytics')">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M9.5 13.75C9.5 14.72 10.25 15.5 11.17 15.5H13.05C13.85 15.5 14.5 14.82 14.5 13.97C14.5 13.06 14.1 12.73 13.51 12.52L10.5 11.47C9.91 11.26 9.51 10.94 9.51 10.02C9.51 9.18 10.16 8.49 10.96 8.49H12.84C13.76 8.49 14.51 9.27 14.51 10.24" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M12 7.5V16.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            <path d="M22 12C22 17.52 17.52 22 12 22C6.48 22 2 17.52 2 12C2 6.48 6.48 2 12 2C17.52 2 22 6.48 22 12Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <div class="stat-content">
          <h3>{{ stats.totalRegistrations }}</h3>
          <p>会议报名</p>
          <div class="stat-trend">
            <span class="trend-up">↗ +23%</span>
          </div>
        </div>
        <div class="stat-bg-pattern"></div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧内容 -->
      <div class="left-section">
        <!-- 即将开始的会议 -->
        <div class="content-card">
          <div class="card-header">
            <h3>
              <div class="header-icon">📅</div>
              即将开始的会议
            </h3>
            <button class="view-all-btn" @click="navigateTo('/dashboard/meetings')">
              查看全部
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M8.91 19.92L15.43 13.4C16.2 12.63 16.2 11.37 15.43 10.6L8.91 4.08" stroke="currentColor" stroke-width="1.5" stroke-miterlimit="10" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
          
          <div class="meeting-timeline">
            <div v-for="meeting in upcomingMeetings" :key="meeting.id" class="timeline-item" @click="viewMeetingDetail(meeting.id)">
              <div class="timeline-marker">
                <div class="marker-dot"></div>
                <div class="marker-line"></div>
              </div>
              <div class="timeline-content">
                <div class="meeting-time">
                  <span class="time">{{ formatTime(meeting.startTime) }}</span>
                  <span class="date">{{ formatDate(meeting.startTime) }}</span>
                </div>
                <div class="meeting-details">
                  <h4>{{ meeting.title }}</h4>
                  <div class="meeting-meta">
                    <span class="location">📍 {{ meeting.location }}</span>
                    <span class="participants">👥 {{ meeting.registrationCount }}人</span>
                  </div>
                  <div class="meeting-tags">
                    <span class="tag" :class="getMeetingTypeClass(meeting.type)">
                      {{ getMeetingTypeText(meeting.type) }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 数据分析图表 -->
        <div class="content-card">
          <div class="card-header">
            <h3>
              <div class="header-icon">📊</div>
              数据分析
            </h3>
            <div class="chart-controls">
              <button class="control-btn active">本周</button>
              <button class="control-btn">本月</button>
              <button class="control-btn">本年</button>
            </div>
          </div>
          
          <div class="chart-container">
            <div class="chart-placeholder">
              <div class="chart-bars">
                <div class="bar" style="height: 60%"></div>
                <div class="bar" style="height: 80%"></div>
                <div class="bar" style="height: 45%"></div>
                <div class="bar" style="height: 90%"></div>
                <div class="bar" style="height: 70%"></div>
                <div class="bar" style="height: 55%"></div>
                <div class="bar" style="height: 85%"></div>
              </div>
              <div class="chart-labels">
                <span>周一</span>
                <span>周二</span>
                <span>周三</span>
                <span>周四</span>
                <span>周五</span>
                <span>周六</span>
                <span>周日</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="right-section">
        <!-- 快速操作 -->
        <div class="content-card">
          <div class="card-header">
            <h3>
              <div class="header-icon">⚡</div>
              快速操作
            </h3>
          </div>
          
          <div class="quick-actions">
            <button class="action-btn primary" @click="navigateTo('/dashboard/meetings/create')">
              <div class="btn-icon">➕</div>
              <span>创建会议</span>
            </button>
            
            <button class="action-btn success" @click="navigateTo('/dashboard/news/create')">
              <div class="btn-icon">📝</div>
              <span>发布资讯</span>
            </button>
            
            <button class="action-btn info" @click="navigateTo('/dashboard/users')">
              <div class="btn-icon">👥</div>
              <span>用户管理</span>
            </button>
            
            <button class="action-btn warning" @click="navigateTo('/dashboard/departments')">
              <div class="btn-icon">🏢</div>
              <span>部门管理</span>
            </button>
          </div>
        </div>

        <!-- 系统状态 -->
        <div class="content-card">
          <div class="card-header">
            <h3>
              <div class="header-icon">💻</div>
              系统状态
            </h3>
          </div>
          
          <div class="system-status">
            <div class="status-item">
              <div class="status-label">CPU使用率</div>
              <div class="status-bar">
                <div class="status-fill" style="width: 45%"></div>
              </div>
              <div class="status-value">45%</div>
            </div>
            
            <div class="status-item">
              <div class="status-label">内存使用率</div>
              <div class="status-bar">
                <div class="status-fill" style="width: 62%"></div>
              </div>
              <div class="status-value">62%</div>
            </div>
            
            <div class="status-item">
              <div class="status-label">存储空间</div>
              <div class="status-bar">
                <div class="status-fill" style="width: 78%"></div>
              </div>
              <div class="status-value">78%</div>
            </div>
            
            <div class="status-item">
              <div class="status-label">网络延迟</div>
              <div class="status-bar">
                <div class="status-fill" style="width: 25%"></div>
              </div>
              <div class="status-value">25ms</div>
            </div>
          </div>
        </div>

        <!-- 最新通知 -->
        <div class="content-card">
          <div class="card-header">
            <h3>
              <div class="header-icon">🔔</div>
              最新通知
            </h3>
          </div>
          
          <div class="notification-list">
            <div class="notification-item">
              <div class="notification-avatar success">✓</div>
              <div class="notification-content">
                <h4>系统更新完成</h4>
                <p>测盟汇管理系统已更新至v1.0.0版本</p>
                <span class="notification-time">2小时前</span>
              </div>
            </div>
            
            <div class="notification-item">
              <div class="notification-avatar info">ℹ</div>
              <div class="notification-content">
                <h4>会议提醒</h4>
                <p>您有3个会议即将开始，请及时参加</p>
                <span class="notification-time">1天前</span>
              </div>
            </div>
            
            <div class="notification-item">
              <div class="notification-avatar warning">⚠</div>
              <div class="notification-content">
                <h4>存储空间提醒</h4>
                <p>系统存储空间使用率已达80%</p>
                <span class="notification-time">3天前</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 热门会议 -->
        <div class="content-card">
          <div class="card-header">
            <h3>
              <div class="header-icon">🔥</div>
              热门会议
            </h3>
          </div>
          
          <div class="popular-list">
            <div v-for="(meeting, index) in popularMeetings.slice(0, 5)" :key="meeting.id" class="popular-item" @click="viewMeetingDetail(meeting.id)">
              <div class="rank" :class="{ 'top-rank': index < 3 }">{{ index + 1 }}</div>
              <div class="popular-content">
                <h4>{{ meeting.title }}</h4>
                <p>{{ meeting.registrationCount }}人报名</p>
              </div>
              <div class="popular-trend">
                <span class="trend-icon">📈</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const stats = reactive({
  totalMeetings: 156,
  totalUsers: 1248,
  totalNews: 89,
  totalRegistrations: 2341
})

// 即将开始的会议
const upcomingMeetings = ref([
  {
    id: 1,
    title: '2024年度技术交流大会',
    location: '北京国际会议中心',
    startTime: '2024-06-20T09:00:00',
    type: 1,
    status: 1,
    registrationCount: 156
  },
  {
    id: 2,
    title: '产品发布会',
    location: '上海展览中心',
    startTime: '2024-06-22T14:00:00',
    type: 2,
    status: 1,
    registrationCount: 89
  },
  {
    id: 3,
    title: '培训课程：项目管理实践',
    location: '在线会议',
    startTime: '2024-06-25T10:00:00',
    type: 3,
    status: 1,
    registrationCount: 234
  }
])

// 热门会议
const popularMeetings = ref([
  { id: 1, title: '2024年度技术交流大会', registrationCount: 456 },
  { id: 2, title: 'AI技术发展趋势研讨会', registrationCount: 389 },
  { id: 3, title: '数字化转型实践分享', registrationCount: 267 },
  { id: 4, title: '创新创业论坛', registrationCount: 234 },
  { id: 5, title: '项目管理最佳实践', registrationCount: 198 }
])

// 导航方法
const navigateTo = (path) => {
  router.push(path)
}

// 查看会议详情
const viewMeetingDetail = (id) => {
  router.push(`/dashboard/meetings/${id}`)
}

// 格式化当前日期
const formatCurrentDate = () => {
  const date = new Date()
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
}

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    month: '2-digit',
    day: '2-digit'
  })
}

// 格式化时间
const formatTime = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取会议类型样式类
const getMeetingTypeClass = (type) => {
  const classes = {
    1: 'tag-primary',
    2: 'tag-success',
    3: 'tag-warning',
    4: 'tag-info'
  }
  return classes[type] || 'tag-info'
}

// 获取会议类型文本
const getMeetingTypeText = (type) => {
  const texts = {
    1: '技术交流',
    2: '产品发布',
    3: '培训课程',
    4: '其他'
  }
  return texts[type] || '其他'
}

onMounted(() => {
  // 初始化数据
})
</script>

<style scoped>
.modern-dashboard {
  padding: 24px;
  background: #f8fafc;
  min-height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 欢迎区域 */
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  padding: 32px;
  margin-bottom: 32px;
  color: white;
  position: relative;
  overflow: hidden;
}

.welcome-section::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  pointer-events: none;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 1;
}

.welcome-text h1 {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 8px 0;
  letter-spacing: -0.5px;
}

.welcome-text p {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.weather-widget {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  padding: 16px 24px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.weather-icon {
  font-size: 32px;
  margin-right: 16px;
}

.weather-info {
  display: flex;
  flex-direction: column;
}

.temperature {
  font-size: 24px;
  font-weight: 600;
  line-height: 1;
}

.weather-desc {
  font-size: 14px;
  opacity: 0.8;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 32px;
}

.stat-card {
  position: relative;
  background: white;
  border-radius: 20px;
  padding: 32px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.stat-card.meetings .stat-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-card.users .stat-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stat-card.news .stat-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-card.registrations .stat-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-icon svg {
  width: 32px;
  height: 32px;
}

.stat-content h3 {
  font-size: 36px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 8px 0;
  letter-spacing: -1px;
}

.stat-content p {
  font-size: 16px;
  color: #666;
  margin: 0 0 12px 0;
  font-weight: 500;
}

.stat-trend {
  display: flex;
  align-items: center;
}

.trend-up {
  color: #10b981;
  font-size: 14px;
  font-weight: 600;
}

.stat-bg-pattern {
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(102, 126, 234, 0.05) 0%, transparent 70%);
  pointer-events: none;
}

/* 主要内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 32px;
}

.content-card {
  background: white;
  border-radius: 20px;
  padding: 32px;
  margin-bottom: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.content-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.card-header h3 {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.header-icon {
  font-size: 24px;
  margin-right: 12px;
}

.view-all-btn {
  display: flex;
  align-items: center;
  background: none;
  border: none;
  color: #667eea;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 16px;
  border-radius: 12px;
}

.view-all-btn:hover {
  background: rgba(102, 126, 234, 0.1);
  color: #5a67d8;
}

.view-all-btn svg {
  width: 16px;
  height: 16px;
  margin-left: 8px;
  transition: transform 0.3s ease;
}

.view-all-btn:hover svg {
  transform: translateX(4px);
}

/* 会议时间线 */
.meeting-timeline {
  position: relative;
}

.timeline-item {
  display: flex;
  margin-bottom: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 16px;
  border-radius: 16px;
}

.timeline-item:hover {
  background: #f8fafc;
  transform: translateX(8px);
}

.timeline-marker {
  position: relative;
  margin-right: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.marker-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #667eea;
  border: 3px solid white;
  box-shadow: 0 0 0 2px #667eea;
  z-index: 2;
}

.marker-line {
  width: 2px;
  height: 40px;
  background: #e2e8f0;
  margin-top: 8px;
}

.timeline-item:last-child .marker-line {
  display: none;
}

.timeline-content {
  flex: 1;
}

.meeting-time {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.meeting-time .time {
  font-size: 18px;
  font-weight: 600;
  color: #667eea;
  margin-right: 12px;
}

.meeting-time .date {
  font-size: 14px;
  color: #64748b;
  background: #f1f5f9;
  padding: 4px 12px;
  border-radius: 8px;
}

.meeting-details h4 {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px 0;
}

.meeting-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.meeting-meta span {
  font-size: 14px;
  color: #64748b;
}

.meeting-tags .tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
}

.tag-primary {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
}

.tag-success {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.tag-warning {
  background: rgba(245, 158, 11, 0.1);
  color: #f59e0b;
}

.tag-info {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

/* 图表容器 */
.chart-controls {
  display: flex;
  gap: 8px;
}

.control-btn {
  padding: 8px 16px;
  border: none;
  background: #f1f5f9;
  color: #64748b;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.control-btn.active,
.control-btn:hover {
  background: #667eea;
  color: white;
}

.chart-container {
  margin-top: 24px;
}

.chart-placeholder {
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.chart-bars {
  display: flex;
  align-items: end;
  justify-content: space-between;
  height: 160px;
  padding: 0 20px;
}

.bar {
  width: 24px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 4px 4px 0 0;
  transition: all 0.3s ease;
}

.bar:hover {
  transform: scaleY(1.1);
  filter: brightness(1.1);
}

.chart-labels {
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  font-size: 12px;
  color: #64748b;
}

/* 快速操作 */
.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 16px;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.action-btn.success {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.action-btn.info {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
}

.action-btn.warning {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
}

.action-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.btn-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

/* 系统状态 */
.system-status {
  space-y: 16px;
}

.status-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.status-label {
  width: 80px;
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.status-bar {
  flex: 1;
  height: 8px;
  background: #f1f5f9;
  border-radius: 4px;
  margin: 0 16px;
  overflow: hidden;
}

.status-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.status-value {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  min-width: 40px;
  text-align: right;
}

/* 通知列表 */
.notification-list {
  space-y: 16px;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 16px;
  border-radius: 12px;
  transition: all 0.3s ease;
  margin-bottom: 16px;
}

.notification-item:hover {
  background: #f8fafc;
}

.notification-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  font-size: 16px;
  font-weight: 600;
  color: white;
}

.notification-avatar.success {
  background: #10b981;
}

.notification-avatar.info {
  background: #3b82f6;
}

.notification-avatar.warning {
  background: #f59e0b;
}

.notification-content {
  flex: 1;
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
  color: #94a3b8;
}

/* 热门列表 */
.popular-list {
  space-y: 12px;
}

.popular-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 12px;
}

.popular-item:hover {
  background: #f8fafc;
  transform: translateX(4px);
}

.rank {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
  background: #f1f5f9;
  color: #64748b;
  margin-right: 16px;
}

.rank.top-rank {
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  color: white;
}

.popular-content {
  flex: 1;
}

.popular-content h4 {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 4px 0;
}

.popular-content p {
  font-size: 12px;
  color: #64748b;
  margin: 0;
}

.popular-trend {
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .main-content {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  }
}

@media (max-width: 768px) {
  .modern-dashboard {
    padding: 16px;
  }
  
  .welcome-content {
    flex-direction: column;
    gap: 20px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
  
  .content-card {
    padding: 20px;
  }
}
</style>
<style>
.meeting-meta {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.participants .el-icon {
  margin-right: 4px;
}

.meeting-status {
  margin-left: 16px;
}

/* 资讯列表 */
.news-list {
  max-height: 400px;
  overflow-y: auto;
}

.news-item {
  display: flex;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.news-item:hover {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px 12px;
}

.news-item:last-child {
  border-bottom: none;
}

.news-cover {
  width: 80px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 16px;
  flex-shrink: 0;
}

.news-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.news-content {
  flex: 1;
}

.news-content h4 {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-summary {
  font-size: 14px;
  color: #606266;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
  color: #909399;
}

.views {
  display: flex;
  align-items: center;
}

.views .el-icon {
  margin-right: 4px;
}

/* 快速操作 */
.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.action-btn {
  height: 60px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-btn .el-icon {
  margin-right: 8px;
}

/* 系统通知 */
.notification-list {
  max-height: 300px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
  color: white;
  font-size: 16px;
}

.notification-icon.success {
  background: #67c23a;
}

.notification-icon.info {
  background: #409eff;
}

.notification-icon.warning {
  background: #e6a23c;
}

.notification-content {
  flex: 1;
}

.notification-content h4 {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 4px 0;
}

.notification-content p {
  font-size: 12px;
  color: #606266;
  margin: 0 0 4px 0;
}

.notification-time {
  font-size: 11px;
  color: #909399;
}

/* 热门会议 */
.popular-meetings {
  max-height: 300px;
  overflow-y: auto;
}

.popular-meeting-item {
  display: flex;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.popular-meeting-item:hover {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 12px;
}

.popular-meeting-item:last-child {
  border-bottom: none;
}

.rank {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  margin-right: 12px;
  flex-shrink: 0;
}

.popular-meeting-item:first-child .rank {
  background: #f56c6c;
}

.popular-meeting-item:nth-child(2) .rank {
  background: #e6a23c;
}

.popular-meeting-item:nth-child(3) .rank {
  background: #67c23a;
}

.popular-meeting-item .meeting-info {
  flex: 1;
}

.popular-meeting-item .meeting-info h4 {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 4px 0;
}

.popular-meeting-item .meeting-info p {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

/* 空状态 */
.empty-state {
  padding: 40px 0;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 15px;
  }
  
  .stats-grid {
    grid-template-columns: 1fr 1fr;
    gap: 15px;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
  
  .meeting-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .meeting-time {
    margin-right: 0;
    margin-bottom: 8px;
  }
  
  .meeting-status {
    margin-left: 0;
    margin-top: 8px;
  }
}
</style>

