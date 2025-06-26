-- =============================================
-- 测盟汇管理系统初始化数据（修复版）
-- 版本: v2.1.0
-- 创建时间: 2024-06-16
-- 描述: 修复字段长度、外键约束和字符编码问题
-- =============================================

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE `cemh_system`;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 1. 租户初始化数据
-- =============================================
INSERT INTO `sys_tenant` (`id`, `tenant_code`, `tenant_name`, `contact_name`, `contact_phone`, `contact_email`, `status`, `max_users`, `remark`) VALUES
(1, 'demo', '演示企业', '张三', '13800138000', 'demo@cemh.org', 1, 500, '系统演示租户'),
(2, 'test', '测试企业', '李四', '13800138001', 'test@cemh.org', 1, 100, '系统测试租户');

-- =============================================
-- 2. 部门初始化数据
-- =============================================
INSERT INTO `sys_dept` (`id`, `tenant_id`, `parent_id`, `dept_code`, `dept_name`, `dept_type`, `sort_order`, `status`, `remark`) VALUES
(1, 1, 0, 'ROOT', '演示企业', 1, 0, 1, '根部门'),
(2, 1, 1, 'TECH', '技术部', 1, 1, 1, '技术研发部门'),
(3, 1, 1, 'MARKET', '市场部', 1, 2, 1, '市场营销部门'),
(4, 1, 1, 'HR', '人事部', 1, 3, 1, '人力资源部门'),
(5, 1, 2, 'DEV', '开发组', 1, 1, 1, '软件开发组'),
(6, 1, 2, 'TEST', '测试组', 1, 2, 1, '软件测试组'),
(7, 2, 0, 'ROOT', '测试企业', 1, 0, 1, '根部门'),
(8, 2, 7, 'ADMIN', '管理部', 1, 1, 1, '管理部门');

-- =============================================
-- 3. 角色初始化数据（缩短角色名称）
-- =============================================
INSERT INTO `sys_role` (`id`, `tenant_id`, `role_code`, `role_name`, `role_type`, `data_scope`, `sort_order`, `status`, `remark`) VALUES
(1, 1, 'SUPER_ADMIN', '超级管理员', 2, 1, 0, 1, '拥有所有权限'),
(2, 1, 'ADMIN', '管理员', 1, 2, 1, 1, '部门管理员'),
(3, 1, 'USER', '普通用户', 1, 4, 2, 1, '普通用户'),
(4, 1, 'MEETING_ADMIN', '会议管理员', 1, 1, 3, 1, '会议管理权限'),
(5, 1, 'NEWS_ADMIN', '新闻管理员', 1, 1, 4, 1, '新闻管理权限'),
(6, 2, 'ADMIN', '管理员', 1, 1, 0, 1, '测试租户管理员'),
(7, 2, 'USER', '普通用户', 1, 4, 1, 1, '测试租户普通用户');

-- =============================================
-- 4. 用户初始化数据（缩短昵称）
-- =============================================
INSERT INTO `sys_user` (`id`, `tenant_id`, `dept_id`, `username`, `nickname`, `password`, `phone`, `email`, `gender`, `user_type`, `status`, `remark`) VALUES
(1, 1, 1, 'admin', '管理员', '123456', '13800138000', 'admin@cemh.org', 0, 0, 1, '系统管理员'),
(2, 1, 2, 'tech_admin', '技术主管', '123456', '13800138001', 'tech@cemh.org', 0, 1, 1, '技术部门管理员'),
(3, 1, 5, 'developer', '开发工程师', '123456', '13800138002', 'dev@cemh.org', 0, 1, 1, '软件开发工程师'),
(4, 1, 3, 'marketing', '市场专员', '123456', '13800138003', 'market@cemh.org', 1, 1, 1, '市场营销专员'),
(5, 1, 4, 'hr_manager', '人事经理', '123456', '13800138004', 'hr@cemh.org', 1, 1, 1, '人力资源经理'),
(6, 2, 7, 'test_admin', '测试管理员', '123456', '13800138005', 'test@cemh.org', 0, 1, 1, '测试租户管理员'),
(7, 2, 8, 'test_user', '测试用户', '123456', '13800138006', 'user@cemh.org', 2, 1, 1, '测试租户普通用户');

-- =============================================
-- 5. 用户角色关联数据
-- =============================================
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- admin -> 超级管理员
(2, 2), -- tech_admin -> 管理员
(2, 4), -- tech_admin -> 会议管理员
(3, 3), -- developer -> 普通用户
(4, 3), -- marketing -> 普通用户
(4, 5), -- marketing -> 新闻管理员
(5, 2), -- hr_manager -> 管理员
(6, 6), -- test_admin -> 测试管理员
(7, 7); -- test_user -> 测试普通用户

-- =============================================
-- 6. 新闻分类初始化数据
-- =============================================
INSERT INTO `news_category` (`id`, `tenant_id`, `parent_id`, `category_name`, `category_code`, `description`, `sort_order`, `status`) VALUES
(1, 1, 0, '公司新闻', 'COMPANY_NEWS', '公司内部新闻动态', 1, 1),
(2, 1, 0, '行业资讯', 'INDUSTRY_NEWS', '行业相关资讯', 2, 1),
(3, 1, 0, '技术分享', 'TECH_SHARE', '技术文章和分享', 3, 1),
(4, 1, 1, '公司公告', 'COMPANY_NOTICE', '公司重要公告', 1, 1),
(5, 1, 1, '活动通知', 'ACTIVITY_NOTICE', '公司活动通知', 2, 1),
(6, 2, 0, '企业动态', 'COMPANY_NEWS', '企业新闻动态', 1, 1);

-- =============================================
-- 7. 新闻资讯初始化数据
-- =============================================
INSERT INTO `news` (`id`, `tenant_id`, `category_id`, `title`, `summary`, `content`, `author`, `source`, `view_count`, `like_count`, `is_top`, `status`, `publish_time`) VALUES
(1, 1, 1, '测盟汇管理系统正式上线', '经过团队的不懈努力，测盟汇管理系统正式上线运行', '<p>经过技术团队数月的精心开发和测试，测盟汇管理系统今日正式上线运行。</p><p>该系统集成了用户管理、会议管理、新闻发布、培训管理等多个功能模块，为企业提供一站式的管理解决方案。</p>', '系统管理员', '测盟汇', 156, 23, 1, 1, NOW()),
(2, 1, 3, 'Spring Boot 3.0 新特性解析', '详细介绍Spring Boot 3.0的新特性和改进', '<p>Spring Boot 3.0 带来了许多令人兴奋的新特性...</p>', '技术主管', '技术部', 89, 12, 0, 1, NOW()),
(3, 1, 2, '2024年企业数字化转型趋势', '分析2024年企业数字化转型的主要趋势', '<p>随着技术的不断发展，企业数字化转型已成为必然趋势...</p>', '市场专员', '市场部', 234, 45, 0, 1, NOW()),
(4, 1, 4, '关于系统维护的通知', '系统将于本周末进行例行维护', '<p>为了提供更好的服务，系统将于本周末进行例行维护...</p>', '系统管理员', '技术部', 67, 8, 1, 1, NOW()),
(5, 2, 6, '测试企业年度总结', '2024年度工作总结和展望', '<p>回顾2024年，我们取得了显著的成绩...</p>', '测试管理员', '测试企业', 45, 6, 0, 1, NOW());

-- =============================================
-- 8. 会议信息初始化数据
-- =============================================
INSERT INTO `meeting` (`id`, `tenant_id`, `dept_id`, `title`, `content`, `meeting_type`, `location`, `start_time`, `end_time`, `max_participants`, `current_participants`, `status`, `is_top`, `need_approval`, `creator_id`) VALUES
(1, 1, 2, '技术部月度例会', '讨论本月技术工作进展和下月计划', 1, '会议室A', '2024-06-20 14:00:00', '2024-06-20 16:00:00', 20, 8, 1, 1, 0, 2),
(2, 1, 1, '公司季度总结大会', '2024年第二季度工作总结和第三季度规划', 1, '大会议室', '2024-06-25 09:00:00', '2024-06-25 12:00:00', 100, 45, 1, 1, 1, 1),
(3, 1, 3, '市场推广策略研讨', '讨论新产品的市场推广策略', 2, '线上会议', '2024-06-22 10:00:00', '2024-06-22 11:30:00', 15, 6, 1, 0, 0, 4),
(4, 1, 5, '代码评审会议', '本周代码提交的评审和讨论', 1, '开发室', '2024-06-21 15:00:00', '2024-06-21 17:00:00', 10, 5, 1, 0, 0, 3),
(5, 2, 8, '测试企业周例会', '每周工作汇报和安排', 1, '会议室1', '2024-06-24 14:00:00', '2024-06-24 15:00:00', 10, 3, 1, 0, 0, 6);

-- =============================================
-- 9. 会议报名初始化数据
-- =============================================
INSERT INTO `meeting_registration` (`meeting_id`, `user_id`, `registration_time`, `status`, `approval_time`, `attendance_status`) VALUES
(1, 2, '2024-06-15 09:00:00', 1, '2024-06-15 09:05:00', 0),
(1, 3, '2024-06-15 10:00:00', 1, '2024-06-15 10:05:00', 0),
(1, 5, '2024-06-15 11:00:00', 1, '2024-06-15 11:05:00', 0),
(2, 1, '2024-06-16 08:00:00', 1, '2024-06-16 08:05:00', 0),
(2, 2, '2024-06-16 08:30:00', 1, '2024-06-16 08:35:00', 0),
(2, 3, '2024-06-16 09:00:00', 1, '2024-06-16 09:05:00', 0),
(2, 4, '2024-06-16 09:30:00', 1, '2024-06-16 09:35:00', 0),
(2, 5, '2024-06-16 10:00:00', 1, '2024-06-16 10:05:00', 0),
(3, 4, '2024-06-17 14:00:00', 1, '2024-06-17 14:05:00', 0),
(3, 1, '2024-06-17 15:00:00', 1, '2024-06-17 15:05:00', 0),
(4, 3, '2024-06-18 13:00:00', 1, '2024-06-18 13:05:00', 0),
(4, 2, '2024-06-18 14:00:00', 1, '2024-06-18 14:05:00', 0),
(5, 6, '2024-06-19 10:00:00', 1, '2024-06-19 10:05:00', 0),
(5, 7, '2024-06-19 11:00:00', 1, '2024-06-19 11:05:00', 0);

-- =============================================
-- 10. 课程信息初始化数据
-- =============================================
INSERT INTO `course` (`id`, `tenant_id`, `category_id`, `course_code`, `course_name`, `course_intro`, `course_author`, `course_order`, `description`, `cover_image`, `video_url`, `instructor`, `duration`, `difficulty`, `price`, `max_students`, `current_students`, `view_count`, `rating`, `rating_count`, `status`, `is_free`, `is_hot`) VALUES
(1, 1, 4, 'JAVA_001', 'Java基础编程', '本课程从零开始，深入浅出地讲解Java编程语言的基础知识，包括语法、面向对象、异常处理等核心概念', '张老师', 1, 'Java语言基础知识和编程技巧', '/uploads/images/courses/java-basic.jpg', '/uploads/videos/courses/java-basic.mp4', '技术主管', 1200, 1, 0.00, 50, 12, 234, 4.5, 8, 1, 1, 1),
(2, 1, 5, 'SPRING_001', 'Spring Boot实战', '通过实际项目案例，全面学习Spring Boot框架的核心技术，掌握企业级应用开发技能', '李老师', 2, 'Spring Boot框架实战开发', '/uploads/images/courses/spring-boot.jpg', '/uploads/videos/courses/spring-boot.mp4', '开发工程师', 1800, 2, 299.00, 30, 8, 156, 4.8, 5, 1, 0, 1),
(3, 1, 2, 'VUE_001', 'Vue.js前端开发', '从Vue.js基础到高级特性，系统学习现代前端开发技术，包括组件化、路由、状态管理等', '王老师', 3, 'Vue.js框架前端开发技术', '/uploads/images/courses/vue-js.jpg', '/uploads/videos/courses/vue-js.mp4', '前端工程师', 1500, 2, 199.00, 40, 15, 189, 4.3, 12, 1, 0, 0),
(4, 1, 3, 'MYSQL_001', 'MySQL数据库管理', '全面讲解MySQL数据库的设计、优化和管理，包括索引设计、查询优化、备份恢复等核心技术', '赵老师', 4, 'MySQL数据库设计和管理', '/uploads/images/courses/mysql.jpg', '/uploads/videos/courses/mysql.mp4', '数据库工程师', 900, 1, 0.00, 60, 25, 298, 4.6, 18, 1, 1, 1),
(5, 2, 6, 'TEST_001', '软件测试基础', '系统学习软件测试的基本理论和方法，包括测试计划、测试用例设计、缺陷管理等内容', '陈老师', 5, '软件测试理论和实践', '/uploads/images/courses/test-basic.jpg', '/uploads/videos/courses/test-basic.mp4', '测试工程师', 600, 1, 0.00, 20, 5, 67, 4.2, 3, 1, 1, 0);

-- =============================================
-- 11. 学习记录初始化数据
-- =============================================
INSERT INTO `learning_record` (`id`, `user_id`, `course_id`, `progress`, `study_duration`, `status`, `last_chapter_id`, `score`, `create_time`, `update_time`, `tenant_id`) VALUES
(1, 3, 1, 85, 600, 1, 3, 92.5, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 5 DAY, 1),
(2, 4, 2, 60, 450, 1, 6, 85.0, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 3 DAY, 1),
(3, 5, 3, 100, 900, 2, 10, 98.0, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 2 DAY, 1),
(4, 2, 4, 30, 250, 1, 11, NULL, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 1 DAY, 1),
(5, 7, 5, 75, 500, 1, 14, 78.5, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 2 DAY, 2);

-- =============================================
-- 12. 系统配置初始化数据
-- =============================================
INSERT INTO `sys_config` (`tenant_id`, `config_key`, `config_value`, `config_type`, `description`, `is_system`) VALUES
(NULL, 'system.name', '测盟汇管理系统', 'string', '系统名称', 1),
(NULL, 'system.version', '2.0.0', 'string', '系统版本', 1),
(NULL, 'system.copyright', '© 2024 测盟汇技术团队', 'string', '版权信息', 1),
(NULL, 'upload.max.size', '100', 'number', '文件上传最大大小(MB)', 1),
(NULL, 'password.min.length', '6', 'number', '密码最小长度', 1),
(1, 'company.logo', '/uploads/logo/demo-logo.png', 'string', '企业Logo', 0),
(1, 'company.address', '北京市朝阳区xxx大厦', 'string', '企业地址', 0),
(1, 'meeting.auto.approval', 'false', 'boolean', '会议自动审批', 0),
(2, 'company.logo', '/uploads/logo/test-logo.png', 'string', '企业Logo', 0),
(2, 'company.address', '上海市浦东新区xxx中心', 'string', '企业地址', 0);

-- =============================================
-- 更新部门负责人（在用户数据插入后）
-- =============================================
UPDATE `sys_dept` SET `leader_id` = 2 WHERE `id` = 2; -- 技术部负责人
UPDATE `sys_dept` SET `leader_id` = 4 WHERE `id` = 3; -- 市场部负责人
UPDATE `sys_dept` SET `leader_id` = 5 WHERE `id` = 4; -- 人事部负责人
UPDATE `sys_dept` SET `leader_id` = 3 WHERE `id` = 5; -- 开发组负责人
UPDATE `sys_dept` SET `leader_id` = 6 WHERE `id` = 8; -- 测试企业管理部负责人

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 数据统计查询（用于验证数据完整性）
-- =============================================
SELECT 'sys_tenant' as table_name, COUNT(*) as count FROM sys_tenant
UNION ALL
SELECT 'sys_dept', COUNT(*) FROM sys_dept
UNION ALL
SELECT 'sys_role', COUNT(*) FROM sys_role
UNION ALL
SELECT 'sys_user', COUNT(*) FROM sys_user
UNION ALL
SELECT 'sys_user_role', COUNT(*) FROM sys_user_role
UNION ALL
SELECT 'meeting', COUNT(*) FROM meeting
UNION ALL
SELECT 'meeting_registration', COUNT(*) FROM meeting_registration
UNION ALL
SELECT 'news', COUNT(*) FROM news
UNION ALL
SELECT 'news_category', COUNT(*) FROM news_category
UNION ALL
SELECT 'course', COUNT(*) FROM course
UNION ALL
SELECT 'learning_record', COUNT(*) FROM learning_record
UNION ALL
SELECT 'sys_config', COUNT(*) FROM sys_config;

-- =============================================
-- 课程分类初始数据
-- =============================================
INSERT INTO `course_category` (`id`, `tenant_id`, `parent_id`, `category_name`, `category_code`, `description`, `sort_order`, `status`, `create_time`, `create_by`) VALUES
(1, 1, 0, 'Java开发', 'JAVA_DEV', 'Java相关开发课程', 1, 1, NOW(), 1),
(2, 1, 0, '前端开发', 'FRONT_DEV', '前端开发相关课程', 2, 1, NOW(), 1),
(3, 1, 0, '数据库', 'DATABASE', '数据库相关课程', 3, 1, NOW(), 1),
(4, 1, 1, 'Java基础', 'JAVA_BASIC', 'Java基础开发课程', 1, 1, NOW(), 1),
(5, 1, 1, 'Java高级', 'JAVA_ADV', 'Java高级开发课程', 2, 1, NOW(), 1),
(6, 2, 0, '测试技术', 'TEST_TECH', '测试相关技术课程', 1, 1, NOW(), 6);

-- =============================================
-- 课程章节初始数据
-- =============================================
INSERT INTO `course_chapter` (`id`, `course_id`, `chapter_name`, `chapter_order`, `description`, `video_url`, `duration`, `status`, `create_time`, `create_by`, `tenant_id`) VALUES
(1, 1, 'Java基础语法', 1, '学习Java的基本语法，包括变量、数据类型、运算符等', '/uploads/videos/chapters/java-001-01.mp4', 3600, 1, NOW(), 1, 1),
(2, 1, '面向对象编程', 2, '深入理解面向对象的概念，包括类、对象、继承、多态等', '/uploads/videos/chapters/java-001-02.mp4', 5400, 1, NOW(), 1, 1),
(3, 1, '异常处理', 3, '学习Java的异常处理机制，包括try-catch、throws等', '/uploads/videos/chapters/java-001-03.mp4', 3000, 1, NOW(), 1, 1),
(4, 1, '集合框架', 4, '掌握Java集合框架，包括List、Set、Map等常用集合', '/uploads/videos/chapters/java-001-04.mp4', 4800, 1, NOW(), 1, 1),
(5, 2, 'Spring Boot入门', 1, '快速入门Spring Boot，搭建第一个Spring Boot应用', '/uploads/videos/chapters/spring-001-01.mp4', 4200, 1, NOW(), 2, 1),
(6, 2, 'RESTful API设计', 2, '学习RESTful API的设计原则和最佳实践', '/uploads/videos/chapters/spring-001-02.mp4', 5400, 1, NOW(), 2, 1),
(7, 2, '数据访问层开发', 3, '使用Spring Data JPA进行数据库操作', '/uploads/videos/chapters/spring-001-03.mp4', 6000, 1, NOW(), 2, 1),
(8, 3, 'Vue.js基础', 1, '学习Vue.js的基本概念和使用方法', '/uploads/videos/chapters/vue-001-01.mp4', 3600, 1, NOW(), 3, 1),
(9, 3, '组件化开发', 2, '深入理解Vue组件，学习组件通信和生命周期', '/uploads/videos/chapters/vue-001-02.mp4', 4800, 1, NOW(), 3, 1),
(10, 3, '路由和状态管理', 3, '学习Vue Router和Vuex状态管理', '/uploads/videos/chapters/vue-001-03.mp4', 5400, 1, NOW(), 3, 1),
(11, 4, 'MySQL基础', 1, '学习MySQL的基本操作和SQL语法', '/uploads/videos/chapters/mysql-001-01.mp4', 3000, 1, NOW(), 1, 1),
(12, 4, '索引优化', 2, '深入理解MySQL索引原理和优化技巧', '/uploads/videos/chapters/mysql-001-02.mp4', 3600, 1, NOW(), 1, 1),
(13, 4, '查询优化', 3, '学习SQL查询优化的方法和技巧', '/uploads/videos/chapters/mysql-001-03.mp4', 4200, 1, NOW(), 1, 1),
(14, 5, '测试基础', 1, '了解软件测试的基本概念和方法', '/uploads/videos/chapters/test-001-01.mp4', 2400, 1, NOW(), 6, 2),
(15, 5, '自动化测试框架', 2, '学习主流的自动化测试框架和工具', '/uploads/videos/chapters/test-001-02.mp4', 3600, 1, NOW(), 6, 2);

-- =============================================
-- 数据统计查询（用于验证数据完整性）
-- =============================================
SELECT 'sys_tenant' as table_name, COUNT(*) as count FROM sys_tenant
UNION ALL
SELECT 'sys_dept', COUNT(*) FROM sys_dept
UNION ALL
SELECT 'sys_role', COUNT(*) FROM sys_role
UNION ALL
SELECT 'sys_user', COUNT(*) FROM sys_user
UNION ALL
SELECT 'sys_user_role', COUNT(*) FROM sys_user_role
UNION ALL
SELECT 'meeting', COUNT(*) FROM meeting
UNION ALL
SELECT 'meeting_registration', COUNT(*) FROM meeting_registration
UNION ALL
SELECT 'news', COUNT(*) FROM news
UNION ALL
SELECT 'news_category', COUNT(*) FROM news_category
UNION ALL
SELECT 'course', COUNT(*) FROM course
UNION ALL
SELECT 'course_category', COUNT(*) FROM course_category
UNION ALL
SELECT 'course_chapter', COUNT(*) FROM course_chapter
UNION ALL
SELECT 'learning_record', COUNT(*) FROM learning_record
UNION ALL
SELECT 'sys_config', COUNT(*) FROM sys_config;

