package org.luna.rpc.core;

/**
 * Created by luliru on 2016/11/1.
 */
public interface Invoker<T> extends Caller<T>{

    Class<T> getInterface();

    URL getUrl();
}
