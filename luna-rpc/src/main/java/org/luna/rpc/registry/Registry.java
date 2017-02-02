package org.luna.rpc.registry;

import org.luna.rpc.core.URL;

/**
 * 注册中心
 * Created by luliru on 2016/12/30.
 */
public interface Registry {

    /**
     * 注册Service到注册中心
     * @param url
     */
    void register(URL url);

    /**
     * 订阅
     * @param url
     * @param listener
     */
    void subscribe(URL url, NotifyListener listener);
}
