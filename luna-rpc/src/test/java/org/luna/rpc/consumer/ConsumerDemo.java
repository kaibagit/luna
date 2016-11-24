package org.luna.rpc.consumer;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.ReferenceConfig;

/**
 * Created by luliru on 2016/11/17.
 */
public class ConsumerDemo {

    public static void main(String[] args){
        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setDirectUrl("localhost:6666");

        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("luna");
        protocol.setHost("localhost");
        protocol.setPort(6666);

        referenceConfig.setProtocol(protocol);

        DemoService demoService = referenceConfig.getRef();
        System.out.println(demoService.getById(1));
        System.out.println(demoService.hello("luna"));
        demoService.hello();


    }

}
