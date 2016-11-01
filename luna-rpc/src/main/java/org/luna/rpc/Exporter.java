package org.luna.rpc;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Exporter<T> {

    Invoker<T> getInvoker();
}
