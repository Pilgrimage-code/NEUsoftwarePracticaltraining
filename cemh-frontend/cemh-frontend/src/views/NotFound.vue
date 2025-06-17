<template>
  <div class="not-found">
    <div class="not-found-container">
      <div class="not-found-content">
        <div class="error-code">404</div>
        <div class="error-title">页面不存在</div>
        <div class="error-description">
          抱歉，您访问的页面不存在或已被删除
        </div>
        <div class="error-actions">
          <el-button type="primary" @click="goHome">
            <i class="el-icon-house"></i>
            返回首页
          </el-button>
          <el-button @click="goBack">
            <i class="el-icon-back"></i>
            返回上页
          </el-button>
        </div>
      </div>
      <div class="not-found-image">
        <svg viewBox="0 0 404 404" class="error-svg">
          <defs>
            <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" style="stop-color:#667eea;stop-opacity:1" />
              <stop offset="100%" style="stop-color:#764ba2;stop-opacity:1" />
            </linearGradient>
          </defs>
          <circle cx="202" cy="202" r="180" fill="url(#gradient)" opacity="0.1"/>
          <text x="202" y="220" text-anchor="middle" font-size="120" font-weight="bold" fill="url(#gradient)">404</text>
        </svg>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  name: 'NotFound',
  setup() {
    const router = useRouter()
    
    // 返回首页
    const goHome = () => {
      router.push('/dashboard')
    }
    
    // 返回上一页
    const goBack = () => {
      router.go(-1)
    }
    
    return {
      goHome,
      goBack
    }
  }
}
</script>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
}

.not-found-container {
  display: flex;
  align-items: center;
  gap: 60px;
  max-width: 800px;
  width: 100%;
}

.not-found-content {
  flex: 1;
  text-align: center;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1;
  margin-bottom: 20px;
}

.error-title {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
}

.error-description {
  font-size: 16px;
  color: #606266;
  margin-bottom: 40px;
  line-height: 1.6;
}

.error-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.not-found-image {
  flex: 1;
  max-width: 300px;
}

.error-svg {
  width: 100%;
  height: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .not-found-container {
    flex-direction: column;
    gap: 40px;
    text-align: center;
  }
  
  .error-code {
    font-size: 80px;
  }
  
  .error-title {
    font-size: 24px;
  }
  
  .error-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .not-found-image {
    max-width: 200px;
  }
}

@media (max-width: 480px) {
  .error-code {
    font-size: 60px;
  }
  
  .error-title {
    font-size: 20px;
  }
  
  .error-description {
    font-size: 14px;
  }
}
</style>

