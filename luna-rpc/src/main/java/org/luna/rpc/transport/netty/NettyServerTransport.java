package org.luna.rpc.transport.netty;

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

/**
 * Created by luliru on 2016/11/12.
 */
public class NettyServerTransport implements ServerTransport {

    private URL url;

    private MessageHandler messageHandler;

    private ServerBootstrap serverBootstrap;

    public NettyServerTransport(URL url, MessageHandler messageHandler) {
        this.url = url;
        this.messageHandler = new WrappedMessageHandler(messageHandler);
    }

    @Override
    public synchronized void start() {
        try{
            Codec codec = ExtensionLoader.getExtension(Codec.class,url.getParameter(URLParamType.codec.getName(),URLParamType.codec.getValue()));
            ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("decoder", new NettyDecoder(NettyServerTransport.this,codec));
                    pipeline.addLast("encoder", new NettyEncoder(NettyServerTransport.this,codec));
                    pipeline.addLast("handler", new NettyMessageHandler(NettyServerTransport.this,messageHandler));
                }
            };

            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();   //默认是CPU核数的2倍
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelChannelInitializer)
                    .bind(url.getPort()).sync();
            System.out.println("==========");
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
