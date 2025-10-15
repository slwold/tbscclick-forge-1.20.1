@echo off
echo TbscClick Mod Build Script
echo =========================

echo 正在检查Gradle...
if exist gradle-8.8 (
    echo 使用本地Gradle 8.8
    gradle-8.8\bin\gradle build
) else (
    echo 使用gradlew脚本构建
    .\gradlew.bat build
)

echo.
echo 构建完成！如果成功，模组文件将在 build\libs\ 目录中。
pause