package org.luna.rpc.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.luna.rpc.codec.Codec;
import org.luna.rpc.transport.Transport;
import org.luna.rpc.transport.TransportBuffer;

/**
 * Created by luliru on 2016/11/14.
 */
public class NettyEncoder extends MessageToByteEncoder {

    private Transport transport;

    private Codec codec;

    public NettyEncoder(Transport transport,Codec codec){
        this.transport = transport;
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] data = codec.encode(transport,msg);
        out.writeBytes(data);
    }
}
