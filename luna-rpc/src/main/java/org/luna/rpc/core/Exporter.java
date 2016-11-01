package org.luna.rpc.core;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Exporter<T> {

    Invoker<T> getInvoker();
}
