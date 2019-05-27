package org.luna.rpc.provider;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.ApplicationConfig;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.RegistryConfig;
import org.luna.rpc.config.ServiceConfig;

/**
 * Created by luliru on 2016/12/30.
 */
public class ZookeeperRegisterProviderDemo2 {

    public static void main(String[] args){
        ApplicationConfig applicationConfig = new ApplicationConfig("luna-rpc-demo");

        ProtocolConfig lunaProtocol = new ProtocolConfig();
        lunaProtocol.setName("luna");
        lunaProtocol.setPort(7777);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setRegProtocol("zookeeper");
        registryConfig.setAddress("localhost:2181");

        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setGroup("luna-rpc-demo");
        serviceConfig.setServiceClass(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.setVersion("1.0");
        serviceConfig.addProtocol(lunaProtocol);
        serviceConfig.export();

        System.out.println("server start...");
    }

}
