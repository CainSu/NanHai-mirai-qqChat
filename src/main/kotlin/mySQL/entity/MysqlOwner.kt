package mySQL.entity

import dsl.TableSchema

data class MysqlOwner(var id: Long? = null, var grantor: Long? = null, var permission: String? = null) {
    companion object : TableSchema("mysqlOwner") {
        val id = column("id")
        val grantor = column("grantor")
        val permission = column("permission")
    }
}