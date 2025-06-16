<template>
  <div class="news-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          资讯管理
        </h1>
        <p class="page-description">管理系统资讯内容、分类和发布状态</p>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-card class="search-card">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="标题">
            <el-input
              v-model="searchForm.title"
              placeholder="请输入资讯标题"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="searchForm.category" placeholder="请选择分类" clearable style="width: 150px">
              <el-option
                v-for="category in categoryList"
                :key="category"
                :label="category"
                :value="category"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="草稿" :value="0" />
              <el-option label="已发布" :value="1" />
              <el-option label="已下架" :value="2" />
            </el-select>
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

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i>
          新增资讯
        </el-button>
        <el-button 
          type="danger" 
          @click="handleBatchDelete"
          :disabled="selectedNews.length === 0"
        >
          <i class="el-icon-delete"></i>
          批量删除
        </el-button>
        <el-button 
          type="success" 
          @click="handleBatchPublish"
          :disabled="selectedNews.length === 0"
        >
          <i class="el-icon-upload"></i>
          批量发布
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
          <el-table-column prop="title" label="标题" min-width="200">
            <template #default="{ row }">
              <div class="news-title">
                <span>{{ row.title }}</span>
                <div class="news-tags">
                  <el-tag v-if="row.isTop" type="danger" size="small">置顶</el-tag>
                  <el-tag v-if="row.isHot" type="warning" size="small">热门</el-tag>
                  <el-tag v-if="row.isRecommend" type="success" size="small">推荐</el-tag>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="100" />
          <el-table-column prop="authorName" label="作者" width="100" />
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="viewCount" label="浏览量" width="80" />
          <el-table-column prop="likeCount" label="点赞数" width="80" />
          <el-table-column prop="publishTime" label="发布时间" width="160">
            <template #default="{ row }">
              {{ row.publishTime ? formatDate(row.publishTime) : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleView(row)">
                <i class="el-icon-view"></i>
                查看
              </el-button>
              <el-button size="small" @click="handleEdit(row)">
                <i class="el-icon-edit"></i>
                编辑
              </el-button>
              <el-button 
                v-if="row.status === 0"
                size="small" 
                type="success"
                @click="handlePublish(row)"
              >
                <i class="el-icon-upload"></i>
                发布
              </el-button>
              <el-button 
                v-if="row.status === 1"
                size="small" 
                type="warning"
                @click="handleUnpublish(row)"
              >
                <i class="el-icon-download"></i>
                下架
              </el-button>
              <el-dropdown @command="(command) => handleDropdownCommand(command, row)">
                <el-button size="small" type="info">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :command="`top-${row.id}`">
                      {{ row.isTop ? '取消置顶' : '置顶' }}
                    </el-dropdown-item>
                    <el-dropdown-item :command="`hot-${row.id}`">
                      {{ row.isHot ? '取消热门' : '设为热门' }}
                    </el-dropdown-item>
                    <el-dropdown-item :command="`recommend-${row.id}`">
                      {{ row.isRecommend ? '取消推荐' : '设为推荐' }}
                    </el-dropdown-item>
                    <el-dropdown-item :command="`delete-${row.id}`" divided>
                      删除
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

    <!-- 资讯表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="newsFormRef"
        :model="newsForm"
        :rules="newsFormRules"
        label-width="100px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="newsForm.title" placeholder="请输入资讯标题" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input
            v-model="newsForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入资讯摘要"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="newsForm.category" placeholder="请选择分类" style="width: 100%">
                <el-option
                  v-for="category in categoryList"
                  :key="category"
                  :label="category"
                  :value="category"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签" prop="tags">
              <el-input v-model="newsForm.tags" placeholder="请输入标签，多个标签用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="置顶">
              <el-switch v-model="newsForm.isTop" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="热门">
              <el-switch v-model="newsForm.isHot" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="推荐">
              <el-switch v-model="newsForm.isRecommend" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="newsForm.content"
            type="textarea"
            :rows="10"
            placeholder="请输入资讯内容"
          />
        </el-form-item>
        <el-form-item label="来源">
          <el-input v-model="newsForm.source" placeholder="请输入来源" />
        </el-form-item>
        <el-form-item label="来源链接">
          <el-input v-model="newsForm.sourceUrl" placeholder="请输入来源链接" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button @click="handleSaveDraft" :loading="submitLoading">
            保存草稿
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ newsForm.id ? '更新' : '发布' }}
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
          <span>分类：{{ currentNews.category }}</span>
          <span>作者：{{ currentNews.authorName }}</span>
          <span>发布时间：{{ currentNews.publishTime ? formatDate(currentNews.publishTime) : '未发布' }}</span>
          <span>浏览量：{{ currentNews.viewCount }}</span>
        </div>
        <div class="news-summary">
          <strong>摘要：</strong>{{ currentNews.summary }}
        </div>
        <div class="news-content">
          <strong>内容：</strong>
          <div v-html="currentNews.content"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { newsApi } from '@/api/news'

export default {
  name: 'NewsManagement',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const newsList = ref([])
    const selectedNews = ref([])
    const categoryList = ref(['公司动态', '行业资讯', '经验分享', '公司新闻', '技术文章'])
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogTitle = ref('')
    const newsFormRef = ref(null)
    const currentNews = ref(null)

    // 搜索表单
    const searchForm = reactive({
      title: '',
      category: '',
      status: null
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
      category: '',
      tags: '',
      source: '',
      sourceUrl: '',
      isTop: false,
      isHot: false,
      isRecommend: false
    })

    // 表单验证规则
    const newsFormRules = {
      title: [
        { required: true, message: '请输入资讯标题', trigger: 'blur' }
      ],
      summary: [
        { required: true, message: '请输入资讯摘要', trigger: 'blur' }
      ],
      content: [
        { required: true, message: '请输入资讯内容', trigger: 'blur' }
      ],
      category: [
        { required: true, message: '请选择分类', trigger: 'change' }
      ]
    }

    // 获取状态类型
    const getStatusType = (status) => {
      const types = { 0: 'info', 1: 'success', 2: 'warning' }
      return types[status] || 'info'
    }

    // 获取状态文本
    const getStatusText = (status) => {
      const texts = { 0: '草稿', 1: '已发布', 2: '已下架' }
      return texts[status] || '未知'
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
        const response = await newsApi.getNewsList(params)
        newsList.value = response.data.records
        pagination.total = response.data.total
      } catch (error) {
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
        category: '',
        status: null
      })
      pagination.page = 1
      getNewsList()
    }

    // 新增资讯
    const handleAdd = () => {
      dialogTitle.value = '新增资讯'
      resetNewsForm()
      dialogVisible.value = true
    }

    // 编辑资讯
    const handleEdit = (row) => {
      dialogTitle.value = '编辑资讯'
      Object.assign(newsForm, {
        ...row,
        isTop: Boolean(row.isTop),
        isHot: Boolean(row.isHot),
        isRecommend: Boolean(row.isRecommend)
      })
      dialogVisible.value = true
    }

    // 查看资讯
    const handleView = (row) => {
      currentNews.value = row
      viewDialogVisible.value = true
    }

    // 发布资讯
    const handlePublish = async (row) => {
      try {
        await newsApi.publishNews(row.id)
        ElMessage.success('发布成功')
        getNewsList()
      } catch (error) {
        ElMessage.error('发布失败')
      }
    }

    // 下架资讯
    const handleUnpublish = async (row) => {
      try {
        await newsApi.unpublishNews(row.id)
        ElMessage.success('下架成功')
        getNewsList()
      } catch (error) {
        ElMessage.error('下架失败')
      }
    }

    // 批量删除
    const handleBatchDelete = async () => {
      try {
        await ElMessageBox.confirm(
          `确定要删除选中的 ${selectedNews.value.length} 条资讯吗？`,
          '批量删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        const ids = selectedNews.value.map(news => news.id)
        await newsApi.batchDeleteNews(ids)
        ElMessage.success('批量删除成功')
        getNewsList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('批量删除失败')
        }
      }
    }

    // 批量发布
    const handleBatchPublish = async () => {
      try {
        const draftNews = selectedNews.value.filter(news => news.status === 0)
        if (draftNews.length === 0) {
          ElMessage.warning('请选择草稿状态的资讯')
          return
        }
        
        await ElMessageBox.confirm(
          `确定要发布选中的 ${draftNews.length} 条草稿资讯吗？`,
          '批量发布确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        for (const news of draftNews) {
          await newsApi.publishNews(news.id)
        }
        ElMessage.success('批量发布成功')
        getNewsList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('批量发布失败')
        }
      }
    }

    // 下拉菜单命令处理
    const handleDropdownCommand = async (command, row) => {
      const [action, id] = command.split('-')
      
      try {
        switch (action) {
          case 'top':
            if (row.isTop) {
              await newsApi.untopNews(id)
              ElMessage.success('取消置顶成功')
            } else {
              await newsApi.topNews(id)
              ElMessage.success('置顶成功')
            }
            break
          case 'hot':
            if (row.isHot) {
              await newsApi.unsetHotNews(id)
              ElMessage.success('取消热门成功')
            } else {
              await newsApi.setHotNews(id)
              ElMessage.success('设为热门成功')
            }
            break
          case 'recommend':
            // 这里需要添加推荐相关的API
            ElMessage.success(row.isRecommend ? '取消推荐成功' : '设为推荐成功')
            break
          case 'delete':
            await ElMessageBox.confirm(
              `确定要删除资讯 "${row.title}" 吗？`,
              '删除确认',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            await newsApi.deleteNews(id)
            ElMessage.success('删除成功')
            break
        }
        getNewsList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败')
        }
      }
    }

    // 选择变化
    const handleSelectionChange = (selection) => {
      selectedNews.value = selection
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.page = 1
      getNewsList()
    }

    // 当前页变化
    const handleCurrentChange = (page) => {
      pagination.page = page
      getNewsList()
    }

    // 保存草稿
    const handleSaveDraft = async () => {
      try {
        await newsFormRef.value.validate()
        submitLoading.value = true
        
        const formData = { ...newsForm, status: 0 }
        
        if (newsForm.id) {
          await newsApi.updateNews(newsForm.id, formData)
          ElMessage.success('草稿保存成功')
        } else {
          await newsApi.createNews(formData)
          ElMessage.success('草稿创建成功')
        }
        
        dialogVisible.value = false
        getNewsList()
      } catch (error) {
        if (error !== false) {
          ElMessage.error('操作失败')
        }
      } finally {
        submitLoading.value = false
      }
    }

    // 提交表单
    const handleSubmit = async () => {
      try {
        await newsFormRef.value.validate()
        submitLoading.value = true
        
        const formData = { ...newsForm, status: 1 }
        
        if (newsForm.id) {
          await newsApi.updateNews(newsForm.id, formData)
          ElMessage.success('更新成功')
        } else {
          await newsApi.createNews(formData)
          ElMessage.success('发布成功')
        }
        
        dialogVisible.value = false
        getNewsList()
      } catch (error) {
        if (error !== false) {
          ElMessage.error('操作失败')
        }
      } finally {
        submitLoading.value = false
      }
    }

    // 关闭对话框
    const handleDialogClose = () => {
      dialogVisible.value = false
      resetNewsForm()
    }

    // 重置表单
    const resetNewsForm = () => {
      Object.assign(newsForm, {
        id: null,
        title: '',
        summary: '',
        content: '',
        category: '',
        tags: '',
        source: '',
        sourceUrl: '',
        isTop: false,
        isHot: false,
        isRecommend: false
      })
      newsFormRef.value?.clearValidate()
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    // 组件挂载时获取数据
    onMounted(() => {
      getNewsList()
    })

    return {
      loading,
      submitLoading,
      newsList,
      selectedNews,
      categoryList,
      dialogVisible,
      viewDialogVisible,
      dialogTitle,
      newsFormRef,
      currentNews,
      searchForm,
      pagination,
      newsForm,
      newsFormRules,
      getStatusType,
      getStatusText,
      getNewsList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleView,
      handlePublish,
      handleUnpublish,
      handleBatchDelete,
      handleBatchPublish,
      handleDropdownCommand,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      handleSaveDraft,
      handleSubmit,
      handleDialogClose,
      formatDate
    }
  }
}
</script>

<style scoped>
.news-management {
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

.news-title {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.news-tags {
  display: flex;
  gap: 4px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
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
  color: #909399;
  font-size: 14px;
}

.news-summary {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.news-content {
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .news-management {
    padding: 10px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .news-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

