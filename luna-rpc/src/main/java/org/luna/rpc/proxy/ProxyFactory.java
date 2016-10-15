package org.luna.rpc.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * Created by luliru on 2016/10/14.
 */
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler);
}
