package org.luna.rpc;

import org.luna.rpc.config.ApplicationConfig;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.ServiceConfig;

/**
 * Created by kaiba on 2016/11/2.
 */
public class ProviderDemo {

    public static void main(String[] args){
        ApplicationConfig applicationConfig = new ApplicationConfig("luna-rpc-demo");

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("luna");
        protocolConfig.setPort(6666);

        ProtocolConfig protocolConfig2 = new ProtocolConfig();
        protocolConfig2.setName("dubbo");
        protocolConfig2.setPort(8087);

        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setServiceClass(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.setVersion("1.0");
        serviceConfig.addProtocol(protocolConfig);

        serviceConfig.export();

        System.out.println("server start...");
    }
}
