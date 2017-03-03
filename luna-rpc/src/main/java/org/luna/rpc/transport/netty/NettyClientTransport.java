package org.luna.rpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.transport.ClientTransport;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.ResponseFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by luliru on 2016/11/18.
 */
public class NettyClientTransport implements ClientTransport {

    private URL url;

    private EventLoopGroup group;

    private Channel channel;

    /** 是否已启动 */
    private volatile boolean started = false;

    private static final int READ_IDEL_TIME_OUT = 9; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 7;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 9; // 所有超时

    private GenericObjectPool channelPool;

    public NettyClientTransport(URL url){
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public synchronized void start() {
        if(started){
            return;
        }
        try{
            int minConnections = url.getIntParameter(URLParamType.minClientConnection.getName(),URLParamType.minClientConnection.getIntValue());
            int maxConnection = url.getIntParameter(URLParamType.maxClientConnection.getName(),URLParamType.maxClientConnection.getIntValue());

            Codec codec = ExtensionLoader.getExtension(Codec.class,url.getParameter(URLParamType.codec.getName(),URLParamType.codec.getValue()));
            ChannelInitializer<SocketChannel> channelChannelInitializer = new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("decoder", new NettyDecoder(NettyClientTransport.this,codec));
                    pipeline.addLast("encoder", new NettyEncoder(NettyClientTransport.this,codec));
                    pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
                            WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));     //心跳触发handler
                    pipeline.addLast("handler",new NettyClientHandler(NettyClientTransport.this));
                    pipeline.addLast("errorHandler",new ExceptionHandler());
                }
            };
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(channelChannelInitializer);

            GenericObjectPool.Config config = new GenericObjectPool.Config();
            config.minIdle = minConnections;
            config.maxActive = maxConnection;
            config.maxWait = 30000;
            channelPool = new GenericObjectPool(new NettyChannelFactory(url,b),config);

            //init channel
            List<Channel> initChannelList = new ArrayList<>();
            for(int i=0;i<minConnections;i++){
                initChannelList.add((Channel) channelPool.borrowObject());
            }
            for(Channel channel : initChannelList){
                channelPool.returnObject(channel);
            }
        }catch (Exception e){
            throw new LunaRpcException("NettyClientTransport start error .",e);
        }
        started = true;
    }

    @Override
    public void destory() {
        group.shutdownGracefully();
    }

    @Override
    public ResponseFuture send(Request request) {
        try {
            Channel channel = (Channel) channelPool.borrowObject();
            long timeout = getUrl().getLongParameter(URLParamType.requestTimeout.name(),URLParamType.requestTimeout.getLongValue());
            ResponseFuture future = new ResponseFuture(request,timeout);
            channel.writeAndFlush(request);
            channelPool.returnObject(channel);
            return future;
        } catch (Exception e) {
            throw new LunaRpcException("",e);
        }
    }
}
