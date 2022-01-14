package org.example.mirai.plugin.privateChat

import io.ktor.client.features.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.AbstractEvent
import net.mamoe.mirai.message.data.Message

//聊天
//开始回话检查
class ChatDetectEvent(val sender: User): AbstractEvent()
//回话中
class ChatWithEvent(val message: Message): AbstractEvent()
//结束回话
class EndChatEvent(val message: Message): AbstractEvent()
//添加词汇
class AddWordEvent(val sentence: String, val message: Message): AbstractEvent()
//删除词汇
class DeleteWordEvent(val receive: String, val seed: String): AbstractEvent()
class DeleteAllWordEvent(val receive: String): AbstractEvent()
//修改词汇
class UpdateReceiveWordEvent(val receive: String, val seed: String, val update: String): AbstractEvent()
class UpdateSeedWordEvent(val receive: String, val seed: String, val update: String): AbstractEvent()
