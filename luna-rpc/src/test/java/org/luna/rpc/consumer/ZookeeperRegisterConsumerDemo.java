package org.luna.rpc.consumer;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.ReferenceConfig;
import org.luna.rpc.config.RegistryConfig;

/**
 * Created by luliru on 2016/12/30.
 */
public class ZookeeperRegisterConsumerDemo {

    public static void main(String[] args){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegProtocol("zookeeper");
        registryConfig.setAddress("localhost:2181");

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGroup("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setProtocol("luna");
        referenceConfig.setRegistry(registryConfig);

        DemoService demoService = referenceConfig.getRef();

        System.out.println(demoService.hello("zookeeper"));
    }
}
