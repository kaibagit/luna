package org.luna.rpc.transport.request;

/**
 * Created by kaiba on 2016/5/20.
 */
public class RpcMessage {

    private RpcMessageHeader header;

    private byte[] body;

    public RpcMessageHeader getHeader() {
        return header;
    }

    public void setHeader(RpcMessageHeader header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
