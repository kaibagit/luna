package org.luna.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.buildin.DefaultRpcInvocation;

/**
 * Created by luliru on 2016/11/17.
 */
public class RefererInvocationHandler implements InvocationHandler {

    private Client<?> client;

    public RefererInvocationHandler(Client<?> client){
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        URL url = client.getUrl();
        DefaultRpcInvocation invocation = new DefaultRpcInvocation();
        invocation.setGroup(url.getGroup());
        invocation.setServiceName(url.getService());
        invocation.setVersion(url.getVersion());
        invocation.setMethodName(method.getName());
        invocation.setParameterTypes(method.getParameterTypes());
        invocation.setArguments(args);

        Result result = client.call(invocation);
        if(result.hasException()){
            throw result.getException();
        }

        return result.getValue();
    }

}
