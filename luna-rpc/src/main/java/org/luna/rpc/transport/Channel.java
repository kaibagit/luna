package org.luna.rpc.transport;

import java.net.SocketAddress;

/**
 * 对一个Connection的抽象
 * Created by luliru on 2016/11/7.
 */
public interface Channel {

    SocketAddress remoteAddress();

}
