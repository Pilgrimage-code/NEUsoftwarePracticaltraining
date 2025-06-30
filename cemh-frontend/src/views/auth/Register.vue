<template>
  <div class="register-page">
    <el-card class="register-card">
      <el-tabs v-model="activeTab" stretch>
        <el-tab-pane label="用户注册" name="user">
          <user-register-form />
        </el-tab-pane>
        <el-tab-pane label="企业注册" name="tenant">
          <tenant-register-form />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import UserRegisterForm from './UserRegisterForm.vue'
import TenantRegisterForm from './TenantRegisterForm.vue'

export default {
  name: 'Register',
  components: { UserRegisterForm, TenantRegisterForm },
  setup() {
    const activeTab = ref('user')
    return { activeTab }
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
}
.register-card {
  width: 480px;
  padding: 40px 30px;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(102, 126, 234, 0.12);
}
</style> 