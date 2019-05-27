package org.luna.rpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.luna.rpc.util.LoggerUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by luliru on 2017/5/4.
 */
public class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask {

    private int maxAttempts = 8;

    private int attempts;

    private Bootstrap bootstrap;

    private String host;

    private int port;

    private HashedWheelTimer timer;

    public ConnectionWatchdog(Bootstrap bootstrap, String host, int port,HashedWheelTimer timer){
        this.bootstrap = bootstrap;
        this.host = host;
        this.port = port;
        this.timer = timer;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (attempts < maxAttempts) {
            attempts++;
        }
        long timeout = 8 << attempts;
        timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);

        LoggerUtil.warn("Disconnects with {}.",ctx.channel());

        ctx.fireChannelInactive();
    }

    @Override
    public void run(Timeout timeout) throws Exception {
//        bootstrap
    }
}
