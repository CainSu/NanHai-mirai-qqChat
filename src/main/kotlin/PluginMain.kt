package org.example.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.info
import org.example.mirai.plugin.config.*
import org.example.mirai.plugin.mySQL.ExecEvent
import org.example.mirai.plugin.mySQL.MySQLCommand
import org.example.mirai.plugin.mySQL.MySqlConnPoll
import org.example.mirai.plugin.mySQL.QueryEvent
import org.example.mirai.plugin.privateChat.*
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 使用 kotlin 版请把
 * `src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin`
 * 文件内容改成 `org.example.mirai.plugin.PluginMain` 也就是当前主类全类名
 *
 * 使用 kotlin 可以把 java 源集删除不会对项目有影响
 *
 * 在 `settings.gradle.kts` 里改构建的插件名称、依赖库和插件版本
 *
 * 在该示例下的 [JvmPluginDescription] 修改插件名称，id和版本，etc
 *
 * 可以使用 `src/test/kotlin/RunMirai.kt` 在 ide 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.example.mirai.NanHai-qqChat",
        name = "qqChat",
        version = "1.1.0",
    ) {
        author("作者名称或联系方式")
        info(
            """
            这是一个测试插件, 
            在这里描述插件的功能和用法等.
        """.trimIndent()
        )
        // author 和 info 可以删除.
    }
) {
    override fun onEnable() {
        logger.info { "加载插件" }
//        配置文件目录 "${dataFolder.absolutePath}/"
        val eventChannel = GlobalEventChannel.parentScope(this)

        //Chat模块
        StartChatCommand.register()
        AbstractPermitteeId.AnyUser.permit(StartChatCommand.permission)
        EndChatCommand.register()
        AbstractPermitteeId.AnyUser.permit(EndChatCommand.permission)
        AddWordCommand.register()
        DeleteWordCommand.register()
        UpdateWordCommand.register()

        //初始化MySQL配置
        MysqlConfig.reload()

        //MySQL模块
        MySQLCommand.register()

        //打开数据连接池
        val db = MySqlConnPoll().MySqlConnPoll()
        println("数据库连接成功")

        //Chat
        var sender_chat : Long = 0
        var chat = 0
        eventChannel.subscribeAlways<ChatDetectEvent> {
            val temp = MySqlConnPoll().querySQL(db, "select id from qqChatOwner where id = '${sender.id}'")
            val temp2 = temp.toString() != "[]"
            if (temp2 && chat == 0) {
                sender_chat = sender.id
                chat = 1
                Chat(EmptyCoroutineContext, db, sender).start()
                sender.sendMessage(buildMessageChain{
                    add(PlainText("欢迎使用聊天系统\n"))
                    add(PlainText("系统还在测试，如有BUG可进行反馈\n"))
                    add(PlainText("联系方式: honanhaioh@icloud.com\n"))
                    add(PlainText("参与制作者: NanHai"))
                })
            } else if (!temp2) {
                sender.sendMessage("请联系管理员！")
            } else {
                sender.sendMessage("已经开启")
            }
        }
        eventChannel.subscribeAlways<EndChatEvent> {
            chat = 0
            println("chat 初始化成功")
        }

        eventChannel.subscribeAlways<MessageEvent> {
            if (sender_chat == sender.id)
                ChatWithEvent(message = message).broadcast()
        }

        //数据库
        eventChannel.subscribeAlways<QueryEvent> {
            if (MySqlConnPoll().checkPermission(db, "mysqlOwner", player = player) == 0) {
                val list =  MySqlConnPoll().querySQL(db, sql)
                for (i in list.indices)
                    player.sendMessage(list[i].toString())
                player.sendMessage("OK")
            }
        }
        eventChannel.subscribeAlways<ExecEvent> {
            if (MySqlConnPoll().checkPermission(db, "mysqlOwner", player = player) == 0) {
                MySqlConnPoll().execSQL(db, sql)
                player.sendMessage("OK")
            }
        }

        //测试使用
        /**
        eventChannel.subscribeAlways<GroupMessageEvent> {

//            println("======各种toString输出结果======")
//            println(message.toString())
//            println(message.contentToString())
//            println(message.serializeToMiraiCode())
//            println("==============================")

        }

        //自动同意好友申请和加群申请
        eventChannel.subscribeAlways<NewFriendRequestEvent> {
            //自动同意好友申请
            accept()
        }
        eventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            //自动同意加群申请
            accept()
        }
        */
    }
}
