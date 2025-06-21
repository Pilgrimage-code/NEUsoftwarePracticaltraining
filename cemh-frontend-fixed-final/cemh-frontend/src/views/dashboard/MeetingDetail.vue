<template>
  <div class="meeting-detail modern-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-button 
            type="text" 
            @click="$router.go(-1)"
            class="back-btn"
          >
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
          <h1 class="page-title">会议详情</h1>
        </div>
        <div class="header-actions">
          <el-button 
            type="primary" 
            @click="editMeeting"
            class="modern-btn primary"
          >
            <el-icon><Edit /></el-icon>
            编辑会议
          </el-button>
          <el-dropdown @command="handleCommand">
            <el-button class="modern-btn secondary">
              更多操作
              <el-icon><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="copy">复制会议</el-dropdown-item>
                <el-dropdown-item command="export">导出详情</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除会议</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 会议基本信息 -->
    <div class="meeting-info-section">
      <div class="modern-card">
        <div class="card-header">
          <h2>基本信息</h2>
          <el-tag 
            :type="getStatusType(meetingInfo.status)" 
            class="status-tag"
          >
            {{ getStatusText(meetingInfo.status) }}
          </el-tag>
        </div>
        <div class="card-body">
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">会议主题</div>
              <div class="info-value">{{ meetingInfo.title }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">会议类型</div>
              <div class="info-value">{{ meetingInfo.type }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">开始时间</div>
              <div class="info-value">
                <el-icon><Clock /></el-icon>
                {{ formatDateTime(meetingInfo.startTime) }}
              </div>
            </div>
            <div class="info-item">
              <div class="info-label">结束时间</div>
              <div class="info-value">
                <el-icon><Clock /></el-icon>
                {{ formatDateTime(meetingInfo.endTime) }}
              </div>
            </div>
            <div class="info-item">
              <div class="info-label">会议地点</div>
              <div class="info-value">
                <el-icon><Location /></el-icon>
                {{ meetingInfo.location }}
              </div>
            </div>
            <div class="info-item">
              <div class="info-label">组织者</div>
              <div class="info-value">
                <el-icon><User /></el-icon>
                {{ meetingInfo.organizer }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 会议描述 -->
    <div class="meeting-description-section" v-if="meetingInfo.description">
      <div class="modern-card">
        <div class="card-header">
          <h2>会议描述</h2>
        </div>
        <div class="card-body">
          <div class="description-content" v-html="meetingInfo.description"></div>
        </div>
      </div>
    </div>

    <!-- 参会人员 -->
    <div class="participants-section">
      <div class="modern-card">
        <div class="card-header">
          <h2>参会人员</h2>
          <div class="participant-stats">
            <span class="stat-item">
              <el-icon><UserFilled /></el-icon>
              已确认: {{ confirmedCount }}
            </span>
            <span class="stat-item">
              <el-icon><Clock /></el-icon>
              待确认: {{ pendingCount }}
            </span>
          </div>
        </div>
        <div class="card-body">
          <div class="participants-list">
            <div 
              v-for="participant in participants" 
              :key="participant.id"
              class="participant-item"
            >
              <div class="participant-avatar">
                <el-avatar 
                  :src="participant.avatar" 
                  :size="40"
                >
                  {{ participant.name.charAt(0) }}
                </el-avatar>
              </div>
              <div class="participant-info">
                <div class="participant-name">{{ participant.name }}</div>
                <div class="participant-dept">{{ participant.department }}</div>
              </div>
              <div class="participant-status">
                <el-tag 
                  :type="getParticipantStatusType(participant.status)"
                  size="small"
                >
                  {{ getParticipantStatusText(participant.status) }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 会议附件 -->
    <div class="attachments-section" v-if="attachments.length > 0">
      <div class="modern-card">
        <div class="card-header">
          <h2>会议附件</h2>
          <span class="attachment-count">{{ attachments.length }} 个文件</span>
        </div>
        <div class="card-body">
          <div class="attachments-list">
            <div 
              v-for="attachment in attachments" 
              :key="attachment.id"
              class="attachment-item"
              @click="downloadAttachment(attachment)"
            >
              <div class="attachment-icon">
                <el-icon><Document /></el-icon>
              </div>
              <div class="attachment-info">
                <div class="attachment-name">{{ attachment.name }}</div>
                <div class="attachment-meta">
                  {{ formatFileSize(attachment.size) }} • 
                  {{ formatDateTime(attachment.uploadTime) }}
                </div>
              </div>
              <div class="attachment-actions">
                <el-button 
                  type="text" 
                  @click.stop="previewAttachment(attachment)"
                >
                  预览
                </el-button>
                <el-button 
                  type="text" 
                  @click.stop="downloadAttachment(attachment)"
                >
                  下载
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 会议记录 -->
    <div class="meeting-notes-section" v-if="meetingInfo.notes">
      <div class="modern-card">
        <div class="card-header">
          <h2>会议记录</h2>
          <el-button 
            type="text" 
            @click="editNotes"
            class="edit-notes-btn"
          >
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
        </div>
        <div class="card-body">
          <div class="notes-content" v-html="meetingInfo.notes"></div>
        </div>
      </div>
    </div>

    <!-- 操作历史 -->
    <div class="history-section">
      <div class="modern-card">
        <div class="card-header">
          <h2>操作历史</h2>
        </div>
        <div class="card-body">
          <div class="history-timeline">
            <div 
              v-for="(history, index) in operationHistory" 
              :key="index"
              class="timeline-item"
            >
              <div class="timeline-dot"></div>
              <div class="timeline-content">
                <div class="timeline-title">{{ history.action }}</div>
                <div class="timeline-meta">
                  {{ history.operator }} • {{ formatDateTime(history.time) }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()

// 响应式数据
const meetingInfo = ref({
  id: '',
  title: '',
  type: '',
  startTime: '',
  endTime: '',
  location: '',
  organizer: '',
  description: '',
  status: 1,
  notes: ''
})

const participants = ref([])
const attachments = ref([])
const operationHistory = ref([])

// 计算属性
const confirmedCount = computed(() => 
  participants.value.filter(p => p.status === 1).length
)

const pendingCount = computed(() => 
  participants.value.filter(p => p.status === 0).length
)

// 方法
const loadMeetingDetail = async () => {
  try {
    const meetingId = route.params.id
    // 这里应该调用API获取会议详情
    // const response = await getMeetingDetail(meetingId)
    
    // 模拟数据
    meetingInfo.value = {
      id: meetingId,
      title: '2024年第一季度业务回顾会议',
      type: '季度会议',
      startTime: '2024-06-20 14:00:00',
      endTime: '2024-06-20 16:00:00',
      location: '会议室A',
      organizer: '张三',
      description: '<p>本次会议将回顾第一季度的业务情况，分析存在的问题，制定下一步的工作计划。</p>',
      status: 1,
      notes: '<p>会议要点：<br/>1. Q1业绩达成率95%<br/>2. 客户满意度提升3%<br/>3. 下季度重点关注新产品推广</p>'
    }

    participants.value = [
      { id: 1, name: '李四', department: '销售部', status: 1, avatar: '' },
      { id: 2, name: '王五', department: '市场部', status: 1, avatar: '' },
      { id: 3, name: '赵六', department: '技术部', status: 0, avatar: '' }
    ]

    attachments.value = [
      { id: 1, name: 'Q1业务报告.pdf', size: 2048576, uploadTime: '2024-06-19 10:00:00' },
      { id: 2, name: '市场分析.xlsx', size: 1024000, uploadTime: '2024-06-19 11:30:00' }
    ]

    operationHistory.value = [
      { action: '创建会议', operator: '张三', time: '2024-06-18 09:00:00' },
      { action: '添加参会人员', operator: '张三', time: '2024-06-18 09:30:00' },
      { action: '上传附件', operator: '李四', time: '2024-06-19 10:00:00' }
    ]
  } catch (error) {
    ElMessage.error('加载会议详情失败')
  }
}

const editMeeting = () => {
  router.push(`/dashboard/meetings/edit/${meetingInfo.value.id}`)
}

const handleCommand = async (command) => {
  switch (command) {
    case 'copy':
      ElMessage.success('会议已复制')
      break
    case 'export':
      ElMessage.success('导出功能开发中')
      break
    case 'delete':
      try {
        await ElMessageBox.confirm('确定要删除这个会议吗？', '确认删除', {
          type: 'warning'
        })
        ElMessage.success('会议已删除')
        router.push('/dashboard/meetings')
      } catch {
        // 用户取消删除
      }
      break
  }
}

const editNotes = () => {
  ElMessage.info('编辑会议记录功能开发中')
}

const downloadAttachment = (attachment) => {
  ElMessage.success(`下载 ${attachment.name}`)
}

const previewAttachment = (attachment) => {
  ElMessage.info(`预览 ${attachment.name}`)
}

// 工具方法
const formatDateTime = (dateTime) => {
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm')
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const getStatusType = (status) => {
  const types = { 0: 'info', 1: 'success', 2: 'warning', 3: 'danger' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '待开始', 1: '进行中', 2: '已结束', 3: '已取消' }
  return texts[status] || '未知'
}

const getParticipantStatusType = (status) => {
  return status === 1 ? 'success' : 'warning'
}

const getParticipantStatusText = (status) => {
  return status === 1 ? '已确认' : '待确认'
}

onMounted(() => {
  loadMeetingDetail()
})
</script>

<style scoped>
.meeting-detail {
  padding: var(--spacing-2xl);
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: var(--spacing-2xl);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.back-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  color: var(--text-secondary);
  transition: color var(--transition-fast);
}

.back-btn:hover {
  color: var(--primary-color);
}

.page-title {
  font-size: var(--font-size-3xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.header-actions {
  display: flex;
  gap: var(--spacing-md);
}

.meeting-info-section,
.meeting-description-section,
.participants-section,
.attachments-section,
.meeting-notes-section,
.history-section {
  margin-bottom: var(--spacing-2xl);
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

.status-tag {
  font-weight: 500;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--spacing-xl);
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.info-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  font-weight: 500;
}

.info-value {
  font-size: var(--font-size-md);
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.description-content {
  line-height: 1.6;
  color: var(--text-primary);
}

.participant-stats {
  display: flex;
  gap: var(--spacing-lg);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.participants-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.participant-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.participant-item:hover {
  background: var(--bg-tertiary);
  transform: translateY(-1px);
}

.participant-info {
  flex: 1;
}

.participant-name {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.participant-dept {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.attachment-count {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.attachments-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.attachment-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.attachment-item:hover {
  background: var(--bg-tertiary);
  transform: translateY(-1px);
}

.attachment-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-color);
  color: white;
  border-radius: var(--radius-md);
}

.attachment-info {
  flex: 1;
}

.attachment-name {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.attachment-meta {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.attachment-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.edit-notes-btn {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.notes-content {
  line-height: 1.6;
  color: var(--text-primary);
}

.history-timeline {
  position: relative;
  padding-left: var(--spacing-2xl);
}

.history-timeline::before {
  content: '';
  position: absolute;
  left: 8px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: var(--border-normal);
}

.timeline-item {
  position: relative;
  margin-bottom: var(--spacing-xl);
}

.timeline-dot {
  position: absolute;
  left: -var(--spacing-2xl);
  top: 4px;
  width: 12px;
  height: 12px;
  background: var(--primary-color);
  border-radius: 50%;
  border: 2px solid var(--bg-primary);
}

.timeline-title {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.timeline-meta {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .meeting-detail {
    padding: var(--spacing-lg);
  }
  
  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: flex-start;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .participant-item,
  .attachment-item {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--spacing-md);
  }
  
  .participant-stats {
    flex-direction: column;
    gap: var(--spacing-sm);
  }
}
</style>

