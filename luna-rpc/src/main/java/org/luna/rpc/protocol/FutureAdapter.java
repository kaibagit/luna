package org.luna.rpc.protocol;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.luna.rpc.transport.ResponseFuture;

/**
 * Created by luliru on 2016/12/5.
 */
public class FutureAdapter<V> implements Future<V> {

    public ResponseFuture responseFuture;

    public FutureAdapter(ResponseFuture responseFuture){
        this.responseFuture = responseFuture;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return responseFuture.isDone();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        Object result = responseFuture.get();
        if(result instanceof Exception){
            throw new ExecutionException("Remote servie throw a exception.",(Exception) result);
        }
        return (V) responseFuture.get();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Object result = responseFuture.get();
        if(result instanceof Exception){
            throw new ExecutionException("Remote servie throw a exception.",(Exception) result);
        }
        return (V) responseFuture.get();
    }
}
