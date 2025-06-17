<template>
  <div class="learning-records">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-notebook-1"></i>
          学习记录
        </h1>
        <p class="page-description">查看和管理学员的学习进度和记录</p>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-card class="search-card">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="学员姓名">
            <el-input
              v-model="searchForm.studentName"
              placeholder="请输入学员姓名"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="课程名称">
            <el-input
              v-model="searchForm.courseName"
              placeholder="请输入课程名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="学习状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="未开始" :value="0" />
              <el-option label="学习中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已过期" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :loading="loading">
              <i class="el-icon-search"></i>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <i class="el-icon-refresh"></i>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon total">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalStudents }}</div>
                <div class="stat-label">总学员数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon learning">
                <i class="el-icon-reading"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.learningStudents }}</div>
                <div class="stat-label">学习中</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon completed">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.completedStudents }}</div>
                <div class="stat-label">已完成</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon rate">
                <i class="el-icon-trophy"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.completionRate }}%</div>
                <div class="stat-label">完成率</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <div class="action-buttons">
        <el-button type="success" @click="handleExport">
          <i class="el-icon-download"></i>
          导出记录
        </el-button>
        <el-button type="info" @click="handleAnalysis">
          <i class="el-icon-data-analysis"></i>
          学习分析
        </el-button>
        <el-button type="warning" @click="handleReminder">
          <i class="el-icon-bell"></i>
          学习提醒
        </el-button>
      </div>
    </div>

    <!-- 学习记录列表 -->
    <div class="table-section">
      <el-card class="table-card">
        <el-table
          :data="recordList"
          v-loading="loading"
          stripe
          style="width: 100%"
        >
          <el-table-column label="学员信息" min-width="200">
            <template #default="{ row }">
              <div class="student-info">
                <el-avatar :src="row.studentAvatar" :size="40">
                  {{ row.studentName.charAt(0) }}
                </el-avatar>
                <div class="student-details">
                  <div class="student-name">{{ row.studentName }}</div>
                  <div class="student-dept">{{ row.deptName }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="课程信息" min-width="250">
            <template #default="{ row }">
              <div class="course-info">
                <div class="course-title">{{ row.courseName }}</div>
                <div class="course-meta">
                  <el-tag size="small" :type="getDifficultyType(row.difficulty)">
                    {{ getDifficultyText(row.difficulty) }}
                  </el-tag>
                  <span class="course-duration">{{ row.courseDuration }}分钟</span>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="学习进度" width="200">
            <template #default="{ row }">
              <div class="progress-info">
                <el-progress
                  :percentage="row.progress"
                  :color="getProgressColor(row.progress)"
                  :stroke-width="8"
                />
                <div class="progress-text">
                  {{ row.completedDuration }}/{{ row.courseDuration }}分钟
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="学习状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="考试成绩" width="100">
            <template #default="{ row }">
              <span v-if="row.score !== null" :class="getScoreClass(row.score)">
                {{ row.score }}分
              </span>
              <span v-else class="no-score">未考试</span>
            </template>
          </el-table-column>
          <el-table-column prop="studyTime" label="学习时长" width="120">
            <template #default="{ row }">
              {{ formatDuration(row.studyTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.startTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="lastStudyTime" label="最后学习" width="160">
            <template #default="{ row }">
              {{ row.lastStudyTime ? formatDate(row.lastStudyTime) : '-' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleViewDetail(row)">
                <i class="el-icon-view"></i>
                详情
              </el-button>
              <el-button size="small" type="warning" @click="handleViewProgress(row)">
                <i class="el-icon-data-line"></i>
                进度
              </el-button>
              <el-dropdown @command="(command) => handleDropdownCommand(command, row)">
                <el-button size="small" type="info">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :command="`reset-${row.id}`">
                      重置进度
                    </el-dropdown-item>
                    <el-dropdown-item :command="`extend-${row.id}`">
                      延长期限
                    </el-dropdown-item>
                    <el-dropdown-item :command="`remind-${row.id}`">
                      发送提醒
                    </el-dropdown-item>
                    <el-dropdown-item :command="`certificate-${row.id}`" v-if="row.status === 2">
                      生成证书
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 学习详情对话框 -->
    <el-dialog
      title="学习详情"
      v-model="detailDialogVisible"
      width="800px"
    >
      <div v-if="currentRecord" class="record-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="学员姓名">{{ currentRecord.studentName }}</el-descriptions-item>
          <el-descriptions-item label="所属部门">{{ currentRecord.deptName }}</el-descriptions-item>
          <el-descriptions-item label="课程名称">{{ currentRecord.courseName }}</el-descriptions-item>
          <el-descriptions-item label="课程难度">
            <el-tag :type="getDifficultyType(currentRecord.difficulty)">
              {{ getDifficultyText(currentRecord.difficulty) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="学习进度">
            <el-progress :percentage="currentRecord.progress" />
          </el-descriptions-item>
          <el-descriptions-item label="学习状态">
            <el-tag :type="getStatusType(currentRecord.status)">
              {{ getStatusText(currentRecord.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="考试成绩">
            <span v-if="currentRecord.score !== null" :class="getScoreClass(currentRecord.score)">
              {{ currentRecord.score }}分
            </span>
            <span v-else>未考试</span>
          </el-descriptions-item>
          <el-descriptions-item label="学习时长">{{ formatDuration(currentRecord.studyTime) }}</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ formatDate(currentRecord.startTime) }}</el-descriptions-item>
          <el-descriptions-item label="最后学习">
            {{ currentRecord.lastStudyTime ? formatDate(currentRecord.lastStudyTime) : '从未学习' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="study-chapters" v-if="currentRecord.chapters">
          <h3>章节学习情况</h3>
          <el-table :data="currentRecord.chapters" stripe>
            <el-table-column prop="title" label="章节名称" />
            <el-table-column label="完成状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.completed ? 'success' : 'info'">
                  {{ row.completed ? '已完成' : '未完成' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="duration" label="学习时长" width="120">
              <template #default="{ row }">
                {{ formatDuration(row.studyTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="lastStudyTime" label="最后学习" width="160">
              <template #default="{ row }">
                {{ row.lastStudyTime ? formatDate(row.lastStudyTime) : '-' }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>

    <!-- 学习进度对话框 -->
    <el-dialog
      title="学习进度分析"
      v-model="progressDialogVisible"
      width="900px"
    >
      <div v-if="currentRecord" class="progress-analysis">
        <div class="progress-chart">
          <h3>学习进度趋势</h3>
          <div class="chart-placeholder">
            <el-empty description="学习进度图表（需要集成图表库）" />
          </div>
        </div>
        
        <div class="progress-stats">
          <h3>学习统计</h3>
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-title">总学习时长</div>
                <div class="stat-value">{{ formatDuration(currentRecord.studyTime) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-title">平均每日学习</div>
                <div class="stat-value">{{ formatDuration(currentRecord.avgDailyTime) }}</div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="stat-item">
                <div class="stat-title">学习天数</div>
                <div class="stat-value">{{ currentRecord.studyDays }}天</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'LearningRecords',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const recordList = ref([])
    const detailDialogVisible = ref(false)
    const progressDialogVisible = ref(false)
    const currentRecord = ref(null)

    // 搜索表单
    const searchForm = reactive({
      studentName: '',
      courseName: '',
      status: null,
      dateRange: []
    })

    // 分页信息
    const pagination = reactive({
      page: 1,
      size: 10,
      total: 0
    })

    // 统计数据
    const stats = reactive({
      totalStudents: 1250,
      learningStudents: 380,
      completedStudents: 720,
      completionRate: 76.5
    })

    // 模拟学习记录数据
    const mockRecords = [
      {
        id: 1,
        studentId: 1,
        studentName: '张三',
        studentAvatar: '',
        deptName: '技术部',
        courseId: 1,
        courseName: 'Vue.js 3.0 从入门到精通',
        courseDuration: 480,
        difficulty: 2,
        progress: 85,
        completedDuration: 408,
        status: 1,
        score: 92,
        studyTime: 450,
        avgDailyTime: 45,
        studyDays: 10,
        startTime: '2024-06-01 09:00:00',
        lastStudyTime: '2024-06-15 14:30:00',
        chapters: [
          { id: 1, title: '第一章：Vue.js 基础', completed: true, studyTime: 120, lastStudyTime: '2024-06-02 10:00:00' },
          { id: 2, title: '第二章：组件开发', completed: true, studyTime: 150, lastStudyTime: '2024-06-05 15:30:00' },
          { id: 3, title: '第三章：状态管理', completed: false, studyTime: 80, lastStudyTime: '2024-06-10 11:20:00' }
        ]
      },
      {
        id: 2,
        studentId: 2,
        studentName: '李四',
        studentAvatar: '',
        deptName: '市场部',
        courseId: 2,
        courseName: '项目管理实战训练营',
        courseDuration: 720,
        difficulty: 3,
        progress: 100,
        completedDuration: 720,
        status: 2,
        score: 88,
        studyTime: 750,
        avgDailyTime: 50,
        studyDays: 15,
        startTime: '2024-05-15 09:00:00',
        lastStudyTime: '2024-06-10 16:45:00',
        chapters: [
          { id: 1, title: '第一章：项目管理基础', completed: true, studyTime: 180, lastStudyTime: '2024-05-20 14:00:00' },
          { id: 2, title: '第二章：项目计划与执行', completed: true, studyTime: 240, lastStudyTime: '2024-05-28 16:30:00' },
          { id: 3, title: '第三章：风险管理', completed: true, studyTime: 200, lastStudyTime: '2024-06-05 10:15:00' }
        ]
      }
    ]

    // 获取难度类型
    const getDifficultyType = (difficulty) => {
      const types = { 1: 'success', 2: 'warning', 3: 'danger' }
      return types[difficulty] || 'info'
    }

    // 获取难度文本
    const getDifficultyText = (difficulty) => {
      const texts = { 1: '初级', 2: '中级', 3: '高级' }
      return texts[difficulty] || '未知'
    }

    // 获取状态类型
    const getStatusType = (status) => {
      const types = { 0: 'info', 1: 'primary', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    }

    // 获取状态文本
    const getStatusText = (status) => {
      const texts = { 0: '未开始', 1: '学习中', 2: '已完成', 3: '已过期' }
      return texts[status] || '未知'
    }

    // 获取进度颜色
    const getProgressColor = (progress) => {
      if (progress < 30) return '#f56c6c'
      if (progress < 70) return '#e6a23c'
      return '#67c23a'
    }

    // 获取成绩样式
    const getScoreClass = (score) => {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 60) return 'score-pass'
      return 'score-fail'
    }

    // 获取学习记录列表
    const getRecordList = async () => {
      loading.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 500))
        recordList.value = mockRecords
        pagination.total = mockRecords.length
      } catch (error) {
        ElMessage.error('获取学习记录失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.page = 1
      getRecordList()
    }

    // 重置搜索
    const handleReset = () => {
      Object.assign(searchForm, {
        studentName: '',
        courseName: '',
        status: null,
        dateRange: []
      })
      pagination.page = 1
      getRecordList()
    }

    // 导出记录
    const handleExport = () => {
      ElMessage.success('学习记录导出成功')
    }

    // 学习分析
    const handleAnalysis = () => {
      ElMessage.info('学习分析功能开发中...')
    }

    // 学习提醒
    const handleReminder = () => {
      ElMessage.success('学习提醒已发送')
    }

    // 查看详情
    const handleViewDetail = (row) => {
      currentRecord.value = row
      detailDialogVisible.value = true
    }

    // 查看进度
    const handleViewProgress = (row) => {
      currentRecord.value = row
      progressDialogVisible.value = true
    }

    // 下拉菜单命令处理
    const handleDropdownCommand = async (command, row) => {
      const [action, id] = command.split('-')
      
      try {
        switch (action) {
          case 'reset':
            await ElMessageBox.confirm(
              `确定要重置学员 "${row.studentName}" 的学习进度吗？`,
              '重置确认',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            ElMessage.success('学习进度重置成功')
            break
          case 'extend':
            ElMessage.success('学习期限延长成功')
            break
          case 'remind':
            ElMessage.success('学习提醒发送成功')
            break
          case 'certificate':
            ElMessage.success('学习证书生成成功')
            break
        }
        getRecordList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败')
        }
      }
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.page = 1
      getRecordList()
    }

    // 当前页变化
    const handleCurrentChange = (page) => {
      pagination.page = page
      getRecordList()
    }

    // 格式化时长
    const formatDuration = (minutes) => {
      if (!minutes) return '0分钟'
      const hours = Math.floor(minutes / 60)
      const mins = minutes % 60
      if (hours > 0) {
        return `${hours}小时${mins}分钟`
      }
      return `${mins}分钟`
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    // 组件挂载时获取数据
    onMounted(() => {
      getRecordList()
    })

    return {
      loading,
      recordList,
      detailDialogVisible,
      progressDialogVisible,
      currentRecord,
      searchForm,
      pagination,
      stats,
      getDifficultyType,
      getDifficultyText,
      getStatusType,
      getStatusText,
      getProgressColor,
      getScoreClass,
      getRecordList,
      handleSearch,
      handleReset,
      handleExport,
      handleAnalysis,
      handleReminder,
      handleViewDetail,
      handleViewProgress,
      handleDropdownCommand,
      handleSizeChange,
      handleCurrentChange,
      formatDuration,
      formatDate
    }
  }
}
</script>

<style scoped>
.learning-records {
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

.search-section {
  margin-bottom: 20px;
}

.search-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.search-form {
  margin: 0;
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

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.learning {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.completed {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.rate {
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
}

.action-section {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.table-section {
  margin-bottom: 20px;
}

.table-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.student-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.student-details {
  flex: 1;
}

.student-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.student-dept {
  font-size: 12px;
  color: #909399;
}

.course-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.course-title {
  font-weight: 600;
  color: #303133;
}

.course-meta {
  display: flex;
  gap: 8px;
  align-items: center;
  font-size: 12px;
}

.course-duration {
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
  color: #909399;
}

.progress-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.progress-text {
  font-size: 12px;
  color: #909399;
  text-align: center;
}

.score-excellent {
  color: #67c23a;
  font-weight: 600;
}

.score-good {
  color: #409eff;
  font-weight: 600;
}

.score-pass {
  color: #e6a23c;
  font-weight: 600;
}

.score-fail {
  color: #f56c6c;
  font-weight: 600;
}

.no-score {
  color: #c0c4cc;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.record-detail {
  padding: 20px 0;
}

.study-chapters {
  margin-top: 30px;
}

.study-chapters h3 {
  margin: 0 0 16px 0;
  color: #303133;
}

.progress-analysis {
  padding: 20px 0;
}

.progress-chart {
  margin-bottom: 30px;
}

.progress-chart h3 {
  margin: 0 0 16px 0;
  color: #303133;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.progress-stats h3 {
  margin: 0 0 16px 0;
  color: #303133;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-item .stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #409eff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .learning-records {
    padding: 10px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .student-info {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>

