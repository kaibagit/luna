package org.luna.rpc.core.buildin;

import java.lang.reflect.Method;

import org.luna.rpc.core.*;
import org.luna.rpc.util.LoggerUtil;

/**
 * Created by luliru on 2016/11/5.
 */
public class DefaultInvoker<T> implements Invoker<T> {

    private T proxyImpl;

    private URL url;

    private Class<T> clz;

    public DefaultInvoker(T proxyImpl, URL url, Class<T> clz){
        this.proxyImpl = proxyImpl;
        this.url = url;
        this.clz = clz;
    }

    @Override
    public Class getInterface() {
        return clz;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public Result call(Invocation invocation) {
        DefaultRpcResult result = new DefaultRpcResult();
        Method method = null;
        try{
            method = findMethod(invocation);
        }catch (NoSuchMethodException e){
            LunaRpcException exception = new LunaRpcException("Service method not exists: "+clz.getName()+"."+invocation.getMethodName());
            result.setException(exception);
            return result;
        }
        try{
            Object value = method.invoke(proxyImpl,invocation.getArguments());
            result.setValue(value);
        } catch (Throwable e) {
            if (e.getCause() != null) {
                LoggerUtil.error("Exception caught when method invoke: " + e.getCause());
                result.setException(new LunaRpcException("provider call process error", e.getCause()));
            } else {
                result.setException(new LunaRpcException("provider call process error", e));
            }
        }
        return result;
    }

    private Method findMethod(Invocation invocation) throws NoSuchMethodException {
        return clz.getMethod(invocation.getMethodName(),invocation.getParameterTypes());
    }
}
