package org.luna.rpc.transport.netty;

import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.LunaRpcException;
import org.luna.rpc.transport.Request;
import org.luna.rpc.transport.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by luliru on 2016/11/19.
 */
public class NettyResponseFuture extends Response implements Future {

    private volatile boolean done = false;

    private long createTime = System.currentTimeMillis();

    /** 客户端请求超时时间，单位ms */
    private long timeout = 0;

    private Request request;

    public NettyResponseFuture(Request request, long timeout) {
        super(request.getMessageId());
        this.request = request;
        this.timeout = timeout;
    }

    public Object getValue(){
        if(!done){
            try {
                synchronized (this){
                    this.wait(timeout);
                }
            } catch (InterruptedException e) {}
            if(!done){
                Object data = request.getData();
                if(data instanceof Invocation){
                    Invocation invocation = (Invocation)data;
                    String msg = String.format("Invoke remote method timeout in %d ms. %s.%s application=%s,version=%s",System.currentTimeMillis() - createTime,invocation.getServiceName(),invocation.getMethodName(),invocation.getApplication(),invocation.getVersion());
                    throw new LunaRpcException(msg);
                }
            }
        }
        return super.getValue();
    }

    public void complete(Response response){
        setValue(response.getValue());
        setException(response.getException());
        done = true;
        synchronized (this){
            this.notifyAll();
        }
    }

    public Exception getException(){
        if(!done){
            try {
                synchronized (this){
                    this.wait();
                }
            } catch (InterruptedException e) {
            }
        }
        return super.getException();
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
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
