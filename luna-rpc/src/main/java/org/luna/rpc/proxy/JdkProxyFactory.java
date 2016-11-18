package org.luna.rpc.proxy;

import org.luna.rpc.core.*;
import org.luna.rpc.core.extension.Spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by kaiba on 2016/5/24.
 */
@Spi(name="jdk")
public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clz, Client<T> client) throws LunaRpcException {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {clz}, new RefererInvocationHandler(client));
    }
}
