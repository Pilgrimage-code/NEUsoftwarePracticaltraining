<template>
  <div class="system-monitor modern-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">系统监控</h1>
        <div class="header-actions">
          <div class="status-indicator">
            <div class="status-dot healthy"></div>
            <span>系统运行正常</span>
          </div>
          <el-button @click="refreshMonitor" class="modern-btn primary">
            <el-icon><Refresh /></el-icon>
            刷新监控
          </el-button>
        </div>
      </div>
    </div>

    <!-- 系统状态概览 -->
    <div class="system-overview">
      <div class="overview-card cpu">
        <div class="card-header">
          <div class="card-icon">
            <el-icon><Cpu /></el-icon>
          </div>
          <div class="card-title">CPU使用率</div>
        </div>
        <div class="card-content">
          <div class="metric-value">{{ systemMetrics.cpu }}%</div>
          <el-progress 
            :percentage="systemMetrics.cpu" 
            :stroke-width="8"
            :show-text="false"
            :color="getProgressColor(systemMetrics.cpu)"
          />
        </div>
      </div>

      <div class="overview-card memory">
        <div class="card-header">
          <div class="card-icon">
            <el-icon><Monitor /></el-icon>
          </div>
          <div class="card-title">内存使用率</div>
        </div>
        <div class="card-content">
          <div class="metric-value">{{ systemMetrics.memory }}%</div>
          <el-progress 
            :percentage="systemMetrics.memory" 
            :stroke-width="8"
            :show-text="false"
            :color="getProgressColor(systemMetrics.memory)"
          />
        </div>
      </div>

      <div class="overview-card disk">
        <div class="card-header">
          <div class="card-icon">
            <el-icon><Coin /></el-icon>
          </div>
          <div class="card-title">磁盘使用率</div>
        </div>
        <div class="card-content">
          <div class="metric-value">{{ systemMetrics.disk }}%</div>
          <el-progress 
            :percentage="systemMetrics.disk" 
            :stroke-width="8"
            :show-text="false"
            :color="getProgressColor(systemMetrics.disk)"
          />
        </div>
      </div>

      <div class="overview-card network">
        <div class="card-header">
          <div class="card-icon">
            <el-icon><Connection /></el-icon>
          </div>
          <div class="card-title">网络流量</div>
        </div>
        <div class="card-content">
          <div class="metric-value">{{ systemMetrics.network }} MB/s</div>
          <div class="network-details">
            <span>↑ {{ systemMetrics.networkUp }} MB/s</span>
            <span>↓ {{ systemMetrics.networkDown }} MB/s</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 性能图表 -->
    <div class="performance-charts">
      <div class="chart-card">
        <div class="card-header">
          <h2>系统性能趋势</h2>
          <el-radio-group v-model="chartTimeRange" @change="updatePerformanceChart">
            <el-radio-button label="1h">最近1小时</el-radio-button>
            <el-radio-button label="6h">最近6小时</el-radio-button>
            <el-radio-button label="24h">最近24小时</el-radio-button>
          </el-radio-group>
        </div>
        <div class="card-body">
          <div ref="performanceChartRef" class="chart-container"></div>
        </div>
      </div>
    </div>

    <!-- 服务状态 -->
    <div class="services-section">
      <div class="modern-card">
        <div class="card-header">
          <h2>服务状态监控</h2>
          <el-button @click="checkAllServices" size="small" class="modern-btn secondary">
            <el-icon><Refresh /></el-icon>
            检查所有服务
          </el-button>
        </div>
        <div class="card-body">
          <div class="services-grid">
            <div 
              v-for="service in services" 
              :key="service.name"
              class="service-item"
              :class="{ 'service-error': service.status === 'error', 'service-warning': service.status === 'warning' }"
            >
              <div class="service-header">
                <div class="service-status">
                  <div 
                    class="status-dot" 
                    :class="service.status"
                  ></div>
                  <span class="service-name">{{ service.name }}</span>
                </div>
                <div class="service-actions">
                  <el-button 
                    type="text" 
                    size="small"
                    @click="restartService(service)"
                    v-if="service.status === 'error'"
                  >
                    重启
                  </el-button>
                  <el-button 
                    type="text" 
                    size="small"
                    @click="viewServiceLogs(service)"
                  >
                    日志
                  </el-button>
                </div>
              </div>
              <div class="service-details">
                <div class="detail-item">
                  <span class="detail-label">响应时间:</span>
                  <span class="detail-value">{{ service.responseTime }}ms</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">运行时间:</span>
                  <span class="detail-value">{{ service.uptime }}</span>
                </div>
                <div class="detail-item">
                  <span class="detail-label">内存占用:</span>
                  <span class="detail-value">{{ service.memory }}MB</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 错误日志 -->
    <div class="logs-section">
      <div class="modern-card">
        <div class="card-header">
          <h2>系统日志</h2>
          <div class="log-filters">
            <el-select v-model="logLevel" @change="filterLogs" size="small">
              <el-option label="全部" value="all" />
              <el-option label="错误" value="error" />
              <el-option label="警告" value="warning" />
              <el-option label="信息" value="info" />
            </el-select>
            <el-button @click="clearLogs" size="small" class="modern-btn secondary">
              清空日志
            </el-button>
          </div>
        </div>
        <div class="card-body">
          <div class="logs-container">
            <div 
              v-for="log in filteredLogs" 
              :key="log.id"
              class="log-item"
              :class="`log-${log.level}`"
            >
              <div class="log-time">{{ formatDateTime(log.timestamp) }}</div>
              <div class="log-level">{{ log.level.toUpperCase() }}</div>
              <div class="log-message">{{ log.message }}</div>
              <div class="log-source">{{ log.source }}</div>
            </div>
          </div>
          <div class="logs-pagination">
            <el-pagination
              v-model:current-page="currentPage"
              :page-size="pageSize"
              :total="totalLogs"
              layout="prev, pager, next"
              @current-change="loadLogs"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 告警设置 -->
    <div class="alerts-section">
      <div class="modern-card">
        <div class="card-header">
          <h2>告警配置</h2>
          <el-button @click="addAlert" class="modern-btn primary">
            <el-icon><Plus /></el-icon>
            添加告警
          </el-button>
        </div>
        <div class="card-body">
          <div class="alerts-list">
            <div 
              v-for="alert in alerts" 
              :key="alert.id"
              class="alert-item"
            >
              <div class="alert-info">
                <div class="alert-name">{{ alert.name }}</div>
                <div class="alert-condition">{{ alert.condition }}</div>
              </div>
              <div class="alert-status">
                <el-switch 
                  v-model="alert.enabled"
                  @change="toggleAlert(alert)"
                />
              </div>
              <div class="alert-actions">
                <el-button type="text" size="small" @click="editAlert(alert)">
                  编辑
                </el-button>
                <el-button type="text" size="small" @click="deleteAlert(alert)" class="danger-btn">
                  删除
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as echarts from 'echarts'
import dayjs from 'dayjs'

// 响应式数据
const chartTimeRange = ref('6h')
const logLevel = ref('all')
const currentPage = ref(1)
const pageSize = ref(20)

const systemMetrics = ref({
  cpu: 45,
  memory: 68,
  disk: 32,
  network: 12.5,
  networkUp: 5.2,
  networkDown: 7.3
})

const services = ref([
  {
    name: 'Web服务器',
    status: 'healthy',
    responseTime: 156,
    uptime: '15天 8小时',
    memory: 512
  },
  {
    name: '数据库',
    status: 'healthy',
    responseTime: 23,
    uptime: '15天 8小时',
    memory: 1024
  },
  {
    name: '缓存服务',
    status: 'warning',
    responseTime: 89,
    uptime: '2天 14小时',
    memory: 256
  },
  {
    name: '文件服务',
    status: 'error',
    responseTime: 0,
    uptime: '0分钟',
    memory: 0
  }
])

const logs = ref([
  {
    id: 1,
    timestamp: '2024-06-20 14:30:25',
    level: 'error',
    message: '文件服务连接失败',
    source: 'FileService'
  },
  {
    id: 2,
    timestamp: '2024-06-20 14:28:15',
    level: 'warning',
    message: '缓存服务响应时间过长',
    source: 'CacheService'
  },
  {
    id: 3,
    timestamp: '2024-06-20 14:25:10',
    level: 'info',
    message: '用户登录成功',
    source: 'AuthService'
  }
])

const alerts = ref([
  {
    id: 1,
    name: 'CPU使用率告警',
    condition: 'CPU > 80%',
    enabled: true
  },
  {
    id: 2,
    name: '内存使用率告警',
    condition: '内存 > 90%',
    enabled: true
  },
  {
    id: 3,
    name: '磁盘空间告警',
    condition: '磁盘 > 85%',
    enabled: false
  }
])

// 图表引用
const performanceChartRef = ref()
let performanceChart = null
let metricsTimer = null

// 计算属性
const filteredLogs = computed(() => {
  if (logLevel.value === 'all') return logs.value
  return logs.value.filter(log => log.level === logLevel.value)
})

const totalLogs = computed(() => filteredLogs.value.length)

// 方法
const initPerformanceChart = async () => {
  await nextTick()
  if (!performanceChartRef.value) return
  
  performanceChart = echarts.init(performanceChartRef.value)
  updatePerformanceChart()
}

const updatePerformanceChart = () => {
  if (!performanceChart) return
  
  // 生成模拟数据
  const now = dayjs()
  const timePoints = []
  const cpuData = []
  const memoryData = []
  const diskData = []
  
  let points = 24
  let interval = 'hour'
  
  if (chartTimeRange.value === '1h') {
    points = 12
    interval = 'minute'
  } else if (chartTimeRange.value === '6h') {
    points = 24
    interval = 'minute'
  }
  
  for (let i = points; i >= 0; i--) {
    const time = now.subtract(i * (interval === 'hour' ? 60 : 15), 'minute')
    timePoints.push(time.format('HH:mm'))
    cpuData.push(Math.floor(Math.random() * 40) + 30)
    memoryData.push(Math.floor(Math.random() * 30) + 50)
    diskData.push(Math.floor(Math.random() * 20) + 25)
  }
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['CPU', '内存', '磁盘']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: timePoints
    },
    yAxis: {
      type: 'value',
      max: 100,
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [
      {
        name: 'CPU',
        type: 'line',
        data: cpuData,
        smooth: true,
        itemStyle: {
          color: '#667eea'
        },
        areaStyle: {
          color: 'rgba(102, 126, 234, 0.3)'
        }
      },
      {
        name: '内存',
        type: 'line',
        data: memoryData,
        smooth: true,
        itemStyle: {
          color: '#f093fb'
        },
        areaStyle: {
          color: 'rgba(240, 147, 251, 0.3)'
        }
      },
      {
        name: '磁盘',
        type: 'line',
        data: diskData,
        smooth: true,
        itemStyle: {
          color: '#4facfe'
        },
        areaStyle: {
          color: 'rgba(79, 172, 254, 0.3)'
        }
      }
    ]
  }
  
  performanceChart.setOption(option)
}

const getProgressColor = (percentage) => {
  if (percentage < 50) return '#67C23A'
  if (percentage < 80) return '#E6A23C'
  return '#F56C6C'
}

const refreshMonitor = () => {
  // 更新系统指标
  systemMetrics.value.cpu = Math.floor(Math.random() * 50) + 20
  systemMetrics.value.memory = Math.floor(Math.random() * 40) + 40
  systemMetrics.value.disk = Math.floor(Math.random() * 30) + 20
  systemMetrics.value.network = (Math.random() * 20 + 5).toFixed(1)
  systemMetrics.value.networkUp = (Math.random() * 10 + 2).toFixed(1)
  systemMetrics.value.networkDown = (Math.random() * 15 + 3).toFixed(1)
  
  updatePerformanceChart()
  ElMessage.success('监控数据已刷新')
}

const checkAllServices = () => {
  services.value.forEach(service => {
    if (service.status === 'error') {
      service.status = Math.random() > 0.5 ? 'healthy' : 'warning'
      service.responseTime = Math.floor(Math.random() * 200) + 50
    }
  })
  ElMessage.success('服务状态检查完成')
}

const restartService = async (service) => {
  try {
    await ElMessageBox.confirm(`确定要重启 ${service.name} 吗？`, '确认重启', {
      type: 'warning'
    })
    
    service.status = 'healthy'
    service.responseTime = Math.floor(Math.random() * 100) + 50
    service.uptime = '刚刚重启'
    
    ElMessage.success(`${service.name} 重启成功`)
  } catch {
    // 用户取消
  }
}

const viewServiceLogs = (service) => {
  ElMessage.info(`查看 ${service.name} 日志功能开发中`)
}

const filterLogs = () => {
  currentPage.value = 1
}

const loadLogs = () => {
  // 这里应该调用API加载日志
  ElMessage.info('加载日志数据')
}

const clearLogs = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有日志吗？', '确认清空', {
      type: 'warning'
    })
    
    logs.value = []
    ElMessage.success('日志已清空')
  } catch {
    // 用户取消
  }
}

const addAlert = () => {
  ElMessage.info('添加告警功能开发中')
}

const editAlert = (alert) => {
  ElMessage.info(`编辑告警: ${alert.name}`)
}

const toggleAlert = (alert) => {
  ElMessage.success(`告警 ${alert.name} 已${alert.enabled ? '启用' : '禁用'}`)
}

const deleteAlert = async (alert) => {
  try {
    await ElMessageBox.confirm(`确定要删除告警 "${alert.name}" 吗？`, '确认删除', {
      type: 'warning'
    })
    
    const index = alerts.value.findIndex(a => a.id === alert.id)
    if (index > -1) {
      alerts.value.splice(index, 1)
      ElMessage.success('告警已删除')
    }
  } catch {
    // 用户取消
  }
}

const formatDateTime = (dateTime) => {
  return dayjs(dateTime).format('MM-DD HH:mm:ss')
}

const startMetricsUpdate = () => {
  metricsTimer = setInterval(() => {
    systemMetrics.value.cpu += Math.floor(Math.random() * 10 - 5)
    systemMetrics.value.memory += Math.floor(Math.random() * 6 - 3)
    systemMetrics.value.disk += Math.floor(Math.random() * 4 - 2)
    
    // 确保数据在合理范围内
    systemMetrics.value.cpu = Math.max(10, Math.min(90, systemMetrics.value.cpu))
    systemMetrics.value.memory = Math.max(30, Math.min(95, systemMetrics.value.memory))
    systemMetrics.value.disk = Math.max(15, Math.min(85, systemMetrics.value.disk))
  }, 5000)
}

const resizeChart = () => {
  performanceChart?.resize()
}

// 生命周期
onMounted(() => {
  initPerformanceChart()
  startMetricsUpdate()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  if (metricsTimer) {
    clearInterval(metricsTimer)
  }
  window.removeEventListener('resize', resizeChart)
  performanceChart?.dispose()
})
</script>

<style scoped>
.system-monitor {
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
  gap: var(--spacing-lg);
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-sm);
  color: var(--success-color);
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.status-dot.healthy {
  background: var(--success-color);
  animation: pulse 2s infinite;
}

.system-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.overview-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-md);
  transition: all var(--transition-fast);
}

.overview-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.card-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.card-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-lg);
  color: white;
}

.overview-card.cpu .card-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-card.memory .card-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.overview-card.disk .card-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.overview-card.network .card-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-title {
  font-size: var(--font-size-md);
  font-weight: 500;
  color: var(--text-secondary);
}

.metric-value {
  font-size: var(--font-size-2xl);
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
}

.network-details {
  display: flex;
  justify-content: space-between;
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin-top: var(--spacing-sm);
}

.performance-charts {
  margin-bottom: var(--spacing-2xl);
}

.chart-card {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-md);
}

.chart-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.chart-card .card-header h2 {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.chart-container {
  height: 300px;
  width: 100%;
}

.services-section,
.logs-section,
.alerts-section {
  margin-bottom: var(--spacing-2xl);
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-lg);
}

.service-item {
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  border-left: 4px solid var(--success-color);
  transition: all var(--transition-fast);
}

.service-item.service-warning {
  border-left-color: var(--warning-color);
}

.service-item.service-error {
  border-left-color: var(--error-color);
}

.service-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.service-status {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.service-status .status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.service-status .status-dot.healthy {
  background: var(--success-color);
}

.service-status .status-dot.warning {
  background: var(--warning-color);
}

.service-status .status-dot.error {
  background: var(--error-color);
}

.service-name {
  font-weight: 500;
  color: var(--text-primary);
}

.service-details {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.detail-item {
  display: flex;
  justify-content: space-between;
  font-size: var(--font-size-sm);
}

.detail-label {
  color: var(--text-secondary);
}

.detail-value {
  color: var(--text-primary);
  font-weight: 500;
}

.log-filters {
  display: flex;
  gap: var(--spacing-md);
}

.logs-container {
  max-height: 400px;
  overflow-y: auto;
  margin-bottom: var(--spacing-lg);
}

.log-item {
  display: grid;
  grid-template-columns: 120px 60px 1fr 120px;
  gap: var(--spacing-md);
  padding: var(--spacing-md);
  border-bottom: 1px solid var(--border-light);
  font-size: var(--font-size-sm);
}

.log-item.log-error {
  background: rgba(245, 108, 108, 0.1);
}

.log-item.log-warning {
  background: rgba(230, 162, 60, 0.1);
}

.log-time {
  color: var(--text-secondary);
}

.log-level {
  font-weight: 500;
}

.log-item.log-error .log-level {
  color: var(--error-color);
}

.log-item.log-warning .log-level {
  color: var(--warning-color);
}

.log-item.log-info .log-level {
  color: var(--info-color);
}

.log-message {
  color: var(--text-primary);
}

.log-source {
  color: var(--text-secondary);
  text-align: right;
}

.logs-pagination {
  display: flex;
  justify-content: center;
}

.alerts-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.alert-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.alert-info {
  flex: 1;
}

.alert-name {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.alert-condition {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.alert-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.danger-btn {
  color: var(--error-color);
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

@media (max-width: 768px) {
  .system-monitor {
    padding: var(--spacing-lg);
  }
  
  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: flex-start;
  }
  
  .system-overview {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .services-grid {
    grid-template-columns: 1fr;
  }
  
  .log-item {
    grid-template-columns: 1fr;
    gap: var(--spacing-sm);
  }
  
  .alert-item {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>

