package org.luna.rpc.transport.netty;

import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.ServerTransport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.luna.rpc.util.LoggerUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by luliru on 2016/11/12.
 */
public class NettyServerTransport implements ServerTransport {

    private URL url;

    private MessageHandler messageHandler;

    private ServerBootstrap serverBootstrap;

    private EventExecutorGroup eventExecutorGroup;

    private Channel channel;

    private volatile boolean started = false;

    private static final int READ_IDEL_TIME_OUT = 15; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 0;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 0; // 所有超时

    public NettyServerTransport(URL url, MessageHandler messageHandler) {
        this.url = url;
        this.messageHandler = new WrappedMessageHandler(messageHandler);
    }

    @Override
    public synchronized void start() {
        if(started){
            return;
        }
        try{
            int workerThrad = url.getIntParameter(URLParamType.workerThread.getName(),Integer.valueOf(URLParamType.workerThread.getValue()));
            eventExecutorGroup = new DefaultEventExecutorGroup(workerThrad);

            Codec codec = ExtensionLoader.getExtension(Codec.class,url.getParameter(URLParamType.codec.getName(),URLParamType.codec.getValue()));
            ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("decoder", new NettyDecoder(NettyServerTransport.this,codec));
                    pipeline.addLast("encoder", new NettyEncoder(NettyServerTransport.this,codec));
                    pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
                            WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));     //心跳触发handler
                    pipeline.addLast("heartbeatHandler",new HeartbeatServerHandler());      //心跳处理handler
                    pipeline.addLast(eventExecutorGroup, new NettyMessageHandler(NettyServerTransport.this,messageHandler));
                    pipeline.addLast("errorHandler",new ExceptionHandler());
                }
            };

            Class channelClass = NioServerSocketChannel.class;
            if(Epoll.isAvailable()){
                channelClass = EpollServerSocketChannel.class;
            }else if(KQueue.isAvailable()){
                channelClass = KQueueServerSocketChannel.class;
            }

            serverBootstrap = new ServerBootstrap();
            channel = serverBootstrap.group(NettySharing.acceptGroup(), NettySharing.ioWorkerGroup())
                    .channel(channelClass)
                    .childHandler(channelChannelInitializer)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .bind(url.getPort()).sync()
                    .channel();
            started = true;
            LoggerUtil.info("NettyServerTransport started , port = {} , workerThrad = {} .",url.getPort(),workerThrad);
        }catch (Exception e){
            throw new LunaRpcException("NettyServerTransport start fail .",e);
        }
    }

    @Override
    public void destroy() {
        if(channel != null) {
            channel.close();
        }
        eventExecutorGroup.shutdownGracefully();
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
