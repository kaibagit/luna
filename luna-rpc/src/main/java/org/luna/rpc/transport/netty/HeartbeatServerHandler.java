package org.luna.rpc.transport.netty;

import org.luna.rpc.util.LoggerUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 服务端的心跳处理，当超时时，直接关闭连接
 * Created by luliru on 2016/11/29.
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.READER_IDLE){
                Channel channel = ctx.channel();
                LoggerUtil.info("Remote "+channel.remoteAddress()+" is timeout. Channel is closed.");
                ctx.channel().close();
            }
        }else{
            ctx.fireUserEventTriggered(evt);
        }
    }
}
