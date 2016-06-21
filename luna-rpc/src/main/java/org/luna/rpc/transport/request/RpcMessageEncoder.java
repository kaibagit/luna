package org.luna.rpc.transport.request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by kaiba on 2016/5/20.
 */
public class RpcMessageEncoder extends
        MessageToByteEncoder<RpcMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage msg, ByteBuf out) throws Exception {
        RpcMessageHeader header = msg.getHeader();
        byte[] body = msg.getBody();

        out.writeShort(header.getMagic());
        out.writeShort(160);
        out.writeByte(header.getVersion());
        int st = header.getSt() << 6;
        int hb = header.getHb() << 4;
        int ow = header.getOw() << 2;
        int rp = header.getRp();
        out.writeByte(st+hb+ow+rp);
        out.writeByte(header.getStatusCode());
        out.writeByte(header.getReserved());
        out.writeInt(header.getMessageId());
        out.writeInt(header.getBodySize());

        out.writeBytes(body);
    }
}
