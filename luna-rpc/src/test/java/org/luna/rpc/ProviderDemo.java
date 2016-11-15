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

        ProtocolConfig lunaProtocol = new ProtocolConfig();
        lunaProtocol.setName("luna");
        lunaProtocol.setPort(6666);

        ProtocolConfig dubboProtocol = new ProtocolConfig();
        dubboProtocol.setName("dubbo");
        dubboProtocol.setPort(8087);

        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setServiceClass(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.setVersion("1.0");
        serviceConfig.addProtocol(lunaProtocol);
//        serviceConfig.addProtocol(dubboProtocol);

        serviceConfig.export();

        System.out.println("server start...");
    }
}
