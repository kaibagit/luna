package org.luna.rpc.transport;

import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.LunaRpcException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ResponseFuture
 * Created by luliru on 2016/12/7.
 */
public class ResponseFuture {

    // 客户端请求回调Map，key值为messageId
    private static ConcurrentMap<Long, ResponseFuture> futureMap = new ConcurrentHashMap<>();

    private volatile boolean done = false;

    private Request request;

    private volatile Response  response;

    private final Lock lock = new ReentrantLock();

    private long createTime = System.currentTimeMillis();

    /** 客户端请求超时时间，单位ms */
    private long timeout = 0;

    public static void received(Response response) {
        ResponseFuture future = futureMap.get(response.getMessageId());
        if(future != null){     //有可能是客户端已经timeout移除掉了
            future.doReceived(response);
        }
    }

    public ResponseFuture(Request request,long timeout){
        this.request = request;
        this.timeout = timeout;
        futureMap.put(request.getMessageId(),this);
    }

    public Object get(){
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
                    String msg = String.format("Invoke remote method timeout in %d ms. %s.%s application=%s,version=%s",System.currentTimeMillis() - createTime,invocation.getServiceName(),invocation.getMethodName(),invocation.getGroup(),invocation.getVersion());
                    throw new LunaRpcException(msg);
                }
            }
        }
        return response.getValue();
    }

    public boolean isDone(){
        return done;
    }

    private void doReceived(Response response) {
        this.response = response;
        done = true;
        synchronized (this){
            this.notifyAll();
        }
    }
}
