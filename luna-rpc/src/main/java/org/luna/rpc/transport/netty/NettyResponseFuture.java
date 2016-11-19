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
        if(done){
            
        }
        return super.getValue();
    }

    public Exception getException(){
        return super.getException();
    }

}
