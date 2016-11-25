package org.luna.rpc.provider;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.api.MemberService;
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

        ServiceConfig<MemberService> serviceConfig2 = new ServiceConfig<MemberService>();
        serviceConfig2.setApplication(applicationConfig);
        serviceConfig2.setServiceClass(MemberService.class);
        serviceConfig2.setRef(new MemberServiceImpl());
        serviceConfig2.setVersion("1.0");
        serviceConfig2.addProtocol(lunaProtocol);

        serviceConfig.export();
        serviceConfig2.export();

        System.out.println("server start...");
    }
}
