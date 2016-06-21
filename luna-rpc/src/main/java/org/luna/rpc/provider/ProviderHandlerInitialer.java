package org.luna.rpc.provider;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.luna.rpc.transport.request.RpcMessageDecoder;

/**
 * Created by kaiba on 2016/5/24.
 */
public class ProviderHandlerInitialer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder",new RpcMessageDecoder());

        pipeline.addLast("handler",null);
    }
}
