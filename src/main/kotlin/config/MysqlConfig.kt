package org.example.mirai.plugin.config

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MysqlConfig : ReadOnlyPluginConfig("MysqlConfig") {
    @ValueDescription("请手动修改以下配置\n数据库配置")
    val mysqlConfig by value(MysqlConfig())
    @Serializable
    data class MysqlConfig(
        val USER : String = "root",
        val PASS : String = "123456",
        val URL : String = "jdbc:mysql://localhost:3306/test?",
        val driverClassName : String = "com.mysql.cj.jdbc.Driver"
    )
}
