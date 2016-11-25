package org.luna.rpc.transport;

/**
 * Created by luliru on 2016/11/15.
 */
public interface TransportBuffer {

    int readableBytes();

    short getShort(int index);

    int getInt(int index);

    long  getLong(int index);

    byte  getByte(int index);

    TransportBuffer readBytes(int length);

    TransportBuffer getBytes(int index, byte[] dst, int dstIndex, int length);
}
