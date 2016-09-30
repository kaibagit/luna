package org.luna.txmqmsg.rocketmq.recover;

import org.luna.txmqmsg.rocketmq.common.RocketMqTxMessage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Created by luliru on 2016/9/19.
 */
public class TxLogRetryStorage {

    private DataSource dataSource;

    private String databaseName;

    public TxLogRetryStorage(DataSource dataSource,String databaseName){
        this.dataSource = dataSource;
        this.databaseName = databaseName;
    }

    public Map<String,RocketMqTxMessage> findReadyToRetryLogs(){
        Map<String,RocketMqTxMessage> map = new HashMap<>();
        try{
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from "+databaseName+".tx_message where create_");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return map;
    }

    public void removeTxLog(final String txLogId){
        try {
            Date now = new Date(new java.util.Date().getTime());
            Connection conn = dataSource.getConnection();
            String sql = "delete from "+databaseName+".tx_message where id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,new Integer(txLogId));
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void logRetry(String txLogId,int failTimes,java.util.Date nextExecuteTime){
        try{
            Connection conn = dataSource.getConnection();
            String sql = "update "+databaseName+".tx_message set fail_times = ? , next_execute_time = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,failTimes);
            pstmt.setDate(2,new Date(nextExecuteTime.getTime()));
            pstmt.execute();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
