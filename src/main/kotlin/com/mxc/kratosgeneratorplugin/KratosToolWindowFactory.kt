package com.mxc.kratosgeneratorplugin

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

/**
 * Kratos Generator 工具窗口工厂类
 * 用于创建和初始化 "Kratos Generator" 工具窗口
 */
class KratosToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val mainPanel = JBPanel<JBPanel<*>>(BorderLayout())
        
        // 创建欢迎面板
        val welcomePanel = JBPanel<JBPanel<*>>()
        welcomePanel.layout = BoxLayout(welcomePanel, BoxLayout.Y_AXIS)
        welcomePanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        
        // 添加标题
        val titleLabel = JBLabel("Kratos Generator 插件")
        titleLabel.font = Font(titleLabel.font.name, Font.BOLD, 16)
        titleLabel.alignmentX = 0.5f
        titleLabel.icon = AllIcons.General.Information
        welcomePanel.add(titleLabel)
        welcomePanel.add(Box.createVerticalStrut(10))
        
        // 创建样式化的说明文本
        val infoPane = JTextPane()
        infoPane.isEditable = false
        infoPane.isOpaque = false
        infoPane.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        
        val doc = infoPane.styledDocument
        
        // 添加说明文本
        val normalStyle = SimpleAttributeSet()
        StyleConstants.setFontFamily(normalStyle, "Dialog")
        StyleConstants.setFontSize(normalStyle, 13)
        
        val titleStyle = SimpleAttributeSet(normalStyle)
        StyleConstants.setBold(titleStyle, true)
        StyleConstants.setForeground(titleStyle, JBColor.BLUE)
        StyleConstants.setFontSize(titleStyle, 14)
        
        val featureStyle = SimpleAttributeSet(normalStyle)
        StyleConstants.setBold(featureStyle, true)
        
        doc.insertString(doc.length, "欢迎使用 Kratos Generator 插件\n\n", titleStyle)
        doc.insertString(doc.length, "此工具窗口将显示命令执行结果。请使用项目视图中的右键菜单访问插件功能：\n\n", normalStyle)
        
        doc.insertString(doc.length, "1. 生成 Proto 客户端代码：", featureStyle)
        doc.insertString(doc.length, " 右键点击非 conf.proto 的 .proto 文件\n", normalStyle)
        
        doc.insertString(doc.length, "2. 生成 Service 服务端代码：", featureStyle)
        doc.insertString(doc.length, " 右键点击非 conf.proto 的 .proto 文件\n", normalStyle)
        
        doc.insertString(doc.length, "3. 生成配置文件：", featureStyle)
        doc.insertString(doc.length, " 右键点击 conf.proto 文件\n", normalStyle)
        
        doc.insertString(doc.length, "4. 执行 wire：", featureStyle)
        doc.insertString(doc.length, " 右键点击 cmd/xxx 目录\n", normalStyle)
        
        welcomePanel.add(infoPane)
        
        // 添加到主面板
        mainPanel.add(welcomePanel, BorderLayout.CENTER)
        
        // 创建并添加内容
        val content = contentFactory.createContent(mainPanel, "欢迎", false)
        toolWindow.contentManager.addContent(content)
    }
} 