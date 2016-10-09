package org.luna.txmqmsg.rocketmq.common;

import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import org.luna.txmqmsg.common.LogUtils;
import org.luna.txmqmsg.common.RetryRuleManager;

/**
 * 事务消息日志push管理器
 * Created by luliru on 2016/10/8.
 */
public class LogPushManager {

    private LogStorage logStorage;

    private RetryRuleManager retryRuleManager;

    private MQProducer mqProducer;

    public LogPushManager(LogStorage logStorage,RetryRuleManager retryRuleManager,MQProducer mqProducer){
        this.logStorage = logStorage;
        this.retryRuleManager = retryRuleManager;
        this.mqProducer = mqProducer;
    }

    public void pushToMq(PersistentLog persistentLog){
        boolean pushSuccess = true;
        try{
            Message message = new Message(persistentLog.getTopic(),persistentLog.getTags(),persistentLog.getKeys(),persistentLog.getBody());
            SendResult sendResult = mqProducer.send(message);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK){
                pushSuccess = false;
            }
        }catch (Exception e){
            LogUtils.error("push message to rocketmq fail",e);
            pushSuccess = false;
        }
        if(pushSuccess){
            logStorage.removeTxMsg(persistentLog.getId());
        }else{
            RetryRuleManager.RetryInterval retryInterval = retryRuleManager.getNext(persistentLog.getFailTimes());
            if(retryInterval == null){
                logStorage.moveToBackup(persistentLog);
            }else{
                Long nextExecuteMillis = persistentLog.getNextExecuteTime().getTime() + retryInterval.toMillis();
                logStorage.updateLogForRetry(persistentLog.getId(),persistentLog.getFailTimes()+1,nextExecuteMillis);
            }
        }
    }

}
