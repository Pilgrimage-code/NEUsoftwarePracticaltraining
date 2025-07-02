<template>
  <div class="course-form">
    <el-form 
      ref="formRef" 
      :model="form" 
      :rules="rules" 
      label-width="100px" 
      label-position="right"
      :disabled="loading"
    >
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="form.courseName" placeholder="请输入课程名称" />
      </el-form-item>
      
      <el-form-item label="课程简介" prop="courseIntro">
        <el-input 
          v-model="form.courseIntro" 
          type="textarea" 
          rows="4" 
          placeholder="请输入课程简介" 
        />
      </el-form-item>
      
      <el-form-item label="课程排序" prop="courseOrder">
        <el-input-number v-model="form.courseOrder" :min="1" :max="999" />
      </el-form-item>
      
      <el-form-item label="课程作者" prop="courseAuthor">
        <el-input v-model="form.courseAuthor" placeholder="请输入课程作者" />
      </el-form-item>
      
      <el-form-item label="课程封面" prop="coverImage">
        <el-upload
          class="cover-uploader"
          action="http://localhost:8080/api/upload/image"
          :show-file-list="false"
          :before-upload="beforeCoverUpload"
          :on-success="handleCoverSuccess"
          accept="image/*"
        >
          <img v-if="form.coverImage" :src="form.coverImage" class="cover-image" />
          <el-icon v-else class="cover-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      
      <el-form-item label="课程视频" prop="videoUrl">
        <file-upload
          v-model="form.videoUrl"
          ref="videoUpload"
          file-type="video"
          button-text="上传视频"
          :tip="'支持 mp4 格式，大小不超过 50MB'"
          :max-size="50"
          :initial-file-url="form.videoUrl"
          :auto-upload="true"
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
      
      <el-form-item label="课程状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="备注" prop="remark">
        <el-input 
          v-model="form.remark" 
          type="textarea" 
          rows="2" 
          placeholder="请输入备注信息" 
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="submitForm" :loading="loading">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { ref, reactive, defineComponent, watch, nextTick, computed } from 'vue'
import { ElMessage } from 'element-plus'
import FileUpload from '@/components/FileUpload.vue'
import { courseApi } from '@/api/course'
import { Plus } from '@element-plus/icons-vue'

export default defineComponent({
  name: 'CourseForm',
  components: {
    FileUpload,
    Plus
  },
  props: {
    // 编辑模式下的课程ID
    courseId: {
      type: [Number, String],
      default: null
    },
    // 是否为编辑模式
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  emits: ['success', 'cancel'],
  setup(props, { emit }) {
    const formRef = ref(null)
    const loading = ref(false)
    
    // 表单数据
    const form = reactive({
      id: null,
      courseName: '',
      courseIntro: '',
      courseOrder: 1,
      courseAuthor: '',
      coverImage: '',
      videoUrl: '',
      status: 1,
      remark: ''
    })
    
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
    
    // 表单验证规则
    const rules = {
      courseName: [
        { required: true, message: '请输入课程名称', trigger: 'blur' },
        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
      ],
      courseIntro: [
        { required: true, message: '请输入课程简介', trigger: 'blur' }
      ],
      courseOrder: [
        { required: true, message: '请输入课程排序', trigger: 'blur' }
      ],
      courseAuthor: [
        { required: true, message: '请输入课程作者', trigger: 'blur' }
      ],
      coverImage: [
        { required: true, message: '请上传课程封面', trigger: 'change' }
      ],
      videoUrl: [
        { required: true, message: '请上传课程视频', trigger: 'change' },
        { validator: (rule, value, callback) => {
          if (value && value.trim() !== '') {
            callback(); // 有值则通过验证
          } else {
            callback(new Error('请上传课程视频'));
          }
        }, trigger: 'change' }
      ]
    }
    
    // 图片上传前校验
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
    
    // 图片上传成功回调
    const handleCoverSuccess = (response) => {
      if (response.code === 200) {
        form.coverImage = response.data.url
        ElMessage.success('图片上传成功')
      } else {
        ElMessage.error(response.message || '图片上传失败')
      }
    }
    
    // 视频上传成功回调
    const handleVideoUploadSuccess = (data) => {
      form.videoUrl = data.url
      ElMessage.success('视频上传成功')
      // 更新上传状态
      videoUploadStatus.uploading = false
      videoUploadStatus.progress = 100
      videoUploadStatus.status = 'success'
      
      console.log('设置课程视频URL:', form.videoUrl)
      
      // 手动触发表单验证更新
      if (formRef.value) {
        formRef.value.validateField('videoUrl')
      }
    }
    
    // 处理视频文件变更
    const handleVideoFileChange = (file) => {
      console.log('视频文件已选择:', file.name)
      // 重置上传状态
      videoUploadStatus.uploading = true
      videoUploadStatus.progress = 0
      videoUploadStatus.status = ''
    }
    
    // 处理视频上传进度
    const handleVideoUploadProgress = (percentage) => {
      console.log('视频上传进度:', percentage)
      videoUploadStatus.progress = percentage
      // 只有在上传中时才显示进度条
      videoUploadStatus.uploading = percentage < 100
    }
    
    // 如果是编辑模式，获取课程详情
    const fetchCourseDetail = async () => {
      if (props.isEdit && props.courseId) {
        loading.value = true
        try {
          const res = await courseApi.getCourseById(props.courseId)
          if (res.code === 200 && res.data) {
            Object.assign(form, res.data)
          } else {
            ElMessage.error('获取课程详情失败')
          }
        } catch (error) {
          console.error('获取课程详情异常:', error)
          ElMessage.error('获取课程详情异常')
        } finally {
          loading.value = false
        }
      }
    }
    
    // 监听courseId变化
    watch(() => props.courseId, (newVal) => {
      if (newVal && props.isEdit) {
        fetchCourseDetail()
      }
    }, { immediate: true })
    
    // 监听videoUrl的变化，确保FileUpload组件正确更新
    const videoUpload = ref(null)
    watch(() => form.videoUrl, (newVal) => {
      if (newVal && videoUpload.value) {
        // 当有值并且组件已挂载时，手动更新验证状态
        nextTick(() => {
          if (formRef.value) {
            formRef.value.validateField('videoUrl')
          }
        })
      }
    })
    
    // 提交表单
    const submitForm = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (!valid) {
          return false
        }
        
        // 在提交前处理视频URL，确保使用相对路径格式
        if (form.videoUrl && form.videoUrl.startsWith('http')) {
          // 从URL中提取文件名部分
          const urlParts = form.videoUrl.split('/')
          const fileName = urlParts[urlParts.length - 1]
          
          if (fileName) {
            // 使用/uploads/文件名格式
            form.videoUrl = `/uploads/${fileName}`
            console.log('提交前格式化URL为相对路径:', form.videoUrl)
          }
        }
        
        loading.value = true
        try {
          let res;
          if (props.isEdit) {
            // 编辑模式
            res = await courseApi.updateCourse(props.courseId, form)
          } else {
            // 新增模式
            res = await courseApi.createCourse(form)
          }
          
          if (res.code === 200) {
            ElMessage.success(props.isEdit ? '修改成功' : '添加成功')
            emit('success')
          } else {
            ElMessage.error(res.msg || (props.isEdit ? '修改失败' : '添加失败'))
          }
        } catch (error) {
          console.error('提交表单异常:', error)
          ElMessage.error('操作异常，请稍后重试')
        } finally {
          loading.value = false
        }
      })
    }
    
    // 取消操作
    const cancel = () => {
      emit('cancel')
    }
    
    return {
      formRef,
      form,
      rules,
      loading,
      submitForm,
      cancel,
      beforeCoverUpload,
      handleCoverSuccess,
      handleVideoUploadSuccess,
      videoUpload,
      videoUploadStatus,
      videoUploadStatusText,
      handleVideoFileChange,
      handleVideoUploadProgress
    }
  }
})
</script>

<style scoped>
.course-form {
  max-width: 800px;
  margin: 0 auto;
}
.cover-uploader {
  display: inline-block;
}
.cover-image {
  width: 200px;
  height: 112px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #eee;
}
.cover-icon {
  font-size: 32px;
  color: #aaa;
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