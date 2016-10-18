package org.luna.rpc.transport.netty;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.luna.rpc.core.extension.Lifecycle;
import org.luna.rpc.rpc.Client;
import org.luna.rpc.rpc.RpcInvocation;
import org.luna.rpc.rpc.RpcResult;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by luliru on 2016/10/17.
 */
public class NettyClient implements Client,Lifecycle {

    private EventLoopGroup group;

    private Bootstrap bootstrap;

    public RpcResult call(RpcInvocation invocation) {
        return null;
    }

    @Override
    public void start() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer(){

                    @Override
                    protected void initChannel(Channel ch) throws Exception {
//                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
//                                Delimiters.lineDelimiter()));
//                        pipeline.addLast("decoder", new StringDecoder());
//                        pipeline.addLast("encoder", new StringEncoder());
//
//                        // 客户端的逻辑
//                        pipeline.addLast("handler", new HelloClientHandler());

                    }
                });
    }

    @Override
    public void destory() {
        group.shutdownGracefully();
    }
}
