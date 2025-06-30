-- =============================================
-- 测盟汇管理系统数据库设计（修复版）
-- 版本: v2.1.0
-- 创建时间: 2024-06-16
-- 描述: 修复外键约束和字符编码问题
-- =============================================

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `cemh_system` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `cemh_system`;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 1. 租户管理表
-- =============================================
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '租户ID',
  `tenant_code` varchar(50) NOT NULL COMMENT '租户代码',
  `tenant_name` varchar(100) NOT NULL COMMENT '租户名称',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系人电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系人邮箱',
  `logo_url` varchar(500) DEFAULT NULL COMMENT '企业Logo地址',
  `domain` varchar(100) DEFAULT NULL COMMENT '企业域名',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=禁用，1=启用',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `max_users` int DEFAULT '100' COMMENT '最大用户数',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`tenant_code`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户信息表';

-- =============================================
-- 2. 部门管理表
-- =============================================
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门ID，0表示顶级部门',
  `dept_code` varchar(50) NOT NULL COMMENT '部门编码',
  `dept_name` varchar(100) NOT NULL COMMENT '部门名称',
  `dept_type` tinyint DEFAULT '1' COMMENT '部门类型：1=普通部门，2=虚拟部门',
  `leader_id` bigint DEFAULT NULL COMMENT '部门负责人ID',
  `phone` varchar(20) DEFAULT NULL COMMENT '部门电话',
  `email` varchar(100) DEFAULT NULL COMMENT '部门邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '部门地址',
  `sort_order` int DEFAULT '0' COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=禁用，1=启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_dept_code` (`tenant_id`, `dept_code`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门信息表';

-- =============================================
-- 3. 角色管理表
-- =============================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_type` tinyint DEFAULT '1' COMMENT '角色类型：1=普通角色，2=系统角色',
  `data_scope` tinyint DEFAULT '1' COMMENT '数据权限：1=全部，2=本部门，3=本部门及下级，4=仅本人',
  `sort_order` int DEFAULT '0' COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=禁用，1=启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_role_code` (`tenant_id`, `role_code`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- =============================================
-- 4. 用户管理表
-- =============================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `gender` tinyint DEFAULT '0' COMMENT '性别：0=男，1=女，2=未知',
  `avatar` varchar(500) DEFAULT NULL COMMENT '头像地址',
  `user_type` tinyint DEFAULT '1' COMMENT '用户类型：0=系统用户，1=企业用户',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=禁用，1=启用',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `password_update_time` datetime DEFAULT NULL COMMENT '密码更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_username` (`tenant_id`, `username`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_email` (`email`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- =============================================
-- 5. 用户角色关联表
-- =============================================
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 6. 会议管理表
-- =============================================
DROP TABLE IF EXISTS `meeting`;
CREATE TABLE `meeting` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会议标题',
  `description` text COLLATE utf8mb4_unicode_ci COMMENT '会议内容描述',
  `type` tinyint DEFAULT '1' COMMENT '会议类型：1=线下会议，2=线上会议，3=混合会议',
  `location` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会议地点',
  `meeting_link` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '线上会议链接',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `max_participants` int DEFAULT '0' COMMENT '最大参与人数，0表示不限制',
  `current_participants` int DEFAULT '0' COMMENT '当前报名人数',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶：0=否，1=是',
  `requires_approval` tinyint DEFAULT '0' COMMENT '是否需要审批：0=否，1=是',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  `registration_deadline` datetime DEFAULT NULL COMMENT '报名截止时间',
  `fee` int DEFAULT NULL COMMENT '会议费用',
  `cover_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会议封面图',
  `organizer` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '主办方',
  `meeting_password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会议密码',
  `tags` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `requirements` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参会要求',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_status` (`status`),
  KEY `idx_is_top` (`is_top`),
  CONSTRAINT `fk_meeting_creator` FOREIGN KEY (`creator_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_meeting_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_meeting_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1939597230884106243 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

-- =============================================
-- 7. 会议报名表
-- =============================================
DROP TABLE IF EXISTS `meeting_registration`;
CREATE TABLE `meeting_registration` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报名ID',
  `meeting_id` bigint NOT NULL COMMENT '会议ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `registration_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0=待审核，1=已通过，2=已拒绝，3=已取消',
  `approval_time` datetime DEFAULT NULL COMMENT '审核时间',
  `approval_by` bigint DEFAULT NULL COMMENT '审核人ID',
  `approval_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `attendance_status` tinyint DEFAULT '0' COMMENT '出席状态：0=未出席，1=已出席，2=请假',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_meeting_user` (`meeting_id`, `user_id`),
  KEY `idx_meeting_id` (`meeting_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会议报名表';

-- =============================================
-- 8. 新闻资讯表
-- =============================================
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `title` varchar(200) NOT NULL COMMENT '新闻标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '新闻摘要',
  `content` longtext COMMENT '新闻内容',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '封面图片',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `source` varchar(100) DEFAULT NULL COMMENT '来源',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签，多个用逗号分隔',
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `like_count` int DEFAULT '0' COMMENT '点赞次数',
  `comment_count` int DEFAULT '0' COMMENT '评论次数',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶：0=否，1=是',
  `is_hot` tinyint DEFAULT '0' COMMENT '是否热门：0=否，1=是',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=草稿，1=已发布，2=已下线',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_top` (`is_top`),
  KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻资讯表';

-- =============================================
-- 9. 新闻分类表
-- =============================================
DROP TABLE IF EXISTS `news_category`;
CREATE TABLE `news_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID，0表示顶级分类',
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) NOT NULL COMMENT '分类编码',
  `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
  `sort_order` int DEFAULT '0' COMMENT '显示顺序',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=禁用，1=启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_category_code` (`tenant_id`, `category_code`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻分类表';

-- =============================================
-- 10. 课程管理表
-- =============================================
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `course_code` varchar(50) NOT NULL COMMENT '课程编码',
  `course_name` varchar(200) NOT NULL COMMENT '课程名称',
  `course_intro` text COMMENT '课程简介',
  `course_author` varchar(100) DEFAULT NULL COMMENT '课程作者',
  `course_order` int DEFAULT '0' COMMENT '课程排序',
  `description` text COMMENT '课程描述',
  `cover_image` varchar(500) DEFAULT NULL COMMENT '课程封面',
  `video_url` varchar(500) DEFAULT NULL COMMENT '课程视频URL',
  `instructor` varchar(100) DEFAULT NULL COMMENT '讲师',
  `duration` int DEFAULT '0' COMMENT '课程时长（分钟）',
  `difficulty` tinyint DEFAULT '1' COMMENT '难度等级：1=初级，2=中级，3=高级',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '课程价格',
  `max_students` int DEFAULT '0' COMMENT '最大学员数，0表示不限制',
  `current_students` int DEFAULT '0' COMMENT '当前学员数',
  `view_count` int DEFAULT '0' COMMENT '浏览次数',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分（0-5分）',
  `rating_count` int DEFAULT '0' COMMENT '评分人数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0=草稿，1=已发布，2=已下线',
  `is_free` tinyint DEFAULT '1' COMMENT '是否免费：0=收费，1=免费',
  `is_hot` tinyint DEFAULT '0' COMMENT '是否热门：0=否，1=是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_course_code` (`tenant_id`, `course_code`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_hot` (`is_hot`),
  KEY `idx_course_order` (`course_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程信息表';

-- =============================================
-- 11. 学习记录表
-- =============================================
DROP TABLE IF EXISTS `learning_record`;
CREATE TABLE `learning_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `enrollment_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始学习时间',
  `completion_time` datetime DEFAULT NULL COMMENT '完成时间',
  `progress` decimal(5,2) DEFAULT '0.00' COMMENT '学习进度（百分比）',
  `study_duration` int DEFAULT '0' COMMENT '学习时长（分钟）',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0=未开始，1=学习中，2=已完成，3=已暂停',
  `last_chapter_id` bigint DEFAULT NULL COMMENT '最后学习章节ID',
  `last_study_time` datetime DEFAULT NULL COMMENT '最后学习时间',
  `score` decimal(5,2) DEFAULT NULL COMMENT '考试成绩',
  `certificate_url` varchar(500) DEFAULT NULL COMMENT '证书地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志：0=未删除，1=已删除',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_course` (`user_id`, `course_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_status` (`status`),
  KEY `idx_last_chapter_id` (`last_chapter_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习记录表';

-- =============================================
-- 12. 系统配置表
-- =============================================
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID，NULL表示全局配置',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(50) DEFAULT 'string' COMMENT '配置类型：string,number,boolean,json',
  `description` varchar(500) DEFAULT NULL COMMENT '配置描述',
  `is_system` tinyint DEFAULT '0' COMMENT '是否系统配置：0=否，1=是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_config_key` (`tenant_id`, `config_key`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- =============================================
-- 12. 课程分类表
-- =============================================
DROP TABLE IF EXISTS `course_category`;
CREATE TABLE `course_category` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父分类ID',
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) NOT NULL COMMENT '分类编码',
  `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
  `sort_order` int(11) DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_category_code` (`tenant_id`, `category_code`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程分类表';

-- =============================================
-- 13. 课程章节表
-- =============================================
DROP TABLE IF EXISTS `course_chapter`;
CREATE TABLE `course_chapter` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `chapter_name` varchar(200) NOT NULL COMMENT '章节名称',
  `chapter_order` int(11) NOT NULL DEFAULT 0 COMMENT '章节排序',
  `description` varchar(500) DEFAULT NULL COMMENT '章节描述',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `duration` int(11) DEFAULT 0 COMMENT '视频时长（秒）',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程章节表';

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

