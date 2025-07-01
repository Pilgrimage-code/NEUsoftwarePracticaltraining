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
            {{ current.data.name }} — 部门管理
          </h2>
          <h2 v-else-if="current.type === 'department'">
            <i class="el-icon-s-management"></i>
            {{ current.data.name }} 部门详情
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
                <p class="page-description">共 {{ current.data.departments.length }} 个部门</p>
              </div>
            </div>
            
            <el-table :data="current.data.departments" class="dept-table">
              <el-table-column prop="name" label="部门名称" width="220">
                <template #default="scope">
                  <div style="display: flex; align-items: center;">
                    <i class="el-icon-s-management" style="color: #0d9e0d; margin-right: 8px;"></i>
                    <span>{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="leader" label="部门负责人" width="150"></el-table-column>
              <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
                    {{ scope.row.status }}
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
                    <h3>{{ current.data.name }}</h3>
                  </div>
                  <el-tag :type="current.data.status === '正常' ? 'success' : 'danger'" class="status-tag">
                    {{ current.data.status }}
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
                      <span class="detail-label">部门负责人：</span>
                      <span class="detail-value">{{ current.data.leader || '未设置' }}</span>
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
                      <span class="detail-label">创建时间：</span>
                      <span class="detail-value">{{ current.data.createTime }}</span>
                    </div>
                  </div>
                </div>
                
                <div class="detail-section">
                  <h4 class="section-title">部门描述</h4>
                  <p class="dept-description">
                    {{ current.data.description || '暂无部门描述信息' }}
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
              <el-table-column prop="name" label="部门名称" width="220">
                <template #default="scope">
                  <div style="display: flex; align-items: center;">
                    <i class="el-icon-s-management" style="color: #0d9e0d; margin-right: 8px;"></i>
                    <span>{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="leader" label="部门负责人" width="150"></el-table-column>
              <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                  <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
                    {{ scope.row.status }}
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
    
    <!-- 部门弹窗（新增/修改）- 优化布局 -->
    <el-dialog 
      :title="deptDialog.title" 
      v-model="deptDialog.visible"
      width="600px"
      :close-on-click-modal="false"
      custom-class="dept-dialog"
    >
      <el-form :model="deptDialog.form" label-width="100px" :rules="deptRules" ref="deptForm">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门名称" prop="name">
              <el-input 
                v-model="deptDialog.form.name" 
                placeholder="请输入部门名称"
                size="medium"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人">
              <el-input 
                v-model="deptDialog.form.leader" 
                placeholder="请输入负责人姓名"
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
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="deptDialog.form.status">
            <el-radio label="正常">正常</el-radio>
            <el-radio label="停用">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="部门描述">
          <el-input
            type="textarea"
            :rows="3"
            v-model="deptDialog.form.description"
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

export default {
  name: 'DepartmentManagement',
  data() {
    return {
      // 表单验证规则
      deptRules: {
        name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
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
        form: {}, 
        isEdit: false
      },
      
      // 搜索状态
      isSearchResult: false,
      searchResultList: [],
      
      // 模拟数据 - 3个公司，每个公司2-3个部门
      companies: [
        {
          id: 1, 
          name: "科技先锋有限公司", 
          departments: [
            {
              id: 11, 
              name: "技术研发部", 
              leader: "张明", 
              phone: "13800001111", 
              email: "tech@example.com",
              status: "正常", 
              createTime: "2025-01-10", 
              description: "负责公司核心技术研发与创新，推动技术升级和产品迭代"
            },
            {
              id:12, 
              name:"市场拓展部", 
              leader:"刘洋", 
              phone:"13900002222", 
              email:"market@example.com",
              status:"正常", 
              createTime:"2025-02-15", 
              description: "负责市场调研、品牌推广及业务拓展，制定营销策略"
            }
          ]
        },
        {
          id:2, 
          name:"创新教育集团", 
          departments:[
            {
              id:21, 
              name:"教学管理部", 
              leader:"周华", 
              phone:"13800003333", 
              email:"teach@example.com",
              status:"正常", 
              createTime:"2025-03-20", 
              description: "负责课程设计、教学质量管理及教师培训工作"
            },
            {
              id:22, 
              name:"运营支持部", 
              leader:"钱琳", 
              phone:"13800004444", 
              email:"support@example.com",
              status:"停用", 
              createTime:"2025-04-05", 
              description: "负责日常运营管理、客户服务及后勤支持工作"
            }
          ]
        },
        {
          id:3, 
          name:"未来制造集团", 
          departments:[
            {
              id:31, 
              name:"生产管理部", 
              leader:"朱军", 
              phone:"13800005555", 
              email:"production@example.com",
              status:"正常", 
              createTime:"2025-05-12", 
              description: "负责生产计划制定、生产流程优化及产品质量控制"
            },
            {
              id:32, 
              name:"质量控制部", 
              leader:"陈明", 
              phone:"13700006666", 
              email:"quality@example.com",
              status:"正常", 
              createTime:"2025-06-18", 
              description: "负责原材料检验、生产过程监控及成品质量检测"
            }
          ]
        }
      ]
    };
  },
  computed: {
    // 树形数据
    treeData() {
      return this.companies.map(c => ({
        id: c.id, 
        label: c.name, 
        children: c.departments.map(d => ({
          id: d.id, 
          label: d.name, 
          type: "department", 
          data: d
        })), 
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
  methods: {
    // 搜索部门
    handleSearch() {
      const { company, department, deptStatus } = this.filters;
      
      // 检查搜索条件是否全部为空
      if (!company && !department && !deptStatus) {
        ElMessage.warning('请输入要查找的公司或部门信息');
        return;
      }
      
      this.searchResultList = [];
      
      this.companies.forEach(comp => {
        // 检查公司名称匹配
        if (company && !comp.name.toLowerCase().includes(company.toLowerCase())) return;
        
        comp.departments.forEach(dept => {
          // 检查部门名称匹配
          if (department && !dept.name.toLowerCase().includes(department.toLowerCase())) return;
          
          // 检查部门状态匹配
          if (deptStatus && dept.status !== deptStatus) return;
          
          // 添加匹配的部门到搜索结果
          this.searchResultList.push({
            ...dept,
            companyName: comp.name,
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
        const department = company.departments.find(d => d.id === dept.id);
        if (department) {
          this.current.type = "department";
          this.current.data = department;
          this.isSearchResult = false;
        }
      }
    },
    
    // 处理树节点点击
    handleTreeNodeClick(node) {
      if (node.type === "company") {
        this.current.type = "company";
        this.current.data = node.data;
      } else if (node.type === "department") {
        this.current.type = "department";
        this.current.data = node.data;
      }
      this.isSearchResult = false;
    },
    
    // 打开部门对话框
    openDeptDialog(dept = null) {
      this.deptDialog.isEdit = !!dept;
      this.deptDialog.title = dept ? "修改部门信息" : "添加新部门";
      this.deptDialog.visible = true; // 确保对话框可见
      
      if (dept) {
        // 编辑：复制当前部门数据
        this.deptDialog.form = { ...dept };
      } else {
        // 新增：初始化表单，默认状态为"正常"
        this.deptDialog.form = { 
          id: Date.now(), // 生成唯一ID
          name: "", 
          leader: "", 
          phone: "", 
          email: "", 
          status: "正常", 
          createTime: new Date().toISOString().slice(0, 10), // 当前日期
          description: ""
        };
      }
      
      // 重置表单验证状态
      this.$nextTick(() => {
        if (this.$refs.deptForm) {
          this.$refs.deptForm.clearValidate();
        }
      });
    },
    
    // 保存部门信息
    saveDept() {
      this.$refs.deptForm.validate(valid => {
        if (valid) {
          const formData = this.deptDialog.form;
          
          if (this.deptDialog.isEdit) {
            // 更新部门信息
            const company = this.companies.find(c => 
              c.departments.some(d => d.id === formData.id)
            );
            
            if (company) {
              const deptIndex = company.departments.findIndex(d => d.id === formData.id);
              if (deptIndex !== -1) {
                // 更新部门信息
                company.departments.splice(deptIndex, 1, formData);
                ElMessage.success("部门信息更新成功");
                
                // 更新当前视图
                if (this.current.data && this.current.data.id === formData.id) {
                  this.current.data = formData;
                }
              }
            }
          } else {
            // 添加新部门：添加到当前选中的公司
            if (this.current.type === 'company') {
              const company = this.current.data;
              // 添加到公司部门列表
              company.departments.push(formData);
              ElMessage.success("部门添加成功");
            } else {
              ElMessage.error("请先选择公司");
            }
          }
          
          this.deptDialog.visible = false;
        }
      });
    },
    
    // 删除部门
    deleteDept(dept) {
      ElMessageBox.confirm(
        `确定要删除 "${dept.name}" 部门吗？此操作不可恢复。`,
        "删除确认",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        const company = this.companies.find(c => 
          c.departments.some(d => d.id === dept.id)
        );
        
        if (company) {
          // 删除部门
          company.departments = company.departments.filter(d => d.id !== dept.id);
          ElMessage.success("部门已删除");
          
          // 如果当前正在查看被删除的部门
          if (this.current.type === "department" && this.current.data.id === dept.id) {
            this.current.type = "";
            this.current.data = null;
          }
          // 如果当前正在查看被删除部门所在的公司，则需要更新公司视图
          else if (this.current.type === "company" && this.current.data.id === company.id) {
            this.current.data = { ...company };
          }
        }
      }).catch(() => {});
    },
    
    // 获取公司名称
    getCompanyName(dept) {
      const company = this.companies.find(c => 
        c.departments.some(d => d.id === dept.id)
      );
      return company ? company.name : "未知公司";
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