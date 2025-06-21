<template>
  <div class="modern-login">
    <!-- 背景装饰 -->
    <div class="login-background">
      <div class="bg-shape shape-1"></div>
      <div class="bg-shape shape-2"></div>
      <div class="bg-shape shape-3"></div>
      <div class="floating-elements">
        <div class="floating-element" v-for="i in 6" :key="i" :style="getFloatingStyle(i)"></div>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="login-container">
      <!-- 左侧信息区域 -->
      <div class="login-info">
        <div class="info-content">
          <div class="logo-section">
            <div class="logo">
              <div class="logo-icon">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                  <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                  <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/>
                </svg>
              </div>
              <h1>测盟汇</h1>
            </div>
            <p class="logo-subtitle">企业会议管理系统</p>
          </div>
          
          <div class="feature-list">
            <div class="feature-item" v-for="(feature, index) in features" :key="index">
              <div class="feature-icon">{{ feature.icon }}</div>
              <div class="feature-content">
                <h3>{{ feature.title }}</h3>
                <p>{{ feature.description }}</p>
              </div>
            </div>
          </div>
          
          <div class="stats-section">
            <div class="stat-item">
              <div class="stat-number">10K+</div>
              <div class="stat-label">活跃用户</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">50K+</div>
              <div class="stat-label">成功会议</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">99.9%</div>
              <div class="stat-label">系统稳定性</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-section">
        <div class="form-container">
          <div class="form-header">
            <h2>欢迎回来</h2>
            <p>请登录您的账户以继续使用</p>
          </div>

          <form @submit.prevent="handleLogin" class="login-form">
            <!-- 租户选择 -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-icon">🏢</span>
                租户代码
              </label>
              <div class="input-wrapper">
                <input
                  v-model="loginForm.tenantCode"
                  type="text"
                  class="form-input"
                  placeholder="请输入租户代码"
                  required
                />
                <div class="input-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M19 21V19C19 17.1362 17.8638 15.5701 16.2299 15.1598M19 21H5M19 21H21M5 21V19C5 16.7909 6.79086 15 9 15H11C13.2091 15 15 16.7909 15 19V21M5 21H3M16.2299 15.1598C17.0143 14.8914 17.5 14.1583 17.5 13.3C17.5 12.1193 16.6307 11.25 15.45 11.25C14.2693 11.25 13.4 12.1193 13.4 13.3C13.4 14.1583 13.8857 14.8914 14.6701 15.1598M16.2299 15.1598C15.6896 14.9943 15.1004 14.9 14.5 14.9M12 11C14.2091 11 16 9.20914 16 7C16 4.79086 14.2091 3 12 3C9.79086 3 8 4.79086 8 7C8 9.20914 9.79086 11 12 11Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </div>
            </div>

            <!-- 用户名 -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-icon">👤</span>
                用户名
              </label>
              <div class="input-wrapper">
                <input
                  v-model="loginForm.username"
                  type="text"
                  class="form-input"
                  placeholder="请输入用户名"
                  required
                />
                <div class="input-icon">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M20 21V19C20 16.7909 18.2091 15 16 15H8C5.79086 15 4 16.7909 4 19V21M16 7C16 9.20914 14.2091 11 12 11C9.79086 11 8 9.20914 8 7C8 4.79086 9.79086 3 12 3C14.2091 3 16 4.79086 16 7Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
              </div>
            </div>

            <!-- 密码 -->
            <div class="form-group">
              <label class="form-label">
                <span class="label-icon">🔒</span>
                密码
              </label>
              <div class="input-wrapper">
                <input
                  v-model="loginForm.password"
                  :type="showPassword ? 'text' : 'password'"
                  class="form-input"
                  placeholder="请输入密码"
                  required
                />
                <button
                  type="button"
                  class="password-toggle"
                  @click="showPassword = !showPassword"
                >
                  <svg v-if="showPassword" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2.99902 3L20.999 21M9.8433 9.91364C9.32066 10.4536 8.99902 11.1892 8.99902 12C8.99902 13.6569 10.3422 15 11.999 15C12.8215 15 13.5667 14.669 14.1086 14.133M6.49902 6.64715C4.59972 7.90034 3.15305 9.78394 2.45703 12C3.73128 16.0571 7.52159 19 11.9992 19C13.9881 19 15.8414 18.4194 17.3988 17.4184M10.999 5.04939C11.328 5.01673 11.6617 5 11.9992 5C16.4769 5 20.2672 7.94291 21.5414 12C21.2607 12.894 20.8577 13.7338 20.3522 14.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                  <svg v-else viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2.45703 12C3.73128 7.94291 7.52159 5 11.9992 5C16.4769 5 20.2672 7.94291 21.5414 12C20.2672 16.0571 16.4769 19 11.9992 19C7.52159 19 3.73128 16.0571 2.45703 12Z" stroke="currentColor" stroke-width="1.5"/>
                    <path d="M15 12C15 13.6569 13.6569 15 12 15C10.3431 15 9 13.6569 9 12C9 10.3431 10.3431 9 12 9C13.6569 9 15 10.3431 15 12Z" stroke="currentColor" stroke-width="1.5"/>
                  </svg>
                </button>
              </div>
            </div>

            <!-- 记住我和忘记密码 -->
            <div class="form-options">
              <label class="checkbox-wrapper">
                <input v-model="rememberMe" type="checkbox" class="checkbox-input">
                <span class="checkbox-custom"></span>
                <span class="checkbox-label">记住我</span>
              </label>
              <a href="#" class="forgot-password">忘记密码？</a>
            </div>

            <!-- 登录按钮 -->
            <button type="submit" class="login-button" :disabled="loading">
              <span v-if="loading" class="loading-spinner"></span>
              <span v-else class="button-content">
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M15 3H19C20.1046 3 21 3.89543 21 5V19C21 20.1046 20.1046 21 19 21H15M10 17L15 12L10 7M15 12H3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                登录系统
              </span>
            </button>

            <!-- 快速登录选项 -->
            <div class="quick-login">
              <div class="divider">
                <span>或者</span>
              </div>
              <div class="quick-login-buttons">
                <button type="button" class="quick-login-btn demo-btn" @click="loginAsDemo">
                  <span class="btn-icon">👤</span>
                  演示账户
                </button>
                <button type="button" class="quick-login-btn admin-btn" @click="loginAsAdmin">
                  <span class="btn-icon">⚡</span>
                  管理员
                </button>
              </div>
            </div>
          </form>

          <!-- 底部信息 -->
          <div class="form-footer">
            <p>还没有账户？ <a href="#" class="register-link">立即注册</a></p>
            <div class="footer-links">
              <a href="#">服务条款</a>
              <span>·</span>
              <a href="#">隐私政策</a>
              <span>·</span>
              <a href="#">帮助中心</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 版本信息 -->
    <div class="version-info">
      <span>测盟汇管理系统 v1.0.0</span>
      <span>© 2024 测盟汇科技. 保留所有权利.</span>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { login } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

// 表单数据
const loginForm = reactive({
  username: '',
  password: '',
  tenantCode: 'default'
})

// 状态管理
const loading = ref(false)
const showPassword = ref(false)
const rememberMe = ref(false)

// 功能特性数据
const features = ref([
  {
    icon: '📅',
    title: '智能会议管理',
    description: '高效的会议安排和资源调度'
  },
  {
    icon: '👥',
    title: '多租户架构',
    description: '支持多组织独立管理'
  },
  {
    icon: '📊',
    title: '数据分析',
    description: '深入的业务洞察和报表'
  },
  {
    icon: '🔒',
    title: '安全可靠',
    description: '企业级安全保障'
  }
])

// 登录处理
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password || !loginForm.tenantCode) {
    ElMessage.warning('请填写完整的登录信息');
    return;
  }

  loading.value = true;
  try {
    // 委托给 userStore.login()
    await userStore.login({
      ...loginForm,
      clientIp: '127.0.0.1'
    });
    ElMessage.success('登录成功');
    router.push('/dashboard');
  } catch (error) {
    ElMessage.error(error.message || '登录失败');
  } finally {
    loading.value = false;
  }
};


// 演示账户登录
const loginAsDemo = () => {
  loginForm.username = 'demo'
  loginForm.password = '123456'
  loginForm.tenantCode = 'demo'
  handleLogin()
}

// 管理员登录
const loginAsAdmin = () => {
  loginForm.username = 'admin'
  loginForm.password = '123456'
  loginForm.tenantCode = 'default'
  handleLogin()
}

// 获取浮动元素样式
const getFloatingStyle = (index) => {
  const positions = [
    { left: '10%', top: '20%', animationDelay: '0s' },
    { left: '80%', top: '10%', animationDelay: '2s' },
    { left: '15%', top: '70%', animationDelay: '4s' },
    { left: '85%', top: '60%', animationDelay: '1s' },
    { left: '50%', top: '15%', animationDelay: '3s' },
    { left: '70%', top: '80%', animationDelay: '5s' }
  ]
  return positions[index - 1] || {}
}

// 组件挂载时恢复记住的用户信息
onMounted(() => {
  const remembered = localStorage.getItem('rememberedUser')
  if (remembered) {
    const userData = JSON.parse(remembered)
    loginForm.username = userData.username
    loginForm.tenantCode = userData.tenantCode
    rememberMe.value = true
  }
})
</script>

<style scoped>
.modern-login {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 背景装饰 */
.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  z-index: 0;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -150px;
  animation: float 6s ease-in-out infinite;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  left: -100px;
  animation: float 8s ease-in-out infinite reverse;
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 10%;
  animation: float 10s ease-in-out infinite;
}

.floating-elements {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.floating-element {
  position: absolute;
  width: 4px;
  height: 4px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 50%;
  animation: floating 8s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

@keyframes floating {
  0%, 100% { transform: translateY(0px) scale(1); opacity: 0.7; }
  50% { transform: translateY(-30px) scale(1.2); opacity: 1; }
}

/* 主容器 */
.login-container {
  position: relative;
  z-index: 1;
  display: flex;
  min-height: 100vh;
}

/* 左侧信息区域 */
.login-info {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  color: white;
}

.info-content {
  max-width: 500px;
  width: 100%;
}

.logo-section {
  text-align: center;
  margin-bottom: 60px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}

.logo-icon {
  width: 48px;
  height: 48px;
  margin-right: 16px;
  color: white;
}

.logo h1 {
  font-size: 36px;
  font-weight: 700;
  margin: 0;
  letter-spacing: -1px;
}

.logo-subtitle {
  font-size: 18px;
  opacity: 0.9;
  margin: 0;
}

.feature-list {
  margin-bottom: 60px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 32px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-item:hover {
  transform: translateY(-4px);
  background: rgba(255, 255, 255, 0.15);
}

.feature-icon {
  font-size: 24px;
  margin-right: 16px;
  margin-top: 4px;
}

.feature-content h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.feature-content p {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
  line-height: 1.5;
}

.stats-section {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.stat-item {
  text-align: center;
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
}

/* 右侧登录表单 */
.login-form-section {
  flex: 0 0 480px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 40px;
  box-shadow: -10px 0 30px rgba(0, 0, 0, 0.1);
}

.form-container {
  width: 100%;
  max-width: 400px;
}

.form-header {
  text-align: center;
  margin-bottom: 40px;
}

.form-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 8px 0;
  letter-spacing: -0.5px;
}

.form-header p {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

/* 表单样式 */
.login-form {
  margin-bottom: 32px;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 8px;
}

.label-icon {
  margin-right: 8px;
  font-size: 16px;
}

.input-wrapper {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 16px 48px 16px 16px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s ease;
  background: white;
  color: #1a1a1a;
}

.form-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-input::placeholder {
  color: #9ca3af;
}

.input-icon {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  color: #9ca3af;
}

.password-toggle {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.password-toggle:hover {
  background: #f3f4f6;
}

.password-toggle svg {
  width: 20px;
  height: 20px;
  color: #6b7280;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.checkbox-input {
  display: none;
}

.checkbox-custom {
  width: 18px;
  height: 18px;
  border: 2px solid #d1d5db;
  border-radius: 4px;
  margin-right: 8px;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-input:checked + .checkbox-custom {
  background: #667eea;
  border-color: #667eea;
}

.checkbox-input:checked + .checkbox-custom::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.checkbox-label {
  font-size: 14px;
  color: #374151;
}

.forgot-password {
  font-size: 14px;
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #5a67d8;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

.button-content {
  display: flex;
  align-items: center;
  justify-content: center;
}

.button-content svg {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: white;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 快速登录 */
.quick-login {
  margin-bottom: 32px;
}

.divider {
  text-align: center;
  margin: 24px 0;
  position: relative;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e5e7eb;
}

.divider span {
  background: white;
  padding: 0 16px;
  color: #9ca3af;
  font-size: 14px;
}

.quick-login-buttons {
  display: flex;
  gap: 12px;
}

.quick-login-btn {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  background: white;
  color: #374151;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quick-login-btn:hover {
  border-color: #667eea;
  color: #667eea;
  transform: translateY(-1px);
}

.btn-icon {
  margin-right: 8px;
  font-size: 16px;
}

/* 表单底部 */
.form-footer {
  text-align: center;
}

.form-footer p {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 16px 0;
}

.register-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.register-link:hover {
  color: #5a67d8;
}

.footer-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  font-size: 12px;
}

.footer-links a {
  color: #9ca3af;
  text-decoration: none;
}

.footer-links a:hover {
  color: #667eea;
}

.footer-links span {
  color: #d1d5db;
}

/* 版本信息 */
.version-info {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
  z-index: 2;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .login-container {
    flex-direction: column;
  }
  
  .login-info {
    flex: none;
    padding: 40px 20px;
  }
  
  .login-form-section {
    flex: none;
    box-shadow: none;
  }
  
  .feature-list {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
  }
  
  .stats-section {
    margin-top: 40px;
  }
}

@media (max-width: 768px) {
  .login-info {
    padding: 30px 20px;
  }
  
  .login-form-section {
    padding: 30px 20px;
  }
  
  .feature-list {
    grid-template-columns: 1fr;
  }
  
  .stats-section {
    flex-direction: column;
    gap: 16px;
  }
  
  .quick-login-buttons {
    flex-direction: column;
  }
  
  .version-info {
    flex-direction: column;
    text-align: center;
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .form-container {
    max-width: none;
  }
  
  .form-header h2 {
    font-size: 24px;
  }
  
  .logo h1 {
    font-size: 28px;
  }
  
  .feature-item {
    padding: 16px;
  }
}
</style>

