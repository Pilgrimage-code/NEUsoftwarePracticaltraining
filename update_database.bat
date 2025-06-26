@echo off
echo [[ 测盟汇平台数据库更新脚本 ]]
echo.

set MYSQL_PATH=mysql
set DB_HOST=localhost
set DB_PORT=3306
set DB_USER=root
set DB_PASS=root
set DB_NAME=cemh_db

echo 请确保您已安装MySQL并设置好环境变量
echo 当前配置:
echo 数据库地址: %DB_HOST%:%DB_PORT%
echo 数据库名称: %DB_NAME%
echo 数据库用户: %DB_USER%
echo.

set /p CONFIRM=是否更新数据库结构？此操作不会删除现有数据 (Y/N)
if /i "%CONFIRM%" NEQ "Y" goto :end

echo 正在更新课程字段...
%MYSQL_PATH% -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% %DB_NAME% < update_course_fields.sql
if %ERRORLEVEL% NEQ 0 goto :error

echo 正在更新学习记录表...
%MYSQL_PATH% -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% %DB_NAME% < update_learning_tables.sql
if %ERRORLEVEL% NEQ 0 goto :error

echo 数据库更新完成！
goto :end

:error
echo 出现错误，请检查错误信息。
goto :end

:end
pause 