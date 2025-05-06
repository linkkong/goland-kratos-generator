package com.mxc.kratosgeneratorplugin

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.diagnostic.Logger

/**
 * IDE版本兼容性工具类
 * 用于检测当前IDE版本并提供适当的API调用方式
 */
object IDEVersionHelper {
    private val LOG = Logger.getInstance(IDEVersionHelper::class.java)
    
    /**
     * 判断当前IDE版本是否至少是指定的版本
     * @param majorVersion 主版本号
     * @param minorVersion 次版本号
     * @return 如果当前版本大于等于指定版本，返回true
     */
    fun isAtLeastVersion(majorVersion: Int, minorVersion: Int): Boolean {
        val appInfo = ApplicationInfo.getInstance()
        val currentMajor = appInfo.majorVersion.toIntOrNull() ?: 0
        val currentMinor = appInfo.minorVersion.toIntOrNull() ?: 0
        
        return (currentMajor > majorVersion) || 
               (currentMajor == majorVersion && currentMinor >= minorVersion)
    }
    
    /**
     * 检查是否至少是2023.1版本
     */
    fun isAtLeast231(): Boolean {
        return isAtLeastVersion(23, 1)
    }
    
    /**
     * 执行兼容性操作，根据IDE版本选择不同的实现
     * @param modernAction 现代版本的操作
     * @param legacyAction 老版本的操作
     */
    fun <T> runCompatibleAction(
        modernVersion: Pair<Int, Int>,
        modernAction: () -> T,
        legacyAction: () -> T
    ): T {
        return if (isAtLeastVersion(modernVersion.first, modernVersion.second)) {
            LOG.info("使用现代API (${modernVersion.first}.${modernVersion.second}+)")
            modernAction()
        } else {
            LOG.info("使用传统API (低于${modernVersion.first}.${modernVersion.second})")
            legacyAction()
        }
    }
    
    /**
     * 获取当前IDE版本的相关信息
     */
    fun getVersionInfo(): String {
        val appInfo = ApplicationInfo.getInstance()
        return "IDE: ${appInfo.fullApplicationName}, " +
               "版本: ${appInfo.majorVersion}.${appInfo.minorVersion}, " +
               "构建: ${appInfo.build}"
    }
} 