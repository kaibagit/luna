package org.luna.rpc;

import org.luna.rpc.config.ServiceConfig;

/**
 * Created by kaiba on 2016/11/2.
 */
public class ProviderDemo {

    public static void main(String[] args){
        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<DemoService>();

        System.out.println("server start...");
    }
}
