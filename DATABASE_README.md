# 测盟汇平台数据库指南

本文档提供了测盟汇平台数据库的设置和维护指南。

## 数据库脚本

项目根目录包含以下数据库相关脚本：

- `setup_database.bat` - 完整初始化数据库（创建新数据库并导入所有数据）
- `update_database.bat` - 更新现有数据库结构（不删除现有数据）
- `update_course_fields.sql` - 修复课程相关表的字段
- `update_learning_tables.sql` - 修复学习记录表的字段
- `cemh_database_schema_fixed.sql` - 数据库表结构
- `cemh_database_data_fixed.sql` - 初始数据
- `add_foreign_keys.sql` - 外键约束设置

## 数据库配置

在运行脚本前，请确保您已安装MySQL并正确设置环境变量。默认的数据库配置如下：

- 数据库地址：localhost:3306
- 数据库名称：cemh_db
- 数据库用户：root
- 数据库密码：root

您可以编辑批处理脚本来更改这些配置。

## 初始化数据库

首次设置数据库时，请按照以下步骤操作：

1. 打开命令提示符或终端窗口
2. 导航到项目根目录
3. 运行 `setup_database.bat` 脚本
4. 根据提示输入确认信息

此脚本将：
- 创建新的数据库
- 导入表结构
- 导入初始数据
- 添加外键约束
- 应用所有修复脚本

## 更新数据库

如果您需要在不重新创建数据库的情况下更新表结构，请按照以下步骤操作：

1. 打开命令提示符或终端窗口
2. 导航到项目根目录
3. 运行 `update_database.bat` 脚本
4. 根据提示输入确认信息

此脚本将：
- 保留现有数据
- 更新课程相关表的字段
- 更新学习记录表的字段

## 数据库结构修复

以下是项目中修复的数据库结构问题：

1. 添加 `course_chapter` 表中的 `tenant_id` 字段
2. 添加 `learning_record` 表中的 `last_chapter_id` 和 `tenant_id` 字段

这些字段在实体类中定义，但在原始数据库结构中缺失。

## 注意事项

- 运行 `setup_database.bat` 将重新创建数据库，会删除已有数据
- 如果您不希望丢失现有数据，请使用 `update_database.bat`
- 数据库用户需要有创建数据库、表和修改权限

## 常见问题

1. **MySQL命令未找到**
   
   请确保MySQL已安装，且其bin目录已添加到系统PATH中。

2. **访问被拒绝**
   
   请检查数据库用户名和密码是否正确。

3. **数据库已存在**
   
   如果您运行setup_database.bat时收到数据库已存在的错误，请先删除现有数据库或改用update_database.bat。 