<template>
  <div class="learning-detail">
    <div class="page-header">
      <el-button icon="Back" @click="goBack">返回</el-button>
      <h2>学习详情</h2>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>

    <div v-else class="detail-container">
      <!-- 课程基本信息 -->
      <div class="course-info card">
        <div class="card-header">
          <h3>课程信息</h3>
        </div>
        <div class="card-body">
          <div class="course-header">
            <div class="course-image">
              <el-image 
                :src="course.coverImage || '/default-course.png'" 
                fit="cover"
                @error="handleImageError"
              />
            </div>
            <div class="course-meta">
              <h4 class="course-title">{{ course.courseName || '未知课程' }}</h4>
              <div class="meta-items">
                <div class="meta-item">
                  <el-icon><User /></el-icon>
                  <span>讲师：{{ course.instructor || '-' }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Clock /></el-icon>
                  <span>课程时长：{{ formatDuration(course.duration) }}</span>
                </div>
                <div class="meta-item">
                  <el-icon><Calendar /></el-icon>
                  <span>发布日期：{{ formatDate(course.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 学习进度展示 -->
          <div class="learning-progress">
            <div class="progress-header">
              <span>学习进度</span>
              <span class="progress-percentage">{{ learningRecord.progress || 0 }}%</span>
              <el-tag 
                :type="getStatusType(learningRecord.status)" 
                size="small"
              >
                {{ getStatusText(learningRecord.status) }}
              </el-tag>
            </div>
            <el-progress 
              :percentage="learningRecord.progress || 0" 
              :color="getProgressColor(learningRecord.progress)" 
            />
            <div class="progress-detail">
              <div class="detail-item">
                <span class="label">首次学习：</span>
                <span class="value">{{ formatDate(learningRecord.createTime) }}</span>
              </div>
              <div class="detail-item">
                <span class="label">最近学习：</span>
                <span class="value">{{ formatDate(learningRecord.updateTime) }}</span>
              </div>
              <div class="detail-item">
                <span class="label">学习时长：</span>
                <span class="value">{{ formatDuration(learningRecord.studyDuration || 0) }}</span>
              </div>
            </div>
          </div>

          <div class="action-buttons">
            <el-button type="primary" @click="continueLearning">
              继续学习
            </el-button>
          </div>
        </div>
      </div>

      <!-- 章节学习详情 -->
      <div class="chapter-progress card">
        <div class="card-header">
          <h3>章节学习详情</h3>
        </div>
        <div class="card-body">
          <div v-if="chapters.length === 0" class="no-data">
            暂无章节信息
          </div>
          <div v-else class="chapter-list">
            <div v-for="(chapter, index) in chapters" :key="chapter.id" class="chapter-item">
              <div class="chapter-info">
                <div class="chapter-number">{{ index + 1 }}</div>
                <div class="chapter-content">
                  <div class="chapter-name">{{ chapter.chapterName }}</div>
                  <div class="chapter-duration">时长：{{ formatDuration(chapter.duration || 0) }}</div>
                </div>
              </div>
              <div class="chapter-status">
                <el-progress 
                  type="circle" 
                  :width="40"
                  :stroke-width="6"
                  :percentage="getChapterProgress(chapter.id)" 
                  :color="getProgressColor(getChapterProgress(chapter.id))"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 学习笔记和评价 -->
      <div class="notes-reviews card">
        <div class="card-header">
          <h3>学习笔记与评价</h3>
        </div>
        <div class="card-body">
          <div class="notes-section">
            <h4>学习笔记</h4>
            <div class="no-data">
              您还没有添加任何学习笔记
            </div>
            <el-button size="small" type="primary" plain>添加笔记</el-button>
          </div>

          <el-divider />

          <div class="review-section">
            <h4>课程评价</h4>
            <div class="no-data">
              您还没有评价此课程
            </div>
            <div class="rating-input">
              <el-rate v-model="rating" allow-half show-score />
              <el-button size="small" type="primary" plain>提交评价</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Clock, Calendar } from '@element-plus/icons-vue'
import { courseApi } from '@/api/course'
import { getUserCourseRecord } from '@/api/learning'
import { useUserStore } from '@/store/user'

export default {
  name: 'LearningDetail',
  components: {
    User,
    Clock,
    Calendar
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const userStore = useUserStore()
    
    // 数据状态
    const loading = ref(true)
    const courseId = ref(parseInt(route.params.courseId) || 0)
    const recordId = ref(route.query.recordId || 0)
    const course = reactive({})
    const learningRecord = reactive({})
    const chapters = ref([])
    const chapterProgress = ref({}) // 存储章节进度，格式: {chapterId: progress}
    const rating = ref(0)
    
    // 获取数据
    const fetchData = async () => {
      loading.value = true
      try {
        // 获取课程信息
        const courseRes = await courseApi.getCourseById(courseId.value)
        if (courseRes.code === 200) {
          Object.assign(course, courseRes.data)
          
          // 获取章节列表
          await fetchChapters()
        } else {
          ElMessage.error('获取课程信息失败')
        }
        
        // 获取学习记录
        await fetchLearningRecord()
        
      } catch (error) {
        console.error('获取学习详情失败:', error)
        ElMessage.error('获取学习详情失败')
      } finally {
        loading.value = false
      }
    }
    
    // 获取章节列表
    const fetchChapters = async () => {
      try {
        const res = await courseApi.getCourseChapters(courseId.value)
        if (res.code === 200) {
          chapters.value = res.data || []
          
          // 模拟章节进度数据（实际应该从后端获取）
          chapters.value.forEach(chapter => {
            chapterProgress.value[chapter.id] = Math.floor(Math.random() * 100)
          })
        }
      } catch (error) {
        console.error('获取章节列表失败:', error)
      }
    }
    
    // 获取学习记录
    const fetchLearningRecord = async () => {
      try {
        const userId = userStore.userId || 1
        const res = await getUserCourseRecord(userId, courseId.value)
        if (res.code === 200 && res.data) {
          Object.assign(learningRecord, res.data)
        } else {
          // 没有找到记录，创建默认值
          Object.assign(learningRecord, {
            userId: userId,
            courseId: courseId.value,
            progress: 0,
            status: 0,
            studyDuration: 0
          })
        }
      } catch (error) {
        console.error('获取学习记录失败:', error)
      }
    }
    
    // 获取章节进度
    const getChapterProgress = (chapterId) => {
      return chapterProgress.value[chapterId] || 0
    }
    
    // 格式化日期
    const formatDate = (date) => {
      if (!date) return '暂无数据'
      const d = new Date(date)
      return d.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
    
    // 格式化时长
    const formatDuration = (minutes) => {
      if (!minutes) return '0分钟'
      if (minutes < 60) return `${minutes}分钟`
      
      const hours = Math.floor(minutes / 60)
      const mins = minutes % 60
      return hours > 0 ? `${hours}小时${mins > 0 ? `${mins}分钟` : ''}` : `${mins}分钟`
    }
    
    // 获取状态类型
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
    
    // 获取进度条颜色
    const getProgressColor = (progress) => {
      if (progress >= 100) return '#67C23A'
      if (progress >= 60) return '#409EFF'
      return '#E6A23C'
    }
    
    // 处理图片加载错误
    const handleImageError = (e) => {
      e.target.src = '/default-course.png'
    }
    
    // 继续学习
    const continueLearning = () => {
      // 如果有最后学习的章节，直接跳转到该章节
      if (learningRecord.lastChapterId) {
        router.push(`/dashboard/course-detail/${courseId.value}?chapterId=${learningRecord.lastChapterId}&action=resume`)
      } else {
        // 否则直接打开课程详情页
        router.push(`/dashboard/course-detail/${courseId.value}?action=start`)
      }
    }
    
    // 返回上一页
    const goBack = () => {
      router.back()
    }
    
    // 监听路由参数变化
    watch(
      () => route.params.courseId,
      (newId) => {
        if (newId) {
          courseId.value = parseInt(newId)
          fetchData()
        }
      }
    )
    
    onMounted(() => {
      fetchData()
    })
    
    return {
      loading,
      course,
      learningRecord,
      chapters,
      rating,
      formatDate,
      formatDuration,
      getStatusType,
      getStatusText,
      getProgressColor,
      getChapterProgress,
      handleImageError,
      continueLearning,
      goBack
    }
  }
}
</script>

<style scoped>
.learning-detail {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 15px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}

.loading-container {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.detail-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.card-body {
  padding: 20px;
}

.course-header {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.course-image {
  width: 120px;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-title {
  margin: 0 0 10px 0;
  font-size: 18px;
  color: #303133;
}

.meta-items {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #606266;
}

.learning-progress {
  margin-bottom: 20px;
}

.progress-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.progress-header span {
  margin-right: 10px;
  color: #606266;
}

.progress-percentage {
  font-weight: bold;
  color: #303133;
}

.progress-detail {
  margin-top: 15px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.detail-item {
  font-size: 14px;
}

.detail-item .label {
  color: #909399;
}

.detail-item .value {
  color: #606266;
}

.action-buttons {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

.chapter-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chapter-item {
  padding: 15px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.chapter-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.chapter-number {
  width: 30px;
  height: 30px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.chapter-name {
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.chapter-duration {
  font-size: 14px;
  color: #909399;
}

.notes-section,
.review-section {
  margin-bottom: 20px;
}

.notes-section h4,
.review-section h4 {
  margin: 0 0 15px 0;
  font-size: 16px;
  color: #303133;
}

.no-data {
  padding: 20px 0;
  text-align: center;
  color: #909399;
}

.rating-input {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 15px;
}

@media (max-width: 768px) {
  .course-header {
    flex-direction: column;
    align-items: center;
  }
  
  .course-image {
    width: 100%;
    max-width: 220px;
    height: 120px;
  }
  
  .meta-items {
    flex-direction: column;
  }
  
  .progress-detail {
    grid-template-columns: 1fr;
  }
  
  .chapter-status {
    margin-left: auto;
  }
}
</style>
