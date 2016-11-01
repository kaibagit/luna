package org.luna.rpc.proxy;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.DefaultRpcInvocation;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by luliru on 2016/10/14.
 */
public class RefererInvocationHandler<T> implements InvocationHandler {

    private Class<T> clz;

    private Client client;

    public RefererInvocationHandler(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new DefaultRpcInvocation();
        //TODO set invocation
        Result result = client.call(invocation);
        //TODO 异常处理
        return result.getValue();
    }
}
