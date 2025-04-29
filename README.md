# Kratos Generator Plugin

适用于 GoLand 的 Kratos 微服务框架代码生成器插件，为 [Kratos](https://github.com/go-kratos/kratos) 项目提供快捷的代码生成功能。

## 功能特性

插件在项目视图中提供以下右键菜单选项：

1. **生成 Proto 客户端代码**
   - 触发对象：非 conf.proto 的 .proto 文件
   - 功能：在项目根目录下执行 `kratos proto client <相对路径>`，自动生成该 proto 文件对应的客户端代码

2. **生成 Service 服务端代码**
   - 触发对象：非 conf.proto 的 .proto 文件
   - 功能：在项目根目录下执行 `kratos proto server <相对路径> -t internal/service`，自动生成该 proto 文件对应的服务端代码

3. **生成配置文件**
   - 触发对象：conf.proto 文件
   - 功能：在项目根目录下执行 `make config`，自动生成或更新配置文件

4. **执行 wire 依赖注入**
   - 触发对象：cmd/xxx 目录
   - 功能：在当前右键点击的 cmd/xxx 目录下执行 `wire .`，自动生成依赖注入代码

## 安装方法

### 从 IDE 插件市场安装（推荐）

即将上线

### 手动安装

1. 下载本仓库中的最新版本 `kratos-generator-plugin-*.zip`
2. 在 GoLand 中，打开 Settings → Plugins → ⚙️ → Install Plugin from Disk...
3. 选择下载的 zip 文件进行安装
4. 重启 GoLand

## 使用前提

确保已正确安装以下工具：

1. Kratos CLI 工具：
   ```bash
   go install github.com/go-kratos/kratos/cmd/kratos/v2@latest
   ```

2. Wire 工具：
   ```bash
   go install github.com/google/wire/cmd/wire@latest
   ```

## 使用方法

1. 在 GoLand 的项目视图中，右键点击相应的文件或目录
2. 在弹出的上下文菜单中选择对应的功能
3. 执行结果将在底部的 "Kratos Generator" 工具窗口中显示

## 开发构建

如需自行构建插件：

```bash
./gradlew buildPlugin
```

生成的插件文件位于 `build/distributions/` 目录下。

## 兼容性

兼容 GoLand 2022.2 及更高版本。

## 问题反馈

如果您在使用过程中遇到任何问题，请在 GitHub Issues 中提交。 