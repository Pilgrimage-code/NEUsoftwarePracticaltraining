<template>
  <div class="department-management">
    <!-- 顶部标题区域 - 修改为紫色渐变 -->
    <div class="tenant-management">
      <div class="page-header">
        <div class="header-content">
          <h1 class="page-title">
            <i class="el-icon-office-building"></i>
            部门管理
          </h1>
          <p class="page-description">管理系统租户和多租户配置</p>
        </div>
      </div>
    </div>

    <!-- 搜索区域 - 按钮位置调整到左下角 -->
    <div class="search-container">
      <div class="search-title">
        <i class="el-icon-search"></i>
        <span>部门搜索</span>
      </div>
      <el-form :model="filters" class="search-form">
        <el-form-item label="公司名称">
          <el-input 
            v-model="filters.company" 
            placeholder="请输入公司名称"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item label="部门名称">
          <el-input 
            v-model="filters.department" 
            placeholder="请输入部门名称"
            clearable
          ></el-input>
        </el-form-item>
        
        <el-form-item label="部门状态">
          <el-select 
            v-model="filters.deptStatus" 
            placeholder="请选择状态" 
            clearable
          >
            <el-option label="正常" value="正常"></el-option>
            <el-option label="停用" value="停用"></el-option>
          </el-select>
        </el-form-item>
        
        <div class="form-actions">
          <el-button 
            type="primary" 
            icon="el-icon-search" 
            @click="handleSearch"
            class="search-btn"
            size="medium"
          >查找</el-button>
          <el-button 
            type="default" 
            @click="resetFilters"
            class="reset-btn"
            size="medium"
          >重置</el-button>
        </div>
      </el-form>
    </div>

    <!-- 主内容区 -->
    <div class="main-container">
      <!-- 左侧树形结构 - 移除部门状态显示 -->
      <div class="tree-container">
        <div class="tree-header">
          <i class="el-icon-s-fold"></i>
          <span>组织架构</span>
        </div>
        <div class="tree-content">
          <el-tree
            v-loading="treeLoading"
            :data="treeData"
            :props="treeProps"
            node-key="id"
            highlight-current
            :expand-on-click-node="false"
            @node-click="handleTreeNodeClick"
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                <i :class="data.type === 'company' ? 'el-icon-office-building company-node' : 'el-icon-s-management department-node'"></i>
                <span>{{ node.label }}</span>
              </div>
            </template>
          </el-tree>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="content-container">
        <div class="content-header">
          <h2 v-if="current.type === 'company'">
            <i class="el-icon-office-building"></i>
            {{ current.data.tenantName }} — 部门管理
          </h2>
          <h2 v-else-if="current.type === 'department'">
            <i class="el-icon-s-management"></i>
            {{ current.data.deptName }} 部门详情
          </h2>
          <h2 v-else-if="isSearchResult">
            <i class="el-icon-search"></i>
            搜索结果
          </h2>
          <h2 v-else>
            <i class="el-icon-info"></i>
            部门信息
          </h2>
          
          <div v-if="current.type === 'company'">
            <el-button 
              type="primary" 
              icon="el-icon-plus" 
              @click="openDeptDialog()"
              class="add-dept-btn"
              size="medium"
            >添加部门</el-button>
          </div>
          
          <div v-else-if="current.type === 'department'" class="action-buttons">
            <el-button 
              type="primary" 
              icon="el-icon-edit" 
              @click="openDeptDialog(current.data)"
              class="edit-btn"
              size="medium"
            >修改部门</el-button>
            <el-button 
              type="danger" 
              icon="el-icon-delete" 
              @click="deleteDept(current.data)"
              class="delete-btn"
              size="medium"
            >删除部门</el-button>
          </div>
          
          <el-button 
            v-else-if="isSearchResult" 
            type="default" 
            @click="exitSearch"
            size="medium"
          >返回</el-button>
        </div>
        
        <div class="content-body">
          <!-- 空状态 - 首次进入时默认显示 -->
          <div v-if="!current.type && !isSearchResult" class="empty-state">
            <i class="el-icon-office-building empty-icon"></i>
            <h3 class="empty-title">请选择公司或部门</h3>
            <p class="empty-desc">从左侧选择公司查看其部门列表，或选择部门查看详细信息<br>您也可以使用搜索功能查找特定部门</p>
          </div>
          
          <!-- 公司视图：部门列表 - 优化布局和操作按钮 -->
          <div v-else-if="current.type === 'company'">
            <div class="dept-list-header">
              <div>
                <h3>部门列表</h3>
                <p class="page-description">共 {{ current.data.departments ? current.data.departments.length : 0 }} 个部门</p>
              </div>
            </div>
            
            <el-table 
              v-loading="tableLoading"
              :data="current.data.departments" 
              class="dept-table"
              v-if="current.data.departments"
            >
              <el-table-column prop="deptName" label="部门名称" width="220">
                <template #default="scope">
                  <div style="display: flex; align-items: center;">
                    <i class="el-icon-s-management" style="color: #0d9e0d; margin-right: 8px;"></i>
                    <span>{{ scope.row.deptName }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="deptCode" label="部门编码" width="180"></el-table-column>
              <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                    {{ scope.row.status === 1 ? '正常' : '停用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
              <el-table-column label="操作" width="180" align="right">
                <template #default="scope">
                  <el-link 
                    type="primary" 
                    @click="openDeptDialog(scope.row)" 
                    :underline="false"
                    style="margin-right: 15px;"
                  >修改</el-link>
                  <el-link 
                    type="danger" 
                    @click="deleteDept(scope.row)" 
                    :underline="false"
                  >删除</el-link>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- 部门视图：详细信息 - 美化展示 -->
          <div v-else-if="current.type === 'department'">
            <div class="dept-detail-card">
              <div class="dept-header">
                <div class="dept-info">
                  <div class="dept-name">
                    <i class="el-icon-s-management"></i>
                    <h3>{{ current.data.deptName }}</h3>
                  </div>
                  <el-tag :type="current.data.status === 1 ? 'success' : 'danger'" class="status-tag">
                    {{ current.data.status === 1 ? '正常' : '停用' }}
                  </el-tag>
                </div>
                <p class="dept-path">
                  <i class="el-icon-office-building"></i>
                  {{ getCompanyName(current.data) }}
                </p>
              </div>
              
              <el-divider></el-divider>
              
              <div class="dept-details">
                <div class="detail-section">
                  <h4 class="section-title">基本信息</h4>
                  <div class="detail-grid">
                    <div class="detail-item">
                      <span class="detail-label">部门编码：</span>
                      <span class="detail-value">{{ current.data.deptCode }}</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">联系电话：</span>
                      <span class="detail-value">
                        <el-link type="primary" :href="`tel:${current.data.phone}`" v-if="current.data.phone">
                          {{ current.data.phone }}
                        </el-link>
                        <span v-else>未设置</span>
                      </span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">邮箱：</span>
                      <span class="detail-value">
                        <el-link type="primary" :href="`mailto:${current.data.email}`" v-if="current.data.email">
                          {{ current.data.email }}
                        </el-link>
                        <span v-else>未设置</span>
                      </span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">地址：</span>
                      <span class="detail-value">{{ current.data.address || '未设置' }}</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">创建时间：</span>
                      <span class="detail-value">{{ current.data.createTime }}</span>
                    </div>
                  </div>
                </div>
                
                <div class="detail-section">
                  <h4 class="section-title">部门描述</h4>
                  <p class="dept-description">
                    {{ current.data.remark || '暂无部门描述信息' }}
                  </p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 搜索结果视图 -->
          <div v-else-if="isSearchResult">
            <div class="dept-list-header">
              <div>
                <h3>搜索结果</h3>
                <p class="page-description">找到 {{ searchResultList.length }} 个匹配的部门</p>
              </div>
            </div>
            
            <el-table :data="searchResultList">
              <el-table-column prop="companyName" label="公司名称" width="220">
                <template #default="scope">
                  <div style="display: flex; align-items: center;">
                    <i class="el-icon-office-building" style="color: #1a73e8; margin-right: 8px;"></i>
                    <span>{{ scope.row.companyName }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="deptName" label="部门名称" width="220">
                <template #default="scope">
                  <div style="display: flex; align-items: center;">
                    <i class="el-icon-s-management" style="color: #0d9e0d; margin-right: 8px;"></i>
                    <span>{{ scope.row.deptName }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="deptCode" label="部门编码" width="180"></el-table-column>
              <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                  <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                    {{ scope.row.status === 1 ? '正常' : '停用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
              <el-table-column label="操作" width="180" align="right">
                <template #default="scope">
                  <el-link 
                    type="primary" 
                    @click="viewDeptDetail(scope.row)" 
                    :underline="false"
                    style="margin-right: 15px;"
                  >详情</el-link>
                  <el-link 
                    type="danger" 
                    @click="deleteDept(scope.row)" 
                    :underline="false"
                  >删除</el-link>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 部门弹窗（新增/修改） -->
    <el-dialog 
      :title="deptDialog.title" 
      v-model="deptDialog.visible"
      width="600px"
      :close-on-click-modal="false"
      custom-class="dept-dialog"
    >
      <el-form :model="deptDialog.form" label-width="100px" :rules="deptRules" ref="deptForm">
        <!-- 添加隐藏的parentId字段 -->
        <el-input type="hidden" v-model="deptDialog.form.parentId"></el-input>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门编码" prop="deptCode">
              <el-input 
                v-model="deptDialog.form.deptCode" 
                placeholder="请输入部门编码"
                size="medium"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input 
                v-model="deptDialog.form.deptName" 
                placeholder="请输入部门名称"
                size="medium"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电话">
              <el-input 
                v-model="deptDialog.form.phone" 
                placeholder="请输入联系电话"
                size="medium"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input 
                v-model="deptDialog.form.email" 
                placeholder="请输入邮箱"
                size="medium"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="地址">
              <el-input 
                v-model="deptDialog.form.address" 
                placeholder="请输入部门地址"
                size="medium"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="deptDialog.form.status">
                <el-radio :label="1">正常</el-radio>
                <el-radio :label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="描述">
          <el-input
            type="textarea"
            :rows="3"
            v-model="deptDialog.form.remark"
            placeholder="请输入部门描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="deptDialog.visible = false" size="medium" class="dialog-btn">取消</el-button>
        <el-button type="primary" @click="saveDept" size="medium" class="dialog-btn">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus';
import { 
  getTenants, 
  getDeptsByTenant,
  createDept,
  updateDept,
  deleteDept
} from '@/api/dept';

export default {
  name: 'DepartmentManagement',
  data() {
    return {
      // 表单验证规则
      deptRules: {
        deptCode: [{ required: true, message: '请输入部门编码', trigger: 'blur' }],
        deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      },
      
      // 搜索条件
      filters: { 
        company: "", 
        department: "", 
        deptStatus: "" 
      },
      
      // 当前选中的节点
      current: { 
        type: "", 
        data: null 
      },
      
      // 部门对话框
      deptDialog: {
        visible: false, 
        title: "", 
        form: {
          deptCode: '',
          deptName: '',
          phone: '',
          email: '',
          address: '',
          status: 1, // 默认正常状态 (1)
          remark: '',
          parentId: null,
          tenantId: null
        }, 
        isEdit: false
      },
      
      // 搜索状态
      isSearchResult: false,
      searchResultList: [],
      
      // 企业列表
      companies: [],
      
      // 加载状态
      treeLoading: false,
      tableLoading: false
    };
  },
  computed: {
    // 树形数据 - 基于后端数据构建
    treeData() {
      return this.companies.map(c => ({
        id: c.id, 
        label: c.tenantName, 
        children: c.departments?.map(d => ({
          id: d.id, 
          label: d.deptName, 
          type: "department", 
          data: d
        })) || [], 
        type: "company", 
        data: c
      }));
    },
    
    // 树形结构配置
    treeProps() { 
      return { 
        children: "children", 
        label: "label" 
      };
    }
  },
  created() {
    this.loadTenants();
  },
  methods: {
    // 添加方法：获取当前公司的根部门ID
    getRootDeptId(tenantId) {
      const company = this.companies.find(c => c.id === tenantId);
      if (company && company.departments) {
        const rootDept = company.departments.find(d => d.parentId === null || d.parentId === 0);
        return rootDept ? rootDept.id : null;
      }
      return null;
    },
    
    // 加载企业数据
    async loadTenants() {
      this.treeLoading = true;
      try {
        const res = await getTenants();
        if (res.code === 200) {
          this.companies = res.data;
          
          // 如果企业数据中有部门信息，直接使用
          // 否则加载第一个企业的部门
          if (this.companies.length > 0 && !this.companies[0].departments) {
            await this.loadDeptsForTenant(this.companies[0].id);
          }
        }
      } catch (error) {
        console.error("加载企业数据失败:", error);
        ElMessage.error("加载企业数据失败");
      } finally {
        this.treeLoading = false;
      }
    },
    
    // 加载指定企业的部门
    async loadDeptsForTenant(tenantId) {
      this.tableLoading = true;
      try {
        const res = await getDeptsByTenant(tenantId);
        if (res.code === 200) {
          const tenant = this.companies.find(c => c.id === tenantId);
          if (tenant) {
            tenant.departments = res.data;
          }
        }
      } catch (error) {
        console.error("加载部门数据失败:", error);
        ElMessage.error("加载部门数据失败");
      } finally {
        this.tableLoading = false;
      }
    },
    
    // 搜索部门
    async handleSearch() {
      const { company, department, deptStatus } = this.filters;
      
      // 检查搜索条件是否全部为空
      if (!company && !department && !deptStatus) {
        ElMessage.warning('请输入要查找的公司或部门信息');
        return;
      }
      
      this.searchResultList = [];
      
      try {
        // 重新加载所有企业及其部门（如果尚未加载）
        if (this.companies.length === 0) {
          await this.loadTenants();
        }
        
        // 遍历企业，筛选部门
        this.companies.forEach(comp => {
          // 检查公司名称匹配
          if (company && !comp.tenantName.toLowerCase().includes(company.toLowerCase())) return;
          
          // 如果该企业的部门尚未加载，则先加载
          if (!comp.departments) return;
          
          comp.departments.forEach(dept => {
            // 检查部门名称匹配
            if (department && !dept.deptName.toLowerCase().includes(department.toLowerCase())) return;
            
            // 检查部门状态匹配
            if (deptStatus) {
              const statusText = dept.status === 1 ? '正常' : '停用';
              if (statusText !== deptStatus) return;
            }
            
            // 添加匹配的部门到搜索结果
            this.searchResultList.push({
              ...dept,
              companyName: comp.tenantName,
              companyId: comp.id
            });
          });
        });
        
        if (this.searchResultList.length > 0) {
          this.isSearchResult = true;
          this.current.type = "";
          this.current.data = null;
          ElMessage.success(`找到 ${this.searchResultList.length} 个部门`);
        } else {
          ElMessage.warning("未找到匹配的部门");
        }
      } catch (error) {
        console.error("搜索失败:", error);
        ElMessage.error("搜索失败");
      }
    },
    
    // 重置搜索条件
    resetFilters() {
      this.filters.company = "";
      this.filters.department = "";
      this.filters.deptStatus = "";
      this.isSearchResult = false;
      // 重置后显示之前选中的节点，如果没有则清空
      if (!this.current.type && this.companies.length > 0) {
        this.current.type = "company";
        this.current.data = this.companies[0];
      }
    },
    
    // 退出搜索结果视图
    exitSearch() {
      this.isSearchResult = false;
    },
    
    // 查看部门详情
    viewDeptDetail(dept) {
      const company = this.companies.find(c => c.id === dept.companyId);
      if (company) {
        const department = company.departments?.find(d => d.id === dept.id);
        if (department) {
          this.current.type = "department";
          this.current.data = department;
          this.isSearchResult = false;
        }
      }
    },
    
    // 处理树节点点击
    async handleTreeNodeClick(node) {
      if (node.type === "company") {
        this.current.type = "company";
        this.current.data = node.data;
        
        // 如果部门数据未加载，则加载
        if (!node.data.departments) {
          await this.loadDeptsForTenant(node.data.id);
        }
      } else if (node.type === "department") {
        this.current.type = "department";
        this.current.data = node.data;
      }
      this.isSearchResult = false;
    },
    
    // 打开部门对话框 - 关键修复
    openDeptDialog(dept = null) {
      this.deptDialog.isEdit = !!dept;
      this.deptDialog.title = dept ? "修改部门信息" : "添加新部门";
      this.deptDialog.visible = true;
      
      if (dept) {
        this.deptDialog.form = { 
          id: dept.id,
          deptCode: dept.deptCode,
          deptName: dept.deptName, 
          phone: dept.phone, 
          email: dept.email, 
          address: dept.address,
          status: dept.status,
          remark: dept.remark,
          tenantId: dept.tenantId,
          parentId: dept.parentId  // 保留原始parentId
        };
      } else {
        // 关键修复：获取当前公司的根部门ID
        const rootDeptId = this.getRootDeptId(this.current.data.id);
        
        this.deptDialog.form = { 
          deptCode: '',
          deptName: "", 
          phone: "", 
          email: "", 
          address: "",
          status: 1,
          remark: "",
          tenantId: this.current.data.id,
          parentId: rootDeptId  // 设置为根部门ID
        };
      }
      
      this.$nextTick(() => {
        if (this.$refs.deptForm) {
          this.$refs.deptForm.clearValidate();
        }
      });
    },
    
    async saveDept() {
      this.$refs.deptForm.validate(async valid => {
        if (valid) {
          try {
            let result;
            if (this.deptDialog.isEdit) {
              result = await updateDept(this.deptDialog.form.id, this.deptDialog.form);
            } else {
              result = await createDept(this.deptDialog.form);
            }
            
            if (result.code === 200) {
              ElMessage.success(this.deptDialog.isEdit ? "部门信息更新成功" : "部门添加成功");
              await this.loadDeptsForTenant(this.deptDialog.form.tenantId);
              this.deptDialog.visible = false;
            } else {
              ElMessage.error(result.message || "操作失败");
            }
          } catch (error) {
            console.error("保存部门失败:", error);
            if (error.response && error.response.data) {
              ElMessage.error(error.response.data.message || "操作失败");
            } else {
              ElMessage.error("操作失败，请检查数据格式");
            }
          }
        }
      });
    },
    
    // 删除部门
    async deleteDept(dept) {
      try {
        await ElMessageBox.confirm(
          `确定要删除 "${dept.deptName}" 部门吗？此操作不可恢复。`,
          "删除确认",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        );
        
        // 正确传递部门ID
        const result = await deleteDept(dept.id);
        if (result.code === 200) {
          ElMessage.success("部门已删除");
          // 刷新部门数据
          await this.loadDeptsForTenant(dept.tenantId);
          // 如果当前正在查看被删除的部门，则清空当前视图
          if (this.current.type === "department" && this.current.data.id === dept.id) {
            this.current.type = "";
            this.current.data = null;
          }
        } else {
          ElMessage.error(result.message || "删除失败");
        }
      } catch (error) {
        // 用户取消删除
      }
    },
    
    // 获取公司名称
    getCompanyName(dept) {
      const company = this.companies.find(c => c.id === dept.tenantId);
      return company ? company.tenantName : "未知公司";
    }
  }
};
</script>



<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Inter', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.department-management {
  background-color: #f5f7fa;
  color: #333;
  line-height: 1.6;
  min-height: 100vh;
  padding: 20px;
}

/* 顶部标题区域 - 修改为紫色渐变 */
.tenant-management {
  background: linear-gradient(135deg, #7e57c2 0%, #4527a0 100%);
  border-radius: 12px;
  padding: 25px 30px;
  margin-bottom: 24px;
  box-shadow: 0 6px 20px rgba(69, 39, 160, 0.2);
  color: white;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-content {
  display: flex;
  flex-direction: column;
}

.page-title {
  display: flex;
  align-items: center;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
  color: white;
}

.page-title i {
  margin-right: 15px;
  font-size: 32px;
}

.page-description {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
  max-width: 600px;
}

/* 搜索区域 - 按钮位置调整到左下角 */
.search-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.search-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #7e57c2;
  margin-bottom: 20px;
}

.search-title i {
  margin-right: 10px;
  font-size: 20px;
}

.search-form {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.form-actions {
  grid-column: 1 / span 3;
  display: flex;
  justify-content: flex-start;
  gap: 12px;
  margin-top: 10px;
}

.search-btn {
  background: #409eff;
  border: none;
  transition: all 0.3s ease;
  padding: 10px 20px;
}

.search-btn:hover {
  background: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.reset-btn {
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  color: #606266;
  padding: 10px 20px;
}

/* 主内容区 */
.main-container {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  height: calc(100vh - 280px);
}

/* 左侧树形结构 - 移除部门状态显示 */
.tree-container {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  height: 100%;
}

.tree-header {
  padding: 18px 24px;
  background: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  font-size: 16px;
  font-weight: 600;
  color: #7e57c2;
  display: flex;
  align-items: center;
}

.tree-header i {
  margin-right: 10px;
  font-size: 18px;
}

.tree-content {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.custom-tree-node {
  display: flex;
  align-items: center;
  padding: 8px 0;
  width: 100%;
}

.custom-tree-node i {
  margin-right: 8px;
  font-size: 16px;
}

.company-node i {
  color: #7e57c2;
}

.department-node i {
  color: #0d9e0d;
}

/* 右侧内容区 */
.content-container {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content-header {
  padding: 18px 24px;
  background: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.content-header h2 {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.content-header h2 i {
  margin-right: 10px;
  color: #7e57c2;
}

.content-body {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 40px;
  text-align: center;
}

.empty-icon {
  font-size: 80px;
  color: #c0c4cc;
  margin-bottom: 20px;
}

.empty-title {
  font-size: 18px;
  color: #909399;
  margin-bottom: 10px;
}

.empty-desc {
  color: #c0c4cc;
  max-width: 400px;
}

/* 部门列表 */
.dept-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.add-dept-btn {
  background: #409eff;
  border: none;
  transition: all 0.3s ease;
  padding: 10px 20px;
}

.add-dept-btn:hover {
  background: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.dept-table {
  margin-top: 20px;
}

/* 部门详情 - 美化展示 */
.dept-detail-card {
  background: white;
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 24px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  border: 1px solid #ebeef5;
}

.dept-header {
  margin-bottom: 20px;
}

.dept-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.dept-name {
  display: flex;
  align-items: center;
}

.dept-name i {
  font-size: 28px;
  color: #409eff;
  margin-right: 15px;
}

.dept-name h3 {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.status-tag {
  font-size: 14px;
  padding: 6px 15px;
  border-radius: 20px;
}

.dept-path {
  display: flex;
  align-items: center;
  color: #7e57c2;
  font-size: 15px;
  padding-left: 42px;
}

.dept-path i {
  margin-right: 8px;
}

.dept-details {
  padding: 0 15px;
}

.detail-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 18px;
  padding-bottom: 8px;
  border-bottom: 1px dashed #ebeef5;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.detail-item {
  display: flex;
  flex-direction: column;
}

.detail-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.detail-value {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.dept-description {
  background: #f9fafc;
  padding: 18px;
  border-radius: 8px;
  border-left: 4px solid #409eff;
  color: #606266;
  line-height: 1.7;
}

.action-buttons {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.edit-btn {
  background: #409eff;
  border: none;
  padding: 10px 20px;
}

.edit-btn:hover {
  background: #66b1ff;
}

.delete-btn {
  background: linear-gradient(135deg, #ef4444 0%, #b91c1c 100%);
  border: none;
  padding: 10px 20px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-container {
    grid-template-columns: 1fr;
    height: auto;
  }
  
  .tree-container {
    height: 400px;
  }
}

@media (max-width: 768px) {
  .search-form {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    grid-column: 1;
    justify-content: flex-end;
  }
  
  .detail-grid {
    grid-template-columns: 1fr;
  }
}

/* 对话框美化 */
:deep(.dept-dialog) {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

:deep(.dept-dialog .el-dialog__header) {
  background: #f5f7fa;
  padding: 20px 24px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.dept-dialog .el-dialog__title) {
  font-size: 18px;
  font-weight: 600;
  color: #7e57c2;
}

:deep(.dept-dialog .el-dialog__body) {
  padding: 25px 30px;
}

:deep(.dept-dialog .el-form-item) {
  margin-bottom: 22px;
}

:deep(.dept-dialog .el-input__inner) {
  border-radius: 8px;
  padding: 10px 15px;
}

:deep(.dept-dialog .el-dialog__footer) {
  padding: 15px 24px;
  border-top: 1px solid #ebeef5;
}

/* 新增：对话框按钮样式 */
:deep(.dept-dialog .el-dialog__footer .el-button) {
  padding: 10px 24px;
  font-size: 14px;
  min-width: 100px;
  margin-left: 15px;
}

/* 新增：两列布局间距调整 */
:deep(.dept-dialog .el-row) {
  margin-bottom: 10px;
}
</style>