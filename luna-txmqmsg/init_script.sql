/*
prepare时数据在一个事务里入库，并缓存
execute会将缓存的消息发送，成功则删除，失败更新数据库
如果用户没有调用execute，会有一个timeout机制，去db中反查这条记录是否存在，存在则发送，不存在则删除该缓存

恢复机制：
每个应用连上zookeeper，选举leader，leader会定期扫描DB中超过一定时间的消息，并发送，失败则重试；当重试到一定次数，将消息转到失败表

后台监控可以看到所有失败表里的数据，在手动排查解决后，将消息重新放入消息表
*/


create database txmqmsg;
create table tx_message(
    id bingint unsigned AUTO_INCREMENT primary key,
    topic varchar(255),
    tags varchar(255),
    keys varchar(255),
    body blob,
    create_time datetime,
    fail_times int default 0,
    next_execute_time datetime,
    KEY `i_tm_net` (`next_execute_time`)
);
create table fail_tx_message(
    id bingint unsigned primary key,
    topic varchar(255),
    tags varchar(255),
    keys varchar(255),
    body blob,
    create_time datetime
);
