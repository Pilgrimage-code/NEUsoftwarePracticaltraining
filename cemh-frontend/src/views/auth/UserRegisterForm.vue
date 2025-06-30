<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" class="register-form">
    <el-form-item label="所属企业" prop="tenantId">
      <el-select v-model="form.tenantId" placeholder="请选择企业" filterable @change="fetchDepts">
        <el-option v-for="item in tenantList" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <el-form-item label="所属部门" prop="deptId">
      <el-select v-model="form.deptId" placeholder="请选择部门" filterable :disabled="!form.tenantId">
        <el-option v-for="item in deptList" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <el-form-item label="用户名" prop="username">
      <el-input v-model="form.username" placeholder="请输入用户名" />
    </el-form-item>
    <el-form-item label="昵称" prop="nickname">
      <el-input v-model="form.nickname" placeholder="请输入昵称" />
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="form.phone" placeholder="请输入手机号" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" placeholder="请输入邮箱" />
    </el-form-item>
    <el-form-item label="头像" prop="avatar">
      <el-upload
        class="avatar-uploader"
        action="/api/upload/avatar"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        accept="image/*"
      >
        <img v-if="form.avatar" :src="form.avatar" class="avatar" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </el-form-item>
    <el-form-item label="验证码" prop="captcha">
      <el-row gutter="10">
        <el-col :span="14">
          <el-input v-model="form.captcha" maxlength="6" placeholder="请输入验证码" />
        </el-col>
        <el-col :span="10">
          <img :src="captchaImg" @click="refreshCaptcha" class="captcha-img" title="点击刷新" />
        </el-col>
      </el-row>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm" :loading="loading">注册</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getTenants, getDeptsByTenant, registerUser, getCaptcha } from '@/api/auth'

export default {
  name: 'UserRegisterForm',
  setup() {
    const formRef = ref(null)
    const form = reactive({
      tenantId: '',
      deptId: '',
      username: '',
      nickname: '',
      password: '',
      phone: '',
      email: '',
      avatar: '',
      captcha: '',
      captchaKey: ''
    })
    const rules = {
      tenantId: [{ required: true, message: '请选择企业', trigger: 'change' }],
      deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }],
      phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }, { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
      email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }],
      avatar: [{ required: true, message: '请上传头像', trigger: 'change' }],
      captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
    }
    const tenantList = ref([])
    const deptList = ref([])
    const loading = ref(false)
    const captchaImg = ref('')

    const fetchTenants = async () => {
      try {
        const res = await getTenants()
        if (res.code === 200) {
          tenantList.value = res.data.map(item => ({ value: item.id, label: item.tenantName }))
        }
      } catch (e) {
        ElMessage.error('获取企业列表失败')
      }
    }
    const fetchDepts = async (tenantId) => {
      form.deptId = ''
      if (!tenantId) {
        deptList.value = []
        return
      }
      try {
        const res = await getDeptsByTenant(tenantId)
        if (res.code === 200) {
          deptList.value = res.data.map(item => ({ value: item.id, label: item.deptName }))
        }
      } catch (e) {
        ElMessage.error('获取部门列表失败')
      }
    }
    const refreshCaptcha = async () => {
      const res = await getCaptcha()
      if (res.code === 200) {
        captchaImg.value = res.data.img
        form.captchaKey = res.data.key
      }
    }
    const handleAvatarSuccess = (res) => {
      if (res.code === 200) {
        form.avatar = res.data.url
        ElMessage.success('头像上传成功')
      } else {
        ElMessage.error(res.message || '头像上传失败')
      }
    }
    const beforeAvatarUpload = (file) => {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isImage) ElMessage.error('只能上传图片文件!')
      if (!isLt2M) ElMessage.error('图片大小不能超过2MB!')
      return isImage && isLt2M
    }
    const submitForm = () => {
      formRef.value.validate(async (valid) => {
        if (!valid) return
        loading.value = true
        try {
          const res = await registerUser({ ...form })
          if (res.code === 200) {
            ElMessage.success('注册成功，请登录')
            window.location.href = '/login'
          } else {
            ElMessage.error(res.message || '注册失败')
            refreshCaptcha()
          }
        } catch (e) {
          ElMessage.error('注册异常')
          refreshCaptcha()
        } finally {
          loading.value = false
        }
      })
    }
    onMounted(() => {
      fetchTenants()
      refreshCaptcha()
    })
    watch(() => form.tenantId, (val) => {
      fetchDepts(val)
    })
    return { formRef, form, rules, tenantList, deptList, loading, captchaImg, fetchDepts, refreshCaptcha, handleAvatarSuccess, beforeAvatarUpload, submitForm }
  }
}
</script>

<style scoped>
.register-form { max-width: 400px; margin: 0 auto; }
.avatar-uploader { display: inline-block; }
.avatar { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; }
.avatar-uploader-icon { font-size: 32px; color: #aaa; }
.captcha-img { height: 36px; cursor: pointer; border-radius: 4px; border: 1px solid #eee; }
</style> 