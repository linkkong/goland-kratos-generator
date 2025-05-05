package com.mxc.kratosgeneratorplugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.ToolWindowId
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.Font
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

object ShellCommandRunner {
    private const val TOOL_WINDOW_ID = "Kratos Generator"
    private const val CONTENT_DISPLAY_NAME = "输出"
    
    private var outputPane: JTextPane? = null
    private var toolWindowContent: Content? = null
    
    /**
     * 在指定目录执行 shell 命令，并将输出展示在控制台中
     * 
     * @param project 当前项目
     * @param workDir 工作目录
     * @param command 要执行的命令
     * @param title 命令标题（显示在输出中）
     */
    fun runInConsole(project: Project, workDir: String, command: String, title: String) {
        try {
            // 获取或创建工具窗口
            val toolWindowManager = ToolWindowManager.getInstance(project)
            var toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID)
            
            // 如果工具窗口不存在，尝试显示在 "Run" 工具窗口中
            if (toolWindow == null) {
                toolWindow = toolWindowManager.getToolWindow(ToolWindowId.RUN)
                if (toolWindow == null) {
                    Messages.showErrorDialog(project, "找不到工具窗口", "错误")
                    return
                }
            }
            
            // 获取或创建输出面板
            if (outputPane == null || toolWindowContent == null) {
                createToolWindowContent(toolWindow)
            }
            
            // 添加分隔线和命令信息
            appendToOutput("\n\n" + "=".repeat(80) + "\n", StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE))
            
            // 添加时间戳
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
            val timestampStyle = SimpleAttributeSet()
            StyleConstants.setForeground(timestampStyle, JBColor.GRAY)
            StyleConstants.setItalic(timestampStyle, true)
            appendToOutput("[$timestamp] ", timestampStyle)
            
            // 添加命令标题
            val titleStyle = SimpleAttributeSet()
            StyleConstants.setBold(titleStyle, true)
            StyleConstants.setForeground(titleStyle, JBColor.BLUE)
            appendToOutput("$title\n", titleStyle)
            
            // 添加命令信息
            val commandStyle = SimpleAttributeSet()
            StyleConstants.setForeground(commandStyle, JBColor.DARK_GRAY)
            appendToOutput("执行命令: $command\n在目录: $workDir\n\n", commandStyle)
            
            // 创建命令行
            val commandLine = GeneralCommandLine()
            commandLine.withExePath("/bin/sh")
            commandLine.withParameters("-c", command)
            commandLine.withWorkDirectory(workDir)
            
            // 创建进程处理器
            val processHandler = OSProcessHandler(commandLine)
            
            // 添加进程监听器
            processHandler.addProcessListener(object : ProcessAdapter() {
                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    val textStyle = SimpleAttributeSet()
                    // 根据输出类型设置颜色
                    if (outputType.toString().contains("stderr")) {
                        StyleConstants.setForeground(textStyle, JBColor.RED)
                    } else {
                        StyleConstants.setForeground(textStyle, JBColor.foreground())
                    }
                    appendToOutput(event.text, textStyle)
                }
                
                override fun processTerminated(event: ProcessEvent) {
                    val resultStyle = SimpleAttributeSet()
                    if (event.exitCode == 0) {
                        StyleConstants.setForeground(resultStyle, JBColor.GREEN)
                    } else {
                        StyleConstants.setForeground(resultStyle, JBColor.RED)
                    }
                    StyleConstants.setBold(resultStyle, true)
                    appendToOutput("\n命令执行完成，退出码: ${event.exitCode}\n", resultStyle)
                }
            })
            
            // 显示工具窗口
            toolWindow.show()
            
            // 启动进程
            processHandler.startNotify()
        } catch (e: Exception) {
            Messages.showErrorDialog(project, "命令执行失败: ${e.message}", title)
        }
    }
    
    /**
     * 创建工具窗口内容
     */
    private fun createToolWindowContent(toolWindow: com.intellij.openapi.wm.ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val panel = JPanel(BorderLayout())
        
        // 创建文本输出区域
        outputPane = JTextPane()
        outputPane?.isEditable = false
        outputPane?.font = Font("Monospaced", Font.PLAIN, 13)
        
        // 使用 JBScrollPane 替代普通的 JScrollPane 以获得更好的 UI 体验
        val scrollPane = JBScrollPane(outputPane)
        scrollPane.horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        scrollPane.verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        
        panel.add(scrollPane, BorderLayout.CENTER)
        
        // 创建内容并添加到工具窗口
        toolWindowContent = contentFactory.createContent(panel, CONTENT_DISPLAY_NAME, false)
        toolWindow.contentManager.removeAllContents(true)
        toolWindow.contentManager.addContent(toolWindowContent!!)
    }
    
    /**
     * 向输出区域添加文本
     */
    private fun appendToOutput(text: String, style: AttributeSet) {
        outputPane?.let {
            val doc = it.styledDocument
            doc.insertString(doc.length, text, style)
            it.caretPosition = doc.length
        }
    }
} 