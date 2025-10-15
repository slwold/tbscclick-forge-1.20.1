@echo off
echo 初始化Git仓库
echo ===============

echo 检查是否已安装Git...
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未找到Git。请先安装Git后再运行此脚本。
    pause
    exit /b 1
)

echo 初始化Git仓库...
git init

echo 配置Git用户信息（如果尚未配置）...
git config user.name "slwold"
git config user.email "sl571ld@outlook.com"

echo 添加所有文件到Git...
git add .

echo 创建初始提交...
git commit -m "Initial commit: TbscClick mod for Minecraft 1.20.1 with Forge 47.4.10"

echo 添加远程仓库...
git remote add origin https://github.com/slwold/tbscclick-forge-1.20.1.git

echo.
echo Git仓库初始化完成！
echo.
echo 要推送到GitHub，请运行以下命令：
echo git push -u origin master
echo.
echo 如果要推送到forge-1.20.1分支，请先创建分支：
echo git checkout -b forge-1.20.1
echo 然后推送：
echo git push -u origin forge-1.20.1
echo.
pause