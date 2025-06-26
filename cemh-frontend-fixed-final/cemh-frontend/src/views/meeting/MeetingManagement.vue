<template>
  <div class="meeting-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1>会议管理</h1>
        <p>管理和组织各类会议活动</p>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="createMeeting">
          <el-icon><Plus /></el-icon>
          创建会议
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="会议标题">
          <el-input
            v-model="searchForm.title"
            placeholder="请输入会议标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        
        <el-form-item label="会议类型">
          <el-select
            v-model="searchForm.type"
            placeholder="请选择会议类型"
            clearable
            style="width: 150px"
          >
            <el-option label="技术交流" :value="1" />
            <el-option label="产品发布" :value="2" />
            <el-option label="培训课程" :value="3" />
            <el-option label="其他" :value="4" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="会议状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择会议状态"
            clearable
            style="width: 150px"
          >
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
            <el-option label="已取消" :value="2" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="开始时间">
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
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 会议列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <h3>会议列表</h3>
          <span class="total-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button
              :type="viewMode === 'table' ? 'primary' : ''"
              @click="viewMode = 'table'"
            >
              <el-icon><List /></el-icon>
              列表视图
            </el-button>
            <el-button
              :type="viewMode === 'card' ? 'primary' : ''"
              @click="viewMode = 'card'"
            >
              <el-icon><Grid /></el-icon>
              卡片视图
            </el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 表格视图 -->
      <el-table
        v-if="viewMode === 'table'"
        v-loading="loading"
        :data="meetingList"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column prop="title" label="会议标题" min-width="200">
          <template #default="{ row }">
            <div class="meeting-title">
              <h4>{{ row.title }}</h4>
              <!-- 显示置顶标识 -->
              <el-tag v-if="row.isTop" type="danger" size="small">置顶</el-tag>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="type" label="会议类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getMeetingTypeTag(row.type)">
              {{ getMeetingTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.startTime) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="location" label="会议地点" min-width="150" />
        
        <el-table-column prop="registrationCount" label="报名人数" width="100" align="center">
          <template #default="{ row }">
            <el-link type="primary" @click="viewRegistrations(row.id)">
              {{ row.registrationCount || 0 }}人
            </el-link>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getMeetingStatusTag(row.status)">
              {{ getMeetingStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="creatorName" label="创建人" width="120" />
        
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-button size="small" @click="viewMeeting(row.id)">
                <el-icon><View /></el-icon>
              </el-button>
              <el-button size="small" @click="editMeeting(row.id)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-dropdown @command="(command) => handleAction(command, row)">
                <el-button size="small">
                  <el-icon><MoreFilled /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                      v-if="row.status === 0"
                      command="publish"
                      icon="Upload"
                    >
                      发布
                    </el-dropdown-item>
                    <el-dropdown-item
                      v-if="row.status === 1"
                      command="cancel"
                      icon="Close"
                    >
                      取消
                    </el-dropdown-item>
                    <el-dropdown-item
                      :command="row.isTop ? 'untop' : 'top'"
                      icon="Top"
                    >
                      {{ row.isTop ? '取消置顶' : '置顶' }}
                    </el-dropdown-item>
                    <el-dropdown-item command="copy" icon="CopyDocument">
                      复制
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" icon="Delete" divided>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 卡片视图 -->
      <div v-if="viewMode === 'card'" v-loading="loading" class="card-view">
        <div class="meeting-cards">
          <div
            v-for="meeting in meetingList"
            :key="meeting.id"
            class="meeting-card"
            @click="viewMeeting(meeting.id)"
          >
            <div class="card-header">
              <div class="meeting-type">
                <el-tag :type="getMeetingTypeTag(meeting.type)" size="small">
                  {{ getMeetingTypeText(meeting.type) }}
                </el-tag>
              </div>
              <div class="card-actions">
                <el-dropdown @command="(command) => handleAction(command, meeting)">
                  <el-button size="small" text>
                    <el-icon><MoreFilled /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="edit" icon="Edit">编辑</el-dropdown-item>
                      <el-dropdown-item command="delete" icon="Delete">删除</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
            
            <div class="card-content">
              <h3 class="meeting-title">
                {{ meeting.title }}
                <!-- 显示置顶标识 -->
                <el-tag v-if="meeting.isTop" type="danger" size="small">置顶</el-tag>
              </h3>
              
              <div class="meeting-info">
                <div class="info-item">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ formatDateTime(meeting.startTime) }}</span>
                </div>
                <div class="info-item">
                  <el-icon><Location /></el-icon>
                  <span>{{ meeting.location }}</span>
                </div>
                <div class="info-item">
                  <el-icon><User /></el-icon>
                  <span>{{ meeting.registrationCount || 0 }}人报名</span>
                </div>
              </div>
              
              <div class="meeting-description">
                {{ meeting.description || '暂无描述' }}
              </div>
            </div>
            
            <div class="card-footer">
              <div class="meeting-status">
                <el-tag :type="getMeetingStatusTag(meeting.status)">
                  {{ getMeetingStatusText(meeting.status) }}
                </el-tag>
              </div>
              <div class="creator-info">
                <span>{{ meeting.creatorName }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 会议详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="会议详情"
      width="800px"
      :before-close="handleDetailClose"
    >
      <div v-if="currentMeeting" class="meeting-detail">
        <div class="detail-header">
          <h2>{{ currentMeeting.title }}</h2>
          <div class="detail-tags">
            <el-tag :type="getMeetingTypeTag(currentMeeting.type)">
              {{ getMeetingTypeText(currentMeeting.type) }}
            </el-tag>
            <el-tag :type="getMeetingStatusTag(currentMeeting.status)">
              {{ getMeetingStatusText(currentMeeting.status) }}
            </el-tag>
            <el-tag v-if="currentMeeting.isTop" type="danger">置顶</el-tag>
          </div>
        </div>
        
        <div class="detail-content">
          <div class="detail-section">
            <h3>基本信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <label>开始时间：</label>
                <span>{{ formatDateTime(currentMeeting.startTime) }}</span>
              </div>
              <div class="info-item">
                <label>结束时间：</label>
                <span>{{ formatDateTime(currentMeeting.endTime) }}</span>
              </div>
              <div class="info-item">
                <label>会议地点：</label>
                <span>{{ currentMeeting.location }}</span>
              </div>
              <div class="info-item">
                <label>报名截止：</label>
                <span>{{ formatDateTime(currentMeeting.registrationDeadline) }}</span>
              </div>
              <div class="info-item">
                <label>最大人数：</label>
                <span>{{ currentMeeting.maxParticipants || '不限' }}</span>
              </div>
              <div class="info-item">
                <label>报名人数：</label>
                <span>{{ currentMeeting.registrationCount || 0 }}人</span>
              </div>
            </div>
          </div>
          
          <div class="detail-section">
            <h3>会议描述</h3>
            <div class="description-content">
              {{ currentMeeting.description || '暂无描述' }}
            </div>
          </div>
          
          <div class="detail-section">
            <h3>会议议程</h3>
            <div class="agenda-content">
              {{ currentMeeting.agenda || '暂无议程' }}
            </div>
          </div>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="editMeeting(currentMeeting.id)">
            编辑会议
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { meetingApi } from '../../api/meeting'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const viewMode = ref('table')
const detailDialogVisible = ref(false)
const meetingList = ref([])
const currentMeeting = ref(null)
const selectedMeetings = ref([])
const total = ref(0)
const meetingFormRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')

// 搜索表单
const searchForm = reactive({
  title: '',
  type: null,
  status: null,
  dateRange: null
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 20
})

// 会议表单
const meetingForm = reactive({
  id: null,
  title: '',
  description: '',
  type: '',
  status: 'draft',
  startTime: null,
  endTime: null,
  location: '',
  maxParticipants: null,
  registrationDeadline: null,
  requiresApproval: false,
  fee: 0,
  requirements: '',
  tags: [],
  coverImage: '',
  remarks: '',
  tenantId: localStorage.getItem('tenantId'),
  userId: localStorage.getItem('userId')
})

// 表单验证规则
const meetingFormRules = {
  title: [
    { required: true, message: '会议标题不能为空', trigger: 'blur' },
    { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '会议类型不能为空', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '开始时间不能为空', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '结束时间不能为空', trigger: 'change' }
  ],
  coverImage: [
    { required: true, message: '会议封面图片不能为空', trigger: 'change' }
  ]
}

// 加载会议列表
const loadMeetingList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    }
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const response = await meetingApi.getMeetingList(params, localStorage.getItem('tenantId')) 
    
    if(response.code == 200) {
      meetingList.value = response.data.records || []
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.message || '加载会议列表失败');
    }
  } catch (error) {
    ElMessage.error('加载会议列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.page = 1
  loadMeetingList()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    title: '',
    type: null,
    status: null,
    dateRange: null
  })
  pagination.page = 1
  loadMeetingList()
}

// 新增会议
const handleAdd = () => {
  dialogTitle.value = '新增会议'
  Object.assign(meetingForm, {
    id: null,
    title: '',
    description: '',
    type: '',
    status: 'draft',
    startTime: null,
    endTime: null,
    location: '',
    maxParticipants: null,
    registrationDeadline: null,
    requiresApproval: false,
    fee: 0,
    requirements: '',
    tags: [],
    coverImage: '',
    remarks: '',
    tenantId: localStorage.getItem('tenantId'),
    userId: localStorage.getItem('userId')
  })
  dialogVisible.value = true
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadMeetingList()
}

const handleCurrentChange = (page) => {
  pagination.page = page
  loadMeetingList()
}

// 选择处理
const handleSelectionChange = (selection) => {
  selectedMeetings.value = selection
}

// 创建会议
const createMeeting = () => {
  router.push('/dashboard/meetings/create')
}

// 查看会议
const viewMeeting = (id) => {
  const meeting = meetingList.value.find(m => m.id === id)
  if (meeting) {
    currentMeeting.value = meeting
    detailDialogVisible.value = true
  }
}

// 编辑会议
const editMeeting = (id) => {
  router.push(`/dashboard/meetings/edit/${id}`)
}

// 查看报名列表
const viewRegistrations = (id) => {
  router.push(`/dashboard/meetings/${id}/registrations`)
}

// 操作处理
const handleAction = async (command, meeting) => {
  switch (command) {
    case 'publish':
      await handlePublish(meeting.id)
      break
    case 'cancel':
      await handleCancel(meeting.id)
      break
    case 'top':
      await handleSetTop(meeting.id, 1)
      break
    case 'untop':
      await handleSetTop(meeting.id, 0)
      break
    case 'copy':
      await handleCopy(meeting.id)
      break
    case 'edit':
      editMeeting(meeting.id)
      break
    case 'delete':
      await handleDelete(meeting.id)
      break
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!meetingFormRef.value) return

  try {
    await meetingFormRef.value.validate()
    submitLoading.value = true

    let response
    if (meetingForm.id) {
      response = await meetingApi.updateMeeting(meetingForm.id, meetingForm)
    } else {
      response = await meetingApi.createMeeting(meetingForm)
    }

    if (response.code === 200) {
      ElMessage.success(meetingForm.id ? '修改成功' : '创建成功')
      dialogVisible.value = false
      loadMeetingList()
    } else {
      ElMessage.error(response.message || (meetingForm.id ? '修改失败' : '创建失败'))
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error(meetingForm.id ? '修改失败' : '创建失败')
  } finally {
    submitLoading.value = false
  }
}

// 发布会议
const handlePublish = async (id) => {
  try {
    await ElMessageBox.confirm('确认发布该会议吗？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟API调用
    ElMessage.success('会议发布成功')
    loadMeetingList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发布失败')
  
    }
  }
}

// 取消会议
const handleCancel = async (id) => {
  try {
    await ElMessageBox.confirm('确认取消该会议吗？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟API调用
    ElMessage.success('会议取消成功')
    loadMeetingList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消失败')
    }
  }
}

// 设置置顶
const handleSetTop = async (id, isTop) => {
  try {
    await ElMessageBox.confirm(
      isTop ? '确认将该会议置顶吗？' : '确认取消该会议置顶吗？',
      '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );

    const response = await meetingApi.topMeeting(id, isTop);
    if (response.code === 200) {
      const action = isTop ? '置顶' : '取消置顶';
      ElMessage.success(`${action}成功`);
      loadMeetingList();
    } else {
      ElMessage.error(response.message || `${action}失败`);
    }
  } catch (error) {
    if (error !== 'cancel') {
      const action = isTop ? '置顶' : '取消置顶';
      ElMessage.error(`${action}失败`);
    }
  }
}

// 复制会议
const handleCopy = async (id) => {
  try {
    // 模拟API调用
    ElMessage.success('会议复制成功')
    loadMeetingList()
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 删除会议
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确认删除该会议吗？删除后无法恢复。', '提示', {
      confirmButtonText: '确认删除',
      cancelButtonText: '取消',
      type: 'error'
    })
    
    const response = await meetingApi.deleteMeeting(id)
    if(response.code == 200) {
      ElMessage.success('会议删除成功')
      loadMeetingList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 关闭详情对话框
const handleDetailClose = () => {
  detailDialogVisible.value = false
  currentMeeting.value = null
}

// 格式化日期时间
const formatDateTime = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
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
  loadMeetingList()
})
</script>

<style scoped>
.meeting-management {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left h1 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 5px 0;
}

.header-left p {
  color: #909399;
  margin: 0;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
  border: none;
  border-radius: 12px;
}

.search-form {
  margin: 0;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

/* 表格卡片 */
.table-card {
  border: none;
  border-radius: 12px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.table-title h3 {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.total-count {
  font-size: 14px;
  color: #909399;
}

/* 会议标题 */
.meeting-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.meeting-title h4 {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

/* 卡片视图 */
.card-view {
  min-height: 400px;
}

.meeting-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.meeting-card {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.meeting-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  border-color: #409eff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-content .meeting-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.meeting-info {
  margin-bottom: 12px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
  color: #606266;
}

.info-item .el-icon {
  margin-right: 8px;
  color: #909399;
}

.meeting-description {
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 16px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.creator-info {
  font-size: 12px;
  color: #909399;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* 会议详情对话框 */
.meeting-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.detail-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.detail-tags {
  display: flex;
  gap: 8px;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section h3 {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.info-grid .info-item {
  display: flex;
  align-items: center;
}

.info-grid .info-item label {
  font-weight: 500;
  color: #606266;
  margin-right: 8px;
  min-width: 80px;
}

.description-content,
.agenda-content {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-wrap;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .meeting-cards {
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  }
}

@media (max-width: 768px) {
  .meeting-management {
    padding: 15px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .search-form .el-form-item {
    margin-bottom: 15px;
  }
  
  .meeting-cards {
    grid-template-columns: 1fr;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>

