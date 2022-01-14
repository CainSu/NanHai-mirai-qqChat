package org.example.mirai.plugin.mySQL

import com.alibaba.druid.pool.DruidDataSource
import database.DB
import database.DBConnection
import database.exec
import database.query
import dsl.Query
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Message
import org.example.mirai.plugin.config.MysqlConfig
import util.generateEntity


class MySqlConnPoll {
    private val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
    private val URL = "jdbc:mysql://localhost:3306/test01?"
    private val USER = "root"
    private val PASS = "31415926"

    //建立线程池
    fun MySqlConnPoll(): DBConnection {
        val druid = DruidDataSource()
        druid.apply {
            username = MysqlConfig.mysqlConfig.USER
            password = MysqlConfig.mysqlConfig.PASS
            url = MysqlConfig.mysqlConfig.URL
            driverClassName = MysqlConfig.mysqlConfig.driverClassName
        }
        val db = DBConnection(druid, DB.MYSQL)
        return db
    }

    //增删查改
    //增删改
    public fun execSQL(db: DBConnection, sql: String) {
        val conn = db.getConnection()
        exec(conn, sql)
        conn.close()
    }
    //查
    public fun querySQL(db: DBConnection, sql: String): List<Map<String,Any?>> {
        //建立连接
        val conn = db.getConnection()
        val temp = query(conn, sql)
        //关闭连接 必须！！！
        conn.close()
        return temp
    }

    //权限查找
    public suspend fun checkPermission(db: DBConnection, owner: String, player: User): Int {
        val temp = org.example.mirai.plugin.mySQL.MySqlConnPoll().querySQL(db, "select * from $owner where id = '${player.id}'")
        //当拥有权限时返回0 没有或异常返回1
        return if(temp.size == 1 && temp[0]["permission"] == "administrator") {
            return 0
        } else if (temp.size >1) {
            player.sendMessage(" 错误代码2001:联系管理员修正权限！依然可以通过！")
            return 0
        } else {
            //这里的坑日后再补 给予指令权限需要同时在qqChatOwner中给用户提权
            player.sendMessage(" 错误代码1002:亲，您没有权限呢")
            return 1
        }
    }

    //Command消息处理 仅供SqlCommand使用
    public suspend fun messageInterception(message: Message): String = message.contentToString().substring(message.contentToString().indexOf("\"")+1,message.contentToString().length-1)

}