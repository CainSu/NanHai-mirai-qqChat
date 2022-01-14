package org.example.mirai.plugin.mySQL

import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.AbstractEvent

class QueryEvent(val sql: String, val player: User): AbstractEvent()
class ExecEvent(val sql: String, val player: User): AbstractEvent()