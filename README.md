# NEUsoftwarePracticaltraining
## 1.Web端登录功能
	1.1 注册功能：
	1.2 登录功能
	1.3 个人信息管理

## 1.Web端用户管理子系统
	1.1用户管理
		1.1.1  用户组织架构列表  
		1.1.2 用户列表根据条件查询
		1.1.3 新增用户
		1.1.4 用户修改
		1.1.5 用户删除
	1.2 租户管理
		1.2.1  租户列表 
		1.2.2 新增租户
		1.2.3 租户修改
		1.2.4 租户删除

## 2.Web端组织管理子系统
	2.1部门管理
		2.1.1部门列表
		2.1.2 新增部门
		2.1.3 部门修改
		2.1.4 部门删除

## 3.Web端行业动态管理子系统（邵）
	3.1  资讯管理
		3.1.1资讯列表
		3.1.2 新增资讯
		3.1.3 资讯修改
		3.1.4 资讯删除

## 4.Web端课程管理子系统（黄）
	4.1 课程管理
		4.1.1课程列表
		4.1.2 新增课程
		4.1.3 课程修改
		4.1.4 课程删除

## 5.Web端会议管理子系统（澈）
	5.1 会议管理
		5.1.1 会议列表
		5.1.2 新增会议
		5.1.3 会议修改
		5.1.4 会议删除

## 课程管理模块

### 功能特性

课程管理子系统是一个完整的在线学习平台，主要功能包括：

1. **课程管理**
   - 课程的增删改查
   - 课程分类管理
   - 课程章节管理
   - 多媒体内容管理（图片和视频）

2. **学习记录**
   - 学习进度追踪
   - 学习统计分析
   - 课程完成率统计
   - 个人学习记录管理

### 技术栈

- 前端：Vue 3 + Element Plus
- 后端：Spring Boot + MyBatis Plus
- 数据库：MySQL

### 数据库设计

课程管理子系统包含以下主要表：

- `course`: 课程信息表，存储课程基本信息
- `course_category`: 课程分类表，用于分类管理课程
- `course_chapter`: 课程章节表，存储课程章节内容
- `learning_record`: 学习记录表，记录用户学习情况

### API接口

课程管理子系统提供了完整的RESTful API接口：

#### 课程接口
- `GET /api/courses`: 获取课程列表
- `GET /api/courses/{id}`: 获取课程详情
- `POST /api/courses`: 创建课程
- `PUT /api/courses/{id}`: 更新课程
- `DELETE /api/courses/{id}`: 删除课程
- `GET /api/courses/categories`: 获取课程分类
- `GET /api/courses/hot`: 获取热门课程
- `GET /api/courses/latest`: 获取最新课程

#### 学习记录接口
- `GET /api/learning-records`: 获取学习记录列表
- `POST /api/learning-records`: 创建/更新学习记录
- `DELETE /api/learning-records/{id}`: 删除学习记录
- `PUT /api/learning-records/progress`: 更新学习进度
- `GET /api/learning-records/user-stats`: 获取用户学习统计
- `GET /api/learning-records/course-stats`: 获取课程学习统计
- `POST /api/learning-records/complete`: 完成课程学习

### 前端页面

1. **课程管理页面**
   - 课程列表展示与管理
   - 课程表单（新增/编辑）
   - 课程批量操作

2. **学习记录页面**
   - 学习进度统计展示
   - 课程学习记录列表
   - 在线学习界面
   - 学习进度追踪
