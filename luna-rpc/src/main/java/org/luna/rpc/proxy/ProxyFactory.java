package org.luna.rpc.proxy;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.exception.LunaRpcException;

/**
 * Created by luliru on 2016/10/14.
 */
public interface ProxyFactory {

    /**
     * create proxy.
     */
    <T> T getProxy(Class<T> clz,Client<T> client) throws LunaRpcException;
}
