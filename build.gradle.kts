plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "com.mxc"
version = "1.0.4"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// 配置IntelliJ Platform Gradle Plugin
// 文档: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        // 使用2025.1版本进行调试
        intellijIdeaCommunity("2025.1")
    }
    
    // 添加额外的运行时依赖
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks {
    // 设置Java编译版本为21，与2025.1兼容
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    prepareSandbox {
        pluginName.set("Kratos Generator")
    }

    patchPluginXml {
        pluginId.set("com.mxc.kratos-generator-plugin")
        sinceBuild.set("222") // 从2022.2开始支持
        // 不设置untilBuild，以兼容所有未来版本
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
