<template>
  <div class="file-upload-component">
    <!-- 上传控件 -->
    <el-upload
      :action="action"
      :accept="accept"
      :multiple="multiple"
      :limit="limit"
      :disabled="disabled || uploading"
      :auto-upload="false"
      :show-file-list="showFileList"
      :on-change="handleChange"
      :on-exceed="handleExceed"
      :on-error="handleError"
      :before-upload="beforeUpload"
      :file-list="fileList"
      class="file-uploader"
      ref="uploadRef"
    >
      <template #trigger>
        <slot name="trigger">
          <el-button :type="buttonType" :disabled="disabled || uploading">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            {{ buttonText }}
          </el-button>
        </slot>
      </template>
      
      <template #tip>
        <slot name="tip">
          <div class="el-upload__tip" v-if="tip">
            {{ tip }}
          </div>
        </slot>
      </template>
    </el-upload>
    
    <!-- 上传进度 -->
    <div v-if="uploading" class="upload-progress">
      <el-progress :percentage="uploadProgress" :status="uploadStatus" />
      <div class="progress-text">{{ progressText }}</div>
    </div>
    
    <!-- 上传成功预览 -->
    <div v-if="showPreview && fileUrl" class="upload-preview">
      <div v-if="fileType === 'image'" class="image-preview">
        <img :src="fileUrl" alt="预览图片" @error="handlePreviewError" />
      </div>
      <div v-else-if="fileType === 'video'" class="video-preview">
        <video controls :src="fileUrl" @error="handlePreviewError">
          您的浏览器不支持视频预览
        </video>
        <div class="video-info">
          <el-tag size="small" type="success">视频已上传</el-tag>
          <span class="video-name">{{ fileName || '视频' }}</span>
        </div>
      </div>
      <div v-else class="file-info">
        <el-link :href="fileUrl" target="_blank">{{ fileName || '查看文件' }}</el-link>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { uploadApi } from '@/api/upload'
import axios from 'axios'

export default {
  name: 'FileUpload',
  components: {
    UploadFilled
  },
  props: {
    // 文件类型：image, video, document, file
    fileType: {
      type: String,
      default: 'image',
      validator: (value) => ['image', 'video', 'document', 'file'].includes(value)
    },
    // 按钮文本
    buttonText: {
      type: String,
      default: '上传文件'
    },
    // 按钮类型
    buttonType: {
      type: String,
      default: 'primary'
    },
    // 提示文本
    tip: {
      type: String,
      default: ''
    },
    // 是否显示文件列表
    showFileList: {
      type: Boolean,
      default: false
    },
    // 是否显示预览
    showPreview: {
      type: Boolean,
      default: true
    },
    // 是否自动上传
    autoUpload: {
      type: Boolean,
      default: false
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false
    },
    // 最大上传数量
    limit: {
      type: Number,
      default: 1
    },
    // 允许多选
    multiple: {
      type: Boolean,
      default: false
    },
    // 初始文件URL
    initialFileUrl: {
      type: String,
      default: ''
    },
    // 初始文件名
    initialFileName: {
      type: String,
      default: ''
    }
  },
  emits: ['update:fileUrl', 'update:fileName', 'upload-success', 'upload-error', 'upload-progress', 'file-change'],
  setup(props, { emit }) {
    const uploadRef = ref(null)
    const fileList = ref([])
    const currentFile = ref(null)
    const fileUrl = ref(props.initialFileUrl || '')
    const fileName = ref(props.initialFileName || '')
    const uploading = ref(false)
    const uploadProgress = ref(0)
    const uploadStatus = ref('')
    
    // 计算属性
    const accept = computed(() => {
      switch (props.fileType) {
        case 'image':
          return 'image/*'
        case 'video':
          return 'video/*'
        case 'document':
          return '.doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx,.txt'
        default:
          return ''
      }
    })
    
    const action = computed(() => {
      return 'javascript:void(0);' // 禁用自动上传
    })
    
    const progressText = computed(() => {
      if (uploadProgress.value === 100 && uploadStatus.value === 'success') {
        return '上传成功'
      } else if (uploadStatus.value === 'exception') {
        return '上传失败'
      } else {
        return `上传中 ${uploadProgress.value}%`
      }
    })
    
    // 方法
    const reset = () => {
      fileList.value = []
      currentFile.value = null
      uploadProgress.value = 0
      uploadStatus.value = ''
      uploading.value = false
    }
    
    const upload = async () => {
      if (!currentFile.value) {
        ElMessage.warning('请先选择文件')
        return
      }
      
      uploading.value = true
      uploadProgress.value = 0
      uploadStatus.value = ''
      
      try {
        console.log('开始上传文件:', currentFile.value.name, '类型:', props.fileType)
        
        // 使用FormData构建上传数据
        const formData = new FormData()
        formData.append('file', currentFile.value.raw)
        
        // 定义上传配置
        const config = {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          onUploadProgress: (progressEvent) => {
            // 计算上传进度百分比
            const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
            uploadProgress.value = percentCompleted
            emit('upload-progress', percentCompleted)
            console.log(`上传进度: ${percentCompleted}%`)
          }
        }
        
        // 根据文件类型选择上传API
        let response
        let url = ''
        switch (props.fileType) {
          case 'image':
            url = '/api/upload/image'
            break
          case 'video':
            url = '/api/upload/video'
            break
          case 'document':
            url = '/api/upload/document'
            break
          default:
            url = '/api/upload/file'
        }
        
        // 直接使用axios上传，以便使用进度事件
        const baseURL = import.meta.env.VITE_APP_BASE_API || 'http://localhost:8080'
        response = await axios.post(`${baseURL}${url}`, formData, config)
        response = response.data // 获取响应数据
        
        if (response.code === 200) {
          uploadProgress.value = 100
          uploadStatus.value = 'success'
          
          // 更新文件URL和名称
          fileUrl.value = response.data.url
          fileName.value = response.data.name || currentFile.value.name
          
          // 如果是视频文件，确保URL格式正确
          if (props.fileType === 'video') {
            const baseUrl = import.meta.env.VITE_APP_BASE_API || 'http://localhost:8080'
            // 检查是否符合 /uploads/20250627_UUID.mp4 格式
            const urlPattern = /\/uploads\/\d{8}_[a-f0-9-]+\.\w+$/
            if (!urlPattern.test(fileUrl.value)) {
              // 如果不符合要求格式，从URL中提取文件名部分并构建新格式
              const urlParts = fileUrl.value.split('/')
              const filename = urlParts[urlParts.length - 1]
              console.log('文件名:', filename)
              fileUrl.value = `${baseUrl}/uploads/${filename}`
            }
            console.log('修正后的视频URL:', fileUrl.value)
          }
          
          emit('update:fileUrl', fileUrl.value)
          emit('update:fileName', fileName.value)
          
          // 修正视频URL格式
          const finalData = {
            url: fileUrl.value,
            name: fileName.value,
            size: response.data.size,
            response
          };
          
          // 确保视频相关信息传递给父组件
          emit('upload-success', finalData)
          
          console.log('上传成功, 最终URL:', fileUrl.value)
          ElMessage.success('上传成功')
        } else {
          throw new Error(response.message || '上传失败')
        }
      } catch (error) {
        console.error('上传失败:', error)
        uploadStatus.value = 'exception'
        emit('upload-error', error)
        ElMessage.error(`上传失败: ${error.message || '未知错误'}`)
      } finally {
        uploading.value = false
      }
    }
    
    // 事件处理
    const handleChange = (uploadFile) => {
      console.log('文件变更:', uploadFile)
      currentFile.value = uploadFile
      
      emit('file-change', uploadFile)
      
      // 如果设置了自动上传，在文件选择后立即上传
      if (props.autoUpload && uploadFile && uploadFile.raw) {
        nextTick(() => {
          upload()
        })
      }
    }
    
    const handleExceed = (files) => {
      ElMessage.warning(`最多只能上传${props.limit}个文件`)
    }
    
    const handleError = (err) => {
      console.error('上传错误:', err)
      uploadStatus.value = 'exception'
      emit('upload-error', err)
      ElMessage.error('上传失败')
    }
    
    const beforeUpload = (file) => {
      console.log('准备上传文件:', file.name, '大小:', file.size)
      
      // 检查文件类型
      const isValidType = checkFileType(file)
      if (!isValidType) {
        ElMessage.error('文件类型不符合要求')
        return false
      }
      
      // 检查文件大小 (20MB)
      const isLt20M = file.size / 1024 / 1024 < 20
      if (!isLt20M) {
        ElMessage.error('文件大小不能超过20MB')
        return false
      }
      
      return true
    }
    
    const checkFileType = (file) => {
      const fileName = file.name.toLowerCase()
      
      switch (props.fileType) {
        case 'image':
          return /\.(jpg|jpeg|png|gif|bmp|webp)$/.test(fileName)
        case 'video':
          return /\.(mp4|webm|ogg|mov|avi|wmv|flv|mkv)$/.test(fileName)
        case 'document':
          return /\.(doc|docx|pdf|xls|xlsx|ppt|pptx|txt)$/.test(fileName)
        default:
          return true
      }
    }
    
    const handlePreviewError = (e) => {
      console.error('预览加载失败:', e)
      e.target.classList.add('preview-error')
      ElMessage.warning('文件预览加载失败')
    }
    
    // 监听初始值变化
    watch(() => props.initialFileUrl, (newVal) => {
      if (newVal !== fileUrl.value) {
        fileUrl.value = newVal
      }
    })
    
    watch(() => props.initialFileName, (newVal) => {
      if (newVal !== fileName.value) {
        fileName.value = newVal
      }
    })
    
    // 暴露方法给父组件
    return {
      uploadRef,
      fileList,
      fileUrl,
      fileName,
      uploading,
      uploadProgress,
      uploadStatus,
      progressText,
      accept,
      action,
      upload,
      reset,
      handleChange,
      handleExceed,
      handleError,
      beforeUpload,
      handlePreviewError,
      currentFile
    }
  }
}
</script>

<style scoped>
.file-upload-component {
  margin-bottom: 15px;
}

.upload-progress {
  margin-top: 10px;
}

.progress-text {
  font-size: 12px;
  margin-top: 5px;
  color: #606266;
}

.upload-preview {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  background-color: #f7f8fa;
}

.image-preview img {
  max-width: 100%;
  max-height: 200px;
  display: block;
  margin: 0 auto;
}

.video-preview video {
  max-width: 100%;
  max-height: 200px;
  display: block;
  margin: 0 auto;
}

.video-info {
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.video-name {
  font-size: 14px;
  color: #606266;
  word-break: break-all;
}

.preview-error {
  display: none;
}

.file-info {
  text-align: center;
  padding: 10px;
}
</style> 