<template>
  <div class="course-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-reading"></i>
          课程管理
        </h1>
        <p class="page-description">管理培训课程和学习资源</p>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-card class="search-card">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="课程名称">
            <el-input
              v-model="searchForm.title"
              placeholder="请输入课程名称"
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
          <el-form-item label="难度">
            <el-select v-model="searchForm.difficulty" placeholder="请选择难度" clearable style="width: 120px">
              <el-option label="初级" :value="1" />
              <el-option label="中级" :value="2" />
              <el-option label="高级" :value="3" />
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
          新增课程
        </el-button>
        <el-button 
          type="danger" 
          @click="handleBatchDelete"
          :disabled="selectedCourses.length === 0"
        >
          <i class="el-icon-delete"></i>
          批量删除
        </el-button>
        <el-button 
          type="success" 
          @click="handleBatchPublish"
          :disabled="selectedCourses.length === 0"
        >
          <i class="el-icon-upload"></i>
          批量发布
        </el-button>
        <el-button type="info" @click="handleImport">
          <i class="el-icon-upload2"></i>
          导入课程
        </el-button>
      </div>
    </div>

    <!-- 课程列表 -->
    <div class="table-section">
      <el-card class="table-card">
        <el-table
          :data="courseList"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column label="课程信息" min-width="250">
            <template #default="{ row }">
              <div class="course-info">
                <div class="course-cover">
                  <img :src="row.coverImage || '/default-course.jpg'" alt="课程封面" />
                </div>
                <div class="course-details">
                  <div class="course-title">{{ row.title }}</div>
                  <div class="course-meta">
                    <el-tag size="small" :type="getDifficultyType(row.difficulty)">
                      {{ getDifficultyText(row.difficulty) }}
                    </el-tag>
                    <span class="course-duration">{{ row.duration }}分钟</span>
                    <span class="course-price">{{ row.price > 0 ? `¥${row.price}` : '免费' }}</span>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="category" label="分类" width="100" />
          <el-table-column prop="instructorName" label="讲师" width="100" />
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="studentCount" label="学员数" width="80" />
          <el-table-column prop="rating" label="评分" width="80">
            <template #default="{ row }">
              <el-rate
                v-model="row.rating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
              />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleView(row)">
                <i class="el-icon-view"></i>
                查看
              </el-button>
              <el-button size="small" @click="handleEdit(row)">
                <i class="el-icon-edit"></i>
                编辑
              </el-button>
              <el-button size="small" type="warning" @click="handleChapters(row)">
                <i class="el-icon-menu"></i>
                章节
              </el-button>
              <el-dropdown @command="(command) => handleDropdownCommand(command, row)">
                <el-button size="small" type="info">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item v-if="row.status === 0" :command="`publish-${row.id}`">
                      发布课程
                    </el-dropdown-item>
                    <el-dropdown-item v-if="row.status === 1" :command="`unpublish-${row.id}`">
                      下架课程
                    </el-dropdown-item>
                    <el-dropdown-item :command="`students-${row.id}`">
                      学员管理
                    </el-dropdown-item>
                    <el-dropdown-item :command="`statistics-${row.id}`">
                      学习统计
                    </el-dropdown-item>
                    <el-dropdown-item :command="`copy-${row.id}`" divided>
                      复制课程
                    </el-dropdown-item>
                    <el-dropdown-item :command="`delete-${row.id}`">
                      删除课程
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

    <!-- 课程表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="900px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="courseFormRef"
        :model="courseForm"
        :rules="courseFormRules"
        label-width="100px"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-form-item label="课程名称" prop="title">
              <el-input v-model="courseForm.title" placeholder="请输入课程名称" />
            </el-form-item>
            <el-form-item label="课程简介" prop="description">
              <el-input
                v-model="courseForm.description"
                type="textarea"
                :rows="3"
                placeholder="请输入课程简介"
              />
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="分类" prop="category">
                  <el-select v-model="courseForm.category" placeholder="请选择分类" style="width: 100%">
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
                <el-form-item label="难度" prop="difficulty">
                  <el-select v-model="courseForm.difficulty" placeholder="请选择难度" style="width: 100%">
                    <el-option label="初级" :value="1" />
                    <el-option label="中级" :value="2" />
                    <el-option label="高级" :value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="课程时长" prop="duration">
                  <el-input-number v-model="courseForm.duration" :min="1" placeholder="分钟" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="课程价格" prop="price">
                  <el-input-number v-model="courseForm.price" :min="0" :precision="2" placeholder="元" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="讲师" prop="instructorId">
              <el-select v-model="courseForm.instructorId" placeholder="请选择讲师" style="width: 100%">
                <el-option
                  v-for="instructor in instructorList"
                  :key="instructor.id"
                  :label="instructor.name"
                  :value="instructor.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="课程封面">
              <el-upload
                class="cover-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload"
              >
                <img v-if="courseForm.coverImage" :src="courseForm.coverImage" class="cover-image" />
                <i v-else class="el-icon-plus cover-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="详细内容" name="content">
            <el-form-item label="课程目标" prop="objectives">
              <el-input
                v-model="courseForm.objectives"
                type="textarea"
                :rows="4"
                placeholder="请输入课程学习目标"
              />
            </el-form-item>
            <el-form-item label="适用人群" prop="targetAudience">
              <el-input
                v-model="courseForm.targetAudience"
                type="textarea"
                :rows="3"
                placeholder="请输入适用人群"
              />
            </el-form-item>
            <el-form-item label="课程内容" prop="content">
              <el-input
                v-model="courseForm.content"
                type="textarea"
                :rows="8"
                placeholder="请输入详细的课程内容"
              />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="设置" name="settings">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="是否免费">
                  <el-switch v-model="courseForm.isFree" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="是否推荐">
                  <el-switch v-model="courseForm.isRecommended" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="允许试看">
                  <el-switch v-model="courseForm.allowPreview" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="标签">
              <el-input v-model="courseForm.tags" placeholder="请输入标签，多个标签用逗号分隔" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="courseForm.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button @click="handleSaveDraft" :loading="submitLoading">
            保存草稿
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ courseForm.id ? '更新' : '发布' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 课程详情对话框 -->
    <el-dialog
      title="课程详情"
      v-model="viewDialogVisible"
      width="800px"
    >
      <div v-if="currentCourse" class="course-detail">
        <div class="detail-header">
          <img :src="currentCourse.coverImage || '/default-course.jpg'" class="detail-cover" />
          <div class="detail-info">
            <h2>{{ currentCourse.title }}</h2>
            <div class="detail-meta">
              <el-tag :type="getDifficultyType(currentCourse.difficulty)">
                {{ getDifficultyText(currentCourse.difficulty) }}
              </el-tag>
              <span>{{ currentCourse.category }}</span>
              <span>{{ currentCourse.duration }}分钟</span>
              <span>{{ currentCourse.price > 0 ? `¥${currentCourse.price}` : '免费' }}</span>
            </div>
            <div class="detail-stats">
              <span>学员：{{ currentCourse.studentCount }}人</span>
              <span>评分：{{ currentCourse.rating }}分</span>
            </div>
          </div>
        </div>
        <div class="detail-content">
          <h3>课程简介</h3>
          <p>{{ currentCourse.description }}</p>
          <h3>学习目标</h3>
          <p>{{ currentCourse.objectives }}</p>
          <h3>适用人群</h3>
          <p>{{ currentCourse.targetAudience }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CourseManagement',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const courseList = ref([])
    const selectedCourses = ref([])
    const categoryList = ref(['技术培训', '管理培训', '职业技能', '软技能', '行业知识'])
    const instructorList = ref([
      { id: 1, name: '张教授' },
      { id: 2, name: '李老师' },
      { id: 3, name: '王专家' }
    ])
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogTitle = ref('')
    const courseFormRef = ref(null)
    const currentCourse = ref(null)
    const activeTab = ref('basic')

    // 搜索表单
    const searchForm = reactive({
      title: '',
      category: '',
      status: null,
      difficulty: null
    })

    // 分页信息
    const pagination = reactive({
      page: 1,
      size: 10,
      total: 0
    })

    // 课程表单
    const courseForm = reactive({
      id: null,
      title: '',
      description: '',
      content: '',
      category: '',
      difficulty: 1,
      duration: 60,
      price: 0,
      instructorId: null,
      coverImage: '',
      objectives: '',
      targetAudience: '',
      tags: '',
      isFree: true,
      isRecommended: false,
      allowPreview: true,
      remark: ''
    })

    // 表单验证规则
    const courseFormRules = {
      title: [
        { required: true, message: '请输入课程名称', trigger: 'blur' }
      ],
      description: [
        { required: true, message: '请输入课程简介', trigger: 'blur' }
      ],
      category: [
        { required: true, message: '请选择分类', trigger: 'change' }
      ],
      difficulty: [
        { required: true, message: '请选择难度', trigger: 'change' }
      ],
      instructorId: [
        { required: true, message: '请选择讲师', trigger: 'change' }
      ]
    }

    // 模拟课程数据
    const mockCourses = [
      {
        id: 1,
        title: 'Vue.js 3.0 从入门到精通',
        description: '全面学习Vue.js 3.0框架，掌握现代前端开发技能',
        category: '技术培训',
        difficulty: 2,
        duration: 480,
        price: 299,
        instructorId: 1,
        instructorName: '张教授',
        coverImage: '/course1.jpg',
        status: 1,
        studentCount: 1250,
        rating: 4.8,
        objectives: '掌握Vue.js 3.0核心概念和API，能够独立开发中大型前端项目',
        targetAudience: '有一定JavaScript基础的前端开发者',
        createTime: '2024-06-15 10:00:00'
      },
      {
        id: 2,
        title: '项目管理实战训练营',
        description: '系统学习项目管理理论与实践，提升项目管理能力',
        category: '管理培训',
        difficulty: 3,
        duration: 720,
        price: 599,
        instructorId: 2,
        instructorName: '李老师',
        coverImage: '/course2.jpg',
        status: 1,
        studentCount: 890,
        rating: 4.6,
        objectives: '掌握项目管理核心方法论，具备独立管理中大型项目的能力',
        targetAudience: '项目经理、团队负责人、有志于从事项目管理的人员',
        createTime: '2024-06-15 11:00:00'
      }
    ]

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

    // 获取课程列表
    const getCourseList = async () => {
      loading.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 500))
        courseList.value = mockCourses
        pagination.total = mockCourses.length
      } catch (error) {
        ElMessage.error('获取课程列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.page = 1
      getCourseList()
    }

    // 重置搜索
    const handleReset = () => {
      Object.assign(searchForm, {
        title: '',
        category: '',
        status: null,
        difficulty: null
      })
      pagination.page = 1
      getCourseList()
    }

    // 新增课程
    const handleAdd = () => {
      dialogTitle.value = '新增课程'
      resetCourseForm()
      dialogVisible.value = true
    }

    // 编辑课程
    const handleEdit = (row) => {
      dialogTitle.value = '编辑课程'
      Object.assign(courseForm, {
        ...row,
        isFree: row.price === 0,
        isRecommended: Boolean(row.isRecommended),
        allowPreview: Boolean(row.allowPreview)
      })
      dialogVisible.value = true
    }

    // 查看课程
    const handleView = (row) => {
      currentCourse.value = row
      viewDialogVisible.value = true
    }

    // 章节管理
    const handleChapters = (row) => {
      ElMessage.info('章节管理功能开发中...')
    }

    // 下拉菜单命令处理
    const handleDropdownCommand = async (command, row) => {
      const [action, id] = command.split('-')
      
      try {
        switch (action) {
          case 'publish':
            ElMessage.success('课程发布成功')
            break
          case 'unpublish':
            ElMessage.success('课程下架成功')
            break
          case 'students':
            ElMessage.info('学员管理功能开发中...')
            break
          case 'statistics':
            ElMessage.info('学习统计功能开发中...')
            break
          case 'copy':
            ElMessage.success('课程复制成功')
            break
          case 'delete':
            await ElMessageBox.confirm(
              `确定要删除课程 "${row.title}" 吗？`,
              '删除确认',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            ElMessage.success('删除成功')
            break
        }
        getCourseList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败')
        }
      }
    }

    // 批量删除
    const handleBatchDelete = async () => {
      try {
        await ElMessageBox.confirm(
          `确定要删除选中的 ${selectedCourses.value.length} 门课程吗？`,
          '批量删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        ElMessage.success('批量删除成功')
        getCourseList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('批量删除失败')
        }
      }
    }

    // 批量发布
    const handleBatchPublish = async () => {
      try {
        const draftCourses = selectedCourses.value.filter(course => course.status === 0)
        if (draftCourses.length === 0) {
          ElMessage.warning('请选择草稿状态的课程')
          return
        }
        
        await ElMessageBox.confirm(
          `确定要发布选中的 ${draftCourses.length} 门草稿课程吗？`,
          '批量发布确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        ElMessage.success('批量发布成功')
        getCourseList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('批量发布失败')
        }
      }
    }

    // 导入课程
    const handleImport = () => {
      ElMessage.info('课程导入功能开发中...')
    }

    // 封面上传成功
    const handleCoverSuccess = (response, file) => {
      courseForm.coverImage = URL.createObjectURL(file.raw)
    }

    // 封面上传前验证
    const beforeCoverUpload = (file) => {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
      }
      if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }

    // 选择变化
    const handleSelectionChange = (selection) => {
      selectedCourses.value = selection
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.page = 1
      getCourseList()
    }

    // 当前页变化
    const handleCurrentChange = (page) => {
      pagination.page = page
      getCourseList()
    }

    // 保存草稿
    const handleSaveDraft = async () => {
      try {
        await courseFormRef.value.validate()
        submitLoading.value = true
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success('草稿保存成功')
        dialogVisible.value = false
        getCourseList()
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
        await courseFormRef.value.validate()
        submitLoading.value = true
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success(courseForm.id ? '更新成功' : '发布成功')
        dialogVisible.value = false
        getCourseList()
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
      resetCourseForm()
    }

    // 重置表单
    const resetCourseForm = () => {
      Object.assign(courseForm, {
        id: null,
        title: '',
        description: '',
        content: '',
        category: '',
        difficulty: 1,
        duration: 60,
        price: 0,
        instructorId: null,
        coverImage: '',
        objectives: '',
        targetAudience: '',
        tags: '',
        isFree: true,
        isRecommended: false,
        allowPreview: true,
        remark: ''
      })
      activeTab.value = 'basic'
      courseFormRef.value?.clearValidate()
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    // 组件挂载时获取数据
    onMounted(() => {
      getCourseList()
    })

    return {
      loading,
      submitLoading,
      courseList,
      selectedCourses,
      categoryList,
      instructorList,
      dialogVisible,
      viewDialogVisible,
      dialogTitle,
      courseFormRef,
      currentCourse,
      activeTab,
      searchForm,
      pagination,
      courseForm,
      courseFormRules,
      getStatusType,
      getStatusText,
      getDifficultyType,
      getDifficultyText,
      getCourseList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleView,
      handleChapters,
      handleDropdownCommand,
      handleBatchDelete,
      handleBatchPublish,
      handleImport,
      handleCoverSuccess,
      beforeCoverUpload,
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
.course-management {
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

.course-info {
  display: flex;
  gap: 12px;
  align-items: center;
}

.course-cover {
  width: 80px;
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

.course-details {
  flex: 1;
}

.course-title {
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.course-meta {
  display: flex;
  gap: 8px;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.course-duration,
.course-price {
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
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

.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-uploader:hover {
  border-color: #409eff;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.course-detail {
  padding: 20px 0;
}

.detail-header {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.detail-cover {
  width: 200px;
  height: 120px;
  border-radius: 8px;
  object-fit: cover;
}

.detail-info h2 {
  margin: 0 0 12px 0;
  color: #303133;
}

.detail-meta {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 12px;
}

.detail-stats {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 14px;
}

.detail-content h3 {
  margin: 20px 0 12px 0;
  color: #303133;
  font-size: 16px;
}

.detail-content p {
  line-height: 1.6;
  color: #606266;
  margin-bottom: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .course-management {
    padding: 10px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .course-info {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .detail-header {
    flex-direction: column;
  }
  
  .detail-cover {
    width: 100%;
    max-width: 300px;
  }
}
</style>

