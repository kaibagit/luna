package org.luna.rpc;

import org.luna.rpc.provider.Provider;

/**
 * Created by kaiba on 2016/5/24.
 */
public class Server {

    public static void main(String[] args){

        Provider provider = new Provider(8080);
        provider.registrer(DemoService.class,new DemoServiceImpl());

        provider.start();
    }
}
