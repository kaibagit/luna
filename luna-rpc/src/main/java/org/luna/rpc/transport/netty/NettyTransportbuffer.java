package org.luna.rpc.transport.netty;

import io.netty.buffer.ByteBuf;
import org.luna.rpc.transport.TransportBuffer;

/**
 * Created by luliru on 2016/11/15.
 */
public class NettyTransportBuffer implements TransportBuffer {

    private ByteBuf byteBuf;

    public NettyTransportBuffer(ByteBuf byteBuf){
        this.byteBuf = byteBuf;
//        byteBuf.getBytes()
    }


    @Override
    public int readableBytes() {
        return byteBuf.readableBytes();
    }

    @Override
    public short getShort(int index) {
        return byteBuf.getShort(index);
    }

    @Override
    public int getInt(int index) {
        return byteBuf.getInt(index);
    }

    @Override
    public long getLong(int index) {
        return byteBuf.getLong(index);
    }

    @Override
    public byte getByte(int index) {
        return byteBuf.getByte(index);
    }

    @Override
    public TransportBuffer getBytes(int index, byte[] dst, int dstIndex, int length) {
        byteBuf.getBytes(index,dst,dstIndex,length);
        return this;
    }
}
