<template>
  <div class="news-form modern-page">
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
          <h1 class="page-title">{{ isEdit ? '编辑资讯' : '发布资讯' }}</h1>
        </div>
        <div class="header-actions">
          <el-button 
            @click="saveDraft"
            class="modern-btn secondary"
          >
            保存草稿
          </el-button>
          <el-button 
            type="primary" 
            @click="submitForm"
            :loading="submitting"
            class="modern-btn primary"
          >
            {{ isEdit ? '更新资讯' : '发布资讯' }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 表单内容 -->
    <div class="form-container">
      <el-form 
        ref="formRef" 
        :model="formData" 
        :rules="formRules" 
        label-width="120px"
        class="news-form-content"
      >
        <!-- 基本信息 -->
        <div class="modern-card">
          <div class="card-header">
            <h2>基本信息</h2>
          </div>
          <div class="card-body">
            <el-form-item label="资讯标题" prop="title">
              <el-input
                v-model="formData.title"
                placeholder="请输入资讯标题"
                maxlength="100"
                show-word-limit
                class="modern-input"
              />
            </el-form-item>

            <el-select
                v-model="formData.tenantId"
                placeholder="请选择所属企业"
                v-if="tenantList.length > 0"
                class="modern-select"
            >
              <el-option v-for="item in tenantList" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>

            <el-select
                v-model="formData.categoryId"
                placeholder="请选择资讯分类"
                v-if="categoryList.length > 0"
                class="modern-select"
            >
              <el-option v-for="item in categoryList" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>



            <el-form-item label="发布状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio :label="0">草稿</el-radio>
                <el-radio :label="1">立即发布</el-radio>
                <el-radio :label="2">定时发布</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item 
              label="发布时间" 
              prop="publishTime"
              v-if="formData.status === 2"
            >
              <el-date-picker
                v-model="formData.publishTime"
                type="datetime"
                placeholder="选择发布时间"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DD HH:mm:ss"
                class="modern-input"
                style="width: 100%"
              />
            </el-form-item>

            <el-form-item label="资讯摘要" prop="summary">
              <el-input
                v-model="formData.summary"
                type="textarea"
                :rows="3"
                placeholder="请输入资讯摘要，将显示在列表页"
                maxlength="200"
                show-word-limit
                class="modern-input"
              />
            </el-form-item>

            <el-form-item label="封面图片">
              <div class="cover-upload">
                <el-upload
                  class="cover-uploader"
                  action="#"
                  :show-file-list="false"
                  :before-upload="beforeCoverUpload"
                  :http-request="uploadCover"
                >
                  <img v-if="formData.coverImage" :src="formData.coverImage" class="cover-image" />
                  <div v-else class="cover-placeholder">
                    <el-icon class="cover-icon"><Plus /></el-icon>
                    <div class="cover-text">上传封面</div>
                  </div>
                </el-upload>
                <div class="cover-tips">
                  建议尺寸：800x450px，支持 JPG、PNG 格式，大小不超过 2MB
                </div>
              </div>
            </el-form-item>
          </div>
        </div>

        <!-- 内容编辑 -->
        <div class="modern-card">
          <div class="card-header">
            <h2>资讯内容</h2>
          </div>
          <div class="card-body">
            <el-form-item prop="content">
              <div class="editor-container">
                <div class="editor-toolbar">
                  <el-button-group>
                    <el-button size="small" @click="insertImage">
                      <el-icon><Picture /></el-icon>
                      插入图片
                    </el-button>
                    <el-button size="small" @click="insertLink">
                      <el-icon><Link /></el-icon>
                      插入链接
                    </el-button>
                    <el-button size="small" @click="insertTable">
                      <el-icon><Grid /></el-icon>
                      插入表格
                    </el-button>
                  </el-button-group>
                </div>
                <el-input
                  v-model="formData.content"
                  type="textarea"
                  :rows="15"
                  placeholder="请输入资讯内容，支持 HTML 格式"
                  class="modern-input content-editor"
                />
              </div>
            </el-form-item>
          </div>
        </div>

        <!-- 高级设置 -->
        <div class="modern-card">
          <div class="card-header">
            <h2>高级设置</h2>
          </div>
          <div class="card-body">
            <el-form-item label="标签">
              <el-tag
                v-for="tag in formData.tags"
                :key="tag"
                closable
                @close="removeTag(tag)"
                class="tag-item"
              >
                {{ tag }}
              </el-tag>
              <el-input
                v-if="tagInputVisible"
                ref="tagInputRef"
                v-model="tagInputValue"
                size="small"
                @keyup.enter="addTag"
                @blur="addTag"
                class="tag-input"
              />
              <el-button
                v-else
                size="small"
                @click="showTagInput"
                class="add-tag-btn"
              >
                <el-icon><Plus /></el-icon>
                添加标签
              </el-button>
            </el-form-item>

            <el-form-item label="SEO关键词">
              <el-input
                v-model="formData.keywords"
                placeholder="请输入关键词，用逗号分隔"
                class="modern-input"
              />
            </el-form-item>

            <el-form-item label="阅读权限">
              <el-select
                v-model="formData.permission"
                placeholder="请选择阅读权限"
                class="modern-input"
                style="width: 100%"
              >
                <el-option label="所有人可见" value="public" />
                <el-option label="仅内部员工" value="internal" />
                <el-option label="指定部门" value="department" />
                <el-option label="指定用户" value="user" />
              </el-select>
            </el-form-item>

            <el-form-item label="评论设置">
              <el-checkbox-group v-model="formData.commentSettings">
                <el-checkbox label="allowComment">允许评论</el-checkbox>
                <el-checkbox label="requireApproval">评论需审核</el-checkbox>
                <el-checkbox label="notifyAuthor">评论通知作者</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, nextTick, computed} from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 响应式数据
const formRef = ref()
const tagInputRef = ref()
const submitting = ref(false)
const tagInputVisible = ref(false)
const tagInputValue = ref('')

const isEdit = computed(() => !!route.params.id)
const tenantList = ref([])
const categoryList = ref([])

const formData = reactive({
  title: '',
  category: '',
  status: 1,
  publishTime: '',
  summary: '',
  coverImage: '',
  content: '',
  tags: [],
  keywords: '',
  permission: 'public',
  commentSettings: ['allowComment'],
  tenantId: '',      // 新增
  categoryId: '',    // 新增
})
const loadTenantAndCategory = async () => {
  try {
    // 租户数据（格式转换）
    const tenantRes = await fetch('http://localhost:8080/api/tenants/all');
    const tenantData = await tenantRes.json();
    if (tenantData.code === 200) {
      tenantList.value = tenantData.data.map(item => ({
        value: item.id,
        label: item.tenantName
      }));
    }
    // 分类数据（格式转换）
    const categoryRes = await fetch('http://localhost:8080/api/news-categories/all');
    const categoryData = await categoryRes.json();
    if (categoryData.code === 200) {
      categoryList.value = categoryData.data.map(item => ({
        value: item.id,
        label: item.categoryName
      }));
    }
    console.log('企业列表', tenantList.value)
    console.log('分类列表', categoryList.value)
  } catch (error) {
    console.error('加载数据失败:', error);
    ElMessage.error('加载数据失败');
  }
};

// 表单验证规则
const formRules = {
  title: [
    { required: true, message: '请输入资讯标题', trigger: 'blur' },
    { min: 5, max: 100, message: '标题长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择资讯分类', trigger: 'change' }
  ],
  summary: [
    { required: true, message: '请输入资讯摘要', trigger: 'blur' },
    { max: 200, message: '摘要不能超过 200 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入资讯内容', trigger: 'blur' },
    { min: 50, message: '内容不能少于 50 个字符', trigger: 'blur' }
  ],
  publishTime: [
    { required: true, message: '请选择发布时间', trigger: 'change' }
  ],
  tenantId: [
    { required: true, message: '请选择所属企业', trigger: 'change' }
  ],
  categoryId: [
    { required: true, message: '请选择资讯分类', trigger: 'change' }
  ],
}

// 方法
const loadNewsData = async () => {
  if (isEdit.value) {
    try {
      const newsId = route.params.id
      // 这里应该调用API获取资讯详情
      // const response = await getNewsDetail(newsId)
      
      // 模拟数据
      Object.assign(formData, {
        title: '公司2024年第一季度业绩报告',
        category: 'company',
        status: 1,
        summary: '本季度公司业绩表现优异，各项指标均超预期完成...',
        content: '<p>详细的业绩报告内容...</p>',
        tags: ['业绩报告', '第一季度'],
        keywords: '业绩,报告,第一季度',
        permission: 'public',
        commentSettings: ['allowComment', 'notifyAuthor']
      })
    } catch (error) {
      ElMessage.error('加载资讯数据失败')
    }
  }
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    // 这里应该调用API提交表单
    // if (isEdit.value) {
    //   await updateNews(route.params.id, formData)
    // } else {
    //   await createNews(formData)
    // }
    
    // 模拟提交
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(isEdit.value ? '资讯更新成功' : '资讯发布成功')
    router.push('/dashboard/news')
  } catch (error) {
    if (error !== 'validation failed') {
      ElMessage.error('提交失败，请重试')
    }
  } finally {
    submitting.value = false
  }
}

const saveDraft = async () => {
  try {
    formData.status = 0
    // 这里应该调用API保存草稿
    ElMessage.success('草稿保存成功')
  } catch (error) {
    ElMessage.error('保存草稿失败')
  }
}

const beforeCoverUpload = (file) => {
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

const uploadCover = async (options) => {
  const file = options.file
  const data = new FormData()
  data.append('file', file)
  try {
    const response = await fetch('http://localhost:8080/api/upload/image', {
      method: 'POST',
      body: data
    })
    const res = await response.json()
    if (res.code === 200) {
      formData.coverImage = res.data.url
      ElMessage.success('图片上传成功')
    } else {
      ElMessage.error(res.message || '图片上传失败')
    }
  } catch (e) {
    ElMessage.error('图片上传失败')
  }
}

const insertImage = () => {
  ElMessage.info('插入图片功能开发中')
}

const insertLink = () => {
  ElMessage.info('插入链接功能开发中')
}

const insertTable = () => {
  ElMessage.info('插入表格功能开发中')
}

const removeTag = (tag) => {
  const index = formData.tags.indexOf(tag)
  if (index > -1) {
    formData.tags.splice(index, 1)
  }
}

const showTagInput = () => {
  tagInputVisible.value = true
  nextTick(() => {
    tagInputRef.value?.focus()
  })
}

const addTag = () => {
  const value = tagInputValue.value.trim()
  if (value && !formData.tags.includes(value)) {
    formData.tags.push(value)
  }
  tagInputVisible.value = false
  tagInputValue.value = ''
}

onMounted(() => {
  loadTenantAndCategory()
  loadNewsData()
})
</script>

<style scoped>
.news-form {
  padding: var(--spacing-2xl);
  max-width: 1000px;
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

.form-container {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-2xl);
}

.card-header {
  margin-bottom: var(--spacing-lg);
}

.card-header h2 {
  font-size: var(--font-size-xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.cover-upload {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.cover-uploader {
  display: inline-block;
}

.cover-image {
  width: 200px;
  height: 112px;
  object-fit: cover;
  border-radius: var(--radius-lg);
  border: 2px solid var(--border-normal);
}

.cover-placeholder {
  width: 200px;
  height: 112px;
  border: 2px dashed var(--border-normal);
  border-radius: var(--radius-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.cover-placeholder:hover {
  border-color: var(--primary-color);
  background: var(--bg-secondary);
}

.cover-icon {
  font-size: var(--font-size-2xl);
  color: var(--text-tertiary);
  margin-bottom: var(--spacing-sm);
}

.cover-text {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.cover-tips {
  font-size: var(--font-size-xs);
  color: var(--text-tertiary);
}

.editor-container {
  border: 1px solid var(--border-normal);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.editor-toolbar {
  padding: var(--spacing-md);
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-normal);
}

.content-editor {
  border: none !important;
  border-radius: 0 !important;
}

.content-editor :deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: var(--font-size-sm);
  line-height: 1.6;
}

.tag-item {
  margin-right: var(--spacing-sm);
  margin-bottom: var(--spacing-sm);
}

.tag-input {
  width: 100px;
  margin-right: var(--spacing-sm);
}

.add-tag-btn {
  border-style: dashed;
}

@media (max-width: 768px) {
  .news-form {
    padding: var(--spacing-lg);
  }
  
  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: flex-start;
  }
  
  .cover-image,
  .cover-placeholder {
    width: 100%;
    max-width: 300px;
  }
}
</style>

