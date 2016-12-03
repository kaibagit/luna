package org.luna.rpc.core;

import java.util.concurrent.Future;

/**
 * RPC调用上下文
 * Created by luliru on 2016/12/3.
 */
public class RpcContext {

    private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    /** 异步调用时的future */
    private Future<?> future;

    protected RpcContext() {
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }

    public Future<?> getFuture() {
        return future;
    }
}
