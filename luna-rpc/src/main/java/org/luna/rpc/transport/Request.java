package org.luna.rpc.transport;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by luliru on 2016/11/15.
 */
public class Request {

    private static final AtomicLong messageIdGenerator = new AtomicLong();

    private long messageId;

    private boolean oneway = false;

    private boolean heartbeat = false;

    private Object data;

    public Request(){
        messageId = messageIdGenerator.getAndIncrement();
    }

    public Request(long messageId){
        this.messageId = messageId;
    }

    public static AtomicLong getMessageIdGenerator() {
        return messageIdGenerator;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean isHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
