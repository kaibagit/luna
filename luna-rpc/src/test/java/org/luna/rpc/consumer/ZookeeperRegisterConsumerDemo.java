package org.luna.rpc.consumer;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.ReferenceConfig;
import org.luna.rpc.config.RegistryConfig;

/**
 * Created by luliru on 2016/12/30.
 */
public class ZookeeperRegisterConsumerDemo {

    public static void main(String[] args) {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegProtocol("zookeeper");
        registryConfig.setAddress("localhost:2181");

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGroup("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setProtocol("luna");
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setRetries(1);
        referenceConfig.setLoadBalance("weightLoadBalance");

        DemoService demoService = referenceConfig.getRef();
        int i = 0;
        while(true){
            try {
                demoService.hello(String.valueOf(i++));
                Thread.sleep(1000L);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
