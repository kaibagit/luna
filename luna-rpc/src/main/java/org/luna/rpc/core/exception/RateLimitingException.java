package org.luna.rpc.core.exception;

/**
 * 服务限流异常
 * Created by luliru on 2017/2/23.
 */
public class RateLimitingException extends LunaRpcException {

    public RateLimitingException(){
        super();
    }

    public RateLimitingException(String message){
        super(message);
    }
}
