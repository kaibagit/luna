package org.luna.rpc.transport.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Response;
import org.luna.rpc.transport.ResponseFuture;
import org.luna.rpc.util.LoggerUtil;

/**
 * Created by luliru on 2016/11/19.
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private NettyClientTransport transport;

    public NettyClientHandler(NettyClientTransport transport){
        this.transport = transport;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Response response = (Response) msg;
        ResponseFuture.received(response);
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            Channel channel = ctx.channel();
            LoggerUtil.debug("Trigger heartbeat from "+channel.localAddress()+" to "+channel.remoteAddress());

            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.WRITER_IDLE){
                Request request = new Request();
                request.setHeartbeat(true);
                try{
                    transport.send(request);
                }catch (Exception e){
                    channel.close();
                    throw new LunaRpcException("Lost connection from "+channel.remoteAddress());
                }
            }else{
                channel.close();
            }
        }else{
            ctx.fireUserEventTriggered(evt);
        }
    }
}
