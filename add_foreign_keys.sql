-- =============================================
-- 测盟汇管理系统外键约束添加脚本
-- 版本: v2.1.0
-- 创建时间: 2024-06-16
-- 描述: 在数据插入完成后添加外键约束
-- =============================================

USE `cemh_system`;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 1. 部门表外键约束
-- =============================================
-- 部门表自引用外键（父部门）
ALTER TABLE `sys_dept` 
ADD CONSTRAINT `fk_dept_parent` 
FOREIGN KEY (`parent_id`) REFERENCES `sys_dept` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- 部门表租户外键
ALTER TABLE `sys_dept` 
ADD CONSTRAINT `fk_dept_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- 部门负责人外键（可选，因为可能存在循环依赖）
-- ALTER TABLE `sys_dept` 
-- ADD CONSTRAINT `fk_dept_leader` 
-- FOREIGN KEY (`leader_id`) REFERENCES `sys_user` (`id`) 
-- ON DELETE SET NULL ON UPDATE CASCADE;

-- =============================================
-- 2. 角色表外键约束
-- =============================================
ALTER TABLE `sys_role` 
ADD CONSTRAINT `fk_role_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 3. 用户表外键约束
-- =============================================
ALTER TABLE `sys_user` 
ADD CONSTRAINT `fk_user_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `sys_user` 
ADD CONSTRAINT `fk_user_dept` 
FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- =============================================
-- 4. 用户角色关联表外键约束
-- =============================================
ALTER TABLE `sys_user_role` 
ADD CONSTRAINT `fk_user_role_user` 
FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `sys_user_role` 
ADD CONSTRAINT `fk_user_role_role` 
FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 5. 会议表外键约束
-- =============================================
ALTER TABLE `meeting` 
ADD CONSTRAINT `fk_meeting_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `meeting` 
ADD CONSTRAINT `fk_meeting_dept` 
FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `meeting` 
ADD CONSTRAINT `fk_meeting_creator` 
FOREIGN KEY (`creator_id`) REFERENCES `sys_user` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 6. 会议报名表外键约束
-- =============================================
ALTER TABLE `meeting_registration` 
ADD CONSTRAINT `fk_registration_meeting` 
FOREIGN KEY (`meeting_id`) REFERENCES `meeting` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `meeting_registration` 
ADD CONSTRAINT `fk_registration_user` 
FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `meeting_registration` 
ADD CONSTRAINT `fk_registration_approval_by` 
FOREIGN KEY (`approval_by`) REFERENCES `sys_user` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- =============================================
-- 7. 新闻表外键约束
-- =============================================
ALTER TABLE `news` 
ADD CONSTRAINT `fk_news_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `news` 
ADD CONSTRAINT `fk_news_category` 
FOREIGN KEY (`category_id`) REFERENCES `news_category` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- =============================================
-- 8. 新闻分类表外键约束
-- =============================================
ALTER TABLE `news_category` 
ADD CONSTRAINT `fk_news_category_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- 新闻分类自引用外键（父分类）
ALTER TABLE `news_category` 
ADD CONSTRAINT `fk_news_category_parent` 
FOREIGN KEY (`parent_id`) REFERENCES `news_category` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 9. 课程表外键约束
-- =============================================
ALTER TABLE `course` 
ADD CONSTRAINT `fk_course_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `course` 
ADD CONSTRAINT `fk_course_category` 
FOREIGN KEY (`category_id`) REFERENCES `course_category` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- =============================================
-- 10. 学习记录表外键约束
-- =============================================
ALTER TABLE `learning_record` 
ADD CONSTRAINT `fk_learning_user` 
FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `learning_record` 
ADD CONSTRAINT `fk_learning_course` 
FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 11. 系统配置表外键约束
-- =============================================
ALTER TABLE `sys_config` 
ADD CONSTRAINT `fk_config_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 12. 课程分类表外键约束
-- =============================================
ALTER TABLE `course_category` 
ADD CONSTRAINT `fk_course_category_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- 课程分类自引用外键（父分类）
ALTER TABLE `course_category` 
ADD CONSTRAINT `fk_course_category_parent` 
FOREIGN KEY (`parent_id`) REFERENCES `course_category` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 13. 课程章节表外键约束
-- =============================================
ALTER TABLE `course_chapter` 
ADD CONSTRAINT `fk_chapter_course` 
FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- 课程章节表租户外键
ALTER TABLE `course_chapter` 
ADD CONSTRAINT `fk_chapter_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 14. 学习记录表额外外键约束
-- =============================================
-- 学习记录表最后学习章节外键
ALTER TABLE `learning_record` 
ADD CONSTRAINT `fk_learning_chapter` 
FOREIGN KEY (`last_chapter_id`) REFERENCES `course_chapter` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

-- 学习记录表租户外键
ALTER TABLE `learning_record` 
ADD CONSTRAINT `fk_learning_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 验证外键约束
-- =============================================
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE 
    REFERENCED_TABLE_SCHEMA = 'cemh_system' 
    AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY 
    TABLE_NAME, CONSTRAINT_NAME;

