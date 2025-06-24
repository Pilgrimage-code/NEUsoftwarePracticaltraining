<template>
  <div class="analytics modern-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">数据分析</h1>
        <div class="header-actions">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
            class="date-picker"
          />
          <el-button @click="exportReport" class="modern-btn secondary">
            <el-icon><Download /></el-icon>
            导出报告
          </el-button>
          <el-button @click="refreshData" class="modern-btn primary">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 概览统计 -->
    <div class="overview-stats">
      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon meetings">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-trend up">
            <el-icon><TrendCharts /></el-icon>
            +12.5%
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ overviewData.totalMeetings }}</div>
          <div class="stat-label">总会议数</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon users">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-trend up">
            <el-icon><TrendCharts /></el-icon>
            +8.3%
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ overviewData.activeUsers }}</div>
          <div class="stat-label">活跃用户</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon participation">
            <el-icon><PieChart /></el-icon>
          </div>
          <div class="stat-trend down">
            <el-icon><TrendCharts /></el-icon>
            -2.1%
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ overviewData.participationRate }}%</div>
          <div class="stat-label">参与率</div>
        </div>
      </div>

      <div class="stat-card">
        <div class="stat-header">
          <div class="stat-icon duration">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-trend up">
            <el-icon><TrendCharts /></el-icon>
            +5.7%
          </div>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ overviewData.avgDuration }}h</div>
          <div class="stat-label">平均时长</div>
        </div>
      </div>
    </div>

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
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// 响应式数据
const dateRange = ref([])
const trendPeriod = ref('month')
const behaviorTab = ref('activity')

const overviewData = ref({
  totalMeetings: 1248,
  activeUsers: 856,
  participationRate: 87.5,
  avgDuration: 2.3
})

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

// 方法
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
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['会议数量', '参与人数']
    },
    xAxis: {
      type: 'category',
      data: ['06-14', '06-15', '06-16', '06-17', '06-18', '06-19', '06-20']
    },
    yAxis: [
      {
        type: 'value',
        name: '会议数量',
        position: 'left'
      },
      {
        type: 'value',
        name: '参与人数',
        position: 'right'
      }
    ],
    series: [
      {
        name: '会议数量',
        type: 'line',
        data: [12, 15, 18, 14, 22, 19, 25],
        smooth: true,
        itemStyle: {
          color: '#667eea'
        }
      },
      {
        name: '参与人数',
        type: 'bar',
        yAxisIndex: 1,
        data: [156, 189, 234, 178, 298, 245, 325],
        itemStyle: {
          color: '#f093fb'
        }
      }
    ]
  }
  trendChart.setOption(option)
}

const initTypeChart = () => {
  if (!typeChartRef.value) return
  
  typeChart = echarts.init(typeChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '会议类型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
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
    tooltip: {
      position: 'top',
      formatter: function (params) {
        return `${days[params.value[1]]} ${hours[params.value[0]]}<br/>会议数量: ${params.value[2]}`
      }
    },
    grid: {
      height: '50%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: hours,
      splitArea: {
        show: true
      }
    },
    yAxis: {
      type: 'category',
      data: days,
      splitArea: {
        show: true
      }
    },
    visualMap: {
      min: 0,
      max: 20,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '15%',
      inRange: {
        color: ['#e0f3ff', '#667eea']
      }
    },
    series: [{
      name: '会议热度',
      type: 'heatmap',
      data: data,
      label: {
        show: true
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
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
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [12, 8, 45, 78, 65, 32],
        type: 'line',
        smooth: true,
        areaStyle: {
          color: 'rgba(102, 126, 234, 0.3)'
        },
        itemStyle: {
          color: '#667eea'
        }
      }]
    }
  } else if (behaviorTab.value === 'participation') {
    option = {
      tooltip: {
        trigger: 'item'
      },
      series: [{
        type: 'pie',
        radius: '50%',
        data: [
          { value: 1048, name: '积极参与' },
          { value: 735, name: '一般参与' },
          { value: 580, name: '被动参与' },
          { value: 484, name: '很少参与' }
        ]
      }]
    }
  } else {
    option = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: ['1星', '2星', '3星', '4星', '5星']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [5, 12, 45, 128, 234],
        type: 'bar',
        itemStyle: {
          color: '#f093fb'
        }
      }]
    }
  }
  
  behaviorChart.setOption(option)
}

const updateTrendChart = () => {
  // 根据选择的时间段更新趋势图
  ElMessage.info('更新趋势图数据')
}

const updateBehaviorData = () => {
  // 更新行为分析数据
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

const handleDateChange = () => {
  ElMessage.info('更新日期范围数据')
}

const exportReport = () => {
  ElMessage.success('报告导出功能开发中')
}

const refreshData = () => {
  ElMessage.success('数据已刷新')
}

const startRealtimeUpdate = () => {
  realtimeTimer = setInterval(() => {
    realtimeData.value.onlineUsers += Math.floor(Math.random() * 10 - 5)
    realtimeData.value.activeMeetings += Math.floor(Math.random() * 3 - 1)
    realtimeData.value.newUsers += Math.floor(Math.random() * 5)
    realtimeData.value.responseTime += Math.floor(Math.random() * 20 - 10)
    
    // 确保数据在合理范围内
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

// 生命周期
onMounted(() => {
  initCharts()
  startRealtimeUpdate()
  window.addEventListener('resize', resizeCharts)
})

onUnmounted(() => {
  if (realtimeTimer) {
    clearInterval(realtimeTimer)
  }
  window.removeEventListener('resize', resizeCharts)
  trendChart?.dispose()
  typeChart?.dispose()
  heatmapChart?.dispose()
  behaviorChart?.dispose()
})
</script>

<style scoped>
.analytics {
  padding: var(--spacing-2xl);
}

.page-header {
  margin-bottom: var(--spacing-2xl);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: var(--font-size-3xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.date-picker {
  width: 300px;
}

.overview-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.stat-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-md);
  transition: all var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.stat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-xl);
  color: white;
}

.stat-icon.meetings {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.users {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.participation {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.duration {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.stat-trend.up {
  color: var(--success-color);
}

.stat-trend.down {
  color: var(--error-color);
}

.stat-value {
  font-size: var(--font-size-3xl);
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
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
  
  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: flex-start;
  }
  
  .header-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .date-picker {
    width: 100%;
  }
  
  .overview-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .behavior-metrics {
    grid-template-columns: 1fr;
  }
  
  .realtime-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>

