# 🎓 测盟汇管理系统 (CEMH)

<div align="center">

[![Contributors](https://img.shields.io/github/contributors/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=for-the-badge)](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/graphs/contributors)
[![Forks](https://img.shields.io/github/forks/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=for-the-badge)](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/network/members)
[![Stargazers](https://img.shields.io/github/stars/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=for-the-badge)](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/stargazers)
[![Issues](https://img.shields.io/github/issues/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=for-the-badge)](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues)
[![MIT License](https://img.shields.io/github/license/Pilgrimage-code/NEUsoftwarePracticaltraining.svg?style=for-the-badge)](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/blob/main/LICENSE.txt)

<img src="images/logo.png" alt="CEMH Logo" width="120" height="120">

### 🏫 Campus Educational Meeting Hub

**一个现代化的企业级多租户教育管理平台**

[📖 查看文档](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/blob/main/ARCHITECTURE.md) • 
[🚀 快速开始](#-快速开始) • 
[🐛 报告问题](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues) • 
[💡 功能建议](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues)

</div>

---

## 📋 项目简介

测盟汇管理系统（Campus Educational Meeting Hub，CEMH）是一个面向电子质量管理协会会员单位和员工的综合性管理平台。该系统采用多租户架构，为不同的租户提供独立的环境，支持会议管理、课程管理、用户管理、新闻资讯等核心功能。

### ✨ 核心特性

- 🏢 **多租户架构**：支持多个教育机构独立使用，数据完全隔离
- 🔐 **安全认证**：基于JWT的用户认证和权限管理系统
- 📊 **智能分析**：集成Diffy大模型，支持自然语言数据查询和可视化
- 📱 **响应式设计**：完美适配桌面端和移动端设备
- 🔄 **实时通信**：支持实时消息推送和在线聊天功能
- 🎯 **模块化设计**：高度模块化的系统架构，易于扩展和维护

### 🎯 适用场景

- 🏫 高等院校教务管理
- 🏢 企业培训中心
- 📚 在线教育平台
- 🎓 职业技能培训机构

---

## 🚀 技术栈

### 后端技术
- **框架**：Spring Boot 2.7.x
- **数据库**：人大金仓 KingbaseES
- **ORM**：MyBatis Plus
- **安全**：Spring Security + JWT
- **缓存**：Redis
- **构建工具**：Maven

### 前端技术
- **框架**：Vue.js 3.x
- **UI库**：Element Plus
- **状态管理**：Vuex
- **路由**：Vue Router
- **构建工具**：Vite
- **HTTP客户端**：Axios

### AI集成
- **大模型**：DeepSeek Chat
- **工作流引擎**：Diffy
- **数据分析**：Text2SQL + 智能可视化

---

## 🏗️ 系统架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端展示层     │    │   后端服务层     │    │   数据存储层     │
│   (Vue.js)      │◄──►│ (Spring Boot)   │◄──►│  (KingbaseES)   │
│                 │    │                 │    │                 │
│ • 用户界面       │    │ • 业务逻辑       │    │ • 数据持久化     │
│ • 交互控制       │    │ • API服务       │    │ • 多租户隔离     │
│ • 状态管理       │    │ • 安全认证       │    │ • 数据备份       │
│ • 路由管理       │    │ • 权限控制       │    │ • 性能优化       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                ↕
                       ┌─────────────────┐
                       │   AI分析层       │
                       │   (Diffy)       │
                       │                 │
                       │ • 自然语言理解   │
                       │ • SQL智能生成    │
                       │ • 数据可视化     │
                       │ • 多格式输出     │
                       └─────────────────┘
```

---



## 🎯 功能模块

### 👥 用户管理模块
- **用户注册/登录**：支持邮箱注册、密码登录
- **权限管理**：基于角色的权限控制（RBAC）
- **个人中心**：用户信息管理、密码修改、头像上传
- **多租户支持**：租户隔离、数据安全保障

### 🏢 组织管理模块
- **部门管理**：部门层级结构、人员分配
- **角色管理**：自定义角色、权限分配
- **租户管理**：多机构独立管理、配置隔离

### 📅 会议管理模块
- **会议创建**：会议信息录入、参会人员邀请
- **会议安排**：时间冲突检测、会议室预约
- **会议记录**：会议纪要、文件共享
- **通知提醒**：邮件通知、系统消息推送

### 📚 课程管理模块
- **课程发布**：课程信息管理、教学计划制定
- **学员管理**：学员注册、学习进度跟踪
- **教学资源**：课件上传、在线学习支持
- **成绩管理**：考试安排、成绩统计分析

### 📰 资讯管理模块
- **新闻发布**：富文本编辑、图片上传
- **分类管理**：新闻分类、标签管理
- **内容审核**：发布审核、内容管理
- **阅读统计**：浏览量统计、热门推荐

### 💬 聊天通信模块
- **即时通讯**：实时消息发送、在线状态显示
- **群组聊天**：创建群组、群组管理
- **文件传输**：文件发送、图片分享
- **消息记录**：聊天历史、消息搜索

### 📊 智能分析模块（AI驱动）
- **自然语言查询**：用自然语言查询数据
- **智能图表生成**：自动选择最佳可视化方案
- **数据导出**：支持Excel、PDF等多种格式
- **报表定制**：个性化报表生成

---

## 🚀 快速开始

### 📋 环境要求

- **Java**：JDK 11 或更高版本
- **Node.js**：16.x 或更高版本
- **KingbaseES**：V8R6 或更高版本
- **Redis**：6.x 或更高版本（可选）
- **Maven**：3.6 或更高版本

### 📥 安装步骤

#### 1. 克隆项目
```bash
git clone https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining.git
cd NEUsoftwarePracticaltraining
```

#### 2. 数据库配置
```bash
# 创建数据库（使用金仓数据库客户端）
ksql -U system -W
CREATE DATABASE cemh_system;

# 导入数据库结构
ksql -U system -W -d cemh_system -f cemh_database_schema_fixed.sql

# 导入初始数据
ksql -U system -W -d cemh_system -f cemh_database_data_fixed.sql
```

#### 3. 后端部署
```bash
# 进入后端目录
cd cemh-backend

# 修改配置文件
cp src/main/resources/application.yml.example src/main/resources/application.yml
# 编辑 application.yml，配置数据库连接信息

# 安装依赖并启动
mvn clean install
mvn spring-boot:run
```

#### 4. 前端部署
```bash
# 进入前端目录
cd cemh-frontend

# 安装依赖
npm install

# 开发环境启动
npm run dev

# 生产环境构建
npm run build
```

### 🔧 配置说明

#### 后端配置 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:kingbase8://localhost:54321/cemh_system
    username: your_username
    password: your_password
    driver-class-name: com.kingbase8.Driver
  
  redis:
    host: localhost
    port: 6379
    password: your_redis_password

jwt:
  secret: your_jwt_secret_key
  expiration: 86400000
```

#### 前端配置 (.env)
```env
# API基础地址
VITE_API_BASE_URL=http://localhost:8080/api

# 应用标题
VITE_APP_TITLE=测盟汇管理系统
```

---


## 📁 项目结构

```
NEUsoftwarePracticaltraining/
├── 📁 cemh-backend/                 # 后端Spring Boot项目
│   ├── 📁 src/main/java/com/cemh/
│   │   ├── 📁 common/               # 通用工具类
│   │   ├── 📁 config/               # 配置类
│   │   ├── 📁 controller/           # 控制器层
│   │   │   ├── 📄 AuthController.java
│   │   │   ├── 📄 NewsController.java
│   │   │   └── 📄 NewsCategoryController.java
│   │   ├── 📁 dto/                  # 数据传输对象
│   │   ├── 📁 entity/               # 实体类
│   │   ├── 📁 mapper/               # MyBatis映射器
│   │   ├── 📁 service/              # 服务层
│   │   │   └── 📁 impl/             # 服务实现
│   │   ├── 📁 utils/                # 工具类
│   │   └── 📁 vo/                   # 视图对象
│   ├── 📁 src/main/resources/
│   │   ├── 📁 mapper/               # MyBatis XML映射
│   │   ├── 📄 application.yml       # 应用配置
│   │   └── 📄 application-dev.yml   # 开发环境配置
│   ├── 📁 src/test/                 # 测试代码
│   └── 📄 pom.xml                   # Maven配置
├── 📁 cemh-frontend/                # 前端Vue.js项目
│   ├── 📁 public/                   # 静态资源
│   ├── 📁 src/
│   │   ├── 📁 api/                  # API接口定义
│   │   ├── 📁 assets/               # 静态资源
│   │   ├── 📁 components/           # 公共组件
│   │   ├── 📁 router/               # 路由配置
│   │   ├── 📁 store/                # Vuex状态管理
│   │   ├── 📁 utils/                # 工具函数
│   │   └── 📁 views/                # 页面组件
│   │       ├── 📁 auth/             # 认证页面
│   │       ├── 📁 course/           # 课程管理
│   │       ├── 📁 dashboard/        # 仪表板
│   │       ├── 📁 meeting/          # 会议管理
│   │       ├── 📁 news/             # 新闻管理
│   │       ├── 📁 system/           # 系统管理
│   │       └── 📁 user/             # 用户管理
│   ├── 📄 package.json              # 依赖配置
│   └── 📄 vite.config.js            # Vite配置
├── 📁 images/                       # 项目图片资源
├── 📄 cemh_database_schema_fixed.sql # 数据库结构
├── 📄 cemh_database_data_fixed.sql   # 初始数据
├── 📄 ARCHITECTURE.md               # 架构文档
├── 📄 LICENSE.txt                   # 开源协议
└── 📄 README.md                     # 项目说明
```

---

## 📖 使用说明

### 🔐 系统登录

1. **管理员账户**
   - 用户名：`admin@cemh.com`
   - 密码：`admin123`
   - 权限：系统管理员，拥有所有权限

2. **普通用户**
   - 可通过注册页面创建新账户
   - 需要管理员审核激活

### 🎯 主要功能使用

#### 📅 会议管理
1. 点击左侧菜单"会议管理"
2. 点击"新建会议"按钮
3. 填写会议信息（标题、时间、地点、参会人员）
4. 保存并发送邀请

#### 📚 课程管理
1. 进入"课程管理"模块
2. 创建新课程或编辑现有课程
3. 上传教学资源
4. 管理学员和成绩

#### 📰 新闻发布
1. 访问"资讯管理"页面
2. 点击"发布新闻"
3. 使用富文本编辑器编写内容
4. 选择分类并发布

#### 🤖 智能数据分析
1. 进入"数据分析"模块
2. 在输入框中用自然语言描述查询需求
   - 例如："显示本月的会议统计图表"
   - 例如："导出所有用户的Excel表格"
3. 系统自动生成相应的图表或报表

### 📱 移动端使用

系统完全支持移动端访问，主要功能：
- 📱 响应式界面设计
- 🔔 消息推送通知
- 📊 移动端数据查看
- 💬 移动端聊天功能

---

## 👥 开发团队

### 🎯 分工详情

| 成员 | 主要职责 | 负责模块 |
|------|----------|----------|
| **邵岩哲** | 项目负责人、架构师、组长 | • 系统架构设计<br>• 注册登录模块<br>• 资讯管理模块<br>• Diffy AI模型构建<br>• 单元测试 |
| **徐澈** | 前端开发工程师 | • Vue.js前端框架<br>• 用户界面设计<br>• 前端组件开发<br>• 前端路由管理 |
| **黄颜兴** | 后端开发工程师 | • Spring Boot后端<br>• API接口开发<br>• 业务逻辑实现<br>• 系统集成 |
| **李旋** | 数据库工程师 | • 数据库设计<br>• SQL优化<br>• 数据迁移<br>• 性能调优 |
| **尚承一** | UI/UX设计师 | • 界面设计<br>• 用户体验优化<br>• 视觉规范制定<br>• 原型设计 |

---


## 🧪 测试

### 单元测试

项目包含完整的单元测试，覆盖率达到 **95.2%**

```bash
# 运行后端测试
cd cemh-backend
mvn test

# 查看测试覆盖率报告
mvn clean test clover:clover
# 报告位置：target/site/clover/index.html
```

---

## 🚀 部署指南

### 🐳 Docker 部署

#### 1. 使用 Docker Compose（推荐）

```bash
# 克隆项目
git clone https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining.git
cd NEUsoftwarePracticaltraining

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

#### 2. 单独构建镜像

```bash
# 构建后端镜像
cd cemh-backend
docker build -t cemh-backend:latest .

# 构建前端镜像
cd cemh-frontend
docker build -t cemh-frontend:latest .
```

### ☁️ 云服务器部署

#### 1. 环境准备

```bash
# 安装Java 11
sudo apt update
sudo apt install openjdk-11-jdk

# 安装Node.js
curl -fsSL https://deb.nodesource.com/setup_16.x | sudo -E bash -
sudo apt-get install -y nodejs

# 安装KingbaseES
# 请参考人大金仓官方文档进行安装配置
```

#### 2. 应用部署

```bash
# 后端部署
cd cemh-backend
mvn clean package -DskipTests
nohup java -jar target/cemh-backend-1.0.0.jar > app.log 2>&1 &

# 前端部署
cd cemh-frontend
npm run build
# 将 dist 目录部署到 Nginx
```

#### 3. Nginx 配置

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /var/www/cemh-frontend/dist;
        try_files $uri $uri/ /index.html;
    }
    
    # 后端API代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 🤝 贡献指南

我们欢迎所有形式的贡献！请遵循以下步骤：

### 📝 贡献流程

1. **Fork 项目**
   ```bash
   # 点击页面右上角的 Fork 按钮
   ```

2. **创建功能分支**
   ```bash
   git checkout -b feature/AmazingFeature
   ```

3. **提交更改**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```

4. **推送到分支**
   ```bash
   git push origin feature/AmazingFeature
   ```

5. **创建 Pull Request**
   - 详细描述你的更改
   - 确保所有测试通过
   - 等待代码审查

### 📋 代码规范

- **Java**: 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- **JavaScript**: 遵循 [Airbnb JavaScript Style Guide](https://github.com/airbnb/javascript)
- **Vue.js**: 遵循 [Vue.js Style Guide](https://vuejs.org/style-guide/)

### 🐛 问题报告

发现 Bug？请创建 [Issue](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues) 并包含：

- 🔍 详细的问题描述
- 🔄 重现步骤
- 💻 运行环境信息
- 📸 截图（如适用）

---

## 📄 许可证

本项目基于 [MIT License](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/blob/main/LICENSE.txt) 开源协议。

---

## 🙏 致谢

感谢以下开源项目和服务：

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java应用框架
- [Vue.js](https://vuejs.org/) - 渐进式JavaScript框架
- [Element Plus](https://element-plus.org/) - 优秀的Vue 3组件库
- [MyBatis Plus](https://baomidou.com/) - 强大的MyBatis增强工具
- [DeepSeek](https://www.deepseek.com/) - 提供AI大模型支持
- [GitHub](https://github.com/) - 代码托管平台

---

## 📞 联系我们

- 📧 **邮箱**: [pilgrimage.code@example.com](mailto:pilgrimage.code@example.com)
- 🐛 **问题反馈**: [GitHub Issues](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/issues)
- 💬 **讨论交流**: [GitHub Discussions](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/discussions)

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给我们一个 Star！⭐**

Made with ❤️ by [CEMH Development Team](https://github.com/Pilgrimage-code/NEUsoftwarePracticaltraining/graphs/contributors)

</div>

