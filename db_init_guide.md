# 数据库初始化指南

本指南将帮助你初始化CEMH系统的数据库，包括基础数据和课程管理子系统的数据。

## 准备工作

1. 确保已安装MySQL数据库（推荐5.7+或8.0+版本）
2. 准备好数据库管理工具（如MySQL Workbench、Navicat、phpMyAdmin等）
3. 确保所有SQL文件都在同一目录下

## 方法一：使用批处理脚本（推荐，仅Windows）

在Windows系统上，可以直接运行`init_database.bat`脚本来初始化数据库：

```bash
init_database.bat
```

这将自动完成所有的数据库初始化步骤。

## 方法二：手动导入

### 1. 创建数据库

首先，创建一个名为`cemh_system`的数据库：

```sql
CREATE DATABASE IF NOT EXISTS cemh_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cemh_system;
```

### 2. 导入SQL文件

按照以下顺序导入SQL文件：

#### 2.1 导入基础架构

导入`cemh_database_schema_fixed.sql`文件，这将创建系统所需的基本表结构：

```bash
mysql -u用户名 -p密码 cemh_system < cemh_database_schema_fixed.sql
```

#### 2.2 导入基础数据

导入`cemh_database_data_fixed.sql`文件，这将添加系统所需的基础数据：

```bash
mysql -u用户名 -p密码 cemh_system < cemh_database_data_fixed.sql
```

#### 2.3 添加外键约束

导入`add_foreign_keys.sql`文件，这将为表添加外键约束：

```bash
mysql -u用户名 -p密码 cemh_system < add_foreign_keys.sql
```

#### 2.4 更新课程表字段

导入`update_course_fields.sql`文件，这将更新课程表的特定字段：

```bash
mysql -u用户名 -p密码 cemh_system < update_course_fields.sql
```

#### 2.5 更新学习记录表

导入`update_learning_tables.sql`文件，这将更新学习记录相关的表结构：

```bash
mysql -u用户名 -p密码 cemh_system < update_learning_tables.sql
```

## 使用图形化工具导入

如果你使用图形化工具（如MySQL Workbench、Navicat等），请按照以下步骤操作：

1. 连接到MySQL服务器
2. 创建名为`cemh_system`的数据库
3. 选择`cemh_system`数据库
4. 按照上述顺序依次导入SQL文件：
   - cemh_database_schema_fixed.sql
   - cemh_database_data_fixed.sql
   - add_foreign_keys.sql
   - update_course_fields.sql
   - update_learning_tables.sql

## 验证安装

导入完成后，可以执行以下SQL语句验证表是否创建成功：

```sql
USE cemh_system;
SHOW TABLES;
SELECT COUNT(*) FROM course;
SELECT COUNT(*) FROM course_category;
SELECT COUNT(*) FROM course_chapter;
SELECT COUNT(*) FROM learning_record;
```

## 检查视图和触发器

验证视图和触发器是否成功创建：

```sql
SHOW FULL TABLES WHERE table_type = 'VIEW';
SHOW TRIGGERS;
```

## 配置应用连接

后端应用的数据库连接已配置在`application.yml`文件中：

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cemh_system?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true
    username: root
    password: hyx123456
```

如需修改连接信息，请更新上述配置。

## 故障排除

- 如果导入过程中出现错误，请检查SQL文件的语法是否正确
- 确保按照正确的顺序导入SQL文件
- 检查数据库用户是否有足够的权限创建表和插入数据
- 如果有外键约束错误，可能是数据不一致，请检查数据完整性
- 如果应用连接失败，请确认数据库用户名、密码和数据库名是否正确 