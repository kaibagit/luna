package org.luna.rpc.filter.buildin;

import org.luna.rpc.core.Caller;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.exception.DegradationException;
import org.luna.rpc.core.exception.RateLimitingException;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.filter.Filter;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;

/**
 * 降级过滤器，主要用于服务端延迟、错误的熔断降级
 * Created by luliru on 2017/3/20.
 */
@Spi
public class DegradationFilter implements Filter{

    @Override
    public Result filter(Caller<?> caller, Invocation invocation) {
        Result result = null;
        result = new FuseCommand(caller,invocation).execute();
//        try{
//            result = new FuseCommand(caller,invocation).execute();
//        }catch (HystrixRuntimeException e){
//            if(e.getFailureType() == HystrixRuntimeException.FailureType.SHORTCIRCUIT){
//                throw new DegradationException();
//            }else{
//                throw (RuntimeException)e.getCause();
//            }
//        }
        return result;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean filterInvoker() {
        return false;
    }

    @Override
    public boolean filterClient() {
        return true;
    }
}
class FuseCommand extends HystrixCommand<Result>{

    private Caller<?> caller;
    private Invocation invocation;

    public FuseCommand(Caller<?> caller,Invocation invocation){
        super(Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey(String.format("%s#%s#%s",invocation.getGroup(),invocation.getServiceName(),invocation.getVersion())))
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s.%s", invocation.getServiceName(),invocation.getMethodName()))
                )
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(3)//10秒钟内至少3此请求失败，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(10000)//熔断器中断请求10秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(30)//错误率达到30开启熔断保护
                        .withExecutionTimeoutEnabled(false)
                )
        );
        this.caller = caller;
        this.invocation = invocation;
    }

    @Override
    protected Result run() throws Exception {
        Result result = caller.call(invocation);
        if(result.hasException()){
            Exception exception = result.getException();
            //provider端返回限流异常，需要触发熔断逻辑
            if(exception instanceof RateLimitingException){
                throw exception;
            }
        }
        return result;
    }

    @Override
    public Result getFallback() {
        //如果已经熔断，则抛出DegradationException；否则抛出原先的Exception
        final Exception e = isCircuitBreakerOpen() ? new DegradationException() : (Exception) getExecutionException();
        Result result = new Result() {

            @Override
            public Object getValue() {
                return e;
            }

            @Override
            public Exception getException() {
                return e;
            }

            @Override
            public boolean hasException() {
                return true;
            }
        };
        return result;
    }
}
