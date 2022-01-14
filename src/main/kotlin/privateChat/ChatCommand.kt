package org.example.mirai.plugin.privateChat

import kotlinx.coroutines.Job
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.broadcast
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.globalEventChannel
import org.example.mirai.plugin.PluginMain
import org.example.mirai.plugin.mySQL.MySqlConnPoll

//激活回话
object StartChatCommand : SimpleCommand(
    PluginMain,
    "start",
) {
    @Handler
    suspend fun CommandSenderOnMessage<MessageEvent>.start() {
        ChatDetectEvent(sender = fromEvent.sender).broadcast()
    }
}

//结束回话
object EndChatCommand : SimpleCommand(
    PluginMain,
    "end",
) {
    @Handler
    @EventHandler(priority = EventPriority.HIGHEST)
    suspend fun CommandSenderOnMessage<MessageEvent>.end() {
        EndChatEvent(message = fromEvent.message).broadcast()
    }
}

//insert
object AddWordCommand : SimpleCommand(
    PluginMain,
    "xr",
) {
    @Handler
    suspend fun CommandSenderOnMessage<MessageEvent>.addWord(receive: String, seed: String) {
        AddWordEvent("$receive#$seed", fromEvent.message).broadcast()
    }
}

//delete
object DeleteWordCommand : CompositeCommand(
    PluginMain,
    "sc",
) {
    @SubCommand("all")
    suspend fun CommandSender.all(receive: String) {
        DeleteAllWordEvent(receive).broadcast()
    }

    @SubCommand("only")
    suspend fun CommandSender.only(receive: String, seed: String) {
        DeleteWordEvent(receive, seed).broadcast()
    }
}

//update
object UpdateWordCommand : CompositeCommand(
    PluginMain,
    "xg",
) {
    @SubCommand("receive")
    suspend fun CommandSender.receive(receive: String, seed: String, updateReceive: String) {
        UpdateReceiveWordEvent(receive, seed, updateReceive).broadcast()
    }

    @SubCommand("seed")
    suspend fun CommandSender.seed(receive: String, seed: String, updateSeed: String) {
        UpdateSeedWordEvent(receive, seed, updateSeed).broadcast()
    }
}