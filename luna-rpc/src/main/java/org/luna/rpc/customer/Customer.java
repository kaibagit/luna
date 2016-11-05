//package org.luna.rpc.customer;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import org.luna.rpc.proxy.ProxyFactory;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//
///**
// * Created by kaiba on 2016/5/24.
// */
//public class Customer {
//
//    private String host;
//    private int port;
//
//    public Customer(String host,int port){
//        this.host = host;
//        this.port = port;
//    }
//
//    public void start(){
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group).channel(NioSocketChannel.class)
//                    .handler(new RpcInitializer());
//
//            // 连接服务端
//            Channel ch = b.connect(host, port).sync().channel();
//
//            // 控制台输入
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    System.in));
//            for (;;) {
//                String line = in.readLine();
//                if (line == null) {
//                    continue;
//                }
//                ch.writeAndFlush(line + "\r\n");
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
//        }
//    }
//
//    public <T> T getClient(Class<T> serviceType){
//        return ProxyFactory.get(serviceType);
//    }
//}
