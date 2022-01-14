import org.jetbrains.kotlin.ir.backend.js.compile


plugins {
    val kotlinVersion = "1.4.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.9.2"
}

group = "org.example"
version = "1.1.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
    implementation("com.gitee.wangzhang:easysql:1.1.6")
    implementation("org.apache.commons:commons-lang3:3.4")
    implementation("c3p0:c3p0:0.9.1.2")
    implementation("com.alibaba:druid:1.0.18")
}

