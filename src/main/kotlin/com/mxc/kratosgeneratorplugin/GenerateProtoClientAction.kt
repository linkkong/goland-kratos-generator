package com.mxc.kratosgeneratorplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.actionSystem.ActionUpdateThread

/**
 * 生成 Proto 客户端代码 Action
 * 菜单出现位置：在资源管理器中，右键任意非 conf.proto 的 .proto 文件时出现。
 * 功能描述：在项目根目录下执行 kratos proto client <相对路径>，自动生成该 proto 文件对应的客户端代码。
 */
class GenerateProtoClientAction : AnAction("生成 Proto 客户端代码") {
    
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
    
    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = e.project ?: return
        val projectRoot = project.basePath ?: return
        
        // 获取相对于项目根目录的相对路径
        val projectDir = project.guessProjectDir() ?: return
        val relativePath = VfsUtilCore.getRelativePath(file, projectDir) ?: return
        
        // 构造命令
        val command = "kratos proto client $relativePath"
        
        // 执行命令并展示输出
        ShellCommandRunner.runInConsole(project, projectRoot, command, "生成 Proto 客户端代码")
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