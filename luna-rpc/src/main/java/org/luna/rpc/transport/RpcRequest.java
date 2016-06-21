package org.luna.rpc.transport;

/**
 * Created by kaiba on 2016/5/29.
 */
public class RpcRequest {

    private Long messageId;

    private Integer serializeType;

    private boolean heartBeat;

    private Object body;

    public Integer getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(Integer serializeType) {
        this.serializeType = serializeType;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public boolean isHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(boolean heartBeat) {
        this.heartBeat = heartBeat;
    }
}
