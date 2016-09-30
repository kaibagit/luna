package org.luna.txmqmsg.rocketmq.common;

import org.luna.txmqmsg.api.TxMessage;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RocketMqTxMessage implements TxMessage {

    private String topic;
    private String tags;
    private String keys;
    private byte[] body;

    public RocketMqTxMessage(String topic, String tags, String keys, byte[] body) {
        this.topic = topic;
        this.tags = tags;
        this.keys = keys;
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public String getTags() {
        return tags;
    }

    public String getKeys() {
        return keys;
    }

    public byte[] getBody() {
        return body;
    }
}
