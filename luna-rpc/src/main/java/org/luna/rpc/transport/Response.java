package org.luna.rpc.transport;

/**
 * Created by luliru on 2016/11/18.
 */
public class Response {

    private long messageId;

    private boolean heartbeat = false;

    private Exception exception;

    private Object value;

    public Response(long messageId){
        this.messageId = messageId;
    }

    public boolean isHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Object getValue() {
        if(exception != null){
            return exception;
        }
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getMessageId() {
        return messageId;
    }
}
