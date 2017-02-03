package org.luna.rpc.registry;

import org.luna.rpc.core.URL;

/**
 * 注册中心URL
 * Created by luliru on 2017/2/3.
 */
public class RegistryURL extends URL {

    public RegistryURL(String protocol, String host, int port) {
        super(protocol, host, port, null, null, null);
    }

    public String toString(){
        return String.format("%s://%s:%d",protocol,host,port);
    }
}
