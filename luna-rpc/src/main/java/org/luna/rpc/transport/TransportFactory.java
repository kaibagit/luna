package org.luna.rpc.transport;

import org.luna.rpc.core.URL;

/**
 * Created by luliru on 2016/11/7.
 */
public interface TransportFactory {

    ServerTransport createServerTransport(URL url, MessageHandler messageHandler);

    ClientTransport createClientTransport(URL url);
}
