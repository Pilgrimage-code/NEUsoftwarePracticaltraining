<template>
  <div class="dept-management">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <i class="el-icon-office-building"></i>
          部门管理
        </h1>
        <p class="page-description">管理组织架构和部门信息</p>
      </div>
    </div>

    <div class="content-wrapper">
      <!-- 左侧部门树 -->
      <div class="dept-tree-section">
        <el-card class="tree-card">
          <template #header>
            <div class="tree-header">
              <span>组织架构</span>
              <el-button type="primary" size="small" @click="handleAddRootDept">
                <i class="el-icon-plus"></i>
                添加根部门
              </el-button>
            </div>
          </template>
          <el-tree
            :data="deptTree"
            :props="treeProps"
            node-key="id"
            :expand-on-click-node="false"
            :default-expand-all="true"
            @node-click="handleNodeClick"
            @node-contextmenu="handleNodeRightClick"
          >
            <template #default="{ node, data }">
              <div class="tree-node">
                <span class="node-label">{{ data.name }}</span>
                <span class="node-actions">
                  <el-button type="text" size="small" @click.stop="handleAddChild(data)">
                    <i class="el-icon-plus"></i>
                  </el-button>
                  <el-button type="text" size="small" @click.stop="handleEdit(data)">
                    <i class="el-icon-edit"></i>
                  </el-button>
                  <el-button type="text" size="small" @click.stop="handleDelete(data)">
                    <i class="el-icon-delete"></i>
                  </el-button>
                </span>
              </div>
            </template>
          </el-tree>
        </el-card>
      </div>

      <!-- 右侧部门详情 -->
      <div class="dept-detail-section">
        <el-card class="detail-card">
          <template #header>
            <div class="detail-header">
              <span>{{ selectedDept ? selectedDept.name : '请选择部门' }}</span>
              <el-button v-if="selectedDept" type="primary" @click="handleEdit(selectedDept)">
                <i class="el-icon-edit"></i>
                编辑部门
              </el-button>
            </div>
          </template>
          
          <div v-if="selectedDept" class="dept-info">
            <!-- 基本信息 -->
            <div class="info-section">
              <h3>基本信息</h3>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="部门名称">{{ selectedDept.name }}</el-descriptions-item>
                <el-descriptions-item label="部门代码">{{ selectedDept.code || '-' }}</el-descriptions-item>
                <el-descriptions-item label="负责人">{{ selectedDept.leader || '-' }}</el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ selectedDept.phone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="邮箱">{{ selectedDept.email || '-' }}</el-descriptions-item>
                <el-descriptions-item label="状态">
                  <el-tag :type="selectedDept.status === 1 ? 'success' : 'danger'">
                    {{ selectedDept.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="创建时间" :span="2">
                  {{ formatDate(selectedDept.createTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">
                  {{ selectedDept.remark || '-' }}
                </el-descriptions-item>
              </el-descriptions>
            </div>

            <!-- 部门成员 -->
            <div class="info-section">
              <h3>部门成员</h3>
              <el-table :data="deptUsers" stripe style="width: 100%">
                <el-table-column prop="realName" label="姓名" width="120" />
                <el-table-column prop="username" label="用户名" width="120" />
                <el-table-column prop="position" label="职位" width="120" />
                <el-table-column prop="phone" label="手机号" width="130" />
                <el-table-column prop="email" label="邮箱" />
                <el-table-column label="状态" width="80">
                  <template #default="{ row }">
                    <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                      {{ row.status === 1 ? '在职' : '离职' }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <div v-else class="empty-state">
            <el-empty description="请从左侧选择部门查看详情" />
          </div>
        </el-card>
      </div>
    </div>

    <!-- 部门表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="deptFormRef"
        :model="deptForm"
        :rules="deptFormRules"
        label-width="100px"
      >
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="deptForm.parentId"
            :data="deptTreeOptions"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择上级部门"
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="deptForm.name" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门代码" prop="code">
              <el-input v-model="deptForm.code" placeholder="请输入部门代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="deptForm.leader" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="deptForm.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="deptForm.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="deptForm.sort" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="deptForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="deptForm.remark"
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
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'DeptManagement',
  setup() {
    // 响应式数据
    const loading = ref(false)
    const submitLoading = ref(false)
    const deptTree = ref([])
    const deptTreeOptions = ref([])
    const selectedDept = ref(null)
    const deptUsers = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const deptFormRef = ref(null)

    // 树形组件配置
    const treeProps = {
      children: 'children',
      label: 'name'
    }

    // 部门表单
    const deptForm = reactive({
      id: null,
      parentId: null,
      name: '',
      code: '',
      leader: '',
      phone: '',
      email: '',
      sort: 0,
      status: 1,
      remark: ''
    })

    // 表单验证规则
    const deptFormRules = {
      name: [
        { required: true, message: '请输入部门名称', trigger: 'blur' }
      ],
      code: [
        { pattern: /^[A-Za-z0-9_-]+$/, message: '部门代码只能包含字母、数字、下划线和横线', trigger: 'blur' }
      ],
      phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }

    // 模拟数据
    const mockDeptTree = [
      {
        id: 1,
        name: '演示企业',
        code: 'demo',
        leader: '张三',
        phone: '13800138000',
        email: 'demo@cemh.com',
        status: 1,
        sort: 1,
        createTime: '2024-06-15 10:00:00',
        remark: '公司总部',
        children: [
          {
            id: 2,
            name: '技术部',
            code: 'tech',
            leader: '王五',
            phone: '13800138002',
            email: 'tech@demo.com',
            status: 1,
            sort: 1,
            createTime: '2024-06-15 10:30:00',
            remark: '负责技术研发',
            children: [
              {
                id: 5,
                name: '前端组',
                code: 'frontend',
                leader: '赵八',
                phone: '13800138008',
                email: 'frontend@demo.com',
                status: 1,
                sort: 1,
                createTime: '2024-06-15 11:00:00',
                remark: '前端开发团队'
              },
              {
                id: 6,
                name: '后端组',
                code: 'backend',
                leader: '钱九',
                phone: '13800138009',
                email: 'backend@demo.com',
                status: 1,
                sort: 2,
                createTime: '2024-06-15 11:30:00',
                remark: '后端开发团队'
              }
            ]
          },
          {
            id: 3,
            name: '市场部',
            code: 'market',
            leader: '赵六',
            phone: '13800138003',
            email: 'market@demo.com',
            status: 1,
            sort: 2,
            createTime: '2024-06-15 10:45:00',
            remark: '负责市场推广'
          },
          {
            id: 4,
            name: '人事部',
            code: 'hr',
            leader: '钱七',
            phone: '13800138004',
            email: 'hr@demo.com',
            status: 1,
            sort: 3,
            createTime: '2024-06-15 11:00:00',
            remark: '负责人力资源'
          }
        ]
      }
    ]

    const mockUsers = [
      { id: 1, realName: '张三', username: 'admin', position: '总经理', phone: '13800138000', email: 'admin@demo.com', status: 1 },
      { id: 2, realName: '王五', username: 'tech', position: '技术经理', phone: '13800138002', email: 'tech@demo.com', status: 1 },
      { id: 3, realName: '赵六', username: 'market', position: '市场经理', phone: '13800138003', email: 'market@demo.com', status: 1 }
    ]

    // 获取部门树
    const getDeptTree = async () => {
      loading.value = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 500))
        deptTree.value = mockDeptTree
        deptTreeOptions.value = buildTreeOptions(mockDeptTree)
      } catch (error) {
        ElMessage.error('获取部门树失败')
      } finally {
        loading.value = false
      }
    }

    // 构建树形选择器选项
    const buildTreeOptions = (tree) => {
      const options = []
      tree.forEach(node => {
        const option = {
          id: node.id,
          name: node.name,
          children: node.children ? buildTreeOptions(node.children) : []
        }
        options.push(option)
      })
      return options
    }

    // 获取部门用户
    const getDeptUsers = async (deptId) => {
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 300))
        deptUsers.value = mockUsers.filter(user => Math.random() > 0.5)
      } catch (error) {
        ElMessage.error('获取部门用户失败')
      }
    }

    // 节点点击
    const handleNodeClick = (data) => {
      selectedDept.value = data
      getDeptUsers(data.id)
    }

    // 节点右键
    const handleNodeRightClick = (event, data) => {
      event.preventDefault()
      // 可以在这里显示右键菜单
    }

    // 添加根部门
    const handleAddRootDept = () => {
      dialogTitle.value = '添加根部门'
      resetDeptForm()
      deptForm.parentId = null
      dialogVisible.value = true
    }

    // 添加子部门
    const handleAddChild = (data) => {
      dialogTitle.value = '添加子部门'
      resetDeptForm()
      deptForm.parentId = data.id
      dialogVisible.value = true
    }

    // 编辑部门
    const handleEdit = (data) => {
      dialogTitle.value = '编辑部门'
      Object.assign(deptForm, data)
      dialogVisible.value = true
    }

    // 删除部门
    const handleDelete = async (data) => {
      try {
        await ElMessageBox.confirm(
          `确定要删除部门 "${data.name}" 吗？删除后该部门下的所有子部门也将被删除。`,
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        
        ElMessage.success('删除成功')
        getDeptTree()
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('删除失败')
        }
      }
    }

    // 提交表单
    const handleSubmit = async () => {
      try {
        await deptFormRef.value.validate()
        submitLoading.value = true
        
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success(deptForm.id ? '更新成功' : '创建成功')
        dialogVisible.value = false
        getDeptTree()
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
      resetDeptForm()
    }

    // 重置表单
    const resetDeptForm = () => {
      Object.assign(deptForm, {
        id: null,
        parentId: null,
        name: '',
        code: '',
        leader: '',
        phone: '',
        email: '',
        sort: 0,
        status: 1,
        remark: ''
      })
      deptFormRef.value?.clearValidate()
    }

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    // 组件挂载时获取数据
    onMounted(() => {
      getDeptTree()
    })

    return {
      loading,
      submitLoading,
      deptTree,
      deptTreeOptions,
      selectedDept,
      deptUsers,
      dialogVisible,
      dialogTitle,
      deptFormRef,
      treeProps,
      deptForm,
      deptFormRules,
      getDeptTree,
      handleNodeClick,
      handleNodeRightClick,
      handleAddRootDept,
      handleAddChild,
      handleEdit,
      handleDelete,
      handleSubmit,
      handleDialogClose,
      formatDate
    }
  }
}
</script>

<style scoped>
.dept-management {
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

.content-wrapper {
  display: flex;
  gap: 20px;
  height: calc(100vh - 200px);
}

.dept-tree-section {
  width: 350px;
  flex-shrink: 0;
}

.dept-detail-section {
  flex: 1;
}

.tree-card, .detail-card {
  height: 100%;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.tree-header, .detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding-right: 8px;
}

.node-label {
  flex: 1;
}

.node-actions {
  display: none;
}

.tree-node:hover .node-actions {
  display: flex;
  gap: 4px;
}

.dept-info {
  padding: 20px 0;
}

.info-section {
  margin-bottom: 30px;
}

.info-section h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
    height: auto;
  }
  
  .dept-tree-section {
    width: 100%;
  }
  
  .tree-card, .detail-card {
    height: auto;
    min-height: 400px;
  }
}
</style>

