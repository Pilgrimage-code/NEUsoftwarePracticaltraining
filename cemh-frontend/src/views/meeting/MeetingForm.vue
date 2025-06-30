<template>
  <div class="meeting-form">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-date"></i>
          {{ isEdit ? '编辑会议' : '创建会议' }}
        </h1>
        <p class="page-description">{{ isEdit ? '修改会议信息和设置' : '创建新的会议活动' }}</p>
      </div>
    </div>

    <!-- 会议表单 -->
    <div class="form-section">
      <el-card class="form-card">
        <el-form
          ref="meetingFormRef"
          :model="meetingForm"
          :rules="meetingFormRules"
          label-width="120px"
          class="meeting-form-content"
        >
          <el-tabs v-model="activeTab">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="basic">
              <el-form-item label="会议标题" prop="title">
                <el-input
                  v-model="meetingForm.title"
                  placeholder="请输入会议标题"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
              
              <el-form-item label="会议描述" prop="description">
                <el-input
                  v-model="meetingForm.description"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入会议描述"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="会议类型" prop="type">
                    <el-select v-model="meetingForm.type" placeholder="请选择会议类型" style="width: 100%">
                      <el-option label="线上会议" value="1" />
                      <el-option label="线下会议" value="2" />
                      <el-option label="混合会议" value="3" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="会议状态" prop="status">
                    <el-select v-model="meetingForm.status" placeholder="请选择会议状态" style="width: 100%">
                      <el-option label="草稿" value="0" />
                      <el-option label="已发布" value="1" />
                      <el-option label="进行中" value="2" />
                      <el-option label="已结束" value="3" />
                      <el-option label="已取消" value="4" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="开始时间" prop="startTime">
                    <el-date-picker
                      v-model="meetingForm.startTime"
                      type="datetime"
                      placeholder="请选择开始时间"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="结束时间" prop="endTime">
                    <el-date-picker
                      v-model="meetingForm.endTime"
                      type="datetime"
                      placeholder="请选择结束时间"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-form-item label="会议地点" prop="location">
                <el-input
                  v-model="meetingForm.location"
                  placeholder="请输入会议地点或线上会议链接"
                />
              </el-form-item>
            </el-tab-pane>
            
            <!-- 参会设置 -->
            <el-tab-pane label="参会设置" name="participants">
              <el-form-item label="参会人数限制">
                <el-input-number
                  v-model="meetingForm.maxParticipants"
                  :min="1"
                  :max="1000"
                  placeholder="不限制"
                  style="width: 200px"
                />
                <span style="margin-left: 8px; color: #909399;">人（0表示不限制）</span>
              </el-form-item>
              
              <el-form-item label="报名截止时间">
                <el-date-picker
                  v-model="meetingForm.registrationDeadline"
                  type="datetime"
                  placeholder="请选择报名截止时间"
                  style="width: 300px"
                />
              </el-form-item>
              
              <el-form-item label="是否需要审核">
                <el-switch v-model="meetingForm.requiresApproval" />
                <span style="margin-left: 8px; color: #909399;">开启后需要管理员审核才能参会</span>
              </el-form-item>
              
              <el-form-item label="参会费用">
                <el-input-number
                  v-model="meetingForm.fee"
                  :min="0"
                  :precision="2"
                  placeholder="免费"
                  style="width: 200px"
                />
                <span style="margin-left: 8px; color: #909399;">元（0表示免费）</span>
              </el-form-item>
              
              <el-form-item label="参会要求">
                <el-input
                  v-model="meetingForm.requirements"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入参会要求，如需要携带的物品、技能要求等"
                />
              </el-form-item>
            </el-tab-pane>
            
            <!-- 其他设置 -->
            <el-tab-pane label="其他设置" name="settings">
              <el-form-item label="会议标签">
                <el-select
                  v-model="meetingForm.tags"
                  
                  filterable
                  allow-create
                  placeholder="请选择或输入会议标签"
                  style="width: 100%"
                >
                  <el-option label="技术分享" value="tech" />
                  <el-option label="产品讨论" value="product" />
                  <el-option label="项目评审" value="review" />
                  <el-option label="培训学习" value="training" />
                  <el-option label="团队建设" value="team" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="会议材料">
                <el-upload
                  class="upload-demo"
                  action="/api/upload"
                  :file-list="meetingForm.attachments"
                  :on-success="handleFileSuccess"
                  :on-remove="handleFileRemove"
                  multiple
                >
                  <el-button size="small" type="primary">
                    <i class="el-icon-upload"></i>
                    上传文件
                  </el-button>
                  <template #tip>
                    <div class="el-upload__tip">
                      支持上传PDF、DOC、PPT等格式文件，单个文件不超过10MB
                    </div>
                  </template>
                </el-upload>
              </el-form-item>
              
              <el-form-item label="会议封面">
                <el-upload
                  class="cover-uploader"
                  action="/api/upload"
                  :show-file-list="false"
                  :on-success="handleCoverSuccess"
                  :before-upload="beforeCoverUpload"
                >
                  <img v-if="meetingForm.coverImage" :src="meetingForm.coverImage" class="cover-image" />
                  <i v-else class="el-icon-plus cover-uploader-icon"></i>
                </el-upload>
              </el-form-item>
              
              <el-form-item label="备注信息">
                <el-input
                  v-model="meetingForm.remark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入备注信息"
                />
              </el-form-item>
            </el-tab-pane>
          </el-tabs>
          
          <!-- 表单操作按钮 -->
          <div class="form-actions">
            <el-button @click="handleCancel">取消</el-button>
            <el-button @click="handleSaveDraft" :loading="saving">保存草稿</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="saving">
              {{ isEdit ? '更新会议' : '发布会议' }}
            </el-button>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {meetingApi} from '@/api/meeting'
import { ElMessage } from 'element-plus'

export default {
  name: 'MeetingForm',
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const meetingFormRef = ref(null)
    const saving = ref(false)
    const activeTab = ref('basic')
    
    // 是否为编辑模式
    const isEdit = computed(() => {
      return route.name === 'MeetingEdit'
    })
    
    // 会议表单数据
    const meetingForm = reactive({
      title: '',
      description: '',
      type: '',
      status: '',
      startTime: '',
      endTime: '',
      location: '',
      maxParticipants: 0,
      registrationDeadline: '',
      requiresApproval: false,
      fee: 0,
      requirements: '',
      tags: '',
      attachments: [],
      coverImage: '',
      remark: '',
      isTop: 0
    })
    
    // 表单验证规则
    const meetingFormRules = {
      title: [
        { required: true, message: '请输入会议标题', trigger: 'blur' }
      ],
      description: [
        { required: true, message: '请输入会议描述', trigger: 'blur' }
      ],
      type: [
        { required: true, message: '请选择会议类型', trigger: 'change' }
      ],
      startTime: [
        { required: true, message: '请选择开始时间', trigger: 'change' }
      ],
      endTime: [
        { required: true, message: '请选择结束时间', trigger: 'change' }
      ],
      location: [
        { required: true, message: '请输入会议地点', trigger: 'blur' }
      ]
    }
    
    // 获取会议详情（编辑模式）
    const getMeetingDetail = async (id) => {
      try {
        //获取会议详情
        console.log('开始获取会议详情，ID:', id);
        // 获取会议详情
        const mockData = await meetingApi.getMeetingDetail(id)
        console.log('后端返回的数据:', mockData); // 打印后端返回的数据，用于调试

        if (!mockData) {
          console.error('后端返回的数据为空');
          ElMessage.error('获取会议详情失败，返回数据为空')
          return;
        }
        const meetingData = mockData.data;

        // 定义一个辅助函数处理时间格式
        const parseDate = (dateString) => {
          if (!dateString) return '';
          console.log('原始时间字符串:', dateString);
          // 正则匹配时区部分，补全时区格式为 ±HH:MM
          let standardDateString = dateString.replace(/([+-])(\d{2})(\d{2})?$/, (_, sign, hours, minutes = '00') => {
            return `${sign}${hours}:${minutes}`;
          });
          console.log('修正后的时间字符串:', standardDateString);
          const date = new Date(standardDateString);
          console.log('创建的 Date 对象:', date);
          return isNaN(date.getTime()) ? '' : date;
        };

        // 手动赋值，处理数据类型
        meetingForm.title = meetingData.title || ''
        meetingForm.description = meetingData.description || ''
        meetingForm.type = String(meetingData.type) || '' // 确保类型为字符串
        meetingForm.status = String(meetingData.status) || '' // 确保状态为字符串
        // Use the parseDate function to handle the startTime
        meetingForm.startTime = parseDate(meetingData.startTime) || ''
        meetingForm.endTime = parseDate(meetingData.endTime) || ''
        meetingForm.location = meetingData.location || ''
        meetingForm.maxParticipants = Number(meetingData.maxParticipants) || 0
        meetingForm.registrationDeadline = parseDate(meetingData.registrationDeadline) || ''
        meetingForm.requiresApproval = Boolean(meetingData.requiresApproval) || false
        meetingForm.fee = Number(meetingData.fee) || 0
        meetingForm.requirements = meetingData.requirements || ''
        meetingForm.tags = meetingData.tags || ''
        meetingForm.attachments = meetingData.attachments || []
        meetingForm.coverImage = meetingData.coverImage || ''
        meetingForm.remark = meetingData.remark || ''


        console.log('赋值后的表单数据:', meetingForm);


      } catch (error) {
        console.log('获取会议详情出错:', error)
        ElMessage.error('获取会议详情失败')
      }
    }
    
    // 文件上传成功
    const handleFileSuccess = (response, file, fileList) => {
      meetingForm.attachments = fileList
      ElMessage.success('文件上传成功')
    }
    
    // 文件移除
    const handleFileRemove = (file, fileList) => {
      meetingForm.attachments = fileList
    }
    
    // 封面上传成功
    const handleCoverSuccess = (response, file) => {
      meetingForm.coverImage = URL.createObjectURL(file.raw)
      ElMessage.success('封面上传成功')
    }
    
    // 封面上传前验证
    const beforeCoverUpload = (file) => {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        ElMessage.error('上传封面只能是 JPG/PNG 格式!')
      }
      if (!isLt2M) {
        ElMessage.error('上传封面大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
    
    // 保存草稿
    const handleSaveDraft = async () => {
      saving.value = true
      try {
        meetingForm.status = 1
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('草稿保存成功')
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        saving.value = false
      }
    }
    
    // 提交表单
    const handleSubmit = async () => {
      try {
        await meetingFormRef.value.validate()
        saving.value = true
        
        let result;

        if(isEdit.value){
          meetingForm.id = route.params.id
         result = await meetingApi.updateMeeting(meetingForm)
        } else {
          result = await meetingApi.createMeeting(meetingForm)
        }
        if(result.code === 200){
          ElMessage.success(isEdit.value ? '会议更新成功' : '会议创建成功')
          router.push('/dashboard/meetings')
        } else {
          ElMessage.error(result.msg)
        }
        
      } catch (error) {
        console.error('操作出错:', error) // 打印错误信息
        if (error !== false) {
          ElMessage.error('操作失败')
        }
      } finally {
        saving.value = false
      }
    }
    
    // 取消操作
    const handleCancel = () => {
      router.go(-1)
    }
    
    // 组件挂载时的初始化
    onMounted(() => {

      if (isEdit.value && route.params.id) {
        getMeetingDetail(route.params.id)
      } 
    })
    
    return {
      meetingFormRef,
      saving,
      activeTab,
      isEdit,
      meetingForm,
      meetingFormRules,
      handleFileSuccess,
      handleFileRemove,
      handleCoverSuccess,
      beforeCoverUpload,
      handleSaveDraft,
      handleSubmit,
      handleCancel
    }
  }
}
</script>

<style scoped>
.meeting-form {
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

.form-section {
  margin-bottom: 20px;
}

.form-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.meeting-form-content {
  padding: 20px 0;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .meeting-form {
    padding: 10px;
  }
  
  .form-actions {
    flex-direction: column;
  }
  
  .meeting-form-content .el-form-item__label {
    width: 100px !important;
  }
}
</style>

