# TbscClick Mod for Minecraft 1.20.1

## 模组介绍

TbscClick是一个自动化点击模组，为Minecraft玩家提供自动左键和右键点击功能。该模组适用于Minecraft 1.20.1版本，使用Forge 47.4.10开发。

## 功能特性

- 自动左键攻击/挖掘
- 自动右键使用/放置
- 可调节的点击速度
- 智能攻击模式（根据攻击冷却时间自动点击）
- 按键绑定自定义
- 按住右键模式

## 安装说明

1. 确保您已安装Minecraft 1.20.1
2. 下载并安装Forge 47.4.10
3. 将编译好的模组jar文件放入`.minecraft/mods`文件夹中
4. 启动游戏

## 编译说明

### 自动编译
使用Gradle进行编译：
```bash
.\gradlew build
```

### 手动编译
1. 下载Gradle 8.8并解压到项目目录下的`gradle-8.8`文件夹
2. 运行以下命令：
```bash
gradle build
```

## 按键绑定

- `G` - 切换右键自动点击
- `H` - 切换左键自动点击
- `V` - 切换智能攻击模式
- `B` - 切换按住右键模式
- `N` - 调整点击速度
- `'` - 切换潜行模式

## 配置选项

模组支持通过配置文件进行自定义设置：
- 点击间隔时间
- 最大/最小点击间隔
- 默认按键绑定

## 技术细节

### 兼容性修复
本版本针对Minecraft 1.20.1和Forge 47.4.10进行了适配，修复了以下问题：
1. 移除了对不存在的`ClientHooks`类的引用
2. 修复了`attackIndicator()`方法不存在的问题
3. 移除了对`IKeyConflictContext.DEFAULT`变量的引用
4. 修复了`MultiPlayerGameMode.attack()`方法参数不匹配的问题
5. 修复了过时的`ResourceLocation`构造函数调用
6. 修复了GUI绘制相关的方法调用

### 访问转换器
模组使用访问转换器来访问Minecraft的私有字段和方法：
- `leftClickCounter` 字段
- `rightClickDelayTimer` 字段
- `leftClick()` 方法
- `rightClick()` 方法

## 开发者信息

该模组基于TbscClick项目开发，适配了最新的Minecraft 1.20.1版本。

## 注意事项

- 该模组仅供单人游戏使用
- 在多人服务器上使用可能会违反服务器规则
- 使用该模组时请遵守Minecraft的使用条款