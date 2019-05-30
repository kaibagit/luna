package org.luna.rpc.transport.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Netty共享对象，主要是EventLoopGroup
 */
public class NettySharing {

    private static EventLoopGroup acceptGroup;

    private static EventLoopGroup ioWorkerGroup;    //默认是CPU核数的2倍

    static{
        if(Epoll.isAvailable()){
            acceptGroup = new EpollEventLoopGroup(1);
            ioWorkerGroup = new EpollEventLoopGroup();
        }else if(KQueue.isAvailable()){
            acceptGroup = new KQueueEventLoopGroup(1);
            ioWorkerGroup = new KQueueEventLoopGroup();
        }else{
            acceptGroup = new NioEventLoopGroup(1);
            ioWorkerGroup = new NioEventLoopGroup();
        }
    }

    public static EventLoopGroup acceptGroup(){
        return acceptGroup;
    }

    public static EventLoopGroup ioWorkerGroup(){
        return ioWorkerGroup;
    }
}
