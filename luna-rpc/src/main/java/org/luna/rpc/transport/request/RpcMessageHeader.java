package org.luna.rpc.transport.request;

import org.luna.rpc.common.constant.MessageConstant;

/**
 * Created by kaiba on 2016/5/20.
 */
public class RpcMessageHeader {

    /** 协议魔数，为解码设计 */
    private int magic = MessageConstant.MAGIC;

    /** 协议头长度，为扩展设计 */
    private int headerSize;

    /** 协议版本，为兼容设计 */
    private int version;

    /** 消息体序列化类型 */
    private int st;

    /** 心跳消息标记，为长连接传输层心跳设计 */
    private int hb;

    /** 单向消息标记 */
    private int ow;

    /** 响应消息标记，不置位默认是请求消息 */
    private int rp;

    /** 响应消息状态码 */
    private short statusCode;

    /** 为字节对齐保留 */
    private short reserved;

    /** 消息 id */
    private int messageId;

    /** 消息体长度 */
    private int bodySize;

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public int getHb() {
        return hb;
    }

    public void setHb(int hb) {
        this.hb = hb;
    }

    public int getOw() {
        return ow;
    }

    public void setOw(int ow) {
        this.ow = ow;
    }

    public int getRp() {
        return rp;
    }

    public void setRp(int rp) {
        this.rp = rp;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public short getReserved() {
        return reserved;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getBodySize() {
        return bodySize;
    }

    public void setBodySize(int bodySize) {
        this.bodySize = bodySize;
    }
}
