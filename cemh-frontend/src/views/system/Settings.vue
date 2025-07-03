<template>
  <div class="settings modern-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">系统设置</h1>
        <div class="header-actions">
          <el-button @click="resetSettings" class="modern-btn secondary">
            <el-icon><RefreshLeft /></el-icon>
            重置设置
          </el-button>
          <el-button @click="saveSettings" type="primary" class="modern-btn primary">
            <el-icon><Check /></el-icon>
            保存设置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 设置导航 -->
    <div class="settings-container">
      <div class="settings-sidebar">
        <div class="sidebar-menu">
          <div 
            v-for="category in settingsCategories" 
            :key="category.key"
            class="menu-item"
            :class="{ active: activeCategory === category.key }"
            @click="activeCategory = category.key"
          >
            <el-icon>
              <component :is="category.icon" />
            </el-icon>
            <span>{{ category.label }}</span>
          </div>
        </div>
      </div>

      <div class="settings-content">
        <!-- 基本设置 -->
        <div v-show="activeCategory === 'basic'" class="settings-section">
          <div class="section-header">
            <h2>基本设置</h2>
            <p>配置系统的基本信息和参数</p>
          </div>
          
          <div class="settings-form">
            <div class="form-group">
              <label class="form-label">系统名称</label>
              <el-input 
                v-model="settings.basic.systemName"
                placeholder="请输入系统名称"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">系统描述</label>
              <el-input 
                v-model="settings.basic.systemDescription"
                type="textarea"
                :rows="3"
                placeholder="请输入系统描述"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">系统版本</label>
              <el-input 
                v-model="settings.basic.systemVersion"
                placeholder="请输入系统版本"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">管理员邮箱</label>
              <el-input 
                v-model="settings.basic.adminEmail"
                placeholder="请输入管理员邮箱"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">时区设置</label>
              <el-select 
                v-model="settings.basic.timezone"
                placeholder="请选择时区"
                class="modern-input"
                style="width: 100%"
              >
                <el-option label="北京时间 (UTC+8)" value="Asia/Shanghai" />
                <el-option label="东京时间 (UTC+9)" value="Asia/Tokyo" />
                <el-option label="纽约时间 (UTC-5)" value="America/New_York" />
                <el-option label="伦敦时间 (UTC+0)" value="Europe/London" />
              </el-select>
            </div>
            
            <div class="form-group">
              <label class="form-label">语言设置</label>
              <el-select 
                v-model="settings.basic.language"
                placeholder="请选择语言"
                class="modern-input"
                style="width: 100%"
              >
                <el-option label="简体中文" value="zh-CN" />
                <el-option label="繁体中文" value="zh-TW" />
                <el-option label="English" value="en-US" />
                <el-option label="日本語" value="ja-JP" />
              </el-select>
            </div>
          </div>
        </div>

        <!-- 安全设置 -->
        <div v-show="activeCategory === 'security'" class="settings-section">
          <div class="section-header">
            <h2>安全设置</h2>
            <p>配置系统的安全策略和权限控制</p>
          </div>
          
          <div class="settings-form">
            <div class="form-group">
              <label class="form-label">密码策略</label>
              <div class="checkbox-group">
                <el-checkbox v-model="settings.security.passwordPolicy.requireUppercase">
                  要求包含大写字母
                </el-checkbox>
                <el-checkbox v-model="settings.security.passwordPolicy.requireLowercase">
                  要求包含小写字母
                </el-checkbox>
                <el-checkbox v-model="settings.security.passwordPolicy.requireNumbers">
                  要求包含数字
                </el-checkbox>
                <el-checkbox v-model="settings.security.passwordPolicy.requireSpecialChars">
                  要求包含特殊字符
                </el-checkbox>
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">最小密码长度</label>
              <el-input-number 
                v-model="settings.security.passwordPolicy.minLength"
                :min="6"
                :max="32"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">密码有效期（天）</label>
              <el-input-number 
                v-model="settings.security.passwordExpiry"
                :min="30"
                :max="365"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">登录失败锁定</label>
              <div class="input-group">
                <el-input-number 
                  v-model="settings.security.maxLoginAttempts"
                  :min="3"
                  :max="10"
                  class="modern-input"
                />
                <span class="input-suffix">次失败后锁定</span>
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">会话超时（分钟）</label>
              <el-input-number 
                v-model="settings.security.sessionTimeout"
                :min="15"
                :max="480"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">双因素认证</label>
              <el-switch 
                v-model="settings.security.twoFactorAuth"
                active-text="启用"
                inactive-text="禁用"
              />
            </div>
          </div>
        </div>

        <!-- 通知设置 -->
        <div v-show="activeCategory === 'notification'" class="settings-section">
          <div class="section-header">
            <h2>通知设置</h2>
            <p>配置系统通知和消息推送</p>
          </div>
          
          <div class="settings-form">
            <div class="form-group">
              <label class="form-label">邮件通知</label>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">系统告警通知</div>
                  <div class="notification-desc">当系统出现异常时发送邮件通知</div>
                </div>
                <el-switch v-model="settings.notification.email.systemAlert" />
              </div>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">会议提醒</div>
                  <div class="notification-desc">会议开始前发送邮件提醒</div>
                </div>
                <el-switch v-model="settings.notification.email.meetingReminder" />
              </div>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">用户注册通知</div>
                  <div class="notification-desc">新用户注册时发送通知</div>
                </div>
                <el-switch v-model="settings.notification.email.userRegistration" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">短信通知</label>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">紧急告警</div>
                  <div class="notification-desc">系统紧急故障时发送短信通知</div>
                </div>
                <el-switch v-model="settings.notification.sms.emergencyAlert" />
              </div>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">验证码</div>
                  <div class="notification-desc">登录验证码短信发送</div>
                </div>
                <el-switch v-model="settings.notification.sms.verificationCode" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">推送通知</label>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">实时消息</div>
                  <div class="notification-desc">系统内实时消息推送</div>
                </div>
                <el-switch v-model="settings.notification.push.realtime" />
              </div>
              <div class="notification-item">
                <div class="notification-info">
                  <div class="notification-title">桌面通知</div>
                  <div class="notification-desc">浏览器桌面通知</div>
                </div>
                <el-switch v-model="settings.notification.push.desktop" />
              </div>
            </div>
          </div>
        </div>

        <!-- 存储设置 -->
        <div v-show="activeCategory === 'storage'" class="settings-section">
          <div class="section-header">
            <h2>存储设置</h2>
            <p>配置文件存储和备份策略</p>
          </div>
          
          <div class="settings-form">
            <div class="form-group">
              <label class="form-label">存储类型</label>
              <el-radio-group v-model="settings.storage.type">
                <el-radio label="local">本地存储</el-radio>
                <el-radio label="oss">对象存储</el-radio>
                <el-radio label="ftp">FTP存储</el-radio>
              </el-radio-group>
            </div>
            
            <div class="form-group" v-if="settings.storage.type === 'oss'">
              <label class="form-label">OSS配置</label>
              <div class="config-group">
                <el-input 
                  v-model="settings.storage.oss.endpoint"
                  placeholder="Endpoint"
                  class="modern-input"
                />
                <el-input 
                  v-model="settings.storage.oss.accessKeyId"
                  placeholder="Access Key ID"
                  class="modern-input"
                />
                <el-input 
                  v-model="settings.storage.oss.accessKeySecret"
                  placeholder="Access Key Secret"
                  type="password"
                  class="modern-input"
                />
                <el-input 
                  v-model="settings.storage.oss.bucket"
                  placeholder="Bucket Name"
                  class="modern-input"
                />
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">文件大小限制（MB）</label>
              <el-input-number 
                v-model="settings.storage.maxFileSize"
                :min="1"
                :max="1024"
                class="modern-input"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">允许的文件类型</label>
                      <el-checkbox-group v-model="settings.storage.allowedTypes">
          <el-checkbox value="image">图片文件</el-checkbox>
          <el-checkbox value="document">文档文件</el-checkbox>
          <el-checkbox value="video">视频文件</el-checkbox>
          <el-checkbox value="audio">音频文件</el-checkbox>
          <el-checkbox value="archive">压缩文件</el-checkbox>
        </el-checkbox-group>
            </div>
            
            <div class="form-group">
              <label class="form-label">自动备份</label>
              <el-switch 
                v-model="settings.storage.autoBackup"
                active-text="启用"
                inactive-text="禁用"
              />
            </div>
            
            <div class="form-group" v-if="settings.storage.autoBackup">
              <label class="form-label">备份频率</label>
              <el-select 
                v-model="settings.storage.backupFrequency"
                placeholder="请选择备份频率"
                class="modern-input"
                style="width: 100%"
              >
                <el-option label="每天" value="daily" />
                <el-option label="每周" value="weekly" />
                <el-option label="每月" value="monthly" />
              </el-select>
            </div>
          </div>
        </div>

        <!-- 系统维护 -->
        <div v-show="activeCategory === 'maintenance'" class="settings-section">
          <div class="section-header">
            <h2>系统维护</h2>
            <p>系统维护和数据管理工具</p>
          </div>
          
          <div class="maintenance-tools">
            <div class="tool-card">
              <div class="tool-icon">
                <el-icon><DataBoard /></el-icon>
              </div>
              <div class="tool-content">
                <h3>数据库优化</h3>
                <p>清理冗余数据，优化数据库性能</p>
                <el-button @click="optimizeDatabase" class="modern-btn primary">
                  开始优化
                </el-button>
              </div>
            </div>
            
            <div class="tool-card">
              <div class="tool-icon">
                <el-icon><Delete /></el-icon>
              </div>
              <div class="tool-content">
                <h3>清理缓存</h3>
                <p>清理系统缓存，释放存储空间</p>
                <el-button @click="clearCache" class="modern-btn secondary">
                  清理缓存
                </el-button>
              </div>
            </div>
            
            <div class="tool-card">
              <div class="tool-icon">
                <el-icon><Download /></el-icon>
              </div>
              <div class="tool-content">
                <h3>数据备份</h3>
                <p>手动创建系统数据备份</p>
                <el-button @click="createBackup" class="modern-btn primary">
                  创建备份
                </el-button>
              </div>
            </div>
            
            <div class="tool-card">
              <div class="tool-icon">
                <el-icon><Upload /></el-icon>
              </div>
              <div class="tool-content">
                <h3>数据恢复</h3>
                <p>从备份文件恢复系统数据</p>
                <el-button @click="restoreData" class="modern-btn warning">
                  恢复数据
                </el-button>
              </div>
            </div>
            
            <div class="tool-card">
              <div class="tool-icon">
                <el-icon><View /></el-icon>
              </div>
              <div class="tool-content">
                <h3>系统日志</h3>
                <p>查看和下载系统运行日志</p>
                <el-button @click="viewLogs" class="modern-btn secondary">
                  查看日志
                </el-button>
              </div>
            </div>
            
            <div class="tool-card">
              <div class="tool-icon">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="tool-content">
                <h3>系统重启</h3>
                <p>重启系统服务（谨慎操作）</p>
                <el-button @click="restartSystem" class="modern-btn danger">
                  重启系统
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const activeCategory = ref('basic')

const settingsCategories = [
  { key: 'basic', label: '基本设置', icon: 'Setting' },
  { key: 'security', label: '安全设置', icon: 'Lock' },
  { key: 'notification', label: '通知设置', icon: 'Bell' },
  { key: 'storage', label: '存储设置', icon: 'FolderOpened' },
  { key: 'maintenance', label: '系统维护', icon: 'Tools' }
]

const settings = reactive({
  basic: {
    systemName: '企业会议管理系统',
    systemDescription: '一个现代化的企业会议管理平台，提供会议安排、资讯发布、文件管理等功能。',
    systemVersion: '1.0.0',
    adminEmail: 'admin@company.com',
    timezone: 'Asia/Shanghai',
    language: 'zh-CN'
  },
  security: {
    passwordPolicy: {
      requireUppercase: true,
      requireLowercase: true,
      requireNumbers: true,
      requireSpecialChars: false,
      minLength: 8
    },
    passwordExpiry: 90,
    maxLoginAttempts: 5,
    sessionTimeout: 120,
    twoFactorAuth: false
  },
  notification: {
    email: {
      systemAlert: true,
      meetingReminder: true,
      userRegistration: false
    },
    sms: {
      emergencyAlert: true,
      verificationCode: true
    },
    push: {
      realtime: true,
      desktop: false
    }
  },
  storage: {
    type: 'local',
    maxFileSize: 100,
    allowedTypes: ['image', 'document'],
    autoBackup: true,
    backupFrequency: 'daily',
    oss: {
      endpoint: '',
      accessKeyId: '',
      accessKeySecret: '',
      bucket: ''
    }
  }
})

// 方法
const saveSettings = async () => {
  try {
    // 这里应该调用API保存设置
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('设置保存成功')
  } catch (error) {
    ElMessage.error('设置保存失败')
  }
}

const resetSettings = async () => {
  try {
    await ElMessageBox.confirm('确定要重置所有设置吗？此操作不可撤销。', '确认重置', {
      type: 'warning'
    })
    
    // 重置设置到默认值
    ElMessage.success('设置已重置')
  } catch {
    // 用户取消
  }
}

const optimizeDatabase = async () => {
  try {
    await ElMessageBox.confirm('数据库优化可能需要几分钟时间，确定要继续吗？', '确认优化', {
      type: 'info'
    })
    
    ElMessage.success('数据库优化已开始，请稍候...')
    // 这里应该调用API执行数据库优化
  } catch {
    // 用户取消
  }
}

const clearCache = async () => {
  try {
    await ElMessageBox.confirm('确定要清理系统缓存吗？', '确认清理', {
      type: 'warning'
    })
    
    ElMessage.success('缓存清理完成')
  } catch {
    // 用户取消
  }
}

const createBackup = async () => {
  try {
    ElMessage.success('数据备份已开始，完成后将通知您')
    // 这里应该调用API创建备份
  } catch (error) {
    ElMessage.error('创建备份失败')
  }
}

const restoreData = async () => {
  try {
    await ElMessageBox.confirm('数据恢复将覆盖当前数据，确定要继续吗？', '确认恢复', {
      type: 'error'
    })
    
    ElMessage.info('数据恢复功能开发中')
  } catch {
    // 用户取消
  }
}

const viewLogs = () => {
  ElMessage.info('跳转到系统监控页面查看日志')
}

const restartSystem = async () => {
  try {
    await ElMessageBox.confirm('系统重启将中断所有用户连接，确定要继续吗？', '确认重启', {
      type: 'error'
    })
    
    ElMessage.warning('系统重启功能需要超级管理员权限')
  } catch {
    // 用户取消
  }
}
</script>

<style scoped>
.settings {
  padding: var(--spacing-2xl);
}

.page-header {
  margin-bottom: var(--spacing-2xl);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: var(--font-size-3xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.header-actions {
  display: flex;
  gap: var(--spacing-md);
}

.settings-container {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: var(--spacing-2xl);
  min-height: 600px;
}

.settings-sidebar {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-lg);
  box-shadow: var(--shadow-md);
  height: fit-content;
}

.sidebar-menu {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.menu-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  border-radius: var(--radius-lg);
  cursor: pointer;
  transition: all var(--transition-fast);
  color: var(--text-secondary);
}

.menu-item:hover {
  background: var(--bg-secondary);
  color: var(--text-primary);
}

.menu-item.active {
  background: var(--primary-color);
  color: white;
}

.settings-content {
  background: var(--bg-primary);
  border-radius: var(--radius-xl);
  padding: var(--spacing-2xl);
  box-shadow: var(--shadow-md);
}

.settings-section {
  max-width: 600px;
}

.section-header {
  margin-bottom: var(--spacing-2xl);
}

.section-header h2 {
  font-size: var(--font-size-2xl);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm) 0;
}

.section-header p {
  color: var(--text-secondary);
  margin: 0;
}

.settings-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xl);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.form-label {
  font-size: var(--font-size-md);
  font-weight: 500;
  color: var(--text-primary);
}

.checkbox-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.input-group {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.input-suffix {
  color: var(--text-secondary);
  font-size: var(--font-size-sm);
}

.config-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-lg);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  margin-bottom: var(--spacing-md);
}

.notification-info {
  flex: 1;
}

.notification-title {
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.notification-desc {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
}

.maintenance-tools {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--spacing-lg);
}

.tool-card {
  display: flex;
  gap: var(--spacing-lg);
  padding: var(--spacing-xl);
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
  transition: all var(--transition-fast);
}

.tool-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.tool-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-lg);
  background: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: var(--font-size-xl);
  flex-shrink: 0;
}

.tool-content {
  flex: 1;
}

.tool-content h3 {
  font-size: var(--font-size-lg);
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 var(--spacing-sm) 0;
}

.tool-content p {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin: 0 0 var(--spacing-lg) 0;
  line-height: 1.5;
}

@media (max-width: 1024px) {
  .settings-container {
    grid-template-columns: 1fr;
    gap: var(--spacing-lg);
  }
  
  .settings-sidebar {
    order: 2;
  }
  
  .sidebar-menu {
    flex-direction: row;
    overflow-x: auto;
    gap: var(--spacing-md);
  }
  
  .menu-item {
    white-space: nowrap;
    flex-shrink: 0;
  }
}

@media (max-width: 768px) {
  .settings {
    padding: var(--spacing-lg);
  }
  
  .header-content {
    flex-direction: column;
    gap: var(--spacing-lg);
    align-items: flex-start;
  }
  
  .settings-content {
    padding: var(--spacing-lg);
  }
  
  .maintenance-tools {
    grid-template-columns: 1fr;
  }
  
  .tool-card {
    flex-direction: column;
    text-align: center;
  }
}
</style>

