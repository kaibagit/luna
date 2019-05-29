package org.luna.rpc.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.luna.rpc.codec.Codec;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.extension.ExtensionLoader;
import org.luna.rpc.transport.ClientTransport;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by luliru on 2016/11/18.
 */
public class NettyClientTransport implements ClientTransport {

    private static Logger logger = LoggerFactory.getLogger(NettyClientTransport.class);

    private NettyTransportFactory factory;

    private URL url;

    private Channel channel;

    /** 是否已启动 */
    private volatile boolean started = false;

    private static final int READ_IDEL_TIME_OUT = 15; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 10;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 0; // 所有超时

    public NettyClientTransport(URL url,NettyTransportFactory factory){
        this.url = url;
        this.factory = factory;
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
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(NettySharing.ioWorkerGroup).channel(NioSocketChannel.class)
                    .handler(channelChannelInitializer)
                    .option(ChannelOption.TCP_NODELAY,true);

            channel = bootstrap.connect(url.getHost(), url.getPort()).sync().channel();
        }catch (Exception e){
            throw new LunaRpcException("NettyClientTransport start error .",e);
        }
        started = true;
    }

    @Override
    public void destroy() {
        factory.destroyClientTransport(url);
        if(channel != null) {
            channel.close();
        }
    }

    @Override
    public ResponseFuture send(Request request) {
        try {
            long timeout = getUrl().getLongParameter(URLParamType.requestTimeout.name(),URLParamType.requestTimeout.getLongValue());
            ResponseFuture future = new ResponseFuture(request,timeout);
            channel.writeAndFlush(request);
            return future;
        } catch (Exception e) {
            throw new LunaRpcException("send to remote server failed.",e);
        }
    }
}
