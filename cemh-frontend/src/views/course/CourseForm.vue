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
        <file-upload
          v-model="form.coverImage"
          file-type="image"
          list-type="picture-card"
          :tip="'支持 jpg、png、gif 格式，大小不超过 2MB'"
          :max-size="2"
        />
      </el-form-item>
      
      <el-form-item label="课程视频" prop="videoUrl">
        <file-upload
          v-model="form.videoUrl"
          file-type="video"
          button-text="上传视频"
          :tip="'支持 mp4 格式，大小不超过 50MB'"
          :max-size="50"
        />
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
import { ref, reactive, defineComponent, watch } from 'vue'
import { ElMessage } from 'element-plus'
import FileUpload from '@/components/FileUpload.vue'
import { courseApi } from '@/api/course'

export default defineComponent({
  name: 'CourseForm',
  components: {
    FileUpload
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
        { required: true, message: '请上传课程视频', trigger: 'change' }
      ]
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
    
    // 提交表单
    const submitForm = async () => {
      if (!formRef.value) return
      
      await formRef.value.validate(async (valid) => {
        if (!valid) {
          return false
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
      cancel
    }
  }
})
</script>

<style scoped>
.course-form {
  max-width: 800px;
  margin: 0 auto;
}
</style> 