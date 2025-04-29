package com.mxc.kratosgeneratorplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VfsUtilCore

/**
 * 生成 Service 服务端代码 Action
 * 菜单出现位置：在资源管理器中，右键任意非 conf.proto 的 .proto 文件时出现。
 * 功能描述：在项目根目录下执行 kratos proto server <相对路径> -t internal/service，自动生成该 proto 文件对应的服务端代码。
 */
class GenerateServiceAction : AnAction("生成 Service 服务端代码") {
    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = e.project ?: return
        val projectRoot = project.basePath ?: return
        
        // 获取相对于项目根目录的相对路径
        val relativePath = VfsUtilCore.getRelativePath(file, project.baseDir) ?: return
        
        // 构造命令
        val command = "kratos proto server $relativePath -t internal/service"
        
        // 执行命令并展示输出
        ShellCommandRunner.runInConsole(project, projectRoot, command, "生成 Service 服务端代码")
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val visible = file != null && 
                !file.isDirectory && 
                file.name.endsWith(".proto") && 
                file.name != "conf.proto"
                
        e.presentation.isEnabledAndVisible = visible
    }
} 