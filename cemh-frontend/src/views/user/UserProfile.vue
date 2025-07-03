<template>
  <div class="profile">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-user"></i>
          个人中心
        </h1>
        <p class="page-description">管理个人信息和账户设置</p>
      </div>
    </div>

    <div class="profile-container">
      <!-- 左侧个人信息卡片 -->
      <div class="profile-sidebar">
        <el-card class="profile-card">
          <div class="profile-avatar-section">
            <el-upload
              class="avatar-uploader"
              :action="uploadAction"
              :headers="uploadHeaders"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :on-success="handleAvatarSuccess"
              :on-error="handleAvatarError"
            >
              <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar" />
              <div v-else class="avatar-uploader-placeholder">
                <i class="el-icon-plus avatar-uploader-icon"></i>
                <span style="color:#bbb;font-size:12px;">点击上传</span>
              </div>
            </el-upload>
            <div class="profile-info">
              <h3>{{ userInfo.username }}</h3>
              <p>{{ userInfo.nickname }}</p>
            </div>
          </div>
          
          <div class="profile-stats">
            <div class="stat-item">
              <div class="stat-value">{{ userStats.meetingCount }}</div>
              <div class="stat-label">参与会议</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.courseCount }}</div>
              <div class="stat-label">学习课程</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.newsCount }}</div>
              <div class="stat-label">发布资讯</div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧内容区域 -->
      <div class="profile-content">
        <el-card class="content-card">
          <el-tabs v-model="activeTab" type="border-card">
            <!-- 基本信息 -->
            <el-tab-pane label="基本信息" name="info">
              <el-form
                ref="infoFormRef"
                :model="userInfo"
                :rules="infoFormRules"
                label-width="100px"
                class="info-form"
              >
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="用户名" prop="username">
                      <el-input v-model="userInfo.username" disabled />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="用户昵称" prop="nickname">
                      <el-input v-model="userInfo.nickname" placeholder="请输入用户昵称" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="手机号" prop="phone">
                      <el-input v-model="userInfo.phone" placeholder="请输入手机号" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="邮箱" prop="email">
                      <el-input v-model="userInfo.email" placeholder="请输入邮箱" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="性别" prop="gender">
                      <el-select v-model="userInfo.gender" placeholder="请选择性别" style="width: 100%">
                        <el-option label="男" :value="0" />
                        <el-option label="女" :value="1" />
                        <el-option label="未知" :value="2" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item>
                  <el-button type="primary" @click="handleUpdateInfo" :loading="infoLoading">
                    保存修改
                  </el-button>
                  <el-button @click="handleResetInfo">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="password">
              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordFormRules"
                label-width="100px"
                class="password-form"
              >
                <el-form-item label="当前密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    placeholder="请输入当前密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    placeholder="请输入新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    placeholder="请再次输入新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleChangePassword" :loading="passwordLoading">
                    修改密码
                  </el-button>
                  <el-button @click="handleResetPassword">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- 安全设置 -->
            <el-tab-pane label="安全设置" name="security">
              <div class="security-settings">
                <div class="security-item">
                  <div class="security-info">
                    <h4>登录密码</h4>
                    <p>定期更换密码可以提高账户安全性</p>
                  </div>
                  <div class="security-action">
                    <el-button type="primary" @click="activeTab = 'password'">修改密码</el-button>
                  </div>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4>手机绑定</h4>
                    <p>绑定手机号：{{ userInfo.phone || '未绑定' }}</p>
                  </div>
                  <div class="security-action">
                    <el-button type="success">{{ userInfo.phone ? '更换手机' : '绑定手机' }}</el-button>
                  </div>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4>邮箱绑定</h4>
                    <p>绑定邮箱：{{ userInfo.email || '未绑定' }}</p>
                  </div>
                  <div class="security-action">
                    <el-button type="success">{{ userInfo.email ? '更换邮箱' : '绑定邮箱' }}</el-button>
                  </div>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4>两步验证</h4>
                    <p>开启两步验证可以大大提高账户安全性</p>
                  </div>
                  <div class="security-action">
                    <el-switch v-model="securitySettings.twoFactorAuth" @change="handleTwoFactorChange" />
                  </div>
                </div>
                
                <div class="security-item">
                  <div class="security-info">
                    <h4>登录通知</h4>
                    <p>有新设备登录时发送通知</p>
                  </div>
                  <div class="security-action">
                    <el-switch v-model="securitySettings.loginNotification" @change="handleLoginNotificationChange" />
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo } from '@/api/auth'
import { userApi } from '@/api/user'

export default {
  name: 'UserProfile',
  setup() {
    // 响应式数据
    const activeTab = ref('info')
    const infoLoading = ref(false)
    const passwordLoading = ref(false)
    const infoFormRef = ref(null)
    const passwordFormRef = ref(null)

    // 用户信息
    const userInfo = reactive({})
    const userId = ref(null)
    // 用户统计（如有接口可补充真实数据）
    const userStats = reactive({
      meetingCount: 0,
      courseCount: 0,
      newsCount: 0
    })

    // 密码表单
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })

    // 安全设置
    const securitySettings = reactive({
      twoFactorAuth: false,
      loginNotification: true
    })

    // 表单验证规则
    const infoFormRules = {
      nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' }
      ],
      phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }
    const passwordFormRules = {
      oldPassword: [
        { required: true, message: '请输入当前密码', trigger: 'blur' }
      ],
      newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== passwordForm.newPassword) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    }

    // 头像上传相关
    const uploadAction = 'http://localhost:8080/api/upload/image'
    const uploadHeaders = {
      Authorization: 'Bearer ' + (localStorage.getItem('token') || '')
    }
    const handleAvatarSuccess = async (response) => {
      if (response.code === 200) {
        await userApi.updateUserAvatar(userId.value, { avatar: response.data.url }, {
          headers: {
            'X-Tenant-Id': userInfo.tenantId
          }
        })
        ElMessage.success('头像上传成功')
        fetchUserInfo()
      } else {
        ElMessage.error(response.message || '头像上传失败')
      }
    }
    const handleAvatarError = () => {
      ElMessage.error('头像上传失败')
    }

    // 获取用户信息
    const fetchUserInfo = async () => {
      const res = await getUserInfo()
      if (res && res.data) {
        Object.assign(userInfo, res.data)
        userId.value = res.data.userId
        // 修正性别类型，保证下拉正常显示
        userInfo.gender = Number(userInfo.gender ?? 0)
      }
    }
    onMounted(fetchUserInfo)

    // 头像上传前验证
    const beforeAvatarUpload = (file) => {
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
    // 更新个人信息
    const handleUpdateInfo = async () => {
      try {
        await infoFormRef.value.validate()
        infoLoading.value = true
        const data = {
          username: userInfo.username,
          nickname: userInfo.nickname,
          phone: userInfo.phone,
          email: userInfo.email,
          gender: userInfo.gender
        }
        await userApi.updateUser(userId.value, data, {
          headers: {
            'X-Tenant-Id': userInfo.tenantId
          }
        })
        ElMessage.success('个人信息更新成功')
        fetchUserInfo()
      } catch (error) {
        if (error !== false) {
          ElMessage.error('更新失败')
        }
      } finally {
        infoLoading.value = false
      }
    }
    // 重置个人信息
    const handleResetInfo = () => {
      fetchUserInfo()
      ElMessage.info('已重置为原始数据')
    }
    // 修改密码
    const handleChangePassword = async () => {
      try {
        await passwordFormRef.value.validate()
        passwordLoading.value = true
        await userApi.changePassword(userId.value, passwordForm.oldPassword, passwordForm.newPassword)
        ElMessage.success('密码修改成功')
        handleResetPassword()
      } catch (error) {
        if (error !== false) {
          ElMessage.error('密码修改失败')
        }
      } finally {
        passwordLoading.value = false
      }
    }
    // 重置密码表单
    const handleResetPassword = () => {
      Object.assign(passwordForm, {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      })
      passwordFormRef.value?.clearValidate()
    }
    // 两步验证变化
    const handleTwoFactorChange = (value) => {
      ElMessage.success(value ? '两步验证已开启' : '两步验证已关闭')
    }
    // 登录通知变化
    const handleLoginNotificationChange = (value) => {
      ElMessage.success(value ? '登录通知已开启' : '登录通知已关闭')
    }

    return {
      activeTab,
      infoLoading,
      passwordLoading,
      infoFormRef,
      passwordFormRef,
      userInfo,
      userStats,
      passwordForm,
      securitySettings,
      infoFormRules,
      passwordFormRules,
      beforeAvatarUpload,
      uploadAction,
      uploadHeaders,
      handleAvatarSuccess,
      handleAvatarError,
      handleUpdateInfo,
      handleResetInfo,
      handleChangePassword,
      handleResetPassword,
      handleTwoFactorChange,
      handleLoginNotificationChange
    }
  }
}
</script>

<style scoped>
.profile {
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

.profile-container {
  display: flex;
  gap: 20px;
}

.profile-sidebar {
  width: 300px;
  flex-shrink: 0;
}

.profile-content {
  flex: 1;
}

.profile-card,
.content-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.profile-avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 20px;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  background: #fff;
  margin: 0 auto;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader .avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-uploader-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.avatar-uploader-icon {
  font-size: 32px;
  color: #8c939d;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.profile-info h3 {
  margin: 0 0 8px 0;
  color: #303133;
}

.profile-info p {
  margin: 0 0 12px 0;
  color: #909399;
}

.profile-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.info-form,
.password-form {
  max-width: 600px;
}

.security-settings {
  max-width: 600px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #ebeef5;
}

.security-item:last-child {
  border-bottom: none;
}

.security-info h4 {
  margin: 0 0 8px 0;
  color: #303133;
}

.security-info p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .profile-container {
    flex-direction: column;
  }
  
  .profile-sidebar {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .profile {
    padding: 10px;
  }
  
  .profile-stats {
    flex-direction: column;
    gap: 16px;
  }
  
  .security-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>

