package org.luna.rpc.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.luna.rpc.transport.Response;

/**
 * Created by luliru on 2016/11/19.
 */
public class NettyCallbackHandler extends ChannelInboundHandlerAdapter {

    private NettyClientTransport transport;

    public NettyCallbackHandler(NettyClientTransport transport){
        this.transport = transport;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Response response = (Response) msg;
        long messageId = response.getMessageId();
        NettyResponseFuture future = transport.removeCallback(messageId);
        future.complete(response);
    }
}
