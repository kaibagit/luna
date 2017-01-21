package org.luna.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.luna.rpc.core.URL;
import org.luna.rpc.core.extension.Spi;
import org.luna.rpc.registry.Registry;
import org.luna.rpc.registry.RegistryFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Zookeeper注册中心工厂
 * Created by luliru on 2016/12/30.
 */
@Spi(name = "zookeeper")
public class ZookeeperRegistryFactory implements RegistryFactory{

    private static ConcurrentHashMap<String, Registry> registries = new ConcurrentHashMap<String, Registry>();

    private static final Object lock = new Object();

    @Override
    public Registry getRegistry(URL url) {
        String regisryUrl = String.format("%s://%s:%d",url.getProtocol(),url.getHost(),url.getPort());
        Registry registry = registries.get(regisryUrl);
        if(registry == null){
            synchronized (lock){
                registry =  registries.get(regisryUrl);
                if(registry == null){
                    String connectionString = String.format("%s:%d",url.getHost(),url.getPort());
                    ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    CuratorFramework client = CuratorFrameworkFactory.builder().connectString(connectionString)
                            .retryPolicy(retryPolicy)
                            .canBeReadOnly(false)
                            .connectionTimeoutMs(30000)
                            .sessionTimeoutMs(30000)
                            .build();

                    ZookeeperRegistry zkRegistry = new ZookeeperRegistry(url,client);
                    zkRegistry.start();
                    registries.put(regisryUrl,zkRegistry);

                    registry = zkRegistry;
                }
            }
        }
        return registry;
    }
}
