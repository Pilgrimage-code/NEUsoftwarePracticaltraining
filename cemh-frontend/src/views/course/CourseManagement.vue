<template>
  <div class="course-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon><Reading /></el-icon>
          课程管理
        </h1>
        <p class="page-description">管理系统课程内容、分类和状态</p>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="课程名称">
          <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable />
        </el-form-item>
        <el-form-item label="课程排序">
          <el-input-number v-model="queryParams.courseOrder" placeholder="请输入课程排序" :min="1" clearable />
        </el-form-item>
        <el-form-item label="课程作者">
          <el-input v-model="queryParams.courseAuthor" placeholder="请输入课程作者" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-container">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增
      </el-button>
      <el-button type="success" @click="handleExport">
        <el-icon><Download /></el-icon>导出
      </el-button>
      <el-button type="warning" @click="navigateToCourseReview">
        <el-icon><View /></el-icon>课程审核管理
      </el-button>
    </div>

    <!-- 表格区域 -->
    <el-table
      v-loading="loading"
      :data="courseList"
      border
      style="width: 100%"
      row-key="id"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="courseName" label="课程名称" min-width="150" show-overflow-tooltip />
      <el-table-column label="课程封面" width="100">
        <template #default="scope">
          <el-image
            v-if="scope.row.coverImage"
            :src="scope.row.coverImage"
            :preview-src-list="[scope.row.coverImage]"
            :preview-teleported="true"
            :z-index="3000"
            fit="cover"
            style="width: 60px; height: 40px; border-radius: 4px; cursor: pointer;"
          />
          <span v-else>无封面</span>
        </template>
      </el-table-column>
      <el-table-column prop="courseAuthor" label="课程作者" width="120" show-overflow-tooltip />
      <el-table-column prop="courseOrder" label="排序" width="80" align="center" />
      <el-table-column prop="viewCount" label="浏览次数" width="100" align="center" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="审核状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="getReviewStatusType(scope.row.remark)">
            {{ getReviewStatusText(scope.row.remark) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" show-overflow-tooltip />
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="scope">
          <el-button type="primary" link @click="handleUpdate(scope.row)">修改</el-button>
          <el-button type="primary" link @click="handleViewChapters(scope.row)">章节</el-button>
          <el-popconfirm
            title="确定要删除该课程吗？"
            @confirm="handleDelete(scope.row)"
          >
            <template #reference>
              <el-button type="danger" link>删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 课程表单对话框 -->
    <el-dialog
      :title="formTitle"
      v-model="dialogVisible"
      width="700px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <course-form
        :course-id="selectedCourseId"
        :is-edit="isEdit"
        @success="handleFormSuccess"
        @cancel="dialogVisible = false"
      />
    </el-dialog>

    <!-- 章节管理对话框 -->
    <el-dialog
      title="章节管理"
      v-model="chapterDialogVisible"
      width="900px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div v-if="currentCourse" class="chapter-header">
        <h3>{{ currentCourse.courseName }}</h3>
        <p>{{ currentCourse.courseIntro }}</p>
      </div>

      <div class="chapter-actions">
        <el-button type="primary" @click="handleAddChapter">
          <el-icon><Plus /></el-icon>添加章节
        </el-button>
      </div>

      <el-table
        v-loading="chapterLoading"
        :data="chapterList"
        border
        style="width: 100%"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="chapterName" label="章节名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="chapterOrder" label="排序" width="80" align="center" />
        <el-table-column prop="duration" label="时长(秒)" width="100" align="center" />
        <el-table-column label="视频" width="100" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.videoUrl" type="success">已上传</el-tag>
            <el-tag v-else type="info">未上传</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button type="success" link @click="handleViewChapter(scope.row)">观看</el-button>
            <el-button type="primary" link @click="handleEditChapter(scope.row)">修改</el-button>
            <el-popconfirm
              title="确定要删除该章节吗？"
              @confirm="handleDeleteChapter(scope.row)"
            >
              <template #reference>
                <el-button type="danger" link>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 章节表单对话框 -->
      <el-dialog
        :title="chapterFormTitle"
        v-model="chapterFormDialogVisible"
        width="600px"
        :close-on-click-modal="false"
        append-to-body
        destroy-on-close
      >
        <el-form 
          ref="chapterFormRef" 
          :model="chapterForm" 
          :rules="chapterRules" 
          label-width="100px" 
          label-position="right"
          :disabled="chapterFormLoading"
        >
          <el-form-item label="章节名称" prop="chapterName">
            <el-input v-model="chapterForm.chapterName" placeholder="请输入章节名称" />
          </el-form-item>
          
          <el-form-item label="章节排序" prop="chapterOrder">
            <el-input-number v-model="chapterForm.chapterOrder" :min="1" :max="999" />
          </el-form-item>
          
          <el-form-item label="章节描述" prop="description">
            <el-input 
              v-model="chapterForm.description" 
              type="textarea" 
              rows="3" 
              placeholder="请输入章节描述" 
            />
          </el-form-item>
          
          <el-form-item label="视频" prop="videoUrl">
            <file-upload
              v-model="chapterForm.videoUrl"
              ref="videoUpload"
              file-type="video"
              button-text="上传视频"
              :tip="'支持 mp4 格式，大小不超过 50MB'"
              :max-size="50"
              :auto-upload="true"
              :initial-file-url="chapterForm.videoUrl"
              @upload-success="handleVideoUploadSuccess"
              @upload-progress="handleVideoUploadProgress"
              @file-change="handleVideoFileChange"
            />
            <div v-if="videoUploadStatus.uploading" class="video-upload-progress">
              <el-progress 
                :percentage="videoUploadStatus.progress" 
                :status="videoUploadStatus.status"
                :stroke-width="10"
                :show-text="true"
              />
              <div class="upload-status-text">{{ videoUploadStatusText }}</div>
            </div>
          </el-form-item>
          
          <el-form-item label="视频时长" prop="duration">
            <el-input-number v-model="chapterForm.duration" :min="0" placeholder="视频时长(秒)" />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitChapterForm" :loading="chapterFormLoading">确定</el-button>
            <el-button @click="chapterFormDialogVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Refresh, Download, Reading, View } from '@element-plus/icons-vue'
import { courseApi } from '@/api/course'
import CourseForm from './CourseForm.vue'
import FileUpload from '@/components/FileUpload.vue'
import { useRouter } from 'vue-router'

export default {
  name: 'CourseManagement',
  components: {
    CourseForm,
    FileUpload,
    Search,
    Plus,
    Refresh,
    Download,
    Reading,
    View
  },
  setup() {
    // 查询参数
    const queryParams = reactive({
      pageNum: 1,
      pageSize: 10,
      courseName: '',
      courseOrder: null,
      courseAuthor: '',
      status: null
    })

    // 引入路由器
    const router = useRouter()

    // 数据列表
    const courseList = ref([])
    const total = ref(0)
    const loading = ref(false)

    // 表单相关
    const dialogVisible = ref(false)
    const isEdit = ref(false)
    const selectedCourseId = ref(null)
    const formTitle = computed(() => isEdit.value ? '修改课程' : '新增课程')

    // 章节管理相关
    const chapterDialogVisible = ref(false)
    const chapterLoading = ref(false)
    const chapterList = ref([])
    const currentCourse = ref(null)
    
    // 章节表单相关
    const chapterFormDialogVisible = ref(false)
    const chapterFormLoading = ref(false)
    const chapterFormRef = ref(null)
    const videoUpload = ref(null)
    const isEditChapter = ref(false)
    const chapterFormTitle = computed(() => isEditChapter.value ? '修改章节' : '添加章节')
    
    const chapterForm = reactive({
      id: null,
      courseId: null,
      chapterName: '',
      chapterOrder: 1,
      description: '',
      videoUrl: '',
      duration: 0
    })
    
    const chapterRules = {
      chapterName: [
        { required: true, message: '请输入章节名称', trigger: 'blur' },
        { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
      ],
      chapterOrder: [
        { required: true, message: '请输入章节排序', trigger: 'blur' }
      ],
      videoUrl: [
        { required: true, message: '请上传章节视频', trigger: 'change' }
      ]
    }

    // 视频上传状态
    const videoUploadStatus = reactive({
      uploading: false,
      progress: 0,
      status: '', // success, exception, ''
    })
    
    // 计算视频上传状态文本
    const videoUploadStatusText = computed(() => {
      if (videoUploadStatus.status === 'success') {
        return '上传成功'
      } else if (videoUploadStatus.status === 'exception') {
        return '上传失败'
      } else {
        return `上传中 ${videoUploadStatus.progress}%`
      }
    })

    // 获取课程列表
    const getList = async () => {
      loading.value = true
      try {
        const res = await courseApi.getCourseList(queryParams)
        if (res.code === 200) {
          courseList.value = res.data.records || []
          total.value = res.data.total || 0
        } else {
          ElMessage.error(res.msg || '获取课程列表失败')
        }
      } catch (error) {
        console.error('获取课程列表异常:', error)
        ElMessage.error('获取课程列表异常')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      queryParams.pageNum = 1
      getList()
    }

    // 重置
    const resetQuery = () => {
      queryParams.courseName = ''
      queryParams.courseOrder = null
      queryParams.courseAuthor = ''
      queryParams.status = null
      handleSearch()
    }

    // 新增
    const handleAdd = () => {
      isEdit.value = false
      selectedCourseId.value = null
      dialogVisible.value = true
    }

    // 修改
    const handleUpdate = (row) => {
      isEdit.value = true
      selectedCourseId.value = row.id
      dialogVisible.value = true
    }

    // 删除
    const handleDelete = async (row) => {
      try {
        const res = await courseApi.deleteCourse(row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          getList()
        } else {
          ElMessage.error(res.msg || '删除失败')
        }
      } catch (error) {
        console.error('删除课程异常:', error)
        ElMessage.error('删除课程异常')
      }
    }

    // 导出
    const handleExport = async () => {
      try {
        ElMessageBox.confirm('确认导出所有课程数据吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          loading.value = true;
          try {
            // 使用URL的方式直接下载文件
            const baseUrl = import.meta.env.VITE_APP_BASE_API || 'http://localhost:8080';
            // 构建查询参数
            const params = new URLSearchParams();
            if (queryParams.courseName) params.append('courseName', queryParams.courseName);
            if (queryParams.courseAuthor) params.append('courseAuthor', queryParams.courseAuthor);
            if (queryParams.status !== null) params.append('status', queryParams.status);
            
            // 创建下载链接
            const url = `${baseUrl}/api/courses/export?${params.toString()}`;
            // 创建一个隐藏的a标签来下载
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `课程列表_${new Date().toLocaleDateString()}.xlsx`);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            
            ElMessage.success('导出成功');
          } catch (error) {
            console.error('导出课程异常:', error);
            ElMessage.error('导出课程异常');
          } finally {
            loading.value = false;
          }
        });
      } catch (error) {
        console.error('导出确认异常:', error);
      }
    };

    // 查看章节
    const handleViewChapters = async (row) => {
      currentCourse.value = row
      chapterDialogVisible.value = true
      await getChapterList(row.id)
    }
    
    // 获取章节列表
    const getChapterList = async (courseId) => {
      if (!courseId) return
      
      chapterLoading.value = true
      try {
        const res = await courseApi.getCourseChapters(courseId)
        if (res.code === 200) {
          chapterList.value = res.data || []
        } else {
          ElMessage.error(res.msg || '获取章节列表失败')
        }
      } catch (error) {
        console.error('获取章节列表异常:', error)
        ElMessage.error('获取章节列表异常')
      } finally {
        chapterLoading.value = false
      }
    }
    
    // 添加章节
    const handleAddChapter = () => {
      isEditChapter.value = false;
      
      // 重置表单
      Object.assign(chapterForm, {
        id: null,
        courseId: currentCourse.value.id,
        chapterName: '',
        chapterOrder: chapterList.value.length + 1,
        description: '',
        videoUrl: '',
        duration: 0
      });
      
      // 确保文件上传组件状态重置
      nextTick(() => {
        if (videoUpload.value) {
          videoUpload.value.reset();
        }
      });
      
      chapterFormDialogVisible.value = true;
    };
    
    // 修改章节
    const handleEditChapter = (row) => {
      isEditChapter.value = true;
      
      // 填充表单数据
      Object.assign(chapterForm, {
        id: row.id,
        courseId: row.courseId,
        chapterName: row.chapterName,
        chapterOrder: row.chapterOrder,
        description: row.description,
        videoUrl: row.videoUrl,
        duration: row.duration
      });
      
      console.log("编辑章节，原始视频URL:", row.videoUrl);
      
      // 确保视频URL格式正确，适应前端展示
      if (chapterForm.videoUrl && !chapterForm.videoUrl.startsWith('http')) {
        // 补全视频URL
        const baseUrl = import.meta.env.VITE_APP_BASE_API || 'http://localhost:8080';
        // 如果是以/开头的相对路径
        if (chapterForm.videoUrl.startsWith('/')) {
          chapterForm.videoUrl = `${baseUrl}${chapterForm.videoUrl}`;
        } else {
          chapterForm.videoUrl = `${baseUrl}/${chapterForm.videoUrl}`;
        }
        console.log("格式化后的视频URL:", chapterForm.videoUrl);
      }
      
      chapterFormDialogVisible.value = true;
    };
    
    // 删除章节
    const handleDeleteChapter = async (row) => {
      try {
        // 使用courseApi调用删除章节API
        const res = await courseApi.deleteChapter(row.courseId, row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          getChapterList(currentCourse.value.id)
        } else {
          ElMessage.error(res.msg || '删除失败')
        }
      } catch (error) {
        console.error('删除章节异常:', error)
        ElMessage.error('删除章节异常')
      }
    }
    
    // 视频上传成功处理
    const handleVideoUploadSuccess = (data) => {
      console.log('视频上传成功:', data);
      if (data && data.url) {
        // 获取视频URL
        let videoUrl = data.url;
        console.log('返回的原始视频URL:', videoUrl);
        
        // 确保URL格式正确
        if (videoUrl.match(/^https?:\/\//)) {
          // 如果是完整URL，检查是否是日期格式文件名
          const filename = videoUrl.substring(videoUrl.lastIndexOf('/') + 1);
          if (filename.match(/\d{8}_.*\.\w+$/)) {
            // 保持URL不变，因为我们已经在前端和后端统一了处理逻辑
            console.log('日期格式文件名:', filename);
          }
        } else if (!videoUrl.startsWith('/')) {
          // 确保相对路径以斜杠开头
          videoUrl = '/' + videoUrl;
          console.log('添加前导斜杠:', videoUrl);
        }
        
        // 将视频URL更新到表单
        chapterForm.videoUrl = videoUrl;
        
        console.log('最终设置的视频URL:', chapterForm.videoUrl);
        
        // 更新上传状态
        videoUploadStatus.uploading = false;
        videoUploadStatus.progress = 100;
        videoUploadStatus.status = 'success';
        
        // 手动触发表单验证更新
        if (chapterFormRef.value) {
          chapterFormRef.value.validateField('videoUrl');
        }
      }
    };
    
    // 处理视频文件变更
    const handleVideoFileChange = (file) => {
      console.log('视频文件已选择:', file.name);
      // 重置上传状态
      videoUploadStatus.uploading = true;
      videoUploadStatus.progress = 0;
      videoUploadStatus.status = '';
    };
    
    // 处理视频上传进度
    const handleVideoUploadProgress = (percentage) => {
      console.log('视频上传进度:', percentage);
      videoUploadStatus.progress = percentage;
      // 只有在上传中时才显示进度条
      videoUploadStatus.uploading = percentage < 100;
    };

    // 提交章节表单
    const submitChapterForm = async () => {
      if (!chapterFormRef.value) return;
      
      await chapterFormRef.value.validate(async (valid) => {
        if (!valid) {
          return false;
        }
        
        // 检查是否正在上传视频
        if (videoUploadStatus.uploading && videoUploadStatus.progress < 100) {
          ElMessage.warning('请等待视频上传完成');
          return false;
        }
        
        // 在提交前处理视频URL，确保使用相对路径格式
        if (chapterForm.videoUrl && chapterForm.videoUrl.startsWith('http')) {
          // 从URL中提取文件名部分
          const urlParts = chapterForm.videoUrl.split('/');
          const fileName = urlParts[urlParts.length - 1];
          
          if (fileName) {
            // 使用/uploads/文件名格式
            chapterForm.videoUrl = `/uploads/${fileName}`;
            console.log('提交前格式化URL为相对路径:', chapterForm.videoUrl);
          }
        }
        
        chapterFormLoading.value = true;
        try {
          console.log('准备提交章节表单:', JSON.stringify(chapterForm));
          
          let res;
          if (isEditChapter.value) {
            // 编辑模式
            res = await courseApi.updateChapter(
              chapterForm.courseId, 
              chapterForm.id, 
              chapterForm
            );
          } else {
            // 新增模式
            res = await courseApi.createChapter(
              chapterForm.courseId, 
              chapterForm
            );
          }
          
          if (res.code === 200) {
            ElMessage.success(isEditChapter.value ? '修改成功' : '添加成功');
            chapterFormDialogVisible.value = false;
            getChapterList(currentCourse.value.id);
          } else {
            ElMessage.error(res.msg || (isEditChapter.value ? '修改失败' : '添加失败'));
          }
        } catch (error) {
          console.error('提交章节表单异常:', error);
          ElMessage.error('操作异常，请稍后重试');
        } finally {
          chapterFormLoading.value = false;
        }
      });
    };

    // 表单提交成功
    const handleFormSuccess = () => {
      dialogVisible.value = false
      getList()
    }

    // 分页相关
    const handleSizeChange = (val) => {
      queryParams.pageSize = val
      getList()
    }

    const handleCurrentChange = (val) => {
      queryParams.pageNum = val
      getList()
    }

    // 观看章节
    const handleViewChapter = (chapter) => {
      // 导航到课程详情页，并传递章节信息
      router.push({
        name: 'CourseDetail',
        params: { id: chapter.courseId },
        query: { chapterId: chapter.id }
      })
    }

    // 导航到课程审核页面
    const navigateToCourseReview = () => {
      router.push({
        name: 'CourseReview'
      })
    }

    // 获取审核状态显示文本
    const getReviewStatusText = (remark) => {
      const statusMap = {
        '0': '审核通过',
        '1': '待审核',
        '2': '审核不通过'
      }
      return statusMap[remark] || '未知状态'
    }
    
    // 获取审核状态标签类型
    const getReviewStatusType = (remark) => {
      const typeMap = {
        '0': 'success',
        '1': 'warning',
        '2': 'danger'
      }
      return typeMap[remark] || 'info'
    }

    onMounted(() => {
      getList()
    })

    return {
      queryParams,
      courseList,
      total,
      loading,
      dialogVisible,
      isEdit,
      selectedCourseId,
      formTitle,
      handleSearch,
      resetQuery,
      handleAdd,
      handleUpdate,
      handleDelete,
      handleExport,
      handleViewChapters,
      handleFormSuccess,
      handleSizeChange,
      handleCurrentChange,
      
      // 章节管理相关
      chapterDialogVisible,
      chapterLoading,
      chapterList,
      currentCourse,
      handleAddChapter,
      handleEditChapter,
      handleDeleteChapter,
      
      // 章节表单相关
      chapterFormDialogVisible,
      chapterFormLoading,
      chapterFormRef,
      chapterForm,
      isEditChapter,
      chapterRules,
      chapterFormTitle,
      submitChapterForm,
      videoUpload,
      handleVideoUploadSuccess,
      
      // 路由器
      router,
      handleViewChapter,
      navigateToCourseReview,
      
      // 审核状态相关
      getReviewStatusType,
      getReviewStatusText,
      
      // 视频上传状态
      videoUploadStatus,
      videoUploadStatusText,
      
      // 处理视频文件变更
      handleVideoFileChange,
      
      // 处理视频上传进度
      handleVideoUploadProgress
    }
  }
}
</script>

<style scoped>
.course-management {
  padding: 20px;
}

.page-header {
  background: var(--bg-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.page-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-2xl);
  margin: 0 0 var(--spacing-xs) 0;
  color: var(--text-primary);
}

.page-description {
  color: var(--text-secondary);
  margin: 0;
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.action-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 章节管理样式 */
.chapter-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.chapter-header h3 {
  margin: 0 0 10px 0;
  font-size: 18px;
}

.chapter-header p {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.chapter-actions {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

.video-upload-progress {
  margin-top: 10px;
  width: 100%;
  max-width: 350px;
}

.upload-status-text {
  margin-top: 5px;
  font-size: 12px;
  color: #606266;
}
</style>

