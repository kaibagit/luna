package org.luna.txmqmsg.rocketmq.common;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.luna.txmqmsg.common.Constraint;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 日志存储器
 * Created by Administrator on 2016/9/14.
 */
public class LogStorage {

    private DataSource dataSource;

    private String databaseName;

    public LogStorage(DataSource dataSource, String databaseName){
        this.dataSource = dataSource;
        if(databaseName == null || databaseName.trim().equals("")){
            this.databaseName = Constraint.DEFAULT_LOG_DATABASE_NAME;
        }else{
            this.databaseName = databaseName;
        }
    }

    public PersistentLog createTxLog(final TransientLog transientLog,long nextExecuteMillis){
        try {
            Date now = new Date(new java.util.Date().getTime());
            Connection conn = DataSourceUtils.getConnection(dataSource); //获得数据库连接
            String sql = "insert into "+databaseName+".tx_message(topic,tags,keys,body,create_time,next_execute_time) values(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,transientLog.getTopic());
            pstmt.setString(2,transientLog.getTags());
            pstmt.setString(3,transientLog.getKeys());
            pstmt.setBlob(4,new ByteArrayInputStream(transientLog.getBody()));
            pstmt.setDate(5,now);
            pstmt.setDate(6,new Date(nextExecuteMillis));
            pstmt.execute();

            PreparedStatement pstmt2 = conn.prepareStatement("SELECT LAST_INSERT_ID() AS ID");
            ResultSet rs = pstmt2.executeQuery();
            rs.next();
            Long id = rs.getLong(1);

            PersistentLog persistentLog = new PersistentLog();
            persistentLog.setId(id.toString());
            persistentLog.setTopic(transientLog.getTopic());
            persistentLog.setTags(transientLog.getTags());
            persistentLog.setBody(transientLog.getBody());
            persistentLog.setCreateTime(now);
            persistentLog.setFailTimes(0);

            return persistentLog;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeTxMsg(String txMsgId){
        try {
            Connection conn = DataSourceUtils.getConnection(dataSource);
            String sql = "delete from "+databaseName+".tx_message where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,new Long(txMsgId));
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveToBackup(PersistentLog persistentLog){
        try{
            Connection conn = DataSourceUtils.getConnection(dataSource);
            conn.setAutoCommit(false);
            String insertSql = "insert into "+databaseName+".fail_tx_message(id,topic,tags,keys,body,create_time) values(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            pstmt.setLong(1,Long.valueOf(persistentLog.getId()));
            pstmt.setString(2,persistentLog.getTopic());
            pstmt.setString(3,persistentLog.getTags());
            pstmt.setString(4,persistentLog.getKeys());
            pstmt.setBytes(5,persistentLog.getBody());
            pstmt.setDate(6,new Date(persistentLog.getCreateTime().getTime()));
            pstmt.execute();

            removeTxMsg(persistentLog.getId());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void updateLogForRetry(String txMsgId, int failTimes, long nextExecuteMillis){
        try{
            Connection conn = DataSourceUtils.getConnection(dataSource);
            String sql = "update "+databaseName+".tx_message set fail_times = ? , next_execute_time = ? where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,failTimes);
            pstmt.setDate(2,new Date(nextExecuteMillis));
            pstmt.setInt(3,new Integer(txMsgId));
            pstmt.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<PersistentLog> findReadyToRetryMessages(){
        List<PersistentLog> msgList = new ArrayList<>();
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from "+databaseName+".tx_message where next_execute_time < ?");
            pstmt.setDate(1,new Date(new java.util.Date().getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String topic = rs.getString("topic");
                String tags = rs.getString("tags");
                String keys = rs.getString("keys");
                byte[] body = rs.getBytes("body");
                Date createTime = rs.getDate("create_time");
                int failTimes = rs.getInt("fail_times");
                Date nextExecuteTime = rs.getDate("next_execute_time");

                PersistentLog message = new PersistentLog();
                message.setId(String.valueOf(id));
                message.setTopic(topic);
                message.setTags(tags);
                message.setKeys(keys);
                message.setBody(body);
                message.setCreateTime(new java.util.Date(createTime.getTime()));
                message.setFailTimes(failTimes);
                message.setNextExecuteTime(new java.util.Date(nextExecuteTime.getTime()));
                msgList.add(message);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return msgList;
    }
}
