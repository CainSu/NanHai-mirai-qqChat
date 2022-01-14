package mySQL.entity

import dsl.TableSchema

data class QqChat(var id: String? = null, var receive: String? = null, var seed: String? = null) {
    companion object : TableSchema("qqChat") {
        val id = column("id")
        val receive = column("receive")
        val seed = column("seed")
    }
}