<template>
  <div class="learning-records">
    <div class="page-header">
      <h2>学习记录</h2>
      <p>查看您的学习进度和成果</p>
    </div>

    <!-- 学习统计卡片 -->
    <div class="stats-cards">
      <div class="stats-card">
        <div class="stats-icon">📚</div>
        <div class="stats-content">
          <div class="stats-number">{{ stats.totalCourses || 0 }}</div>
          <div class="stats-label">已学课程</div>
        </div>
      </div>
      <div class="stats-card">
        <div class="stats-icon">✅</div>
        <div class="stats-content">
          <div class="stats-number">{{ stats.completedCourses || 0 }}</div>
          <div class="stats-label">已完成</div>
        </div>
      </div>
      <div class="stats-card">
        <div class="stats-icon">⏱️</div>
        <div class="stats-content">
          <div class="stats-number">{{ stats.totalStudyTime || 1}}</div>
          <div class="stats-label">学习时长(小时)</div>
        </div>
      </div>
      <div class="stats-card">
        <div class="stats-icon">🎯</div>
        <div class="stats-content">
          <div class="stats-number">{{ stats.averageProgress || 50 }}%</div>
          <div class="stats-label">平均进度</div>
        </div>
      </div>
    </div>

    <!-- 学习记录列表 -->
    <div class="records-section">
      <div class="section-header">
        <h3>学习记录</h3>
        <div class="search-bar">
          <el-input
            v-model="searchText"
            placeholder="搜索课程..."
            prefix-icon="el-icon-search"
            @input="handleSearch"
          />
        </div>
      </div>

      <div v-if="loading" class="loading-wrapper">
        <el-loading text="加载中..." />
      </div>

      <div v-else-if="records.length === 0" class="empty-state">
        <div class="empty-icon">📖</div>
        <h3>暂无学习记录</h3>
        <p>开始学习您的第一门课程吧！</p>
        <el-button type="primary" @click="goToCourses">浏览课程</el-button>
      </div>

      <div v-else class="records-list">
        <div
          v-for="record in filteredRecords"
          :key="record.id"
          class="record-card"
        >
          <div class="record-info">
            <div class="course-cover">
              <img 
                :src="record.courseImage" 
                :alt="record.courseName"
                @error="handleImageError"
              />
            </div>
            <div class="course-details">
              <h4>{{ record.courseName || '未知课程' }}</h4>
              <p class="instructor">讲师：{{ record.instructor || '未知讲师' }}</p>
              <p class="enrollment-time">报名时间：{{ formatDate(record.enrollmentTime) }}</p>
            </div>
          </div>
          
          <div class="progress-section">
            <div class="progress-info">
              <span class="progress-text">学习进度</span>
              <span class="progress-percentage">{{ record.progress || 0 }}%</span>
              <el-tag 
                :type="getStatusType(record.status)" 
                size="small" 
                class="status-tag"
              >
                {{ getStatusText(record.status) }}
              </el-tag>
            </div>
            <el-progress 
              :percentage="record.progress || 0" 
              :color="getProgressColor(record.progress || 0)"
            />
            <div class="progress-details">
              <span>学习时长：{{ formatDuration(record.studyDuration || 0) }}</span>
              <span v-if="record.completionTime">
                完成时间：{{ formatDate(record.completionTime) }}
              </span>
            </div>
          </div>

          <div class="actions">
            <el-button 
              v-if="record.status === 0" 
              type="primary" 
              @click="startLearning(record)"
            >
              开始学习
            </el-button>
            <el-button 
              v-else-if="record.status === 1" 
              type="primary" 
              @click="continueLearning(record)"
            >
              继续学习
            </el-button>
            <el-button 
              v-else-if="record.status === 2" 
              type="success" 
              @click="viewCourse(record)"
            >
              已完成
            </el-button>
            <el-button 
              v-else
              type="warning" 
              @click="continueLearning(record)"
            >
              继续学习
            </el-button>
            <el-button 
              type="text" 
              @click="viewDetails(record)"
            >
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { getLearningRecords, getLearningStats } from '@/api/learning'
import { courseApi } from '@/api/course'

export default {
  name: 'LearningRecords',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    const loading = ref(false)
    const searchText = ref('')
    const records = ref([])
    const stats = reactive({
      totalCourses: 0,
      completedCourses: 0,
      totalStudyTime: 0,
      averageProgress: 0
    })

    // 计算过滤后的记录
    const filteredRecords = computed(() => {
      if (!searchText.value.trim()) {
        return records.value
      }
      return records.value.filter(record => 
        record.courseName?.toLowerCase().includes(searchText.value.toLowerCase()) ||
        record.instructor?.toLowerCase().includes(searchText.value.toLowerCase())
      )
    })

    // 获取学习记录
    const fetchLearningRecords = async () => {
      try {
        loading.value = true
        const userId = userStore.userId
        
        console.log("当前用户ID:", userId)
        
        // 确保用户ID存在
        if (!userId) {
          ElMessage.warning('请先登录')
          records.value = []
          return
        }
        
        const response = await getLearningRecords({
          userId: userId,
          current: 1,
          size: 100
        })
        
        console.log("学习记录API返回:", response)
        
        if (response.code === 200) {
          records.value = response.data?.records || response.data || []
          console.log("获取到学习记录:", records.value.length)
          
          // 如果学习记录中缺少课程信息，尝试补充
          if (records.value.length > 0) {
            for (const record of records.value) {
              if (!record.courseName && record.courseId) {
                try {
                  const courseRes = await courseApi.getCourseById(record.courseId)
                  if (courseRes.code === 200) {
                    record.courseName = courseRes.data.courseName
                    record.courseImage = courseRes.data.coverImage
                    record.instructor = courseRes.data.instructor
                  }
                } catch (error) {
                  console.warn(`无法获取课程信息(ID=${record.courseId}):`, error)
                }
              }
            }
          }
        } else {
          console.warn('获取学习记录失败:', response.message)
          ElMessage.warning(`获取学习记录失败: ${response.message || '未知错误'}`)
          records.value = []
        }
      } catch (error) {
        console.error('获取学习记录失败:', error)
        ElMessage.warning('获取学习记录失败，请稍后重试')
        records.value = []
      } finally {
        loading.value = false
      }
    }

    // 获取学习统计
    const fetchLearningStats = async () => {
      try {
        const userId = userStore.getUserId
        const response = await getLearningStats(userId)
        
        if (response.code === 200) {
          Object.assign(stats, response.data)
        } else {
          // 计算统计数据
          calculateStats()
        }
      } catch (error) {
        console.error('获取学习统计失败:', error)
        calculateStats()
      }
    }

    // 计算统计数据
    const calculateStats = () => {
      const totalCourses = records.value.length
      const completedCourses = records.value.filter(r => (r.progress || 0) >= 100).length
      const totalMinutes = records.value.reduce((sum, r) => sum + (r.studyDuration || 0), 0)
      const totalStudyTime = Math.round(totalMinutes / 60 * 10) / 10
      const averageProgress = totalCourses > 0 
        ? Math.round(records.value.reduce((sum, r) => sum + (r.progress || 0), 0) / totalCourses)
        : 0

      Object.assign(stats, {
        totalCourses,
        completedCourses,
        totalStudyTime,
        averageProgress
      })
    }

    // 处理搜索
    const handleSearch = () => {
      // 搜索逻辑已在computed中处理
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return '暂无'
      return new Date(date).toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    // 格式化时长
    const formatDuration = (minutes) => {
      if (!minutes) return '0小时'
      const hours = Math.floor(minutes / 60)
      const remainingMinutes = minutes % 60
      return `${hours}小时${remainingMinutes}分钟`
    }

    // 获取进度条颜色
    const getProgressColor = (progress) => {
      if (progress >= 100) return '#67C23A'
      if (progress >= 60) return '#409EFF'
      return '#E6A23C'
    }

    // 获取状态标签类型
    const getStatusType = (status) => {
      switch (status) {
        case 0: return 'info'    // 未开始
        case 1: return 'primary' // 学习中
        case 2: return 'success' // 已完成
        case 3: return 'warning' // 已暂停
        default: return 'info'
      }
    }

    // 获取状态文本
    const getStatusText = (status) => {
      switch (status) {
        case 0: return '未开始'
        case 1: return '学习中'
        case 2: return '已完成'
        case 3: return '已暂停'
        default: return '未知'
      }
    }

    // 处理图片加载错误
    const handleImageError = (e) => {
      e.target.src = '/default-course.png'
    }

    // 开始学习
    const startLearning = (record) => {
      router.push(`/dashboard/course-detail/${record.courseId}?action=start`)
    }

    // 继续学习
    const continueLearning = (record) => {
      // 如果有最后学习的章节，直接跳转到该章节
      if (record.lastChapterId) {
        router.push(`/dashboard/course-detail/${record.courseId}?chapterId=${record.lastChapterId}&action=resume`)
      } else {
        // 否则直接打开课程详情页
        router.push(`/dashboard/course-detail/${record.courseId}?action=start`)
      }
    }

    // 查看课程
    const viewCourse = (record) => {
      router.push(`/dashboard/course-detail/${record.courseId}`)
    }

    // 查看详情
    const viewDetails = (record) => {
      // 使用命名路由方式跳转，确保正确匹配路由
      router.push({
        name: 'LearningDetail',
        params: { courseId: record.courseId },
        query: { recordId: record.id }
      })
    }

    // 跳转到课程列表
    const goToCourses = () => {
      router.push('/dashboard/course-management')
    }

    onMounted(() => {
      fetchLearningRecords()
      fetchLearningStats()
    })

    return {
      loading,
      searchText,
      records,
      stats,
      filteredRecords,
      formatDate,
      formatDuration,
      getProgressColor,
      getStatusType,
      getStatusText,
      handleImageError,
      startLearning,
      continueLearning,
      viewCourse,
      viewDetails,
      goToCourses
    }
  }
}
</script>

<style scoped>
.learning-records {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 30px;
  text-align: center;
}

.page-header h2 {
  color: #333;
  margin-bottom: 8px;
}

.page-header p {
  color: #666;
  font-size: 14px;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stats-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 15px;
}

.stats-icon {
  font-size: 32px;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  border-radius: 50%;
}

.stats-number {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stats-label {
  color: #666;
  font-size: 14px;
}

.records-section {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.section-header {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-header h3 {
  margin: 0;
  color: #333;
}

.search-bar {
  width: 250px;
}

.loading-wrapper {
  padding: 40px;
  text-align: center;
}

.empty-state {
  padding: 60px 20px;
  text-align: center;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state h3 {
  color: #333;
  margin-bottom: 8px;
}

.empty-state p {
  color: #666;
  margin-bottom: 20px;
}

.records-list {
  padding: 0;
}

.record-card {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  gap: 20px;
}

.record-card:last-child {
  border-bottom: none;
}

.record-info {
  display: flex;
  align-items: center;
  gap: 15px;
  flex: 1;
  min-width: 0;
}

.course-cover {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.course-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-details h4 {
  margin: 0 0 4px 0;
  color: #333;
  font-size: 16px;
}

.course-details p {
  margin: 2px 0;
  color: #666;
  font-size: 12px;
}

.progress-section {
  flex: 1;
  max-width: 300px;
}

.progress-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.status-tag {
  margin-left: auto;
}

.progress-text {
  color: #666;
  font-size: 14px;
}

.progress-percentage {
  color: #333;
  font-weight: bold;
  font-size: 14px;
}

.progress-details {
  display: flex;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}

.actions {
  display: flex;
  gap: 10px;
  flex-shrink: 0;
}

@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .section-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }
  
  .search-bar {
    width: 100%;
  }
  
  .record-card {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }
  
  .progress-section {
    max-width: none;
  }
  
  .actions {
    justify-content: center;
  }
}
</style>

