<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

export default {
  name: 'App',
  
  created() {
    // 应用启动时检测后端连接状态
    this.checkBackendConnection()
  },
  
  methods: {
    async checkBackendConnection() {
      try {
        // 使用测试连接方法
        const result = await request.testConnection()
        
        if (!result.success) {
          ElMessage({
            message: `后端连接警告: ${result.message}`,
            type: 'warning',
            duration: 10000,
            showClose: true,
          })
        } else {
          console.log('后端连接成功:', result.message)
        }
      } catch (error) {
        console.error('检测后端连接失败:', error)
        ElMessage({
          message: '无法连接到后端服务，请确认服务已启动',
          type: 'error',
          duration: 0,
          showClose: true,
        })
      }
    }
  }
}
</script>

<style>
#app {
  height: 100vh;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  background-color: #f5f5f5;
}

.clearfix::after {
  content: "";
  display: table;
  clear: both;
}

.text-center {
  text-align: center;
}

.text-right {
  text-align: right;
}

.text-left {
  text-align: left;
}

.pull-left {
  float: left;
}

.pull-right {
  float: right;
}

.mb-10 {
  margin-bottom: 10px;
}

.mb-20 {
  margin-bottom: 20px;
}

.mt-10 {
  margin-top: 10px;
}

.mt-20 {
  margin-top: 20px;
}
</style>

