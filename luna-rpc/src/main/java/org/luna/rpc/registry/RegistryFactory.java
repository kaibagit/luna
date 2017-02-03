package org.luna.rpc.registry;

import org.luna.rpc.core.URL;

/**
 * 注册中心工厂
 * Created by luliru on 2016/12/30.
 */
public interface RegistryFactory {

    Registry getRegistry(RegistryURL url);

}
