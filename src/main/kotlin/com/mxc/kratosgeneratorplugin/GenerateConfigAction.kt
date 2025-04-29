package com.mxc.kratosgeneratorplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * 生成配置文件 Action
 * 菜单出现位置：在资源管理器中，右键 conf.proto 文件时出现。
 * 功能描述：在项目根目录下执行 make config，自动生成或更新配置文件。
 */
class GenerateConfigAction : AnAction("生成配置文件") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val projectRoot = project.basePath ?: return
        
        // 构造命令
        val command = "make config"
        
        // 执行命令并展示输出
        ShellCommandRunner.runInConsole(project, projectRoot, command, "生成配置文件")
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val visible = file != null && 
                !file.isDirectory && 
                file.name == "conf.proto"
                
        e.presentation.isEnabledAndVisible = visible
    }
} 