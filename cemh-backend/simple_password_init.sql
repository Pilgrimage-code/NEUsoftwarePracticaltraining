-- 简化密码验证 - 数据库初始化脚本
-- 将所有用户密码设置为明文或简单MD5加密

USE cemh_system;

-- 方案1：明文密码（最简单，推荐开发使用）
UPDATE sys_user SET 
    password = '123456',
    update_time = NOW()
WHERE deleted = 0;

-- 如果您希望使用MD5加密，请注释掉上面的语句，使用下面的语句：
-- UPDATE sys_user SET 
--     password = 'e10adc3949ba59abbe56e057f20f883e',  -- 123456的MD5值
--     update_time = NOW()
-- WHERE deleted = 0;

-- 验证更新结果
SELECT id, username, password, update_time 
FROM sys_user 
WHERE deleted = 0;

