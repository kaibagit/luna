package org.luna.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.luna.rpc.rpc.Client;
import org.luna.rpc.rpc.RpcInvocation;
import org.luna.rpc.rpc.RpcResult;

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
        RpcInvocation invocation = new RpcInvocation();
        //TODO set invocation
        RpcResult result = client.call(invocation);
        //TODO 异常处理
        return result.getValue();
    }
}
