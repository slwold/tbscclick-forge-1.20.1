@echo off
echo 推送代码到GitHub
echo ================

echo 检查Git状态...
git status >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未找到Git仓库。请先运行init_git.bat初始化仓库。
    pause
    exit /b 1
)

echo 添加所有更改...
git add .

echo 提交更改...
set /p commit_message=请输入提交信息（直接回车使用默认信息）: 
if "%commit_message%"=="" set commit_message=Update TbscClick mod

git commit -m "%commit_message%"

echo 推送到GitHub...
echo.
echo 请选择要推送的分支：
echo 1. master 主分支
echo 2. forge-1.20.1 分支
echo.

choice /c 12 /m "请选择分支"
if errorlevel 2 (
    echo 切换到 forge-1.20.1 分支...
    git checkout forge-1.20.1
    echo 推送到 forge-1.20.1 分支...
    git push -u origin forge-1.20.1
) else (
    echo 推送到 master 分支...
    git push -u origin master
)

echo.
echo 推送完成！
pause