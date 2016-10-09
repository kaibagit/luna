package org.luna.txmqmsg.rocketmq.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.luna.txmqmsg.api.TxLog;

import com.alibaba.rocketmq.client.producer.MQProducer;
import org.luna.txmqmsg.common.RetryRuleManager;
import org.luna.txmqmsg.rocketmq.common.LogPushManager;
import org.luna.txmqmsg.rocketmq.common.PersistentLog;
import org.luna.txmqmsg.rocketmq.common.LogStorage;
import org.luna.txmqmsg.rocketmq.common.TransientLog;

/**
 * Created by Administrator on 2016/9/13.
 */
public class TxLogManager implements org.luna.txmqmsg.api.TxLogManager {

    private ThreadLocal<List<PersistentLog>> cachedLogs = new ThreadLocal<>();

    private MQProducer mqProducer;

    private int executeThreadNum = 20;

    private ExecutorService pushExecutor;

    private DataSource dataSource;

    private LogStorage logStorage;

    private String databaseName;

    private LogPushManager logPushManager;

    private RetryRuleManager retryRuleManager = new RetryRuleManager();

    public void init(){
        pushExecutor = Executors.newFixedThreadPool(executeThreadNum);
        logStorage = new LogStorage(dataSource,databaseName);
        logPushManager = new LogPushManager(logStorage,retryRuleManager,mqProducer);
    }

    public void destroy(){
        pushExecutor.shutdown();
    }

    @Override
    public String prepare(TxLog txMessage) {
        checkMessage(txMessage);
        TransientLog rocketMqTxMessage = (TransientLog) txMessage;
        Long retryMillis = null;
        RetryRuleManager.RetryInterval retryInterval = retryRuleManager.getNext(0);
        if(retryInterval == null){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE,1); //如果没有定义重试规则，则默认在一分钟之后重试
            retryMillis = cal.getTimeInMillis();
        }else{
            retryMillis = new Date().getTime() + retryInterval.toMillis();
        }
        PersistentLog persistentTxMessage = logStorage.createTxLog(rocketMqTxMessage,retryMillis);
        String id = persistentTxMessage.getId();

        List<PersistentLog> logs = cachedLogs.get();
        if(logs == null){
            logs = new ArrayList<>();
            cachedLogs.set(logs);
        }
        logs.add(persistentTxMessage);
        return id;
    }

    private void checkMessage(TxLog txMessage){
        if(txMessage == null){
            throw new RuntimeException("TxLog不能为空");
        }
        if(!(txMessage instanceof TransientLog)){
            throw new RuntimeException("不是合法的RocketMqTxMessage");
        }
    }

    @Override
    public void execute() {
        List<PersistentLog> logMap = cachedLogs.get();
        if(logMap != null && logMap.size() > 0){
            for(final PersistentLog log : logMap){
                pushExecutor.execute(new Runnable(){
                    @Override
                    public void run() {
                        logPushManager.pushToMq(log);
                    }
                });
            }
        }
        cachedLogs.remove();
    }

    @Override
    public void execute(String logId) {
        final PersistentLog log =getCachedLogById(logId);
        pushExecutor.execute(new Runnable(){
            @Override
            public void run() {
                logPushManager.pushToMq(log);
            }
        });
        removeCachedLogByLogId(logId);
    }

    private PersistentLog getCachedLogById(String logId){
        PersistentLog targetLog = null;
        List<PersistentLog> logs = cachedLogs.get();
        for(PersistentLog log : logs){
            if(log.getId().equals(logId)){
                targetLog = log;
            }
            break;
        }
        return targetLog;
    }

    private void removeCachedLogByLogId(String logId){
        List<PersistentLog> logs = cachedLogs.get();
        for(int i=0;i<logs.size();i++){
            PersistentLog log = logs.get(i);
            if(log.getId().equals(logId)){
                logs.remove(i);
            }
            break;
        }
    }

    @Override
    public void cancel() {
        cachedLogs.remove();
    }

    @Override
    public void cancel(String logId) {
        removeCachedLogByLogId(logId);
    }

    public void setMqProducer(MQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    public void setExecuteThreadNum(int executeThreadNum) {
        this.executeThreadNum = executeThreadNum;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
