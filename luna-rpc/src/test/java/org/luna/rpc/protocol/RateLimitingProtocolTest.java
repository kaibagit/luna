package org.luna.rpc.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.luna.rpc.api.DemoService;
import org.luna.rpc.config.*;
import org.luna.rpc.core.exception.RateLimitingException;
import org.luna.rpc.provider.DemoServiceImpl;

/**
 * Created by luliru on 2017/2/28.
 */
public class RateLimitingProtocolTest {

    public static void main(String[] args){
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
        serviceConfig.setRateLimit(1D);     //每秒一次

        serviceConfig.export();

        System.out.println("server start...");
    }

    @Test
    public void test(){

    }

    @Test
    public void testConsumer() throws InterruptedException {
        List<MethodConfig> methodConfigs = new ArrayList<>();
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("hello");
        methodConfig.setRequestTimeout(5000);
        methodConfigs.add(methodConfig);

        ReferenceConfig<DemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setGroup("luna-rpc-demo");
        referenceConfig.setServiceClass(DemoService.class);
        referenceConfig.setVersion("1.0");
        referenceConfig.setProtocol("luna");
        referenceConfig.setDirect("localhost:6666");
        referenceConfig.setMethods(methodConfigs);

        DemoService demoService = referenceConfig.getRef();
        long begin = System.currentTimeMillis();
        for(int i=0;i<10;i++){
            try{
                demoService.hello();
            }catch (RateLimitingException e){
                e.printStackTrace();
                Thread.sleep(1000L);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }
}
