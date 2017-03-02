package org.luna.rpc.core;

/**
 * Created by kaiba on 2016/11/1.
 */
public interface Caller<T> {

    public Result call(Invocation invocation);

    URL getUrl();
}
