package org.luna.txmqmsg.rocketmq.client;

import java.util.Calendar;

import org.luna.txmqmsg.common.Constraint;
import org.luna.txmqmsg.rocketmq.common.RocketMqPersistentTxMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;

/**
 * Created by Administrator on 2016/9/18.
 */
public class RocketMqPushTask implements Runnable{

    private static Logger log = LoggerFactory.getLogger(Constraint.LOG_NAME);

    private MQProducer mqProducer;

    private RocketMqPersistentTxMessage txMessage;

    private RocketMqTxLogStorage storage;

    private String txMessageId;

    public RocketMqPushTask(MQProducer mqProducer,RocketMqTxLogStorage storage,String txMessageId,RocketMqPersistentTxMessage txMessage){
        this.mqProducer = mqProducer;
        this.txMessage = txMessage;
        this.storage = storage;
        this.txMessageId = txMessageId;
    }

    public void run() {
        boolean pushSuccess = true;
        try{
            Message message = new Message(txMessage.getTopic(),txMessage.getTags(),txMessage.getKeys(),txMessage.getBody());
            SendResult sendResult = mqProducer.send(message);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK){
                pushSuccess = false;
            }
        }catch (Exception e){
            log.error("push message to rocketmq fail",e);
            pushSuccess = false;
        }
        if(pushSuccess){
            storage.removeTxMsg(txMessageId);
        }else{
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE,1);
            storage.recordForRetry(txMessageId,1,cal.getTime());
        }
    }

}
