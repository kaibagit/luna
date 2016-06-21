package org.luna.rpc.transport;

/**
 * Created by kaiba on 2016/5/29.
 */
public class RpcResult {

    private Object result;

    private Throwable exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
