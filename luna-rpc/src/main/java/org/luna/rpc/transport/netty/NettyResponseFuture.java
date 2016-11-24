package org.luna.rpc.transport.netty;

import org.luna.rpc.transport.Response;

/**
 * Created by luliru on 2016/11/19.
 */
public class NettyResponseFuture extends Response {

    private volatile boolean done = false;

    public NettyResponseFuture(long messageId) {
        super(messageId);
    }

    public Object getValue(){
        if(!done){
            try {
                synchronized (this){
                    this.wait();
                }
            } catch (InterruptedException e) {
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
        return super.getException();
    }

}
