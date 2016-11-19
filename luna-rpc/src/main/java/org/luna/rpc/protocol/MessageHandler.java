package org.luna.rpc.protocol;

import org.luna.rpc.transport.Transport;

/**
 * Created by luliru on 2016/11/7.
 */
public interface MessageHandler {

    Object handle(Transport transport, Object message);

}
