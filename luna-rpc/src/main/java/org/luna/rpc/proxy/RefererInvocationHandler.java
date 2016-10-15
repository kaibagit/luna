package org.luna.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by luliru on 2016/10/14.
 */
public class RefererInvocationHandler<T> implements InvocationHandler {

    private Class<T> clz;

    private Object client;

    public RefererInvocationHandler(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}
