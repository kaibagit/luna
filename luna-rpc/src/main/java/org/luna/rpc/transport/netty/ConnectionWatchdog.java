package org.luna.rpc.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 断线重连Handler
 * Created by luliru on 2017/5/4.
 */
public class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask {

    private static Logger logger = LoggerFactory.getLogger(ConnectionWatchdog.class);

    private int exponentialIncreaseMaxAttempts = 8;

    private int attempts;

    private NettyClientTransport clientTransport;

    private HashedWheelTimer timer;

    public ConnectionWatchdog(NettyClientTransport clientTransport,HashedWheelTimer timer){
        this.clientTransport = clientTransport;
        this.timer = timer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Channel{} active.",ctx.channel());
        attempts = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        attemptToReconnectAfterAWhile();

        logger.warn("Disconnects with {}.",ctx.channel());

        ctx.fireChannelInactive();
    }

    private void attemptToReconnectAfterAWhile(){
        long timeout;
        if (attempts < exponentialIncreaseMaxAttempts) {
            attempts++;
            timeout = 16 << attempts;
        }else{
            timeout = 5000;
        }
        logger.debug("{} attempt to reconnect after {}ms",clientTransport,timeout);
        timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        try{
            clientTransport.reconnect();
        }catch (Exception e){
            logger.warn("{} reconnect failure.",clientTransport,e);
            attemptToReconnectAfterAWhile();
        }
    }
}
