package org.luna.rpc.transport;

import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.exception.LunaRpcException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ResponseFuture
 * Created by luliru on 2016/12/7.
 */
public class ResponseFuture {

    private static ConcurrentMap<Long/** messageId */, ResponseFuture> futureMap = new ConcurrentHashMap<>();

    private Request request;

    private volatile Response  response;

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
        if(!isDone()){
            try {
                synchronized (this){
                    this.wait(timeout);
                }
            } catch (InterruptedException e) {}
        }
        futureMap.remove(request.getMessageId());
        if(!isDone()){
            Object data = request.getData();
            String msg = null;
            if(data != null && data instanceof Invocation){
                Invocation invocation = (Invocation)data;
                msg = String.format("Invoke remote method timeout in %d ms. %s.%s application=%s,version=%s",System.currentTimeMillis() - createTime,invocation.getServiceName(),invocation.getMethodName(),invocation.getGroup(),invocation.getVersion());
            }else{
                msg = String.format("Invoke remote method timeout in %d ms. %s.%s application=%s,version=%s",System.currentTimeMillis() - createTime);
            }
            throw new LunaRpcException(msg);
        }
        return response.getValue();
    }

    public boolean isDone(){
        return response != null;
    }

    private void doReceived(Response response) {
        this.response = response;
        synchronized (this){
            this.notifyAll();
        }
    }
}
