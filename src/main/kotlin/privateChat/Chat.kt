package org.example.mirai.plugin.privateChat

import database.DBConnection
import io.ktor.client.features.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import org.example.mirai.plugin.mySQL.MySqlConnPoll
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.Subject
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

public class Chat(
    override val coroutineContext: CoroutineContext,
    private val db: DBConnection,
    private val player: User,
) : CoroutineScope {

    private val chat = Job()
    private val channel = globalEventChannel(chat)

    public fun start() {

        channel.subscribeAlways<EndChatEvent> {
            if (checkPermission("qqChatOwner") == 0) {
                val temp = SimpleDateFormat("HH").format(Date()).toInt()
                if (temp in 22..24) {
                    player.sendMessage("晚安啦")
                }
                chat.cancel()
            }
        }

        channel.subscribeAlways<ChatWithEvent> {
            val temp = MySqlConnPoll().querySQL(db, "select seed from qqChat where id = '${player.id}' and receive = '${message.contentToString()}'")
            val mcts = message.contentToString()

            if (temp.size == 1) {
                player.sendMessage(temp[0]["seed"].toString())
            } else if (temp.size > 1) {
                val random = (temp.indices).random()
                player.sendMessage(temp[random]["seed"].toString())
            } else if (!mcts.startsWith("/")) {
                player.sendMessage("错误代码1003: 请添加词库")
            }
        }

        //增删查改
        //增加
        channel.subscribeAlways<AddWordEvent> {
            //id, sentence
            if (checkPermission("qqChatOwner") == 0) {
                val temp = sentence.indexOf("#")
                val receive = sentence.substring(0,temp)
                val seed = sentence.substring(temp+1,sentence.length)
                if (!receive.startsWith("/") && !seed.startsWith("/")) {
                    MySqlConnPoll().execSQL(db, "insert into qqChat values (${player.id},'$receive','$seed')")
                    player.sendMessage("OK")
                }
            }
            //INSERT INTO pet
            //       VALUES ('Puffball','Diane','hamster','f','1999-03-30',NULL);
        }

        //删除
        channel.subscribeAlways<DeleteAllWordEvent> {
            if (checkPermission("qqChatOwner") == 0) {
                MySqlConnPoll().execSQL(db, "delete from qqChat where id = '${player.id}' and receive = '$receive'")
                player.sendMessage("OK")
            }
        }

        channel.subscribeAlways<DeleteWordEvent> {
            if (checkPermission("qqChatOwner") == 0) {
                MySqlConnPoll().execSQL(db, "delete from qqChat where id = '${player.id}' and receive = '$receive' and seed = '$seed'")
                player.sendMessage("OK")
            }
        }

        //查找 有需要再写 只有不是九年义务教育漏网之余 都知道怎么办

        //修改
        channel.subscribeAlways<UpdateReceiveWordEvent> {
            if (checkPermission("qqChatOwner") == 0) {
                MySqlConnPoll().execSQL(db, "update qqChat set receive = '$update' where id = '${player.id}' and receive = '$receive' and seed = '$seed'")
                player.sendMessage("OK")
            }
        }

        channel.subscribeAlways<UpdateSeedWordEvent> {
            if (checkPermission("qqChatOwner") == 0) {
                MySqlConnPoll().execSQL(db, "update qqChat set receive = '$update' where id = '${player.id}' and receive = '$receive' and seed = '$seed'")
                player.sendMessage("OK")
            }
        }

    }

    //检查权限(仅供chat使用)
    private suspend fun checkPermission(owner: String): Int {
        val temp = MySqlConnPoll().querySQL(db, "select * from $owner where id = '${player.id}'")
        //当拥有权限时返回0 没有或异常返回1
        if (temp.size == 1 && temp[0]["permission"] == "administrator") {
            return 0
        } else if (temp.size >1) {
            player.sendMessage(" 错误代码1001:联系管理员！")
            return 1
        } else {
            //这里的坑日后再补 给予指令权限需要同时在qqChatOwner中给用户提权
            player.sendMessage(" 错误代码1002:亲，您没有权限呢")
            return 1
        }
    }

}
