package org.luna.rpc.transport.netty;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Netty共享对象，主要是EventLoopGroup
 */
public class NettySharing {

    public static final NioEventLoopGroup eventWorkerGroup = new NioEventLoopGroup(1);

    public static final NioEventLoopGroup ioWorkerGroup = new NioEventLoopGroup();    //默认是CPU核数的2倍
}
