<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" class="register-form">
    <el-form-item label="租户代码" prop="tenantCode">
      <el-input v-model="form.tenantCode" placeholder="请输入租户代码" />
    </el-form-item>
    <el-form-item label="企业名称" prop="tenantName">
      <el-input v-model="form.tenantName" placeholder="请输入企业名称" />
    </el-form-item>
    <el-form-item label="联系人" prop="contactName">
      <el-input v-model="form.contactName" placeholder="请输入联系人姓名" />
    </el-form-item>
    <el-form-item label="联系电话" prop="contactPhone">
      <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
    </el-form-item>
    <el-form-item label="联系邮箱" prop="contactEmail">
      <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
    </el-form-item>
    <el-form-item label="部门列表" prop="departments">
      <div v-for="(dept, idx) in form.departments" :key="idx" class="dept-item">
        <el-input v-model="form.departments[idx]" placeholder="请输入部门名称" style="width: 80%" />
        <el-button icon="el-icon-delete" @click="removeDept(idx)" v-if="form.departments.length > 1" circle type="danger" size="small" />
      </div>
      <el-button type="primary" icon="el-icon-plus" @click="addDept" size="small" style="margin-top:8px;">添加部门</el-button>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { registerTenant, getCaptcha } from '@/api/auth'

export default {
  name: 'TenantRegisterForm',
  setup() {
    const formRef = ref(null)
    const form = reactive({
      tenantCode: '',
      tenantName: '',
      contactName: '',
      contactPhone: '',
      contactEmail: '',
      departments: [''],
      captcha: '',
      captchaKey: ''
    })
    const rules = {
      tenantCode: [{ required: true, message: '请输入租户代码', trigger: 'blur' }],
      tenantName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
      contactName: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
      contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }, { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }],
      contactEmail: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }],
      departments: [{ required: true, type: 'array', min: 1, message: '请至少填写一个部门', trigger: 'change' }],
      captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
    }
    const loading = ref(false)
    const captchaImg = ref('')
    const addDept = () => { form.departments.push('') }
    const removeDept = (idx) => { form.departments.splice(idx, 1) }
    const refreshCaptcha = async () => {
      const res = await getCaptcha()
      if (res.code === 200) {
        captchaImg.value = res.data.img
        form.captchaKey = res.data.key
      }
    }
    const submitForm = () => {
      formRef.value.validate(async (valid) => {
        if (!valid) return
        loading.value = true
        try {
          const res = await registerTenant({ ...form })
          if (res.code === 200) {
            ElMessage.success('企业注册成功，请登录')
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
    onMounted(() => { refreshCaptcha() })
    return { formRef, form, rules, loading, captchaImg, addDept, removeDept, refreshCaptcha, submitForm }
  }
}
</script>

<style scoped>
.register-form { max-width: 400px; margin: 0 auto; }
.dept-item { display: flex; align-items: center; margin-bottom: 6px; }
.captcha-img { height: 36px; cursor: pointer; border-radius: 4px; border: 1px solid #eee; }
</style> 