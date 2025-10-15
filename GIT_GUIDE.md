# Git推送指南

本指南将帮助您将TbscClick模组代码推送到GitHub仓库。

## 前提条件

1. 确保您已安装Git
2. 确保您有GitHub账户
3. 确保您有目标仓库的访问权限

## 使用提供的脚本（推荐）

项目中包含了两个Windows批处理脚本，可以帮助您轻松完成Git操作：

1. `init_git.bat` - 初始化Git仓库并进行首次提交
2. `push_to_github.bat` - 添加更改、提交并推送到GitHub

### 初始化仓库

双击运行 `init_git.bat` 脚本，它将：
- 初始化Git仓库
- 配置用户信息
- 添加所有文件并创建初始提交
- 添加远程仓库地址

### 推送代码

双击运行 `push_to_github.bat` 脚本，它将：
- 添加所有更改到暂存区
- 提示您输入提交信息
- 提交更改
- 推送到GitHub（可选择分支）

## 手动操作步骤

如果您更喜欢手动操作，可以按照以下步骤进行：

### 初始化仓库（如果尚未初始化）

```bash
cd D:\Users\slwold\Downloads\TbscClick-1.21\TbscClick-1.20.1
git init
```

### 配置用户信息

```bash
git config user.name "您的用户名"
git config user.email "您的邮箱"
```

### 添加所有文件到Git

```bash
git add .
```

### 创建初始提交

```bash
git commit -m "Initial commit: TbscClick mod for Minecraft 1.20.1"
```

### 添加远程仓库

如果您还没有添加远程仓库：

```bash
git remote add origin https://github.com/slwold/tbscclick-forge-1.20.1.git
```

### 推送到GitHub

#### 如果分支不存在，先创建并推送：

```bash
git push -u origin forge-1.20.1
```

#### 如果分支已存在：

```bash
git push origin forge-1.20.1
```

## 后续推送

之后的推送只需使用：

```bash
git push
```

## 常用Git命令

### 查看状态
```bash
git status
```

### 查看提交历史
```bash
git log --oneline
```

### 创建新分支
```bash
git checkout -b forge-1.20.1
```

### 切换分支
```bash
git checkout forge-1.20.1
```

### 拉取最新更改
```bash
git pull origin forge-1.20.1
```

## 注意事项

1. 确保您的GitHub账户有权限推送到目标仓库
2. 如果是私有仓库，可能需要配置SSH密钥或使用个人访问令牌
3. 如果遇到权限问题，请检查您的GitHub账户设置
4. 首次推送时可能需要输入GitHub用户名和密码（或个人访问令牌）