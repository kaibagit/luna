package org.luna.rpc.rpc;

/**
 * Created by luliru on 2016/10/17.
 */
public interface Client {

    public RpcResult call(RpcInvocation invocation);

}
