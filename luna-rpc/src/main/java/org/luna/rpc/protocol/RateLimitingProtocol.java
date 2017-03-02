package org.luna.rpc.protocol;

import com.google.common.util.concurrent.RateLimiter;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.*;
import org.luna.rpc.core.buildin.DefaultRpcResult;
import org.luna.rpc.core.exception.LunaRpcException;
import org.luna.rpc.core.exception.RateLimitingException;

/**
 * 支持服务端限速协议
 * Created by luliru on 2017/2/28.
 */
public class RateLimitingProtocol implements Protocol{

    private Protocol protocol;

    public RateLimitingProtocol(Protocol protocol){
        if(protocol == null){
            throw new LunaRpcException("protocol can't be null.");
        }
        this.protocol = protocol;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker, URL url) {
        Invoker<T> invokerToExpor = invoker;
        Double rateLimit = url.getDoubleParameter(URLParamType.rateLimit.getName());
        if(rateLimit != null){
            invokerToExpor = new RateLImitingInvokerr<>(invoker,rateLimit);
        }
        return protocol.export(invokerToExpor,url);
    }

    @Override
    public <T> Client<T> refer(Class<T> clz, URL url) {
        return protocol.refer(clz,url);
    }
}

class RateLImitingInvokerr<T> implements Invoker<T>{

    private Invoker<T> invoker;
    private RateLimiter limiter;

    RateLImitingInvokerr(Invoker<T> invoker,double rateLimit){
        this.invoker = invoker;
        this.limiter = RateLimiter.create(rateLimit);
    }

    @Override
    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    @Override
    public Result call(Invocation invocation) {
        boolean acquireSuccess = limiter.tryAcquire();
        if(!acquireSuccess){
            DefaultRpcResult result = new DefaultRpcResult();
            result.setException(new RateLimitingException("Invoke raached rate limit " + limiter.getRate() + " per secornd."));
            return result;
        }
        return invoker.call(invocation);
    }

    @Override
    public URL getUrl() {
        return invoker.getUrl();
    }
}
