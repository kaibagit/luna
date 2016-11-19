package org.luna.rpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.transport.ClientTransport;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by luliru on 2016/11/18.
 */
public class NettyClientTransport implements ClientTransport {

    private URL url;

    private EventLoopGroup group;

    private Channel channel;

    // 客户端请求回调Map，key值为messageId
    private ConcurrentMap<Long, NettyResponseFuture> callbackMap = new ConcurrentHashMap<>();

    public NettyClientTransport(URL url){
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public void start() {
        try{
            Codec codec = ExtensionLoader.getExtension(Codec.class,url.getParameter(URLParamType.codec.getName(),URLParamType.codec.getValue()));
            ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("decoder", new NettyDecoder(NettyClientTransport.this,codec));
                    pipeline.addLast("encoder", new NettyEncoder(NettyClientTransport.this,codec));
                    pipeline.addLast("handler",new NettyCallbackHandler(NettyClientTransport.this));
                }
            };
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(channelChannelInitializer);

            channel = b.connect(url.getHost(), url.getPort()).sync().channel();
        }catch (Exception e){
            throw new LunaRpcException("NettyClientTransport start error .",e);
        }
    }

    @Override
    public void destory() {
        group.shutdownGracefully();
    }

    @Override
    public Response send(Request request) {
        NettyResponseFuture response = new NettyResponseFuture(request.getMessageId());
        callbackMap.put(request.getMessageId(),response);
        channel.writeAndFlush(request);
        response.getValue();    //阻塞直到获取返回值
        return response;
    }

    public NettyResponseFuture removeCallback(long messageId) {
        return callbackMap.remove(messageId);
    }
}
