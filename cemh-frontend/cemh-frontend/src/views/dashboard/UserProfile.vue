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
              action="/api/upload/avatar"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <div class="profile-info">
              <h3>{{ userInfo.realName }}</h3>
              <p>{{ userInfo.position || '暂无职位' }}</p>
              <el-tag :type="userInfo.status === 1 ? 'success' : 'danger'">
                {{ userInfo.status === 1 ? '在职' : '离职' }}
              </el-tag>
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

        <!-- 快捷操作 -->
        <el-card class="quick-actions">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="action-list">
            <el-button type="primary" @click="activeTab = 'info'" plain>
              <i class="el-icon-edit"></i>
              编辑资料
            </el-button>
            <el-button type="success" @click="activeTab = 'password'" plain>
              <i class="el-icon-key"></i>
              修改密码
            </el-button>
            <el-button type="warning" @click="activeTab = 'security'" plain>
              <i class="el-icon-lock"></i>
              安全设置
            </el-button>
            <el-button type="info" @click="activeTab = 'logs'" plain>
              <i class="el-icon-document"></i>
              操作日志
            </el-button>
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
                    <el-form-item label="真实姓名" prop="realName">
                      <el-input v-model="userInfo.realName" placeholder="请输入真实姓名" />
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
                        <el-option label="未知" :value="0" />
                        <el-option label="男" :value="1" />
                        <el-option label="女" :value="2" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="生日" prop="birthday">
                      <el-date-picker
                        v-model="userInfo.birthday"
                        type="date"
                        placeholder="请选择生日"
                        style="width: 100%"
                      />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="职位" prop="position">
                      <el-input v-model="userInfo.position" placeholder="请输入职位" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="工号" prop="employeeNo">
                      <el-input v-model="userInfo.employeeNo" placeholder="请输入工号" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="个人简介" prop="bio">
                  <el-input
                    v-model="userInfo.bio"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入个人简介"
                  />
                </el-form-item>
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

            <!-- 操作日志 -->
            <el-tab-pane label="操作日志" name="logs">
              <div class="logs-section">
                <div class="logs-filter">
                  <el-form :model="logFilter" :inline="true">
                    <el-form-item label="操作类型">
                      <el-select v-model="logFilter.type" placeholder="请选择类型" clearable style="width: 150px">
                        <el-option label="登录" value="login" />
                        <el-option label="修改资料" value="update_profile" />
                        <el-option label="修改密码" value="change_password" />
                        <el-option label="安全设置" value="security" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="时间范围">
                      <el-date-picker
                        v-model="logFilter.dateRange"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        style="width: 240px"
                      />
                    </el-form-item>
                    <el-form-item>
                      <el-button type="primary" @click="handleSearchLogs">查询</el-button>
                      <el-button @click="handleResetLogs">重置</el-button>
                    </el-form-item>
                  </el-form>
                </div>
                
                <el-table :data="logList" v-loading="logLoading" stripe>
                  <el-table-column prop="type" label="操作类型" width="120">
                    <template #default="{ row }">
                      <el-tag :type="getLogType(row.type)">{{ getLogText(row.type) }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="操作描述" />
                  <el-table-column prop="ip" label="IP地址" width="140" />
                  <el-table-column prop="userAgent" label="设备信息" width="200" show-overflow-tooltip />
                  <el-table-column prop="createTime" label="操作时间" width="160">
                    <template #default="{ row }">
                      {{ formatDate(row.createTime) }}
                    </template>
                  </el-table-column>
                </el-table>
                
                <div class="pagination-wrapper">
                  <el-pagination
                    v-model:current-page="logPagination.page"
                    v-model:page-size="logPagination.size"
                    :page-sizes="[10, 20, 50]"
                    :total="logPagination.total"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleLogSizeChange"
                    @current-change="handleLogCurrentChange"
                  />
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
import { useUserStore } from '@/store/user'

export default {
  name: 'Profile',
  setup() {
    const userStore = useUserStore()
    
    // 响应式数据
    const activeTab = ref('info')
    const infoLoading = ref(false)
    const passwordLoading = ref(false)
    const logLoading = ref(false)
    const infoFormRef = ref(null)
    const passwordFormRef = ref(null)

    // 用户信息
    const userInfo = reactive({
      id: 1,
      username: 'admin',
      realName: '管理员',
      phone: '13800138000',
      email: 'admin@cemh.com',
      gender: 1,
      birthday: '1990-01-01',
      position: '系统管理员',
      employeeNo: 'EMP001',
      avatar: '',
      bio: '这是一个系统管理员账户',
      status: 1
    })

    // 用户统计
    const userStats = reactive({
      meetingCount: 25,
      courseCount: 12,
      newsCount: 8
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

    // 日志筛选
    const logFilter = reactive({
      type: '',
      dateRange: []
    })

    // 日志分页
    const logPagination = reactive({
      page: 1,
      size: 10,
      total: 0
    })

    // 日志列表
    const logList = ref([
      {
        id: 1,
        type: 'login',
        description: '用户登录系统',
        ip: '192.168.1.100',
        userAgent: 'Chrome 91.0.4472.124',
        createTime: '2024-06-15 09:30:00'
      },
      {
        id: 2,
        type: 'update_profile',
        description: '修改个人资料',
        ip: '192.168.1.100',
        userAgent: 'Chrome 91.0.4472.124',
        createTime: '2024-06-15 10:15:00'
      },
      {
        id: 3,
        type: 'change_password',
        description: '修改登录密码',
        ip: '192.168.1.100',
        userAgent: 'Chrome 91.0.4472.124',
        createTime: '2024-06-14 16:20:00'
      }
    ])

    // 表单验证规则
    const infoFormRules = {
      realName: [
        { required: true, message: '请输入真实姓名', trigger: 'blur' }
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

    // 头像上传成功
    const handleAvatarSuccess = (response, file) => {
      userInfo.avatar = URL.createObjectURL(file.raw)
      ElMessage.success('头像上传成功')
    }

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
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success('个人信息更新成功')
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
      // 重置为原始数据
      ElMessage.info('已重置为原始数据')
    }

    // 修改密码
    const handleChangePassword = async () => {
      try {
        await passwordFormRef.value.validate()
        passwordLoading.value = true
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
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

    // 获取日志类型样式
    const getLogType = (type) => {
      const types = {
        login: 'success',
        update_profile: 'primary',
        change_password: 'warning',
        security: 'danger'
      }
      return types[type] || 'info'
    }

    // 获取日志类型文本
    const getLogText = (type) => {
      const texts = {
        login: '登录',
        update_profile: '修改资料',
        change_password: '修改密码',
        security: '安全设置'
      }
      return texts[type] || '未知'
    }

    // 搜索日志
    const handleSearchLogs = () => {
      logPagination.page = 1
      // 模拟搜索
      ElMessage.success('日志搜索完成')
    }

    // 重置日志筛选
    const handleResetLogs = () => {
      Object.assign(logFilter, {
        type: '',
        dateRange: []
      })
      logPagination.page = 1
      ElMessage.info('筛选条件已重置')
    }

    // 日志分页大小变化
    const handleLogSizeChange = (size) => {
      logPagination.size = size
      logPagination.page = 1
    }

    // 日志当前页变化
    const handleLogCurrentChange = (page) => {
      logPagination.page = page
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    // 组件挂载时初始化数据
    onMounted(() => {
      logPagination.total = logList.value.length
    })

    return {
      activeTab,
      infoLoading,
      passwordLoading,
      logLoading,
      infoFormRef,
      passwordFormRef,
      userInfo,
      userStats,
      passwordForm,
      securitySettings,
      logFilter,
      logPagination,
      logList,
      infoFormRules,
      passwordFormRules,
      handleAvatarSuccess,
      beforeAvatarUpload,
      handleUpdateInfo,
      handleResetInfo,
      handleChangePassword,
      handleResetPassword,
      handleTwoFactorChange,
      handleLoginNotificationChange,
      getLogType,
      getLogText,
      handleSearchLogs,
      handleResetLogs,
      handleLogSizeChange,
      handleLogCurrentChange,
      formatDate
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
.quick-actions,
.content-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.profile-avatar-section {
  text-align: center;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 20px;
}

.avatar-uploader {
  border: 2px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
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

.action-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.action-list .el-button {
  justify-content: flex-start;
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

.logs-section {
  max-width: 100%;
}

.logs-filter {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
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
  
  .logs-filter .el-form {
    flex-direction: column;
  }
}
</style>

