package org.luna.rpc.registry;

import org.luna.rpc.core.URL;

import java.util.List;

/**
 * Created by kaiba on 2017/2/1.
 */
public interface NotifyListener {

    void notify(URL registryUrl, List<URL> urls);
}
