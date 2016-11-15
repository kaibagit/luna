package org.luna.rpc.core;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Exporter<T> extends Lifecycle {

    Invoker<T> getInvoker();

    void unexport();
}
