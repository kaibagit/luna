package org.luna.rpc.transport.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Response;
import org.luna.rpc.transport.Transport;
import org.luna.rpc.util.LoggerUtil;

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
                LoggerUtil.debug("Received message from {} , mid = {} , heartbeat = {}",ctx.channel().remoteAddress(),request.getMessageId(),request.isHeartbeat());
                if(request.isHeartbeat()){
                    Response response = new Response(request.getMessageId());
                    response.setHeartbeat(true);
                    ctx.writeAndFlush(response);
                }else{
                    Object result = messageHandler.handle(transport,request);
                    ctx.writeAndFlush(result);
                    if(result instanceof Response){
                        Response response = (Response) result;
                        if(response.getException() != null){
                            LoggerUtil.error("MessageHandler handler error",response.getException());
                        }
                    }
                }
            }else{
                throw new LunaRpcException("Unsupported message type : "+msg.getClass().getName());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
