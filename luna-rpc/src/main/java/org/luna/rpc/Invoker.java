package org.luna.rpc;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Invoker<T> {

    Class<T> getInterface();

    Result invoke(Invocation invocation);
}
