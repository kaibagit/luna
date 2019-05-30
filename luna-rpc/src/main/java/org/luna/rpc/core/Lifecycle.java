package org.luna.rpc.core;

/**
 * Created by luliru on 2016/10/17.
 */
public interface Lifecycle {

    void start();

    void destroy();

    default boolean isAvailable(){
        return true;
    }
}
