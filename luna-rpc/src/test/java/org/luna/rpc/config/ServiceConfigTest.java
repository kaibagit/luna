package org.luna.rpc.config;

import org.junit.Test;
import org.luna.rpc.api.DemoService;
import org.luna.rpc.provider.DemoServiceImpl;

/**
 * Created by luliru on 2017/2/21.
 */
public class ServiceConfigTest {

    @Test
    public void testParams(){
        ApplicationConfig applicationConfig = new ApplicationConfig("luna-rpc-demo");

        ProtocolConfig lunaProtocol = new ProtocolConfig();
        lunaProtocol.setName("luna");
        lunaProtocol.setPort(6666);

        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();
        serviceConfig.setApplication(applicationConfig);
        serviceConfig.setGroup("luna-rpc-demo");
        serviceConfig.setServiceClass(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.setVersion("1.0");
        serviceConfig.addProtocol(lunaProtocol);
        serviceConfig.setWorkerThread(16);

        serviceConfig.export();

        System.out.println("server start...");
    }
}
