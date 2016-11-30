package org.luna.rpc.transport.netty;

import org.luna.rpc.core.URL;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.ClientTransport;
import org.luna.rpc.transport.ServerTransport;
import org.luna.rpc.transport.TransportFactory;
import org.luna.rpc.util.LoggerUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NettyTransport工厂类
 * Created by luliru on 2016/11/7.
 */
public class NettyTransportFactory implements TransportFactory {

    private Map<String,ClientTransport> clientTransportMap = new ConcurrentHashMap<>();

    private Map<String,ServerTransport> serverTransportMap = new ConcurrentHashMap<>();

    private Object lock = new Object();     //并发锁，防止并发创建Transport

    @Override
    public ServerTransport createServerTransport(URL url, MessageHandler messageHandler) {
        String key = getMapKey(url);
        ServerTransport serverTransport = serverTransportMap.get(key);
        if(serverTransport == null){
            synchronized (lock){
                serverTransport = serverTransportMap.get(key);
                if(serverTransport == null){
                    serverTransport = new NettyServerTransport(url,messageHandler);
                    serverTransportMap.put(key,serverTransport);
                }
            }
        }
        return serverTransport;
    }

    @Override
    public ClientTransport createClientTransport(URL url) {
        String key = getMapKey(url);
        ClientTransport clientTransport = clientTransportMap.get(key);
        if(clientTransport == null){
            synchronized (lock){
                clientTransport = clientTransportMap.get(key);
                if(clientTransport == null){
                    clientTransport = new NettyClientTransport(url);
                    clientTransportMap.put(key,clientTransport);
                }
            }
        }
        LoggerUtil.debug("Mapping {} => {}",url,clientTransport);
        return clientTransport;
    }

    private String getMapKey(URL url){
        String key = String.format("%s://%s:%d",url.getProtocol(),url.getHost(),url.getPort());
        return key;
    }
}
