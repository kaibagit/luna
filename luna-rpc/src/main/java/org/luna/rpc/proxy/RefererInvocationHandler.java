package org.luna.rpc.proxy;

import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.buildin.DefaultRpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        Class<?>[] argClasses = new Class[args.length];
        for(int i=0; i< args.length; i++){
            argClasses[i] = args[i].getClass();
        }

        URL url = client.getUrl();
        DefaultRpcInvocation invocation = new DefaultRpcInvocation();
        invocation.setApplication(url.getApplication());
        invocation.setServiceName(url.getService());
        invocation.setVersion(url.getVersion());
        invocation.setMethodName(method.getName());
        invocation.setParameterTypes(argClasses);
        invocation.setArguments(args);

        return client.call(invocation);
    }

}
