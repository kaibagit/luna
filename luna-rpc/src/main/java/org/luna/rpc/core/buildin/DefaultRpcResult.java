package org.luna.rpc.core.buildin;

import org.luna.rpc.core.Result;

/**
 * Created by luliru on 2016/11/5.
 */
public class DefaultRpcResult implements Result {

    private Object value;

    private Throwable exception;

    @Override
    public Object getValue() {
        return null;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Throwable getException() {
        return null;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public boolean hasException() {
        return exception != null;
    }
}
