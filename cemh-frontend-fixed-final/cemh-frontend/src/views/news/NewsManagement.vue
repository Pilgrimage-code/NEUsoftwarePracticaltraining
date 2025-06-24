<template>
  <div class="news-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon><Document /></el-icon>
          资讯管理
        </h1>
        <p class="page-description">管理系统资讯内容、分类和发布状态</p>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-card class="search-card">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="新闻标题">
            <el-input
                v-model="searchForm.title"
                placeholder="请输入新闻标题"
                clearable
                style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="新闻简介">
            <el-input
                v-model="searchForm.summary"
                placeholder="请输入新闻简介"
                clearable
                style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="作者">
            <el-input
                v-model="searchForm.author"
                placeholder="请输入作者"
                clearable
                style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="新闻图片路径">
            <el-input
                v-model="searchForm.coverImage"
                placeholder="请输入图片路径"
                clearable
                style="width: 200px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :loading="loading">
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
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增
        </el-button>
        <el-button
            type="danger"
            @click="handleBatchDelete"
            :disabled="selectedNews.length === 0"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 资讯列表 -->
    <div class="table-section">
      <el-card class="table-card">
        <el-table
            :data="newsList"
            v-loading="loading"
            @selection-change="handleSelectionChange"
            stripe
            style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="title" label="新闻标题" min-width="200">
            <template #default="{ row }">
              <div class="news-title">
                <span>{{ row.title }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="summary" label="新闻简介" min-width="200" show-overflow-tooltip />
          <el-table-column prop="coverImage" label="新闻图片" width="120">
            <template #default="{ row }">
              <el-image
                  v-if="row.coverImage"
                  :src="row.coverImage"
                  :preview-src-list="[row.coverImage]"
                  style="width: 50px; height: 50px"
                  fit="cover"
              />
              <span v-else>无图片</span>
            </template>
          </el-table-column>
          <el-table-column prop="author" label="作者" width="100" />
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleView(row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button size="small" @click="handleEdit(row)">
                <el-icon><Edit /></el-icon>
                修改
              </el-button>
              <el-button
                  size="small"
                  type="danger"
                  @click="handleDelete(row)"
              >
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
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

    <!-- 资讯表单对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="900px"
        :before-close="handleDialogClose"
        destroy-on-close
    >
      <el-form
          ref="newsFormRef"
          :model="newsForm"
          :rules="newsFormRules"
          label-width="120px"
      >
        <el-form-item label="新闻标题" prop="title">
          <el-input
              v-model="newsForm.title"
              placeholder="请输入新闻标题"
              maxlength="100"
              show-word-limit
          />
        </el-form-item>

        <el-form-item label="新闻简介" prop="summary">
          <el-input
              v-model="newsForm.summary"
              type="textarea"
              :rows="3"
              placeholder="请输入新闻简介"
              maxlength="500"
              show-word-limit
          />
        </el-form-item>

        <el-form-item label="作者" prop="author">
          <el-input
              v-model="newsForm.author"
              placeholder="请输入作者"
              maxlength="50"
          />
        </el-form-item>

        <el-form-item label="新闻图片" prop="coverImage">
          <div class="image-upload-container">
            <el-upload
                ref="uploadRef"
                :action="uploadAction"
                :headers="uploadHeaders"
                :before-upload="beforeImageUpload"
                :on-success="handleImageSuccess"
                :on-error="handleImageError"
                :show-file-list="false"
                accept="image/*"
                class="image-uploader"
            >
              <div v-if="newsForm.coverImage" class="image-preview">
                <el-image
                    :src="newsForm.coverImage"
                    style="width: 150px; height: 150px"
                    fit="cover"
                />
                <div class="image-overlay">
                  <el-button size="small" type="primary" @click.stop="handleImagePreview">
                    <el-icon><View /></el-icon>
                  </el-button>
                  <el-button size="small" type="danger" @click.stop="handleImageRemove">
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
              </div>
              <div v-else class="upload-placeholder">
                <el-icon class="upload-icon"><Plus /></el-icon>
                <div class="upload-text">点击上传图片</div>
              </div>
            </el-upload>
            <div class="upload-tips">
              支持 jpg、png、gif 格式，文件大小不超过 2MB
            </div>
          </div>
        </el-form-item>

        <el-form-item label="新闻内容" prop="content">
          <div class="rich-editor-container">
            <div ref="editorRef" class="rich-editor"></div>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看资讯对话框 -->
    <el-dialog
        title="查看资讯"
        v-model="viewDialogVisible"
        width="800px"
    >
      <div v-if="currentNews" class="news-detail">
        <h2>{{ currentNews.title }}</h2>
        <div class="news-meta">
          <span>作者：{{ currentNews.author }}</span>
          <span>创建时间：{{ formatDate(currentNews.createTime) }}</span>
        </div>
        <div class="news-summary">
          <strong>简介：</strong>{{ currentNews.summary }}
        </div>
        <div v-if="currentNews.coverImage" class="news-image">
          <strong>图片：</strong>
          <el-image
              :src="currentNews.coverImage"
              :preview-src-list="[currentNews.coverImage]"
              style="max-width: 100%; margin-top: 10px"
          />
        </div>
        <div class="news-content">
          <strong>内容：</strong>
          <div v-html="currentNews.content" class="content-html"></div>
        </div>
      </div>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog
        title="图片预览"
        v-model="imagePreviewVisible"
        width="600px"
    >
      <el-image
          :src="previewImageUrl"
          style="width: 100%"
          fit="contain"
      />
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Search, Refresh, Plus, Delete, View, Edit } from '@element-plus/icons-vue'
import { newsApi } from '@/api/news'

// 简单的富文本编辑器实现
class SimpleRichEditor {
  constructor(container) {
    this.container = container
    this.init()
  }

  init() {
    // 创建工具栏
    const toolbar = document.createElement('div')
    toolbar.className = 'editor-toolbar'
    toolbar.innerHTML = `
      <button type="button" data-command="bold" title="粗体"><b>B</b></button>
      <button type="button" data-command="italic" title="斜体"><i>I</i></button>
      <button type="button" data-command="underline" title="下划线"><u>U</u></button>
      <button type="button" data-command="insertOrderedList" title="有序列表">1.</button>
      <button type="button" data-command="insertUnorderedList" title="无序列表">•</button>
      <button type="button" data-command="justifyLeft" title="左对齐">←</button>
      <button type="button" data-command="justifyCenter" title="居中">↔</button>
      <button type="button" data-command="justifyRight" title="右对齐">→</button>
    `

    // 创建编辑区域
    const editor = document.createElement('div')
    editor.className = 'editor-content'
    editor.contentEditable = true
    editor.innerHTML = '<p><br></p>'

    this.container.appendChild(toolbar)
    this.container.appendChild(editor)

    this.toolbar = toolbar
    this.editor = editor

    // 绑定事件
    this.bindEvents()
  }

  bindEvents() {
    // 工具栏按钮事件
    this.toolbar.addEventListener('click', (e) => {
      if (e.target.tagName === 'BUTTON') {
        e.preventDefault()
        const command = e.target.dataset.command
        document.execCommand(command, false, null)
        this.editor.focus()
      }
    })

    // 编辑器焦点事件
    this.editor.addEventListener('focus', () => {
      this.container.classList.add('focused')
    })

    this.editor.addEventListener('blur', () => {
      this.container.classList.remove('focused')
    })
  }

  getContent() {
    return this.editor.innerHTML
  }

  setContent(content) {
    this.editor.innerHTML = content || '<p><br></p>'
  }

  destroy() {
    if (this.container) {
      this.container.innerHTML = ''
    }
  }
}

export default {
  name: 'NewsManagement',
  components: {
    Document,
    Search,
    Refresh,
    Plus,
    Delete,
    View,
    Edit
  },
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const newsList = ref([])
    const selectedNews = ref([])
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const imagePreviewVisible = ref(false)
    const dialogTitle = ref('')
    const newsFormRef = ref(null)
    const editorRef = ref(null)
    const uploadRef = ref(null)
    const currentNews = ref(null)
    const previewImageUrl = ref('')
    let richEditor = null

    // 上传配置
    const uploadAction = ref('http://localhost:8080/api/upload/image')
    const uploadHeaders = ref({
      'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
    })

    // 搜索表单
    const searchForm = reactive({
      title: '',
      summary: '',
      author: '',
      coverImage: ''
    })

    // 分页信息
    const pagination = reactive({
      page: 1,
      size: 10,
      total: 0
    })

    // 资讯表单
    const newsForm = reactive({
      id: null,
      title: '',
      summary: '',
      content: '',
      author: '',
      coverImage: ''
    })

    // 表单验证规则
    const newsFormRules = {
      title: [
        { required: true, message: '新闻标题不能为空', trigger: 'blur' },
        { min: 1, max: 100, message: '标题长度在 1 到 100 个字符', trigger: 'blur' }
      ],
      summary: [
        { required: true, message: '新闻简介不能为空', trigger: 'blur' },
        { min: 1, max: 500, message: '简介长度在 1 到 500 个字符', trigger: 'blur' }
      ],
      author: [
        { required: true, message: '作者不能为空', trigger: 'blur' },
        { min: 1, max: 50, message: '作者长度在 1 到 50 个字符', trigger: 'blur' }
      ],
      content: [
        { required: true, message: '新闻内容不能为空', trigger: 'blur' }
      ],
      coverImage: [
        { required: true, message: '新闻图片不能为空', trigger: 'change' }
      ]
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }

    // 获取资讯列表
    const getNewsList = async () => {
      loading.value = true
      try {
        const params = {
          page: pagination.page,
          size: pagination.size,
          ...searchForm
        }
        const response = await newsApi.getNewsList(params, localStorage.getItem('tenantId'))
        if (response.code === 200) {
          newsList.value = response.data.records || []
          pagination.total = response.data.total || 0
        } else {
          ElMessage.error(response.message || '获取资讯列表失败')
        }
      } catch (error) {
        console.error('获取资讯列表失败:', error)
        ElMessage.error('获取资讯列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.page = 1
      getNewsList()
    }

    // 重置搜索
    const handleReset = () => {
      Object.assign(searchForm, {
        title: '',
        summary: '',
        author: '',
        coverImage: ''
      })
      pagination.page = 1
      getNewsList()
    }

    // 重置表单
    const resetNewsForm = () => {
      Object.assign(newsForm, {
        id: null,
        title: '',
        summary: '',
        content: '',
        author: '',
        coverImage: ''
      })
      if (richEditor) {
        richEditor.setContent('')
      }
    }

    // 新增资讯
    const handleAdd = () => {
      dialogTitle.value = '新增资讯'
      resetNewsForm()
      dialogVisible.value = true
      nextTick(() => {
        initRichEditor()
      })
    }

    // 编辑资讯
    const handleEdit = (row) => {
      dialogTitle.value = '修改资讯'
      Object.assign(newsForm, { ...row })
      dialogVisible.value = true
      nextTick(() => {
        initRichEditor()
        if (richEditor) {
          richEditor.setContent(newsForm.content)
        }
      })
    }

    // 查看资讯
    const handleView = (row) => {
      currentNews.value = row
      viewDialogVisible.value = true
    }

    // 删除资讯
    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm(
            `确定要删除资讯"${row.title}"吗？`,
            '删除确认',
            {
              confirmButtonText: '是',
              cancelButtonText: '否',
              type: 'warning'
            }
        )

        const response = await newsApi.deleteNews(row.id, localStorage.getItem("tenantId"))
        if (response.code === 200) {
          ElMessage.success('删除成功')
          getNewsList()
        } else {
          ElMessage.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          ElMessage.error('删除失败')
        }
      }
    }

    // 批量删除
    const handleBatchDelete = async () => {
      try {
        await ElMessageBox.confirm(
            `确定要删除选中的 ${selectedNews.value.length} 条资讯吗？`,
            '批量删除确认',
            {
              confirmButtonText: '是',
              cancelButtonText: '否',
              type: 'warning'
            }
        )

        const ids = selectedNews.value.map(news => news.id)
        const response = await newsApi.batchDeleteNews(ids, localStorage.getItem("tenantId"))
        if (response.code === 200) {
          ElMessage.success('批量删除成功')
          getNewsList()
        } else {
          ElMessage.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          ElMessage.error('批量删除失败')
        }
      }
    }

    // 选择变化
    const handleSelectionChange = (selection) => {
      selectedNews.value = selection
    }

    // 分页变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.page = 1
      getNewsList()
    }

    const handleCurrentChange = (page) => {
      pagination.page = page
      getNewsList()
    }

    // 初始化富文本编辑器
    const initRichEditor = () => {
      if (editorRef.value && !richEditor) {
        richEditor = new SimpleRichEditor(editorRef.value)
      }
    }

    // 图片上传前验证
    const beforeImageUpload = (file) => {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        ElMessage.error('只能上传图片文件!')
        return false
      }
      if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!')
        return false
      }
      return true
    }

    // 图片上传成功
    const handleImageSuccess = (response) => {
      if (response.code === 200) {
        newsForm.coverImage = response.data.url
        ElMessage.success('图片上传成功')
      } else {
        ElMessage.error(response.message || '图片上传失败')
      }
    }

    // 图片上传失败
    const handleImageError = () => {
      ElMessage.error('图片上传失败')
    }

    // 图片预览
    const handleImagePreview = () => {
      previewImageUrl.value = newsForm.coverImage
      imagePreviewVisible.value = true
    }

    // 移除图片
    const handleImageRemove = () => {
      newsForm.coverImage = ''
    }

    // 提交表单
    const handleSubmit = async () => {
      if (!newsFormRef.value) return

      try {
        await newsFormRef.value.validate()

        // 获取富文本编辑器内容
        if (richEditor) {
          newsForm.content = richEditor.getContent()
        }

        // 验证内容不能为空
        if (!newsForm.content || newsForm.content.trim() === '<p><br></p>' || newsForm.content.trim() === '') {
          ElMessage.error('新闻内容不能为空')
          return
        }

        submitLoading.value = true

        let response
        const tenantId = localStorage.getItem("tenantId")
        const userId = localStorage.getItem("userId")
        if (newsForm.id) {
          response = await newsApi.updateNews(newsForm.id, newsForm, tenantId, userId)
        } else {
          response = await newsApi.createNews(newsForm, tenantId, userId)
        }


        if (response.code === 200) {
          ElMessage.success(newsForm.id ? '修改成功' : '创建成功')
          dialogVisible.value = false
          getNewsList()
        } else {
          ElMessage.error(response.message || (newsForm.id ? '修改失败' : '创建失败'))
        }
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error(newsForm.id ? '修改失败' : '创建失败')
      } finally {
        submitLoading.value = false
      }
    }

    // 关闭对话框
    const handleDialogClose = () => {
      dialogVisible.value = false
      if (richEditor) {
        richEditor.destroy()
        richEditor = null
      }
    }

    // 组件挂载
    onMounted(() => {
      getNewsList()
    })

    return {
      // 响应式数据
      loading,
      submitLoading,
      newsList,
      selectedNews,
      dialogVisible,
      viewDialogVisible,
      imagePreviewVisible,
      dialogTitle,
      newsFormRef,
      editorRef,
      uploadRef,
      currentNews,
      previewImageUrl,
      uploadAction,
      uploadHeaders,
      searchForm,
      pagination,
      newsForm,
      newsFormRules,

      // 方法
      formatDate,
      getNewsList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleView,
      handleDelete,
      handleBatchDelete,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      beforeImageUpload,
      handleImageSuccess,
      handleImageError,
      handleImagePreview,
      handleImageRemove,
      handleSubmit,
      handleDialogClose
    }
  }
}
</script>

<style scoped>
.news-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-description {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-section {
  margin-bottom: 20px;
}

.search-card {
  border-radius: 8px;
}

.search-form {
  margin: 0;
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
  border-radius: 8px;
}

.news-title {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.image-upload-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.image-uploader {
  display: inline-block;
}

.image-preview {
  position: relative;
  display: inline-block;
  border-radius: 6px;
  overflow: hidden;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-preview:hover .image-overlay {
  opacity: 1;
}

.upload-placeholder {
  width: 150px;
  height: 150px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.upload-placeholder:hover {
  border-color: #409eff;
}

.upload-icon {
  font-size: 28px;
  color: #8c939d;
  margin-bottom: 8px;
}

.upload-text {
  color: #8c939d;
  font-size: 14px;
}

.upload-tips {
  font-size: 12px;
  color: #909399;
}

.rich-editor-container {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.rich-editor {
  min-height: 200px;
}

.editor-toolbar {
  background: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
  padding: 8px 12px;
  display: flex;
  gap: 8px;
}

.editor-toolbar button {
  border: 1px solid #dcdfe6;
  background: white;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.editor-toolbar button:hover {
  background: #ecf5ff;
  border-color: #409eff;
  color: #409eff;
}

.editor-content {
  padding: 12px;
  min-height: 180px;
  outline: none;
  line-height: 1.6;
}

.rich-editor-container.focused {
  border-color: #409eff;
}

.news-detail {
  padding: 20px 0;
}

.news-detail h2 {
  margin: 0 0 16px 0;
  color: #303133;
}

.news-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
  font-size: 14px;
  color: #909399;
}

.news-summary {
  margin-bottom: 16px;
  line-height: 1.6;
}

.news-image {
  margin-bottom: 16px;
}

.news-content {
  line-height: 1.6;
}

.content-html {
  margin-top: 10px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>

