package org.luna.rpc.consumer;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.MethodConfig;
import org.luna.rpc.config.ReferenceConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 负载均衡的客户端demo
 * Created by luliru on 2016/12/10.
 */
public class ConsumerLoadBalancingDemo {

    public static void main(String[] args){
        List<MethodConfig> methodConfigs = new ArrayList<>();
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("hello");
        methodConfigs.add(methodConfig);

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGroup("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setProtocol("luna");
        referenceConfig.setDirect("localhost:5555,localhost:7777");
        referenceConfig.setMethods(methodConfigs);

        DemoService demoService = referenceConfig.getRef();
        System.out.println(demoService.hello("luna"));
        System.out.println(demoService.hello("luna"));
        System.out.println(demoService.hello("luna"));
    }

}
