<template>
  <div class="system-settings">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-setting"></i>
          系统设置
        </h1>
        <p class="page-description">管理系统配置和参数设置</p>
      </div>
    </div>

    <div class="settings-container">
      <!-- 左侧导航 -->
      <div class="settings-sidebar">
        <el-card class="nav-card">
          <el-menu
            :default-active="activeMenu"
            @select="handleMenuSelect"
            class="settings-menu"
          >
            <el-menu-item index="basic">
              <i class="el-icon-info"></i>
              <span>基本设置</span>
            </el-menu-item>
            <el-menu-item index="email">
              <i class="el-icon-message"></i>
              <span>邮件配置</span>
            </el-menu-item>
            <el-menu-item index="storage">
              <i class="el-icon-folder"></i>
              <span>存储配置</span>
            </el-menu-item>
            <el-menu-item index="security">
              <i class="el-icon-lock"></i>
              <span>安全设置</span>
            </el-menu-item>
            <el-menu-item index="backup">
              <i class="el-icon-download"></i>
              <span>备份设置</span>
            </el-menu-item>
            <el-menu-item index="logs">
              <i class="el-icon-document"></i>
              <span>日志配置</span>
            </el-menu-item>
            <el-menu-item index="performance">
              <i class="el-icon-cpu"></i>
              <span>性能优化</span>
            </el-menu-item>
            <el-menu-item index="integration">
              <i class="el-icon-connection"></i>
              <span>第三方集成</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </div>

      <!-- 右侧内容 -->
      <div class="settings-content">
        <el-card class="content-card">
          <!-- 基本设置 -->
          <div v-show="activeMenu === 'basic'" class="setting-section">
            <div class="section-header">
              <h3>基本设置</h3>
              <p>配置系统的基本信息和参数</p>
            </div>
            <el-form :model="basicSettings" label-width="120px" class="setting-form">
              <el-form-item label="系统名称">
                <el-input v-model="basicSettings.systemName" placeholder="请输入系统名称" />
              </el-form-item>
              <el-form-item label="系统版本">
                <el-input v-model="basicSettings.systemVersion" placeholder="请输入系统版本" />
              </el-form-item>
              <el-form-item label="公司名称">
                <el-input v-model="basicSettings.companyName" placeholder="请输入公司名称" />
              </el-form-item>
              <el-form-item label="联系邮箱">
                <el-input v-model="basicSettings.contactEmail" placeholder="请输入联系邮箱" />
              </el-form-item>
              <el-form-item label="系统logo">
                <el-upload
                  class="logo-uploader"
                  action="/api/upload"
                  :show-file-list="false"
                  :on-success="handleLogoSuccess"
                  :before-upload="beforeLogoUpload"
                >
                  <img v-if="basicSettings.logo" :src="basicSettings.logo" class="logo-image" />
                  <i v-else class="el-icon-plus logo-uploader-icon"></i>
                </el-upload>
              </el-form-item>
              <el-form-item label="时区设置">
                <el-select v-model="basicSettings.timezone" placeholder="请选择时区" style="width: 100%">
                  <el-option label="北京时间 (UTC+8)" value="Asia/Shanghai" />
                  <el-option label="东京时间 (UTC+9)" value="Asia/Tokyo" />
                  <el-option label="纽约时间 (UTC-5)" value="America/New_York" />
                  <el-option label="伦敦时间 (UTC+0)" value="Europe/London" />
                </el-select>
              </el-form-item>
              <el-form-item label="语言设置">
                <el-select v-model="basicSettings.language" placeholder="请选择语言" style="width: 100%">
                  <el-option label="简体中文" value="zh-CN" />
                  <el-option label="繁体中文" value="zh-TW" />
                  <el-option label="English" value="en-US" />
                  <el-option label="日本語" value="ja-JP" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveBasic" :loading="saving">保存设置</el-button>
                <el-button @click="handleResetBasic">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 邮件配置 -->
          <div v-show="activeMenu === 'email'" class="setting-section">
            <div class="section-header">
              <h3>邮件配置</h3>
              <p>配置系统邮件发送服务</p>
            </div>
            <el-form :model="emailSettings" label-width="120px" class="setting-form">
              <el-form-item label="SMTP服务器">
                <el-input v-model="emailSettings.smtpHost" placeholder="请输入SMTP服务器地址" />
              </el-form-item>
              <el-form-item label="SMTP端口">
                <el-input-number v-model="emailSettings.smtpPort" :min="1" :max="65535" style="width: 100%" />
              </el-form-item>
              <el-form-item label="发件人邮箱">
                <el-input v-model="emailSettings.fromEmail" placeholder="请输入发件人邮箱" />
              </el-form-item>
              <el-form-item label="发件人名称">
                <el-input v-model="emailSettings.fromName" placeholder="请输入发件人名称" />
              </el-form-item>
              <el-form-item label="邮箱密码">
                <el-input v-model="emailSettings.password" type="password" placeholder="请输入邮箱密码" show-password />
              </el-form-item>
              <el-form-item label="加密方式">
                <el-select v-model="emailSettings.encryption" placeholder="请选择加密方式" style="width: 100%">
                  <el-option label="无加密" value="none" />
                  <el-option label="SSL" value="ssl" />
                  <el-option label="TLS" value="tls" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveEmail" :loading="saving">保存设置</el-button>
                <el-button @click="handleTestEmail" :loading="testing">测试连接</el-button>
                <el-button @click="handleResetEmail">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 存储配置 -->
          <div v-show="activeMenu === 'storage'" class="setting-section">
            <div class="section-header">
              <h3>存储配置</h3>
              <p>配置文件存储和上传设置</p>
            </div>
            <el-form :model="storageSettings" label-width="120px" class="setting-form">
              <el-form-item label="存储方式">
                <el-radio-group v-model="storageSettings.type">
                  <el-radio label="local">本地存储</el-radio>
                  <el-radio label="oss">阿里云OSS</el-radio>
                  <el-radio label="cos">腾讯云COS</el-radio>
                  <el-radio label="qiniu">七牛云</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="上传路径" v-if="storageSettings.type === 'local'">
                <el-input v-model="storageSettings.localPath" placeholder="请输入本地存储路径" />
              </el-form-item>
              <el-form-item label="访问域名" v-if="storageSettings.type !== 'local'">
                <el-input v-model="storageSettings.domain" placeholder="请输入访问域名" />
              </el-form-item>
              <el-form-item label="Access Key" v-if="storageSettings.type !== 'local'">
                <el-input v-model="storageSettings.accessKey" placeholder="请输入Access Key" />
              </el-form-item>
              <el-form-item label="Secret Key" v-if="storageSettings.type !== 'local'">
                <el-input v-model="storageSettings.secretKey" type="password" placeholder="请输入Secret Key" show-password />
              </el-form-item>
              <el-form-item label="存储桶名称" v-if="storageSettings.type !== 'local'">
                <el-input v-model="storageSettings.bucket" placeholder="请输入存储桶名称" />
              </el-form-item>
              <el-form-item label="文件大小限制">
                <el-input-number v-model="storageSettings.maxSize" :min="1" :max="1024" style="width: 200px" />
                <span style="margin-left: 8px;">MB</span>
              </el-form-item>
              <el-form-item label="允许的文件类型">
                <el-input v-model="storageSettings.allowedTypes" placeholder="如：jpg,png,pdf,doc" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveStorage" :loading="saving">保存设置</el-button>
                <el-button @click="handleTestStorage" :loading="testing">测试连接</el-button>
                <el-button @click="handleResetStorage">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 安全设置 -->
          <div v-show="activeMenu === 'security'" class="setting-section">
            <div class="section-header">
              <h3>安全设置</h3>
              <p>配置系统安全策略和参数</p>
            </div>
            <el-form :model="securitySettings" label-width="140px" class="setting-form">
              <el-form-item label="密码最小长度">
                <el-input-number v-model="securitySettings.minPasswordLength" :min="6" :max="20" style="width: 200px" />
              </el-form-item>
              <el-form-item label="密码复杂度要求">
                        <el-checkbox-group v-model="securitySettings.passwordRules">
          <el-checkbox value="uppercase">包含大写字母</el-checkbox>
          <el-checkbox value="lowercase">包含小写字母</el-checkbox>
          <el-checkbox value="number">包含数字</el-checkbox>
          <el-checkbox value="special">包含特殊字符</el-checkbox>
        </el-checkbox-group>
              </el-form-item>
              <el-form-item label="登录失败锁定">
                <el-switch v-model="securitySettings.loginLockEnabled" />
              </el-form-item>
              <el-form-item label="最大失败次数" v-if="securitySettings.loginLockEnabled">
                <el-input-number v-model="securitySettings.maxLoginAttempts" :min="3" :max="10" style="width: 200px" />
              </el-form-item>
              <el-form-item label="锁定时间(分钟)" v-if="securitySettings.loginLockEnabled">
                <el-input-number v-model="securitySettings.lockDuration" :min="5" :max="60" style="width: 200px" />
              </el-form-item>
              <el-form-item label="会话超时时间">
                <el-input-number v-model="securitySettings.sessionTimeout" :min="30" :max="1440" style="width: 200px" />
                <span style="margin-left: 8px;">分钟</span>
              </el-form-item>
              <el-form-item label="强制HTTPS">
                <el-switch v-model="securitySettings.forceHttps" />
              </el-form-item>
              <el-form-item label="IP白名单">
                <el-input
                  v-model="securitySettings.ipWhitelist"
                  type="textarea"
                  :rows="3"
                  placeholder="每行一个IP地址或IP段，如：192.168.1.1 或 192.168.1.0/24"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveSecurity" :loading="saving">保存设置</el-button>
                <el-button @click="handleResetSecurity">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 备份设置 -->
          <div v-show="activeMenu === 'backup'" class="setting-section">
            <div class="section-header">
              <h3>备份设置</h3>
              <p>配置数据备份策略和计划</p>
            </div>
            <el-form :model="backupSettings" label-width="120px" class="setting-form">
              <el-form-item label="自动备份">
                <el-switch v-model="backupSettings.autoBackup" />
              </el-form-item>
              <el-form-item label="备份频率" v-if="backupSettings.autoBackup">
                <el-select v-model="backupSettings.frequency" placeholder="请选择备份频率" style="width: 200px">
                  <el-option label="每日" value="daily" />
                  <el-option label="每周" value="weekly" />
                  <el-option label="每月" value="monthly" />
                </el-select>
              </el-form-item>
              <el-form-item label="备份时间" v-if="backupSettings.autoBackup">
                <el-time-picker v-model="backupSettings.backupTime" placeholder="选择备份时间" style="width: 200px" />
              </el-form-item>
              <el-form-item label="保留份数">
                <el-input-number v-model="backupSettings.retentionCount" :min="1" :max="30" style="width: 200px" />
              </el-form-item>
              <el-form-item label="备份路径">
                <el-input v-model="backupSettings.backupPath" placeholder="请输入备份存储路径" />
              </el-form-item>
              <el-form-item label="压缩备份">
                <el-switch v-model="backupSettings.compress" />
              </el-form-item>
              <el-form-item label="备份内容">
                        <el-checkbox-group v-model="backupSettings.backupContent">
          <el-checkbox value="database">数据库</el-checkbox>
          <el-checkbox value="files">上传文件</el-checkbox>
          <el-checkbox value="config">配置文件</el-checkbox>
          <el-checkbox value="logs">日志文件</el-checkbox>
        </el-checkbox-group>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSaveBackup" :loading="saving">保存设置</el-button>
                <el-button type="success" @click="handleManualBackup" :loading="backing">立即备份</el-button>
                <el-button @click="handleResetBackup">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 其他设置面板... -->
          <div v-show="activeMenu === 'logs'" class="setting-section">
            <div class="section-header">
              <h3>日志配置</h3>
              <p>配置系统日志记录和管理</p>
            </div>
            <el-empty description="日志配置功能开发中..." />
          </div>

          <div v-show="activeMenu === 'performance'" class="setting-section">
            <div class="section-header">
              <h3>性能优化</h3>
              <p>配置系统性能优化参数</p>
            </div>
            <el-empty description="性能优化功能开发中..." />
          </div>

          <div v-show="activeMenu === 'integration'" class="setting-section">
            <div class="section-header">
              <h3>第三方集成</h3>
              <p>配置第三方服务集成</p>
            </div>
            <el-empty description="第三方集成功能开发中..." />
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'SystemSettings',
  setup() {
    // 响应式数据
    const activeMenu = ref('basic')
    const saving = ref(false)
    const testing = ref(false)
    const backing = ref(false)

    // 基本设置
    const basicSettings = reactive({
      systemName: '测盟汇管理系统',
      systemVersion: 'v1.0.0',
      companyName: '测盟汇科技有限公司',
      contactEmail: 'admin@cemh.com',
      logo: '',
      timezone: 'Asia/Shanghai',
      language: 'zh-CN'
    })

    // 邮件设置
    const emailSettings = reactive({
      smtpHost: 'smtp.qq.com',
      smtpPort: 587,
      fromEmail: 'noreply@cemh.com',
      fromName: '测盟汇系统',
      password: '',
      encryption: 'tls'
    })

    // 存储设置
    const storageSettings = reactive({
      type: 'local',
      localPath: '/uploads',
      domain: '',
      accessKey: '',
      secretKey: '',
      bucket: '',
      maxSize: 10,
      allowedTypes: 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx'
    })

    // 安全设置
    const securitySettings = reactive({
      minPasswordLength: 8,
      passwordRules: ['lowercase', 'number'],
      loginLockEnabled: true,
      maxLoginAttempts: 5,
      lockDuration: 15,
      sessionTimeout: 120,
      forceHttps: false,
      ipWhitelist: ''
    })

    // 备份设置
    const backupSettings = reactive({
      autoBackup: true,
      frequency: 'daily',
      backupTime: new Date(),
      retentionCount: 7,
      backupPath: '/backups',
      compress: true,
      backupContent: ['database', 'files', 'config']
    })

    // 菜单选择
    const handleMenuSelect = (index) => {
      activeMenu.value = index
    }

    // logo上传成功
    const handleLogoSuccess = (response, file) => {
      basicSettings.logo = URL.createObjectURL(file.raw)
      ElMessage.success('Logo上传成功')
    }

    // logo上传前验证
    const beforeLogoUpload = (file) => {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        ElMessage.error('上传logo图片只能是 JPG/PNG 格式!')
      }
      if (!isLt2M) {
        ElMessage.error('上传logo图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }

    // 保存基本设置
    const handleSaveBasic = async () => {
      saving.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('基本设置保存成功')
      } catch (error) {
        ElMessage.error('保存失败')
      } finally {
        saving.value = false
      }
    }

    // 重置基本设置
    const handleResetBasic = () => {
      Object.assign(basicSettings, {
        systemName: '测盟汇管理系统',
        systemVersion: 'v1.0.0',
        companyName: '测盟汇科技有限公司',
        contactEmail: 'admin@cemh.com',
        logo: '',
        timezone: 'Asia/Shanghai',
        language: 'zh-CN'
      })
      ElMessage.info('已重置为默认设置')
    }

    // 保存邮件设置
    const handleSaveEmail = async () => {
      saving.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('邮件配置保存成功')
      } finally {
        saving.value = false
      }
    }

    // 测试邮件连接
    const handleTestEmail = async () => {
      testing.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 2000))
        ElMessage.success('邮件服务器连接测试成功')
      } catch (error) {
        ElMessage.error('邮件服务器连接测试失败')
      } finally {
        testing.value = false
      }
    }

    // 重置邮件设置
    const handleResetEmail = () => {
      Object.assign(emailSettings, {
        smtpHost: '',
        smtpPort: 587,
        fromEmail: '',
        fromName: '',
        password: '',
        encryption: 'tls'
      })
      ElMessage.info('已重置邮件配置')
    }

    // 保存存储设置
    const handleSaveStorage = async () => {
      saving.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('存储配置保存成功')
      } finally {
        saving.value = false
      }
    }

    // 测试存储连接
    const handleTestStorage = async () => {
      testing.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 2000))
        ElMessage.success('存储服务连接测试成功')
      } catch (error) {
        ElMessage.error('存储服务连接测试失败')
      } finally {
        testing.value = false
      }
    }

    // 重置存储设置
    const handleResetStorage = () => {
      Object.assign(storageSettings, {
        type: 'local',
        localPath: '/uploads',
        domain: '',
        accessKey: '',
        secretKey: '',
        bucket: '',
        maxSize: 10,
        allowedTypes: 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx'
      })
      ElMessage.info('已重置存储配置')
    }

    // 保存安全设置
    const handleSaveSecurity = async () => {
      saving.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('安全设置保存成功')
      } finally {
        saving.value = false
      }
    }

    // 重置安全设置
    const handleResetSecurity = () => {
      Object.assign(securitySettings, {
        minPasswordLength: 8,
        passwordRules: ['lowercase', 'number'],
        loginLockEnabled: true,
        maxLoginAttempts: 5,
        lockDuration: 15,
        sessionTimeout: 120,
        forceHttps: false,
        ipWhitelist: ''
      })
      ElMessage.info('已重置安全设置')
    }

    // 保存备份设置
    const handleSaveBackup = async () => {
      saving.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 1000))
        ElMessage.success('备份设置保存成功')
      } finally {
        saving.value = false
      }
    }

    // 立即备份
    const handleManualBackup = async () => {
      backing.value = true
      try {
        await new Promise(resolve => setTimeout(resolve, 3000))
        ElMessage.success('数据备份完成')
      } catch (error) {
        ElMessage.error('数据备份失败')
      } finally {
        backing.value = false
      }
    }

    // 重置备份设置
    const handleResetBackup = () => {
      Object.assign(backupSettings, {
        autoBackup: true,
        frequency: 'daily',
        backupTime: new Date(),
        retentionCount: 7,
        backupPath: '/backups',
        compress: true,
        backupContent: ['database', 'files', 'config']
      })
      ElMessage.info('已重置备份设置')
    }

    return {
      activeMenu,
      saving,
      testing,
      backing,
      basicSettings,
      emailSettings,
      storageSettings,
      securitySettings,
      backupSettings,
      handleMenuSelect,
      handleLogoSuccess,
      beforeLogoUpload,
      handleSaveBasic,
      handleResetBasic,
      handleSaveEmail,
      handleTestEmail,
      handleResetEmail,
      handleSaveStorage,
      handleTestStorage,
      handleResetStorage,
      handleSaveSecurity,
      handleResetSecurity,
      handleSaveBackup,
      handleManualBackup,
      handleResetBackup
    }
  }
}
</script>

<style scoped>
.system-settings {
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

.settings-container {
  display: flex;
  gap: 20px;
}

.settings-sidebar {
  width: 250px;
  flex-shrink: 0;
}

.settings-content {
  flex: 1;
}

.nav-card,
.content-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.settings-menu {
  border: none;
}

.settings-menu .el-menu-item {
  border-radius: 8px;
  margin-bottom: 4px;
}

.settings-menu .el-menu-item.is-active {
  background-color: #409eff;
  color: white;
}

.setting-section {
  padding: 20px 0;
}

.section-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.section-header h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 20px;
}

.section-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.setting-form {
  max-width: 600px;
}

.logo-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-uploader:hover {
  border-color: #409eff;
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.logo-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .settings-container {
    flex-direction: column;
  }
  
  .settings-sidebar {
    width: 100%;
  }
  
  .settings-menu {
    display: flex;
    overflow-x: auto;
  }
  
  .settings-menu .el-menu-item {
    white-space: nowrap;
    margin-right: 8px;
    margin-bottom: 0;
  }
}

@media (max-width: 768px) {
  .system-settings {
    padding: 10px;
  }
  
  .setting-form {
    max-width: 100%;
  }
  
  .setting-form .el-form-item__label {
    width: 100px !important;
  }
}
</style>

