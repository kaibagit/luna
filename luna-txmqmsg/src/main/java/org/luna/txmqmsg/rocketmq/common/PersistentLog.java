package org.luna.txmqmsg.rocketmq.common;

import org.luna.txmqmsg.api.TxLog;

import java.util.Date;

/**
 * 已持久化的事务日志
 * Created by luliru on 2016/9/23.
 */
public class PersistentLog implements TxLog {

    private String id;

    private String topic;

    private String tags;

    private String keys;

    private byte[] body;

    private Date createTime;

    private Integer failTimes;

    private Date nextExecuteTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFailTimes() {
        return failTimes;
    }

    public void setFailTimes(Integer failTimes) {
        this.failTimes = failTimes;
    }

    public Date getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Date nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }
}
