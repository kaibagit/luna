package org.luna.txmqmsg.rocketmq.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.luna.txmqmsg.api.TxMessage;
import org.luna.txmqmsg.api.TxMessageManager;

import com.alibaba.rocketmq.client.producer.MQProducer;
import org.luna.txmqmsg.rocketmq.common.RocketMqPersistentTxMessage;
import org.luna.txmqmsg.rocketmq.common.RocketMqTxMessage;

/**
 * Created by Administrator on 2016/9/13.
 */
public class RocketMqTxMessageManager implements TxMessageManager {

    private ThreadLocal<Map<String,RocketMqPersistentTxMessage>> cachedMessages = new ThreadLocal<Map<String, RocketMqPersistentTxMessage>>();

    private MQProducer mqProducer;

    private int executeThreadNum = 20;

    private ExecutorService executor;

    private DataSource dataSource;

    private RocketMqTxLogStorage mqTxLogStorage;

    private String databaseName = "txmqmsg";

    public void init(){
        executor = Executors.newFixedThreadPool(executeThreadNum);
        mqTxLogStorage = new RocketMqTxLogStorage(dataSource,databaseName);
    }

    public void destroy(){
        executor.shutdown();
    }

    @Override
    public String prepare(TxMessage txMessage) {
        checkMessage(txMessage);
        RocketMqTxMessage rocketMqTxMessage = (RocketMqTxMessage) txMessage;
        RocketMqPersistentTxMessage persistentTxMessage = mqTxLogStorage.createTxLog(rocketMqTxMessage);
        String id = persistentTxMessage.getId();

        Map<String,RocketMqPersistentTxMessage> messageMap = cachedMessages.get();
        if(messageMap == null){
            messageMap = new HashMap<>();
            cachedMessages.set(messageMap);
        }
        messageMap.put(id,persistentTxMessage);
        return id;
    }

    private void checkMessage(TxMessage txMessage){
        if(txMessage == null){
            throw new RuntimeException("TxMessage不能为空");
        }
        if(!(txMessage instanceof RocketMqTxMessage)){
            throw new RuntimeException("不是合法的RocketMqTxMessage");
        }
    }

    @Override
    public void execute() {
        Map<String,RocketMqPersistentTxMessage> messageMap = cachedMessages.get();
        if(messageMap != null && messageMap.size() > 0){
            for(Map.Entry<String,RocketMqPersistentTxMessage> entry : messageMap.entrySet()){
                RocketMqPushTask task = new RocketMqPushTask(mqProducer,mqTxLogStorage,entry.getKey(),entry.getValue());
                executor.execute(task);
                messageMap.remove(entry.getKey());
            }
        }
    }

    @Override
    public void execute(String txMessageId) {
        Map<String,RocketMqPersistentTxMessage> messageMap = cachedMessages.get();
        RocketMqPersistentTxMessage txMessage = messageMap.get(txMessageId);
        RocketMqPushTask task = new RocketMqPushTask(mqProducer,mqTxLogStorage,txMessageId,txMessage);
        executor.execute(task);
        messageMap.remove(txMessageId);
    }

    @Override
    public void cancel() {
        Map<String,RocketMqPersistentTxMessage> messageMap = cachedMessages.get();
        if(messageMap != null && messageMap.size() > 0){
            messageMap.clear();
        }
    }

    @Override
    public void cancel(String txMessageId) {
        Map<String,RocketMqPersistentTxMessage> messageMap = cachedMessages.get();
        messageMap.remove(txMessageId);
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
