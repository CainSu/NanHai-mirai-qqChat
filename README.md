**屁用没有的功能**


**使用方式**

1.

    在Release下载最新版jar
    
    放置在Mirai目录的plugins文件下

2.

    下载并安装MySQL数据库,版本8.0以上,并配置好账号密码
    
    建立一个数据库
    
    mysql>create database 数据库名称;
    
    使用这个数据库
    
    mysql>use 数据库名称;
    
    建立表单
    
    mysql>create table qqChat (id varchar(20), receive varchar(100), seed varchar(100));
    
    mysql>create table qqChatOwner (id bigint, grantor bigint, permission varchar(15));
    
    mysql>create table mysqlOwner (id bigint, grantor bigint, permission varchar(15));
    
    在表单中先插入两条数据
    
    mysql>insert into mysqlOwner values (自己的QQ,自己的QQ,'administrator');
    
    mysql>insert into qqChatowner values (自己的QQ,自己的QQ,'administrator');
    
    建错表可使用delete删除
    
    mysql>delete from 表单;
    

3.

    先运行一次Mirai 然后stop
    
    打开Mirai目录下的config的org.example.mirai.NanHai-qqChat
    
    修改MysqlConfig.yml
    
    USER: Mysql账号名
    
    PASS: Mysql密码
    
    URL: 'jdbc:mysql://localhost:3306/数据库名称?'
    
    driverClassname: #这个不用修改 默认就好
    
    再次启动Mirai
    
    在群里或者私信Bot 输入/start 有欢迎语就是打开成功
    
    如果输入无任何反应就是没有安装chat-command或没有权限 安装chat-command或增加权限即可

**可使用命令**

|  命令   | 使用方式  | 解释 |
|  ----  | ----  | ---- |
| /start  | /start | 开启私信聊天模式 |
| /end  | /end | 结束聊天模式 |
| /xr | /xr 你好啊 你好 | 写入对话库 |
| /sc | /sc only 你好啊 你好 | 删除对话 |
| /xg | /xg receive 你好啊 你好 嗨 | 修改对话 |
| /mysql | /mysql select "select * from qqChat" | 可自己对数据库进行操作 |
