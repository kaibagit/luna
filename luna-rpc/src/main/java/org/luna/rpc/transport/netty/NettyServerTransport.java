package org.luna.rpc.transport.netty;

import io.netty.handler.timeout.IdleStateHandler;
import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.ServerTransport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
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

    private volatile boolean started = false;

    private static final int READ_IDEL_TIME_OUT = 30; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 35;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 35; // 所有超时

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
                    pipeline.addLast("handler", new NettyMessageHandler(NettyServerTransport.this,messageHandler));
                    pipeline.addLast("errorHandler",new ExceptionHandler());
                }
            };

            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();   //默认是CPU核数的2倍
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelChannelInitializer)
                    .bind(url.getPort()).sync();
            started = true;
            LoggerUtil.info("NettyServerTransport started.");
        }catch (Exception e){
            throw new LunaRpcException("NettyServerTransport start fail .",e);
        }
    }

    @Override
    public void destory() {
        serverBootstrap.group().shutdownGracefully();
        serverBootstrap.childGroup().shutdownGracefully();
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
