package org.luna.rpc.transport.netty;

import org.luna.rpc.core.URL;
import org.luna.rpc.protocol.MessageHandler;
import org.luna.rpc.transport.ClientTransport;
import org.luna.rpc.transport.ServerTransport;
import org.luna.rpc.transport.TransportFactory;

/**
 * Created by luliru on 2016/11/7.
 */
public class NettyTransportFactory implements TransportFactory {

    private ServerTransport serverTransport;

    private ClientTransport clientTransport;

    @Override
    public ServerTransport createServerTransport(URL url, MessageHandler messageHandler) {
        return getSingtonServerTransport(url,messageHandler);
    }

    @Override
    public ClientTransport createClientTransport(URL url) {
        return getSingtonClientTransport(url);
    }

    private synchronized ServerTransport getSingtonServerTransport(URL url, MessageHandler messageHandler){
        if(serverTransport == null){
            NettyServerTransport serverTransport = new NettyServerTransport(url,messageHandler);
            this.serverTransport = serverTransport;
        }
        return serverTransport;
    }

    private synchronized ClientTransport getSingtonClientTransport(URL url){
        if(clientTransport == null){
            ClientTransport clientTransport = new NettyClientTransport(url);
            this.clientTransport = clientTransport;
        }
        return clientTransport;
    }
}
