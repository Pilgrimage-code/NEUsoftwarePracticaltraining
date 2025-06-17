<template>
  <div class="statistics">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-data-analysis"></i>
          统计报表
        </h1>
        <p class="page-description">查看系统运营数据和统计分析</p>
      </div>
    </div>

    <!-- 时间筛选 -->
    <div class="filter-section">
      <el-card class="filter-card">
        <el-form :model="filterForm" :inline="true" class="filter-form">
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px"
              @change="handleDateChange"
            />
          </el-form-item>
          <el-form-item label="快捷选择">
            <el-button-group>
              <el-button @click="handleQuickSelect('today')" :type="quickActive === 'today' ? 'primary' : ''">今日</el-button>
              <el-button @click="handleQuickSelect('week')" :type="quickActive === 'week' ? 'primary' : ''">本周</el-button>
              <el-button @click="handleQuickSelect('month')" :type="quickActive === 'month' ? 'primary' : ''">本月</el-button>
              <el-button @click="handleQuickSelect('year')" :type="quickActive === 'year' ? 'primary' : ''">本年</el-button>
            </el-button-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleRefresh" :loading="loading">
              <i class="el-icon-refresh"></i>
              刷新数据
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 核心指标卡片 -->
    <div class="metrics-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon users">
                <i class="el-icon-user"></i>
              </div>
              <div class="metric-info">
                <div class="metric-value">{{ metrics.totalUsers }}</div>
                <div class="metric-label">总用户数</div>
                <div class="metric-change positive">
                  <i class="el-icon-top"></i>
                  +{{ metrics.userGrowth }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon meetings">
                <i class="el-icon-date"></i>
              </div>
              <div class="metric-info">
                <div class="metric-value">{{ metrics.totalMeetings }}</div>
                <div class="metric-label">总会议数</div>
                <div class="metric-change positive">
                  <i class="el-icon-top"></i>
                  +{{ metrics.meetingGrowth }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon courses">
                <i class="el-icon-reading"></i>
              </div>
              <div class="metric-info">
                <div class="metric-value">{{ metrics.totalCourses }}</div>
                <div class="metric-label">总课程数</div>
                <div class="metric-change positive">
                  <i class="el-icon-top"></i>
                  +{{ metrics.courseGrowth }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="metric-card">
            <div class="metric-content">
              <div class="metric-icon active">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="metric-info">
                <div class="metric-value">{{ metrics.activeRate }}%</div>
                <div class="metric-label">活跃率</div>
                <div class="metric-change negative">
                  <i class="el-icon-bottom"></i>
                  -{{ metrics.activeRateChange }}%
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 用户增长趋势 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>用户增长趋势</span>
                <el-button-group size="small">
                  <el-button @click="handleChartType('user', 'line')" :type="userChartType === 'line' ? 'primary' : ''">线图</el-button>
                  <el-button @click="handleChartType('user', 'bar')" :type="userChartType === 'bar' ? 'primary' : ''">柱图</el-button>
                </el-button-group>
              </div>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                <el-empty description="用户增长趋势图表（需要集成图表库）" />
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 会议统计 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>会议统计分析</span>
                <el-button-group size="small">
                  <el-button @click="handleChartType('meeting', 'pie')" :type="meetingChartType === 'pie' ? 'primary' : ''">饼图</el-button>
                  <el-button @click="handleChartType('meeting', 'doughnut')" :type="meetingChartType === 'doughnut' ? 'primary' : ''">环图</el-button>
                </el-button-group>
              </div>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                <el-empty description="会议统计图表（需要集成图表库）" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 学习完成率 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>学习完成率统计</span>
                <el-select v-model="learningPeriod" size="small" style="width: 100px;">
                  <el-option label="本周" value="week" />
                  <el-option label="本月" value="month" />
                  <el-option label="本季" value="quarter" />
                </el-select>
              </div>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                <el-empty description="学习完成率图表（需要集成图表库）" />
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 系统使用情况 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>系统使用情况</span>
                <el-button size="small" @click="handleExportChart">
                  <i class="el-icon-download"></i>
                  导出
                </el-button>
              </div>
            </template>
            <div class="chart-container">
              <div class="chart-placeholder">
                <el-empty description="系统使用情况图表（需要集成图表库）" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 详细数据表格 -->
    <div class="table-section">
      <el-card class="table-card">
        <template #header>
          <div class="table-header">
            <span>详细数据统计</span>
            <div class="table-actions">
              <el-button size="small" @click="handleExportData">
                <i class="el-icon-download"></i>
                导出数据
              </el-button>
              <el-button size="small" @click="handlePrintReport">
                <i class="el-icon-printer"></i>
                打印报表
              </el-button>
            </div>
          </div>
        </template>

        <el-tabs v-model="activeTableTab">
          <el-tab-pane label="用户统计" name="users">
            <el-table :data="userStats" stripe>
              <el-table-column prop="date" label="日期" width="120" />
              <el-table-column prop="newUsers" label="新增用户" width="100" />
              <el-table-column prop="activeUsers" label="活跃用户" width="100" />
              <el-table-column prop="totalUsers" label="累计用户" width="100" />
              <el-table-column prop="loginCount" label="登录次数" width="100" />
              <el-table-column prop="avgOnlineTime" label="平均在线时长" width="120" />
              <el-table-column label="增长率" width="100">
                <template #default="{ row }">
                  <span :class="row.growthRate >= 0 ? 'positive' : 'negative'">
                    {{ row.growthRate >= 0 ? '+' : '' }}{{ row.growthRate }}%
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="会议统计" name="meetings">
            <el-table :data="meetingStats" stripe>
              <el-table-column prop="date" label="日期" width="120" />
              <el-table-column prop="totalMeetings" label="总会议数" width="100" />
              <el-table-column prop="completedMeetings" label="已完成" width="100" />
              <el-table-column prop="cancelledMeetings" label="已取消" width="100" />
              <el-table-column prop="participants" label="参与人数" width="100" />
              <el-table-column prop="avgDuration" label="平均时长" width="120" />
              <el-table-column label="完成率" width="100">
                <template #default="{ row }">
                  <el-progress
                    :percentage="Math.round((row.completedMeetings / row.totalMeetings) * 100)"
                    :stroke-width="6"
                    :show-text="false"
                  />
                  <span style="margin-left: 8px;">
                    {{ Math.round((row.completedMeetings / row.totalMeetings) * 100) }}%
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="课程统计" name="courses">
            <el-table :data="courseStats" stripe>
              <el-table-column prop="date" label="日期" width="120" />
              <el-table-column prop="totalCourses" label="总课程数" width="100" />
              <el-table-column prop="publishedCourses" label="已发布" width="100" />
              <el-table-column prop="enrollments" label="报名人数" width="100" />
              <el-table-column prop="completions" label="完成人数" width="100" />
              <el-table-column prop="avgScore" label="平均分数" width="100" />
              <el-table-column label="完成率" width="100">
                <template #default="{ row }">
                  <el-progress
                    :percentage="Math.round((row.completions / row.enrollments) * 100)"
                    :stroke-width="6"
                    :show-text="false"
                  />
                  <span style="margin-left: 8px;">
                    {{ Math.round((row.completions / row.enrollments) * 100) }}%
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="系统统计" name="system">
            <el-table :data="systemStats" stripe>
              <el-table-column prop="date" label="日期" width="120" />
              <el-table-column prop="totalRequests" label="总请求数" width="120" />
              <el-table-column prop="successRequests" label="成功请求" width="120" />
              <el-table-column prop="errorRequests" label="错误请求" width="120" />
              <el-table-column prop="avgResponseTime" label="平均响应时间" width="140" />
              <el-table-column prop="peakConcurrency" label="峰值并发" width="120" />
              <el-table-column label="成功率" width="100">
                <template #default="{ row }">
                  <span :class="row.successRate >= 99 ? 'positive' : row.successRate >= 95 ? 'warning' : 'negative'">
                    {{ row.successRate }}%
                  </span>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'Statistics',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const quickActive = ref('month')
    const userChartType = ref('line')
    const meetingChartType = ref('pie')
    const learningPeriod = ref('month')
    const activeTableTab = ref('users')

    // 筛选表单
    const filterForm = reactive({
      dateRange: []
    })

    // 核心指标
    const metrics = reactive({
      totalUsers: 12580,
      userGrowth: 15.6,
      totalMeetings: 3420,
      meetingGrowth: 8.3,
      totalCourses: 856,
      courseGrowth: 12.1,
      activeRate: 78.5,
      activeRateChange: 2.3
    })

    // 模拟统计数据
    const userStats = ref([
      {
        date: '2024-06-15',
        newUsers: 125,
        activeUsers: 1580,
        totalUsers: 12580,
        loginCount: 3420,
        avgOnlineTime: '2小时15分',
        growthRate: 15.6
      },
      {
        date: '2024-06-14',
        newUsers: 98,
        activeUsers: 1456,
        totalUsers: 12455,
        loginCount: 3180,
        avgOnlineTime: '2小时8分',
        growthRate: 12.3
      },
      {
        date: '2024-06-13',
        newUsers: 156,
        activeUsers: 1623,
        totalUsers: 12357,
        loginCount: 3650,
        avgOnlineTime: '2小时22分',
        growthRate: 18.9
      }
    ])

    const meetingStats = ref([
      {
        date: '2024-06-15',
        totalMeetings: 45,
        completedMeetings: 38,
        cancelledMeetings: 7,
        participants: 520,
        avgDuration: '1小时30分'
      },
      {
        date: '2024-06-14',
        totalMeetings: 52,
        completedMeetings: 46,
        cancelledMeetings: 6,
        participants: 680,
        avgDuration: '1小时45分'
      },
      {
        date: '2024-06-13',
        totalMeetings: 38,
        completedMeetings: 35,
        cancelledMeetings: 3,
        participants: 450,
        avgDuration: '1小时20分'
      }
    ])

    const courseStats = ref([
      {
        date: '2024-06-15',
        totalCourses: 856,
        publishedCourses: 742,
        enrollments: 2580,
        completions: 1890,
        avgScore: 85.6
      },
      {
        date: '2024-06-14',
        totalCourses: 852,
        publishedCourses: 738,
        enrollments: 2520,
        completions: 1845,
        avgScore: 84.2
      },
      {
        date: '2024-06-13',
        totalCourses: 848,
        publishedCourses: 735,
        enrollments: 2480,
        completions: 1820,
        avgScore: 86.1
      }
    ])

    const systemStats = ref([
      {
        date: '2024-06-15',
        totalRequests: 125680,
        successRequests: 124520,
        errorRequests: 1160,
        avgResponseTime: '125ms',
        peakConcurrency: 580,
        successRate: 99.1
      },
      {
        date: '2024-06-14',
        totalRequests: 118950,
        successRequests: 117680,
        errorRequests: 1270,
        avgResponseTime: '132ms',
        peakConcurrency: 520,
        successRate: 98.9
      },
      {
        date: '2024-06-13',
        totalRequests: 132450,
        successRequests: 131180,
        errorRequests: 1270,
        avgResponseTime: '118ms',
        peakConcurrency: 650,
        successRate: 99.0
      }
    ])

    // 快捷时间选择
    const handleQuickSelect = (type) => {
      quickActive.value = type
      const now = new Date()
      let startDate, endDate = new Date()

      switch (type) {
        case 'today':
          startDate = new Date()
          break
        case 'week':
          startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
          break
        case 'month':
          startDate = new Date(now.getFullYear(), now.getMonth(), 1)
          break
        case 'year':
          startDate = new Date(now.getFullYear(), 0, 1)
          break
      }

      filterForm.dateRange = [startDate, endDate]
      handleRefresh()
    }

    // 日期变化
    const handleDateChange = () => {
      quickActive.value = ''
      handleRefresh()
    }

    // 刷新数据
    const handleRefresh = async () => {
      loading.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('数据刷新成功')
      } catch (error) {
        ElMessage.error('数据刷新失败')
      } finally {
        loading.value = false
      }
    }

    // 图表类型切换
    const handleChartType = (chart, type) => {
      if (chart === 'user') {
        userChartType.value = type
      } else if (chart === 'meeting') {
        meetingChartType.value = type
      }
      ElMessage.info(`切换到${type}图表`)
    }

    // 导出图表
    const handleExportChart = () => {
      ElMessage.success('图表导出成功')
    }

    // 导出数据
    const handleExportData = () => {
      ElMessage.success('数据导出成功')
    }

    // 打印报表
    const handlePrintReport = () => {
      ElMessage.success('报表打印成功')
    }

    // 组件挂载时初始化
    onMounted(() => {
      handleQuickSelect('month')
    })

    return {
      loading,
      quickActive,
      userChartType,
      meetingChartType,
      learningPeriod,
      activeTableTab,
      filterForm,
      metrics,
      userStats,
      meetingStats,
      courseStats,
      systemStats,
      handleQuickSelect,
      handleDateChange,
      handleRefresh,
      handleChartType,
      handleExportChart,
      handleExportData,
      handlePrintReport
    }
  }
}
</script>

<style scoped>
.statistics {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30px;
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.page-description {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.filter-form {
  margin: 0;
}

.metrics-section {
  margin-bottom: 20px;
}

.metric-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.metric-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.metric-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.metric-icon.users {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.metric-icon.meetings {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.metric-icon.courses {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.metric-icon.active {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.metric-info {
  flex: 1;
}

.metric-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.metric-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.metric-change {
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
}

.metric-change.positive {
  color: #67c23a;
}

.metric-change.negative {
  color: #f56c6c;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
}

.chart-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.table-section {
  margin-bottom: 20px;
}

.table-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-actions {
  display: flex;
  gap: 8px;
}

.positive {
  color: #67c23a;
  font-weight: 600;
}

.negative {
  color: #f56c6c;
  font-weight: 600;
}

.warning {
  color: #e6a23c;
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .metrics-section .el-col {
    margin-bottom: 20px;
  }
  
  .charts-section .el-col {
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .statistics {
    padding: 10px;
  }
  
  .filter-form {
    flex-direction: column;
  }
  
  .chart-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .table-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .table-actions {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>

