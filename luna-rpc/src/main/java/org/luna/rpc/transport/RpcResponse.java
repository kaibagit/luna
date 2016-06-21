package org.luna.rpc.transport;

/**
 * Created by kaiba on 2016/5/29.
 */
public class RpcResponse {

    private Long messageId;

    private boolean heartBeat;

    private Integer serializeType;

    private Integer statusCode;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public boolean isHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(boolean heartBeat) {
        this.heartBeat = heartBeat;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(Integer serializeType) {
        this.serializeType = serializeType;
    }
}
