package org.luna.rpc.core;

/**
 * Created by kaiba on 2016/11/1.
 */
public interface Client<T> extends Caller<T>,Lifecycle {
    URL getUrl();
}
