package org.example.mirai.plugin.mySQL

import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.event.broadcast
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import org.example.mirai.plugin.PluginMain

object MySQLCommand: CompositeCommand(
    PluginMain,
    "mysql"
) {
    @SubCommand("list")
    suspend fun CommandSenderOnMessage<MessageEvent>.list() {
        subject?.sendMessage(buildMessageChain {
            add(PlainText("insert\n" +
                "delete\n" +
                "select\n" +
                "update"))
        })
    }

    @SubCommand("insert")
    suspend fun CommandSenderOnMessage<MessageEvent>.insert() {
        ExecEvent(MySqlConnPoll().messageInterception(fromEvent.message), fromEvent.sender).broadcast()
    }

    @SubCommand("delete")
    suspend fun CommandSenderOnMessage<MessageEvent>.delete() {
        ExecEvent(MySqlConnPoll().messageInterception(fromEvent.message), fromEvent.sender).broadcast()
    }

    @SubCommand("select")
    suspend fun CommandSenderOnMessage<MessageEvent>.select() {
        QueryEvent(MySqlConnPoll().messageInterception(fromEvent.message), fromEvent.sender).broadcast()
    }

    @SubCommand("update")
    suspend fun CommandSenderOnMessage<MessageEvent>.update() {
        ExecEvent(MySqlConnPoll().messageInterception(fromEvent.message), fromEvent.sender).broadcast()
    }
}
