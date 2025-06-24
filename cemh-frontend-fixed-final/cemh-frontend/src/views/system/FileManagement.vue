<template>
  <div class="file-management modern-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">文件管理中心</h1>
        <div class="header-actions">
          <el-upload
            ref="uploadRef"
            action="#"
            :multiple="true"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="uploadFile"
            class="upload-btn"
          >
            <el-button type="primary" class="modern-btn primary">
              <el-icon><Upload /></el-icon>
              上传文件
            </el-button>
          </el-upload>
          <el-button @click="createFolder" class="modern-btn secondary">
            <el-icon><FolderAdd /></el-icon>
            新建文件夹
          </el-button>
        </div>
      </div>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/">
          <el-breadcrumb-item @click="navigateToPath('')">
            <el-icon><HomeFilled /></el-icon>
            根目录
          </el-breadcrumb-item>
          <el-breadcrumb-item 
            v-for="(path, index) in breadcrumbPaths" 
            :key="index"
            @click="navigateToPath(path.fullPath)"
          >
            {{ path.name }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="toolbar-right">
        <!-- 搜索框 -->
        <el-input
          v-model="searchKeyword"
          placeholder="搜索文件..."
          class="search-input"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        
        <!-- 视图切换 -->
        <el-button-group class="view-toggle">
          <el-button 
            :type="viewMode === 'grid' ? 'primary' : 'default'"
            @click="viewMode = 'grid'"
          >
            <el-icon><Grid /></el-icon>
          </el-button>
          <el-button 
            :type="viewMode === 'list' ? 'primary' : 'default'"
            @click="viewMode = 'list'"
          >
            <el-icon><List /></el-icon>
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 文件统计 -->
    <div class="file-stats">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ totalFiles }}</div>
          <div class="stat-label">总文件数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Folder /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ totalFolders }}</div>
          <div class="stat-label">文件夹数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><PieChart /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ formatFileSize(totalSize) }}</div>
          <div class="stat-label">总大小</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Clock /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ recentFiles }}</div>
          <div class="stat-label">最近文件</div>
        </div>
      </div>
    </div>

    <!-- 文件列表 -->
    <div class="file-content">
      <!-- 网格视图 -->
      <div v-if="viewMode === 'grid'" class="file-grid">
        <div 
          v-for="item in filteredFiles" 
          :key="item.id"
          class="file-item"
          @click="handleItemClick(item)"
          @contextmenu.prevent="showContextMenu($event, item)"
        >
          <div class="file-icon">
            <el-icon v-if="item.type === 'folder'"><Folder /></el-icon>
            <el-icon v-else-if="isImageFile(item)"><Picture /></el-icon>
            <el-icon v-else-if="isVideoFile(item)"><VideoPlay /></el-icon>
            <el-icon v-else-if="isAudioFile(item)"><Headphone /></el-icon>
            <el-icon v-else><Document /></el-icon>
          </div>
          <div class="file-name" :title="item.name">{{ item.name }}</div>
          <div class="file-meta" v-if="item.type !== 'folder'">
            {{ formatFileSize(item.size) }}
          </div>
        </div>
      </div>

      <!-- 列表视图 -->
      <div v-else class="file-list">
        <el-table 
          :data="filteredFiles" 
          class="modern-table"
          @row-click="handleItemClick"
          @row-contextmenu="showContextMenu"
        >
          <el-table-column width="50">
            <template #default="{ row }">
              <el-icon v-if="row.type === 'folder'"><Folder /></el-icon>
              <el-icon v-else-if="isImageFile(row)"><Picture /></el-icon>
              <el-icon v-else-if="isVideoFile(row)"><VideoPlay /></el-icon>
              <el-icon v-else-if="isAudioFile(row)"><Headphone /></el-icon>
              <el-icon v-else><Document /></el-icon>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称" min-width="200" />
          <el-table-column prop="size" label="大小" width="120">
            <template #default="{ row }">
              {{ row.type === 'folder' ? '-' : formatFileSize(row.size) }}
            </template>
          </el-table-column>
          <el-table-column prop="type" label="类型" width="100">
            <template #default="{ row }">
              {{ getFileTypeText(row) }}
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="修改时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.updateTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button 
                type="text" 
                size="small"
                @click.stop="downloadFile(row)"
                v-if="row.type !== 'folder'"
              >
                下载
              </el-button>
              <el-button 
                type="text" 
                size="small"
                @click.stop="renameFile(row)"
              >
                重命名
              </el-button>
              <el-button 
                type="text" 
                size="small"
                @click.stop="deleteFile(row)"
                class="danger-btn"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredFiles.length === 0" class="empty-state">
        <el-empty description="暂无文件">
          <el-button type="primary" @click="$refs.uploadRef.$el.click()">
            上传第一个文件
          </el-button>
        </el-empty>
      </div>
    </div>

    <!-- 右键菜单 -->
    <div 
      v-show="contextMenuVisible"
      :style="{ left: contextMenuX + 'px', top: contextMenuY + 'px' }"
      class="context-menu"
      @click="hideContextMenu"
    >
      <div class="menu-item" @click="downloadFile(selectedItem)" v-if="selectedItem?.type !== 'folder'">
        <el-icon><Download /></el-icon>
        下载
      </div>
      <div class="menu-item" @click="renameFile(selectedItem)">
        <el-icon><Edit /></el-icon>
        重命名
      </div>
      <div class="menu-item" @click="copyFile(selectedItem)">
        <el-icon><CopyDocument /></el-icon>
        复制
      </div>
      <div class="menu-item" @click="moveFile(selectedItem)">
        <el-icon><Rank /></el-icon>
        移动
      </div>
      <div class="menu-divider"></div>
      <div class="menu-item danger" @click="deleteFile(selectedItem)">
        <el-icon><Delete /></el-icon>
        删除
      </div>
    </div>

    <!-- 文件预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      :title="previewFile?.name"
      width="80%"
      class="preview-dialog"
    >
      <div class="preview-content">
        <img 
          v-if="previewFile && isImageFile(previewFile)"
          :src="previewFile.url"
          class="preview-image"
        />
        <video 
          v-else-if="previewFile && isVideoFile(previewFile)"
          :src="previewFile.url"
          controls
          class="preview-video"
        />
        <audio 
          v-else-if="previewFile && isAudioFile(previewFile)"
          :src="previewFile.url"
          controls
          class="preview-audio"
        />
        <div v-else class="preview-unsupported">
          <el-icon><Document /></el-icon>
          <p>该文件类型不支持预览</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

// 响应式数据
const searchKeyword = ref('')
const viewMode = ref('grid')
const currentPath = ref('')
const files = ref([])
const contextMenuVisible = ref(false)
const contextMenuX = ref(0)
const contextMenuY = ref(0)
const selectedItem = ref(null)
const previewVisible = ref(false)
const previewFile = ref(null)
const uploadRef = ref()

// 计算属性
const breadcrumbPaths = computed(() => {
  if (!currentPath.value) return []
  const paths = currentPath.value.split('/').filter(Boolean)
  return paths.map((path, index) => ({
    name: path,
    fullPath: paths.slice(0, index + 1).join('/')
  }))
})

const filteredFiles = computed(() => {
  if (!searchKeyword.value) return files.value
  return files.value.filter(file => 
    file.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const totalFiles = computed(() => 
  files.value.filter(f => f.type !== 'folder').length
)

const totalFolders = computed(() => 
  files.value.filter(f => f.type === 'folder').length
)

const totalSize = computed(() => 
  files.value.reduce((total, file) => total + (file.size || 0), 0)
)

const recentFiles = computed(() => {
  const oneWeekAgo = dayjs().subtract(7, 'day')
  return files.value.filter(f => 
    f.type !== 'folder' && dayjs(f.updateTime).isAfter(oneWeekAgo)
  ).length
})

// 方法
const loadFiles = async () => {
  try {
    // 这里应该调用API获取文件列表
    // const response = await getFileList(currentPath.value)
    
    // 模拟数据
    files.value = [
      {
        id: 1,
        name: '会议资料',
        type: 'folder',
        updateTime: '2024-06-20 10:00:00'
      },
      {
        id: 2,
        name: '用户头像',
        type: 'folder',
        updateTime: '2024-06-19 15:30:00'
      },
      {
        id: 3,
        name: '业绩报告.pdf',
        type: 'file',
        size: 2048576,
        updateTime: '2024-06-20 09:30:00',
        url: 'https://example.com/file.pdf'
      },
      {
        id: 4,
        name: '产品介绍.pptx',
        type: 'file',
        size: 5242880,
        updateTime: '2024-06-19 14:20:00',
        url: 'https://example.com/file.pptx'
      },
      {
        id: 5,
        name: '团队合影.jpg',
        type: 'file',
        size: 1024000,
        updateTime: '2024-06-18 16:45:00',
        url: 'https://via.placeholder.com/800x600'
      }
    ]
  } catch (error) {
    ElMessage.error('加载文件列表失败')
  }
}

const navigateToPath = (path) => {
  currentPath.value = path
  loadFiles()
}

const handleItemClick = (item) => {
  if (item.type === 'folder') {
    currentPath.value = currentPath.value ? `${currentPath.value}/${item.name}` : item.name
    loadFiles()
  } else {
    previewFile.value = item
    previewVisible.value = true
  }
}

const handleSearch = () => {
  // 搜索逻辑已在计算属性中实现
}

const beforeUpload = (file) => {
  const isLt100M = file.size / 1024 / 1024 < 100
  if (!isLt100M) {
    ElMessage.error('文件大小不能超过 100MB!')
    return false
  }
  return true
}

const uploadFile = async (options) => {
  try {
    // 这里应该实现真实的文件上传
    const file = options.file
    
    // 模拟上传进度
    const newFile = {
      id: Date.now(),
      name: file.name,
      type: 'file',
      size: file.size,
      updateTime: new Date().toISOString(),
      url: URL.createObjectURL(file)
    }
    
    files.value.push(newFile)
    ElMessage.success('文件上传成功')
  } catch (error) {
    ElMessage.error('文件上传失败')
  }
}

const createFolder = async () => {
  try {
    const { value: folderName } = await ElMessageBox.prompt('请输入文件夹名称', '新建文件夹', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    
    if (folderName) {
      const newFolder = {
        id: Date.now(),
        name: folderName,
        type: 'folder',
        updateTime: new Date().toISOString()
      }
      
      files.value.push(newFolder)
      ElMessage.success('文件夹创建成功')
    }
  } catch {
    // 用户取消
  }
}

const downloadFile = (file) => {
  if (file.type === 'folder') return
  
  // 这里应该实现真实的文件下载
  const link = document.createElement('a')
  link.href = file.url
  link.download = file.name
  link.click()
  
  ElMessage.success('开始下载文件')
}

const renameFile = async (file) => {
  try {
    const { value: newName } = await ElMessageBox.prompt('请输入新名称', '重命名', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputValue: file.name
    })
    
    if (newName && newName !== file.name) {
      file.name = newName
      ElMessage.success('重命名成功')
    }
  } catch {
    // 用户取消
  }
}

const copyFile = (file) => {
  ElMessage.info('复制功能开发中')
}

const moveFile = (file) => {
  ElMessage.info('移动功能开发中')
}

const deleteFile = async (file) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除 "${file.name}" 吗？`,
      '确认删除',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )
    
    const index = files.value.findIndex(f => f.id === file.id)
    if (index > -1) {
      files.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  } catch {
    // 用户取消
  }
}

const showContextMenu = (event, item) => {
  event.preventDefault()
  selectedItem.value = item
  contextMenuX.value = event.clientX
  contextMenuY.value = event.clientY
  contextMenuVisible.value = true
}

const hideContextMenu = () => {
  contextMenuVisible.value = false
  selectedItem.value = null
}

// 工具方法
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatDateTime = (dateTime) => {
  return dayjs(dateTime).format('YYYY-MM-DD HH:mm')
}

const isImageFile = (file) => {
  const imageExts = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp']
  return imageExts.some(ext => file.name.toLowerCase().endsWith(ext))
}

const isVideoFile = (file) => {
  const videoExts = ['.mp4', '.avi', '.mov', '.wmv', '.flv', '.webm']
  return videoExts.some(ext => file.name.toLowerCase().endsWith(ext))
}

const isAudioFile = (file) => {
  const audioExts = ['.mp3', '.wav', '.flac', '.aac', '.ogg']
  return audioExts.some(ext => file.name.toLowerCase().endsWith(ext))
}

const getFileTypeText = (file) => {
  if (file.type === 'folder') return '文件夹'
  
  const ext = file.name.split('.').pop()?.toLowerCase()
  const typeMap = {
    pdf: 'PDF文档',
    doc: 'Word文档',
    docx: 'Word文档',
    xls: 'Excel表格',
    xlsx: 'Excel表格',
    ppt: 'PPT演示',
    pptx: 'PPT演示',
    txt: '文本文件',
    jpg: '图片',
    jpeg: '图片',
    png: '图片',
    gif: '图片',
    mp4: '视频',
    mp3: '音频',
    zip: '压缩包',
    rar: '压缩包'
  }
  
  return typeMap[ext] || '未知类型'
}

// 生命周期
onMounted(() => {
  loadFiles()
  document.addEventListener('click', hideContextMenu)
})

onUnmounted(() => {
  document.removeEventListener('click', hideContextMenu)
})
</script>

<style scoped>
.file-management {
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
  gap: var(--spacing-md);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-2xl);
  padding: var(--spacing-lg);
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.search-input {
  width: 250px;
}

.file-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-lg);
  margin-bottom: var(--spacing-2xl);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-lg);
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-fast);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-color);
  color: white;
  border-radius: var(--radius-lg);
  font-size: var(--font-size-xl);
}

.stat-value {
  font-size: var(--font-size-2xl);
  font-weight: 600;
  color: var(--text-primary);
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.file-content {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
  min-height: 400px;
}

.file-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: var(--spacing-lg);
}

.file-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
  border: 2px solid transparent;
}

.file-item:hover {
  background: var(--bg-secondary);
  border-color: var(--primary-color);
  transform: translateY(-2px);
}

.file-icon {
  font-size: var(--font-size-4xl);
  color: var(--primary-color);
  margin-bottom: var(--spacing-md);
}

.file-name {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  text-align: center;
  word-break: break-all;
  margin-bottom: var(--spacing-sm);
  line-height: 1.4;
}

.file-meta {
  font-size: var(--font-size-xs);
  color: var(--text-secondary);
}

.context-menu {
  position: fixed;
  background: var(--bg-primary);
  border: 1px solid var(--border-normal);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xl);
  padding: var(--spacing-sm);
  z-index: 1000;
  min-width: 150px;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: var(--font-size-sm);
  color: var(--text-primary);
  transition: background var(--transition-fast);
}

.menu-item:hover {
  background: var(--bg-secondary);
}

.menu-item.danger {
  color: var(--error-color);
}

.menu-divider {
  height: 1px;
  background: var(--border-light);
  margin: var(--spacing-sm) 0;
}

.danger-btn {
  color: var(--error-color);
}

.preview-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.preview-content {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.preview-image {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
}

.preview-video {
  max-width: 100%;
  max-height: 70vh;
}

.preview-audio {
  width: 100%;
}

.preview-unsupported {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-lg);
  color: var(--text-secondary);
}

.preview-unsupported .el-icon {
  font-size: var(--font-size-4xl);
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

@media (max-width: 768px) {
  .file-management {
    padding: var(--spacing-lg);
  }
  
  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: flex-start;
  }
  
  .toolbar {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: stretch;
  }
  
  .toolbar-right {
    justify-content: space-between;
  }
  
  .search-input {
    width: 100%;
  }
  
  .file-stats {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .file-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }
}
</style>

