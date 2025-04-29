package com.mxc.kratosgeneratorplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

/**
 * 执行 wire 依赖注入 Action
 * 菜单出现位置：在资源管理器中，右键 cmd/xxx 目录（即 cmd 下的任意一级子目录）时出现。
 * 功能描述：在当前右键点击的 cmd/xxx 目录下执行 wire .，自动生成依赖注入代码。
 */
class RunWireAction : AnAction("执行 wire") {
    override fun actionPerformed(e: AnActionEvent) {
        val dir = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = e.project ?: return
        
        // 构造命令并在选中的目录中执行
        val command = "wire ."
        
        // 执行命令并展示输出，工作目录是当前选中的目录
        ShellCommandRunner.runInConsole(project, dir.path, command, "执行 wire 依赖注入")
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        // 检查是否为 cmd 下的一级子目录
        val visible = file != null && 
                file.isDirectory && 
                file.path.matches(Regex(".*/cmd/[^/]+$"))
                
        e.presentation.isEnabledAndVisible = visible
    }
} 