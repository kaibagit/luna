package org.luna.txmqmsg.rocketmq.client;

import java.io.ByteArrayInputStream;
import java.sql.*;

import javax.sql.DataSource;

import org.luna.txmqmsg.rocketmq.common.RocketMqPersistentTxMessage;
import org.luna.txmqmsg.rocketmq.common.RocketMqTxMessage;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RocketMqTxLogStorage {

    private DataSource dataSource;

    private String databaseName;

    public RocketMqTxLogStorage(DataSource dataSource,String databaseName){
        this.dataSource = dataSource;
        this.databaseName = databaseName;
    }

    public RocketMqPersistentTxMessage createTxLog(final RocketMqTxMessage txMessage){
        try {
            Date now = new Date(new java.util.Date().getTime());
            Connection conn = DataSourceUtils.getConnection(dataSource); //获得数据库连接
            String sql = "insert into "+databaseName+".tx_message(topic,tags,keys,body,create_time,next_execute_time) values(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,txMessage.getTopic());
            pstmt.setString(2,txMessage.getTags());
            pstmt.setString(3,txMessage.getKeys());
            pstmt.setBlob(4,new ByteArrayInputStream(txMessage.getBody()));
            pstmt.setDate(5,now);
            pstmt.setDate(6,now);
            pstmt.execute();

            PreparedStatement pstmt2 = conn.prepareStatement("SELECT LAST_INSERT_ID() AS ID");
            ResultSet rs = pstmt2.executeQuery();
            rs.next();
            Long id = rs.getLong(1);

            RocketMqPersistentTxMessage persistentTxMessage = new RocketMqPersistentTxMessage();
            persistentTxMessage.setId(id.toString());
            persistentTxMessage.setTopic(txMessage.getTopic());
            persistentTxMessage.setTags(txMessage.getTags());
            persistentTxMessage.setBody(txMessage.getBody());
            persistentTxMessage.setCreateTime(now);
            persistentTxMessage.setFailTimes(0);

            return persistentTxMessage;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTxMsg(final String txMsgId){
        try {
            Date now = new Date(new java.util.Date().getTime());
            Connection conn = DataSourceUtils.getConnection(dataSource);
            String sql = "delete from "+databaseName+".tx_message where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,new Integer(txMsgId));
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void recordForRetry(String txMsgId, int failTimes, java.util.Date nextExecuteTime){
        try{
            Connection conn = DataSourceUtils.getConnection(dataSource);
            String sql = "update "+databaseName+".tx_message set fail_times = ? , next_execute_time = ? where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,failTimes);
            pstmt.setDate(2,new Date(nextExecuteTime.getTime()));
            pstmt.setInt(3,new Integer(txMsgId));
            pstmt.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
