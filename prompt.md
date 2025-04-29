VSCode 插件右键菜单项及功能说明
1. 生成 Proto 客户端代码
菜单出现位置：在资源管理器中，右键任意非 conf.proto 的 .proto 文件时出现。
菜单名称：生成 Proto 客户端代码（实际命令为 kratos-proto-generator.generateProto）
功能描述：在项目根目录下执行 kratos proto client <相对路径>，自动生成该 proto 文件对应的客户端代码。
典型场景：你在 api/xxx.proto 文件上右键，选择此菜单，自动生成 client 代码。
2. 生成 Service 服务端代码
菜单出现位置：在资源管理器中，右键任意非 conf.proto 的 .proto 文件时出现。
菜单名称：生成 Service 服务端代码（实际命令为 kratos-proto-generator.generateService）
功能描述：在项目根目录下执行 kratos proto server <相对路径> -t internal/service，自动生成该 proto 文件对应的服务端代码。
典型场景：你在 api/xxx.proto 文件上右键，选择此菜单，自动生成 service 代码。
3. 生成配置文件
菜单出现位置：在资源管理器中，右键 conf.proto 文件时出现。
菜单名称：生成配置文件（实际命令为 kratos-proto-generator.generateConfig）
功能描述：在项目根目录下执行 make config，自动生成或更新配置文件。
典型场景：你在 api/conf.proto 文件上右键，选择此菜单，自动生成 config 相关代码或文件。
4. 执行 wire 依赖注入
菜单出现位置：在资源管理器中，右键 cmd/xxx 目录（即 cmd 下的任意一级子目录）时出现。
菜单名称：执行 wire（实际命令为 kratos-proto-generator.runWire）
功能描述：在当前右键点击的 cmd/xxx 目录下执行 wire .，自动生成依赖注入代码。
典型场景：你在 cmd/xxx 目录上右键，选择此菜单，自动在该目录下执行 wire。
菜单项与出现条件总结
| 菜单名称 | 触发对象 | 触发条件表达式（when） | 主要功能描述 |
|--------------------------|-------------------------|--------------------------------------------------------|----------------------------------------------|
| 生成 Proto 客户端代码 | 非 conf.proto 的 .proto | resourceFilename =~ /\\.proto$/ && !conf.proto | 生成 proto 客户端代码 |
| 生成 Service 服务端代码 | 非 conf.proto 的 .proto | resourceFilename =~ /\\.proto$/ && !conf.proto | 生成 proto 服务端代码 |
| 生成配置文件 | conf.proto 文件 | resourceFilename =~ /conf\\.proto$/ | 生成/更新配置文件 |
| 执行 wire | cmd/xxx 目录 | explorerResourceIsFolder && resourcePath =~ /cmd\\/[^/]+$/ | 在该目录下执行 wire 依赖注入生成 |
GoLand 插件实现建议
你可以在 GoLand 的 Project View（项目树）右键菜单中，按上述规则添加对应菜单项。每个菜单项的触发条件和功能与 VSCode 保持一致：
判断文件/目录类型和路径，决定菜单是否显示。
获取当前选中的文件或目录路径，作为命令的参数。
在指定目录下执行 shell 命令，并将输出展示给用户（可用 ToolWindow、弹窗、或 ConsoleView）。