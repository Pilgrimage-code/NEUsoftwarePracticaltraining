<template>
  <div class="role-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-user-solid"></i>
          角色管理
        </h1>
        <p class="page-description">管理系统角色和权限分配</p>
      </div>
    </div>

    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-card class="search-card">
        <el-form :model="searchForm" :inline="true" class="search-form">
          <el-form-item label="角色名称">
            <el-input
              v-model="searchForm.name"
              placeholder="请输入角色名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="角色代码">
            <el-input
              v-model="searchForm.code"
              placeholder="请输入角色代码"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
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

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <i class="el-icon-plus"></i>
          新增角色
        </el-button>
        <el-button 
          type="danger" 
          @click="handleBatchDelete"
          :disabled="selectedRoles.length === 0"
        >
          <i class="el-icon-delete"></i>
          批量删除
        </el-button>
      </div>
    </div>

    <!-- 角色列表 -->
    <div class="table-section">
      <el-card class="table-card">
        <el-table
          :data="roleList"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          stripe
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="角色名称" width="150" />
          <el-table-column prop="code" label="角色代码" width="150" />
          <el-table-column prop="description" label="描述" min-width="200" />
          <el-table-column label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" />
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleEdit(row)">
                <i class="el-icon-edit"></i>
                编辑
              </el-button>
              <el-button size="small" type="warning" @click="handlePermission(row)">
                <i class="el-icon-key"></i>
                权限
              </el-button>
              <el-button 
                size="small" 
                :type="row.status === 1 ? 'warning' : 'success'"
                @click="handleToggleStatus(row)"
              >
                <i :class="row.status === 1 ? 'el-icon-lock' : 'el-icon-unlock'"></i>
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">
                <i class="el-icon-delete"></i>
                删除
              </el-button>
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

    <!-- 角色表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleFormRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="roleForm.name" placeholder="请输入角色名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色代码" prop="code">
              <el-input v-model="roleForm.code" placeholder="请输入角色代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="roleForm.sort" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="roleForm.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="roleForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
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

    <!-- 权限分配对话框 -->
    <el-dialog
      title="权限分配"
      v-model="permissionDialogVisible"
      width="800px"
    >
      <div class="permission-content">
        <div class="permission-header">
          <span>为角色 "{{ currentRole?.name }}" 分配权限</span>
        </div>
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          :props="permissionTreeProps"
          node-key="id"
          show-checkbox
          :default-expand-all="true"
          :check-strictly="false"
          @check="handlePermissionCheck"
        >
          <template #default="{ node, data }">
            <div class="permission-node">
              <i :class="data.icon || 'el-icon-folder'"></i>
              <span>{{ data.name }}</span>
              <el-tag v-if="data.type" size="small" :type="getPermissionType(data.type)">
                {{ getPermissionText(data.type) }}
              </el-tag>
            </div>
          </template>
        </el-tree>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermission" :loading="permissionLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'RoleManagement',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const permissionLoading = ref(false)
    const roleList = ref([])
    const selectedRoles = ref([])
    const dialogVisible = ref(false)
    const permissionDialogVisible = ref(false)
    const dialogTitle = ref('')
    const roleFormRef = ref(null)
    const permissionTreeRef = ref(null)
    const currentRole = ref(null)

    // 搜索表单
    const searchForm = reactive({
      name: '',
      code: '',
      status: null
    })

    // 分页信息
    const pagination = reactive({
      page: 1,
      size: 10,
      total: 0
    })

    // 角色表单
    const roleForm = reactive({
      id: null,
      name: '',
      code: '',
      description: '',
      sort: 0,
      status: 1,
      remark: ''
    })

    // 表单验证规则
    const roleFormRules = {
      name: [
        { required: true, message: '请输入角色名称', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入角色代码', trigger: 'blur' },
        { pattern: /^[A-Za-z0-9_-]+$/, message: '角色代码只能包含字母、数字、下划线和横线', trigger: 'blur' }
      ]
    }

    // 权限树配置
    const permissionTreeProps = {
      children: 'children',
      label: 'name'
    }

    // 权限树数据
    const permissionTree = ref([
      {
        id: 1,
        name: '系统管理',
        icon: 'el-icon-setting',
        type: 'menu',
        children: [
          {
            id: 11,
            name: '用户管理',
            icon: 'el-icon-user',
            type: 'menu',
            children: [
              { id: 111, name: '查看用户', type: 'button' },
              { id: 112, name: '新增用户', type: 'button' },
              { id: 113, name: '编辑用户', type: 'button' },
              { id: 114, name: '删除用户', type: 'button' }
            ]
          },
          {
            id: 12,
            name: '角色管理',
            icon: 'el-icon-user-solid',
            type: 'menu',
            children: [
              { id: 121, name: '查看角色', type: 'button' },
              { id: 122, name: '新增角色', type: 'button' },
              { id: 123, name: '编辑角色', type: 'button' },
              { id: 124, name: '删除角色', type: 'button' },
              { id: 125, name: '分配权限', type: 'button' }
            ]
          },
          {
            id: 13,
            name: '部门管理',
            icon: 'el-icon-office-building',
            type: 'menu',
            children: [
              { id: 131, name: '查看部门', type: 'button' },
              { id: 132, name: '新增部门', type: 'button' },
              { id: 133, name: '编辑部门', type: 'button' },
              { id: 134, name: '删除部门', type: 'button' }
            ]
          }
        ]
      },
      {
        id: 2,
        name: '会议管理',
        icon: 'el-icon-date',
        type: 'menu',
        children: [
          { id: 21, name: '查看会议', type: 'button' },
          { id: 22, name: '创建会议', type: 'button' },
          { id: 23, name: '编辑会议', type: 'button' },
          { id: 24, name: '删除会议', type: 'button' },
          { id: 25, name: '会议审核', type: 'button' }
        ]
      },
      {
        id: 3,
        name: '资讯管理',
        icon: 'el-icon-document',
        type: 'menu',
        children: [
          { id: 31, name: '查看资讯', type: 'button' },
          { id: 32, name: '发布资讯', type: 'button' },
          { id: 33, name: '编辑资讯', type: 'button' },
          { id: 34, name: '删除资讯', type: 'button' },
          { id: 35, name: '资讯审核', type: 'button' }
        ]
      }
    ])

    // 模拟角色数据
    const mockRoles = [
      {
        id: 1,
        name: '超级管理员',
        code: 'super_admin',
        description: '拥有系统所有权限',
        sort: 1,
        status: 1,
        createTime: '2024-06-15 10:00:00',
        remark: '系统超级管理员'
      },
      {
        id: 2,
        name: '管理员',
        code: 'admin',
        description: '拥有大部分管理权限',
        sort: 2,
        status: 1,
        createTime: '2024-06-15 10:30:00',
        remark: '普通管理员'
      },
      {
        id: 3,
        name: '普通用户',
        code: 'user',
        description: '基础用户权限',
        sort: 3,
        status: 1,
        createTime: '2024-06-15 11:00:00',
        remark: '普通用户'
      }
    ]

    // 获取权限类型样式
    const getPermissionType = (type) => {
      const types = { menu: 'primary', button: 'success', api: 'warning' }
      return types[type] || 'info'
    }

    // 获取权限类型文本
    const getPermissionText = (type) => {
      const texts = { menu: '菜单', button: '按钮', api: '接口' }
      return texts[type] || '未知'
    }

    // 获取角色列表
    const getRoleList = async () => {
      loading.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 500))
        roleList.value = mockRoles
        pagination.total = mockRoles.length
      } catch (error) {
        ElMessage.error('获取角色列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.page = 1
      getRoleList()
    }

    // 重置搜索
    const handleReset = () => {
      Object.assign(searchForm, {
        name: '',
        code: '',
        status: null
      })
      pagination.page = 1
      getRoleList()
    }

    // 新增角色
    const handleAdd = () => {
      dialogTitle.value = '新增角色'
      resetRoleForm()
      dialogVisible.value = true
    }

    // 编辑角色
    const handleEdit = (row) => {
      dialogTitle.value = '编辑角色'
      Object.assign(roleForm, row)
      dialogVisible.value = true
    }

    // 删除角色
    const handleDelete = async (row) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除角色 "${row.name}" 吗？`,
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        ElMessage.success('删除成功')
        getRoleList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    // 批量删除
    const handleBatchDelete = async () => {
      try {
        await ElMessageBox.confirm(
          `确定要删除选中的 ${selectedRoles.value.length} 个角色吗？`,
          '批量删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        ElMessage.success('批量删除成功')
        getRoleList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('批量删除失败')
        }
      }
    }

    // 切换角色状态
    const handleToggleStatus = async (row) => {
      const newStatus = row.status === 1 ? 0 : 1
      const action = newStatus === 1 ? '启用' : '禁用'
      
      try {
        await ElMessageBox.confirm(
          `确定要${action}角色 "${row.name}" 吗？`,
          `${action}确认`,
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        ElMessage.success(`${action}成功`)
        getRoleList()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error(`${action}失败`)
        }
      }
    }

    // 权限分配
    const handlePermission = (row) => {
      currentRole.value = row
      permissionDialogVisible.value = true
      // 模拟加载角色权限
      setTimeout(() => {
        const checkedKeys = [111, 112, 121, 122, 21, 22, 31, 32]
        permissionTreeRef.value?.setCheckedKeys(checkedKeys)
      }, 100)
    }

    // 权限选择变化
    const handlePermissionCheck = (data, checked) => {
      // 处理权限选择逻辑
    }

    // 保存权限
    const handleSavePermission = async () => {
      try {
        permissionLoading.value = true
        const checkedKeys = permissionTreeRef.value?.getCheckedKeys()
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success('权限保存成功')
        permissionDialogVisible.value = false
      } catch (error) {
        ElMessage.error('权限保存失败')
      } finally {
        permissionLoading.value = false
      }
    }

    // 选择变化
    const handleSelectionChange = (selection) => {
      selectedRoles.value = selection
    }

    // 分页大小变化
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.page = 1
      getRoleList()
    }

    // 当前页变化
    const handleCurrentChange = (page) => {
      pagination.page = page
      getRoleList()
    }

    // 提交表单
    const handleSubmit = async () => {
      try {
        await roleFormRef.value.validate()
        submitLoading.value = true
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success(roleForm.id ? '更新成功' : '创建成功')
        dialogVisible.value = false
        getRoleList()
      } catch (error) {
        if (error !== false) {
          ElMessage.error('操作失败')
        }
      } finally {
        submitLoading.value = false
      }
    }

    // 关闭对话框
    const handleDialogClose = () => {
      dialogVisible.value = false
      resetRoleForm()
    }

    // 重置表单
    const resetRoleForm = () => {
      Object.assign(roleForm, {
        id: null,
        name: '',
        code: '',
        description: '',
        sort: 0,
        status: 1,
        remark: ''
      })
      roleFormRef.value?.clearValidate()
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    // 组件挂载时获取数据
    onMounted(() => {
      getRoleList()
    })

    return {
      loading,
      submitLoading,
      permissionLoading,
      roleList,
      selectedRoles,
      dialogVisible,
      permissionDialogVisible,
      dialogTitle,
      roleFormRef,
      permissionTreeRef,
      currentRole,
      searchForm,
      pagination,
      roleForm,
      roleFormRules,
      permissionTree,
      permissionTreeProps,
      getPermissionType,
      getPermissionText,
      getRoleList,
      handleSearch,
      handleReset,
      handleAdd,
      handleEdit,
      handleDelete,
      handleBatchDelete,
      handleToggleStatus,
      handlePermission,
      handlePermissionCheck,
      handleSavePermission,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      handleSubmit,
      handleDialogClose,
      formatDate
    }
  }
}
</script>

<style scoped>
.role-management {
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

.permission-content {
  max-height: 500px;
  overflow-y: auto;
}

.permission-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
  font-weight: 600;
  color: #303133;
}

.permission-node {
  display: flex;
  align-items: center;
  gap: 8px;
}

.permission-node i {
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .role-management {
    padding: 10px;
  }
  
  .search-form {
    flex-direction: column;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>

