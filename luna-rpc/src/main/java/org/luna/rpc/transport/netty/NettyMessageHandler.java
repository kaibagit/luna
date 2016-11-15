package org.luna.rpc.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.luna.rpc.transport.MessageHandler;
import org.luna.rpc.transport.Transport;

/**
 * Created by luliru on 2016/11/15.
 */
public class NettyMessageHandler extends ChannelInboundHandlerAdapter {

    private Transport transport;

    private MessageHandler messageHandler;

    public NettyMessageHandler(Transport transport,MessageHandler messageHandler){
        this.transport = transport;
        this.messageHandler = messageHandler;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            messageHandler.handle(transport,msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
