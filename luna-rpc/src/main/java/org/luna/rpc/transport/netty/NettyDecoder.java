package org.luna.rpc.transport.netty;

import java.util.List;

import org.luna.rpc.codec.Codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.luna.rpc.transport.Transport;
import org.luna.rpc.transport.TransportBuffer;

/**
 * Created by luliru on 2016/11/14.
 */
public class NettyDecoder extends ByteToMessageDecoder {

    private Codec codec;

    private Transport transport;

    private TransportBuffer transportBuffer;

    public NettyDecoder(Transport transport, Codec codec){
        this.transport = transport;
        this.codec = codec;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(transportBuffer == null){
            transportBuffer = new NettyTransportBuffer(in);
        }
        Object decodeResult = codec.decode(transport,transportBuffer);
        if(decodeResult == null){
            return;
        }
        out.add(decodeResult);
    }

}
