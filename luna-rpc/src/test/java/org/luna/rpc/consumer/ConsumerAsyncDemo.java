package org.luna.rpc.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.MethodConfig;
import org.luna.rpc.config.ProtocolConfig;
import org.luna.rpc.config.ReferenceConfig;
import org.luna.rpc.core.RpcContext;

/**
 * Created by luliru on 2016/12/8.
 */
public class ConsumerAsyncDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("luna");
        protocol.setHost("localhost");
        protocol.setPort(6666);

        List<MethodConfig> methodConfigs = new ArrayList<>();
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("hello");
        methodConfig.setAsync(true);
        methodConfigs.add(methodConfig);

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGroup("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setUrls("localhost:6666");
        referenceConfig.setProtocol("luna");
        referenceConfig.setMethods(methodConfigs);

        DemoService demoService = referenceConfig.getRef();
        System.out.println(demoService.hello("luna"));
        Future<String> future = RpcContext.getContext().getFuture();
        String result = future.get();
        System.out.println(result);
    }
}
