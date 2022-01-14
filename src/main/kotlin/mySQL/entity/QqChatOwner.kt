package mySQL.entity

import dsl.TableSchema

data class QqChatOwner(var id: Long? = null, var grantor: Long? = null, var permission: String? = null) {
    companion object : TableSchema("qqChatOwner") {
        val id = column("id")
        val grantor = column("grantor")
        val permission = column("permission")
    }
}