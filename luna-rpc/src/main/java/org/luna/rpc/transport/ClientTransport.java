package org.luna.rpc.transport;

/**
 * Created by luliru on 2016/11/12.
 */
public interface ClientTransport extends Transport {

    ResponseFuture send(Request request);
}
