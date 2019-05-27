package org.luna.rpc.registry;

import org.luna.rpc.core.URL;

import java.util.List;

/**
 * Created by kaiba on 2017/2/1.
 */
public interface NotifyListener {

    /**
     * provider节点变更通知
     * @param registryUrl 注册中心的URL
     * @param urls 新的provider的全量url
     */
    void notify(RegistryURL registryUrl, List<URL> urls);
}
