package org.luna.rpc.core.buildin;

import org.luna.rpc.core.Result;

/**
 * Created by luliru on 2016/11/5.
 */
public class DefaultRpcResult implements Result {

    private Object value;

    private Exception exception;

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public boolean hasException() {
        return exception != null;
    }
}
