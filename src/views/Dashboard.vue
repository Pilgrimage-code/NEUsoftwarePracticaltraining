<template>
  <div class="dashboard-container">
    <!-- 顶部统计卡片 -->
    <div class="stats-grid">
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-icon meetings">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stats-info">
            <h3>{{ stats.totalMeetings }}</h3>
            <p>总会议数</p>
          </div>
        </div>
      </el-card>
      
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-icon users">
            <el-icon><User /></el-icon>
          </div>
          <div class="stats-info">
            <h3>{{ stats.totalUsers }}</h3>
            <p>注册用户</p>
          </div>
        </div>
      </el-card>
      
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-icon news">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stats-info">
            <h3>{{ stats.totalNews }}</h3>
            <p>发布资讯</p>
          </div>
        </div>
      </el-card>
      
      <el-card class="stats-card" shadow="hover">
        <div class="stats-content">
          <div class="stats-icon registrations">
            <el-icon><Tickets /></el-icon>
          </div>
          <div class="stats-info">
            <h3>{{ stats.totalRegistrations }}</h3>
            <p>会议报名</p>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧内容 -->
      <div class="left-content">
        <!-- 即将开始的会议 -->
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3><el-icon><Calendar /></el-icon> 即将开始的会议</h3>
              <el-link type="primary" @click="$router.push('/dashboard/meetings')">查看全部</el-link>
            </div>
          </template>
          
          <div v-if="upcomingMeetings.length === 0" class="empty-state">
            <el-empty description="暂无即将开始的会议" />
          </div>
          
          <div v-else class="meeting-list">
            <div
              v-for="meeting in upcomingMeetings"
              :key="meeting.id"
              class="meeting-item"
              @click="viewMeetingDetail(meeting.id)"
            >
              <div class="meeting-time">
                <div class="date">{{ formatDate(meeting.startTime) }}</div>
                <div class="time">{{ formatTime(meeting.startTime) }}</div>
              </div>
              <div class="meeting-info">
                <h4>{{ meeting.title }}</h4>
                <p><el-icon><Location /></el-icon> {{ meeting.location }}</p>
                <div class="meeting-meta">
                  <el-tag size="small" :type="getMeetingTypeTag(meeting.type)">
                    {{ getMeetingTypeText(meeting.type) }}
                  </el-tag>
                  <span class="participants">
                    <el-icon><User /></el-icon> {{ meeting.registrationCount || 0 }}人报名
                  </span>
                </div>
              </div>
              <div class="meeting-status">
                <el-tag :type="getMeetingStatusTag(meeting.status)">
                  {{ getMeetingStatusText(meeting.status) }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 最新资讯 -->
        <el-card class="content-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h3><el-icon><Document /></el-icon> 最新资讯</h3>
              <el-link type="primary" @click="$router.push('/dashboard/news')">查看全部</el-link>
            </div>
          </template>
          
          <div v-if="latestNews.length === 0" class="empty-state">
            <el-empty description="暂无最新资讯" />
          </div>
          
          <div v-else class="news-list">
            <div
              v-for="news in latestNews"
              :key="news.id"
              class="news-item"
              @click="viewNewsDetail(news.id)"
            >
              <div class="news-cover" v-if="news.coverImage">
                <img :src="news.coverImage" :alt="news.title" />
              </div>
              <div class="news-content">
                <h4>{{ news.title }}</h4>
                <p class="news-summary">{{ news.summary }}</p>
                <div class="news-meta">
                  <span class="author">{{ news.authorName }}</span>
                  <span class="date">{{ formatDate(news.publishTime) }}</span>
                  <span class="views">
                    <el-icon><View /></el-icon> {{ news.viewCount }}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧内容 -->
      <div class="right-content">
        <!-- 快速操作 -->
        <el-card class="content-card" shadow="hover">
          <template #header>
            <h3><el-icon><Operation /></el-icon> 快速操作</h3>
          </template>
          
          <div class="quick-actions">
            <el-button
              type="primary"
              size="large"
              class="action-btn"
              @click="$router.push('/dashboard/meetings/create')"
            >
              <el-icon><Plus /></el-icon>
              创建会议
            </el-button>
            
            <el-button
              type="success"
              size="large"
              class="action-btn"
              @click="$router.push('/dashboard/news/create')"
            >
              <el-icon><EditPen /></el-icon>
              发布资讯
            </el-button>
            
            <el-button
              type="info"
              size="large"
              class="action-btn"
              @click="$router.push('/dashboard/users')"
            >
              <el-icon><UserFilled /></el-icon>
              用户管理
            </el-button>
            
            <el-button
              type="warning"
              size="large"
              class="action-btn"
              @click="$router.push('/dashboard/departments')"
            >
              <el-icon><OfficeBuilding /></el-icon>
              部门管理
            </el-button>
          </div>
        </el-card>

        <!-- 系统通知 -->
        <el-card class="content-card" shadow="hover">
          <template #header>
            <h3><el-icon><Bell /></el-icon> 系统通知</h3>
          </template>
          
          <div class="notification-list">
            <div class="notification-item">
              <div class="notification-icon success">
                <el-icon><SuccessFilled /></el-icon>
              </div>
              <div class="notification-content">
                <h4>系统更新完成</h4>
                <p>测盟汇管理系统已更新至v1.0.0版本</p>
                <span class="notification-time">2小时前</span>
              </div>
            </div>
            
            <div class="notification-item">
              <div class="notification-icon info">
                <el-icon><InfoFilled /></el-icon>
              </div>
              <div class="notification-content">
                <h4>会议提醒</h4>
                <p>您有3个会议即将开始，请及时参加</p>
                <span class="notification-time">1天前</span>
              </div>
            </div>
            
            <div class="notification-item">
              <div class="notification-icon warning">
                <el-icon><WarningFilled /></el-icon>
              </div>
              <div class="notification-content">
                <h4>存储空间提醒</h4>
                <p>系统存储空间使用率已达80%</p>
                <span class="notification-time">3天前</span>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 热门会议 -->
        <el-card class="content-card" shadow="hover">
          <template #header>
            <h3><el-icon><TrendCharts /></el-icon> 热门会议</h3>
          </template>
          
          <div v-if="popularMeetings.length === 0" class="empty-state">
            <el-empty description="暂无热门会议" />
          </div>
          
          <div v-else class="popular-meetings">
            <div
              v-for="(meeting, index) in popularMeetings"
              :key="meeting.id"
              class="popular-meeting-item"
              @click="viewMeetingDetail(meeting.id)"
            >
              <div class="rank">{{ index + 1 }}</div>
              <div class="meeting-info">
                <h4>{{ meeting.title }}</h4>
                <p>{{ meeting.registrationCount || 0 }}人报名</p>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getMeetingList, getPopularMeetings } from '@/api/meeting'
import { getNewsList } from '@/api/news'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const stats = reactive({
  totalMeetings: 0,
  totalUsers: 0,
  totalNews: 0,
  totalRegistrations: 0
})

// 即将开始的会议
const upcomingMeetings = ref([])
// 最新资讯
const latestNews = ref([])
// 热门会议
const popularMeetings = ref([])

// 加载统计数据
const loadStats = async () => {
  try {
    // 这里应该调用实际的API获取统计数据
    // 暂时使用模拟数据
    stats.totalMeetings = 156
    stats.totalUsers = 1248
    stats.totalNews = 89
    stats.totalRegistrations = 2341
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 加载即将开始的会议
const loadUpcomingMeetings = async () => {
  try {
    // 模拟数据
    upcomingMeetings.value = [
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
    ]
  } catch (error) {
    console.error('加载即将开始的会议失败:', error)
  }
}

// 加载最新资讯
const loadLatestNews = async () => {
  try {
    // 模拟数据
    latestNews.value = [
      {
        id: 1,
        title: '测盟汇管理系统正式上线',
        summary: '经过团队的不懈努力，测盟汇管理系统正式上线，为用户提供更好的会议管理体验。',
        authorName: '系统管理员',
        publishTime: '2024-06-15T10:00:00',
        viewCount: 1256,
        coverImage: '/news-cover-1.jpg'
      },
      {
        id: 2,
        title: '新功能发布：在线培训模块',
        summary: '我们很高兴地宣布，在线培训模块已经正式发布，支持课程管理和学习进度跟踪。',
        authorName: '产品经理',
        publishTime: '2024-06-14T15:30:00',
        viewCount: 892,
        coverImage: '/news-cover-2.jpg'
      },
      {
        id: 3,
        title: '系统维护通知',
        summary: '为了提供更好的服务，系统将于本周末进行例行维护，预计维护时间2小时。',
        authorName: '技术团队',
        publishTime: '2024-06-13T09:00:00',
        viewCount: 567
      }
    ]
  } catch (error) {
    console.error('加载最新资讯失败:', error)
  }
}

// 加载热门会议
const loadPopularMeetings = async () => {
  try {
    // 模拟数据
    popularMeetings.value = [
      {
        id: 1,
        title: '2024年度技术交流大会',
        registrationCount: 456
      },
      {
        id: 2,
        title: 'AI技术发展趋势研讨会',
        registrationCount: 389
      },
      {
        id: 3,
        title: '数字化转型实践分享',
        registrationCount: 267
      },
      {
        id: 4,
        title: '创新创业论坛',
        registrationCount: 234
      },
      {
        id: 5,
        title: '项目管理最佳实践',
        registrationCount: 198
      }
    ]
  } catch (error) {
    console.error('加载热门会议失败:', error)
  }
}

// 查看会议详情
const viewMeetingDetail = (id) => {
  router.push(`/dashboard/meetings/${id}`)
}

// 查看资讯详情
const viewNewsDetail = (id) => {
  router.push(`/dashboard/news/${id}`)
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

// 获取会议类型标签
const getMeetingTypeTag = (type) => {
  const tags = {
    1: 'primary',
    2: 'success',
    3: 'warning',
    4: 'info'
  }
  return tags[type] || 'info'
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

// 获取会议状态标签
const getMeetingStatusTag = (status) => {
  const tags = {
    0: 'info',
    1: 'success',
    2: 'danger'
  }
  return tags[status] || 'info'
}

// 获取会议状态文本
const getMeetingStatusText = (status) => {
  const texts = {
    0: '草稿',
    1: '已发布',
    2: '已取消'
  }
  return texts[status] || '未知'
}

onMounted(() => {
  loadStats()
  loadUpcomingMeetings()
  loadLatestNews()
  loadPopularMeetings()
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stats-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stats-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.stats-icon.meetings {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-icon.users {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stats-icon.news {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stats-icon.registrations {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stats-info h3 {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 5px 0;
}

.stats-info p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

/* 主要内容区域 */
.main-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
}

.content-card {
  border: none;
  border-radius: 12px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.content-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.card-header h3 .el-icon {
  margin-right: 8px;
  color: #409eff;
}

/* 会议列表 */
.meeting-list {
  max-height: 400px;
  overflow-y: auto;
}

.meeting-item {
  display: flex;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.meeting-item:hover {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px 12px;
}

.meeting-item:last-child {
  border-bottom: none;
}

.meeting-time {
  text-align: center;
  margin-right: 16px;
  min-width: 60px;
}

.meeting-time .date {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
}

.meeting-time .time {
  font-size: 12px;
  color: #909399;
}

.meeting-info {
  flex: 1;
}

.meeting-info h4 {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  margin: 0 0 8px 0;
}

.meeting-info p {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
  margin: 0 0 8px 0;
}

.meeting-info p .el-icon {
  margin-right: 4px;
}

.meeting-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.participants {
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

