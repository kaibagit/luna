package org.luna.rpc.core.exception;

/**
 * Created by kaiba on 2016/5/29.
 */
public class LunaRpcException extends RuntimeException {

    public LunaRpcException() {
        super();
    }

    public LunaRpcException(String message){
        super(message);
    }

    public LunaRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
