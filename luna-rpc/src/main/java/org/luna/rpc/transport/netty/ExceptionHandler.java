package org.luna.rpc.transport.netty;

import io.netty.channel.*;
import org.luna.rpc.util.LoggerUtil;

import java.net.SocketAddress;

/**
 * Created by luliru on 2016/11/24.
 */
public class ExceptionHandler extends ChannelDuplexHandler {

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LoggerUtil.error("NettyTransprot handler error",cause);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    LoggerUtil.error("NettyTransprot connect fail");
                }
            }
        }));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    LoggerUtil.error("NettyTransprot write fail");
                }
            }
        }));
    }

}
