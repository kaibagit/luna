package org.luna.rpc.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.luna.rpc.util.LoggerUtil;

/**
 * Created by luliru on 2016/11/24.
 */
public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LoggerUtil.error("NettyTransprot handler error",cause);
    }
}
