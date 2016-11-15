package org.luna.rpc.transport;

/**
 * Created by luliru on 2016/11/7.
 */
public interface MessageHandler {

    Object handle(Transport transport, Object message);

}
