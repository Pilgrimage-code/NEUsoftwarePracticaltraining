-- =============================================
-- 学习记录表字段更新脚本
-- 版本: v1.0.0
-- 创建时间: 2025-06-25
-- 描述: 为学习记录表添加必要的字段和索引
-- =============================================

USE `cemh_system`;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- =============================================
-- 1. 更新课程章节表结构
-- =============================================
-- 添加tenant_id字段到course_chapter表
ALTER TABLE `course_chapter` 
ADD COLUMN IF NOT EXISTS `tenant_id` bigint DEFAULT NULL COMMENT '租户ID' AFTER `deleted`;

-- 为tenant_id字段添加索引
ALTER TABLE `course_chapter` 
ADD INDEX `idx_tenant_id` (`tenant_id`);

-- 添加外键约束
ALTER TABLE `course_chapter` 
ADD CONSTRAINT `fk_chapter_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 2. 更新学习记录表结构
-- =============================================
-- 添加last_chapter_id字段(最后学习章节ID)
ALTER TABLE `learning_record` 
ADD COLUMN IF NOT EXISTS `last_chapter_id` bigint DEFAULT NULL COMMENT '最后学习章节ID' AFTER `status`;

-- 添加tenant_id字段
ALTER TABLE `learning_record` 
ADD COLUMN IF NOT EXISTS `tenant_id` bigint DEFAULT NULL COMMENT '租户ID' AFTER `deleted`;

-- 添加last_study_time字段(最后学习时间)
ALTER TABLE `learning_record` 
ADD COLUMN IF NOT EXISTS `last_study_time` datetime DEFAULT NULL COMMENT '最后学习时间' AFTER `last_chapter_id`;

-- 为new fields添加索引
ALTER TABLE `learning_record` 
ADD INDEX `idx_last_chapter_id` (`last_chapter_id`),
ADD INDEX `idx_tenant_id` (`tenant_id`);

-- 添加外键约束
ALTER TABLE `learning_record` 
ADD CONSTRAINT `fk_learning_chapter` 
FOREIGN KEY (`last_chapter_id`) REFERENCES `course_chapter` (`id`) 
ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE `learning_record` 
ADD CONSTRAINT `fk_learning_tenant` 
FOREIGN KEY (`tenant_id`) REFERENCES `sys_tenant` (`id`) 
ON DELETE CASCADE ON UPDATE CASCADE;

-- =============================================
-- 3. 更新视图(如果需要)
-- =============================================
-- 创建或替换用户学习统计视图
CREATE OR REPLACE VIEW `v_user_learning_stats` AS
SELECT 
    `user_id`,
    COUNT(*) AS `total_courses`,
    SUM(CASE WHEN `status` = 2 THEN 1 ELSE 0 END) AS `completed_courses`,
    SUM(CASE WHEN `status` = 1 THEN 1 ELSE 0 END) AS `in_progress_courses`,
    SUM(CASE WHEN `status` = 0 THEN 1 ELSE 0 END) AS `not_started_courses`,
    SUM(`study_duration`) AS `total_study_duration`,
    AVG(`progress`) AS `avg_progress`,
    MAX(`last_study_time`) AS `last_study_time`
FROM 
    `learning_record`
WHERE 
    `deleted` = 0
GROUP BY 
    `user_id`;

-- 创建或替换课程学习统计视图
CREATE OR REPLACE VIEW `v_course_learning_stats` AS
SELECT 
    `course_id`,
    COUNT(*) AS `total_students`,
    SUM(CASE WHEN `status` = 2 THEN 1 ELSE 0 END) AS `completed_students`,
    SUM(CASE WHEN `status` = 1 THEN 1 ELSE 0 END) AS `in_progress_students`,
    AVG(`progress`) AS `avg_progress`,
    AVG(`score`) AS `avg_score`
FROM 
    `learning_record`
WHERE 
    `deleted` = 0
GROUP BY 
    `course_id`;

-- =============================================
-- 4. 添加触发器(自动更新相关字段)
-- =============================================
-- 创建触发器：在学习记录更新时自动更新课程表的当前学员数
DELIMITER //
CREATE TRIGGER IF NOT EXISTS `trg_update_course_student_count` 
AFTER INSERT ON `learning_record`
FOR EACH ROW
BEGIN
    UPDATE `course` 
    SET `current_students` = (
        SELECT COUNT(*) 
        FROM `learning_record` 
        WHERE `course_id` = NEW.`course_id` AND `deleted` = 0
    )
    WHERE `id` = NEW.`course_id`;
END//
DELIMITER ;

-- =============================================
-- 5. 验证更新结果
-- =============================================
-- 显示更新后的表结构
SHOW COLUMNS FROM `learning_record`;

-- 显示添加的视图
SHOW CREATE VIEW `v_user_learning_stats`;
SHOW CREATE VIEW `v_course_learning_stats`;

-- 显示添加的触发器
SHOW TRIGGERS LIKE 'trg_update_course_student_count';

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1; 