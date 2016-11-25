package org.luna.rpc.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.Request;
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
            if(msg instanceof Request){
                Request request = (Request)msg;
                if(request.isHeartbeat()){

                }else{
                    Object result = messageHandler.handle(transport,request);
                    ctx.writeAndFlush(result);
                }
            }else{
                throw new LunaRpcException("Unsupported message type : "+msg.getClass().getName());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
//        ctx.fireExceptionCaught(cause);
    }

}