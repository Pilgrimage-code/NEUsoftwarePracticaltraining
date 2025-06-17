<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <div class="logo">
          <img src="/logo.png" alt="测盟汇" class="logo-img" />
          <h1 class="logo-text">测盟汇管理系统</h1>
        </div>
        <p class="login-desc">专业的会议管理与培训平台</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="tenantCode">
          <el-input
            v-model="loginForm.tenantCode"
            placeholder="请输入租户代码"
            size="large"
            prefix-icon="OfficeBuilding"
          />
        </el-form-item>
        
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          <el-link type="primary" class="forgot-password">忘记密码？</el-link>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>还没有账号？<el-link type="primary">立即注册</el-link></p>
        <div class="demo-account">
          <p>演示账号：</p>
          <el-tag @click="fillDemoAccount('admin')" class="demo-tag">管理员: admin/123456</el-tag>
          <el-tag @click="fillDemoAccount('user')" class="demo-tag">普通用户: user/123456</el-tag>
        </div>
      </div>
    </div>
    
    <div class="login-bg">
      <div class="bg-content">
        <h2>欢迎使用测盟汇管理系统</h2>
        <p>高效的会议管理，专业的培训平台</p>
        <ul class="feature-list">
          <li><el-icon><Check /></el-icon> 会议全流程管理</li>
          <li><el-icon><Check /></el-icon> 在线报名与签到</li>
          <li><el-icon><Check /></el-icon> 培训课程管理</li>
          <li><el-icon><Check /></el-icon> 多租户架构支持</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)

// 表单数据
const loginForm = reactive({
  tenantCode: 'demo',
  username: '',
  password: '',
  rememberMe: false
})

// 表单验证规则
const loginRules = {
  tenantCode: [
    { required: true, message: '请输入租户代码', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    // 添加客户端IP（模拟）
    const loginData = {
      ...loginForm,
      clientIp: '127.0.0.1'
    }
    
    await userStore.login(loginData)
    
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}

// 填充演示账号
const fillDemoAccount = (type) => {
  if (type === 'admin') {
    loginForm.username = 'admin'
    loginForm.password = '123456'
  } else if (type === 'user') {
    loginForm.username = 'user'
    loginForm.password = '123456'
  }
}

onMounted(() => {
  // 如果已经登录，直接跳转到首页
  if (userStore.isLoggedIn) {
    router.push('/dashboard')
  }
})
</script>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  flex: 1;
  max-width: 500px;
  background: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 60px 80px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.logo-img {
  width: 48px;
  height: 48px;
  margin-right: 12px;
}

.logo-text {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.login-desc {
  color: #909399;
  font-size: 16px;
  margin: 0;
}

.login-form {
  margin-bottom: 30px;
}

.login-form .el-form-item {
  margin-bottom: 24px;
}

.login-form .el-form-item:nth-child(4) {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.forgot-password {
  font-size: 14px;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 500;
}

.login-footer {
  text-align: center;
}

.login-footer p {
  margin-bottom: 20px;
  color: #606266;
}

.demo-account {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.demo-account p {
  margin-bottom: 10px;
  font-weight: 500;
  color: #495057;
}

.demo-tag {
  margin: 0 5px 5px 0;
  cursor: pointer;
  transition: all 0.3s;
}

.demo-tag:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.login-bg {
  flex: 1;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.9) 0%, rgba(118, 75, 162, 0.9) 100%),
              url('/bg-pattern.png') center/cover;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  padding: 60px;
}

.bg-content {
  text-align: center;
  max-width: 400px;
}

.bg-content h2 {
  font-size: 36px;
  font-weight: 600;
  margin-bottom: 20px;
  line-height: 1.2;
}

.bg-content p {
  font-size: 18px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.feature-list {
  list-style: none;
  padding: 0;
  text-align: left;
}

.feature-list li {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  font-size: 16px;
}

.feature-list li .el-icon {
  margin-right: 12px;
  color: #67c23a;
  font-size: 18px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .login-box {
    max-width: none;
    padding: 40px 30px;
  }
  
  .login-bg {
    min-height: 200px;
    padding: 30px;
  }
  
  .bg-content h2 {
    font-size: 24px;
  }
  
  .bg-content p {
    font-size: 16px;
  }
}
</style>

