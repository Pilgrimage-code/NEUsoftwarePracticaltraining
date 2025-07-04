<template>
  <div class="tenant-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-office-building"></i>
          租户管理
        </h1>
        <p class="page-description">管理系统租户和多租户配置</p>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-card class="search-card">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="租户名称">
            <el-input
              v-model="searchForm.name"
              placeholder="请输入租户名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="租户代码">
            <el-input
              v-model="searchForm.code"
              placeholder="请输入租户代码"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="正常" :value="1" />
              <el-option label="停用" :value="0" />
              <el-option label="过期" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="套餐类型">
            <el-select v-model="searchForm.packageType" placeholder="请选择套餐" clearable style="width: 120px">
              <el-option label="基础版" :value="1" />
              <el-option label="专业版" :value="2" />
              <el-option label="企业版" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :loading="loading">
              <i class="el-icon-search"></i>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <i class="el-icon-refresh"></i>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon total">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalTenants }}</div>
                <div class="stat-label">总租户数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon active">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.activeTenants }}</div>
                <div class="stat-label">活跃租户</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon expired">
                <i class="el-icon-warning"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.expiredTenants }}</div>
                <div class="stat-label">即将过期</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-icon revenue">
                <i class="el-icon-money"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">¥{{ stats.monthlyRevenue }}</div>
                <div class="stat-label">月收入</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i>
          新增租户
        </el-button>
        <el-button type="success" @click="handleBatchRenew" :disabled="selectedTenants.length === 0">
          <i class="el-icon-refresh"></i>
          批量续费
        </el-button>
        <el-button type="warning" @click="handleBatchNotify" :disabled="selectedTenants.length === 0">
          <i class="el-icon-message"></i>
          批量通知
        </el-button>
        <el-button type="info" @click="handleExport">
          <i class="el-icon-download"></i>
          导出数据
        </el-button>
        <el-button type="danger" @click="showDeletedDialog = true">
          最近删除
        </el-button>
      </div>
    </div>

    <!-- 租户列表 -->
    <div class="table-section">
      <el-card class="table-card">
        <el-table
          :data="tenantList"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column label="租户信息" min-width="250">
            <template #default="{ row }">
              <div class="tenant-info">
                <div class="tenant-logo">
                  <img v-if="row.logo" :src="row.logo" alt="租户logo" />
                  <div v-else class="default-logo">{{ row.name.charAt(0) }}</div>
                </div>
                <div class="tenant-details">
                  <div class="tenant-name">{{ row.name }}</div>
                  <div class="tenant-code">{{ row.code }}</div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="contactPerson" label="联系人" width="100" />
          <el-table-column prop="contactPhone" label="联系电话" width="130" />
          <el-table-column label="套餐类型" width="100">
            <template #default="{ row }">
              <el-tag :type="getPackageType(row.packageType)">
                {{ getPackageText(row.packageType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="userCount" label="用户数" width="80" />
          <el-table-column prop="maxUsers" label="用户上限" width="80" />
          <el-table-column label="到期时间" width="120">
            <template #default="{ row }">
              <span :class="getExpireClass(row.expireTime)">
                {{ formatDate(row.expireTime, 'YYYY-MM-DD') }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleView(row)">
                <i class="el-icon-view"></i>
                查看
              </el-button>
              <el-button size="small" @click="handleEdit(row)">
                <i class="el-icon-edit"></i>
                编辑
              </el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">
                <i class="el-icon-delete"></i>
                删除
              </el-button>
              <el-dropdown @command="(command) => handleDropdownCommand(command, row)">
                <el-button size="small" type="info">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item :command="`renew-${row.id}`">
                      续费管理
                    </el-dropdown-item>
                    <el-dropdown-item :command="`config-${row.id}`">
                      配置管理
                    </el-dropdown-item>
                    <el-dropdown-item :command="`users-${row.id}`">
                      用户管理
                    </el-dropdown-item>
                    <el-dropdown-item :command="`stats-${row.id}`">
                      使用统计
                    </el-dropdown-item>
                    <el-dropdown-item :command="`backup-${row.id}`" divided>
                      数据备份
                    </el-dropdown-item>
                    <el-dropdown-item 
                      :command="`${row.status === 1 ? 'disable' : 'enable'}-${row.id}`"
                      :class="row.status === 1 ? 'danger-item' : 'success-item'"
                    >
                      {{ row.status === 1 ? '停用租户' : '启用租户' }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 租户表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="tenantFormRef"
        :model="tenantForm"
        :rules="tenantFormRules"
        label-width="120px"
      >
        <el-tabs v-model="activeTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-form-item label="租户名称" prop="name">
              <el-input v-model="tenantForm.name" placeholder="请输入租户名称" />
            </el-form-item>
            <el-form-item label="租户代码" prop="code">
              <el-input v-model="tenantForm.code" placeholder="请输入租户代码" />
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="联系人" prop="contactPerson">
                  <el-input v-model="tenantForm.contactPerson" placeholder="请输入联系人" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="contactPhone">
                  <el-input v-model="tenantForm.contactPhone" placeholder="请输入联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="tenantForm.contactEmail" placeholder="请输入联系邮箱" />
            </el-form-item>
            <el-form-item label="公司地址" prop="address">
              <el-input v-model="tenantForm.address" placeholder="请输入公司地址" />
            </el-form-item>
            <el-form-item label="租户logo">
              <el-upload
                class="logo-uploader"
                action="/api/upload"
                :show-file-list="false"
                :on-success="handleLogoSuccess"
                :before-upload="beforeLogoUpload"
              >
                <img v-if="tenantForm.logo" :src="tenantForm.logo" class="logo-image" />
                <i v-else class="el-icon-plus logo-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="套餐配置" name="package">
            <el-form-item label="套餐类型" prop="packageType">
              <el-select v-model="tenantForm.packageType" placeholder="请选择套餐类型" style="width: 100%">
                <el-option label="基础版" :value="1" />
                <el-option label="专业版" :value="2" />
                <el-option label="企业版" :value="3" />
              </el-select>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户上限" prop="maxUsers">
                  <el-input-number v-model="tenantForm.maxUsers" :min="1" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="存储空间(GB)" prop="storageLimit">
                  <el-input-number v-model="tenantForm.storageLimit" :min="1" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="开始时间" prop="startTime">
                  <el-date-picker
                    v-model="tenantForm.startTime"
                    type="date"
                    placeholder="请选择开始时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="到期时间" prop="expireTime">
                  <el-date-picker
                    v-model="tenantForm.expireTime"
                    type="date"
                    placeholder="请选择到期时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="功能权限">
                      <el-checkbox-group v-model="tenantForm.features">
          <el-checkbox value="user_management">用户管理</el-checkbox>
          <el-checkbox value="meeting_management">会议管理</el-checkbox>
          <el-checkbox value="news_management">资讯管理</el-checkbox>
          <el-checkbox value="course_management">课程管理</el-checkbox>
          <el-checkbox value="data_analysis">数据分析</el-checkbox>
          <el-checkbox value="api_access">API接口</el-checkbox>
        </el-checkbox-group>
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="其他设置" name="settings">
            <el-form-item label="状态" prop="status">
              <el-select v-model="tenantForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="正常" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="tenantForm.remark"
                type="textarea"
                :rows="4"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 租户详情对话框 -->
    <el-dialog
      title="租户详情"
      v-model="viewDialogVisible"
      width="900px"
    >
      <div v-if="currentTenant" class="tenant-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="租户名称">{{ currentTenant.name }}</el-descriptions-item>
          <el-descriptions-item label="租户代码">{{ currentTenant.code }}</el-descriptions-item>
          <el-descriptions-item label="联系人">{{ currentTenant.contactPerson }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentTenant.contactPhone }}</el-descriptions-item>
          <el-descriptions-item label="联系邮箱">{{ currentTenant.contactEmail }}</el-descriptions-item>
          <el-descriptions-item label="公司地址">{{ currentTenant.address }}</el-descriptions-item>
          <el-descriptions-item label="套餐类型">
            <el-tag :type="getPackageType(currentTenant.packageType)">
              {{ getPackageText(currentTenant.packageType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentTenant.status)">
              {{ getStatusText(currentTenant.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="用户数量">{{ currentTenant.userCount }}/{{ currentTenant.maxUsers }}</el-descriptions-item>
          <el-descriptions-item label="存储使用">{{ currentTenant.storageUsed }}/{{ currentTenant.storageLimit }}GB</el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ formatDate(currentTenant.startTime, 'YYYY-MM-DD') }}</el-descriptions-item>
          <el-descriptions-item label="到期时间">
            <span :class="getExpireClass(currentTenant.expireTime)">
              {{ formatDate(currentTenant.expireTime, 'YYYY-MM-DD') }}
            </span>
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="feature-list">
          <h3>功能权限</h3>
          <el-tag
            v-for="feature in currentTenant.features"
            :key="feature"
            style="margin-right: 8px; margin-bottom: 8px;"
          >
            {{ getFeatureText(feature) }}
          </el-tag>
        </div>
      </div>
    </el-dialog>

    <!-- 最近删除的租户对话框 -->
    <el-dialog v-model="showDeletedDialog" title="最近删除的租户" width="80%">
      <el-table :data="deletedTenantList" ref="deletedTable" @selection-change="handleDeletedSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="租户名称" />
        <el-table-column prop="code" label="租户代码" />
        <el-table-column prop="contactPerson" label="联系人" />
        <el-table-column prop="contactPhone" label="联系电话" />
        <el-table-column prop="status" label="状态" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row)">查看</el-button>
            <el-button size="small" type="success" @click="handleRestore(row)">恢复</el-button>
            <el-button size="small" type="danger" @click="handleRealDelete(row)">彻底删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px;">
        <el-button type="danger" :disabled="deletedSelected.length === 0" @click="handleBatchRealDelete">
          批量彻底删除
        </el-button>
      </div>
    </el-dialog>

    <!-- 续费管理弹窗 -->
    <el-dialog v-model="renewDialogVisible" :title="renewTenantId ? '续费管理' : '批量续费管理'" width="400px">
      <el-form>
        <div v-if="!renewTenantId" class="batch-renew-info">
          <el-alert
            title="批量续费"
            type="info"
            :closable="false"
            description="您正在对多个租户进行批量续费操作，所有选中的租户将增加相同的有效期时间。"
          />
          <div class="selected-count">
            已选择 <span class="highlight">{{ selectedTenants.length }}</span> 个租户
          </div>
        </div>
        <el-form-item label="年">
          <el-input-number v-model="renewForm.years" :min="0" :controls="false" placeholder="可为空" style="width: 100%" />
        </el-form-item>
        <el-form-item label="月">
          <el-input-number v-model="renewForm.months" :min="0" :controls="false" placeholder="可为空" style="width: 100%" />
        </el-form-item>
        <el-form-item label="日">
          <el-input-number v-model="renewForm.days" :min="0" :controls="false" placeholder="可为空" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="renewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRenewSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTenantList as fetchTenantList, createTenant, updateTenant, deleteTenant, realDeleteTenant, renewTenant, disableTenant, enableTenant, batchRenewTenants, exportTenants } from '@/api/tenant'
import request from '@/utils/request'

export default {
  name: 'TenantManagement',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const tenantList = ref([])
    const selectedTenants = ref([])
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogTitle = ref('')
    const tenantFormRef = ref(null)
    const currentTenant = ref(null)
    const activeTab = ref('basic')
    const showDeletedDialog = ref(false)
    const deletedTenantList = ref([])
    const deletedSelected = ref([])
    const renewDialogVisible = ref(false)
    const renewForm = reactive({ years: null, months: null, days: null })
    const renewTenantId = ref(null)

    // 搜索表单
    const searchForm = reactive({
      name: '',
      code: '',
      status: null,
      packageType: null
    })

    // 分页信息
    const pagination = reactive({
      page: 1,
      size: 10,
      total: 0
    })

    // 统计数据
    const stats = reactive({
      totalTenants: 156,
      activeTenants: 142,
      expiredTenants: 8,
      monthlyRevenue: '125,680'
    })

    // 租户表单
    const tenantForm = reactive({
      id: null,
      name: '',
      code: '',
      contactPerson: '',
      contactPhone: '',
      contactEmail: '',
      address: '',
      logo: '',
      packageType: 1,
      maxUsers: 50,
      storageLimit: 10,
      startTime: '',
      expireTime: '',
      features: ['user_management', 'meeting_management'],
      status: 1,
      remark: ''
    })

    // 表单验证规则
    const tenantFormRules = {
      name: [
        { required: true, message: '请输入租户名称', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入租户代码', trigger: 'blur' },
        { pattern: /^[A-Za-z0-9_-]+$/, message: '租户代码只能包含字母、数字、下划线和横线', trigger: 'blur' }
      ],
      contactPerson: [
        { required: true, message: '请输入联系人', trigger: 'blur' }
      ],
      contactPhone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      contactEmail: [
        { required: true, message: '请输入联系邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }

    // 获取租户列表（多条件）
    const getTenantList = async () => {
      loading.value = true
      try {
        const params = {
          page: pagination.page,
          size: pagination.size,
          name: searchForm.name,
          code: searchForm.code,
          status: searchForm.status,
          packageType: searchForm.packageType
        }
        const res = await fetchTenantList(params)
        if (res && res.data) {
          tenantList.value = (res.data.records || []).map(item => ({
            id: item.id,
            name: item.tenantName,
            code: item.tenantCode,
            contactPerson: item.contactName,
            contactPhone: item.contactPhone,
            contactEmail: item.contactEmail,
            logo: item.logoUrl,
            status: item.status,
            packageType: item.createBy,
            expireTime: item.expireTime,
            maxUsers: item.maxUsers,
            remark: item.remark,
            createTime: item.createTime,
            address: item.remark,
          }))
          pagination.total = res.data.total || 0
        } else {
          tenantList.value = []
          pagination.total = 0
        }
      } catch (error) {
        ElMessage.error('获取租户列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.page = 1
      getTenantList()
    }

    // 重置搜索
    const handleReset = () => {
      Object.assign(searchForm, {
        name: '',
        code: '',
        status: null,
        packageType: null
      })
      pagination.page = 1
      getTenantList()
    }

    // 新增租户
    const handleAdd = () => {
      dialogTitle.value = '新增租户'
      resetTenantForm()
      dialogVisible.value = true
    }

    // 编辑租户
    const handleEdit = (row) => {
      dialogTitle.value = '编辑租户'
      Object.assign(tenantForm, {
        ...row,
        startTime: new Date(row.startTime),
        expireTime: new Date(row.expireTime)
      })
      dialogVisible.value = true
    }

    // 查看租户
    const handleView = (row) => {
      currentTenant.value = row
      viewDialogVisible.value = true
    }

    // 下拉菜单命令处理
    const handleDropdownCommand = async (command, row) => {
      const [action, id] = command.split('-')
      
      try {
        switch (action) {
          case 'renew':
            renewTenantId.value = row.id
            renewForm.years = null
            renewForm.months = null
            renewForm.days = null
            renewDialogVisible.value = true
            break
          case 'config':
            ElMessage.info('配置管理功能开发中...')
            break
          case 'users':
            ElMessage.info('用户管理功能开发中...')
            break
          case 'stats':
            ElMessage.info('使用统计功能开发中...')
            break
          case 'backup':
            ElMessage.success('数据备份已启动')
            break
          case 'enable':
            await ElMessageBox.confirm(
              `确定要启用租户 "${row.name}" 吗？`,
              '启用确认',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'success'
              }
            )
            await enableTenant(row.id)
            ElMessage.success('租户启用成功')
            break
          case 'disable':
            await ElMessageBox.confirm(
              `确定要停用租户 "${row.name}" 吗？`,
              '停用确认',
              {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }
            )
            await disableTenant(row.id)
            ElMessage.success('租户停用成功')
            break
        }
        getTenantList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败')
        }
      }
    }

    // 批量续费
    const handleBatchRenew = () => {
      if (selectedTenants.value.length === 0) {
        ElMessage.warning('请选择要续费的租户')
        return
      }
      
      renewForm.years = null
      renewForm.months = null
      renewForm.days = null
      renewDialogVisible.value = true
    }

    // 批量通知
    const handleBatchNotify = () => {
      ElMessage.success('批量通知发送成功')
    }

    // 导出数据
    const handleExport = () => {
      try {
        // 构建查询参数
        const params = {};
        if (searchForm.name) params.name = searchForm.name;
        if (searchForm.code) params.code = searchForm.code;
        if (searchForm.status !== null && searchForm.status !== undefined) params.status = searchForm.status;
        if (searchForm.packageType !== null && searchForm.packageType !== undefined) params.packageType = searchForm.packageType;
        
        // 使用URLSearchParams构建查询字符串
        const queryString = new URLSearchParams(params).toString();
        
        // 直接使用完整的URL路径
        const url = `http://localhost:8080/api/tenants/export?${queryString}`;
        window.open(url, '_blank');
        
        ElMessage.success('导出请求已发送');
      } catch (error) {
        console.error('导出租户数据异常:', error);
        ElMessage.error('导出租户数据异常');
      }
    }

    // logo上传成功
    const handleLogoSuccess = (response, file) => {
      tenantForm.logo = URL.createObjectURL(file.raw)
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

    // 选择变化
    const handleSelectionChange = (selection) => {
      selectedTenants.value = selection
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.page = 1
      getTenantList()
    }

    // 当前页变化
    const handleCurrentChange = (page) => {
      pagination.page = page
      getTenantList()
    }

    // 提交表单
    const handleSubmit = async () => {
      try {
        await tenantFormRef.value.validate()
        submitLoading.value = true
        // 字段适配
        const payload = {
          tenantName: tenantForm.name,
          tenantCode: tenantForm.code,
          contactName: tenantForm.contactPerson,
          contactPhone: tenantForm.contactPhone,
          contactEmail: tenantForm.contactEmail,
          logoUrl: tenantForm.logo,
          status: tenantForm.status,
          expireTime: tenantForm.expireTime,
          maxUsers: tenantForm.maxUsers,
          remark: tenantForm.address,
          packageType: tenantForm.packageType
        }
        if (!tenantForm.id) {
          // 新增
          const res = await createTenant(payload)
          if (res && res.success) {
            ElMessage.success('创建成功')
            dialogVisible.value = false
            getTenantList()
          } else {
            ElMessage.error(res?.message || '创建失败')
          }
        } else {
          // 编辑
          const res = await updateTenant(tenantForm.id, payload)
          if (res && res.success) {
            ElMessage.success('更新成功')
            dialogVisible.value = false
            getTenantList()
          } else {
            ElMessage.error(res?.message || '更新失败')
          }
        }
      } catch (error) {
        console.error('提交表单错误:', error)
        if (error !== false) {
          ElMessage.error(error?.message || '操作失败')
        }
      } finally {
        submitLoading.value = false
      }
    }

    // 删除租户
    const handleDelete = async (row) => {
      try {
        console.log('删除租户ID:', row.id, row)
        await ElMessageBox.confirm(`确定要删除租户 "${row.name}" 吗？`, '删除确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await deleteTenant(row.id)
        console.log('删除请求返回:', res)
        if (res && res.success) {
          ElMessage.success('删除成功')
          getTenantList()
        } else {
          ElMessage.error(res?.message || '删除失败')
        }
      } catch (error) {
        console.error('删除租户错误:', error)
        if (error !== 'cancel') {
          ElMessage.error(error?.message || '删除失败')
        }
      }
    }

    // 关闭对话框
    const handleDialogClose = () => {
      dialogVisible.value = false
      resetTenantForm()
    }

    // 重置表单
    const resetTenantForm = () => {
      Object.assign(tenantForm, {
        id: null,
        name: '',
        code: '',
        contactPerson: '',
        contactPhone: '',
        contactEmail: '',
        address: '',
        logo: '',
        packageType: 1,
        maxUsers: 50,
        storageLimit: 10,
        startTime: '',
        expireTime: '',
        features: ['user_management', 'meeting_management'],
        status: 1,
        remark: ''
      })
      activeTab.value = 'basic'
      tenantFormRef.value?.clearValidate()
    }

    // 格式化日期
    const formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
      if (!date) return ''
      const d = new Date(date)
      if (format === 'YYYY-MM-DD') {
        return d.toLocaleDateString('zh-CN')
      }
      return d.toLocaleString('zh-CN')
    }

    // 获取套餐类型
    const getPackageType = (type) => {
      const types = { 1: 'info', 2: 'warning', 3: 'success' }
      return types[type] || 'info'
    }

    // 获取套餐文本
    const getPackageText = (type) => {
      const texts = { 1: '基础版', 2: '专业版', 3: '企业版' }
      return texts[type] || '未知'
    }

    // 获取状态类型
    const getStatusType = (status) => {
      const types = { 0: 'danger', 1: 'success', 2: 'warning' }
      return types[status] || 'info'
    }

    // 获取状态文本
    const getStatusText = (status) => {
      const texts = { 0: '停用', 1: '正常', 2: '过期' }
      return texts[status] || '未知'
    }

    // 获取到期时间样式
    const getExpireClass = (expireTime) => {
      const now = new Date()
      const expire = new Date(expireTime)
      const diffDays = Math.ceil((expire - now) / (1000 * 60 * 60 * 24))
      
      if (diffDays < 0) return 'expired'
      if (diffDays <= 30) return 'warning'
      return 'normal'
    }

    // 获取功能文本
    const getFeatureText = (feature) => {
      const texts = {
        user_management: '用户管理',
        meeting_management: '会议管理',
        news_management: '资讯管理',
        course_management: '课程管理',
        data_analysis: '数据分析',
        api_access: 'API接口'
      }
      return texts[feature] || feature
    }

    // 获取已删除的租户列表
    const fetchDeletedTenants = async () => {
      const res = await fetchTenantList({ page: 1, size: 1000, deleted: 1 })
      deletedTenantList.value = (res?.data?.records || []).map(item => ({
        id: item.id,
        name: item.tenantName,
        code: item.tenantCode,
        contactPerson: item.contactName,
        contactPhone: item.contactPhone,
        status: item.status,
        createTime: item.createTime,
        ...item
      }))
    }

    // 监听showDeletedDialog的变化
    watch(showDeletedDialog, (val) => {
      if (val) fetchDeletedTenants()
    })

    // 处理已删除租户的选择变化
    const handleDeletedSelectionChange = (rows) => {
      deletedSelected.value = rows
    }

    // 恢复已删除的租户
    const handleRestore = async (row) => {
      await request.post(`/api/tenants/restore/${row.id}`)
      ElMessage.success('恢复成功')
      fetchDeletedTenants()
      getTenantList()
    }

    // 彻底删除已删除的租户
    const handleRealDelete = async (row) => {
      await realDeleteTenant(row.id)
      ElMessage.success('彻底删除成功')
      fetchDeletedTenants()
    }

    // 批量彻底删除已删除的租户
    const handleBatchRealDelete = async () => {
      await Promise.all(deletedSelected.value.map(row => realDeleteTenant(row.id)))
      ElMessage.success('批量彻底删除成功')
      fetchDeletedTenants()
    }

    // 续费提交
    const handleRenewSubmit = async () => {
      if (!renewForm.years && !renewForm.months && !renewForm.days) {
        ElMessage.error('年/月/日不能全部为空')
        return
      }
      
      try {
        // 如果有renewTenantId，说明是单个租户续费
        if (renewTenantId.value) {
          await renewTenant({
            tenantId: renewTenantId.value,
            years: renewForm.years,
            months: renewForm.months,
            days: renewForm.days
          })
          ElMessage.success('续费成功')
        } else {
          // 批量续费
          const tenantIds = selectedTenants.value.map(tenant => tenant.id)
          await batchRenewTenants({
            tenantIds: tenantIds,
            years: renewForm.years,
            months: renewForm.months,
            days: renewForm.days
          })
          ElMessage.success(`已成功续费 ${tenantIds.length} 个租户`)
        }
        
        renewDialogVisible.value = false
        renewTenantId.value = null
        getTenantList()
      } catch (e) {
        ElMessage.error('续费失败')
      }
    }

    // 组件挂载时获取数据
    onMounted(() => {
      getTenantList()
    })

    return {
      loading,
      submitLoading,
      tenantList,
      selectedTenants,
      dialogVisible,
      viewDialogVisible,
      dialogTitle,
      tenantFormRef,
      currentTenant,
      activeTab,
      searchForm,
      pagination,
      stats,
      tenantForm,
      tenantFormRules,
      getPackageType,
      getPackageText,
      getStatusType,
      getStatusText,
      getExpireClass,
      getFeatureText,
      getTenantList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleView,
      handleDropdownCommand,
      handleBatchRenew,
      handleBatchNotify,
      handleExport,
      handleLogoSuccess,
      beforeLogoUpload,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      handleSubmit,
      handleDialogClose,
      formatDate,
      handleDelete,
      showDeletedDialog,
      deletedTenantList,
      deletedSelected,
      fetchDeletedTenants,
      handleDeletedSelectionChange,
      handleRestore,
      handleRealDelete,
      handleBatchRealDelete,
      renewDialogVisible,
      renewForm,
      renewTenantId,
      handleRenewSubmit
    }
  }
}
</script>

<style scoped>
.tenant-management {
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

.search-section {
  margin-bottom: 20px;
}

.search-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.search-form {
  margin: 0;
}

.stats-section {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.active {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.expired {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-icon.revenue {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.action-section {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.table-section {
  margin-bottom: 20px;
}

.table-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.tenant-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.tenant-logo {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.tenant-logo img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-logo {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
}

.tenant-details {
  flex: 1;
}

.tenant-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.tenant-code {
  font-size: 12px;
  color: #909399;
}

.expired {
  color: #f56c6c;
  font-weight: 600;
}

.warning {
  color: #e6a23c;
  font-weight: 600;
}

.normal {
  color: #67c23a;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.logo-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
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
  object-fit: cover;
}

.tenant-detail {
  padding: 20px 0;
}

.feature-list {
  margin-top: 30px;
}

.feature-list h3 {
  margin: 0 0 16px 0;
  color: #303133;
}

.danger-item {
  color: #f56c6c;
}

.success-item {
  color: #67c23a;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .tenant-management {
    padding: 10px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .tenant-info {
    flex-direction: column;
    align-items: flex-start;
  }
}

.batch-renew-info {
  margin-bottom: 20px;
}

.selected-count {
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
}

.selected-count .highlight {
  color: #409EFF;
  font-weight: bold;
  font-size: 16px;
}
</style>

