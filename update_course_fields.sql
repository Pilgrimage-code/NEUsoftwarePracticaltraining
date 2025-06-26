-- =============================================
-- 课程管理系统字段更新脚本
-- 版本: v1.0.0
-- 创建时间: 2025-06-25
-- 描述: 为满足target.md中的需求，添加课程相关字段
-- =============================================

USE `cemh_system`;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 1. 更新课程表结构
-- =============================================
-- 检查并添加course_intro字段
ALTER TABLE `course` 
ADD COLUMN IF NOT EXISTS `course_intro` text COMMENT '课程简介' AFTER `course_name`;

-- 检查并添加course_author字段
ALTER TABLE `course` 
ADD COLUMN IF NOT EXISTS `course_author` varchar(100) DEFAULT NULL COMMENT '课程作者' AFTER `course_intro`;

-- 检查并添加course_order字段
ALTER TABLE `course` 
ADD COLUMN IF NOT EXISTS `course_order` int DEFAULT '0' COMMENT '课程排序' AFTER `course_author`;

-- 检查并添加video_url字段
ALTER TABLE `course` 
ADD COLUMN IF NOT EXISTS `video_url` varchar(500) DEFAULT NULL COMMENT '课程视频URL' AFTER `cover_image`;

-- 添加course_order字段的索引
ALTER TABLE `course` 
ADD INDEX IF NOT EXISTS `idx_course_order` (`course_order`);

-- =============================================
-- 2. 更新现有数据（如果需要）
-- =============================================
-- 将description字段的内容复制到course_intro（如果course_intro为空）
UPDATE `course` 
SET `course_intro` = `description` 
WHERE `course_intro` IS NULL OR `course_intro` = '';

-- 将instructor字段的内容复制到course_author（如果course_author为空）
UPDATE `course` 
SET `course_author` = `instructor` 
WHERE `course_author` IS NULL OR `course_author` = '';

-- 设置默认的课程排序值
UPDATE `course` 
SET `course_order` = `id` 
WHERE `course_order` IS NULL OR `course_order` = 0;

-- =============================================
-- 3. 添加外键约束（如果不存在）
-- =============================================
-- 检查并添加course表与course_category表的外键关系
SET @constraint_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE CONSTRAINT_SCHEMA = 'cemh_system' 
    AND TABLE_NAME = 'course' 
    AND CONSTRAINT_NAME = 'fk_course_category'
);

SET @sql = IF(@constraint_exists = 0,
    'ALTER TABLE `course` ADD CONSTRAINT `fk_course_category` FOREIGN KEY (`category_id`) REFERENCES `course_category` (`id`) ON DELETE SET NULL ON UPDATE CASCADE',
    'SELECT "Foreign key fk_course_category already exists"'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- =============================================
-- 4. 验证更新结果
-- =============================================
-- 显示更新后的表结构
DESCRIBE `course`;

-- 显示外键关系
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE 
    TABLE_SCHEMA = 'cemh_system' 
    AND TABLE_NAME = 'course'
    AND REFERENCED_TABLE_NAME IS NOT NULL;

-- 显示更新后的数据示例
SELECT 
    id,
    course_name,
    course_intro,
    course_author,
    course_order,
    video_url
FROM 
    `course` 
LIMIT 5;

-- 检查course_chapter表是否有tenant_id列, 如果没有则添加
ALTER TABLE course_chapter 
ADD COLUMN IF NOT EXISTS tenant_id BIGINT COMMENT '租户ID';

-- 检查learning_record表是否有last_chapter_id列, 如果没有则添加
ALTER TABLE learning_record 
ADD COLUMN IF NOT EXISTS last_chapter_id BIGINT COMMENT '最后学习章节ID';

-- 检查learning_record表是否有tenant_id列, 如果没有则添加
ALTER TABLE learning_record 
ADD COLUMN IF NOT EXISTS tenant_id BIGINT COMMENT '租户ID'; 