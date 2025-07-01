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
    <!-- 统计卡片网格结束 -->

    <!-- 数据分析区整体迁移自Analytics.vue -->
    <div class="analytics modern-page">
      <!-- 页面头部（可选，如需显示可保留） -->
      <!-- <div class="page-header">...</div> -->

      <!-- 概览统计（可选，如需显示可保留） -->
      <!-- <div class="overview-stats">...</div> -->

      <!-- 图表区域 -->
      <div class="charts-section">
        <!-- 会议趋势图 -->
        <div class="chart-card">
          <div class="card-header">
            <h2>会议趋势分析</h2>
            <el-radio-group v-model="trendPeriod" @change="updateTrendChart">
              <el-radio-button label="week">最近7天</el-radio-button>
              <el-radio-button label="month">最近30天</el-radio-button>
              <el-radio-button label="quarter">最近3个月</el-radio-button>
            </el-radio-group>
          </div>
          <div class="card-body">
            <div ref="trendChartRef" class="chart-container"></div>
          </div>
        </div>
        <!-- 会议类型分布 -->
        <div class="chart-card">
          <div class="card-header">
            <h2>会议类型分布</h2>
          </div>
          <div class="card-body">
            <div ref="typeChartRef" class="chart-container"></div>
          </div>
        </div>
      </div>

      <!-- 详细数据 -->
      <div class="data-section">
        <!-- 部门参与度 -->
        <div class="data-card">
          <div class="card-header">
            <h2>部门参与度排行</h2>
          </div>
          <div class="card-body">
            <div class="department-list">
              <div 
                v-for="(dept, index) in departmentData" 
                :key="dept.name"
                class="department-item"
              >
                <div class="dept-rank">{{ index + 1 }}</div>
                <div class="dept-info">
                  <div class="dept-name">{{ dept.name }}</div>
                  <div class="dept-stats">
                    {{ dept.meetings }}场会议 • {{ dept.participants }}人参与
                  </div>
                </div>
                <div class="dept-progress">
                  <el-progress 
                    :percentage="dept.participation" 
                    :stroke-width="8"
                    :show-text="false"
                  />
                  <span class="progress-text">{{ dept.participation }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 热门时段 -->
        <div class="data-card">
          <div class="card-header">
            <h2>热门会议时段</h2>
          </div>
          <div class="card-body">
            <div ref="heatmapChartRef" class="chart-container"></div>
          </div>
        </div>
      </div>

      <!-- 用户行为分析 -->
      <div class="behavior-section">
        <div class="modern-card">
          <div class="card-header">
            <h2>用户行为分析</h2>
            <el-tabs v-model="behaviorTab" @tab-change="updateBehaviorData">
              <el-tab-pane label="活跃度" name="activity" />
              <el-tab-pane label="参与度" name="participation" />
              <el-tab-pane label="满意度" name="satisfaction" />
            </el-tabs>
          </div>
          <div class="card-body">
            <div class="behavior-metrics">
              <div class="metric-item">
                <div class="metric-icon">
                  <el-icon><DataAnalysis /></el-icon>
                </div>
                <div class="metric-content">
                  <div class="metric-value">{{ behaviorMetrics.primary.value }}</div>
                  <div class="metric-label">{{ behaviorMetrics.primary.label }}</div>
                </div>
              </div>
              <div class="metric-item">
                <div class="metric-icon">
                  <el-icon><TrendCharts /></el-icon>
                </div>
                <div class="metric-content">
                  <div class="metric-value">{{ behaviorMetrics.secondary.value }}</div>
                  <div class="metric-label">{{ behaviorMetrics.secondary.label }}</div>
                </div>
              </div>
              <div class="metric-item">
                <div class="metric-icon">
                  <el-icon><Star /></el-icon>
                </div>
                <div class="metric-content">
                  <div class="metric-value">{{ behaviorMetrics.tertiary.value }}</div>
                  <div class="metric-label">{{ behaviorMetrics.tertiary.label }}</div>
                </div>
              </div>
            </div>
            <div ref="behaviorChartRef" class="chart-container"></div>
          </div>
        </div>
      </div>

      <!-- 实时数据 -->
      <div class="realtime-section">
        <div class="modern-card">
          <div class="card-header">
            <h2>实时数据监控</h2>
            <div class="realtime-indicator">
              <div class="indicator-dot"></div>
              <span>实时更新</span>
            </div>
          </div>
          <div class="card-body">
            <div class="realtime-grid">
              <div class="realtime-item">
                <div class="realtime-label">当前在线用户</div>
                <div class="realtime-value">{{ realtimeData.onlineUsers }}</div>
              </div>
              <div class="realtime-item">
                <div class="realtime-label">进行中的会议</div>
                <div class="realtime-value">{{ realtimeData.activeMeetings }}</div>
              </div>
              <div class="realtime-item">
                <div class="realtime-label">今日新增用户</div>
                <div class="realtime-value">{{ realtimeData.newUsers }}</div>
              </div>
              <div class="realtime-item">
                <div class="realtime-label">系统响应时间</div>
                <div class="realtime-value">{{ realtimeData.responseTime }}ms</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const stats = reactive({
  totalMeetings: 156,
  totalUsers: 1248,
  totalNews: 89,
  totalRegistrations: 2341
})

// 数据分析区相关响应式数据
const trendPeriod = ref('month')
const behaviorTab = ref('activity')

const departmentData = ref([
  { name: '技术部', meetings: 45, participants: 128, participation: 92 },
  { name: '销售部', meetings: 38, participants: 95, participation: 88 },
  { name: '市场部', meetings: 32, participants: 76, participation: 85 },
  { name: '人事部', meetings: 28, participants: 52, participation: 82 },
  { name: '财务部', meetings: 25, participants: 48, participation: 79 }
])

const behaviorMetrics = ref({
  primary: { value: '4.2', label: '平均活跃度' },
  secondary: { value: '89%', label: '参与率' },
  tertiary: { value: '4.6', label: '满意度评分' }
})

const realtimeData = ref({
  onlineUsers: 234,
  activeMeetings: 12,
  newUsers: 18,
  responseTime: 156
})

// 图表引用
const trendChartRef = ref()
const typeChartRef = ref()
const heatmapChartRef = ref()
const behaviorChartRef = ref()

let trendChart = null
let typeChart = null
let heatmapChart = null
let behaviorChart = null
let realtimeTimer = null

// 图表初始化方法
const initCharts = async () => {
  await nextTick()
  initTrendChart()
  initTypeChart()
  initHeatmapChart()
  initBehaviorChart()
}

const initTrendChart = () => {
  if (!trendChartRef.value) return
  trendChart = echarts.init(trendChartRef.value)
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
    legend: { data: ['会议数量', '参与人数'] },
    xAxis: { type: 'category', data: ['06-14', '06-15', '06-16', '06-17', '06-18', '06-19', '06-20'] },
    yAxis: [
      { type: 'value', name: '会议数量', position: 'left' },
      { type: 'value', name: '参与人数', position: 'right' }
    ],
    series: [
      { name: '会议数量', type: 'line', data: [12, 15, 18, 14, 22, 19, 25], smooth: true, itemStyle: { color: '#667eea' } },
      { name: '参与人数', type: 'bar', yAxisIndex: 1, data: [156, 189, 234, 178, 298, 245, 325], itemStyle: { color: '#f093fb' } }
    ]
  }
  trendChart.setOption(option)
}

const initTypeChart = () => {
  if (!typeChartRef.value) return
  typeChart = echarts.init(typeChartRef.value)
  const option = {
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        name: '会议类型', type: 'pie', radius: ['40%', '70%'], avoidLabelOverlap: false,
        label: { show: false, position: 'center' },
        emphasis: { label: { show: true, fontSize: '18', fontWeight: 'bold' } },
        labelLine: { show: false },
        data: [
          { value: 335, name: '部门会议' },
          { value: 310, name: '项目会议' },
          { value: 234, name: '培训会议' },
          { value: 135, name: '客户会议' },
          { value: 148, name: '其他会议' }
        ]
      }
    ]
  }
  typeChart.setOption(option)
}

const initHeatmapChart = () => {
  if (!heatmapChartRef.value) return
  heatmapChart = echarts.init(heatmapChartRef.value)
  const hours = ['9:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00']
  const days = ['周一', '周二', '周三', '周四', '周五']
  const data = []
  for (let i = 0; i < days.length; i++) {
    for (let j = 0; j < hours.length; j++) {
      data.push([j, i, Math.floor(Math.random() * 20)])
    }
  }
  const option = {
    tooltip: { position: 'top', formatter: params => `${days[params.value[1]]} ${hours[params.value[0]]}<br/>会议数量: ${params.value[2]}` },
    grid: { height: '50%', top: '10%' },
    xAxis: { type: 'category', data: hours, splitArea: { show: true } },
    yAxis: { type: 'category', data: days, splitArea: { show: true } },
    visualMap: { min: 0, max: 20, calculable: true, orient: 'horizontal', left: 'center', bottom: '15%', inRange: { color: ['#e0f3ff', '#667eea'] } },
    series: [{ name: '会议热度', type: 'heatmap', data, label: { show: true }, emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0, 0, 0, 0.5)' } } }]
  }
  heatmapChart.setOption(option)
}

const initBehaviorChart = () => {
  if (!behaviorChartRef.value) return
  behaviorChart = echarts.init(behaviorChartRef.value)
  updateBehaviorChart()
}

const updateBehaviorChart = () => {
  if (!behaviorChart) return
  let option = {}
  if (behaviorTab.value === 'activity') {
    option = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00'] },
      yAxis: { type: 'value' },
      series: [{ data: [12, 8, 45, 78, 65, 32], type: 'line', smooth: true, areaStyle: { color: 'rgba(102, 126, 234, 0.3)' }, itemStyle: { color: '#667eea' } }]
    }
  } else if (behaviorTab.value === 'participation') {
    option = {
      tooltip: { trigger: 'item' },
      series: [{ type: 'pie', radius: '50%', data: [ { value: 1048, name: '积极参与' }, { value: 735, name: '一般参与' }, { value: 580, name: '被动参与' }, { value: 484, name: '很少参与' } ] }]
    }
  } else {
    option = {
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: ['1星', '2星', '3星', '4星', '5星'] },
      yAxis: { type: 'value' },
      series: [{ data: [5, 12, 45, 128, 234], type: 'bar', itemStyle: { color: '#f093fb' } }]
    }
  }
  behaviorChart.setOption(option)
}

const updateTrendChart = () => {
  ElMessage.info('更新趋势图数据')
}

const updateBehaviorData = () => {
  if (behaviorTab.value === 'activity') {
    behaviorMetrics.value = {
      primary: { value: '4.2', label: '平均活跃度' },
      secondary: { value: '89%', label: '在线时长' },
      tertiary: { value: '156', label: '日均操作' }
    }
  } else if (behaviorTab.value === 'participation') {
    behaviorMetrics.value = {
      primary: { value: '87%', label: '参与率' },
      secondary: { value: '3.2', label: '平均发言' },
      tertiary: { value: '92%', label: '出席率' }
    }
  } else {
    behaviorMetrics.value = {
      primary: { value: '4.6', label: '满意度评分' },
      secondary: { value: '94%', label: '推荐意愿' },
      tertiary: { value: '4.3', label: '体验评分' }
    }
  }
  updateBehaviorChart()
}

const startRealtimeUpdate = () => {
  realtimeTimer = setInterval(() => {
    realtimeData.value.onlineUsers += Math.floor(Math.random() * 10 - 5)
    realtimeData.value.activeMeetings += Math.floor(Math.random() * 3 - 1)
    realtimeData.value.newUsers += Math.floor(Math.random() * 5)
    realtimeData.value.responseTime += Math.floor(Math.random() * 20 - 10)
    realtimeData.value.onlineUsers = Math.max(200, Math.min(300, realtimeData.value.onlineUsers))
    realtimeData.value.activeMeetings = Math.max(8, Math.min(20, realtimeData.value.activeMeetings))
    realtimeData.value.responseTime = Math.max(100, Math.min(200, realtimeData.value.responseTime))
  }, 3000)
}

const resizeCharts = () => {
  trendChart?.resize()
  typeChart?.resize()
  heatmapChart?.resize()
  behaviorChart?.resize()
}

onMounted(() => {
  initCharts()
  startRealtimeUpdate()
  window.addEventListener('resize', resizeCharts)
})

onUnmounted(() => {
  if (realtimeTimer) clearInterval(realtimeTimer)
  window.removeEventListener('resize', resizeCharts)
  trendChart?.dispose()
  typeChart?.dispose()
  heatmapChart?.dispose()
  behaviorChart?.dispose()
})

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
</script>

<style scoped>
:root {
  --spacing-2xl: 32px;
  --spacing-xl: 28px;
  --spacing-lg: 24px;
  --spacing-md: 16px;
  --spacing-sm: 8px;
  --spacing-xs: 4px;
  --bg-primary: #fff;
  --bg-secondary: #f8fafc;
  --radius-xl: 20px;
  --radius-lg: 16px;
  --radius-md: 12px;
  --font-size-3xl: 32px;
  --font-size-2xl: 24px;
  --font-size-xl: 20px;
  --font-size-lg: 16px;
  --font-size-sm: 14px;
  --primary-color: #667eea;
  --success-color: #10b981;
  --error-color: #ef4444;
  --text-primary: #1a1a1a;
  --text-secondary: #64748b;
  --shadow-md: 0 4px 20px rgba(0,0,0,0.08);
  --shadow-lg: 0 20px 40px rgba(0,0,0,0.15);
  --transition-fast: 0.3s;
}

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
.analytics {
  padding: var(--spacing-2xl);
}

.charts-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.chart-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-md);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.card-header h2 {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.data-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.data-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-md);
}

.department-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.department-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.dept-rank {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: var(--font-size-sm);
}

.dept-info {
  flex: 1;
}

.dept-name {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.dept-stats {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.dept-progress {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  min-width: 120px;
}

.progress-text {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--text-primary);
}

.behavior-section,
.realtime-section {
  margin-bottom: var(--spacing-2xl);
}

.behavior-metrics {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-xl);
}

.metric-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.metric-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  background: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-lg);
}

.metric-value {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
}

.metric-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.realtime-indicator {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-sm);
  color: var(--success-color);
}

.indicator-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--success-color);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(16, 185, 129, 0.7);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(16, 185, 129, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(16, 185, 129, 0);
  }
}

.realtime-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
}

.realtime-item {
  text-align: center;
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.realtime-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin-bottom: var(--spacing-sm);
}

.realtime-value {
  font-size: var(--font-size-2xl);
  font-weight: 600;
  color: var(--text-primary);
}

@media (max-width: 1200px) {
  .charts-section {
    grid-template-columns: 1fr;
  }
  .data-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .analytics {
    padding: var(--spacing-lg);
  }
  .charts-section {
    grid-template-columns: 1fr;
  }
  .data-section {
    grid-template-columns: 1fr;
  }
  .behavior-metrics {
    grid-template-columns: 1fr;
  }
  .realtime-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>

