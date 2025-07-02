<template>
  <div class="course-review">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon><Reading /></el-icon>
          课程审核管理
        </h1>
        <p class="page-description">管理待审核的课程内容，进行课程审核操作</p>
      </div>
    </div>

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="课程名称">
          <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable />
        </el-form-item>
        <el-form-item label="课程作者">
          <el-input v-model="queryParams.courseAuthor" placeholder="请输入课程作者" clearable />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="queryParams.remark" placeholder="请选择审核状态" clearable>
            <el-option label="未审核" value="1" />
            <el-option label="审核通过" value="0" />
            <el-option label="审核失败" value="2" />
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
          <el-button type="success" link @click="handleReview(scope.row)" v-if="scope.row.remark === '1'">审核</el-button>
          <el-button type="primary" link @click="viewCourseDetails(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="queryParams.page"
        v-model:page-size="queryParams.size"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 课程审核对话框 -->
    <el-dialog
      title="课程审核"
      v-model="reviewDialogVisible"
      width="600px"
      :close-on-click-modal="false"
      destroy-on-close
    >
      <div v-if="currentCourse" class="review-course-info">
        <h3>{{ currentCourse.courseName }}</h3>
        <p>作者：{{ currentCourse.courseAuthor }}</p>
        <p>简介：{{ currentCourse.courseIntro }}</p>
      </div>
      
      <el-form ref="reviewFormRef" :model="reviewForm" label-width="100px">
        <el-form-item label="审核结果" prop="status">
          <el-radio-group v-model="reviewForm.status">
            <el-radio :label="0">通过</el-radio>
            <el-radio :label="2">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="reviewComment">
          <el-input 
            v-model="reviewForm.reviewComment" 
            type="textarea" 
            rows="4"
            placeholder="请输入审核意见（不通过时必填）" 
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="reviewDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReview" :loading="reviewSubmitting">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Refresh, Download, Reading } from '@element-plus/icons-vue'
import { courseApi } from '@/api/course'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

export default {
  name: 'CourseReview',
  components: {
    Search,
    Refresh,
    Reading
  },
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    // 查询参数
    const queryParams = reactive({
      page: 1,
      size: 10,
      courseName: '',
      courseAuthor: '',
      remark: '1' // 默认查询未审核的课程
    })
    
    // 表格数据
    const courseList = ref([])
    const total = ref(0)
    const loading = ref(false)
    
    // 审核对话框
    const reviewDialogVisible = ref(false)
    const currentCourse = ref(null)
    const reviewForm = reactive({
      status: 0,
      reviewComment: ''
    })
    const reviewSubmitting = ref(false)
    
    // 获取课程列表
    const getCourseList = async () => {
      loading.value = true
      try {
        const response = await courseApi.getCourseList(queryParams, userStore.getTenantId)
        courseList.value = response.data.records || []
        total.value = response.data.total || 0
      } catch (error) {
        console.error('获取课程列表失败:', error)
        ElMessage.error('获取课程列表失败')
      } finally {
        loading.value = false
      }
    }
    
    // 搜索
    const handleSearch = () => {
      queryParams.page = 1
      getCourseList()
    }
    
    // 重置搜索条件
    const resetQuery = () => {
      queryParams.courseName = ''
      queryParams.courseAuthor = ''
      queryParams.remark = '1'
      handleSearch()
    }
    
    // 分页处理
    const handleSizeChange = (size) => {
      queryParams.size = size
      getCourseList()
    }
    
    const handleCurrentChange = (page) => {
      queryParams.page = page
      getCourseList()
    }
    
    // 查看课程详情
    const viewCourseDetails = (course) => {
      router.push({ path: `/dashboard/course-detail/${course.id}` })
    }
    
    // 打开审核对话框
    const handleReview = (course) => {
      currentCourse.value = course
      reviewForm.status = 0
      reviewForm.reviewComment = ''
      reviewDialogVisible.value = true
    }
    
    // 提交审核
    const submitReview = async () => {
      if (reviewForm.status === 2 && !reviewForm.reviewComment) {
        ElMessage.warning('审核不通过时，请填写审核意见')
        return
      }
      
      reviewSubmitting.value = true
      try {
        await courseApi.reviewCourse(
          currentCourse.value.id,
          reviewForm,
          userStore.getTenantId,
          userStore.getUserId
        )
        ElMessage.success('审核提交成功')
        reviewDialogVisible.value = false
        getCourseList() // 刷新列表
      } catch (error) {
        console.error('审核提交失败:', error)
        ElMessage.error('审核提交失败')
      } finally {
        reviewSubmitting.value = false
      }
    }
    
    // 获取审核状态显示
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
    
    // 初始化
    onMounted(() => {
      getCourseList()
    })
    
    return {
      queryParams,
      courseList,
      total,
      loading,
      reviewDialogVisible,
      currentCourse,
      reviewForm,
      reviewSubmitting,
      handleSearch,
      resetQuery,
      handleSizeChange,
      handleCurrentChange,
      viewCourseDetails,
      handleReview,
      submitReview,
      getReviewStatusText,
      getReviewStatusType
    }
  }
}
</script>

<style scoped>
.course-review {
  padding: 20px;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  display: flex;
  align-items: center;
  font-size: 24px;
  margin: 0 0 8px;
}

.page-title .el-icon {
  margin-right: 8px;
}

.page-description {
  color: #606266;
  font-size: 14px;
  margin: 0;
}

.search-container {
  background-color: #fff;
  padding: 18px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.review-course-info {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.review-course-info h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.review-course-info p {
  margin: 5px 0;
  color: #606266;
}
</style> 